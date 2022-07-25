package org.nodes.wms.biz.outstock.pickingbybox.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.pickingbybox.PickingByBoxBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.PickingByBoxResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PickingByBoxBizImpl implements PickingByBoxBiz {
	@Override
	public PickingByBoxResponse pickingByBoxAction(PickingByBoxRequest request) {
		return null;
	}
}
