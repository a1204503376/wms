package org.nodes.wms.biz.picking.pickingByBox.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.picking.pickingByBox.PickingByBoxBiz;
import org.nodes.wms.dao.picking.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.picking.dto.output.PickingByBoxResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PickingByBoxBizImpl implements PickingByBoxBiz {
	@Override
	public PickingByBoxResponse pickingByBoxAction(PickingByBoxRequest request) {
		return null;
	}
}
