package org.nodes.core.tool.utils;

import org.springblade.core.tool.utils.CharPool;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author pengwei
 * @program WmsCore
 * @description BigDecimal工具类
 * @since 2020-10-28
 */
public class BigDecimalUtil {

	/**
	 * 全局设定：对于小数点保留位数（BigDecimalSerializer, JsonUtil）
	 *
	 * @param value
	 * @return
	 */
	public static BigDecimal globeScale(BigDecimal value) {
		if (Func.isEmpty(value)) {
			return null;
		}
		// 去除小数点后面没有用的 0
		int scale = 6;
		if (value.scale() <= 0) {
			scale = 0;
		} else {
			String valueStr = value.toString();
			for (int i = 0; i <= value.scale(); i++) {
				if (valueStr.charAt(valueStr.length() - i - 1) != CharPool.ZERO) {
					scale = value.scale() - i;
					break;
				}
			}
		}
		return value.setScale(scale, RoundingMode.HALF_UP);
//		return value.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros();
	}

	/**
	 * 比较两个 BigDecimal 值是否相等，都为 Null 时返回 true
	 *
	 * @param number1 第一个 BigDecimal 数量
	 * @param number2 第二个 BigDecimal 数量
	 * @return 是否相等
	 */
	public static boolean eq(BigDecimal number1, BigDecimal number2) {
		if (Func.isEmpty(number1) && Func.isEmpty(number2)) {
			return true;
		}
		if (Func.isEmpty(number1) || Func.isEmpty(number2)) {
			return false;
		}
		return number1.compareTo(number2) == 0;
	}

	/**
	 * 比较两个 BigDecimal 值是否不相等
	 *
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static boolean ne(BigDecimal number1, BigDecimal number2) {
		return !eq(number1, number2);
	}

	/**
	 * 判断 number1 是否大于且等于 number2
	 *
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static boolean ge(BigDecimal number1, BigDecimal number2) {
		if (Func.isEmpty(number1) || Func.isEmpty(number2)) {
			return false;
		}

		return number1.compareTo(number2) > -1;
	}

	/**
	 * 判断 number1 是否大于 number2
	 *
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static boolean gt(BigDecimal number1, BigDecimal number2) {
		if (Func.isEmpty(number1) || Func.isEmpty(number2)) {
			return false;
		}

		return number1.compareTo(number2) == 1;
	}

	/**
	 * 判断 number1 是否小于 number2
	 *
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static boolean lt(BigDecimal number1, BigDecimal number2) {
		if (Func.isEmpty(number1) || Func.isEmpty(number2)) {
			return false;
		}

		return number1.compareTo(number2) == -1;
	}

	/**
	 * 判断 number1 是否小于等于 number2
	 *
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static boolean le(BigDecimal number1, BigDecimal number2) {
		if (Func.isEmpty(number1) || Func.isEmpty(number2)) {
			return false;
		}

		return number1.compareTo(number2) < 1;
	}
}
