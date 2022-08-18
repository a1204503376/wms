package org.nodes.wms.dao.outstock.soPickPlan.impl;

import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.outstock.soPickPlan.mapper.SoPickPlanMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 拣货计划Dao接口实现类
 *
 * @author nodesc
 **/
@Repository
public class SoPickPlanDaoImpl
	extends BaseServiceImpl<SoPickPlanMapper, SoPickPlan>
	implements SoPickPlanDao {

	@Override
	public List<SoPickPlanForDistributionResponse> getBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId) {
		return super.baseMapper.selectSoPickPlanBySoBillIdAndSoDetailId(soBillId, soDetailId);
	}

	@Override
	public boolean hasEnablePickPlan(Long soBillId) {
		AssertUtil.notNull(soBillId, "判断是否有分配中的计划失败，出库单ID为空");
		return super.lambdaQuery()
			.eq(SoPickPlan::getSoBillId, soBillId)
			.count() > 0;
	}

	@Override
	public List<SoPickPlan> findBySoHeaderId(Long soBillId) {
		AssertUtil.notNull(soBillId, "判断是否有分配中的计划失败，出库单ID为空");
		return super.lambdaQuery()
			.eq(SoPickPlan::getSoBillId, soBillId)
			.list();
	}
}
