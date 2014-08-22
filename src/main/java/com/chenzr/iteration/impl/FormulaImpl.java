package com.chenzr.iteration.impl;
import java.util.Map;

import com.chenzr.iteration.Formula;

/**
 * 公式计算
 * @author chenzr
 *
 */
public class FormulaImpl implements Formula {
	
	@Override
	public Object eval(String exp, Map<String, Object> params) {
//		MapContext ctx2 = new MapContext();
//		for (Entry<String, Object> element : params.entrySet()) {
//			ctx2.set(element.getKey(), element.getValue());
//		}
//		return new FelEngineImpl().eval(exp,ctx2);
		return null;
	}

}
