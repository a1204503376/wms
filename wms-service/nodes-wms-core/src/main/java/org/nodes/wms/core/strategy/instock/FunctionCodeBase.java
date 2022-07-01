package org.nodes.wms.core.strategy.instock;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.dao.putway.entities.Instock;
import org.nodes.wms.core.strategy.entity.InstockDetail;
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
	public void execute(Stock stock, InstockDetail instockDetail, InstockExecuteVO instockExecute) {

	}

	protected Zone getSourceZone(Stock stock) {
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone zone = zoneService.getById(stock.getZoneId());
		if (Func.isEmpty(zone)) {
			throw new ServiceException("从库区不存在或已删除！");
		}
		return zone;
	}

	protected List<Zone> getTargetZone(Stock stock, InstockDetail instockDetail) {
		// 目标库区列表
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

	protected List<Location> getTargetLocationList(Stock stock, InstockDetail instockDetail,
												   InstockExecuteVO instockExecute) {
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		Instock instock = instockService.getById(instockDetail.getSsiId());
		// 获取 从库区 信息
		Zone sourceZone = this.getSourceZone(stock);
		// 获取 从库位 信息
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		Location sourceLoc = LocationCache.getById(stock.getLocId());

		boolean equalZoneType = true;
		if (Func.isNotEmpty(instockDetail.getFromZoneType())) {
			equalZoneType = instockDetail.getFromZoneType().equals(sourceZone.getZoneType());
		}

		// 获取 至库位 信息
		if (!(equalZoneType || sourceZone.getZoneId().equals(instockDetail.getFromZoneId()))) {
			throw new ServiceException(String.format("库存物品所在库区与上架策略[%s]的第[%s]个策略的从库区不一致",
				instock.getSsiName(), instockDetail.getSsidProcOrder()));
		}
		// 目标库区列表
		List<Zone> targetZoneList = this.getTargetZone(stock, instockDetail);
		instockExecute.setZoneCode(NodesUtil.join(targetZoneList, "zoneCode"));
		instockExecute.setZoneName(NodesUtil.join(targetZoneList, "zoneName"));

		// 获取 满足条件的 至库位
		List<Location> locationList = new ArrayList<>();
		if (Func.isEmpty(sourceLoc) || Func.isEmpty(instockDetail.getToLocId())) {
			// 确认排序的字段、排序的方式(默认按 路线顺序)
			Comparator<Location> locationComparator = Comparator.comparing(Location::getLogicAllocation);
			if (LocSortTypeEnum.LOC_CODE.equals(instockDetail.getLocSortType())) {
				// 按库位编码排序
				locationComparator = Comparator.comparing(Location::getLocCode);
			}
			// 确认 排序方式
			if (LocSortModeEnum.DESC.equals(instockDetail.getLocSortMode())) {
				// 降序
				locationComparator = locationComparator.reversed();
			}
			// 如果 从库位 与 至库位 都为空的情况下，从 至库区 获取所有库位
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
			// 将 至库位 放到集合中
			locationList.add(LocationCache.getById(instockDetail.getToLocId()));
		}
		return locationList;
	}

	protected List<Location> filterLocation(List<Location> locationList, InstockDetail instockDetail) {
		List<Stock> stockList = new ArrayList<>();
		if (Func.isNotEmpty(locationList)) {
			stockList = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.in(Stock::getLocId, NodesUtil.toList(locationList, Location::getLocId))
				.last("and stock_qty <> pick_qty"));
		}
		// 筛选出可用的库位(禁用的库位等)
		List<Stock> finalStockList = stockList;
		return locationList.stream().filter(u -> {
			List<Stock> currentStockList = finalStockList.stream().filter(stock -> {
				return stock.getLocId().equals(u.getLocId());
			}).collect(Collectors.toList());
			if (!StringUtil.isEmpty(u.getLockFlag())) {
				// 锁定
				return false;
			}
			if (Func.isNotEmpty(u.getCountBillNo())) {
				// 盘点占用
				return false;
			}
			// 重量
			if (instockDetail.getConfMixWight() != 0) {
				BigDecimal currentWeight = this.getWeight(currentStockList);
				if (BigDecimalUtil.gt(currentWeight, u.getLoadWeight())) {
					return false;
				}
			}
			// 容积
			if (instockDetail.getConfMixVolume() != 0) {
				BigDecimal currentVolume = this.getVolume(currentStockList);
				if (BigDecimalUtil.gt(currentVolume, u.getCapacity())) {
					return false;
				}
			}
			// 箱数
//			if (instockDetail.getConfMixBoxCount() != 0) {
//				Integer currentBoxCount = this.getBoxCount(currentStockList);
//
//			}
			return true;
		}).collect(Collectors.toList());

	}

	/**
	 * 计算库存信息总重量
	 *
	 * @param stockList 库存-列表
	 * @return 占用重量
	 */
	protected BigDecimal getWeight(List<Stock> stockList) {
		return stockList.stream().map(stock -> {
			BigDecimal qty = stock.getStockQty().subtract(stock.getPickQty());
			Sku sku = SkuCache.getById(stock.getSkuId());
			return sku.getSkuGrossWeight().multiply(qty);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 计算库存信息总体积
	 *
	 * @param stockList 库存-列表
	 * @return 占用体积
	 */
	protected BigDecimal getVolume(List<Stock> stockList) {
		return stockList.stream().map(stock -> {
			BigDecimal realQty = stock.getStockQty().subtract(stock.getPickQty());
			Sku sku = SkuCache.getById(stock.getSkuId());
			return sku.getSkuVolume().multiply(realQty);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 计算库存信息总箱数
	 *
	 * @param stockList 库存-列表
	 * @return 总箱数（半箱按整箱算）
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
