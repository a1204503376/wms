package org.nodes.wms.core.count.service.impl;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.enums.CountByEnum;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ABCCountTypeQuery extends CountTypeQuery {
	@Override
	public List<CountHeaderVO> queryByCountType() {
		super.queryByCountType();
		if (countByEnum == CountByEnum.LOCATION) {
			countHeaderVOList = countHeaderVOList.stream().sorted(
				Comparator.comparing(CountHeaderVO::getLocCode))
				.collect(Collectors.toList());
		} else {
			countHeaderVOList = countHeaderVOList.stream().sorted(
				Comparator.comparing(CountHeaderVO::getAbc, Comparator.nullsLast(Integer::compareTo)))
				.collect(Collectors.toList());
		}
		return countHeaderVOList;
	}

	@Override
	protected void queryByLocation() {
		if (Func.isNotEmpty(countHeader.getAbc())) {
			locationList.removeIf(new Predicate<Location>() {
				@Override
				public boolean test(Location location) {
					return !countHeader.getAbc().equals(location.getAbc());
				}
			});
		}
		if (Func.isNotEmpty(countHeader.getChecked()) && countHeader.getChecked()) {
			if (locationList.size() > 0) {
				Iterator<Location> iterator = locationList.iterator();
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
			locationList.forEach(location -> {
				CountHeaderVO countHeaderVO = new CountHeaderVO();
				countHeaderVO.setWhName(warehouse.getWhName());
				countHeaderVO.setLocCode(location.getLocCode());
				countHeaderVO.setCountBillNo(location.getCountBillNo());
				countHeaderVO.setLocId(location.getLocId());
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
		List<Stock> stockList = stockService.selectStockByLoc(
			NodesUtil.toList(locationList, Location::getLocId), countHeader.getAbc(), null);
		if (Func.isNotEmpty(stockList)) {
			List<Long> skuIds = NodesUtil.toList(stockList, Stock::getSkuId);
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailBySkuCodes(skuIds);
			for (Stock stock : stockList) {
				CountHeaderVO findObj = countHeaderVOList.stream().filter(u -> {
					return u.getSkuId().equals(stock.getSkuId()) && u.getLocId().equals(stock.getLocId());
				}).findFirst().orElse(null);
				if (Func.isNotEmpty(findObj)) {
					continue;
				}
				CountHeaderVO countHeaderVO = new CountHeaderVO();
				countHeaderVO.setWhName(warehouse.getWhName());
				countHeaderVO.setLocId(stock.getLocId());
				countHeaderVO.setSkuId(stock.getSkuId());
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
	}
}
