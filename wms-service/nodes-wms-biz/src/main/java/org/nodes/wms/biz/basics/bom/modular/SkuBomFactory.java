package org.nodes.wms.biz.basics.bom.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.dao.basics.bom.dto.input.SkuBomAddOrEditRequest;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.SkuUmDao;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

/**
 * 物料清单工厂类
 **/
@Service
@RequiredArgsConstructor
public class SkuBomFactory {

	private final OwnerBiz ownerBiz;

	private final SkuBiz skuBiz;


	public SkuBom createSkuBom(SkuBomAddOrEditRequest skuBomAddOrEditRequest) {
		SkuBom skuBom = new SkuBom();
		Func.copy(skuBomAddOrEditRequest, skuBom);

		// 根据货主id获取获取编码与名称
		if (Func.isNotEmpty(skuBom.getWoId())) {
			Owner owner = ownerBiz.findById(skuBom.getWoId());
			skuBom.setOwnerCode(owner.getOwnerCode());
			skuBom.setOwnerName(owner.getOwnerName());
		}
		// 根据主物品id获取物品编码与名称
		if (Func.isNotEmpty(skuBom.getSkuId())) {
			Sku sku = skuBiz.findById(skuBom.getSkuId());
			skuBom.setSkuCode(sku.getSkuCode());
			skuBom.setSkuName(sku.getSkuName());
			skuBom.setWsuName(sku.getWspName());
			skuBom.setWsuCode(sku.getWspName());

		}
		// 根据组合物品id获取物品编码与名称
		if (Func.isNotEmpty(skuBom.getJoinSkuId())) {
			Sku sku = skuBiz.findById(skuBom.getJoinSkuId());
			skuBom.setJoinSkuCode(sku.getSkuCode());
			skuBom.setJoinSkuName(sku.getSkuName());
			skuBom.setJoinWsuName(sku.getWspName());
			skuBom.setJoinWsuCode(sku.getWspName());
		}
		return skuBom;
	}

}
