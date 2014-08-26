package com.chenzr.iteration;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;

public class AviatorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int size = 1 ;
			long startTime = System.currentTimeMillis();
			Map<String, Object> map = new HashMap<String, Object>();
//			for (int j = 0; j < 10; j++) {
//				map.put("字段" + j, (int) (Math.random() * 1000));
//			}
			for (int i = 0; i < size; i++) {
				map.clear();
				for (int j = 0; j < 10; j++) {
					map.put("字段" + j, (int) (Math.random() * 100));
				}
				Long result = (Long) AviatorEvaluator.execute(
						"字段1+字段2+字段3-字段4+字段5-字段6*字段7+字段8", map, true);
				System.out.println(result);
			}
			Object object = AviatorEvaluator.execute("rand()", map);
			System.out.println(object);
			System.out.println(System.currentTimeMillis() - startTime);

			AviatorEvaluator.addFunction(new AddFunction());
			System.out.println(AviatorEvaluator.execute("add(1,2)"));
			System.out.println(AviatorEvaluator.execute("add(add(1,2),100)"));

			System.out
					.println(AviatorEvaluator
							.execute("1000+100.0*99-(600-3*15)/(((68-9)-3)*2-100)+10000%7*71"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
