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
		sb.append("BEGIN EXECUTE IMMEDIATE 'DROP TABLE ");
		sb.append(tableName);
		sb.append("' ; EXCEPTION WHEN OTHERS THEN NULL ; END;");
		return sb.toString();
	}

}
