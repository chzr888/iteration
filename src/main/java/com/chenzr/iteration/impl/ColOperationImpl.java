package com.chenzr.iteration.impl;

import java.util.List;
import java.util.Map;

import com.chenzr.iteration.Operation;
import com.chenzr.iteration.bean.StepSetOperation;


public class ColOperationImpl implements Operation{

	//本列数据  字段（中文）->值
	private Map<String, Object> rowMap;
	//计算公式  
	private List<StepSetOperation> stepOperation;
	
	@Override
	public Map<String, Object> operation(Map<String, Object> _row,
			List<StepSetOperation> _stepOperation) {
		this.rowMap = _row;
		this.stepOperation = _stepOperation;
		
		
		return rowMap;
	}

	@Override
	public Map<String, Object> operation(Map<String, Object> row,
			StepSetOperation stepOperation) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
