package org.nodes.wms.biz.outstock.plan.impl;

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
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
	public String runByPickStrategy(SoHeader soHeader, List<SoDetail> soDetials, List<SoPickPlan> existPickPlans) {
		String result = "";

		for (SoDetail detail : soDetials) {
			List<SoPickPlan> pickPlanOfSoDetail = null;
			if (Func.notNull(existPickPlans)) {
				pickPlanOfSoDetail = existPickPlans.stream()
					.filter(soPickPlan -> detail.getSoDetailId().equals(soPickPlan.getSoDetailId()))
					.collect(Collectors.toList());
			}
			List<SoPickPlan> newPickPlan = tianyiPickStrategy.run(soHeader, detail, soDetials, pickPlanOfSoDetail);
			result = createResultByRunPickStrategy(newPickPlan, detail, result);
			if (Func.isNotEmpty(newPickPlan)) {
				stockBiz.occupyStock(newPickPlan);
				soPickPlanDao.saveBatch(newPickPlan);
				existPickPlans.addAll(newPickPlan);
			}
		}

		return result;
	}

	@Override
	public void updatePickRealQty(Long pickPlanId, BigDecimal pickRealQty) {
		AssertUtil.notNull(pickPlanId, "修改拣货计划失败,拣货计划ID为空");
		AssertUtil.notNull(pickRealQty, "修改拣货计划失败,拣货量为空");
		soPickPlanDao.updatePickRealQty(pickPlanId, pickRealQty);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public LogSoPick pickByPlan(SoDetail soDetail, SoPickPlan pickPlan, BigDecimal pickQty, List<String> serialNoList) {
		AssertUtil.notNull(pickPlan, "按计划拣货失败,拣货计划参数为空");
		AssertUtil.notNull(pickQty, "按拣货计划拣货失败，拣货数量参数为空");

		if (Func.isNull(pickPlan.getPickRealQty())){
			pickPlan.setPickRealQty(BigDecimal.ZERO);
		}

		Stock stock = stockQueryBiz.findStockById(pickPlan.getStockId());
		checkPickByPlan(pickPlan, pickQty, serialNoList, stock);
		// 1.释放库存占用
		stock.setOccupyQty(stock.getOccupyQty().subtract(pickQty));
		// 2.移动库存到出库暂存区
		Location pickToLocation = locationBiz.getLocationByZoneType(
			pickPlan.getWhId(), DictKVConstant.ZONE_TYPE_PICK_TO).get(0);
		stockBiz.moveStock(stock, serialNoList, pickQty, pickToLocation, StockLogTypeEnum.OUTSTOCK_BY_PICK_PLAN,
			pickPlan.getSoBillId(),	pickPlan.getSoBillNo(), soDetail.getSoLineNo());
		// 3.更新拣货计划
		updatePickRealQty(pickPlan.getPickPlanId(), pickPlan.getPickRealQty().add(pickQty));
		// 4.生产并保存拣货记录
		LogSoPick logSoPick = logSoPickFactory.create(soDetail, pickPlan, pickQty, serialNoList, stock);
		logSoPickDao.save(logSoPick);
		return logSoPick;
	}

	private void checkPickByPlan(SoPickPlan pickPlan, BigDecimal pickQty, List<String> serialNoList, Stock stock){
		if (BigDecimalUtil.ge(pickQty, BigDecimal.ZERO)){
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货数量必须大于0");
		}

		if (BigDecimalUtil.gt(pickQty, pickPlan.getSurplusQty())){
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货数量超过计划量[%f]", pickPlan.getSurplusQty());
		}

		if (Func.isNotEmpty(serialNoList) && serialNoList.size() != pickQty.intValue()){
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货数量[%f]与拣货序列号个数[%d]不符", pickQty, serialNoList.size());
		}

		if (BigDecimalUtil.gt(pickQty, stock.getStockBalance())){
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货量超过库存[%d]的余额[%f]", stock.getStockId(), stock.getStockBalance());
		}

		if (BigDecimalUtil.gt(pickQty, stock.getOccupyQty())){
			throw ExceptionUtil.mpe("按拣货计划拣货失败,拣货量[%f]超过库存[%d]的占用量[%f]",
				pickQty, stock.getStockId(), stock.getOccupyQty());
		}

		List<Serial> serialOfStock = stockQueryBiz.findSerialByStock(stock.getStockId());
		if (Func.isNotEmpty(serialOfStock) && Func.isEmpty(serialNoList)){
			throw ExceptionUtil.mpe("按拣货计划拣货失败,库存[%s]是按序列号管理的,请采集出库的序列号", stock.getStockId());
		}

		if (Func.isNotEmpty(serialOfStock) && Func.isNotEmpty(serialNoList)){
			List<String> serianNoOfStock = serialOfStock.stream()
				.map(Serial::getSerialNumber)
				.collect(Collectors.toList());
			if (!serialNoList.containsAll(serialNoList)) {
				throw ExceptionUtil.mpe("按拣货计划拣货失败, 存在无效的拣货序列号");
			}
		}

		if (Func.isEmpty(serialOfStock) && Func.isNotEmpty(serialNoList)){
			throw ExceptionUtil.mpe("按拣货计划拣货失败,库存[%s]不是按序列号管理的,序列号无效", stock.getStockId());
		}
	}

	@Override
	public List<SoPickPlan> findPickByTaskId(Long taskId) {
		return soPickPlanDao.getPickByTaskId(taskId);
	}

	private String createResultByRunPickStrategy(List<SoPickPlan> newPickPlan, SoDetail detail, String result) {
		if (Func.isEmpty(newPickPlan)) {
			return String.format("%s,%s行库存不足未分配", result, detail.getSoLineNo());
		}

		BigDecimal occupy = newPickPlan.stream()
			.filter(item -> item.getSoDetailId().equals(detail.getSoDetailId()))
			.map(SoPickPlan::getSurplusQty)
			.reduce(BigDecimal::add)
			.orElse(BigDecimal.ZERO);
		if (BigDecimalUtil.ne(occupy, detail.getSurplusQty())) {
			return String.format("%s,%s行库存不足,部分分配", result, detail.getSoLineNo());
		}

		return result;
	}

}
