package org.nodes.wms.biz.picking.pickingByBox;

import org.nodes.wms.dao.picking.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.picking.dto.output.PickingByBoxResponse;

public interface PickingByBoxBiz {
	/**
	 * 按箱拣货动作
	 *
	 * @param request 请求参数
	 * @return 是否拣货成功
	 */
	PickingByBoxResponse pickingByBoxAction(PickingByBoxRequest request);
}
