package org.nodes.wms.biz.outstock.plan.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nodesc
 */
@Component
@RequiredArgsConstructor
public class SoPickPlanFactory {

	public List<SoPickPlan> createOnAgvArea(SoDetail soDetail, List<SoDetail> soDetailList,
											Stock stock, List<Stock> stockOfLoc) {
		List<SoPickPlan> result = new ArrayList<>();
		for (Stock item : stockOfLoc) {
			SoDetail currentSoDetail = soDetailList.stream()
				.filter(soDetailItem -> {
					if (Func.isNotEmpty(soDetailItem.getSkuLot1()) && !soDetailItem.getSkuLot1().equals(item.getSkuLot1())){
						return false;
					}
					if (Func.isNotEmpty(soDetailItem.getSkuLot2()) && !soDetailItem.getSkuLot2().equals(item.getSkuLot2())){
						return false;
					}
					if ((Func.isNotEmpty(soDetailItem.getSkuLot4()) && !soDetailItem.getSkuLot4().equals(item.getSkuLot4()))){
						return false;
					}

					return item.getSkuId().equals(soDetailItem.getSkuId())
						&& BigDecimalUtil.gt(soDetailItem.getSurplusQty(), BigDecimal.ZERO);
				})
				.findFirst()
				.orElse(null);
			SoPickPlan soPickPlan = create(soDetail.getSoBillId(), currentSoDetail, item, item.getStockEnable());
			result.add(soPickPlan);
		}

		return result;
	}

	public SoPickPlan create(Long soHeaderId, SoDetail soDetail, Stock stock, BigDecimal planQty) {
		if (BigDecimalUtil.le(planQty, BigDecimal.ZERO)) {
			throw ExceptionUtil.mpe("创建拣货计划失败,计划数量不能小于等于0");
		}

		SoPickPlan soPickPlan = Func.copy(stock, SoPickPlan.class);
		if (Func.notNull(soDetail)) {
			soPickPlan.setSoDetailId(soDetail.getSoDetailId());
		}
		soPickPlan.setLotNumber(stock.getSkuLot1());
		soPickPlan.setWellenId(0L);
		soPickPlan.setSoBillId(soHeaderId);
		soPickPlan.setSoBillNo(soDetail.getSoBillNo());
		soPickPlan.setPickPlanQty(planQty);
		soPickPlan.setPickRealQty(BigDecimal.ZERO);
		return soPickPlan;
	}
}
