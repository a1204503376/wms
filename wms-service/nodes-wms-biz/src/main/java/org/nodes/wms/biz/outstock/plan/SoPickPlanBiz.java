package org.nodes.wms.biz.outstock.plan;

import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;

import java.math.BigDecimal;
import java.util.List;

/**
 * 拣货计划相关业务
 *
 * @author nodes
 */
public interface SoPickPlanBiz {

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
	 * 运行分配策略
	 *
	 * @param soHeader       发货单信息
	 * @param soDetials      发货单未发明细
	 * @param existPickPlans 已经存在的拣货计划
	 * @return 运行的信息，如果全部分配成功则返回分配成功
	 */
	String runByPickStrategy(SoHeader soHeader, List<SoDetail> soDetials, List<SoPickPlan> existPickPlans);

	/**
	 * 根据拣货计划ID修改拣货计划数量
	 *
	 * @param pickPlanId  拣货计划ID
	 * @param pickRealQty 拣货实际数量
	 */
	void updatePickPlanPickRealQtyById(Long pickPlanId, BigDecimal pickRealQty);
}
