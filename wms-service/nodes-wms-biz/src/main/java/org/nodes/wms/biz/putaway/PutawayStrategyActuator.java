package org.nodes.wms.biz.putaway;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putaway.strategy.TianYiPutawayStrategy;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 上架策略执行器
 */
@Service
@RequiredArgsConstructor
public class PutawayStrategyActuator {
	private final LocationBiz locationBiz;
	private final TianYiPutawayStrategy tianYiPutawayStrategy;

	public Location run(BigDecimal putawayQty, List<Stock> stocks) {
		Location location = tianYiPutawayStrategy.run(stocks);
		if (!Func.isNull(location)) {
			return location;
		}

		// 计算不到合适的库位，则返回unknow库位
		return locationBiz.getUnknowLocation(stocks.get(0).getWhId());
	}
}
