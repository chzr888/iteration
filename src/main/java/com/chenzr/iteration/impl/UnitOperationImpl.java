package com.chenzr.iteration.impl;

import java.util.List;
import java.util.Map;

import com.chenzr.iteration.Operation;
import com.chenzr.iteration.UnitOperation;
import com.chenzr.iteration.bean.StepSetOperation;


public class UnitOperationImpl extends UnitOperation implements Operation {

	// 本行数据 字段（中文）->值
	private Map<String, Object> rowMap;
	// 计算公式
	private StepSetOperation stepOperation;

	@Override
	public Object execute() {
		String columnName = stepOperation.getColumnName();
		UnitOperation operation = null;
		if("栏目运算".equalsIgnoreCase(stepOperation.getType())){
			operation = new DefaultOperation();
		}
		if("自定义".equalsIgnoreCase(stepOperation.getType())){
			operation = new CustomOperation();
		}
		operation.setOperation(stepOperation);
		operation.setScale(stepOperation.getScale());
		operation.setRoundMode(stepOperation.getRoundMode());
		operation.setRowMap(rowMap);
		Object value = operation.execute();
		operation.setValue(value);
		operation.format();
		rowMap.put(columnName, operation.getValue());
		return rowMap;
	}

	@Override
	public Map<String, Object> operation(Map<String, Object> row,
			List<StepSetOperation> stepOperation) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> operation(Map<String, Object> _row,
			StepSetOperation _stepOperation) throws Exception {
		this.rowMap = _row;
		this.stepOperation = _stepOperation;
		execute();
		return rowMap;
	}

}
