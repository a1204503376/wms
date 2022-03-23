package org.nodes.wms.core.count.service.impl;

import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.enums.CountByEnum;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PointToCountTypeQuery extends CountTypeQuery {
	private List<Location> myLocation = new ArrayList<>();
	private List<Stock> stockList = new ArrayList<>();

	@Override
	public List<CountHeaderVO> queryByCountType() {
		super.queryByCountType();
		if (countByEnum == CountByEnum.LOCATION) {
			countHeaderVOList = countHeaderVOList.stream().sorted(
				Comparator.comparing(CountHeaderVO::getLocCode))
				.collect(Collectors.toList());
		}
		return countHeaderVOList;
	}

	@Override
	protected void queryByLocation() {
		common();
		if (Func.isNotEmpty(countHeader.getChecked()) && countHeader.getChecked()) {
			if (myLocation.size() > 0) {
				Iterator<Location> iterator = myLocation.iterator();
				while (iterator.hasNext()) {
					Location location = iterator.next();
					if (location.getLocStatus() != 1) {
						iterator.remove();
					}
				}
			}
		}
		if (Func.isNotEmpty(locationList)) {
			List<Long> locIds = NodesUtil.toList(locationList, Location::getLocId);
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailByLocCodes(locIds);
			myLocation.forEach(location -> {
				CountHeaderVO countHeaderVO = new CountHeaderVO();
				countHeaderVO.setWhName(warehouse.getWhName());
				countHeaderVO.setLocCode(location.getLocCode());
				countHeaderVO.setLocId(location.getLocId());
				countHeaderVO.setZoneId(countHeader.getZoneId());
				if (Func.isNotEmpty(countDetails)) {
					List<CountDetail> collect = countDetails.stream().filter(countDetail -> countDetail.getLocId().equals(location.getLocId()))
						.collect(Collectors.toList());
					countHeaderVO.setCountBillNo(NodesUtil.join(collect, "countBillNo"));
				}
				countHeaderVO.setLocState(location.getLocStatus());
				countHeaderVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, location.getLocStatus()));
				countHeaderVO.setLastLocCountDate(location.getLastLocCountDate());
				countHeaderVO.setAbc(location.getAbc());
				countHeaderVO.setAbcDesc(DictCache.getValue(DictConstant.LOC_ABC, location.getAbc()));
				countHeaderVOList.add(countHeaderVO);
			});
		}
	}

	@Override
	protected void queryBySku() {
		common();
		stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
			.eq(Stock::getSkuCode, this.countHeader.getSkuCode()));
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("该物料暂无库存！");
		}
		List<Long> skuIds = NodesUtil.toList(stockList, Stock::getSkuId);
		List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailBySkuCodes(skuIds);
		for (Stock stock : stockList) {
			CountHeaderVO countHeaderVO = new CountHeaderVO();
			countHeaderVO.setWhName(warehouse.getWhName());
			countHeaderVO.setLocId(stock.getLocId());
			countHeaderVO.setSkuId(stock.getSkuId());
			countHeaderVO.setZoneId(countHeader.getZoneId());
			Location location = locationList.stream().filter(u -> {
				return u.getLocId().equals(stock.getLocId());
			}).findFirst().orElse(null);

			if (Func.isNotEmpty(location)) {
				countHeaderVO.setLocCode(location.getLocCode());
				if (Func.isNotEmpty(countDetails)) {
					List<CountDetail> collect = countDetails.stream().filter(countDetail ->
						countDetail.getSkuId().equals(stock.getSkuId())
							&& countDetail.getLocId().equals(location.getLocId()))
						.collect(Collectors.toList());
					countHeaderVO.setCountBillNo(NodesUtil.join(collect, "countBillNo"));
				}
				countHeaderVO.setLocState(location.getLocStatus());
				countHeaderVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, location.getLocStatus()));
				countHeaderVO.setLastLocCountDate(location.getLastLocCountDate());
				Sku sku = SkuCache.getById(stock.getSkuId());
				if (Func.isNotEmpty(sku)) {
					countHeaderVO.setSkuCode(sku.getSkuCode());
					countHeaderVO.setSkuName(sku.getSkuName());
					countHeaderVO.setAbc(sku.getAbc());
					countHeaderVO.setAbcDesc(DictCache.getValue(DictConstant.LOC_ABC, sku.getAbc()));
					countHeaderVOList.add(countHeaderVO);
				}
			}
		}
	}

	private void common() {
		myLocation.clear();
		stockList.clear();
		if (Func.isNotEmpty(countHeader.getLocId()) && Func.isNotEmpty(countHeader.getLocIdEnd())) {
			//如果开始货位和结束货位都不为空
			Location locationBegin = LocationCache.getById(countHeader.getLocId());
			Location locationEnd = LocationCache.getById(countHeader.getLocIdEnd());

			countHeader.setLocCode(locationBegin.getLocCode());
			countHeader.setLocIdEndCode(locationEnd.getLocCode());

			if (Func.isNotEmpty(locationList)) {
				Iterator<Location> iterator = locationList.iterator();
				while (iterator.hasNext()) {
					Location location = iterator.next();
					boolean locBegin = Func.isNotEmpty(countHeader.getLocIdCode()) &&
						countHeader.getLocIdCode().compareTo(location.getLocCode()) <= 0;
					boolean locEnd = Func.isNotEmpty(countHeader.getLocIdEndCode()) &&
						countHeader.getLocIdEndCode().compareTo(location.getLocCode()) >= 0;
					if (!locBegin && !locEnd) {
						iterator.remove();
					}
				}
			}
		}
		//判断是否有开始结束时间
//		if (Func.isNotEmpty(countHeader.getStartTime()) && Func.isNotEmpty(locationList)) {
//			Iterator<Location> iterator = locationList.iterator();
//			while (iterator.hasNext()) {
//				Location location = iterator.next();
//				if (Func.isEmpty(location.getLastChangeTime())) {
//					continue;
//				}
//				if (location.getLastChangeTime().isBefore(countHeader.getStartTime())) {
//					iterator.remove();
//				}
//			}
//		}
		if (Func.isNotEmpty(countHeader.getWoId()) || Func.isNotEmpty(countHeader.getSkuId())) {
			stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
				.func(sql -> {
					if (Func.isNotEmpty(countHeader.getWoId())) {
						sql.eq(Stock::getWoId, countHeader.getWoId());
					}
					if (Func.isNotEmpty(countHeader.getSkuId())) {
						sql.eq(Stock::getSkuId, countHeader.getSkuId());
					}
					if (Func.isNotEmpty(locationList)) {
						sql.in(Stock::getLocId, NodesUtil.toList(locationList, Location::getLocId));
					}
//					if (countByEnum == CountByEnum.LOCATION) {
//						sql.groupBy(Stock::getLocId);
//					}else {
//						sql.groupBy(Stock::getSkuId);
//					}
				}));
			List<Location> collect = locationList.stream().filter(location -> {
				for (Stock stock : stockList) {
					if (location.getLocId().equals(stock.getLocId())) {
						return true;
					}
				}
				return false;
			}).collect(Collectors.toList());
			myLocation.addAll(collect);
		}
	}
}
