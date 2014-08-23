package com.chenzr.iteration.function.numeric;

import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.function.CommonFunction;

public class FunctionRandom extends CommonFunction {

	@Override
	public String getName() {
		return "RANDOM";
	}

	@Override
	public Object call(Object[] arguments) {
		if(null != arguments && arguments.length>0){
			double l = NumberUtil.toDouble(arguments[0]);
			if(arguments.length>1){
				double r =  NumberUtil.toDouble(arguments[1]);
				return Math.random() * l * r;
			}
			return Math.random() * l;
		}
		return Math.random();
	}

}
