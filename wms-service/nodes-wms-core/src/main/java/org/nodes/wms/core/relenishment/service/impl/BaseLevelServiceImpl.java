package org.nodes.wms.core.relenishment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.enums.RelStateEnum;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Primary
public class BaseLevelServiceImpl extends AbsLevelServiceImpl {
	@Override
	public List<RelDetail>  execute(Stock stock, SkuInstock skuInstock, RelenishmentDetail relenishmentDetail) {
		List<RelDetail> relDetails = new ArrayList<>();
		RelDetail relDetail = new RelDetail();
		QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
		queryWrapper.lambda().eq(Stock::getWhId,stock.getWhId())
			.eq(Stock::getZoneId, relenishmentDetail.getFromZoneId())
			.eq(Stock::getSkuId,stock.getSkuId())
		.orderByDesc(Stock::getStockQty);
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++){
			if (stock.skuLotChk(i)) {
				queryWrapper.like("sku_lot" + i, stock.skuLotGet(i));
			}
		}
		List<Stock> stockList = stockService.list(queryWrapper);
		BigDecimal subtract = BigDecimal.valueOf(skuInstock.getHighStorage()).subtract(stock.getStockQty());
		relDetail.setToWhId(stock.getWhId());
		relDetail.setFromWhId(stock.getWhId());
		relDetail.setToZoneId(stock.getZoneId());
		relDetail.setToLocId(stock.getLocId());
		relDetail.setWspId(stock.getWspId());
		relDetail.setSkuLevel(SkuLevelEnum.Base.getIndex());
//		relDetail.setToLpnCode(stock.getLpnCode());
		SkuPackageDetail skuPackageDetail1 = SkuPackageDetailCache.listByWspId(stock.getWspId()).stream()
			.filter(skuPackageDetail -> skuPackageDetail.getSkuLevel().equals(relenishmentDetail.getSkuLevel()))
			.findFirst().orElse(null);
		relDetail.setUmCode(skuPackageDetail1.getWsuCode());
		relDetail.setUmName(skuPackageDetail1.getWsuName());
		relDetail.setSkuId(stock.getSkuId());
		Sku sku = SkuCache.getById(stock.getSkuId());
		if (Func.isNotEmpty(sku)){
			relDetail.setSkuCode(sku.getSkuCode());
			relDetail.setSkuName(sku.getSkuName());
		}
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++){
			if (stock.skuLotChk(i)) {
				relDetail.skuLotSet(i,stock.skuLotGet(i));
			}
		}
		relDetail.setWoId(stock.getWoId());
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(stock.getWoId());
		if(Func.isNotEmpty(owner)){
			relDetail.setWoCode(owner.getOwnerCode());
			relDetail.setWoName(owner.getOwnerName());
		}
		relDetail.setRelQty(subtract);
		relDetail.setRelStatus(RelStateEnum.UNEXECUTED.getIndex());
		if(Func.isEmpty(stockList)){
			relDetail.setRealQty(BigDecimal.ZERO);
			relDetails.add(relDetail);
			return relDetails;
		}
		relDetail.setRealQty(BigDecimal.ZERO);
		for(Stock stock1:stockList) {
			if (BigDecimalUtil.ge(stock1.getStockQty(), subtract)) {
				relDetail.setFromZoneId(stock1.getZoneId());
				relDetail.setFromLocId(stock1.getLocId());
//				relDetail.setFromLpnCode(stock1.getLpnCode());
				relDetail.setAttribute1("1");
//				relDetail.setRealQty(subtract);
				break;
			}
		}
		if(relDetail.getRealQty().equals(BigDecimal.ZERO)){
			BigDecimal sum = BigDecimal.ZERO;

			for(Stock stock1:stockList) {
				sum = sum.add(stock1.getStockQty());
				RelDetail relDetail1 = new RelDetail();
				Sku sku1 = SkuCache.getById(stock.getSkuId());
				relDetail1.setSkuCode(sku1.getSkuCode());
				relDetail1.setSkuName(sku1.getSkuName());
				relDetail1.setSkuLevel(SkuLevelEnum.Base.getIndex());
				Owner owner1 = ownerService.getById(stock.getWoId());
				if(Func.isNotEmpty(owner1)) {
					relDetail1.setWoCode(owner1.getOwnerCode());
					relDetail1.setWoName(owner1.getOwnerName());
				}
				relDetail1.setToWhId(stock.getWhId());
				relDetail1.setWspId(stock.getWspId());
				relDetail1.setFromWhId(stock.getWhId());
				relDetail1.setToZoneId(stock.getZoneId());
				relDetail1.setToLocId(stock.getLocId());
//				relDetail1.setToLpnCode(stock.getLpnCode());
				SkuPackageDetail skuPackageDetail2 = SkuPackageDetailCache.listByWspId(stock.getWspId()).stream()
					.filter(skuPackageDetail -> skuPackageDetail.getSkuLevel().equals(relenishmentDetail.getSkuLevel()))
					.findFirst().orElse(null);
				relDetail1.setUmCode(skuPackageDetail2.getWsuCode());
				relDetail1.setUmName(skuPackageDetail2.getWsuName());
				relDetail1.setSkuId(stock.getSkuId());
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++){
					if (stock.skuLotChk(i)) {
						relDetail1.skuLotSet(i,stock.skuLotGet(i));
					}
				}
				relDetail1.setWoId(stock.getWoId());
				relDetail1.setRelQty(subtract);
				relDetail1.setRelStatus(RelStateEnum.UNEXECUTED.getIndex());
				relDetail1.setFromZoneId(stock1.getZoneId());
				relDetail1.setFromLocId(stock1.getLocId());
//				relDetail1.setFromLpnCode(stock1.getLpnCode());
				relDetail1.setAttribute1("1");
				relDetail1.setRealQty(BigDecimal.ZERO);
				if(BigDecimalUtil.ge(sum,subtract)){
//					relDetail1.setRealQty(subtract.subtract(sum.subtract(stock1.getStockQty())));
					relDetails.add(relDetail1);
					break;
				}else{
					relDetails.add(relDetail1);
				}
			}
		}else{
			relDetails.add(relDetail);
		}
		return relDetails;
	}
}
