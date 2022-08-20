package org.nodes.wms.dao.outstock;

import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.springblade.core.mp.base.BaseService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 拣货计划 Dao接口
 **/
public interface SoPickPlanDao extends BaseService<SoPickPlan> {

	/**
	 * 根据发货单id和发货单明细id查询拣货计划信息
	 *
	 * @param soBillId   查询参数
	 * @param soDetailId 查询参数
	 * @return 拣货计划
	 */
	List<SoPickPlanForDistributionResponse> getBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId);

	/**
	 * 判断是否有分配中的计划
	 *
	 * @param soBillId 出库单id
	 * @return true：表示出库单存在有效的分配计划
	 */
	boolean hasEnablePickPlan(Long soBillId);

	/**
	 * 根据出库id查找所有有效的拣货计划
	 *
	 * @param soBillId 出库单id
	 * @return 拣货计划集合
	 */
	List<SoPickPlan> findBySoHeaderId(Long soBillId);

	/**
	 * 根据拣货计划ID修改拣货计划数量
	 *
	 * @param pickPlanId  拣货计划ID
	 * @param pickRealQty 拣货实际数量
	 */
	void updatePickRealQty(Long pickPlanId, BigDecimal pickRealQty);

	/**
	 * 根据任务ID查询拣货计划
	 *
	 * @param taskId 任务ID
	 * @return 拣货计划
	 */
	List<SoPickPlan> getPickByTaskId(Long taskId);
}
