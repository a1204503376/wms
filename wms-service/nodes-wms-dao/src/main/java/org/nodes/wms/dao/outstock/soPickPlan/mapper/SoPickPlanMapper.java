package org.nodes.wms.dao.outstock.soPickPlan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
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
}
