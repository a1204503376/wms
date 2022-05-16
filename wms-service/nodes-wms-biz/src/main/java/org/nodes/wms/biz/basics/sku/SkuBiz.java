package org.nodes.wms.biz.basics.sku;

import org.nodes.wms.dao.basics.sku.dto.SkuSelectQuery;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;

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
	 * @param skuId: 物品id
	 * @return List<SkuUmSelectResponse>
	 */
    List<SkuUmSelectResponse> findSkuUmSelectResponseListBySkuId(Long skuId);

	/**
	 * 根据物品id查询包装明细
	 *
	 * @param skuId: 物品id
	 * @return List<SkuPackageDetailResponse>
	 */
	SkuPackageAggregate findSkuPackageAggregateBySkuId(Long skuId);

	/**
	 * 根据计量单位编码查询计量单位实体
	 * @param skuUmCode:计量单位
	 * @return SkuUm
	 */
	SkuUm findSkuUmByUmCode(String skuUmCode);
}
