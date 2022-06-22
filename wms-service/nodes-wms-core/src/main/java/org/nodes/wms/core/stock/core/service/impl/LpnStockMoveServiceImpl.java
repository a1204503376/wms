package org.nodes.wms.core.stock.core.service.impl;

import lombok.Data;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockMoveService;
import org.springframework.context.annotation.Primary;

import java.util.List;
@Primary
public class LpnStockMoveServiceImpl implements IStockMoveService<LpnStockMoveServiceImpl.LpnStockMoveParam>{
	@Override
	public List<Stock> move(LpnStockMoveParam moveParam) {
		return null;
	}

	@Data
	public static class LpnStockMoveParam extends IStockMoveService.MoveParam {

	}
}
