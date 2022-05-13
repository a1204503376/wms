package org.nodes.wms.biz.basics.sku;

import org.nodes.wms.dao.basics.sku.dto.SkuSelectQuery;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;

import java.util.List;

/**
 * 物品 业务层接口
 */
public interface SkuBiz {

	/**
	 * 根据关键词获取最近10个物品信息
	 * 根据修改时间倒序
	 *
	 * @param skuSelectQuery 关键词对象
	 * @return List<SkuSelectResponse>
	 */
	List<SkuSelectResponse> getSkuSelectResponseTop10List(SkuSelectQuery skuSelectQuery);

	/**
	 * 根据id查询物品信息
	 *
	 * @param skuId: 物品id
	 * @return Sku
	 */
	Sku findById(Long skuId);
}
