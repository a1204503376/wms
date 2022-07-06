package org.nodes.wms.biz.putway;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.NewPutawayByBoxlRequest;
import org.nodes.wms.dao.stock.entities.Stock;

import java.math.BigDecimal;

public interface PutwayBiz {
	/**
	 * 提交按箱收货
	 * @param request 请求对象
	 */
	void  addByBoxShelf(NewPutawayByBoxlRequest request);
}
