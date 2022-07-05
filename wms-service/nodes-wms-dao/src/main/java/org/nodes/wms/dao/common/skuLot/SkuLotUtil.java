package org.nodes.wms.dao.common.skuLot;

import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ReflectUtil;
import org.springframework.core.convert.Property;

import java.lang.reflect.InvocationTargetException;

/**
 * skuLot工具类。该工具类的前提条件：要求两个对象必须有skuLot[n]的属性
 */
public class SkuLotUtil {
	private static final String SKULOT = "skuLot";
	private static final int SKULOT_NUMBER = 30;

	/**
	 * 将原对象中的skuLot1-skuLot30赋值给目标对象
	 *
	 * @param source 原对象
	 * @param target 目标对象
	 * @param <T1>   存在skuLot[n]属性的对象
	 * @param <T2>   存在skuLot[n]属性的对象
	 */
	public static <T1, T2> void setAllSkuLot(T1 source, T2 target) {
		String propertyName;
		for (int i = 0; i < SKULOT_NUMBER; ++i) {
			propertyName = String.format("%s%d", SKULOT, i + 1);
			Property sourceProperty = ReflectUtil.getProperty(source.getClass(), propertyName);
			Property targetProperty = ReflectUtil.getProperty(target.getClass(), propertyName);
			try {
				if (Func.isNotEmpty(sourceProperty) && Func.isNotEmpty(targetProperty)) {
					targetProperty.getWriteMethod().invoke(target, sourceProperty.getReadMethod().invoke(source));
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 比较两个对象中的所有skuLot是否相同
	 *
	 * @param t1
	 * @param t2
	 * @param <T1> 存在skuLot[n]属性的对象
	 * @param <T2> 存在skuLot[n]属性的对象
	 * @return 相同返回ture，否则返回false,如果没有skuLot属性也返回false
	 */
	public static <T1, T2> boolean compareAllSkuLot(T1 t1, T2 t2) {
		if (Func.isEmpty(t1) || Func.isEmpty(t2)) {
			return false;
		}

		String propertyName;
		for (int i = 0; i < SKULOT_NUMBER; ++i) {
			propertyName = String.format("%s%d", SKULOT, i + 1);
			Property t1Property = ReflectUtil.getProperty(t1.getClass(), propertyName);
			Property t2Property = ReflectUtil.getProperty(t2.getClass(), propertyName);
			if (Func.isEmpty(t1Property) || Func.isEmpty(t2Property)) {
				return false;
			}

			try {
				Object t1SkuLot = t1Property.getReadMethod().invoke(t1);
				Object t2SkuLot = t2Property.getReadMethod().invoke(t2);
				if (!isSame(t1SkuLot, t2SkuLot)) {
					return false;
				}
			} catch (IllegalAccessException | InvocationTargetException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断两个是否相同，如果两个都为空（null和空白字符）或者两个值相等
	 *
	 * @param t1SkuLot
	 * @param t2SkuLot
	 * @return true：相同
	 */
	private static boolean isSame(Object t1SkuLot, Object t2SkuLot) {
		if (t1SkuLot != null && t1SkuLot.equals(t2SkuLot)) {
			return true;
		}

		return Func.isEmpty(t1SkuLot) && Func.isEmpty(t2SkuLot);
	}

	/**
	 * 判断对象是否有30个skuLot属性
	 *
	 * @param object
	 * @param <T>
	 * @return true:存在30个批属性
	 */
	public static <T> boolean hasSkuLot(T object) {
		String propertyName;
		for (int i = 0; i < SKULOT_NUMBER; ++i) {
			propertyName = String.format("%s%d", SKULOT, i + 1);
			Property skuLotProperty = ReflectUtil.getProperty(object.getClass(), propertyName);
			if (Func.isNull(skuLotProperty)) {
				return false;
			}
		}

		return true;
	}
}
