package org.nodes.wms.biz.basics.sku;

import org.nodes.wms.dao.basics.sku.dto.input.SkuAddOrEditRequest;
import org.nodes.wms.dao.basics.sku.dto.input.SkuSelectQuery;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.output.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.*;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;

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
	 * 根据物品编码查询物品实 体
	 * @param skuCode 物品编码
	 * @return Sku 物品实体
	 */
	Sku findByCode(String skuCode);

	/**
	 * 根据物品分类id获取物品分类实体
	 * @param skuTypeId 物品分类id
	 * @return SkuType
	 */
	SkuType findSkuTypeById(Long skuTypeId);

	/**
	 * 根据物品分类编码获取物品分类实体
	 * @param typeCode 物品分类编码
	 * @return SkuType
	 */
    SkuType findSkuTypeByCode(String typeCode);
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

	/**
	 * 根据计量单位id获取计量单位实体
	 * @param wsuId 计量单位id
	 * @return SkuUm
	 */
	SkuUm findSkuUmById( Long wsuId);

	/**
	 * 根据物料获取基础包装信息
	 * @param skuId 物料id
	 * @return SkuPackageDetail
	 */
	SkuPackageDetail findBaseSkuPackageDetail(Long skuId);

	/**
	 * 新增或编辑物品
	 *
	 * @param skuAddOrEditRequest: 物品新增或编辑dto对象
	 * @return Sku
	 */
	Sku save(SkuAddOrEditRequest skuAddOrEditRequest);

	/**
	 * 根据包装id获取包装信息
	 *
	 * @param wspId: 包装id
	 * @return SkuPackage
	 */
	SkuPackage findSkuPackageByWspId(Long wspId);
}
