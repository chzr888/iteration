package com.chenzr.iteration.bean;

public class TableField {

	//步骤名称
	private String stepName;
	//序号
	private int no;
	//是否主键
	private boolean isKey;
	//字段
	private String fieldName;
	//字段名称
	private String fieldCaption;
	//字段类型
	private String dataType;
	//容纳带有小数的数字。
	private int decimalSize;
	
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public boolean isKey() {
		return isKey;
	}
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldCaption() {
		return fieldCaption;
	}
	public void setFieldCaption(String fieldCaption) {
		this.fieldCaption = fieldCaption;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getDecimalSize() {
		return decimalSize;
	}
	public void setDecimalSize(int decimalSize) {
		this.decimalSize = decimalSize;
	}
}
