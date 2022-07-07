package org.nodes.wms.biz.putway;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putway.dto.input.NewPutawayByBoxlRequest;
import org.nodes.wms.dao.putway.dto.output.LocResponse;
import org.nodes.wms.dao.stock.entities.Stock;

import java.math.BigDecimal;
import java.util.List;

public interface PutwayBiz {
	/**
	 * 提交按箱收货
	 * @param request 请求对象
	 */
	void  addByBoxShelf(NewPutawayByBoxlRequest request);
	/**
	 * 天宜定制：呼叫Agv
	 * @param request  前端传入参数
	 */
    void callAgv(CallAgvRequest request);
	/**
	 * 天宜定制：呼叫Agv 根据库房id和箱型获取库位信息
	 * @param request  包含库房id和箱型
	 */
	List<LocResponse> findLocByLpnType(LpnTypeRequest request);
}
