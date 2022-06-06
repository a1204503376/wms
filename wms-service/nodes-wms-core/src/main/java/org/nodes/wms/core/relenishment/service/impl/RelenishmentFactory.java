package org.nodes.wms.core.relenishment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.nodes.wms.core.relenishment.service.IFunctionCode;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class RelenishmentFactory {


	public static RelenishmentFactory instance;

	public static RelenishmentFactory getInstance() {
		if (instance == null) {
			instance = new RelenishmentFactory();
		}
		return instance;
	}

	/**
	 * 根据层级创建策略代码执行对象
	 *
	 */
	public IFunctionCode create(Integer skuLevel) {
		if(skuLevel.equals(SkuLevelEnum.Base.getIndex())){
			return new BaseLevelServiceImpl();
		}else if(skuLevel.equals(SkuLevelEnum.Inner.getIndex())){
			return new InnerLevelServiceImpl();
		}else if(skuLevel.equals(SkuLevelEnum.Box.getIndex())){
			return new BoxLevelServiceImpl();
		}else if(skuLevel.equals(SkuLevelEnum.Plate.getIndex())){
			return new PlateLevelServiceImpl();
		}
		return null;
	}

	/**
	 * 根据层级创建策略代码执行对象
	 *
	 */
	public List<RelDetail> create1(Stock stock, SkuInstock skuInstock, List<Long> fromZoneIds) {
		List<RelDetail> relDetails = new ArrayList<>();
		List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(stock.getWspId()).stream()
			.sorted(Comparator.comparing(SkuPackageDetail::getSkuLevel).reversed()).collect(Collectors.toList());
		BigDecimal lowStorage = new BigDecimal(skuInstock.getLowStorage());
		BigDecimal highStorage = new BigDecimal(skuInstock.getHighStorage());
		IStockService stockService = SpringUtil.getBean(IStockService.class);
		List<Stock> fromStocks = stockService.list(Wrappers.lambdaQuery(Stock.class).in(Stock::getZoneId, fromZoneIds)
			.eq(Stock::getSkuId, stock.getSkuId()).orderByDesc(Stock::getStockQty));

		outer:for(Stock fromStock:fromStocks){
			for(SkuPackageDetail skuPackageDetail:skuPackageDetails){
				 BigDecimal convertQty = new BigDecimal(skuPackageDetail.getConvertQty());
				 BigDecimal[] bigDecimals = fromStock.getStockQty().divideAndRemainder(convertQty);
				BigDecimal relqty = bigDecimals[0].multiply(convertQty);
				if(BigDecimalUtil.eq(relqty,BigDecimal.ZERO)
					||(BigDecimalUtil.ne(relqty,BigDecimal.ZERO)
					&&BigDecimalUtil.gt(relqty,highStorage))){
				 	if(BigDecimalUtil.ne(relqty,BigDecimal.ZERO)
						&&BigDecimalUtil.gt(relqty,highStorage)){
						BigDecimal[] bigDecimals1 = relqty.subtract(relqty.subtract(highStorage)).divideAndRemainder(convertQty);
						if(BigDecimalUtil.ge(bigDecimals1[0].multiply(convertQty),lowStorage)){
							RelDetail relDetail = createRelDetail(stock, fromStock, skuPackageDetail, bigDecimals1[0]);
							relDetails.add(relDetail);
							break outer;
						}
					}else{
						continue;
					}
				}else{
				 	if(BigDecimalUtil.ge(relqty,lowStorage)){
						RelDetail relDetail = createRelDetail(stock, fromStock, skuPackageDetail, bigDecimals[0]);
						relDetails.add(relDetail);
						break outer;
					}
				}
			}
		}
		if(Func.isEmpty(relDetails)){
			SkuPackageDetail one = SkuPackageDetailCache.getOne(stock.getWspId(), SkuLevelEnum.Base);
			RelDetail relDetail = createRelDetail(stock, null, one, BigDecimal.ZERO);
			relDetails.add(relDetail);
		}
		return relDetails;
	}

	public RelDetail createRelDetail(Stock stock,Stock fromStock,SkuPackageDetail skuPackageDetail,BigDecimal qty){
		RelDetail relDetail=new RelDetail();
		relDetail.setToWhId(stock.getWhId());
		relDetail.setFromWhId(stock.getWhId());
		relDetail.setToZoneId(stock.getZoneId());
		relDetail.setToLocId(stock.getLocId());
		relDetail.setWspId(stock.getWspId());
		relDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
//		relDetail.setToLpnCode(stock.getLpnCode());
		relDetail.setUmCode(skuPackageDetail.getWsuCode());
		relDetail.setUmName(skuPackageDetail.getWsuName());
		relDetail.setConvertQty(skuPackageDetail.getConvertQty());
		relDetail.setSkuId(stock.getSkuId());
		Sku sku = SkuCache.getById(stock.getSkuId());
		relDetail.setSkuCode(sku.getSkuCode());
		relDetail.setSkuName(sku.getSkuName());
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
		relDetail.setRelQty(qty);
		relDetail.setRelStatus(RelStateEnum.UNEXECUTED.getIndex());
		relDetail.setRealQty(BigDecimal.ZERO);
		if(Func.isNotEmpty(fromStock)){
			relDetail.setFromZoneId(fromStock.getZoneId());
			relDetail.setFromLocId(fromStock.getLocId());
//			relDetail.setFromLpnCode(fromStock.getLpnCode());
			relDetail.setStockId(fromStock.getStockId());
			relDetail.setAttribute1("1");
		}
		return relDetail;
	}
}
