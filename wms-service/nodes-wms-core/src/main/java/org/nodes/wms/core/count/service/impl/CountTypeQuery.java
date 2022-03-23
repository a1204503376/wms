package org.nodes.wms.core.count.service.impl;

import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.count.enums.CountByEnum;
import org.nodes.wms.core.count.service.ICountDetailService;
import org.nodes.wms.core.count.service.ICountTypeQuery;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.outstock.so.service.ISoPickService;
import org.nodes.wms.core.stock.transfer.service.ITransferRecordService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
@Primary
public abstract class CountTypeQuery implements ICountTypeQuery {
	protected CountHeaderVO countHeader;
	protected List<CountHeaderVO> countHeaderVOList = new ArrayList<>();
	protected List<Location> locationList = new ArrayList<>();
	protected CountByEnum countByEnum;
	protected Warehouse warehouse;
	protected IStockService stockService;
	protected ISoPickService soPickService;
	protected ITransferRecordService transferRecordService;
	protected ICountDetailService countDetailService;

	public CountTypeQuery() {
		this.transferRecordService = SpringUtil.getBean(ITransferRecordService.class);
		this.soPickService = SpringUtil.getBean(ISoPickService.class);
		this.stockService = SpringUtil.getBean(IStockService.class);
		this.countDetailService = SpringUtil.getBean(ICountDetailService.class);
	}

	@Override
	public void setParam(CountHeaderVO countHeader) {
		this.countHeader = countHeader;
		this.warehouse = WarehouseCache.getById(countHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("查询时库房不能为空");
		}
		countByEnum = CountByEnum.getCountByEnumByIndex(countHeader.getCountBy());
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		this.locationList = locationService.list(Condition.getQueryWrapper(new Location())
			.lambda()
			.eq(Location::getWhId, warehouse.getWhId())
		).stream().collect(Collectors.toList());
		if (Func.isNotEmpty(countHeader.getZoneId())) {
			this.locationList.removeIf(new Predicate<Location>() {
				@Override
				public boolean test(Location location) {
					return !countHeader.getZoneId().contains(location.getZoneId());
				}
			});
		}
	}

	@Override
	public List<CountHeaderVO> queryByCountType() {
		countHeaderVOList.clear();
		switch (countByEnum) {
			case SKU:
				queryBySku();
				break;
			case LOCATION:
				queryByLocation();
				break;
		}
		return countHeaderVOList;
	}

	protected abstract void queryByLocation();

	protected abstract void queryBySku();
}
