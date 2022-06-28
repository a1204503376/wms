package org.nodes.wms.biz.stock.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.ConvertUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockBizImpl implements StockBiz {

	private final StockDao stockDao;
	private final ZoneBiz zoneBiz;
	private final LocationBiz locationBiz;
	private final SkuBiz skuBiz;

	@Override
	public void freezeByLoc(StockLogTypeEnum type, Long locId) {

	}

	@Override
	public void unfreezeByLoc(StockLogTypeEnum type, Long locId) {

	}

	private <T> void canInStockByLocation(Location location, Long skuId, T skuLotObject) {
		if (Func.isEmpty(location)) {
			throw new ServiceException("新增库存失败,库位不存在");
		}
		// 库位是否冻结
		if (locationBiz.isFrozen(location)) {
			throw new ServiceException("新增库存失败,库位被冻结");
		}
		// 库位是否允许混放物品
		if (!locationBiz.isMixSku(location)) {
			List<Stock> stockList = stockDao.getStockByLocId(location.getLocId());
			if (Func.isNotEmpty(stockList)) {
				for (Stock stock : stockList) {
					if (!stock.getSkuId().equals(skuId)) {
						throw new ServiceException("新增库存失败,库位不允许混放物品");
					}
				}
			}
		}

		// 库位是否允许混放批次
		if (!locationBiz.isMixSkuLot(location)) {
			List<Stock> stockList = stockDao.getStockByLocId(location.getLocId());
			if (Func.isNotEmpty(stockList)) {
				for (Stock stock : stockList) {
					if (!SkuLotUtil.compareAllSkuLot(stock, skuLotObject)) {
						throw new ServiceException("新增库存失败,库位不允许混放批次");
					}
				}
			}
		}
	}

	@Override
	public Stock inStock(StockLogTypeEnum type, ReceiveLog receiveLog) {
		Location location = locationBiz.findByLocId(receiveLog.getLocId());
		canInStockByLocation(location, receiveLog.getSkuId(), receiveLog);
		Stock stock = createStock(receiveLog, location);


		// 形成库存，需要考虑库存合并

		// 形成序列号信息


		return null;
	}

	private Stock createStock(ReceiveLog receiveLog, Location location) {
		Stock stock = new Stock();
		SkuLotUtil.setAllSkuLot(receiveLog, stock);
		stock.setLastInTime(LocalDateTime.now());
		stock.setStockStatus(StockStatusEnum.NORMAL);
		stock.setSkuLevel(receiveLog.getSkuLevel());
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

		return stock;
	}

	@Override
	public Stock moveStock(Stock sourceStock, List<String> serialNoList,
						   BigDecimal qty, Location targetLocation, StockLogTypeEnum type) {
		return null;
	}

	@Override
	public List<Stock> findStockByBoxCode(String boxCode) {
		List<Long> pickToLocList = locationBiz.getAllPickTo()
			.stream()
			.map(Location::getLocId)
			.collect(Collectors.toList());
		return stockDao.getStockByBoxCode(boxCode, pickToLocList);
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
		if (stockSkuCount == 0) {
			response.setLocOccupy((double) 0);
		} else {
			BigDecimal decimal = new BigDecimal((double) stockSkuCount / locCount * 100);
			response.setLocOccupy(decimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
		}
		return response;
	}


}
