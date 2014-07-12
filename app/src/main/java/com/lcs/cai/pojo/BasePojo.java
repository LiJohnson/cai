package com.lcs.cai.pojo;

import java.lang.reflect.Field;

/**
 * Created by lcs on 7/12/2014.
 */
public class BasePojo {
	/**
	 * 获取一个属性的值
	 * @param fieldName
	 * @return
	 */
	private Object fieldValue( String fieldName ){
		try {
			return this.getClass().getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), (Class<?>[]) null).invoke(this, (Object[]) null);
		} catch (Exception e) {
			return "error" ;
		}
	}

	/**
	 * 	打印一个实例
	 *  格式: fieldName		value		type
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	 @Override
	 public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\n-------------------start---------------------- \n");
		buf.append("class Name \t\t" +this.getClass().getName() +"\n");
		Field[] fields = this.getClass().getDeclaredFields();
		for( Field field : fields ){
			String name = field.getName();
			Object object = this.fieldValue(name);
			buf.append(name + "\t\t" + (object == null ? "null" : object.toString()) + "\t\t" +field.getType().getName() + "\n");
		}
		buf.append("-------------------end  ----------------------\n");
		return buf.toString();
	 }

}
