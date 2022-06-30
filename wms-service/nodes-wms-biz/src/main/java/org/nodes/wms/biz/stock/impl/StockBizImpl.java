package org.nodes.wms.biz.stock.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.merge.StockMergeStrategy;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.SerialDao;
import org.nodes.wms.dao.stock.SerialLogDao;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.StockLogDao;
import org.nodes.wms.dao.stock.constant.SerialLogConstant;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogPageResponse;
import org.nodes.wms.dao.stock.entities.SerialLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.entities.StockLog;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ConvertUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
	private final StockMergeStrategy stockMergeStrategy;
	private final StockLogDao stockLogDao;
	private final SerialLogDao serialLogDao;
	private final SerialDao serialDao;

	@Override
	public void freezeByLoc(StockLogTypeEnum type, Long locId, String occupyFlag) {

	}

	@Override
	public void unfreezeByLoc(StockLogTypeEnum type, Long locId) {

	}

	// 库位是否被冻结，库位是否允许混放
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

	// 将sourceStock合并到targetStock
	private void mergeStock(Stock sourceStock, Stock targetStock,
							LocalDateTime lastIn, LocalDateTime lastOut) {
		targetStock.setStockQty(targetStock.getStockQty().add(sourceStock.getStockQty()));
		targetStock.setStayStockQty(targetStock.getStayStockQty().add(sourceStock.getStayStockQty()));
		targetStock.setPickQty(targetStock.getPickQty().add(sourceStock.getPickQty()));
		targetStock.setOccupyQty(targetStock.getOccupyQty().add(sourceStock.getOccupyQty()));
		if (!Func.isNull(lastIn)) {
			targetStock.setLastInTime(lastIn);
		}
		if (!Func.isNull(lastOut)) {
			targetStock.setLastOutTime(lastOut);
		}
	}

	@Override
	public Stock inStock(StockLogTypeEnum type, ReceiveLog receiveLog) {
		Location location = locationBiz.findByLocId(receiveLog.getLocId());
		canInStockByLocation(location, receiveLog.getSkuId(), receiveLog);
		// 形成库存，需要考虑库存合并
		Stock stock = createStock(receiveLog, location);
		Stock existStock = stockMergeStrategy.matchCanMergeStock(stock);
		// 本次入库保存的库存对象，如果需要合并则是数据库中保存的stock对象，否则为新的库存对象
		Stock finalStock = null;
		StockLog stockLog = null;
		if (Func.isNull(existStock)) {
			// 新建库存
			finalStock = stockDao.saveNewStock(stock);
			stockLog = createAndSaveStockLog(type, finalStock, receiveLog, "新建库存");
		} else {
			// 合并库存
			mergeStock(stock, existStock, LocalDateTime.now(), null);
			finalStock = stockDao.updateStock(existStock);
			stockLog = createAndSaveStockLog(type, finalStock, receiveLog, "合并库存");
		}

		// 形成序列号信息
		if (Func.isNotEmpty(receiveLog.getSnCode())){
			List<String> serialNoList = Arrays.asList(Func.split(receiveLog.getSnCode(), ","));
			createAndSaveSerial(serialNoList, finalStock, stockLog);
		}

		return finalStock;
	}

	@Override
	public Stock outStockByCancleReceive(StockLogTypeEnum type, ReceiveLog receiveLog) {
		return null;
	}

	private StockLog createAndSaveStockLog(StockLogTypeEnum type, Stock finalStock,
										   ReceiveLog receiveLog, String msg){
		StockLog stockLog = new StockLog();
		BeanUtil.copy(finalStock, stockLog);
		stockLog.setLogType(type.getDesc());
		stockLog.setSourceBillId(receiveLog.getReceiveId());
		stockLog.setSourceBillNo(receiveLog.getReceiveNo());
		stockLog.setLineNo(receiveLog.getLineNo());
		stockLog.setCurrentStayStockQty(BigDecimal.ZERO);
		stockLog.setCurrentStockQty(receiveLog.getQty());
		stockLog.setCurrentPickQty(BigDecimal.ZERO);
		stockLogDao.save(stockLog);
		return stockLog;
	}

	// 生成并保存序列号
	private List<Serial> createAndSaveSerial(List<String> serialNoList, Stock stock, StockLog stockLog){
		if (Func.isEmpty(serialNoList) || Func.isNull(stock)){
			throw new NullArgumentException("保存序列号时参数为空");
		}

		// 判断序列号是否重复
		List<Serial> existSerialList = findSerialBySerialNo(serialNoList);
		if (Func.isNotEmpty(existSerialList)){
			List<String> existSerialNo = existSerialList.stream()
				.map(Serial::getSerialNumber)
				.collect(Collectors.toList());
			throw new ServiceException(String.format("保存序列号失败,%s序列号已在库",
				StringUtil.join(existSerialNo, ",")));
		}

		List<Serial> resultStockSerial = new ArrayList<>();
		List<SerialLog> serialLogList = new ArrayList<>();
		// 找出已经存在但出库的序列号，该类序列号入库次数加1
		List<String> outBoundSerialNoList = null;
		List<Serial> outBoundSerialList = serialDao.getOutBoundSerialBySerialNo(serialNoList);
		if (Func.isNotEmpty(outBoundSerialList)){
			for (Serial serial : outBoundSerialList){
				updateSerial(serial, stock, serial.getInstockNumber() + 1);
				outBoundSerialNoList.add(serial.getSerialNumber());
				resultStockSerial.add(serial);
				serialLogList.add(createSerialLog(SerialLogConstant.SERIAL_LOG_UPDATE, serial, stock, stockLog));
			}
		}

		// 针对不存在的序列号则新增序列号
		List<String> newSerialNoList = serialNoList;
		if (Func.isNotEmpty(outBoundSerialNoList)) {
			newSerialNoList.removeAll(outBoundSerialNoList);
		}
		for (String serialNo : newSerialNoList){
			Serial stockSerial = new Serial();
			stockSerial.setSerialNumber(serialNo);
			updateSerial(stockSerial, stock, 1);
			resultStockSerial.add(stockSerial);
			serialLogList.add(createSerialLog(SerialLogConstant.SERIAL_LOG_NEW, stockSerial, stock, stockLog));
		}

		serialDao.saveOrUpdateBatch(resultStockSerial);
		serialLogDao.saveBatch(serialLogList);
		return resultStockSerial;
	}

	// 生成序列号日志
	private SerialLog createSerialLog(int proType, Serial serial, Stock stock, StockLog stockLog){
		SerialLog serialLog = new SerialLog();
		BeanUtil.copy(serial, serialLog);
		serialLog.setBillId(stockLog.getSourceBillId());
		serialLog.setBillNo(stockLog.getSourceBillNo());
		serialLog.setLineNo(stockLog.getLineNo());
		serialLog.setProType(proType);
		serialLog.setSystemProcId(stockLog.getWlslId());
		return serialLog;
	}

	private void updateSerial(Serial serial, Stock stock, int inStockNumber){
		serial.setStockId(stock.getStockId());
		serial.setWhId(stock.getWhId());
		serial.setSerialState(SerialStateEnum.IN_STOCK);
		serial.setInstockNumber(inStockNumber);
		serial.setSkuCode(stock.getSkuCode());
		serial.setSkuId(stock.getSkuId());
		serial.setSkuName(stock.getSkuName());
		serial.setStatus(1);
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
	public List<Serial> findSerialBySerialNo(List<String> serialNoList) {
		return serialDao.getSerialBySerialNo(serialNoList);
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
	public List<Stock> findStockOnStageByBoxCode(Long whId, String boxCode) {
		return null;
	}

	@Override
	public Stock findStockOnStage(ReceiveLog receiveLog) {
		return stockMergeStrategy.matchSameStock(receiveLog);
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
			response.setStageSkuQty(BigDecimal.ZERO);
			response.setStageSkuStoreDay(0);
		}

		if (Func.isNotEmpty(qcStock)) {
			response.setQcSkuQty(ConvertUtil.convert(qcStock.get("skuQty"), BigDecimal.class));
			response.setQcSkuStoreDay(ConvertUtil.convert(qcStock.get("skuStoreDay"), Integer.class));
		} else {
			response.setQcSkuQty(BigDecimal.ZERO);
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

	@Override
	public Page<StockLogPageResponse> pageStockLog(Query query,
												   StockLogPageQuery stockLogPageQuery) {
		return stockLogDao.page(Condition.getPage(query), stockLogPageQuery);
	}

	@Override
	public void export(StockLogPageQuery stockLogPageQuery,
					   HttpServletResponse response) {
		List<StockLogExcelResponse> stockLogList = stockLogDao.listByQuery(stockLogPageQuery);
		ExcelUtil.export(response, "", "", stockLogList, StockLogExcelResponse.class);
	}
}
