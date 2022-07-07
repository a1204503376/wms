package org.nodes.wms.biz.putway.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putway.PutwayBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putway.dto.input.NewPutawayByBoxlRequest;
import org.nodes.wms.dao.putway.dto.output.LocResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PutwayBizImpl implements PutwayBiz {
	private LocationBiz locationBiz;
	private StockBiz stockBiz;

	@Override
	public void addByBoxShelf(NewPutawayByBoxlRequest request) {
		// 调用库存移动，如果关联了序列号需要获取序列号
		// 生成上架记录
	}

    @Override
    public void callAgv(CallAgvRequest request) {
		// 调用库存移动，如果关联了序列号需要获取序列号
		// 生成上架记录
    }

	@Override
	public List<LocResponse> findLocByLpnType(LpnTypeRequest request) {
		List<LocResponse> locResponseList = new ArrayList<>();
		// 根据箱型和库房id获取库位信息
		List<Location> locationList = locationBiz.findLocationByLpnType(request);
		for(Location location:locationList){
			LocResponse locResponse = new LocResponse();
			locResponse.setLocId(location.getLocId());
			locResponse.setLocCode(location.getLocCode());
			// 库位是否为空
			locResponse.setIsEmpty(stockBiz.judgeEnableOnLocation(location));
			locResponseList.add(locResponse);
		}
		return locResponseList;

	}
}
