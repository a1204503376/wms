package org.nodes.wms.dao.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.soPickPlan.dto.input.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
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
	 * 获取分配记录分页
	 *
	 * @param page                分页条件
	 * @param soPickPlanPageQuery 查询条件
	 * @return 分页对象
	 */
	Page<SoPickPlanPageResponse> getPage(IPage<Object> page, SoPickPlanPageQuery soPickPlanPageQuery);
}
