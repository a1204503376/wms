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
}
