package com.chenzr.iteration.function.aggregate;

import com.chenzr.iteration.CustomFunction;


public class FunctionCount extends CustomFunction {

	@Override
	public String getName() {
		return "COUNT";
	}

	@Override
	public Object call(Object[] arguments) {
		if(null != arguments && arguments.length >0){
			return arguments.length;
		}
		return 0;
	}

}
