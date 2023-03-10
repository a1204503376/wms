package org.nodes.wms.core.relenishment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.enums.RelStateEnum;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Primary
public class BoxLevelServiceImpl extends AbsLevelServiceImpl {
	@Override
	public List<RelDetail>  execute(Stock stock, SkuInstock skuInstock, RelenishmentDetail relenishmentDetail) {
		List<RelDetail> relDetails = new ArrayList<>();
		RelDetail relDetail = new RelDetail();
		QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
		queryWrapper.lambda().eq(Stock::getWhId,stock.getWhId())
			.eq(Stock::getZoneId, relenishmentDetail.getFromZoneId())
//			.ne(Stock::getBoxCode,"")
//			.isNotNull(Stock::getBoxCode)
			.eq(Stock::getPickQty,0)
			.eq(Stock::getSkuId,stock.getSkuId());
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++){
			if (stock.skuLotChk(i)) {
				queryWrapper.like("sku_lot" + i, stock.skuLotGet(i));
			}
		}
		List<Stock> stockList = stockService.list(queryWrapper);
		BigDecimal subtract = BigDecimal.valueOf(skuInstock.getHighStorage()).subtract(stock.getStockQty());
		SkuPackageDetail skuPackageDetail1 = SkuPackageDetailCache.listByWspId(stock.getWspId()).stream()
			.filter(skuPackageDetail -> skuPackageDetail.getSkuLevel().equals(relenishmentDetail.getSkuLevel()))
			.findFirst().orElse(null);
		relDetail.setUmCode(skuPackageDetail1.getWsuCode());
		relDetail.setUmName(skuPackageDetail1.getWsuName());
		Sku sku = SkuCache.getById(stock.getSkuId());
		if (Func.isNotEmpty(sku)){
			relDetail.setSkuCode(sku.getSkuCode());
			relDetail.setSkuName(sku.getSkuName());
		}
		relDetail.setToWhId(stock.getWhId());
		relDetail.setSkuLevel(SkuLevelEnum.Box.getIndex());
		relDetail.setToZoneId(stock.getZoneId());
		relDetail.setToLocId(stock.getLocId());
		relDetail.setWspId(stock.getWspId());
//		relDetail.setToLpnCode(stock.getLpnCode());
		relDetail.setFromWhId(stock.getWhId());
		relDetail.setSkuId(stock.getSkuId());
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++){
			if (stock.skuLotChk(i)) {
				relDetail.skuLotSet(i,stock.skuLotGet(i));
			}
		}
		relDetail.setWoId(stock.getWoId());
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(stock.getWoId());
		if(Func.isNotEmpty(owner)) {
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
			if (BigDecimalUtil.gt(stock1.getStockQty(), subtract)) {
				relDetail.setFromZoneId(stock1.getZoneId());
				relDetail.setFromLocId(stock1.getLocId());
//				relDetail.setFromLpnCode(stock1.getLpnCode());
				relDetail.setAttribute1("1");
//				relDetail.setRealQty(stock1.getStockQty());
				break;
			}
		}
		relDetails.add(relDetail);
		return relDetails;
	}
}
