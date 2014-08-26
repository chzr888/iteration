package com.chenzr.iteration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chenzr.iteration.bean.StepSetOperation;
import com.chenzr.iteration.bean.TableField;
import com.chenzr.iteration.dialect.Dialect;
import com.chenzr.iteration.impl.IterationContextImpl;
import com.chenzr.iteration.impl.IterationEngineImpl;
import com.chenzr.iteration.impl.UnitOperationImpl;
import com.chenzr.iteration.utils.SqlUtil;


public class StepOperation {

	private Logger logger = LoggerFactory.getLogger(StepOperation.class);
	private SqlUtil util = new SqlUtil();
	// 每页大小
	private int chunkSize = 10000;

	/**
	 * 运行
	 * 
	 * @param tableName
	 *            表名
	 * @param query
	 *            数据集SQL
	 * @param fields
	 *            字段信息
	 * @param operations
	 *            栏目设置
	 * @param memConn
	 *            内存数据库连接
	 * @param bizConn
	 *            业务数据库连接
	 * @param isMemoryCompute
	 *            是否在内存计算
	 * @throws Exception
	 */
	public String execute(String tableName, String query,
			List<TableField> fields, List<StepSetOperation> operations,
			Connection memConn, Connection bizConn, boolean isMemoryCompute)
			throws Exception {
		String msg = "";
		Connection memoryConn = null;
		long startTime = 0;
		try {
			memoryConn = memConn;
			if (isMemoryCompute) {
				if (null == memoryConn || memoryConn.isClosed()) {
					memoryConn = util.getMemoryConnection();
				}

				// 实例化内存表
				startTime = System.currentTimeMillis();
				System.out.println("开始创造内存表!");
				createMemoryTable(tableName, fields, memoryConn);
				System.out.println("内存表创造完成! 消耗时间："
						+ (System.currentTimeMillis() - startTime + " 毫秒"));

				// 导入数据
				startTime = System.currentTimeMillis();
				System.out.println("开始导入数据到内存数据库!");
				importData(tableName, query, fields, bizConn, memoryConn);
				System.out.println("导入数据完成! 消耗时间："
						+ (System.currentTimeMillis() - startTime + " 毫秒"));
			}
			// 计算
			startTime = System.currentTimeMillis();
			System.out.println("开始计算!");
			if (isMemoryCompute) {
				compute(tableName, fields, operations, memoryConn);
			} else {
				compute(tableName, fields, operations, bizConn);
			}
			System.out.println("计算完成! 消耗时间："
					+ (System.currentTimeMillis() - startTime + " 毫秒"));

			// 批量更新数据集数据库
			startTime = System.currentTimeMillis();
			System.out.println("开始批量更新数据集数据!");
			if (isMemoryCompute) {
				batchUpdateData(tableName, fields, bizConn, memoryConn);
			}
			System.out.println("批量更新数据集数据完成! 消耗时间："
					+ (System.currentTimeMillis() - startTime + " 毫秒"));

		} catch (Exception e) {
			logger.error("{}", e);
			throw e;
		} finally {
			util.dropMemoryTable(tableName, memConn);
		}
		return msg;
	}

	private String compute(String tableName, List<TableField> fields,
			List<StepSetOperation> operations, Connection memConn)
			throws Exception {
		long startTime = 0;
		// 栏目设置整理
		List<List<StepSetOperation>> lists = new ArrayList<List<StepSetOperation>>();
		// 共多少行
		int totalRowNum = 0;
		// 共多少页
		int totalPage = 0;
		// 字段
		Map<String, String> fieldsMap = new HashMap<String, String>();
		try {
			for (int i = 0; i < fields.size(); i++) {
				TableField field = fields.get(i);
				fieldsMap.put(field.getFieldName(), field.getFieldCaption());
			}
			String operateMode = "";
			List<StepSetOperation> list = null;
			for (int i = 0; i < operations.size(); i++) {
				StepSetOperation bean = operations.get(i);
				String opMode = bean.getOperateMode();
				if (!operateMode.equalsIgnoreCase(opMode)) {
					list = new ArrayList<StepSetOperation>();
					lists.add(list);
				}
				list.add(bean);
				operateMode = opMode;
			}
			Dialect dialect = Dialect.getInstance(memConn);
			String query = "SELECT * FROM " + tableName;
			totalRowNum = dialect.getMaxRowNum(query, memConn);
			totalPage = dialect.totalPage(totalRowNum, chunkSize);
			// 迭代执行
			for (int i = 0; i < lists.size(); i++) {
				List<StepSetOperation> listSSO = lists.get(i);
				StepSetOperation bean = listSSO.get(0);
				// 后续处理分片计算
				if ("行运算".equalsIgnoreCase(bean.getOperateMode())) {
					System.out.println("开始行运算");
					for (int j = 0; j < totalPage; j++) {
						startTime = System.currentTimeMillis();
						String sql = dialect.getLimitString(query, j
								* chunkSize, chunkSize);
						int count = rowIteration(listSSO, fieldsMap, tableName,
								sql, memConn);
						System.out
								.println("运算行数："
										+ count
										+ " 消耗时间："
										+ (System.currentTimeMillis()
												- startTime + " 毫秒"));
					}
					System.out.println("行运算完成");
				}
				// 列运算不可以分片计算
				if ("列运算".equalsIgnoreCase(bean.getOperateMode())) {
					System.out.println("开始列运算");
					startTime = System.currentTimeMillis();
					colIteration(listSSO, fieldsMap, tableName, memConn);
					System.out.println("列运算完成! 消耗时间："
							+ (System.currentTimeMillis() - startTime + " 毫秒"));
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return "栏目运算完成";
	}

	/**
	 * 行迭代
	 * 
	 * @param categorySet
	 *            栏目设置
	 * @param fields
	 *            字段信息
	 * @param tableName
	 *            表名
	 * @param sql
	 *            分片查询
	 * @param conn
	 *            连接
	 * @throws Exception
	 */
	private int rowIteration(List<StepSetOperation> categorySet,
			Map<String, String> fields, String tableName, String sql,
			Connection memConn) throws Exception {
		ResultSet rs = null;
		int upRowNum = 0;
		long time = System.currentTimeMillis();
		try {
			// 修改后的数据
			Map<String, Map<String, Object>> afterRowListMap = new LinkedHashMap<String, Map<String, Object>>();
			rs = util.getResultSetBySql(memConn, sql);
			try {
				IterationEngine engine = IterationEngineImpl.getInstance();
				UnitOperation unitOperation = new UnitOperationImpl();
				IterationContext ctx = new IterationContextImpl();
				// 更新id
				String id = "";
				while (rs.next()) {
//					time = System.currentTimeMillis();
					// 当前行数据
					Map<String, Object> _row = new HashMap<String, Object>();
					id = rs.getString("id");

					for (Entry<String, String> stepOperation : fields
							.entrySet()) {
						String key = stepOperation.getValue();
						Object value = rs.getObject(stepOperation.getKey());
						_row.put(key, value);
					}
					ctx.clear();
					ctx.set(_row);
					
					// 迭代
					for (int i = 0; i < categorySet.size(); i++) {

						StepSetOperation stepSetOperation = categorySet.get(i);
						String columnName = stepSetOperation.getColumnName();
						// 更新条件
						String updateCriteria = stepSetOperation
								.getUpdateCriteria();
						boolean isUpdate = true;
						if (stepSetOperation.isUpdate()) {
							isUpdate = false;
							Object object = unitOperation.operation(engine,
									updateCriteria, ctx);
							if (object instanceof Boolean) {
								isUpdate = (Boolean) object;
							}
						}
						if (isUpdate) {
							// 修改后的数据
							String formula = stepSetOperation.getFormula();
							Object value = unitOperation.operation(engine,
									formula, ctx);
							ctx.set(columnName, value);
							_row.put(columnName, value);
						}
					}
					afterRowListMap.put(id, _row);
//					System.out.println(System.currentTimeMillis() - time);
				}
				System.out.println(System.currentTimeMillis() - time);
				util.close(rs);
			} catch (Exception e) {
				throw e;
			}
			time = System.currentTimeMillis();
			upRowNum = batchRowUpdate(categorySet, afterRowListMap, tableName,
					memConn);
			System.out.println(System.currentTimeMillis() - time);
		} catch (Exception e) {
			logger.error("{}", e);
			throw e;
		} finally {
			util.close(rs);
		}
		return upRowNum;
	}

	/**
	 * 查询栏目设置是否有更新条件
	 * 
	 * @param categorySet
	 *            栏目设置
	 * @return true?false
	 */
	private boolean getCategorySetIsUpdateCriteria(StepSetOperation categorySet) {
		if (null == categorySet || "".equals(categorySet)) {
			return false;
		}
		if (null != categorySet.getUpdateCriteria()) {
			if(!"".equalsIgnoreCase(categorySet.getUpdateCriteria().trim())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 行数据批量更新
	 * 
	 * @param categorySet
	 *            更新栏目
	 * @param afterRowListMap
	 *            修改后的数据
	 * @param tableName
	 *            表名
	 * @param bizConn
	 *            连接
	 * @throws Exception
	 */
	private int batchRowUpdate(List<StepSetOperation> categorySet,
			Map<String, Map<String, Object>> afterRowListMap, String tableName,
			Connection memConn) throws Exception {
		PreparedStatement pst = null;
		int batchSize = 0;
		try {
			memConn.setAutoCommit(false);
			// 批量更新
			StringBuffer sbSet = new StringBuffer();
			for (int i = 0; i < categorySet.size(); i++) {
				StepSetOperation bean = categorySet.get(i);
				if (sbSet.length() > 0) {
					sbSet.append(",");
				}
				sbSet.append(bean.getColumnId());
				sbSet.append("=?");
			}
			StringBuffer upDateStr = new StringBuffer();
			upDateStr.append("UPDATE ");
			upDateStr.append(tableName);
			upDateStr.append(" SET ");
			upDateStr.append(sbSet);
			upDateStr.append(" WHERE ");
			upDateStr.append(" id=? ");
			String upSql = upDateStr.toString();
			pst = memConn
					.prepareStatement(upSql, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			for (Entry<String, Map<String, Object>> entry : afterRowListMap
					.entrySet()) {
				String rowid = entry.getKey();
				Map<String, Object> rowMap = entry.getValue();
				rowUpdate(categorySet, rowMap, rowid, pst);
			}
			int[] batch = pst.executeBatch();
			memConn.commit();
			batchSize = batch.length;
		} catch (Exception e) {
			memConn.rollback();
			logger.error("更新失败,回滚");
			throw e;
		} finally {
			util.close(pst);
		}
		return batchSize;
	}

	/**
	 * 行更新
	 * 
	 * @param categorySet
	 *            更新栏目
	 * @param rowMap
	 *            当前行更新数据
	 * @param rowid
	 *            更新条件
	 * @throws Exception
	 */
	private void rowUpdate(List<StepSetOperation> categorySet,
			Map<String, Object> rowMap, String rowid, PreparedStatement pst)
			throws Exception {
		try {
			int k = 0;
			for (int i = 0; i < categorySet.size(); i++) {
				StepSetOperation bean = categorySet.get(i);
				String key = bean.getColumnName();
				Object value = rowMap.get(key);
				pst.setObject(i + 1, value);
				k++;
			}
			pst.setString(k + 1, rowid);
			pst.addBatch();
		} catch (Exception e) {
			throw e;
		}
	}

//	/**
//	 * 单个单元格更新
//	 * 
//	 * @param categorySet
//	 *            更新栏目
//	 * @param afterRowMap
//	 *            修改后数据
//	 * @param beforeRowMap
//	 *            修改前数据
//	 * @param tableName
//	 *            表名
//	 * @param id
//	 *            更新ID
//	 * @param bizConn
//	 *            连接
//	 * @throws Exception
//	 */
//	private int unitUpdate(StepSetOperation stepSetOperation,
//			Map<String, Object> afterRowMap, Map<String, Object> beforeRowMap,
//			String tableName, String id, Connection memConn) throws Exception {
//		PreparedStatement pst = null;
//		int count = 0;
//		try {
//			memConn.setAutoCommit(false);
//			// 修改后数据
//			Map<String, Object> afterRow = afterRowMap;
//			// 更新前数据
//			Map<String, Object> beforeRow = beforeRowMap;
//			StepSetOperation bean = stepSetOperation;
//			String keyName = bean.getColumnName();
//			// 更新字段
//			String key = bean.getColumnId();
//			// 更新条件
//			String whereStr = bean.getUpdateCriteria();
//
//			StringBuffer upDateStr = new StringBuffer();
//			upDateStr.append("UPDATE ");
//			upDateStr.append(tableName);
//			upDateStr.append(" SET ");
//			upDateStr.append(key);
//			upDateStr.append("=? ");
//			upDateStr.append(" WHERE ");
//			upDateStr.append(" id=? ");
//			if (null != whereStr && !"".equals(whereStr)) {
//				for (Entry<String, Object> data : beforeRow.entrySet()) {
//					String cnName = data.getKey();
//					Object object = data.getValue();
//					if (null != object) {
//						String value = "";
//						boolean isReplace = false;
//						if (object instanceof String) {
//							isReplace = true;
//							value = (String) object;
//						}
//						if (object instanceof Integer) {
//							isReplace = true;
//							int str = (Integer) object;
//							value = str + "";
//						}
//						if (object instanceof Double) {
//							isReplace = true;
//							double str = (Double) object;
//							value = str + "";
//						}
//						if (object instanceof BigDecimal) {
//							isReplace = true;
//							BigDecimal str = (BigDecimal) object;
//							value = str.toString() + "";
//						}
//						if (object instanceof Float) {
//							isReplace = true;
//							Float str = (Float) object;
//							value = str.toString() + "";
//						}
//						if (isReplace) {
//							whereStr = whereStr.replaceAll(cnName, value);
//						}
//					}
//				}
//				if (null != whereStr && !"".equals(whereStr)) {
//					upDateStr.append(" AND ");
//					upDateStr.append(whereStr);
//				}
//			}
//			String upSql = upDateStr.toString();
//			pst = memConn
//					.prepareStatement(upSql, ResultSet.TYPE_SCROLL_SENSITIVE,
//							ResultSet.CONCUR_UPDATABLE);
//			pst.setObject(1, afterRow.get(keyName));
//			pst.setString(2, id);
//			count = pst.executeUpdate();
//			util.close(pst);
//			memConn.commit();
//		} catch (Exception e) {
//			logger.error("更新失败,回滚");
//			memConn.rollback();
//			throw e;
//		} finally {
//			util.close(pst);
//		}
//		return count;
//	}

	/**
	 * 列迭代
	 * 
	 * @param categorySet
	 *            栏目设置
	 * @param fields
	 *            字段信息
	 * @param tableName
	 *            表名
	 * @param conn
	 *            连接
	 * @throws Exception
	 */
	private void colIteration(List<StepSetOperation> categorySet,
			Map<String, String> fields, String tableName, Connection memConn)
			throws Exception {
		try {
			long startTime = 0;
			memConn.setAutoCommit(false);
			for (int i = 0; i < categorySet.size(); i++) {
				startTime = System.currentTimeMillis();
				StepSetOperation bean = categorySet.get(i);
				// 字段
				String columnid = bean.getColumnId();
				// 名称
				String columnName = bean.getColumnName();
				// 公式
				String formula = bean.getFormula();
				// 更新条件
				String ucStr = bean.getUpdateCriteria();
				StringBuffer sb = new StringBuffer();
				sb.append("UPDATE ");
				sb.append(tableName);
				sb.append(" SET ");
				sb.append(columnid);
				sb.append("=");
				if (null != formula && !"".equals(formula)) {
					for (Entry<String, String> fieldMap : fields.entrySet()) {
						String key = fieldMap.getKey();
						String value = fieldMap.getValue();
						formula = formula.replaceAll(value, key);
					}
				}
				sb.append(formula);
				sb.append(" ");
				if (null != ucStr && !"".equals(ucStr)) {
					for (Entry<String, String> fieldMap : fields.entrySet()) {
						String key = fieldMap.getKey();
						String value = fieldMap.getValue();
						ucStr = ucStr.replaceAll(value, key);
					}
					sb.append(" WHERE ");
					sb.append(" ");
					sb.append(ucStr);
				}
				String sql = sb.toString();
				PreparedStatement pst = memConn.prepareStatement(sql);
				int count = pst.executeUpdate();
				pst.close();
				memConn.commit();
				System.out.println("运算【" + columnName + "】列，影响行数：" + count
						+ " 消耗时间："
						+ (System.currentTimeMillis() - startTime + " 毫秒"));
			}
		} catch (Exception e) {
			logger.error("更新失败,回滚");
			memConn.rollback();
			throw e;
		}
	}

	/**
	 * 建内存表
	 * 
	 * @param tableName
	 *            表名
	 * @param listFields
	 *            表字段信息
	 * @param memConn
	 *            内存数据库连接
	 * @throws Exception
	 */
	public void createMemoryTable(String tableName,
			List<TableField> fieldsList, Connection memConn) throws Exception {
		try {
			Dialect dialect = Dialect.getInstance(memConn);
			StringBuffer keyStr = new StringBuffer();
			util.upDateBySql("DROP TABLE IF EXISTS " + tableName, memConn);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fieldsList.size(); i++) {
				TableField field = fieldsList.get(i);
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(field.getFieldName());
				String dataType = field.getDataType();
				String JdbcDataType = dialect.getColumnType(dataType);
				sb.append(" ");
				sb.append(JdbcDataType);
				// 判断DecimalSize
				int decimalSize = field.getDecimalSize();
				if ("金额".equals(dataType) || "浮点型".equals(dataType)) {
					sb.append("(38," + decimalSize + ")");
				}
				if (field.isKey()) {
					if (keyStr.length() > 0) {
						keyStr.append(",");
					}
					keyStr.append(field.getFieldName());
				}
			}
			String sqlString = "CREATE TABLE " + tableName + " " + "( "
					+ sb.toString() + ",PRIMARY KEY (" + keyStr.toString()
					+ "))";
			util.upDateBySql(sqlString, memConn);
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	/**
	 * 导入数据
	 * 
	 * @param tableName
	 *            业务数据库表名
	 * @param query
	 *            查询数据集的SQL
	 * @param listFields
	 *            字段信息
	 * @param bizConn
	 *            业务数据库连接
	 * @param memConn
	 *            内存数据库连接
	 * @throws Exception
	 */
	private void importData(String tableName, String query,
			List<TableField> fieldsList, Connection bizConn, Connection memConn)
			throws Exception {
		// 共多少行
		int totalRowNum = 0;
		// 共多少页
		int totalPage = 0;
		// 相同字段
		List<String> joinFieldList = null;
		PreparedStatement pst = null;
		try {
			memConn.setAutoCommit(false);
			Dialect dialect = Dialect.getInstance(bizConn);
			// 相同字段
			joinFieldList = sortingFields(query, fieldsList, bizConn);

			StringBuffer sbJoin = new StringBuffer();
			for (int i = 0; i < joinFieldList.size(); i++) {
				if (sbJoin.length() > 0) {
					sbJoin.append(",");
				}
				sbJoin.append(joinFieldList.get(i));
			}
			String querySql = "SELECT " + sbJoin.toString() + " FROM (" + query
					+ ") queryTmpView ";
			totalRowNum = dialect.getMaxRowNum(querySql, bizConn);
			totalPage = dialect.totalPage(totalRowNum, chunkSize);
			if (totalPage > 0) {
				StringBuffer fieldSB = new StringBuffer();
				StringBuffer fieldSBV = new StringBuffer();
				for (int i = 0; i < joinFieldList.size(); i++) {
					String fieldName = joinFieldList.get(i);
					if (fieldSB.length() > 0) {
						fieldSB.append(",");
						fieldSBV.append(",");
					}
					fieldSB.append(fieldName);
					fieldSBV.append("?");
				}
				StringBuffer sb = new StringBuffer();
				sb.append("INSERT INTO ");
				sb.append(tableName);
				sb.append(" (");
				sb.append(fieldSB);
				sb.append(") ");
				sb.append(" VALUES ");
				sb.append("(");
				sb.append(fieldSBV);
				sb.append(")");
				pst = memConn.prepareStatement(sb.toString(),
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
			}
			for (int i = 0; i < totalPage; i++) {
				try {
					memConn.setAutoCommit(false);
					String sql = dialect.getLimitString(querySql,
							i * chunkSize, chunkSize);
					ResultSet rs = util.getResultSetBySql(bizConn, sql);
					int batchSize = chunkUpdate(rs, pst, joinFieldList);
					System.out.println("导入数据量：" + batchSize);
					util.close(rs);
					memConn.commit();
				} catch (Exception e) {
					memConn.rollback();
					throw e;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			util.close(pst);
		}
	}

	/**
	 * 批量更新数据集
	 * 
	 * @param tableName
	 *            表名
	 * @param listFields
	 *            字段信息
	 * @param bizConn
	 *            数据集数据库连接
	 * @param memConn
	 *            内存数据库连接
	 * @throws Exception
	 */
	private void batchUpdateData(String tableName, List<TableField> fieldsList,
			Connection bizConn, Connection memConn) throws Exception {
		// 共多少行
		int totalRowNum = 0;
		// 共多少页
		int totalPage = 0;
		// 字段
		List<String> fields = new ArrayList<String>();
		List<String> keyFields = new ArrayList<String>();
		PreparedStatement pst = null;
		try {
			for (int i = 0; i < fieldsList.size(); i++) {
				TableField bean = fieldsList.get(i);
				if (!bean.isKey()) {
					fields.add(bean.getFieldName());
				} else {
					keyFields.add(bean.getFieldName());
				}
			}
			Dialect dialect = Dialect.getInstance(memConn);
			String query = "SELECT * FROM " + tableName;
			totalRowNum = dialect.getMaxRowNum(query, memConn);
			totalPage = dialect.totalPage(totalRowNum, chunkSize);
			if (totalPage > 0) {
				// 更新字段
				StringBuffer fieldSB = new StringBuffer();
				// 更新条件
				StringBuffer fieldWhere = new StringBuffer();
				for (int i = 0; i < fields.size(); i++) {
					String fieldName = fields.get(i);
					if (fieldSB.length() > 0) {
						fieldSB.append(",");
					}
					fieldSB.append(fieldName);
					fieldSB.append("=?");
				}
				for (int i = 0; i < keyFields.size(); i++) {
					String keyFieldName = keyFields.get(i);
					fields.add(keyFieldName);
					if (fieldWhere.length() > 0) {
						fieldWhere.append(" AND ");
					}
					fieldWhere.append(keyFieldName);
					fieldWhere.append("=?");
				}

				StringBuffer sb = new StringBuffer();
				sb.append("UPDATE ");
				sb.append(tableName);
				sb.append(" SET ");
				sb.append(fieldSB);
				sb.append(" WHERE ");
				sb.append(fieldWhere);
				pst = bizConn.prepareStatement(sb.toString(),
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
			}
			for (int i = 0; i < totalPage; i++) {
				try {
					bizConn.setAutoCommit(false);
					String sql = dialect.getLimitString(query, i * chunkSize,
							chunkSize);
					// util.showResult(sql, memConn);
					ResultSet rs = util.getResultSetBySql(memConn, sql);
					int batchSize = chunkUpdate(rs, pst, fields);
					System.out.println("更新数据量：" + batchSize);
					util.close(rs);
					bizConn.commit();
				} catch (Exception e) {
					bizConn.rollback();
					throw e;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			util.close(pst);
		}
	}

	/**
	 * 分片更新
	 * 
	 * @param rs
	 *            数据集
	 * @param pst
	 *            游标
	 * @param fields
	 *            字段
	 */
	private int chunkUpdate(ResultSet rs, PreparedStatement pst,
			List<String> fields) throws Exception {
		try {
			while (rs.next()) {
				for (int i = 0; i < fields.size(); i++) {
					String key = fields.get(i);
					Object value = rs.getObject(key);
					pst.setObject(i + 1, value);
				}
				pst.addBatch();
			}
			int[] batch = pst.executeBatch();
			return batch.length;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 整理字段
	 * 
	 * @param query
	 * @param fields
	 * @param bizConn
	 * @return
	 * @throws Exception
	 */
	private List<String> sortingFields(String query, List<TableField> fields,
			Connection bizConn) throws Exception {
		ResultSet rs = null;
		List<String> joinFields = null;
		try {
			// 更新字段
			Map<String, String> queryFieldsMap = new HashMap<String, String>();
			String sql = "SELECT * FROM (" + query + ") www WHERE 1 = 2";
			rs = util.getResultSetBySql(bizConn, sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				String columnId = rsmd.getColumnLabel(i + 1);
				queryFieldsMap.put(columnId.toLowerCase().trim(),
						columnId.toLowerCase());
			}
			util.close(rs);

			// 数据原表字段
			Map<String, String> dataSetFieldsMap = new HashMap<String, String>();
			for (int i = 0; i < fields.size(); i++) {
				TableField field = fields.get(i);
				dataSetFieldsMap.put(field.getFieldName().toLowerCase().trim(),
						field.getFieldName().toLowerCase());
			}

			joinFields = getJoinFields(queryFieldsMap, dataSetFieldsMap);
		} catch (Exception e) {
			throw e;
		} finally {
			util.close(rs);
		}
		return joinFields;
	}

	/**
	 * Join字段
	 * 
	 * @param fields1
	 *            字段集1
	 * @param fields2
	 *            字段集2
	 * @return
	 * @throws Exception
	 */
	private List<String> getJoinFields(Map<String, String> fields1,
			Map<String, String> fields2) throws Exception {
		List<String> joinList = new ArrayList<String>();
		try {
			// 共同字段
			Map<String, String> joinFields = new HashMap<String, String>();

			for (Entry<String, String> tableField : fields1.entrySet()) {
				String key = tableField.getKey();
				String value = fields2.get(key);
				if (null != value) {
					joinFields.put(key, key);
				}
			}

			for (Entry<String, String> tableField : fields2.entrySet()) {
				String key = tableField.getKey();
				String value = fields1.get(key);
				if (null != value) {
					joinFields.put(key, key);
				}
			}

			for (Entry<String, String> entry : joinFields.entrySet()) {
				joinList.add(entry.getKey());
			}

		} catch (Exception e) {
			throw e;
		}
		return joinList;
	}

}
