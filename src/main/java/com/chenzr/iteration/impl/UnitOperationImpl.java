package com.chenzr.iteration.impl;

import com.chenzr.iteration.IterationContext;
import com.chenzr.iteration.IterationEngine;
import com.chenzr.iteration.UnitOperation;
import com.greenpineyu.fel.Expression;


/**
 * 单元运算
 * @author chenzr
 * 
 */
public class UnitOperationImpl implements UnitOperation {

	@Override
	public Object operation(IterationEngine engine, String exp)
			throws Exception {
		return engine.eval(exp);
	}

	@Override
	public Object operation(IterationEngine engine, String exp,
			IterationContext ctx) throws Exception {
//		long time = System.currentTimeMillis();
		Object value = engine.eval(exp, ctx);
//		System.out.println(System.currentTimeMillis() - time);
		return value;
	}

	@Override
	public Object operation(Expression exp, IterationContext ctx)
			throws Exception {
		return exp.eval(ctx);
	}



}
