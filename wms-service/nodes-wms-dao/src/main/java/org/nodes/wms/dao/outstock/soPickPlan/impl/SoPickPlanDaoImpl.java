package org.nodes.wms.dao.outstock.soPickPlan.impl;

import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.outstock.soPickPlan.mapper.SoPickPlanMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 拣货计划Dao接口实现类
 **/
@Repository
public class SoPickPlanDaoImpl extends BaseServiceImpl<SoPickPlanMapper, SoPickPlan> implements SoPickPlanDao {

	public List<SoPickPlanForDistributionResponse> getBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId) {
		return super.baseMapper.selectSoPickPlanBySoBillIdAndSoDetailId(soBillId, soDetailId);
	}
}
