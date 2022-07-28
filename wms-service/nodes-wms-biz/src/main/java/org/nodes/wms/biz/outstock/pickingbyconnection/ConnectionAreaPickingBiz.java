package org.nodes.wms.biz.outstock.pickingbyconnection;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.ConnectionAreaPickingRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.OutboundAccessAreaLocationQueryRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.springblade.core.mp.support.Query;

public interface ConnectionAreaPickingBiz {

	/**
	 * 查询出库接驳区的库未
	 *
	 * @param request 查询条件
	 * @param query   分页参数
	 * @return 多个库位信息
	 */
	IPage<OutboundAccessAreaLocationQueryResponse> selectLocationByConnectionArea(OutboundAccessAreaLocationQueryRequest request, Query query);

	/**
	 * 接驳区拣货动作
	 *
	 * @param request 请求参数
	 */
	void connectionAreaPickAction(ConnectionAreaPickingRequest request);

	/**
	 * 接驳区移动
	 *
	 * @param request 请求参数
	 */
	void connectionAreaMoveAction(ConnectionAreaPickingRequest request);
}
