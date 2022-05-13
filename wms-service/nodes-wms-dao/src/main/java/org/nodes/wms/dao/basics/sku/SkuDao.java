package org.nodes.wms.dao.basics.sku;

import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;

import java.util.List;

/**
 * 物品 DAO接口
 */
public interface SkuDao {

	/**
	 * 根据物品编码或者物品名称查询前10个物品信息
	 *
	 * @param skuCode 物品编码
	 * @param skuName 物品名称
	 * @return List<SkuSelectResponse>
	 */
	List<SkuSelectResponse> listTop10BySkuCodeSkuName(String skuCode, String skuName);

	/**
	 * 根据id查找物品信息
	 *
	 * @param skuId: 物品id
	 * @return Sku
	 */
    Sku getById(Long skuId);
}
