package org.nodes.wms.dao.outstock.soPickPlan.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.outstock.soPickPlan.mapper.SoPickPlanMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

	@Override
	public void updatePickRealQty(Long pickPlanId, BigDecimal pickRealQty) {
		UpdateWrapper<SoPickPlan> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
				.eq(SoPickPlan::getPickPlanId, pickPlanId);
		SoPickPlan soPickPlan = new SoPickPlan();
		soPickPlan.setPickRealQty(pickRealQty);
		if (!super.update(soPickPlan, updateWrapper)) {
			throw new ServiceException("修改拣货计划失败,请再次重试");
		}
	}

	@Override
	public List<SoPickPlan> getPickByTaskId(Long taskId) {
		AssertUtil.notNull(taskId, "根据任务查询关联的拣货计划失败，任务ID为空");
		return super.lambdaQuery()
				.eq(SoPickPlan::getTaskId, taskId)
				.list();
	}

	@Override
	public void updateTask(List<SoPickPlan> soPickPlanList, Long taskId) {
		for (SoPickPlan soPickPlan : soPickPlanList) {
			UpdateWrapper<SoPickPlan> updateWrapper = Wrappers.update();
			updateWrapper.lambda().eq(SoPickPlan::getPickPlanId, soPickPlan.getPickPlanId());
			SoPickPlan saveDto = new SoPickPlan();
			saveDto.setTaskId(taskId);
			if (!super.update(saveDto, updateWrapper)) {
				throw new ServiceException("更新拣货计划的任务id失败,请再次重试");
			}
		}
	}

	@Override
	public Page<SoPickPlanPageResponse> getPage(IPage<Object> page, SoPickPlanPageQuery soPickPlanPageQuery) {
		return super.baseMapper.page(page, soPickPlanPageQuery);
	}

	@Override
	public List<SoPickPlan> getByStockIds(List<Long> stockIdList) {
		return super.lambdaQuery().in(SoPickPlan::getStockId, stockIdList).list();
	}

	@Override
	public List<SoPickPlan> getByPickPlanIds(List<Long> soPickPlanIdList) {
		AssertUtil.notEmpty(soPickPlanIdList, "soPickPlanIdList must not be empty");
		return super.lambdaQuery().in(SoPickPlan::getPickPlanId, soPickPlanIdList).list();
	}

}
