package org.nodes.wms.dao.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
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

	/**
	 * 更新拣货计划中的任务id
	 *
	 * @param soPickPlanList 拣货计划
	 * @param taskId         任务id
	 */
	void updateTask(List<SoPickPlan> soPickPlanList, Long taskId);

	/**
	 * 获取分配记录分页
	 *
	 * @param page                分页条件
	 * @param soPickPlanPageQuery 查询条件
	 * @return 分页对象
	 */
	Page<SoPickPlanPageResponse> getPage(IPage<Object> page, SoPickPlanPageQuery soPickPlanPageQuery);

	/**
	 * 根据库存id查询拣货计划
	 *
	 * @param stockIdList 库存id
	 * @return 拣货计划
	 */
	List<SoPickPlan> getByStockIds(List<Long> stockIdList);

	/**
	 * 根据分配记录id集合查找分配记录
	 * 
	 * @param soPickPlanIdList 分配id集合
	 * @return List<SoPickPlan>
	 */
	List<SoPickPlan> getByPickPlanIds(List<Long> soPickPlanIdList);
}
