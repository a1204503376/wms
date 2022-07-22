package org.nodes.wms.biz.picking.connectionAreaPicking.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.picking.connectionAreaPicking.ConnectionAreaPickingBiz;
import org.nodes.wms.dao.picking.dto.input.ConnectionAreaPickingRequest;
import org.nodes.wms.dao.picking.dto.input.OutboundAccessAreaLocationQueryRequest;
import org.nodes.wms.dao.picking.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnectionAreaPickingBizImpl implements ConnectionAreaPickingBiz {

	@Override
	public IPage<OutboundAccessAreaLocationQueryResponse> selectLocationByConnectionArea(OutboundAccessAreaLocationQueryRequest request, Query query) {
		return null;
	}

	@Override
	public void connectionAreaPickAction(ConnectionAreaPickingRequest request) {

	}

	@Override
	public void connectionAreaMoveAction(ConnectionAreaPickingRequest request) {

	}
}
