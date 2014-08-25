package com.chenzr.iteration;

import java.util.Map;

import com.greenpineyu.fel.context.FelContext;

/**
 * 迭代运算上下文
 * @author chenzr
 *
 */
public interface IterationContext extends FelContext {

	void set(Map<String,Object> map);
	
	void clear();
	
}
