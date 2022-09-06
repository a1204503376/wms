package org.nodes.wms.biz.stock.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.instock.receiveLog.modular.ReceiveLogFactory;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stock.factory.StockFactory;
import org.nodes.wms.biz.stock.merge.StockMergeStrategy;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.instock.receiveLog.ReceiveLogDao;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.SerialDao;
import org.nodes.wms.dao.stock.SerialLogDao;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.StockLogDao;
import org.nodes.wms.dao.stock.dto.input.StockBySerialPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockImportRequest;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockBySerialPageResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.SerialLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.entities.StockLog;
import org.nodes.wms.dao.stock.enums.SerialLogTypeEnum;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class StockBizImpl implements StockBiz {

	private final StockDao stockDao;
	private final LocationBiz locationBiz;
	private final StockMergeStrategy stockMergeStrategy;
	private final StockLogDao stockLogDao;
	private final SerialLogDao serialLogDao;
	private final SerialDao serialDao;
	private final StockFactory stockFactory;
	private final ReceiveLogDao receiveLogDao;
	private final LogBiz logBiz;
	private final ReceiveLogFactory receiveLogFactory;
	private final StockQueryBiz stockQueryBiz;

	/**
	 * 库位是否被冻结，库位是否允许混放
	 */
	private <T> void canInStock(Location location, Long skuId, T skuLotObject) {
		if (Func.isEmpty(location)) {
			throw new ServiceException("新增库存失败,库位不存在");
		}
		// 库位是否冻结
		if (!location.enableStock()) {
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

	/**
	 * 将sourceStock合并到targetStock
	 */
	private void mergeStock(Stock sourceStock, Stock targetStock) {
		targetStock.setStockQty(targetStock.getStockQty().add(sourceStock.getStockQty()));
		targetStock.setStayStockQty(targetStock.getStayStockQty().add(sourceStock.getStayStockQty()));
		targetStock.setPickQty(targetStock.getPickQty().add(sourceStock.getPickQty()));
		targetStock.setOccupyQty(targetStock.getOccupyQty().add(sourceStock.getOccupyQty()));
		targetStock.setLastInTime(LocalDateTime.now());
	}

	private List<String> checkSerialOnInStock(ReceiveLog receiveLog) {
		if (Func.isNotEmpty(receiveLog.getSnCode())) {
			List<String> serialNoList = Arrays.asList(Func.split(receiveLog.getSnCode(), ","));
			if (serialNoList.size() != receiveLog.getQty().intValue()) {
				throw new ServiceException(
					String.format("入库失败,采集的序列号个数[%d]与收货数量[%s]不一致",
						serialNoList.size(), receiveLog.getQty().toString()));
			}

			return serialNoList;
		}

		return null;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock inStock(StockLogTypeEnum type, ReceiveLog receiveLog) {
		List<String> serialNoList = checkSerialOnInStock(receiveLog);
		Location location = locationBiz.findByLocId(receiveLog.getLocId());
		canInStock(location, receiveLog.getSkuId(), receiveLog);
		// 验证批属性
		SkuLotUtil.check(receiveLog, receiveLog.getWoId(), receiveLog.getWhId());
		// 形成库存，需要考虑库存合并
		Stock stock = stockFactory.create(receiveLog, location);
		Stock existStock = stockMergeStrategy.matchCanMergeStock(stock);
		// 本次入库保存的库存对象，如果需要合并则是数据库中保存的stock对象，否则为新的库存对象
		Stock finalStock;
		StockLog stockLog;
		if (Func.isNull(existStock)) {
			// 新建库存
			finalStock = stockDao.saveNewStock(stock);
			stockLog = createAndSaveStockLog(type, finalStock, receiveLog, "新建库存");
		} else {
			// 合并库存
			mergeStock(stock, existStock);
			finalStock = stockDao.updateStock(existStock);
			stockLog = createAndSaveStockLog(type, finalStock, receiveLog, "合并库存");
		}

		// 形成序列号信息
		if (Func.isNotEmpty(serialNoList)) {
			createAndSaveSerial(serialNoList, finalStock, stockLog);
		}

		return finalStock;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock outStockByCancelReceive(StockLogTypeEnum type, ReceiveLog cancelReceiveLog, Stock stock) {
		AssertUtil.notNull(cancelReceiveLog, "撤销收货失败，清点记录为空");
		AssertUtil.notNull(stock, "撤销收货失败，没有查询可撤销的库存");

		if (BigDecimalUtil.ge(cancelReceiveLog.getQty(), BigDecimal.ZERO)) {
			throw new ServiceException("撤销收货下架库存失败,清点记录的数量必须是负数");
		}
		// 下架库存
		BigDecimal cancelQty = cancelReceiveLog.getQty().abs();
		StockUtil.pickQty(stock, cancelQty, "撤销收货下架库存");
		stockDao.updateStock(stock.getStockId(), stock.getStockQty(),
			stock.getStayStockQty(), stock.getPickQty(), stock.getOccupyQty(), null, null);
		// 生成库存日志
		StockLog stockLog = createAndSaveStockLog(type, stock, cancelReceiveLog, "撤销收货");
		// 修改序列号状态和生成序列号日志
		if (Func.isNotEmpty(cancelReceiveLog.getSnCode())) {
			List<String> serialNoList = Arrays.asList(Func.split(cancelReceiveLog.getSnCode(), ","));
			updateSerialAndSaveLog(serialNoList, SerialStateEnum.OUT_STOCK, stock.getStockId(), stockLog);
		}

		return stock;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void outStockByCloseBill(List<LogSoPick> logSoPicks) {
		if (Func.isEmpty(logSoPicks)) {
			return;
		}

		for (LogSoPick pickLog : logSoPicks) {
			Stock stock = stockQueryBiz.findStockOnPickTo(pickLog);
			if (Func.isNull(stock)) {
				continue;
			}

			StockUtil.pickQty(stock, pickLog.getPickRealQty(), "单据关闭");
			stockDao.updateStock(stock.getStockId(), stock.getStockQty(), stock.getStayStockQty(),
				stock.getPickQty(), stock.getOccupyQty(), null, LocalDateTime.now());

			createAndSaveStockLog(false, stock, pickLog.getPickRealQty(), StockLogTypeEnum.OUTSTOCK_CLOSE_BILL,
				pickLog.getSoBillId(), pickLog.getSoBillNo(), pickLog.getSoLineNo(), "单据关闭");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void moveStockByCancelPick(StockLogTypeEnum type, LogSoPick pickLog) {
		AssertUtil.notNull(pickLog, "撤销拣货失败，原拣货记录为空");

		if (BigDecimalUtil.le(pickLog.getPickRealQty(), BigDecimal.ZERO)) {
			throw new ServiceException("撤销拣货失败,原拣货中的拣货数量小于等于0");
		}

		// 根据撤销记录查找库存，判断库存是否够
		Stock stock = stockQueryBiz.findStockOnPickTo(pickLog);
		if (Func.isNull(stock)) {
			throw ExceptionUtil.mpe(
				"撤销拣货失败，出库集货区无此库存[物品编码:{},生产批次:{}]",
				pickLog.getSkuCode(), pickLog.getSkuLot1());
		}
		// 将库存移动到原库位上
		List<String> serialNoList = null;
		if (Func.isNotEmpty(pickLog.getSnCode())) {
			serialNoList = Arrays.asList(Func.split(pickLog.getSnCode(), ","));
		}
		Location loc = locationBiz.findByLocId(pickLog.getLocId());
		moveStock(stock, serialNoList, pickLog.getPickRealQty(), loc, StockLogTypeEnum.INSTOCK_BY_CANCEL_PICK,
			pickLog.getSoBillId(), pickLog.getSoBillNo(), pickLog.getSoDetailId().toString());
	}

	private void updateSerialAndSaveLog(List<String> serialNoList, SerialStateEnum state, Long stockId,
										StockLog stockLog) {
		serialDao.updateSerialState(serialNoList, state, stockId);
		List<Serial> serialList = serialDao.getSerialBySerialNo(serialNoList);
		List<SerialLog> serialLogList = new ArrayList<>();
		for (Serial serial : serialList) {
			SerialLog serialLog = createSerialLog(SerialLogTypeEnum.UPDATE, serial, stockLog);
			serialLogList.add(serialLog);
		}
		serialLogDao.saveBatch(serialLogList);
	}

	/**
	 * 适用场景：收货时生成库存日志
	 *
	 * @param type       type
	 * @param finalStock 目标库存
	 * @param receiveLog 清点记录
	 * @param msg        msg
	 * @return StockLog
	 */
	private StockLog createAndSaveStockLog(StockLogTypeEnum type, Stock finalStock,
										   ReceiveLog receiveLog, String msg) {
		StockLog stockLog = new StockLog();
		BeanUtil.copy(finalStock, stockLog);
		stockLog.setLogType(type.getDesc());
		stockLog.setSourceBillId(receiveLog.getReceiveId());
		stockLog.setSourceBillNo(receiveLog.getReceiveNo());
		stockLog.setLineNo(receiveLog.getLineNo());
		stockLog.setCurrentStayStockQty(BigDecimal.ZERO);
		stockLog.setMsg(msg);
		if (BigDecimalUtil.gt(receiveLog.getQty(), BigDecimal.ZERO)) {
			// 收货日志
			stockLog.setCurrentStockQty(receiveLog.getQty());
			stockLog.setCurrentPickQty(BigDecimal.ZERO);
		} else {
			// 撤销收货日志
			stockLog.setCurrentStockQty(BigDecimal.ZERO);
			stockLog.setCurrentPickQty(receiveLog.getQty().abs());
		}
		stockLogDao.save(stockLog);
		return stockLog;
	}

	private List<Serial> createAndSaveSerial(List<String> serialNoList, Stock stock, StockLog stockLog) {
		if (Func.isEmpty(serialNoList) || Func.isNull(stock)) {
			throw new NullArgumentException("保存序列号时参数为空");
		}

		// 判断序列号是否重复
		List<Serial> existSerialList = serialDao.getSerialBySerialNo(serialNoList);
		if (Func.isNotEmpty(existSerialList)) {
			List<String> existSerialNo = existSerialList.stream()
				.map(Serial::getSerialNumber)
				.collect(Collectors.toList());
			throw new ServiceException(String.format("保存序列号失败,%s序列号已在库",
				StringUtil.join(existSerialNo, ",")));
		}

		List<Serial> resultStockSerial = new ArrayList<>();
		List<SerialLog> serialLogList = new ArrayList<>();
		// 找出已经存在但出库的序列号，该类序列号入库次数加1
		List<String> outBoundSerialNoList = new ArrayList<>();
		List<Serial> outBoundSerialList = serialDao.getOutBoundSerialBySerialNo(serialNoList);
		if (Func.isNotEmpty(outBoundSerialList)) {
			for (Serial serial : outBoundSerialList) {
				updateSerialAndSaveLog(serial, stock, serial.getInstockNumber() + 1);
				outBoundSerialNoList.add(serial.getSerialNumber());
				resultStockSerial.add(serial);
				serialLogList.add(createSerialLog(SerialLogTypeEnum.UPDATE, serial, stockLog));
			}
		}

		// 针对不存在的序列号则新增序列号
		List<String> newSerialNoList = serialNoList;
		if (Func.isNotEmpty(outBoundSerialNoList)) {
			newSerialNoList = serialNoList.stream()
				.filter(item -> !outBoundSerialNoList.contains(item))
				.collect(Collectors.toList());
		}
		for (String serialNo : newSerialNoList) {
			Serial stockSerial = new Serial();
			stockSerial.setSerialNumber(serialNo);
			updateSerialAndSaveLog(stockSerial, stock, 1);
			resultStockSerial.add(stockSerial);
			serialLogList.add(createSerialLog(SerialLogTypeEnum.NEW, stockSerial, stockLog));
		}

		serialDao.saveOrUpdateBatch(resultStockSerial);
		serialLogDao.saveBatch(serialLogList);
		return resultStockSerial;
	}

	private SerialLog createSerialLog(SerialLogTypeEnum type, Serial serial, StockLog stockLog) {
		SerialLog serialLog = new SerialLog();
		BeanUtil.copy(serial, serialLog);
		serialLog.setBillId(stockLog.getSourceBillId());
		serialLog.setBillNo(stockLog.getSourceBillNo());
		serialLog.setLineNo(stockLog.getLineNo());
		serialLog.setProType(type.getCode());
		serialLog.setSystemProcId(stockLog.getWlslId());
		return serialLog;
	}

	private void updateSerialAndSaveLog(Serial serial, Stock stock, int inStockNumber) {
		serial.setStockId(stock.getStockId());
		serial.setWhId(stock.getWhId());
		serial.setSerialState(SerialStateEnum.IN_STOCK);
		serial.setInstockNumber(inStockNumber);
		serial.setSkuCode(stock.getSkuCode());
		serial.setSkuId(stock.getSkuId());
		serial.setSkuName(stock.getSkuName());
		serial.setStatus(1);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock moveStock(Stock sourceStock, List<String> serialNoList,
						   BigDecimal qty, Location targetLocation, StockLogTypeEnum type,
						   Long billId, String billNo, String lineNo) {
		return moveStock(sourceStock, serialNoList, qty, sourceStock.getBoxCode(),
			sourceStock.getLpnCode(), targetLocation, type, billId, billNo, lineNo);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock moveAllStock(Stock sourceStock, String targetBoxCode, String targetLpnCode,
							  Location targetLocation, StockLogTypeEnum type, Long billId, String billNo, String lineNo) {
		List<String> serialNoList = serialDao.getSerialNoByStockId(sourceStock.getStockId());

		return moveStock(sourceStock, serialNoList, sourceStock.getStockEnable(),
			targetBoxCode, targetLpnCode, targetLocation, type, billId, billNo, lineNo);
	}

	@Override
	public Stock moveAllStockOfOccupy(Stock sourceStock, String targetBoxCode, String targetLpnCode,
									  Location targetLocation, StockLogTypeEnum type, Long billId,
									  String billNo, String lineNo) {
		return runMoveStockOfOccupy(sourceStock, targetLocation, targetBoxCode, targetLpnCode,
			type, false, null, billId, billNo, lineNo);
	}

	private void canMoveStock(Stock sourceStock, BigDecimal qty, Location targetLocation) {
		AssertUtil.notNull(targetLocation, "库存移动失败，目标库位为空");

		StockUtil.assertPick(sourceStock, qty, "库存移动失败");

		if (!targetLocation.enableStock()) {
			throw new ServiceException(
				String.format("库存移动失败，目标库位[%s]不能上架库存", targetLocation.getLocCode()));
		}
	}

	private void checkStockStatus(Stock sourceStock) {
		if (StockStatusEnum.SYSTEM_FREEZE.equals(sourceStock.getStockStatus())) {
			throw new ServiceException(
				String.format("库存移动失败,原库存[%d]被系统冻结,不能移动", sourceStock.getStockId()));
		}
	}

	private void checkQtyOfSerial(List<String> serialNoList, BigDecimal moveQty) {
		if (Func.isNotEmpty(serialNoList)) {
			List<String> serialNos = serialNoList.stream()
				.distinct()
				.collect(Collectors.toList());
			if (serialNoList.size() != serialNos.size()) {
				throw new ServiceException(String.format("库存移动失败,存在重复的的序列号,%s",
					String.join(",", serialNoList)));
			}

			if (serialNos.size() != moveQty.intValue()) {
				throw new ServiceException("库存移动失败,移动的个数与序列号个数不一致");
			}
		}
	}

	@Override
	public void checkSerialOnStock(Stock stock, List<String> serialNoList) {
		List<String> serialNosOfStock = serialDao.getSerialNoByStockId(stock.getStockId());
		if (Func.isNotEmpty(serialNosOfStock)) {
			if (Func.isEmpty(serialNoList)) {
				throw new ServiceException(
					String.format("序列号校验失败,库存[%d]有关联序列号,请选择序列号", stock.getStockId()));
			}

			for (String item : serialNoList) {
				if (!serialNosOfStock.contains(item)) {
					throw new ServiceException(
						String.format("序列号校验失败,序列号[%s]不在库存[%d]中", item, stock.getStockId()));
				}
			}
		} else {
			if (Func.isNotEmpty(serialNoList)) {
				throw new ServiceException(
					String.format("序列号校验失败,库存[%d]没有关联任何序列号", stock.getStockId()));
			}
		}
	}

	@Override
	public boolean equalStockStatus(List<Stock> stockList, StockStatusEnum status, boolean isThrow) {
		AssertUtil.notNull(status, "库存校验失败，库存状态不能为空");

		for (Stock stock : stockList) {
			if (!status.equals(stock.getStockStatus())) {
				if (isThrow) {
					throw new ServiceException(String.format("库存状态校验失败,库存[%d]现状态为[%s]不等于[%s]",
						stock.getStockId(), stock.getStockStatus().getDesc(), status.getDesc()));
				} else {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock moveStock(Stock sourceStock, List<String> serialNoList, BigDecimal qty,
						   String targetBoxCode, String targetLpnCode, Location targetLocation,
						   StockLogTypeEnum type, Long billId, String billNo, String lineNo) {
		return moveStock(sourceStock, serialNoList, qty, targetBoxCode,
			targetLpnCode, targetLocation, type, null, billId, billNo, lineNo);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock moveStock(Stock sourceStock, List<String> serialNoList, BigDecimal qty,
						   String targetBoxCode, String targetLpnCode,
						   Location targetLocation, StockLogTypeEnum type, String dropId,
						   Long billId, String billNo, String lineNo) {

		canMoveStock(sourceStock, qty, targetLocation);
		checkStockStatus(sourceStock);
		checkQtyOfSerial(serialNoList, qty);
		checkSerialOnStock(sourceStock, serialNoList);

		return runMoveStock(sourceStock, serialNoList, qty, targetBoxCode, targetLpnCode,
			targetLocation, type, dropId, billId, billNo, lineNo);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public List<Stock> moveStockByBoxCode(String boxCode, String targetBoxCode, String targetLpnCode,
										  Location targetLocation, StockLogTypeEnum type, Long billId,
										  String billNo, String lineNo) {
		AssertUtil.notEmpty(boxCode, "库存按BOX移动失败,原BOX不能为空");
		AssertUtil.notEmpty(targetBoxCode, "库存按BOX移动失败,目标BOX不能为空");
		AssertUtil.notNull(targetLocation, "库存按BOX移动失败,目标LOC不能为空");
		AssertUtil.notNull(type, "库存按BOX移动失败,库存移动类型不能为空");

		List<Stock> sourceStockList = stockDao.getStockByBoxCode(boxCode, null);
		List<Stock> targetStockList = new ArrayList<>();
		for (Stock sourceStock : sourceStockList) {
			List<String> serialNoList = serialDao.getSerialNoByStockId(sourceStock.getStockId());
			Stock targetStock = moveStock(sourceStock, serialNoList, StockUtil.getStockBalance(sourceStock),
				targetBoxCode, targetLpnCode, targetLocation, type, billId, billNo, lineNo);
			targetStockList.add(targetStock);
		}

		return targetStockList;
	}

	@Override
	public List<Stock> moveStockByBoxCodeOfOccupy(String boxCode, String targetBoxCode,
												  String targetLpnCode, Location targetLocation,
												  StockLogTypeEnum type, Long billId, String billNo, String lineNo) {
		AssertUtil.notEmpty(boxCode, "库存按BOX移动(带占用)失败,原BOX不能为空");
		AssertUtil.notEmpty(targetBoxCode, "库存按BOX移动(带占用)失败,目标BOX不能为空");
		AssertUtil.notNull(targetLocation, "库存按BOX移动(带占用)失败,目标LOC不能为空");
		AssertUtil.notNull(type, "库存按BOX移动(带占用)失败,库存移动类型不能为空");

		List<Stock> sourceStockList = stockDao.getStockByBoxCode(boxCode, null);
		List<Stock> targetStockList = new ArrayList<>();
		for (Stock sourceStock : sourceStockList) {
			Stock targetStock = runMoveStockOfOccupy(sourceStock, targetLocation, targetBoxCode, targetLpnCode,
				type, false, null, billId, billNo, lineNo);
			targetStockList.add(targetStock);
		}

		return targetStockList;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public List<Stock> moveStockByLpnCode(String lpnCode, String targetLpnCode, Location targetLocation,
										  StockLogTypeEnum type, Long billId, String billNo, String lineNo) {
		AssertUtil.notEmpty(lpnCode, "库存按LPN移动失败,原LPN不能为空");
		AssertUtil.notEmpty(targetLpnCode, "库存按LPN移动失败,目标LPN不能为空");
		AssertUtil.notNull(targetLocation, "库存按LPN移动失败,目标LOC不能为空");
		AssertUtil.notNull(type, "库存按LPN移动失败,库存移动类型不能为空");

		List<Stock> sourceStockList = stockDao.getStockByLpnCode(lpnCode, null);
		List<Stock> targetStockList = new ArrayList<>();
		for (Stock sourceStock : sourceStockList) {
			List<String> serialNoList = serialDao.getSerialNoByStockId(sourceStock.getStockId());
			Stock targetStock = moveStock(sourceStock, serialNoList, StockUtil.getStockBalance(sourceStock),
				sourceStock.getBoxCode(), targetLpnCode, targetLocation, type, billId, billNo, lineNo);
			targetStockList.add(targetStock);
		}

		return targetStockList;
	}

	@Override
	public List<Stock> moveStockByLpnCodeOfOccupy(String lpnCode, String targetLpnCode, Location targetLocation,
												  StockLogTypeEnum type, Long billId, String billNo, String lineNo) {
		AssertUtil.notEmpty(lpnCode, "库存按LPN移动(带占用)失败,原LPN不能为空");
		AssertUtil.notEmpty(targetLpnCode, "库存按LPN移动(带占用)失败,目标LPN不能为空");
		AssertUtil.notNull(targetLocation, "库存按LPN移动(带占用)失败,目标LOC不能为空");
		AssertUtil.notNull(type, "库存按LPN移动(带占用)失败,库存移动类型不能为空");

		List<Stock> sourceStockList = stockDao.getStockByLpnCode(lpnCode, null);
		List<Stock> targetStockList = new ArrayList<>();
		for (Stock sourceStock : sourceStockList) {
			Stock targetStock = runMoveStockOfOccupy(sourceStock, targetLocation, sourceStock.getBoxCode(),
				targetLpnCode, type, false, null, billId, billNo, lineNo);
			targetStockList.add(targetStock);
		}

		return targetStockList;
	}

	/**
	 * 适用场景：没有上架或下架库存时形成库存日志
	 *
	 * @param type  类型
	 * @param stock stock
	 * @param msg   msg
	 * @return StockLog
	 */
	private StockLog createAndSaveStockLog(StockLogTypeEnum type, Stock stock, String msg) {
		StockLog stockLog = new StockLog();
		Func.copy(stock, stockLog);
		stockLog.setLogType(type.getDesc());
		stockLog.setMsg(msg);
		stockLogDao.save(stockLog);
		return stockLog;
	}

	private Stock runMoveStock(Stock sourceStock, List<String> serialNoList, BigDecimal qty,
							   String targetBoxCode, String targetLpnCode, Location targetLocation,
							   StockLogTypeEnum type, String dropId, Long billId, String billNo, String lineNo) {
		if (Func.isNull(dropId)) {
			dropId = "";
		}

		Stock tempTargetStock = stockMergeStrategy.newExpectedStock(sourceStock, targetLocation,
			targetBoxCode, targetLpnCode, dropId);
		Stock targetStock = stockMergeStrategy.matchCanMergeStock(tempTargetStock);
		StockLog targetStockLog;
		if (Func.isNull(targetStock)) {
			targetStock = stockFactory.create(sourceStock, targetLocation, targetLpnCode, targetBoxCode, qty,
				serialNoList);
			stockDao.saveNewStock(targetStock);
			targetStockLog = createAndSaveStockLog(true, targetStock, qty,
				type, billId, billNo, lineNo, "库存移动-新库存");
		} else {
			StockUtil.addQty(targetStock, qty);
			stockDao.updateStock(targetStock);
			targetStockLog = createAndSaveStockLog(true, targetStock, qty,
				type, billId, billNo, lineNo, "库存移动-合并");
		}

		// 下架原库存
		StockUtil.pickQty(sourceStock, qty, "库存移动");
		stockDao.updateStock(sourceStock.getStockId(), sourceStock.getStockQty(), sourceStock.getStayStockQty(),
			sourceStock.getPickQty(), sourceStock.getOccupyQty(), null, LocalDateTime.now());
		// 生成库存日志
		createAndSaveStockLog(false, sourceStock, qty, type, billId, billNo,
			lineNo, "库存移动下架");
		// 更新序列号和库存的关联关系
		if (Func.isNotEmpty(serialNoList)) {
			SerialStateEnum serialStateEnum = locationBiz.isPickToLocation(targetLocation) ? SerialStateEnum.OUT_STOCK
				: SerialStateEnum.IN_STOCK;
			updateSerialAndSaveLog(serialNoList, serialStateEnum, targetStock.getStockId(), targetStockLog);
		}

		return targetStock;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock moveToInTransitByDropId(Stock sourceStock, String dropId, StockLogTypeEnum type) {
		// 将库存移动到中间库存，原库存的占用需要释放并下架，生成的中间库存需要重新占用
		if (BigDecimalUtil.le(sourceStock.getStockBalance(), BigDecimal.ZERO)) {
			throw ExceptionUtil.mpe("按DropId[%s]移动库存,原库存[%d]已全部下架", dropId, sourceStock.getStockId());
		}

		Location inTransitLocation = locationBiz.getInTransitLocation(sourceStock.getWhId());
		return runMoveStockOfOccupy(sourceStock, inTransitLocation, sourceStock.getBoxCode(), sourceStock.getLpnCode(),
			type, false, dropId, null, null, null);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock moveAllStockFromDropId(Stock sourceStock, Location targetLocation, String dropId,
										StockLogTypeEnum type) {
		// 中间库存的占用需要释放并下架，生成的目标库存需要重新占用
		if (BigDecimalUtil.le(sourceStock.getStockBalance(), BigDecimal.ZERO)) {
			throw ExceptionUtil.mpe("按DropId[%s]移动库存,原库存[%d]已全部下架", dropId, sourceStock.getStockId());
		}

		return runMoveStockOfOccupy(sourceStock, targetLocation, sourceStock.getBoxCode(), sourceStock.getLpnCode(),
			type, true, dropId, null, null, null);
	}

	private Stock runMoveStockOfOccupy(Stock sourceStock, Location targetLoc, String targetBoxCode,
									   String targetLpnCode, StockLogTypeEnum type, boolean cleanDropId,
									   String dropId, Long billId, String billNo, String lineNo) {
		List<String> serialNoList = serialDao.getSerialNoByStockId(sourceStock.getStockId());
		if (sourceStock.isEmptyStock()) {
			throw ExceptionUtil.mpe("库存移动(带占用),库存[{}]已经全部下架", sourceStock.getStockId());
		}

		checkQtyOfSerial(serialNoList, sourceStock.getStockBalance());

		if (Func.isEmpty(dropId)) {
			dropId = "";
		}
		Stock tempTargetStock = stockMergeStrategy.newExpectedStock(sourceStock, targetLoc,
			targetBoxCode, targetLpnCode, dropId);
		Stock targetStock = stockMergeStrategy.matchCanMergeStock(tempTargetStock);
		StockLog targetStockLog;
		if (Func.isNull(targetStock)) {
			targetStock = stockFactory.create(sourceStock, targetLoc, targetLpnCode,
				targetBoxCode, sourceStock.getStockBalance(), serialNoList);
			targetStock.setOccupyQty(sourceStock.getOccupyQty());
			if (cleanDropId) {
				targetStock.setDropId("");
			}
			stockDao.saveNewStock(targetStock);
			targetStockLog = createAndSaveStockLog(true, targetStock, sourceStock.getStockBalance(),
				type, billId, billNo, lineNo, "库存移动(带占用)-新库存");
		} else {
			StockUtil.addQty(targetStock, sourceStock.getStockBalance());
			targetStock.setOccupyQty(targetStock.getOccupyQty().add(sourceStock.getOccupyQty()));
			if (cleanDropId) {
				targetStock.setDropId("");
			}
			stockDao.updateStock(targetStock);
			targetStockLog = createAndSaveStockLog(true, targetStock, sourceStock.getStockBalance(),
				type, billId, billNo, lineNo, "库存移动(带占用)-合并");
		}

		// 下架原库存并释放占用
		sourceStock.setPickQty(sourceStock.getStockQty());
		sourceStock.setOccupyQty(BigDecimal.ZERO);
		stockDao.updateStock(sourceStock.getStockId(), sourceStock.getStockQty(), sourceStock.getStayStockQty(),
			sourceStock.getPickQty(), sourceStock.getOccupyQty(), null, LocalDateTime.now());
		// 生成库存日志
		createAndSaveStockLog(false, sourceStock, sourceStock.getStockBalance(), type,
			billId, billNo, lineNo, "库存移动(带占用)下架");
		// 更新序列号和库存的关联关系
		if (Func.isNotEmpty(serialNoList)) {
			SerialStateEnum serialStateEnum = SerialStateEnum.IN_STOCK;
			updateSerialAndSaveLog(serialNoList, serialStateEnum, targetStock.getStockId(), targetStockLog);
		}

		return targetStock;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeStock(List<Long> stockIds) {
		AssertUtil.notEmpty(stockIds, "冻结库存失败，参数为空");

		List<Stock> stocks = stockDao.getStockById(stockIds);
		AssertUtil.notEmpty(stocks, "冻结库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.NORMAL, true);

		stockDao.updateStock(stockIds, StockStatusEnum.FREEZE);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_FREEZE, stock, "按stock id冻结");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unfreezeStock(List<Long> stockIds) {
		AssertUtil.notEmpty(stockIds, "解冻库存失败，参数为空");

		List<Stock> stocks = stockDao.getStockById(stockIds);
		AssertUtil.notEmpty(stocks, "解冻库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.FREEZE, true);

		stockDao.updateStock(stockIds, StockStatusEnum.NORMAL);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_UNFREEZE, stock, "按stock id解冻");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeStockByLoc(List<Long> locIds) {
		AssertUtil.notEmpty(locIds, "冻结库存失败，参数为空");
		List<Stock> stocks = stockDao.getStockByLocIdList(locIds);
		AssertUtil.notEmpty(stocks, "冻结库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.NORMAL, true);

		List<Long> stockIds = stocks.stream().map(Stock::getStockId).collect(Collectors.toList());
		stockDao.updateStock(stockIds, StockStatusEnum.FREEZE);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_FREEZE, stock, "按库位冻结");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unfreezeStockByLoc(List<Long> locIds) {
		AssertUtil.notEmpty(locIds, "解冻库存失败，参数为空");
		List<Stock> stocks = stockDao.getStockByLocIdList(locIds);
		AssertUtil.notEmpty(stocks, "解冻库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.FREEZE, true);

		List<Long> stockIds = stocks.stream().map(Stock::getStockId).collect(Collectors.toList());
		stockDao.updateStock(stockIds, StockStatusEnum.NORMAL);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_UNFREEZE, stock, "按库位解冻");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeStockByBoxCode(List<String> boxCodes) {
		AssertUtil.notEmpty(boxCodes, "冻结库存失败，参数为空");
		List<Stock> stocks = stockDao.getStockByBoxCode(boxCodes, null);
		AssertUtil.notEmpty(stocks, "冻结库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.NORMAL, true);

		List<Long> stockIds = stocks.stream().map(Stock::getStockId).collect(Collectors.toList());
		stockDao.updateStock(stockIds, StockStatusEnum.FREEZE);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_FREEZE, stock, "按boxCode冻结");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unFreezeStockByBoxCode(List<String> boxCodes) {
		AssertUtil.notEmpty(boxCodes, "解冻库存失败，参数为空");
		List<Stock> stocks = stockDao.getStockByBoxCode(boxCodes, null);
		AssertUtil.notEmpty(stocks, "解冻库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.FREEZE, true);

		List<Long> stockIds = stocks.stream().map(Stock::getStockId).collect(Collectors.toList());
		stockDao.updateStock(stockIds, StockStatusEnum.NORMAL);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_UNFREEZE, stock, "按boxCode解冻");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeStockByLpnCode(List<String> lpnCodes) {
		AssertUtil.notEmpty(lpnCodes, "冻结库存失败，参数为空");
		List<Stock> stocks = stockDao.getStockByLpnCode(lpnCodes, null);
		AssertUtil.notEmpty(stocks, "冻结库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.NORMAL, true);

		List<Long> stockIds = stocks.stream().map(Stock::getStockId).collect(Collectors.toList());
		stockDao.updateStock(stockIds, StockStatusEnum.FREEZE);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_FREEZE, stock, "按lpnCode冻结");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void unfreezeStockByLpnCode(List<String> lpnCodes) {
		AssertUtil.notEmpty(lpnCodes, "解冻库存失败，参数为空");
		List<Stock> stocks = stockDao.getStockByLpnCode(lpnCodes, null);
		AssertUtil.notEmpty(stocks, "解冻库存失败,没有查询到可用库存");
		equalStockStatus(stocks, StockStatusEnum.FREEZE, true);

		List<Long> stockIds = stocks.stream().map(Stock::getStockId).collect(Collectors.toList());
		stockDao.updateStock(stockIds, StockStatusEnum.NORMAL);
		for (Stock stock : stocks) {
			createAndSaveStockLog(StockLogTypeEnum.STOCK_UNFREEZE, stock, "按lpnCode解冻");
		}
	}

	/**
	 * 适用场景：有执行上架或下架时生成库存日志
	 *
	 * @param isInStock true：入库
	 * @param stock     库存
	 * @param qty       qty
	 * @param type      type
	 * @param billId    billId
	 * @param billNo    billNo
	 * @param lineNo    lineNo
	 * @param msg       msg
	 * @return StockLog
	 */
	private StockLog createAndSaveStockLog(boolean isInStock, Stock stock, BigDecimal qty,
										   StockLogTypeEnum type, Long billId, String billNo,
										   String lineNo, String msg) {
		StockLog stockLog = new StockLog();
		BeanUtil.copy(stock, stockLog);
		stockLog.setSourceBillId(billId);
		stockLog.setSourceBillNo(billNo);
		stockLog.setLogType(type.getDesc());
		stockLog.setLineNo(lineNo);
		stockLog.setMsg(msg);
		if (isInStock) {
			stockLog.setCurrentStockQty(qty);
		} else {
			stockLog.setCurrentPickQty(qty);
		}

		stockLogDao.save(stockLog);
		return stockLog;
	}

	@Override
	public void exportStockLogToExcel(StockLogPageQuery stockLogPageQuery,
									  HttpServletResponse response) {
		List<StockLogExcelResponse> stockLogList = stockLogDao.listByQuery(stockLogPageQuery);
		ExcelUtil.export(response, "", "", stockLogList, StockLogExcelResponse.class);
	}

	@Override
	public void exportBySerial(StockBySerialPageQuery stockBySerialPageQuery, HttpServletResponse response) {
		IPage<?> page = new Page<>();
		page.setCurrent(1);
		page.setSize(100000);
		List<StockBySerialPageResponse> stockBySerialPageResponseList = stockDao.page(page, stockBySerialPageQuery)
			.getRecords();
		ExcelUtil.export(response, "库存余额", "库存余额数据表",
			stockBySerialPageResponseList, StockBySerialPageResponse.class);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void occupyStock(List<SoPickPlan> newPickPlan) {
		AssertUtil.notEmpty(newPickPlan, "更新库存占用失败, 拣货计划为空无法更新");

		Map<Long, List<SoPickPlan>> stockId2Qty = newPickPlan.stream()
			.collect(Collectors.groupingBy(SoPickPlan::getStockId, Collectors.toList()));
		stockId2Qty.forEach((stockId, soPickPlanList) -> {
			BigDecimal currentOccupy = soPickPlanList.stream()
				.map(SoPickPlan::getSurplusQty)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			SoPickPlan soPickPlan = soPickPlanList.get(0);
			increaseOccupy(soPickPlan.getSoBillId(), soPickPlan.getSoBillNo(), soPickPlan.getSoDetailId(),
				stockId, currentOccupy);
		});
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock increaseOccupy(Long soBillId, String soBillNo, Long soDetailId,
								Long stockId, BigDecimal currentOccupy) {
		Stock stock = stockDao.getStockById(stockId);
		if (BigDecimalUtil.gt(currentOccupy, stock.getStockEnable())) {
			throw new ServiceException(String.format("占用库存失败,[%d][%s]当前余额[%s]可用[%s],当前占用量[%s]超过可用量",
				stockId, stock.getSkuCode(), stock.getStockBalance().toString(),
				stock.getStockEnable().toString(), currentOccupy.toString()));
		}

		stock.setOccupyQty(stock.getOccupyQty().add(currentOccupy));
		stockDao.upateOccupyQty(stock);
		createAndSaveOccupyStockLog(stock, soBillId, soBillNo, soDetailId, currentOccupy,
			StockLogTypeEnum.STOCK_OCCUPY_BY_STRATEGY, "分配占用库存");

		return stock;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Stock reduceOccupy(Long soBillId, String soBillNo, Long soDetailId,
							  Long stockId, BigDecimal currentUnOccupy) {
		Stock stock = stockDao.getStockById(stockId);
		AssertUtil.notNull(stock, "释放库存占用失败,库存[{}]不存在", stockId);
		if (BigDecimalUtil.gt(currentUnOccupy, stock.getOccupyQty())) {
			throw new ServiceException(String.format("释放占用库存失败,[%d][%s]当前余额[%s]占用[%s],当前释放量[%s]超过占用量",
				stockId, stock.getSkuCode(), stock.getStockBalance().toString(),
				stock.getOccupyQty().toString(), currentUnOccupy.toString()));
		}

		stock.setOccupyQty(stock.getOccupyQty().subtract(currentUnOccupy));
		stockDao.upateOccupyQty(stock);
		createAndSaveOccupyStockLog(stock, soBillId, soBillNo, soDetailId, currentUnOccupy,
			StockLogTypeEnum.STOCK_CANCEL_OCCUPY, "释放分配占用库存");

		return stock;
	}

	/**
	 * 适用场景：占用时生成库存日志
	 *
	 * @param stock         stock
	 * @param soBillId      soBillId
	 * @param soBillNo      soBillNo
	 * @param soDetailId    soDetailId
	 * @param currentOccupy currentOccupy
	 * @param type          type
	 * @param msg           msg
	 */
	private void createAndSaveOccupyStockLog(Stock stock, Long soBillId, String soBillNo, Long soDetailId,
											 BigDecimal currentOccupy, StockLogTypeEnum type, String msg) {
		StockLog stockLog = new StockLog();
		Func.copy(stock, stockLog);
		if (Func.notNull(soBillId)) {
			stockLog.setSourceBillId(soBillId);
		}
		if (Func.notNull(soBillNo)) {
			stockLog.setSourceBillNo(soBillNo);
		}
		if (Func.notNull(soDetailId)) {
			stockLog.setLineNo(soDetailId.toString());
		}
		stockLog.setCurrentOccupyQty(currentOccupy);
		stockLog.setLogType(type.getDesc());
		stockLog.setMsg(msg);
		stockLogDao.save(stockLog);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeStockByDropId(List<Stock> stocks, Long dropId) {
		AssertUtil.notEmpty(stocks, "库存系统冻结失败,库存不能为空");
		AssertUtil.notNull(dropId, "库存系统冻结失败,dropId is not null");

		stockDao.updateStockByDropId(stocks, StockStatusEnum.SYSTEM_FREEZE, dropId.toString());

		for (Stock stock : stocks) {
			stock.setStockStatus(StockStatusEnum.SYSTEM_FREEZE);
			stock.setDropId(dropId.toString());

			createAndSaveStockLog(StockLogTypeEnum.STOCK_FREEZE, stock,
				String.format("系统冻结 by %d", dropId));
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public List<Stock> unfreezeStockByDropId(List<Stock> stocks, Long dropId) {
		AssertUtil.notEmpty(stocks, "根据任务解冻库存失败,没有任务[{}]关联的库存", dropId);

		stockDao.updateStockByDropId(stocks, StockStatusEnum.NORMAL, "");

		for (Stock item : stocks) {
			item.setStockStatus(StockStatusEnum.NORMAL);
			item.setDropId("");

			createAndSaveStockLog(StockLogTypeEnum.STOCK_UNFREEZE, item, String.format("系统解结 by %d", dropId));
		}

		return stocks;
	}

	@Override
	public boolean judgeEnableOnLocation(Location location) {
		List<Stock> stock = stockDao.getStockByLocId(location.getLocId());
		if (Func.isNotEmpty(stock)
			&& BigDecimalUtil.gt(StockUtil.getStockBalance(stock), BigDecimal.ZERO)) {
			return false;
		}
		return location.enableStock();
	}

	@Override
	public void exportStockToExcel(StockPageQuery stockPageQuery, HttpServletResponse response) {
		List<StockPageResponse> stockPageResponseList = stockDao.getStockResponseByQuery(stockPageQuery);
		for (StockPageResponse stockPageResponse : stockPageResponseList) {
			String stockStatus = stockPageResponse.getStockStatus().getDesc();
			stockPageResponse.setStockStatusDesc(stockStatus);
			// 设置库存可用量
			stockPageResponse.setStockEnable(stockPageResponse.getStockQty()
				.add(stockPageResponse.getPickQty().subtract(stockPageResponse.getOccupyQty())));
			// 设置库存余额
			stockPageResponse.setStockBalance(stockPageResponse.getStockQty().subtract(stockPageResponse.getPickQty()));
		}
		ExcelUtil.export(response, "库存余额", "库存余额数据表", stockPageResponseList, StockPageResponse.class);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public boolean importStockByExcel(List<StockImportRequest> importDataList) {

		if (Func.isEmpty(importDataList)) {
			throw new ServiceException("导入失败，没有可导入的数据");
		}
		// 校验参数并生成清点记录
		List<ReceiveLog> receiveLogList = receiveLogFactory.createReceiveLogListForImport(importDataList);
		for (ReceiveLog receiveLog : receiveLogList) {
			// 调用入库方法
			inStock(StockLogTypeEnum.INSTOCK_BY_Import, receiveLog);
		}
		// 保存清点记录
		receiveLogDao.saveBatch(receiveLogList);
		// 记录业务日志
		logBiz.auditLog(AuditLogType.INSTOCK, "导入库存" + receiveLogList.size() + "条");
		return true;
	}

}
