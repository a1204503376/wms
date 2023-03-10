package org.nodes.wms.dao.outstock.soPickPlan.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.outstock.soPickPlan.mapper.SoPickPlanMapper;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
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
	public List<SoPickPlan> getSoPickPlanForDistribution(Long soBillId) {
		return super.lambdaQuery()
			.eq(SoPickPlan::getSoBillId, soBillId)
			.list();
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
			.apply("pick_plan_qty != pick_real_qty")
			.list();
	}

	@Override
	public void updatePickRealQty(Long pickPlanId, BigDecimal pickRealTotalQty) {
		UpdateWrapper<SoPickPlan> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(SoPickPlan::getPickPlanId, pickPlanId);
		SoPickPlan soPickPlan = new SoPickPlan();
		soPickPlan.setPickRealQty(pickRealTotalQty);
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
			updateWrapper.lambda()
				.eq(SoPickPlan::getPickPlanId, soPickPlan.getPickPlanId());
			SoPickPlan saveDto = new SoPickPlan();
			saveDto.setTaskId(taskId);
			soPickPlan.setTaskId(taskId);
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
	public List<SoPickPlan> getByStockIdsAndSoBillId(List<Long> stockIdList, Long soBillId) {
		return super.lambdaQuery()
			.in(SoPickPlan::getStockId, stockIdList)
			.eq(SoPickPlan::getSoBillId, soBillId)
			.apply("pick_plan_qty > pick_real_qty")
			.list();
	}

	@Override
	public List<SoPickPlan> getByPickPlanIds(List<Long> soPickPlanIdList) {
		AssertUtil.notEmpty(soPickPlanIdList, "soPickPlanIdList must not be empty");
		return super.lambdaQuery().in(SoPickPlan::getPickPlanId, soPickPlanIdList).list();
	}

	@Override
	public void updatePickByPartParam(Long pickPlanId, Long stockId, Location location, Zone zone, String boxCode, BigDecimal stockBalance) {
		UpdateWrapper<SoPickPlan> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(SoPickPlan::getPickPlanId, pickPlanId);
		SoPickPlan soPickPlan = new SoPickPlan();
		soPickPlan.setStockId(stockId);
		soPickPlan.setLocId(location.getLocId());
		soPickPlan.setLocCode(location.getLocCode());
		soPickPlan.setZoneId(zone.getZoneId());
		soPickPlan.setZoneCode(zone.getZoneCode());
		if (Func.isNotEmpty(boxCode)) {
			soPickPlan.setBoxCode(boxCode);
		}
		if (Func.isNotEmpty(stockBalance)) {
			soPickPlan.setPickPlanQty(stockBalance);
		}
		if (!super.update(soPickPlan, updateWrapper)) {
			throw new ServiceException("修改拣货计划失败,请再次重试");
		}
	}

	@Override
	public List<SoPickPlan> getPickByTaskIdAndStockId(Long taskId, Long stockId) {
		AssertUtil.notNull(taskId, "根据任务和库存ID查询关联的拣货计划失败，任务ID为空");
		AssertUtil.notNull(stockId, "根据任务和库存ID查询关联的拣货计划失败，库存ID为空");
		return super.lambdaQuery()
			.eq(SoPickPlan::getTaskId, taskId)
			.eq(SoPickPlan::getStockId, stockId)
			.list();
	}

	@Override
	public void updatePickByTaskIdAndStockId(Long taskId, Long sourceStockId, Long targetStockId, Location location, Zone zone) {
		UpdateWrapper<SoPickPlan> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(SoPickPlan::getTaskId, taskId)
			.eq(SoPickPlan::getStockId, sourceStockId);
		SoPickPlan soPickPlan = new SoPickPlan();
		soPickPlan.setStockId(targetStockId);
		soPickPlan.setLocId(location.getLocId());
		soPickPlan.setLocCode(location.getLocCode());
		soPickPlan.setZoneId(zone.getZoneId());
		soPickPlan.setZoneCode(zone.getZoneCode());
		if (!super.update(soPickPlan, updateWrapper)) {
			throw new ServiceException("修改拣货计划失败,请再次重试");
		}
	}

	@Override
	public List<SoPickPlan> selectPickBySoDetailId(Long soDetailId) {
		return super.lambdaQuery()
			.eq(SoPickPlan::getSoDetailId, soDetailId)
			.list();
	}

	@Override
	public List<SoPickPlan> selectSoPickPlanByBoxCode(Long soBillId, String boxCode) {
		AssertUtil.notNull(soBillId, "查询拣货计划失败,发货单ID为空");
		AssertUtil.notEmpty(boxCode, "查询拣货计划失败,箱码为空");
		return super.lambdaQuery()
			.eq(SoPickPlan::getSoBillId, soBillId)
			.eq(SoPickPlan::getBoxCode, boxCode)
			.apply("pick_plan_qty != pick_real_qty")
			.list();
	}

	@Override
	public SoPickPlan selectSoPickPlanById(Long pickPlanId) {
		AssertUtil.notNull(pickPlanId, "查询拣货计划失败,拣货计划ID为空");
		return super.lambdaQuery()
			.eq(SoPickPlan::getPickPlanId, pickPlanId)
			.one();
	}

	@Override
	public List<SoPickPlan> selectSoPickPlansByBoxCode(String boxCode) {
		AssertUtil.notEmpty(boxCode, "查询拣货计划失败,箱码为空");
		return super.lambdaQuery()
			.eq(SoPickPlan::getBoxCode, boxCode)
			.apply("pick_plan_qty != pick_real_qty")
			.list();
	}

	@Override
	public void updatePlanOfStock(SoPickPlan soPickPlan, Stock newStock) {
		soPickPlan.setStockId(newStock.getStockId());
		soPickPlan.setLocId(newStock.getLocId());
		soPickPlan.setLocCode(newStock.getLocCode());
		soPickPlan.setZoneId(newStock.getZoneId());
		soPickPlan.setZoneCode(newStock.getZoneCode());
		soPickPlan.setBoxCode(newStock.getBoxCode());
		soPickPlan.setLpnCode(newStock.getLpnCode());
		soPickPlan.setStockStatus(newStock.getStockStatus());
		soPickPlan.setPickRealQty(BigDecimal.ZERO);
		SkuLotUtil.setAllSkuLot(newStock, soPickPlan);

		if (!super.updateById(soPickPlan)) {
			throw new ServiceException("修改拣货计划失败,请再次重试");
		}
	}
}
