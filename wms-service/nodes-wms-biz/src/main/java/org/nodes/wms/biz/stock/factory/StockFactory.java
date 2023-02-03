package org.nodes.wms.biz.stock.factory;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.entities.StockBalance;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StockFactory {
	private final SkuBiz skuBiz;
	private final ZoneBiz zoneBiz;


	public Stock create(ReceiveLog receiveLog, Location location) {
		AssertUtil.notNull(receiveLog, "创建库存失败，清点记录为空");
		AssertUtil.notNull(location, "创建库存失败，目标为空");
		if (Func.isNull(receiveLog.getStockStatus())) {
			throw ExceptionUtil.mpe("生成库存失败,库存状态不能为空");
		}

		Stock stock = new Stock();
		SkuLotUtil.setAllSkuLot(receiveLog, stock);
		stock.setLastInTime(LocalDateTime.now());
		stock.setStockStatus(receiveLog.getStockStatus());
		stock.setSkuLevel(receiveLog.getSkuLevel());
		stock.setWsuCode(receiveLog.getWsuCode());
		Sku sku = skuBiz.findById(receiveLog.getSkuId());
		if (Func.isNotEmpty(sku)) {
			stock.setWspName(sku.getWspName());
		}
		stock.setWspId(receiveLog.getWspId());
		stock.setSkuId(receiveLog.getSkuId());
		stock.setSkuCode(receiveLog.getSkuCode());
		stock.setSkuName(receiveLog.getSkuName());
		stock.setStayStockQty(BigDecimal.ZERO);
		stock.setStockQty(receiveLog.getQty());
		stock.setPickQty(BigDecimal.ZERO);
		stock.setOccupyQty(BigDecimal.ZERO);
		stock.setBoxCode(receiveLog.getBoxCode());
		stock.setLpnCode(receiveLog.getLpnCode());
		stock.setLocCode(receiveLog.getLocCode());
		stock.setLocId(receiveLog.getLocId());
		stock.setZoneId(location.getZoneId());
		Zone zone = zoneBiz.findById(location.getZoneId());
		if (Func.isNotEmpty(zone)) {
			stock.setZoneCode(zone.getZoneCode());
		}
		stock.setWhId(receiveLog.getWhId());
		stock.setWhCode(receiveLog.getWhCode());
		stock.setWoId(receiveLog.getWoId());

		if (Func.isNotEmpty(receiveLog.getSnCode())) {
			stock.setHasSerial(WmsAppConstant.TRUE_DEFAULT);
		} else {
			stock.setHasSerial(WmsAppConstant.FALSE_DEFAULT);
		}

		return stock;
	}

	public Stock create(Stock sourceStock, Location targetLocation,
						String targetLpnCode, String targetBoxCode, BigDecimal qty, List<String> serialNoList) {
		AssertUtil.notNull(sourceStock, "创建库存失败，原库存为空");
		AssertUtil.notNull(targetLocation, "创建库存失败，目标库位为空");
		AssertUtil.notNull(qty, "创建库存失败，数量为空");

		Stock targetStock = new Stock();
		BeanUtil.copy(sourceStock, targetStock);
		StockUtil.resetStockInfo(targetStock);

		targetStock.setStockQty(qty);
		targetStock.setLocId(targetLocation.getLocId());
		targetStock.setLocCode(targetLocation.getLocCode());
		targetStock.setZoneId(targetLocation.getZoneId());
		targetStock.setLpnCode(targetLpnCode);
		targetStock.setBoxCode(targetBoxCode);
		Zone zone = zoneBiz.findById(targetLocation.getZoneId());
		targetStock.setZoneCode(zone.getZoneCode());
		targetStock.setLastInTime(LocalDateTime.now());

		if (Func.isNotEmpty(serialNoList)) {
			targetStock.setHasSerial(WmsAppConstant.TRUE_DEFAULT);
		} else {
			targetStock.setHasSerial(WmsAppConstant.FALSE_DEFAULT);
		}

		return targetStock;
	}

	public static boolean compare(ReceiveLog receiveLog, LogSoPick logSoPick) {
		if (receiveLog.getSkuId().equals(logSoPick.getSkuId()) && receiveLog.getWhId().equals(logSoPick.getWhId())
			&& receiveLog.getWoId().equals(logSoPick.getWoId()) && receiveLog.getWspId().equals(logSoPick.getWspId())
			&& receiveLog.getSkuLevel().equals(logSoPick.getSkuLevel()) && SkuLotUtil.compareAllSkuLot(receiveLog, logSoPick)
		) {
			return true;
		}
		return false;

	}

	public static StockBalance createStockBalabce(ReceiveLog receiveLog, LogSoPick logSoPick) throws ParseException {
		StockBalance stockBalance = new StockBalance();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String balanceDate = simpleDateFormat.format(calendar.getTime());
		stockBalance.setBalanceDate(simpleDateFormat.parse(balanceDate));
		if (Func.isNotEmpty(receiveLog) && Func.isNotEmpty(logSoPick)) {
			Func.copy(receiveLog, stockBalance);
			stockBalance.setInStockQty(receiveLog.getQty());
			stockBalance.setOutStockQty(logSoPick.getPickRealQty());
		} else if (Func.isNotEmpty(receiveLog) && Func.isEmpty(logSoPick)) {
			Func.copy(receiveLog, stockBalance);
			stockBalance.setInStockQty(receiveLog.getQty());
			stockBalance.setOutStockQty(BigDecimal.ZERO);
		} else if (Func.isEmpty(receiveLog) && Func.isNotEmpty(logSoPick)) {
			Func.copy(logSoPick, stockBalance);
			stockBalance.setInStockQty(BigDecimal.ZERO);
			stockBalance.setOutStockQty(logSoPick.getPickRealQty());
		}
		stockBalance.setStockStatus(StockStatusEnum.NORMAL);
		stockBalance.setOpeningQty(BigDecimal.ZERO);
		stockBalance.setBalanceQty(stockBalance.getInStockQty().subtract(stockBalance.getOutStockQty()));
		return stockBalance;
	}

	public static void AssigntoArray(List<ReceiveLog> receiveLogList, List<ReceiveLog> receiveLogList1,
									 List<LogSoPick> logSoPickList, List<LogSoPick> logSoPickList1,
									 List<StockBalance> stockBalanceList1) throws ParseException {
		if (logSoPickList.size() == 0) {
			receiveLogList1.addAll(receiveLogList);
		}
		for (int i = 0; i < receiveLogList.size(); i++) {
			for (int j = 0; j < logSoPickList.size(); j++) {
				if (compare(receiveLogList.get(i), logSoPickList.get(j))) {
					StockBalance stockBalance = StockFactory.createStockBalabce(receiveLogList.get(i), logSoPickList.get(j));
					stockBalanceList1.add(stockBalance);
					logSoPickList1.set(j, null);
					break;
				}
				if (j == logSoPickList.size() - 1) {
					receiveLogList1.add(receiveLogList.get(i));
				}
			}
		}
		for (ReceiveLog receiveLog : receiveLogList1) {
			StockBalance stockBalance = StockFactory.createStockBalabce(receiveLog, null);
			stockBalanceList1.add(stockBalance);
		}
		for (LogSoPick logSoPick : logSoPickList1) {
			if (Func.isNotEmpty(logSoPick)) {
				StockBalance stockBalance = StockFactory.createStockBalabce(null, logSoPick);
				stockBalanceList1.add(stockBalance);
			}
		}
	}

	public static List<StockBalance> createStockBalanceList(List<StockBalance> stockBalanceList, List<StockBalance> stockBalanceList1) throws ParseException {
		if (stockBalanceList.size() == 0) {
			return stockBalanceList1;
		}
		List<StockBalance> stockBalanceList3 = new ArrayList<>();
		for (int i = 0; i < stockBalanceList1.size(); i++) {
			for (int j = 0; j < stockBalanceList.size(); j++) {
				StockBalance stockBalance = stockBalanceList1.get(i);
				if (compare(stockBalanceList1.get(i), stockBalanceList.get(j))) {
					stockBalance.setOpeningQty(stockBalanceList.get(j).getBalanceQty());
					stockBalance.setBalanceQty(stockBalance.getOpeningQty()
						.add(stockBalance.getInStockQty().subtract(stockBalance.getOutStockQty())));
					stockBalanceList.remove(j);
					stockBalanceList3.add(stockBalance);
					break;
				}
				if (j == stockBalanceList.size() - 1) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.DATE, 1);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String balanceDate = simpleDateFormat.format(calendar.getTime());
					stockBalance.setBalanceDate(simpleDateFormat.parse(balanceDate));
					stockBalanceList3.add(stockBalance);
				}
			}
		}
		for (StockBalance stockBalance : stockBalanceList) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DATE, 1);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String balanceDate = simpleDateFormat.format(calendar.getTime());
			stockBalance.setBalanceDate(simpleDateFormat.parse(balanceDate));
			stockBalance.setOpeningQty(stockBalance.getBalanceQty());
			stockBalance.setInStockQty(BigDecimal.ZERO);
			stockBalance.setOutStockQty(BigDecimal.ZERO);
			stockBalance.setId(null);
			stockBalanceList3.add(stockBalance);
		}
		return stockBalanceList3;
	}

	private static boolean compare(StockBalance stockBalance, StockBalance stockBalance1) {
		if (Objects.equals(stockBalance.getSkuId(), stockBalance1.getSkuId()) && Objects.equals(stockBalance.getWhId(), stockBalance1.getWhId())
			&& Objects.equals(stockBalance.getWoId(), stockBalance1.getWoId()) && Objects.equals(stockBalance.getWspId(), stockBalance1.getWspId())
			&& stockBalance.getSkuLevel().equals(stockBalance1.getSkuLevel()) && SkuLotUtil.compareAllSkuLot(stockBalance, stockBalance1)
		) {
			return true;
		}
		return false;
	}
}
