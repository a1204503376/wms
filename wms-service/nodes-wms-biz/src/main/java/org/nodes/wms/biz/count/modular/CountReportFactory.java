package org.nodes.wms.biz.count.modular;

import org.nodes.core.base.cache.ParamCache;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.count.entity.CountHeader;
import org.nodes.wms.dao.count.entity.CountRecord;
import org.nodes.wms.dao.count.entity.CountReport;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 盘点生成差异报告工厂对象
 *
 * @author nodes
 */
@Component
public class CountReportFactory {
	/**
	 * 盘点生成差异报告构造工厂
	 *
	 * @param countHeader countHeader
	 * @param countRecord countRecord
	 * @param location    location
	 * @param sku         sku
	 * @param stock       stock
	 * @return 盘点差异报告表实体类
	 */
	public CountReport createCountReport(CountHeader countHeader,
										 CountRecord countRecord,
										 Location location,
										 Sku sku,
										 Stock stock) {
		CountReport countReport = new CountReport();
		countReport.setWhId(countHeader.getWhId());
		countReport.setCountBillId(countHeader.getCountBillId());
		countReport.setCountBillNo(countHeader.getCountBillNo());
		countReport.setLocId(countRecord.getLocId());
		countReport.setLocCode(countRecord.getLocCode());
		countReport.setSkuId(sku.getSkuId());
		countReport.setSkuCode(countRecord.getSkuCode());
		countReport.setSkuName(countRecord.getSkuName());
		countReport.setCountQty(countRecord.getCountQty());
		countReport.setSystemLot(stock.getSkuLot1());
		countReport.setCountLot(stock.getSkuLot1());
		if (Func.isNotEmpty(stock.getLpnCode())) {
			countReport.setLpnCode(countRecord.getLpnCode());
		}
		if (Func.isNotEmpty(countRecord)) {
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				countReport.skuLotSet(i, countRecord.skuLotGet(i));
			}
		}
		if (Func.isNotEmpty(stock)) {
			countReport.setStockId(stock.getStockId());
			countReport.setWspId(stock.getWspId());

			countReport.setWmsQty(stock.getStockQty().subtract(stock.getPickQty()));
		} else {
			countReport.setWspId(sku.getWspId());
			countReport.setWmsQty(BigDecimal.ZERO);
		}
		// 获取物品单位
		countReport.setWsuName(sku.getWspName());
		return countReport;
	}

}
