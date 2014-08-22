package com.chenzr.iteration.impl;

import java.util.List;
import java.util.Map;

import com.chenzr.iteration.Operation;
import com.chenzr.iteration.UnitOperation;
import com.chenzr.iteration.bean.StepSetOperation;


/**
 * 行计算
 * @author chenzr
 *
 */
public class RowOperationImpl implements Operation {
	
	//本行数据  字段（中文）->值
	private Map<String, Object> rowMap;
	//计算公式  
	private List<StepSetOperation> stepOperation;
			
	/**
	 * 单元执行
	 */
	public void container() throws Exception {
		for (int i = 0; i < stepOperation.size(); i++) {
			StepSetOperation step = stepOperation.get(i);
			String columnName = step.getColumnName();
			UnitOperation operation = null;
			if("栏目运算".equalsIgnoreCase(step.getType())){
				operation = new DefaultOperation();
			}
			if("自定义".equalsIgnoreCase(step.getType())){
				operation = new CustomOperation();
			}
			operation.setOperation(step);
			operation.setScale(step.getScale());
			operation.setRoundMode(step.getRoundMode());
			operation.setRowMap(rowMap);
			Object value = operation.execute();
			operation.setValue(value);
			operation.format();
			rowMap.put(columnName, operation.getValue());
		}
	}

	@Override
	public Map<String, Object> operation(Map<String, Object> _row,
			List<StepSetOperation> _stepOperation) throws Exception {
		this.rowMap = _row;
		this.stepOperation = _stepOperation;
		container();
		return rowMap;
	}

	@Override
	public Map<String, Object> operation(Map<String, Object> row,
			StepSetOperation stepOperation) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
