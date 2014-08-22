package com.chenzr.iteration;

import java.util.List;
import java.util.Map;

import com.chenzr.iteration.bean.StepSetOperation;


public interface Operation {

	/**
	 * 运算
	 * @param row
	 * @param formulas
	 * @return
	 */
	Map<String, Object> operation(Map<String, Object> row, List<StepSetOperation> stepOperation) throws Exception;
	
	/**
	 * 运算
	 * @param row
	 * @param formulas
	 * @return
	 */
	Map<String, Object> operation(Map<String, Object> row, StepSetOperation stepOperation) throws Exception;
	
}
