package com.chenzr.iteration.function.numeric;

import com.chenzr.iteration.CustomFunction;
import com.greenpineyu.fel.common.NumberUtil;

public class FunctionRandom extends CustomFunction {

	@Override
	public String getName() {
		return "RANDOM";
	}

	@Override
	public Object call(Object[] arguments) {
		if(null != arguments && arguments.length>0){
			double l = NumberUtil.toDouble(arguments[0]);
			if(arguments.length>1){
				Object value = NumberUtil.toInteger(Math.random() * l );
				return value;
			}
			return Math.random() * l;
		}
		return Math.random();
	}

}
