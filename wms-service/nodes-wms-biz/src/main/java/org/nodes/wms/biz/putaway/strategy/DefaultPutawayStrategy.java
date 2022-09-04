package org.nodes.wms.biz.putaway.strategy;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DefaultPutawayStrategy {

	public Location run(BigDecimal putawayQty, Stock sourceStock){
		return null;
	}
}
