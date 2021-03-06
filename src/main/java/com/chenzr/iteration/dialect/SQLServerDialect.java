package com.chenzr.iteration.dialect;

public class SQLServerDialect extends Dialect {

	public SQLServerDialect(){
		typeNames.put("短字符", "varchar(20)");
		typeNames.put("字符", "varchar(50)");
		typeNames.put("中字符", "varchar(150)");
		typeNames.put("长字符", "varchar(500)");
		typeNames.put("超长字符", "varchar(8000)");
		typeNames.put("整型", "int");
		typeNames.put("金额", "money");
		typeNames.put("浮点型", "float");
		typeNames.put("日期", "datetime");
		typeNames.put("longtext", "NTEXT");
		typeNames.put("blob", "blob");
		typeNames.put("GUID", "varchar(50)");
	}
	
	@Override
	public String getLimitString(String query, int offset, int limit) {
		return getLimitString(query, offset, limit, "id");
	}
	
	@Override
	public String getLimitString(String query, int offset, int limit, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (select row_number() over(order by ");
		sb.append(key);
		sb.append(" ) as rownum_,queryView.* from ( ");
		sb.append(query);
		sb.append(" ) queryView ) queryView2 where rownum_>");
		sb.append(offset);
		sb.append("and rownum_<=");
		sb.append((offset+limit));
		return sb.toString();
	}
	
	@Override
	public String getLimitString(String query, int offset, int limit,
			String key, String keyType) {
		return getLimitString(query, offset, limit, key);
	}

	@Override
	public String getDecimalSize(String typeName, int p, int s) {
		if(p > 38){
			p = 38;
		}
		if("浮点型".equals(typeName)){
			return typeNames.get(typeName)+"("+p+","+s+")";
		}
		return typeNames.get(typeName);
	}

	@Override
	public String getDropTableSql(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("IF EXISTS ( SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '");
		sb.append(tableName);
		sb.append("' ) DROP TABLE ");
		sb.append(tableName);
		return sb.toString();
	}
}
