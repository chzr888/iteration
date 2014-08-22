package com.chenzr.iteration;

import java.util.Map;

/**
 * 常规公式定义接口
 * @author chenzr
 */
public interface Formula {

	/**
	 * 计算
	 * @param exp
	 * @param params
	 * @return
	 */
	Object eval(String exp, Map<String, Object> params);

}
