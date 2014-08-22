package com.chenzr.iteration;

import java.math.BigDecimal;
import java.util.Map;

import com.chenzr.iteration.bean.StepSetOperation;



/**
 * 单元运算接口
 * @author chenzr
 *
 */
public abstract class UnitOperation {
	
	//默认精确到小数点以后第3位
	private int scale = 3;
	//默认4舍5入
	private int roundMode = BigDecimal.ROUND_HALF_UP;
	//计算后的值
	private Object value;
	//当前行数据
	private Map<String, Object> rowMap;
	//数据运算设置
	private StepSetOperation operation;
	
	/**
	 * 格式化
	 */
	public void format() {
		BigDecimal decimal = null; 
		if(null == value){
			return ;
		}
		if(value instanceof Integer){
			decimal = new BigDecimal((Integer)value);
			decimal.setScale(scale,roundMode);
			this.value = decimal.intValue();
		}else if(value instanceof Double){
			decimal = new BigDecimal((Double)value);	
			decimal.setScale(scale, roundMode);
			this.value = decimal.doubleValue();
		}else if(value instanceof Float){
			decimal = new BigDecimal((Float)value);	
			decimal.setScale(scale, roundMode);
			this.value = decimal.doubleValue();
		}
	}
	
	/**
	 * 表示需要精确到小数点以后几位
	 * @param scale
	 */
	public void setScale(int _scale){
		this.scale = _scale;
	}
	
	/**
	 * 表示用户指定的舍入模式
	 * @param _roundingMode
	 */
	public void setRoundMode(int _roundingMode){
		this.roundMode = _roundingMode;
	}
	
	/**
	 * 表示用户的舍入模式
	 * @return
	 */
	public int getRoundMode() {
		return roundMode;
	}

	/**
	 * 计算后的值
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 设置计算后的值
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 精确到小数点以后几位
	 * @return
	 */
	public int getScale() {
		return scale;
	}
	
	/**
	 * 当前行数据
	 * @return
	 */
	public Map<String, Object> getRowMap() {
		return rowMap;
	}

	/**
	 * 设置当前行数据
	 * @param rowMap
	 */
	public void setRowMap(Map<String, Object> rowMap) {
		this.rowMap = rowMap;
	}

	/**
	 * 运算方式
	 * @return
	 */
	public StepSetOperation getOperation() {
		return operation;
	}

	/**
	 * 设置运算方式
	 * @return
	 */
	public void setOperation(StepSetOperation operation) {
		this.operation = operation;
	}

	/**
	 * 自定义方法
	 * @param operation 公式设置Baen
	 * @param row 当前行数据 
	 */
	public abstract Object execute();

}
