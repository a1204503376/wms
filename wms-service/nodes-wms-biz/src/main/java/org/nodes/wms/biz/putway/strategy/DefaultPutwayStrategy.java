package org.nodes.wms.biz.putway.strategy;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DefaultPutwayStrategy {

	public Location run(BigDecimal putwayQty, Stock sourceStock){
		return null;
	}
}
