package com.chenzr.iteration;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;


/**
 * 迭代运算引擎
 * 
 * @author chenzr
 * 
 */
public interface IterationEngine extends FelEngine{
	
	Expression compileExp(String exp, IterationContext ctx);
	
}
