package com.chenzr.iteration.function;

import java.util.UUID;

import com.greenpineyu.fel.function.CommonFunction;

/**
 * GUID
 * @author chenzr
 *
 */
public class FunctionUUID extends CommonFunction{

	@Override
	public String getName() {
		return "UUID";
	}

	@Override
	public Object call(Object[] arguments) {
		return UUID.randomUUID().toString();
	}

}
