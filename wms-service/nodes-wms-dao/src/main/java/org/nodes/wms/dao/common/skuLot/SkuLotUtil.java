package org.nodes.wms.dao.common.skuLot;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.dao.basics.skulot.SkuLotValDao;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotVal;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ReflectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.core.convert.Property;

import java.lang.reflect.InvocationTargetException;

/**
 * skuLot工具类。该工具类的前提条件：要求两个对象必须有skuLot[n]的属性
 *
 * @author nodesc
 */
@Slf4j
public class SkuLotUtil {

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
		for (int i = 0; i < WmsAppConstant.SKU_LOT_TOTAL_NUM; ++i) {
			propertyName = String.format("%s%d", WmsAppConstant.SKU_LOT_FIELD_PREFIX, i + 1);
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
		for (int i = 0; i < WmsAppConstant.SKU_LOT_TOTAL_NUM; ++i) {
			propertyName = String.format("%s%d", WmsAppConstant.SKU_LOT_FIELD_PREFIX, i + 1);
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
		if (Func.isNull(object)) {
			throw new ServiceException("批属性校验失败，对象为空");
		}

		String propertyName;
		for (int i = 0; i < WmsAppConstant.SKU_LOT_TOTAL_NUM; ++i) {
			propertyName = String.format("%s%d", WmsAppConstant.SKU_LOT_FIELD_PREFIX, i + 1);
			Property skuLotProperty = ReflectUtil.getProperty(object.getClass(), propertyName);
			if (Func.isNull(skuLotProperty)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 校验SKU LOT是否符合设置，目前只校验是否必填
	 *
	 * @param skuLotObject 含skuLot的对象
	 * @param woId         货主id，不能为空
	 * @param whId         库房id，不能为空
	 * @param <T>          有sku lot属性的类
	 */
	public static <T> void check(T skuLotObject, Long woId, Long whId) {
		if (Func.isNull(woId) || Func.isNull(whId) || !hasSkuLot(skuLotObject)) {
			throw new ServiceException("批属性校验失败，货主和库房编码不能为空,或对象中没有SKU LOT属性");
		}

		SkuLotValDao skuLotValDao = SpringUtil.getBean(SkuLotValDao.class);
		SkuLotVal skuLotVal = skuLotValDao.getByWhIdAndWoId(whId, woId);
		if (Func.isNull(skuLotVal)) {
			return;
		}

		String propertyName;
		for (int i = 0; i < WmsAppConstant.SKU_LOT_TOTAL_NUM; ++i) {
			Integer skuLotMust = skuLotVal.skuLotMustGet(i + 1);
			if (WmsAppConstant.SKU_LOT_MUST.equals(skuLotMust)) {
				propertyName = String.format("%s%d", WmsAppConstant.SKU_LOT_FIELD_PREFIX, i + 1);
				Property skuLotProperty = ReflectUtil.getProperty(skuLotObject.getClass(), propertyName);
				if (isEmpty(skuLotProperty, skuLotObject)) {
					throw new ServiceException(
						String.format("批属性校验失败,批属性%d不能为空", i + 1));
				}
			}
		}
	}

	private static <T> boolean isEmpty(Property skuLotProperty, T skuLotObject) {
		try {
			assert skuLotProperty.getReadMethod() != null;
			String t1SkuLot = (String) (skuLotProperty.getReadMethod().invoke(skuLotObject));
			if (Func.isNull(t1SkuLot)) {
				return true;
			}

			return Func.isEmpty(t1SkuLot.trim());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 如果sku lot不为null时，将会把该批属性追加到sql中
	 *
	 * @param queryWrapper
	 * @param skuLot
	 * @param <T>
	 * @param <R>
	 */
	public static <T, R> void applySql(LambdaQueryWrapper<R> queryWrapper, T skuLot) {
		String propertyName;
		boolean hasSkuLot = hasSkuLot(skuLot);
		if (Boolean.FALSE == hasSkuLot) {
			throw ExceptionUtil.mpe("参数：{}，批属性个数小于30个", skuLot);
		}
		try {
			for (int i = 0; i < WmsAppConstant.SKU_LOT_TOTAL_NUM; ++i) {
				propertyName = String.format("%s%d", WmsAppConstant.SKU_LOT_FIELD_PREFIX, i + 1);
				Property skuLotProperty = ReflectUtil.getProperty(skuLot.getClass(), propertyName);
				Object skuLotValue = skuLotProperty.getReadMethod().invoke(skuLot);
				if (Func.notNull(skuLotValue)) {
					queryWrapper.apply(String.format("sku_lot%d = '%s'", i + 1, skuLotValue));
				}
			}
		} catch (Exception e) {
			log.warn("applySql 异常", e);
		}
	}
}
