package org.nodes.wms.dao.basics.sku;

import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.output.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.*;

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
	SkuPackageAggregate getSkuPackageAggregateBySkuId(Long skuId);

	/**
	 * 根据计量单位编码查询计量单位实体
	 *
	 * @param skuUmCode:计量单位编码
	 * @return SkuUm
	 */
	SkuUm getSkuUmByUmCode(String skuUmCode);

	SkuPackageDetail getBaseSkuPackageDetail(Long skuId);

	/**
	 * 根据物品编码查询物品实体
	 *
	 * @param skuCode 物品编码
	 */
	Sku getSkuByCode(String skuCode);

	/**
	 * 新增或修改物品信息
	 *
	 * @param sku: 物品对象
	 * @return void
	 */
	void saveSku(Sku sku);

	/**
	 * 根据包装id获取包装信息
	 *
	 * @param wspId: 包装id
	 * @return SkuPackage
	 */
	SkuPackage getSkuPackageByWspId(Long wspId);

	/**
	 * 获取sku的List集合
	 *
	 * @return sku的List集合
	 */
	List<Sku> getSkuList();

	/**
	 * 根据编码查询数据条数
	 *
	 * @param skuCode 物料编码
	 */
	int countByCode(String skuCode);

	/**
	 * 根据物料id和计量单位编码获取
	 * @param skuId 物料id
	 * @param wsuCode 计量单位编码
	 * @return 包装明细
	 */
	SkuPackageDetail getSkuPackageDetailBySkuId(Long skuId, String wsuCode);
}
