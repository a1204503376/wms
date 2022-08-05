package org.nodes.wms.biz.outstock.plan;

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
}
