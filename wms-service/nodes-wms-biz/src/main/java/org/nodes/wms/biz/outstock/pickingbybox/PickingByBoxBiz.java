package org.nodes.wms.biz.outstock.pickingbybox;

import org.nodes.wms.dao.outstock.logSoPick.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.PickingByBoxResponse;

public interface PickingByBoxBiz {
	/**
	 * 按箱拣货动作
	 *
	 * @param request 请求参数
	 * @return 是否拣货成功
	 */
	PickingByBoxResponse pickingByBoxAction(PickingByBoxRequest request);
}
