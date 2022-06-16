package org.nodes.wms.core.basedata.service.impl;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.basedata.service.*;
import org.nodes.wms.core.basedata.mapper.SkuBomMapper;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 物料清单 服务实现类
 *
 * @author NodeX
 * @since 2021-05-19
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuBomServiceImpl<M extends SkuMapper, T extends Sku>
	extends BaseServiceImpl<SkuBomMapper, SkuBom>
	implements ISkuBomService<T> {

	@Override
	public boolean  saveOUpdate(SkuBom skuBom) {
		//货主
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(skuBom.getWoId());
		skuBom.setOwnerCode(owner.getOwnerCode());
		skuBom.setOwnerName(owner.getOwnerName());
		//物品
		Sku sku = SkuCache.getById(skuBom.getSkuId());
		skuBom.setSkuCode(sku.getSkuCode());
		skuBom.setSkuName(sku.getSkuName());
		//组合物品
		Sku joinSku = SkuCache.getById(skuBom.getJoinSkuId());
		skuBom.setJoinSkuCode(joinSku.getSkuCode());
		skuBom.setJoinSkuName(joinSku.getSkuName());
		//包装
		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(sku.getWspId(), 1);
		skuBom.setWsuCode(skuPackageDetail.getWsuCode());
		skuBom.setWsuName(skuPackageDetail.getWsuName());
		skuBom.setJoinWsuCode(skuPackageDetail.getWsuCode());
		skuBom.setJoinWsuName(skuPackageDetail.getWsuName());

		return super.saveOrUpdate(skuBom);
	}
}

