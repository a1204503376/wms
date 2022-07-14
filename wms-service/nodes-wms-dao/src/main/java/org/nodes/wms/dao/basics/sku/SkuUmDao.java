package org.nodes.wms.dao.basics.sku;

import org.nodes.wms.dao.basics.sku.entities.SkuUm;

/**
 * 计量单位 DAO接口
 */
public interface SkuUmDao {
	/**
	 * 根据计量单位id获取计量单位实体
	 * @param wsuId 计量单位id
	 * @return  SkuUm
	 */
	SkuUm getSkuUmById(Long wsuId);

	/**
	 * 查询计量单位编码是否存在
	 * @param wsuCode   计量单位编码
	 * @return
	 */
	boolean isExistUmCode(String wsuCode);

	/**
	 * 计量单位修改
	 * @param skuUm   计量单位实体
	 */
	void update(SkuUm skuUm);

	/**
	 * 计量单位保存
	 * @param skuUm 计量单位实体
	 */
	void insert(SkuUm skuUm);

	/**
	 * 根据编码获取数据条数
	 * @param umCode 计量单位编码
	 * @return 数据条数
	 */
	int countByCode(String umCode);
}
