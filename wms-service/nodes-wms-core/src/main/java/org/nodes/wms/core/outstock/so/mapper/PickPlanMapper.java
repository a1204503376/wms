package org.nodes.wms.core.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 拣货计划表 Mapper 接口
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Primary
public interface PickPlanMapper extends BaseMapper<PickPlan> {

	/**
	 * 通过任务id查询按件拣货单据信息
	 * @param taskId
	 * @return
	 */
	List<PickPlan> selectPickPlanListForPickByTaskId(Long wellenId, Long taskId);
	List<PickPlan> selectPickPlanListForPickByWellenId(Long wellenId, Long taskId,String soBillNo);
}
