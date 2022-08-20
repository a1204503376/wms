package org.nodes.wms.biz.outstock.plan;

import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
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
	void updatePickRealQty(Long pickPlanId, BigDecimal pickRealQty);

	/**
	 * 根据拣货计划拣货，
	 * 该函数会执行如下操作：1. 释放库存占用 2.移动库存到出库暂存区 3.更新拣货计划 4.生产并保存拣货记录
	 *
	 * @param pickPlan     必填，拣货计划
	 * @param pickQty      必填，本次拣货数量
	 * @param serialNoList 非必填，序列号集合
	 * @return 拣货记录
	 */
	LogSoPick pickByPlan(SoPickPlan pickPlan, BigDecimal pickQty, List<String> serialNoList);

	/**
	 * 根据任务ID查询跟当前任务相关联的拣货计划
	 *
	 * @param taskId 任务ID
	 * @return 拣货计划
	 */
	List<SoPickPlan> findPickByTaskId(Long taskId);
}
