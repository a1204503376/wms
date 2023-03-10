package org.nodes.wms.biz.outstock.plan.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.outstock.logSoPick.modular.LogSoPickFactory;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.strategy.TianyiPickStrategy;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.dto.intput.SoPickPlanPageQuery;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanPageResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拣货计划相关业务
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class SoPickPlanBizImpl implements SoPickPlanBiz {

	private final LocationBiz locationBiz;
	private final TianyiPickStrategy tianyiPickStrategy;
	private final SoPickPlanDao soPickPlanDao;
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;
	private final LogSoPickFactory logSoPickFactory;
	private final LogSoPickDao logSoPickDao;

	@Override
	public boolean hasEnablePickPlan(Long soBillId) {
		return soPickPlanDao.hasEnablePickPlan(soBillId);
	}

	@Override
	public List<SoPickPlan> findBySoHeaderId(Long soBillId) {
		return soPickPlanDao.findBySoHeaderId(soBillId);
	}

	@Override
	public String runPickStrategy(SoHeader soHeader, List<SoDetail> soDetails, List<SoPickPlan> existPickPlans) {
		String result = "";
		List<SoPickPlan> pickPlanOfSoDetail;
		for (SoDetail detail : soDetails) {
			pickPlanOfSoDetail = null;
			if (Func.notNull(existPickPlans)) {
				pickPlanOfSoDetail = existPickPlans.stream()
					.filter(soPickPlan -> detail.getSoDetailId().equals(soPickPlan.getSoDetailId()))
					.collect(Collectors.toList());
			}

			List<SoPickPlan> pickPlan = tianyiPickStrategy.run(soHeader, detail, soDetails, pickPlanOfSoDetail);
			if (Func.isNotEmpty(pickPlan)) {
				List<SoPickPlan> newPickPlan = pickPlan.stream()
					.distinct()
					.collect(Collectors.toList());
				occupyStockAndSavePlan(newPickPlan);
				existPickPlans.addAll(newPickPlan);
			}

			result = createResultByRunPickStrategy(pickPlan, pickPlanOfSoDetail, detail, result);
		}

		return result;
	}

	@Override
	public void occupyStockAndSavePlan(List<SoPickPlan> soPickPlanList) {
		AssertUtil.notEmpty(soPickPlanList, "分配失败,拣货计划参数不能为空");

		stockBiz.occupyStock(soPickPlanList);
		soPickPlanDao.saveBatch(soPickPlanList);
	}

	@Override
	public void updatePickRealQty(Long pickPlanId, BigDecimal pickRealTotalQty) {
		AssertUtil.notNull(pickPlanId, "修改拣货计划失败,拣货计划ID为空");
		AssertUtil.notNull(pickRealTotalQty, "修改拣货计划失败,拣货量为空");
		soPickPlanDao.updatePickRealQty(pickPlanId, pickRealTotalQty);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public LogSoPick pickByPlan(SoDetail soDetail, SoPickPlan pickPlan, BigDecimal pickQty, List<String> serialNoList) {
		AssertUtil.notNull(pickPlan, "按计划拣货失败,拣货计划参数为空");
		AssertUtil.notNull(pickQty, "按拣货计划拣货失败，拣货数量参数为空");

		if (Func.isNull(pickPlan.getPickRealQty())) {
			pickPlan.setPickRealQty(BigDecimal.ZERO);
		}

		Stock stock = stockQueryBiz.findStockById(pickPlan.getStockId());
		checkPickByPlan(pickPlan, pickQty, serialNoList, stock);
		// 1.释放库存占用
		stock.setOccupyQty(stock.getOccupyQty().subtract(pickQty));
		// 2.移动库存到出库暂存区
		Location pickToLocation = locationBiz.getLocationByZoneType(
			pickPlan.getWhId(), DictKVConstant.ZONE_TYPE_PICK_TO).get(0);
		stockBiz.moveStock(stock, serialNoList, pickQty, stock.getBoxCode(), stock.getBoxCode(), pickToLocation, StockLogTypeEnum.OUTSTOCK_BY_PICK_PLAN,
			pickPlan.getSoBillId(), pickPlan.getSoBillNo(), soDetail.getSoLineNo(), null);
		// 3.更新拣货计划
		updatePickRealQty(pickPlan.getPickPlanId(), pickPlan.getPickRealQty().add(pickQty));
		// 4.生产并保存拣货记录
		LogSoPick logSoPick = logSoPickFactory.create(soDetail, pickPlan, pickQty, serialNoList, stock);
		logSoPickDao.save(logSoPick);
		return logSoPick;
	}

	private void checkPickByPlan(SoPickPlan pickPlan, BigDecimal pickQty, List<String> serialNoList, Stock stock) {
		if (BigDecimalUtil.le(pickQty, BigDecimal.ZERO)) {
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货数量必须大于0");
		}

		if (BigDecimalUtil.gt(pickQty, pickPlan.getSurplusQty())) {
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货数量超过计划量[{}]", pickPlan.getSurplusQty());
		}

		if (Func.isNotEmpty(serialNoList) && serialNoList.size() != pickQty.intValue()) {
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货数量[{}]与拣货序列号个数[{}]不符", pickQty, serialNoList.size());
		}

		if (BigDecimalUtil.gt(pickQty, stock.getStockBalance())) {
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货量超过库存[{}]的余额[{}]", stock.getStockId(), stock.getStockBalance());
		}

		if (BigDecimalUtil.gt(pickQty, stock.getOccupyQty())) {
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货量[{}]超过库存[{}]的占用量[{}]",
				pickQty, stock.getStockId(), stock.getOccupyQty());
		}

		List<Serial> serialOfStock = stockQueryBiz.findSerialByStock(stock.getStockId());
		if (Func.isNotEmpty(serialOfStock) && Func.isEmpty(serialNoList)) {
			throw ExceptionUtil.mpe("按拣货计划拣货失败,库存[{}]是按序列号管理的,请采集出库的序列号", stock.getStockId());
		}

		if (Func.isNotEmpty(serialOfStock) && Func.isNotEmpty(serialNoList)) {
			List<String> serialNoOfStock = serialOfStock.stream()
				.map(Serial::getSerialNumber)
				.collect(Collectors.toList());
			if (!serialNoOfStock.containsAll(serialNoList)) {
				throw ExceptionUtil.mpe("按拣货计划拣货失败, 存在无效的拣货序列号");
			}
		}

		if (Func.isEmpty(serialOfStock) && Func.isNotEmpty(serialNoList)) {
			throw ExceptionUtil.mpe("按拣货计划拣货失败,库存[{}]不是按序列号管理的,序列号无效", stock.getStockId());
		}
	}

	@Override
	public List<SoPickPlan> findPickByTaskId(Long taskId) {
		return soPickPlanDao.getPickByTaskId(taskId);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelPickPlan(List<SoPickPlan> soPickPlanList, SoHeader soHeader) {
		AssertUtil.notEmpty(soPickPlanList, "全部取消分配失败,待取消的拣货计划参数为空");

		// 删除拣货计划
		List<Long> soPickPlanIdList = soPickPlanList.stream()
			.map(SoPickPlan::getPickPlanId)
			.collect(Collectors.toList());
		soPickPlanDao.removeByIds(soPickPlanIdList);
		// 释放库存占用
		for (SoPickPlan soPickPlan : soPickPlanList) {
			stockBiz.reduceOccupy(soHeader.getSoBillId(), soHeader.getSoBillNo(),
				soPickPlan.getSoDetailId(), soPickPlan.getStockId(), soPickPlan.getSurplusQty());
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelPickPlan(SoHeader soHeader, List<Long> soPickPlanIdList) {
		AssertUtil.notEmpty(soPickPlanIdList, "全部取消分配失败,待取消的拣货计划参数为空");

		List<SoPickPlan> soPickPlan = soPickPlanDao.getByPickPlanIds(soPickPlanIdList);
		cancelPickPlan(soPickPlan, soHeader);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelPickPlan(WmsTask task, SoHeader soHeader) {
		List<SoPickPlan> soPickPlanList = soPickPlanDao.getPickByTaskId(task.getTaskId());
		cancelPickPlan(soPickPlanList, soHeader);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelPickPlanByClose(SoHeader soHeader) {
		List<SoPickPlan> soPickPlan = soPickPlanDao.findBySoHeaderId(soHeader.getSoBillId());
		if (Func.isNotEmpty(soPickPlan)) {
			cancelPickPlan(soPickPlan, soHeader);
		}
	}

	@Override
	public Page<SoPickPlanPageResponse> page(Query query, SoPickPlanPageQuery soPickPlanPageQuery) {
		return soPickPlanDao.getPage(Condition.getPage(query), soPickPlanPageQuery);
	}

	@Override
	public void export(SoPickPlanPageQuery soPickPlanPageQuery, HttpServletResponse response) {
		IPage<Object> page = new Page<>();
		page.setCurrent(1);
		page.setSize(100000);
		List<SoPickPlanPageResponse> soPickPlanPageResponseList = soPickPlanDao.getPage(page, soPickPlanPageQuery)
			.getRecords();
		ExcelUtil.export(response, "分配记录", "分配记录数据表", soPickPlanPageResponseList, SoPickPlanPageResponse.class);
	}

	@Override
	public List<SoPickPlan> findByStockIdsAndSoBillId(List<Long> stockIdList, Long soBillId) {
		return soPickPlanDao.getByStockIdsAndSoBillId(stockIdList, soBillId);
	}

	@Override
	public void updatePickByPartParam(Long pickPlanId, Long stockId, Location location, Zone zone, String boxCode, BigDecimal stockBalance) {
		AssertUtil.notNull(pickPlanId, "修改拣货计划失败，拣货计划ID为空");
		AssertUtil.notNull(stockId, "修改拣货计划失败，库存ID为空");
		AssertUtil.notNull(location, "修改拣货计划失败，库位为空");
		AssertUtil.notNull(zone, "修改拣货计划失败，库区为空");
		soPickPlanDao.updatePickByPartParam(pickPlanId, stockId, location, zone, boxCode, stockBalance);
	}

	@Override
	public List<SoPickPlan> findPickByTaskId(Long taskId, Long stockId) {
		return soPickPlanDao.getPickByTaskIdAndStockId(taskId, stockId);
	}

	@Override
	public void updatePickByTaskIdAndStockId(Long taskId, Long sourceStockId, Long targetStockId, Location location, Zone zone) {
		AssertUtil.notNull(taskId, "修改拣货计划失败，任务ID为空");
		AssertUtil.notNull(sourceStockId, "修改拣货计划失败，原库存ID为空");
		AssertUtil.notNull(targetStockId, "修改拣货计划失败，目标库存ID为空");
		AssertUtil.notNull(location, "修改拣货计划失败，库位为空");
		AssertUtil.notNull(zone, "修改拣货计划失败，库区为空");
		soPickPlanDao.updatePickByTaskIdAndStockId(taskId, sourceStockId, targetStockId, location, zone);
	}

	@Override
	public void deletePickByPickPlanId(Long pickPlanId) {
		AssertUtil.notNull(pickPlanId, "根据拣货计划ID删除拣货拣货计划失败，拣货计划ID为空");
		soPickPlanDao.removeById(pickPlanId);
	}

	@Override
	public List<SoPickPlan> findPickBySoDetailId(Long soDetailId) {
		return soPickPlanDao.selectPickBySoDetailId(soDetailId);
	}

	@Override
	public List<SoPickPlan> findSoPickPlanByBoxCode(Long soBillId, String boxCode) {
		return soPickPlanDao.selectSoPickPlanByBoxCode(soBillId, boxCode);
	}

	@Override
	public SoPickPlan findSoPickPlanById(Long pickPlanId) {
		return soPickPlanDao.selectSoPickPlanById(pickPlanId);
	}

	@Override
	public List<SoPickPlan> findSoPickPlanByBoxCode(String boxCode) {
		return soPickPlanDao.selectSoPickPlansByBoxCode(boxCode);
	}

	@Override
	public void setTaskId(List<SoPickPlan> soPickPlanList, Long taskId) {
		AssertUtil.notEmpty(soPickPlanList, "更新拣货计划中的任务id失败,拣货计划集合参数为空");
		soPickPlanDao.updateTask(soPickPlanList, taskId);
	}

	@Override
	public void updatePlanOfStock(SoPickPlan soPickPlan, Stock newStock, Stock oldStock) {
		AssertUtil.notNull(soPickPlan, "更新拣货计划中的库存失败,拣货计划参数为空");
		AssertUtil.notNull(newStock, "更新拣货计划中的库存失败,新库存参数为空");
		AssertUtil.notNull(oldStock, "更新拣货计划中的库存失败,旧库存参数为空");

		// 释放原库存中的占用量
		stockBiz.reduceOccupy(soPickPlan.getSoBillId(), soPickPlan.getSoBillNo(),
			soPickPlan.getSoDetailId(), oldStock.getStockId(), soPickPlan.getSurplusQty());
		// 更新拣货计划
		soPickPlanDao.updatePlanOfStock(soPickPlan, newStock);
		// 占用新的库存
		stockBiz.occupyStock(Collections.singletonList(soPickPlan));
	}

	private String createResultByRunPickStrategy(List<SoPickPlan> newPickPlans,
												 List<SoPickPlan> oldPickPlans,
												 SoDetail detail, String result) {
		List<SoPickPlan> allPickPlans = new ArrayList<>();
		if (Func.isNotEmpty(newPickPlans)) {
			allPickPlans.addAll(newPickPlans);
		}
		if (Func.isNotEmpty(oldPickPlans)) {
			allPickPlans.addAll(oldPickPlans);
		}
		if (Func.isEmpty(allPickPlans)) {
			return String.format("%s %s行库存不足未分配", result, detail.getSoLineNo());
		}

		BigDecimal occupy = allPickPlans.stream()
			.filter(item -> item.getSoDetailId().equals(detail.getSoDetailId()))
			.map(SoPickPlan::getSurplusQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		if (BigDecimalUtil.lt(occupy, detail.getSurplusQty())) {
			return String.format("%s %s行库存不足,部分分配", result, detail.getSoLineNo());
		}

		return result;
	}

}
