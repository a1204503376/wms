package org.nodes.wms.dao.outstock.soPickPlan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;

import java.util.List;

/**
 * 拣货计划mapper接口
 **/
public interface SoPickPlanMapper extends BaseMapper<SoPickPlan> {

	/**
	 * 根据发货单id和发货单明细id查询拣货计划信息
	 *
	 * @param soBillId   查询参数
	 * @param soDetailId 查询参数
	 * @return 拣货计划
	 */
	List<SoPickPlanForDistributionResponse> selectSoPickPlanBySoBillIdAndSoDetailId(@Param("soBillId") Long soBillId, @Param("soDetailId") Long soDetailId);

	/**
	 * 获取分配记录分页
	 *
	 * @param page                分页条件
	 * @param soPickPlanPageQuery 查询条件
	 * @return 分页对象
	 */
	Page<SoPickPlanPageResponse> page(IPage<Object> page, @Param("param") SoPickPlanPageQuery soPickPlanPageQuery);
}


