package com.chenzr.iteration.bean;

/**
 * 
 * @author chenzr
 * 步骤运算逻辑
 */
public class StepSetOperation {
	//步骤名称
	private String stepName;
	//序号
	private int no;
	//字段名称
	private String columnName;
	//字段
	private String columnId;
	//类型
	private String type;
	//运算方式 (行运算，列运算)
	private String operateMode;
	//运算公式
	private String formula;
	//更新条件
	private String updateCriteria;
	//表示需要精确到小数点以后几位
	private int scale;
	//表示用户指定的舍入模式
	private int roundMode;
	
	/**
	 * 步骤名称
	 * @return
	 */
	public String getStepName() {
		return stepName;
	}
	/**
	 * 步骤名称
	 * @param stepName
	 */
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	/**
	 * 运算序号
	 * @return
	 */
	public int getNo() {
		return no;
	}
	/**
	 * 运算序号
	 */
	public void setNo(int no) {
		this.no = no;
	}
	/**
	 * 字段名称（中文名称）
	 * @return
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 *  字段名称（中文名称）
	 * @param columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 *  字段名称（英文名称）
	 * @return
	 */
	public String getColumnId() {
		return columnId;
	}
	/**
	 * 字段名称（英文名称）
	 * @param columnId
	 */
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	/**
	 * 类型
	 * @return
	 */
	public String getType() {
		return type;
	}
	/**
	 * 类型
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 计算公式
	 * @return
	 */
	public String getFormula() {
		return formula;
	}
	/**
	 * 计算公式
	 * @param formula
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}
	/**
	 * 运算方式 (行运算，列运算)
	 * @return
	 */
	public String getOperateMode() {
		return operateMode;
	}
	/**
	 * 运算方式 (行运算，列运算)
	 * @param operateMode
	 */
	public void setOperateMode(String operateMode) {
		this.operateMode = operateMode;
	}
	/**
	 * 更新条件
	 * @return
	 */
	public String getUpdateCriteria() {
		return updateCriteria;
	}
	/**
	 * 设置更新条件
	 * @param updateCriteria
	 */
	public void setUpdateCriteria(String updateCriteria) {
		this.updateCriteria = updateCriteria;
	}
	/**
	 * 表示需要精确到小数点以后几位
	 */
	public int getScale() {
		return scale;
	}
	/**
	 * 表示需要精确到小数点以后几位
	 * @param scale
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}
	/**
	 * 表示用户的舍入模式
	 * @return
	 */
	public int getRoundMode() {
		return roundMode;
	}
	/**
	 * 表示用户指定的舍入模式
	 * @param _roundingMode
	 */
	public void setRoundMode(int roundMode) {
		this.roundMode = roundMode;
	}
}
