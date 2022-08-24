package org.nodes.wms.biz.outstock.plan.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nodesc
 */
@Component
@RequiredArgsConstructor
public class SoPickPlanFactory {

	public List<SoPickPlan> createOnAgvArea(SoDetail soDetail, List<SoDetail> soDetailList,
											Stock stock, List<Stock> stockOfLoc) {
		List<SoPickPlan> result = new ArrayList<>();
		result.add(create(soDetail.getSoBillId(), soDetail, stock, stock.getStockEnable()));

		for (Stock item : stockOfLoc) {
			if (item.getStockId().equals(stock.getStockId())) {
				continue;
			}

			List<SoDetail> soDetailOfSku = soDetailList.stream()
				.filter(soDetailItem -> item.getSkuId().equals(soDetailItem.getSkuId()))
				.collect(Collectors.toList());
			SoDetail currentSoDetail = null;
			if (Func.isNotEmpty(soDetailOfSku)) {
				currentSoDetail = soDetailOfSku.get(0);
			}
			result.add(create(soDetail.getSoBillId(), currentSoDetail, item, item.getStockEnable()));
		}

		return result;
	}

	public SoPickPlan create(Long soHeaderId, SoDetail soDetail, Stock stock, BigDecimal planQty) {
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
