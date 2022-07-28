package org.nodes.wms.biz.outstock.pickingbypcs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.FindAllPickingRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.FindPickingBySoBillIdRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.PickingByBoxResponse;
import org.springblade.core.mp.support.Query;

public interface PickingByPcsBiz {
	/**
	 * 拣货分页查询
	 *
	 * @param request 查询条件
	 * @param query   分页参数
	 * @return 返回多个能按件拣货的对象
	 */
	IPage<FindAllPickingResponse> selectAllPickingByNo(FindAllPickingRequest request, Query query);

	/**
	 * 按件拣货动作
	 *
	 * @param request 请求对象
	 * @return 是否拣货成功
	 */
	PickingByBoxResponse pickingByPcsAction(PickingByBoxRequest request);

	/**
	 * Pda根据发货单ID查询出库单明细
	 *
	 * @param request 请求对象-发货单ID
	 * @param query   分页参数
	 * @return 实现的分页
	 */
	IPage<FindPickingBySoBillIdResponse> selectPickingBySoBillId(FindPickingBySoBillIdRequest request, Query query);
}
