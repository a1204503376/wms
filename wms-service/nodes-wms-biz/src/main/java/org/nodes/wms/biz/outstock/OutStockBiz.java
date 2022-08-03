package org.nodes.wms.biz.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.*;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.PickingByBoxResponse;
import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 出库业务
 *
 * @author nodesc
 */
public interface OutStockBiz {

	/**
	 * PC按件拣货
	 *
	 * @param request
	 */
	void pickByPc(PickByPcRequest request);

	/**
	 * 根据发货单id和发货单明细id查询拣货计划
	 *
	 * @param soBillId   发货单id 必填
	 * @param soDetailId 发货单明细id 非必填
	 * @return 拣货计划信息
	 */
	List<SoPickPlanForDistributionResponse> getSoPickPlanBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId);


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
	PickingByBoxResponse pickingByPcs(PickingByBoxRequest request);

	/**
	 * Pda根据发货单ID查询出库单明细
	 *
	 * @param request 请求对象-发货单ID
	 * @param query   分页参数
	 * @return 实现的分页
	 */
	IPage<FindPickingBySoBillIdResponse> selectPickingBySoBillId(FindPickingBySoBillIdRequest request, Query query);


	/**
	 * 按箱拣货动作
	 *
	 * @param request 请求参数
	 * @return 是否拣货成功
	 */
	PickingByBoxResponse pickingByBox(PickingByBoxRequest request);


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
	void connectionAreaPick(ConnectionAreaPickingRequest request);

	/**
	 * 接驳区移动
	 *
	 * @param request 请求参数
	 */
	void connectionAreaMove(ConnectionAreaPickingRequest request);
}
