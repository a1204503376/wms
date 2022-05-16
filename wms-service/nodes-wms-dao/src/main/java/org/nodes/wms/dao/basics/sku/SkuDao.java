package org.nodes.wms.dao.basics.sku;

import org.nodes.wms.dao.basics.sku.dto.SkuPackageDetailResponse;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.SkuUmSelectResponse;
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

	/**
	 * 根据物品id查询所有计量单位，基础计量单位放第一个
	 *
	 * @param skuId: 物品id
	 * @return List<SkuUmSelectResponse>
	 */
    List<SkuUmSelectResponse> listSkuUmBySkuId(Long skuId);

	/**
	 * 根据物品id查询包装明细
	 *
	 * @param skuId: 物品id
	 * @return List<SkuPackageDetailResponse>
	 */
	List<SkuPackageDetailResponse> listSkuPackDetailBySkuId(Long skuId);
}
