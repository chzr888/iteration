package com.chenzr.iteration.impl;

import com.chenzr.iteration.UnitOperation;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.context.FelContext;


/**
 * 单元运算
 * @author chenzr
 * 
 */
public class UnitOperationImpl implements UnitOperation {

	@Override
	public Object operation(FelEngine engine, String exp) throws Exception {
		return engine.eval(exp);
	}

	@Override
	public Object operation(FelEngine engine, String exp,FelContext ctx )
			throws Exception {
		return engine.eval(exp, ctx);
	}



}
