package com.chenzr.iteration.dialect;

public class H2Dialect extends Dialect{
	
	public H2Dialect() {
		typeNames.put("短字符", "varchar(20)");
		typeNames.put("字符", "varchar(50)");
		typeNames.put("中字符", "varchar(150)");
		typeNames.put("长字符", "varchar(800)");
		typeNames.put("超长字符", "varchar(2147483647)");
		typeNames.put("整型", "int");
		typeNames.put("金额", "decimal");
		typeNames.put("浮点型", "decimal");
		typeNames.put("日期", "datetime");
		typeNames.put("longtext", "CLOB");
		typeNames.put("blob", "blob");
		typeNames.put("GUID", "varchar(50)");
	}

	@Override
	public String getLimitString(String query, int offset, int limit) {
		return "select * from ("+query+") queryView LIMIT "+offset+","+limit;
	}

	@Override
	public String getLimitString(String query, int offset, int limit, String key) {
		return getLimitString(query, offset, limit);
	}
}
