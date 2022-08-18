package org.nodes.wms.dao.outstock;

import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 拣货计划 Dao接口
 **/
public interface SoPickPlanDao extends BaseService<SoPickPlan> {

	/**
	 * 根据发货单id和发货单明细id查询拣货计划信息
	 *
	 * @param soBillId       查询参数
	 * @param soDetailId 查询参数
	 * @return 拣货计划
	 */
	List<SoPickPlanForDistributionResponse> getBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId);
}
