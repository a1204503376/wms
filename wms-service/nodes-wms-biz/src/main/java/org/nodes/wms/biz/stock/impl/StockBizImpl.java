package org.nodes.wms.biz.stock.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.tool.utils.ConvertUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 **/
@Service
@RequiredArgsConstructor
public class StockBizImpl implements StockBiz {

	private final StockDao stockDao;

	private final LocationBiz locationBiz;

	@Override
	public void freezeByLoc(StockLogTypeEnum type, Long locId) {

	}

	@Override
	public void unfreezeByLoc(StockLogTypeEnum type, Long locId) {

	}

	@Override
	public Stock inStock(StockLogTypeEnum type, ReceiveLog receiveLog) {
		return null;
	}

	@Override
	public Stock moveStock(Stock sourceStock, List<String> serialNoList,
						   BigDecimal qty, Location targetLocation, StockLogTypeEnum type) {
		return null;
	}

	@Override
	public List<Stock> findStockByBoxCode(String boxCode) {
		return null;
	}

	@Override
	public StockIndexResponse staticsStockDataOnIndexPage() {
		// 获取所有入库暂存区库位
		List<Location> allStageList = locationBiz.getAllStage();
		// 获取所有入库检验区库位
		List<Location> allQcList = locationBiz.getAllQc();
		// 获取所有出库暂存区库位
		List<Location> allPickToList = locationBiz.getAllPickTo();
		// 根据入库暂存区id获取入库暂存区的物品数量和存放天数
		Map<String, Object> stageStock = stockDao.getStockQtyByLocIdList(
			allStageList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// 根据入库检验区id获取入库检验区的物品数量和存放天数
		Map<String, Object> qcStock = stockDao.getStockQtyByLocIdList(
			allQcList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// 根据出库暂存区id获取库存中不是入库暂存区的物品总数
		int stockSkuCount = stockDao.getStockSkuCountByLocIdList(
			allPickToList.stream().map(Location::getLocId).collect(Collectors.toList()));
		// 查询库位总数
		int locCount = locationBiz.countAll();
		StockIndexResponse response = new StockIndexResponse();

		if (Func.isNotEmpty(stageStock)) {
			response.setStageSkuQty(ConvertUtil.convert(stageStock.get("skuQty"), BigDecimal.class));
			response.setStageSkuStoreDay(ConvertUtil.convert(stageStock.get("skuStoreDay"), Integer.class));
		} else {
			response.setStageSkuQty(new BigDecimal(0));
			response.setStageSkuStoreDay(0);
		}

		if (Func.isNotEmpty(qcStock)) {
			response.setQcSkuQty(ConvertUtil.convert(qcStock.get("skuQty"), BigDecimal.class));
			response.setQcSkuStoreDay(ConvertUtil.convert(qcStock.get("skuStoreDay"), Integer.class));
		} else {
			response.setQcSkuQty(new BigDecimal(0));
			response.setQcSkuStoreDay(0);
		}

		response.setStockSkuCount(stockSkuCount);
		if (new Integer(0).equals(stockSkuCount)) {
			response.setLocOccupy((double) 0);
		} else {
			BigDecimal decimal = new BigDecimal((double) stockSkuCount / locCount * 100);
			response.setLocOccupy(decimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
		}
		return response;
	}

	@Override
	public Stock findStockBySku(String skuCode, String skuName, String locCode) {
		return stockDao.getStockBySku(skuCode, skuName, skuCode);
	}

}
