package org.nodes.wms.biz.outstock;

import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;

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
}
