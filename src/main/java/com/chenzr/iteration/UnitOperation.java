package com.chenzr.iteration;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.context.FelContext;

/**
 * 单元运算接口
 * @author chenzr
 *
 */
public interface UnitOperation {
	
	/**
	 * 运算
	 * @param engine 运算引擎
	 * @param exp 表达式
	 * @return
	 * @throws Exception
	 */
	Object operation(FelEngine engine,String exp)throws Exception;
	
	/**
	 * 运算引擎
	 * @param engine 运算引擎
	 * @param exp 表达式
	 * @param ctx 上下文
	 * @return
	 * @throws Exception
	 */
	Object operation(FelEngine engine,String exp,FelContext ctx) throws Exception;
}
