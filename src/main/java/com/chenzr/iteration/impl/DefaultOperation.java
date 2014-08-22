package com.chenzr.iteration.impl;

import com.chenzr.iteration.UnitOperation;



/**
 * 默认的计算
 * @author chenzr
 *
 */
public class DefaultOperation extends UnitOperation{

	@Override
	public Object execute() {
		FormulaImpl engine = new FormulaImpl();
		Object value = engine.eval(this.getOperation().getFormula(), this.getRowMap());
		return value;
	}

}
