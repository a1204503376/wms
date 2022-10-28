package org.nodes.wms.dao.basics.sku.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.output.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物品 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-09
 */
@Repository("skuRepository")
public interface SkuMapper extends BaseMapper<Sku> {

	/**
	 * 根据物品id查询所有的计量单位
	 *
	 * @param skuId: 物品id
	 * @return List<SkuUmSelectResponse>
	 */
	List<SkuUmSelectResponse> listSkuUmBySkuId(@Param("skuId") Long skuId);

	/**
	 * 根据物品id查询包装和包装明细
	 *
	 * @param skuId: 物品id
	 * @return SkuPackageAggregate: 包装和包装明细聚合对象
	 */
	SkuPackageAggregate getSkuPackageAggregateBySkuId(@Param("skuId") Long skuId);

	SkuUm getSkuUmByUmCode(@Param("skuUmCode") String skuUmCode);

	SkuPackageDetail getBaseSkuPackageDetail(@Param("skuId") Long skuId);

	/**
	 * 根据包装id查询包装信息
	 *
	 * @param wspId: 包装id
	 * @return SkuPackage
	 */
	SkuPackage selectSkuPackageByWspId(@Param("wspId") Long wspId);

	/**
	 * 根据物料id和计量单位编码获取包装明细
	 *
	 * @param skuId   物料id
	 * @param wsuCode 计量单位编码
	 * @return 包装明细
	 */
	SkuPackageDetail getSkuPackageDetailBySkuId(@Param("skuId")Long skuId,@Param("wsuCode") String wsuCode);
}
