package org.nodes.core.tool.entity;

/**
 * @author pengwei
 * @program WmsCore
 * @description 批属性接口
 * @since 2020-08-06
 */
public interface ISkuLotBase {

	/**
	 * 根据编号获取对应的批属性值
	 *
	 * @param i 编号
	 * @return 批属性值
	 */
	String skuLotGet(int i);

	/**
	 * 检查指定编号的批属性值是否为空
	 *
	 * @param i 批属性编号
	 * @return 批属性值是否为空
	 */
	Boolean skuLotChk(int i);

	/**
	 * 通过索引存入批属性
	 *
	 * @param i
	 * @param skuLotValue
	 */
	void skuLotSet(int i, String skuLotValue);

	/**
	 * 比较批属性是否一致
	 *
	 * @param skuLotBase 批属性基类
	 * @return 批属性是否完全相等
	 */
	Boolean skuLotCompare(ISkuLotBase skuLotBase);
}
