package org.nodes.wms.biz.outstock.plan.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.strategy.TianyiPickStrategy;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

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

	private final TianyiPickStrategy tianyiPickStrategy;
	private final SoPickPlanDao soPickPlanDao;
	private final StockBiz stockBiz;

	@Override
	public boolean hasPickPlan(Long soHeaderId) {
		// TODO
		return false;
	}

    @Override
    public List<SoPickPlan> findBySoHeaderId(Long soHeaderId) {
		// TODO
        return null;
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
			if (Func.isNotEmpty(newPickPlan)){
				stockBiz.occupyStock(newPickPlan);
				soPickPlanDao.saveBatch(newPickPlan);
				existPickPlans.addAll(newPickPlan);
			}
		}

		return result;
	}

	private String createResultByRunPickStrategy(List<SoPickPlan> newPickPlan, SoDetail detail, String result) {
		if (Func.isEmpty(newPickPlan)){
			return String.format("%s,%s行库存不足未分配", result, detail.getSoLineNo());
		}

		BigDecimal occupy = newPickPlan.stream()
			.filter(item -> item.getSoDetailId().equals(detail.getSoDetailId()))
			.map(SoPickPlan::getSurplusQty)
			.reduce(BigDecimal::add)
			.orElse(BigDecimal.ZERO);
		if (BigDecimalUtil.ne(occupy, detail.getSurplusQty())){
			return String.format("%s,%s行库存不足,部分分配", result, detail.getSoLineNo());
		}

		return result;
	}

}
