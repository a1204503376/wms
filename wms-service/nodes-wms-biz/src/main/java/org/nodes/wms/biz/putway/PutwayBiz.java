package org.nodes.wms.biz.putway;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.NewPutawayByBoxlRequest;
import org.nodes.wms.dao.stock.entities.Stock;

import java.math.BigDecimal;

public interface PutwayBiz {
	/**
	 * 上架策略运行
	 *
	 * @param putwayQty   上架数量
	 * @param sourceStock 需要上架的原始库存数据
	 * @return 可能为空或unkown库位
	 */
	Location runStrategyOfPutway(BigDecimal putwayQty, Stock sourceStock);

	/**
	 * 提交按箱收货
	 * @param request 请求对象
	 */
	void  addByBoxShelf(NewPutawayByBoxlRequest request);
}
