package org.nodes.wms.core.count.wrapper;

import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.entity.CountReport;
import org.nodes.wms.core.count.vo.CountReportVO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author pengwei
 * @description 盘点差异报告封装类
 * @since 2020-06-18
 */
public class CountReportWrapper extends BaseEntityWrapper<CountReport, CountReportVO> {

	public static CountReportWrapper build() {
		return new CountReportWrapper();
	}

	@Override
	public CountReportVO entityVO(CountReport entity) {
		return BeanUtil.copy(entity, CountReportVO.class);
	}


	/**
	 * 封装 CountReport 实体
	 *
	 * @param countHeader 盘点单表头
	 * @param countRecord 盘点记录信息
	 * @param location    库位信息
	 * @param sku         物品信息
	 * @param stock       库存信息
	 * @param serialNumber      序列号
	 * @return CountReport
	 */
	public CountReport entity(
		@NotNull CountHeader countHeader,
		CountRecord countRecord,
		@NotNull Location location,
		@NotNull Sku sku,
		Stock stock,
		String serialNumber) {

		CountReport countReport = new CountReport();
		countReport.setWhId(countHeader.getWhId());
		countReport.setCountBillId(countHeader.getCountBillId());
		countReport.setCountBillNo(countHeader.getCountBillNo());
		countReport.setLocId(location.getLocId());
		countReport.setLocCode(location.getLocCode());
		countReport.setSkuId(sku.getSkuId());
		countReport.setSkuCode(sku.getSkuCode());
		countReport.setSkuName(sku.getSkuName());
		countReport.setCountQty(BigDecimal.ZERO);
		if (Func.isNotEmpty(countRecord)) {
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				countReport.skuLotSet(i, countRecord.skuLotGet(i));
			}
		}
		if (Func.isNotEmpty(stock)) {
			countReport.setStockId(stock.getStockId());
			countReport.setWspId(stock.getWspId());
			IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
			StockDetail stockDetail = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.eq(StockDetail::getStockId,stock.getStockId())).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(stockDetail.getLpnCode())){
				countReport.setLpnCode(stockDetail.getLpnCode());
			}
			countReport.setLpnCode(StringPool.SPACE);
			countReport.setWmsQty(stock.getStockQty().subtract(stock.getPickQty()));
			countReport.setSystemLot(stock.getLotNumber());
		} else {
			countReport.setWspId(sku.getWspId());
			countReport.setLpnCode(StringPool.SPACE);
			countReport.setWmsQty(BigDecimal.ZERO);
			countReport.setSystemLot(StringPool.SPACE);
		}
		// 获取物品最小单位
		SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(sku.getWspId(), 1);
		if (Func.isEmpty(packageDetail)) {
			throw new ServiceException("物品: " + sku.getSkuName() + " 未找到库存单位！");
		}
		countReport.setWsuName(packageDetail.getWsuName());
		if (Func.isNotEmpty(serialNumber)) {
			countReport.setSerialNumber(serialNumber);
		}

		return countReport;
	}
}
