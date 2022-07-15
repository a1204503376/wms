package org.nodes.wms.biz.task.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.task.SchedulingBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.zone.constant.ZoneConstant;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulingBizImpl implements SchedulingBiz {
	private final LocationBiz locationBiz;
	private final ZoneBiz zoneBiz;

	@Override
	public String selectAndFrozenEnableOutbound(QueryAndFrozenEnableOutboundRequest request) {
		//获取入库接驳区信息
		Zone zone = zoneBiz.findByCode(ZoneConstant.AGV_SHIPMENTS_CONNECTION_AREA);
		//获取所有入库接驳区的库位
		List<Location> locationList = locationBiz.findLocationByZoneId(zone.getZoneId());
		//将入库接驳区库位进行判断 寻找符合条件的库位 没冻结，无库存占用
		Location location = locationList.stream().filter(item -> !locationBiz.isFrozen(item)).findFirst().orElse(null);
		//冻结库位
		locationBiz.freezeByOccupyFlag(location.getLocId(), request.getTaskDetailId().toString());
		return location.getLocCode();
	}
}
