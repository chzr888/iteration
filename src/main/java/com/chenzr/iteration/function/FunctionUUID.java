package com.chenzr.iteration.function;

import java.util.UUID;

import com.chenzr.iteration.CustomFunction;

/**
 * GUID
 * @author chenzr
 *
 */
public class FunctionUUID extends CustomFunction{

	@Override
	public String getName() {
		return "UUID";
	}

	@Override
	public Object call(Object[] arguments) {
		return UUID.randomUUID().toString();
	}

}
