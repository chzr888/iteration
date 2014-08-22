package com.chenzr.iteration.bean;

import java.util.List;

/**
 * 步骤
 * @author chenzr
 *
 */
public class StepBean {
	// 步骤名称
	private String stepName;
	// jndi名称
	private String jndiName;
	// 表名
	private String sqlTableName;
	// 数据准备检查
	private String prepareDataClass;
	// 日期检查
	private String stampStateClass;
	// 取数前检查
	private String prereQuisite;
	// 运算公式
	private List<StepSetOperation> operation;
	
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	public String getSqlTableName() {
		return sqlTableName;
	}
	public void setSqlTableName(String sqlTableName) {
		this.sqlTableName = sqlTableName;
	}
	public String getPrepareDataClass() {
		return prepareDataClass;
	}
	public void setPrepareDataClass(String prepareDataClass) {
		this.prepareDataClass = prepareDataClass;
	}
	public String getStampStateClass() {
		return stampStateClass;
	}
	public void setStampStateClass(String stampStateClass) {
		this.stampStateClass = stampStateClass;
	}
	public String getPrereQuisite() {
		return prereQuisite;
	}
	public void setPrereQuisite(String prereQuisite) {
		this.prereQuisite = prereQuisite;
	}
	public List<StepSetOperation> getOperation() {
		return operation;
	}
	public void setOperation(List<StepSetOperation> operation) {
		this.operation = operation;
	}

}
