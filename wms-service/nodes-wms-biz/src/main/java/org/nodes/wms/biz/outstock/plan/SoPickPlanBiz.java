package org.nodes.wms.biz.outstock.plan;

import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;

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
	 * @param soHeaderId 出库单id
	 * @return true：表示出库单存在有效的分配计划
	 */
	boolean hasPickPlan(Long soHeaderId);

	/**
	 * 根据出库id查找所有有效的拣货计划
	 *
	 * @param soHeaderId 出库单id
	 * @return 拣货计划集合
	 */
	List<SoPickPlan> findBySoHeaderId(Long soHeaderId);
}
