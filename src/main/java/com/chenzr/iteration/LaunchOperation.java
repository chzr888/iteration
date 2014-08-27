package com.chenzr.iteration;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.chenzr.iteration.bean.StepSetOperation;
import com.chenzr.iteration.bean.TableField;
import com.chenzr.iteration.utils.JdbcHelper;


public class LaunchOperation {

	private JdbcHelper util = new JdbcHelper();

	/**
	 * 运行
	 * 
	 * @param stepName
	 *            步骤名称
	 * @param whereStr
	 *            数据集条件
	 * @param conn
	 *            配置数据库连接
	 * @param bizConn
	 *            数据集数据库
	 * @param memConn
	 *            内存数据库
	 * @param isMemoryCompute
	 *            是否在内存计算
	 * @return
	 * @throws Exception
	 */
	public String launch(String stepName, String whereStr, Connection conn,
			Connection bizConn, Connection memConn, boolean isMemoryCompute)
			throws Exception {
		String msg = "";
		// 表名
		String tableName = "";
		// 步骤运算设置
		List<StepSetOperation> operations = new ArrayList<StepSetOperation>();
		// 表字段信息
		List<TableField> fieldsList = new ArrayList<TableField>();
		try {
			// 表名
			tableName = getTableName(stepName, conn);
			// 表字段设置
			fieldsList = getTableFields(stepName, conn);
			// 步骤运算设置
			operations = getStepSetsOperation(stepName, conn);
			// 查询语句
			String query = "select * from " + tableName + " " + whereStr;
			// 执行
			StepOperation step = new StepOperation();
			msg = step.execute(tableName, query, fieldsList, operations,
					memConn, bizConn, isMemoryCompute);

		} catch (Exception e) {
			throw e;
		}
		return msg;
	}

	/**
	 * 查询保存数据的物理表名
	 * 
	 * @param stepName
	 *            步骤名称
	 * @param conn
	 *            连接
	 * @return 返回表名
	 * @throws Exception
	 */
	public String getTableName(String stepName, Connection conn)
			throws Exception {
		String tableName = "";
		ResultSet rs = null;
		try {
			String sql = "select sqltablename from GSprojectStepDefined where stepname = '"
					+ stepName + "'";
			rs = util.getResultSetBySql(conn, sql);
			if (rs.next()) {
				tableName = rs.getString(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			util.close(rs);
		}
		return tableName;
	}

	/**
	 * 查询步骤运算设置
	 * 
	 * @param stepName
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public List<StepSetOperation> getStepSetsOperation(String stepName,
			Connection conn) throws Exception {
		ResultSet rs = null;
		List<StepSetOperation> operations = new ArrayList<StepSetOperation>();
		try {
			String sql = "SELECT a.*,b.decimalsize FROM gmStepOperation a "
					+ " LEFT JOIN gsprojectstepcolumnfield b on a.stepname = b.stepname"
					+ " and b.columnid = a.columnName where a.stepname ='"
					+ stepName + "' ORDER by a.no";
			rs = util.getResultSetBySql(conn, sql);
			while (rs.next()) {
				StepSetOperation bean = new StepSetOperation();
				bean.setStepName(rs.getString("stepname"));
				bean.setColumnName(rs.getString("columnName"));
				bean.setColumnId(rs.getString("columnId"));
				bean.setNo(rs.getInt("no"));
				bean.setOperateMode(rs.getString("operateMode"));
				bean.setUpdateCriteria(rs.getString("updateCriteria"));
				bean.setType(rs.getString("type"));
				bean.setFormula(rs.getString("formula"));
				bean.setScale(rs.getInt("decimalsize"));
				bean.setRoundMode(BigDecimal.ROUND_HALF_UP);
				operations.add(bean);
			}
			util.close(rs);
		} catch (Exception e) {
			throw e;
		} finally {
			util.close(rs);
		}
		return operations;
	}

	/**
	 * 查询表字段信息
	 * 
	 * @param stepName
	 *            步骤名称
	 * @param conn
	 *            连接
	 * @return
	 * @throws Exception
	 */
	public List<TableField> getTableFields(String stepName, Connection conn)
			throws Exception {
		ResultSet rs = null;
		List<TableField> listFields = new ArrayList<TableField>();
		try {
			String sql = "SELECT a.*,b.decimalsize FROM gsprojectstepdatastore a"
					+ " LEFT JOIN gsprojectstepcolumnfield b ON a.stepname = b.stepname"
					+ " AND b.columnid = a.fieldcaption where a.stepname = '"
					+ stepName + "' ORDER BY a.no";
			rs = util.getResultSetBySql(conn, sql);
			while (rs.next()) {
				TableField field = new TableField();
				field.setStepName(stepName);
				field.setNo(rs.getInt("no"));
				field.setFieldName(rs.getString("fieldname").trim());
				field.setFieldCaption(rs.getString("fieldcaption").trim());
				field.setDataType(rs.getString("datatype").trim());
				int key = rs.getInt("iskey");
				boolean iskey = false;
				if (key == 1) {
					iskey = true;
				}
				field.setKey(iskey);
				field.setDecimalSize(rs.getInt("decimalsize"));
				listFields.add(field);
			}
			util.close(rs);
		} catch (Exception e) {
			throw e;
		} finally {
			util.close(rs);
		}
		return listFields;
	}

}
