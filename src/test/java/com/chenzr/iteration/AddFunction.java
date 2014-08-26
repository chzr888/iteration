package com.chenzr.iteration;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class AddFunction extends AbstractFunction{

	@Override
	public String getName() {
		return "add";
	}
	
	public AviatorObject call(Map<String, Object> env, AviatorObject... args) {   
//        Number left = FunctionUtils.getNumberValue(arg0, env);
//        Number right = FunctionUtils.getNumberValue(arg1, env);  
//        return new AviatorDouble(left.doubleValue() + right.doubleValue());  
        return new AviatorDouble(0.0);
    } 
	
}
