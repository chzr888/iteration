package com.chenzr.iteration.dialect;

public class MySQLDialect extends Dialect{
	
	public MySQLDialect(){
		typeNames.put("短字符", "varchar(20)");
		typeNames.put("字符", "varchar(50)");
		typeNames.put("中字符", "varchar(150)");
		typeNames.put("长字符", "varchar(500)");
		typeNames.put("超长字符", "varchar(8000)");
		typeNames.put("整型", "int");
		typeNames.put("金额", "float");
		typeNames.put("浮点型", "float");
		typeNames.put("日期", "datetime");
		typeNames.put("longtext", "LONGTEXT");
		typeNames.put("blob", "blob");
		typeNames.put("GUID", "varchar(50)");
	}

	@Override
	public String getLimitString(String query, int offset, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM (");
		sb.append(query);
		sb.append(") queryView LIMIT ");
		sb.append(offset+","+limit);
		return sb.toString();
	}

	@Override
	public String getLimitString(String query, int offset, int limit, String key) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM (");
		sb.append(query);
		sb.append(") queryView ORDER BY ");
		sb.append(key);
		sb.append(" LIMIT ");
		sb.append(offset+","+limit);
		return sb.toString();
	}
	
	@Override
	public String getLimitString(String query, int offset, int limit,
			String key, String keyType) {
		if("整型".equals(keyType)){
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT * FROM (");
			sb.append(query);
			sb.append(") queryView WHERE ");
			sb.append(key);
			sb.append(">=");
			sb.append(offset * limit);
			sb.append(" ORDER BY ");
			sb.append(key);
			sb.append(" LIMIT ");
			sb.append(limit);
			return sb.toString();
		}
		return getLimitString(query, offset, limit, key);
	}
	@Override
	public String getDecimalSize(String typeName, int p, int s) {
		if("整型".equals(typeName)){
			if(p > 11){
				p = 11;
			}
			return typeNames.get(typeName)+"("+p+")";
		}
		if(p > 38){
			p = 38;
		}
		if("金额".equals(typeName)){
			return typeNames.get(typeName)+"("+p+","+s+")";
		}
		if("浮点型".equals(typeName)){
			return typeNames.get(typeName)+"("+p+","+s+")";
		}
		return typeNames.get(typeName);
	}

	@Override
	public String getDropTableSql(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP TABLE IF EXISTS ");
		sb.append(tableName);
		return sb.toString();
	}
}
