
package com.chenzr.iteration.bean;

import java.util.List;

/**
 * 方案
 * @author chenzr
 *
 */
public class Project extends Object {
	//方案名称
	private String projectName;
	//方案ID
	private String projectId;
	//参数
	private List<Object> param;
	
    /**
     * @return 创建并返回此对象的一个副本。
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        //直接调用父类的clone()方法,返回克隆副本
        return super.clone();
    }
	
}
