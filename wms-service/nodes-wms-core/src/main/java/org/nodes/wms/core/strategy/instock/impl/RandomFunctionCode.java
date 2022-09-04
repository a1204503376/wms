package org.nodes.wms.core.strategy.instock.impl;

import org.nodes.core.base.cache.ParamCache;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.putaway.entities.StInstock;
import org.nodes.wms.dao.putaway.entities.StInstockDetail;
import org.nodes.wms.core.strategy.enums.MixModeEnum;
import org.nodes.wms.core.strategy.instock.FunctionCodeBase;
import org.nodes.wms.core.strategy.instock.IFunctionCode;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * author: pengwei
 * date: 2021/5/26 10:50
 * description: RandomFunctionCode
 */
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RandomFunctionCode extends FunctionCodeBase implements IFunctionCode {

	@Override
	public void execute(Stock stock, StInstockDetail instockDetail, InstockExecuteVO instockExecute) {

		List<Location> locationList = super.getTargetLocationList(stock, instockDetail, instockExecute);
		// 筛选出可用的库位(禁用的库位等)
		locationList = super.filterLocation(locationList, instockDetail);
		// 找到 至库位 信息后，遍历找到满足条件的库位
		Location targetLoc = null;
		for (Location location : locationList) {
			if (Func.isNotEmpty(targetLoc)) {
				break;
			}
			// 找到当前库位的库存信息(暂时就找第一条库存)
			List<Stock> findStockList = super.stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
				.eq(Stock::getLocId, location.getLocId()));
			if (Func.isEmpty(findStockList)) {
				// 如果库存为空的情况下，表示该库位就可以用
				targetLoc = location;
				break;
			}
			// 有库存的情况下
			// 1. 判断是否允许混放货品
			if (MixModeEnum.UN_ALLOW.equals(location.getLocSkuMix())) {
				// 从库存里筛选出其他物品的库存信息
				List<Stock> otherSkuStockList = findStockList.stream().filter(u -> {
					return !u.getSkuId().equals(stock.getSkuId());
				}).collect(Collectors.toList());
				if (otherSkuStockList.size() > 0) {
					// 不允许混放货品，并且该库位上已有其他物品库存的情况下, 直接跳过
					continue;
				}
			}
			// 2. 判断是否允许混放批号
			if (MixModeEnum.UN_ALLOW.equals(location.getLocLotNoMix())) {
				// 从库存里筛选出其他批次号的库存信息
				List<Stock> otherLotStockList = findStockList.stream().filter(u -> {
					return !u.getLotNumber().equals(stock.getLotNumber());
				}).collect(Collectors.toList());
				if (otherLotStockList.size() > 0) {
					// 不允许混放批号，并且改库位上已有其他批次库存的情况下，直接跳过
					continue;
				}
			}
			// 3. 判断批属性是否允许混放
			boolean mixResult = false;   // 混放结果
			for (Stock stockItem : findStockList) {
				// 判断批属性是否匹配
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					String locMix = location.getLocLotMix(i);            // 库位的混放规则
					String instockMix = instockDetail.getSkuLotMix(i);    // 策略明细的混放规则
					String stockLot = stock.skuLotGet(i);                // 源库存批属性
					String stockItemLot = stockItem.skuLotGet(i);        // 目标库存的批属性
					// 确认混放规则(优先匹配库位的混放规则，未设置库位混放规则的情况下用策略的混放规则)
					String mixMode = Func.isBlank(locMix) ? instockMix : locMix;
					// 允许混放的情况下，就不需要对比批属性了
					if (!MixModeEnum.UN_ALLOW.getIndex().equals(mixMode)) {
						mixResult = true;
						continue;
					}
					// 批属性都为空格，或者为空的情况下，直接跳过
					if (Func.isBlank(stockLot) && Func.isBlank(stockItemLot)) {
						mixResult = true;
						continue;
					}
					// 不允许混放的情况下，需要比对批属性是否一致
					if (Func.isNotEmpty(stockLot) && stockLot.equals(stockItemLot)) {
						mixResult = false;
						break;
					}
				}
				// 如果 混放结果 = false 表示这个库位的库存都没有用了，继续循环下去也没有意义了，所以：结束
//				if (mixResult) {
					targetLoc = location;
//				}
				break;
			}
		}
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		StInstock instock = instockService.getById(instockDetail.getSsiId());
		Zone sourceZone = super.getSourceZone(stock);
		Location sourceLoc = LocationCache.getById(stock.getLocId());
//		List<Zone> targetZoneList = super.getTargetZone(stock, instockDetail);

		String msg = String.format(
			"上架策略[%s]的第[%s]个策略执行成功", instock.getSsiName(), instockDetail.getSsidProcOrder());
		if (Func.isNotEmpty(targetLoc)) {
			instockExecute.setTargetLocId(targetLoc.getLocId());
			instockExecute.setTargetLocCode(targetLoc.getLocCode());
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone targetZone = zoneService.getById(targetLoc.getZoneId());
			if (Func.isNotEmpty(targetZone)) {
				instockExecute.setZoneCode(targetZone.getZoneCode());
				instockExecute.setZoneName(targetZone.getZoneName());
				msg += String.format("上架策略[%s]的第[%s]个策略执行成功, 推荐从库区[%s]上架到库区[%s]",
					sourceZone.getZoneName(), targetZone.getZoneName());
			}
			msg += String.format(", 推荐从库位[%s]上架到至库位[%s]",
				sourceLoc.getLocCode(), targetLoc.getLocCode());
		}
		instockExecute.setMsg(msg);
	}
}
