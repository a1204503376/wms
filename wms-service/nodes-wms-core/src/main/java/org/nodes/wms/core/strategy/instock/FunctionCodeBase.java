package org.nodes.wms.core.strategy.instock;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.dao.putaway.entities.StInstock;
import org.nodes.wms.dao.putaway.entities.StInstockDetail;
import org.nodes.wms.core.strategy.enums.LocSortModeEnum;
import org.nodes.wms.core.strategy.enums.LocSortTypeEnum;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: pengwei
 * date: 2021/5/27 14:21
 * description: BaseFunctionCode
 */
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class FunctionCodeBase implements IFunctionCode {

	protected IStockService stockService = SpringUtil.getBean(IStockService.class);

	@Override
	public void execute(Stock stock, StInstockDetail instockDetail, InstockExecuteVO instockExecute) {

	}

	protected Zone getSourceZone(Stock stock) {
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone zone = zoneService.getById(stock.getZoneId());
		if (Func.isEmpty(zone)) {
			throw new ServiceException("?????????????????????????????????");
		}
		return zone;
	}

	protected List<Zone> getTargetZone(Stock stock, StInstockDetail instockDetail) {
		// ??????????????????
		List<Zone> targetZoneList = new ArrayList<>();
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		if (Func.isNotEmpty(instockDetail.getToZoneType())) {
			/*List<Zone> zoneList = ZoneCache.listByWhId(stock.getWhId()).stream().filter(u -> {
				return instockDetail.getToZoneType().equals(u.getZoneType());
			}).collect(Collectors.toList());*/
			List<Zone> zoneList = zoneService.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getWhId, stock.getWhId())
				.eq(Zone::getZoneType, instockDetail.getToZoneType())
			);

			targetZoneList.addAll(zoneList);
		} else if (Func.isNotEmpty(instockDetail.getToZoneId())) {
			Zone zone = zoneService.getById(instockDetail.getToZoneId());
			targetZoneList.add(zone);
		}
		return targetZoneList;
	}

	protected List<Location> getTargetLocationList(Stock stock, StInstockDetail instockDetail,
												   InstockExecuteVO instockExecute) {
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		StInstock instock = instockService.getById(instockDetail.getSsiId());
		// ?????? ????????? ??????
		Zone sourceZone = this.getSourceZone(stock);
		// ?????? ????????? ??????
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		Location sourceLoc = LocationCache.getById(stock.getLocId());

		boolean equalZoneType = true;
		if (Func.isNotEmpty(instockDetail.getFromZoneType())) {
			equalZoneType = instockDetail.getFromZoneType().equals(sourceZone.getZoneType());
		}

		// ?????? ????????? ??????
		if (!(equalZoneType || sourceZone.getZoneId().equals(instockDetail.getFromZoneId()))) {
			throw new ServiceException(String.format("???????????????????????????????????????[%s]??????[%s]??????????????????????????????",
				instock.getSsiName(), instockDetail.getSsidProcOrder()));
		}
		// ??????????????????
		List<Zone> targetZoneList = this.getTargetZone(stock, instockDetail);
		instockExecute.setZoneCode(NodesUtil.join(targetZoneList, "zoneCode"));
		instockExecute.setZoneName(NodesUtil.join(targetZoneList, "zoneName"));

		// ?????? ??????????????? ?????????
		List<Location> locationList = new ArrayList<>();
		if (Func.isEmpty(sourceLoc) || Func.isEmpty(instockDetail.getToLocId())) {
			// ???????????????????????????????????????(????????? ????????????)
			Comparator<Location> locationComparator = Comparator.comparing(Location::getLogicAllocation);
			if (LocSortTypeEnum.LOC_CODE.equals(instockDetail.getLocSortType())) {
				// ?????????????????????
				locationComparator = Comparator.comparing(Location::getLocCode);
			}
			// ?????? ????????????
			if (LocSortModeEnum.DESC.equals(instockDetail.getLocSortMode())) {
				// ??????
				locationComparator = locationComparator.reversed();
			}
			// ?????? ????????? ??? ????????? ??????????????????????????? ????????? ??????????????????
			/*locationList = LocationCache.listByZoneId(
				NodesUtil.toList(targetZoneList, Zone::getZoneId)
			)*/
			locationList = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.in(Location::getZoneId, NodesUtil.toList(targetZoneList, Zone::getZoneId)))
				.stream()
				.sorted(locationComparator)
				.collect(Collectors.toList());
		} else {
			// ??? ????????? ???????????????
			locationList.add(LocationCache.getById(instockDetail.getToLocId()));
		}
		return locationList;
	}

	protected List<Location> filterLocation(List<Location> locationList, StInstockDetail instockDetail) {
		List<Stock> stockList = new ArrayList<>();
		if (Func.isNotEmpty(locationList)) {
			stockList = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.in(Stock::getLocId, NodesUtil.toList(locationList, Location::getLocId))
				.last("and stock_qty <> pick_qty"));
		}
		// ????????????????????????(??????????????????)
		List<Stock> finalStockList = stockList;
		return locationList.stream().filter(u -> {
			List<Stock> currentStockList = finalStockList.stream().filter(stock -> {
				return stock.getLocId().equals(u.getLocId());
			}).collect(Collectors.toList());
			if (!StringUtil.isEmpty(u.getLockFlag())) {
				// ??????
				return false;
			}
			if (Func.isNotEmpty(u.getCountBillNo())) {
				// ????????????
				return false;
			}
			// ??????
			if (instockDetail.getConfMixWight() != 0) {
				BigDecimal currentWeight = this.getWeight(currentStockList);
				if (BigDecimalUtil.gt(currentWeight, u.getLoadWeight())) {
					return false;
				}
			}
			// ??????
			if (instockDetail.getConfMixVolume() != 0) {
				BigDecimal currentVolume = this.getVolume(currentStockList);
				if (BigDecimalUtil.gt(currentVolume, u.getCapacity())) {
					return false;
				}
			}
			// ??????
//			if (instockDetail.getConfMixBoxCount() != 0) {
//				Integer currentBoxCount = this.getBoxCount(currentStockList);
//
//			}
			return true;
		}).collect(Collectors.toList());

	}

	/**
	 * ???????????????????????????
	 *
	 * @param stockList ??????-??????
	 * @return ????????????
	 */
	protected BigDecimal getWeight(List<Stock> stockList) {
		return stockList.stream().map(stock -> {
			BigDecimal qty = stock.getStockQty().subtract(stock.getPickQty());
			Sku sku = SkuCache.getById(stock.getSkuId());
			return sku.getSkuGrossWeight().multiply(qty);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * ???????????????????????????
	 *
	 * @param stockList ??????-??????
	 * @return ????????????
	 */
	protected BigDecimal getVolume(List<Stock> stockList) {
		return stockList.stream().map(stock -> {
			BigDecimal realQty = stock.getStockQty().subtract(stock.getPickQty());
			Sku sku = SkuCache.getById(stock.getSkuId());
			return sku.getSkuVolume().multiply(realQty);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * ???????????????????????????
	 *
	 * @param stockList ??????-??????
	 * @return ?????????????????????????????????
	 */
	protected Integer getBoxCount(List<Stock> stockList) {
		Integer boxCount = 0;
		for (Stock stock : stockList) {
			SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(stock.getWspId(), SkuLevelEnum.Box);
			if (Func.isEmpty(skuPackageDetail)) {
				continue;
			}
			BigDecimal stock_qty = stock.getStockQty().subtract(stock.getPickQty());
			BigDecimal convert_qty = new BigDecimal(skuPackageDetail.getConvertQty());
			if (stock_qty.intValue() % skuPackageDetail.getConvertQty() == 0) {
				boxCount += (stock_qty.divide(convert_qty)).intValue();
			} else {
				boxCount += (stock_qty.divide(convert_qty).intValue() + 1);
			}
		}
		return boxCount;
	}
}
