package org.nodes.wms.dao.basics.skuType;

import org.nodes.wms.dao.basics.skuType.entities.SkuType;

import java.util.List;

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

	/**
	 * 根据分类编码和货主id查询物品分类数据的总条数
	 *
	 * @param typeCode: 分类编码
	 * @param woId:     货主id
	 * @return int
	 */
    SkuType selectSkuTypeByCodeAndWoId(String typeCode, Long woId);

	/**
	 * 新增或修改物品分类
	 *
	 * @param skuType: 物品分类对象
	 * @return SkuType
	 */
	SkuType saveSkuType(SkuType skuType);

	/**
	 * 根据id修改物品分类
	 *
	 * @param skuType: 物品分类对象
	 */
	void updateByTypeId(SkuType skuType);

	/**
	 * 查询所有物品分类
	 *
	 * @return 物品分类
	 */
    List<SkuType> getAll();
}
