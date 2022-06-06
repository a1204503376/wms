package org.nodes.wms.dao.basics.sku;

import org.nodes.wms.dao.basics.sku.entities.SkuType;

/**
 * 物品分类 DAO接口
 */
public interface SkuTypeDao {
	/**
	 * 根据物品分类id获取物品分类实体
	 * @param skuTypeId 物品分类id
	 * @return SkuType
	 */
	SkuType getSkuTypeById(Long skuTypeId);

	/**
	 * 根据物品分类编码获取物品分类实体
	 * @param typeCode 物品分类编码
	 * @return  SkuType
	 */
	SkuType getSkuTypeByCode(String typeCode);
}
