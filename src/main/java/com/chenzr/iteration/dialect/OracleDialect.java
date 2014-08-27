package com.chenzr.iteration.dialect;

public class OracleDialect extends Dialect{
	
	public OracleDialect(){
		typeNames.put("短字符", "varchar2(20)");
		typeNames.put("字符", "varchar2(50)");
		typeNames.put("中字符", "varchar2(150)");
		typeNames.put("长字符", "varchar2(500)");
		typeNames.put("超长字符", "varchar2(4000)");
		typeNames.put("整型", "integer");
		typeNames.put("金额", "float");
		typeNames.put("浮点型", "float");
		typeNames.put("日期", "date");
		typeNames.put("longtext", "LONG");
		typeNames.put("blob", "blob");
		typeNames.put("GUID", "varchar(50)");
	}

	@Override
	public String getLimitString(String query, int offset, int limit) {
		StringBuilder sb = new StringBuilder();
		sb.append( "SELECT * FROM ( SELECT ROWNUM rownum_, queryView.* FROM (" );
		sb.append( query );
		sb.append( ") queryView WHERE ROWNUM <= ");
		sb.append(offset+limit);
		sb.append(" ) WHERE rownum_ > " );
		sb.append(offset);
		return sb.toString();
	}

	@Override
	public String getLimitString(String query, int offset, int limit, String key) {
		return getLimitString(query, offset, limit);
	}
	
	@Override
	public String getLimitString(String query, int offset, int limit,
			String key, String keyType) {
		return getLimitString(query, offset, limit);
	}

	@Override
	public String getDecimalSize(String typeName, int p, int s) {
		return typeNames.get(typeName);
	}

	@Override
	public String getDropTableSql(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("BEGIN EXECUTE IMMEDIATE 'DROP TABLE ");
		sb.append(tableName);
		sb.append("' ; EXCEPTION WHEN OTHERS THEN NULL ; END;");
		return sb.toString();
	}
}
