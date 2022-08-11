package org.nodes.wms.biz.count.modular;

import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.count.dto.input.GenerateCountReport;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.entity.CountReport;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Component;

import java.util.List;

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
	 * @param generateCountReport 生成盘点差异报告请求对象
	 * @param stock               库存对象
	 * @param countDetail         盘点单明细表
	 * @return 盘点差异报告表实体类
	 */
	public CountReport createCountReport(GenerateCountReport generateCountReport, Stock stock, CountDetail countDetail) {
		CountReport countReport = BeanUtil.copy(stock, CountReport.class);
		countReport.setSystemLot(stock.getSkuLot1());
		countReport.setCountLot(stock.getSkuLot1());
		countReport.setCountBillId(countDetail.getCountBillId());
		countReport.setCountBillNo(countDetail.getCountBillNo());
		countReport.setWmsQty(stock.getStockBalance());
		countReport.setCountQty(generateCountReport.getStockBalance());
		return countReport;
	}

	/**
	 * 盘点生成差异报告构造工厂
	 *
	 * @param stockList               库存集合对象
	 * @param countDetail         盘点单明细表
	 * @return 盘点差异报告表实体类
	 */
	public CountReport createCountReport(List<Stock> stockList, CountDetail countDetail) {
		CountReport countReport = BeanUtil.copy(stockList.get(0), CountReport.class);
		countReport.setSystemLot(stockList.get(0).getSkuLot1());
		countReport.setCountLot(stockList.get(0).getSkuLot1());
		countReport.setCountBillId(countDetail.getCountBillId());
		countReport.setCountBillNo(countDetail.getCountBillNo());
		countReport.setWmsQty(StockUtil.getStockBalance(stockList));
		return countReport;
	}
}
