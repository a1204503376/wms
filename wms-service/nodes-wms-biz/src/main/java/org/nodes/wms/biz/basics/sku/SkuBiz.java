package org.nodes.wms.biz.basics.sku;

import org.nodes.wms.dao.basics.sku.dto.*;
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

	/**
	 * 根据物品id查询所有计量单位，基础计量单位放第一个
	 *
	 * @param skuUmSelectQuery: 物品id
	 * @return void
	 */
    List<SkuUmSelectResponse> getSkuUmSelectResponseListBySkuId(SkuUmSelectQuery skuUmSelectQuery);

	/**
	 * 根据物品id查询包装明细
	 *
	 * @param skuPackageDetailQuery: 物品id
	 * @return List<SkuPackageDetailResponse>
	 */
	List<SkuPackageDetailResponse> getSkuPackDetailListBySkuId(SkuPackageDetailQuery skuPackageDetailQuery);
}
