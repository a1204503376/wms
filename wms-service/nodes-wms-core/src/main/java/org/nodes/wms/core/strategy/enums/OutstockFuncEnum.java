package org.nodes.wms.core.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author pengwei
 * @program WmsCore
 * @description 分配策略计算代码枚举
 * @since 2020-10-29
 */
@Getter
@AllArgsConstructor
public enum OutstockFuncEnum {
	/**
	 * 未知
	 */
	UN_KNOWN(0, "未知"),
	/**
	 * 清库位优先
	 */
	CLEAR_LOCATION(1, "清库位优先"),
	/**
	 * 足量优先
	 */
	FULL_DOSE(2, "足量优先")
	;

	final Integer key;

	final String name;

	/**
	 * 将 Integer 值转换为枚举
	 *
	 * @param key 枚举对应的Integer值
	 * @return 分配策略计算代码枚举
	 */
	public static OutstockFuncEnum parse(Integer key) {

		return Arrays.asList(OutstockFuncEnum.values()).stream().filter(u->{
			return u.getKey().equals(key);
		}).findFirst().orElse(null);
	}
}
