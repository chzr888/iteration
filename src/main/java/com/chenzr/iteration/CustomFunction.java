package com.chenzr.iteration;

import java.util.List;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.compile.InterpreterSourceBuilder;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.parser.FelNode;

/**
 * 自定义方法
 * @author chenzr
 *
 */
public abstract class CustomFunction implements Function {

	/*
	 * (non-Javadoc)
	 * 
	 * @see .script.function.Function#call(.script.antlr.AstNode,
	 *      .script.context.ScriptContext)
	 */
	public Object call(FelNode node, FelContext context) {
		// 如果参数中包含表达式，执行表达式。将表达式替换成表达式执行结果。
		Object[] children = evalArgs(node, context);
		return call(children);
	}
	
	public SourceBuilder toMethod(FelNode node, FelContext ctx) {
		return InterpreterSourceBuilder.getInstance();
	}


	public static Object[] evalArgs(FelNode node, FelContext context) {
		Object[] returnMe = null;
		List<FelNode> children = node.getChildren();
		if(children!=null&& children.size()>0){
			Object[] args = children.toArray();
			int size = args.length;
			returnMe = new Object[size];
			System.arraycopy(args, 0, returnMe, 0, size);
			for (int i = 0; i < size; i++) {
				Object child = args[i];
				if (child instanceof Expression) {
					Expression childExp = ((Expression) child);
					returnMe[i] = childExp.eval(context);
				}
			}
		}
		return returnMe;
	}
	
	/**
	 * 调用函数
	 * 
	 * @param arguments
	 * @return
	 */
	abstract public Object call(Object[] arguments);
}
