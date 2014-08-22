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
		String sql = query.trim();
		StringBuilder pagingSelect = new StringBuilder();
		pagingSelect.append( "select * from ( select row_.*, rownum rownum_ from ( " );
		pagingSelect.append( sql );
		pagingSelect.append( " ) row_ ) where rownum_ <= "+(offset+limit)+" and rownum_ > "+offset );
		return pagingSelect.toString();
	}

	@Override
	public String getLimitString(String query, int offset, int limit, String key) {
		return getLimitString(query, offset, limit);
	}

}
