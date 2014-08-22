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
	public String getLimitString(String query, int offset, int limit, String keyStr) {
		String sqlStr = query.trim();
		StringBuilder pagingSelect = new StringBuilder();
		pagingSelect.append("select * from (select row_number() over(order by ");
		pagingSelect.append(keyStr);
		pagingSelect.append(" ) as rownum_,queryView.* from ( ");
		pagingSelect.append(sqlStr);
		pagingSelect.append(" ) queryView ) queryView2 where rownum_>=");
		pagingSelect.append(offset);
		pagingSelect.append("and rownum_<=");
		pagingSelect.append((offset+limit));
		return pagingSelect.toString();
	}
}
