package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.DevanningBiz;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.dto.input.DevanningSubmitRequest;
import org.nodes.wms.dao.stock.dto.input.FindAllSerialNumberManageRequest;
import org.nodes.wms.dao.stock.dto.output.DevanningStockResponse;
import org.nodes.wms.dao.stock.dto.output.FindAllSerialNumberManageResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 拆箱相关业务
 *
 * @author admin
 */
@Service
@RequiredArgsConstructor
public class DevanningBizImpl implements DevanningBiz {
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final SerialBiz serialBiz;
	private final LpnTypeBiz lpnTypeBiz;
	private final LocationBiz locationBiz;
	private final WmsTaskBiz wmsTaskBiz;
	private final SoPickPlanBiz soPickPlanBiz;
	private final SoBillBiz soBillBiz;
	private final ZoneBiz zoneBiz;
	private final StockDao stockDao;

	@Override
	public FindAllSerialNumberManageResponse getAllSerialNumberManage(FindAllSerialNumberManageRequest request) {
		FindAllSerialNumberManageResponse response = new FindAllSerialNumberManageResponse();
		// 根据箱码查询库存
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		if (Func.isNull(stockList) || stockList.size() == 0) {
			throw new ServiceException("拆箱失败，拆箱根据箱码查询库存失败");
		}
		// 返回是否是序列号管理----根据序列号列表是否为空进行判断，如果不是则需要查询出所有跟当前托盘有关的库存进行返回
		List<String> serialNumberList = new ArrayList<>();
		for (Stock stock : stockList) {
			List<String> serialBizSerialNoByStockId = serialBiz.findSerialNoByStockId(stock.getStockId());
			if (Func.isNotEmpty(serialBizSerialNoByStockId)) {
				serialNumberList.addAll(serialBizSerialNoByStockId);
			}
		}

		if (Func.isNotEmpty(serialNumberList)) {
			// 序列号不为空赋值响应对象-序列号集合赋值、并且将序列号集合判断变为true、将库存响应对象置空
			response.setIsSn(true);
			response.setSerialNumberList(serialNumberList);
			response.setStockList(null);
		} else {
			// 序列号为空赋值响应对象-序列号集合置空、并且将序列号集合判断变为false、库存响应对象集合
			response.setIsSn(false);
			response.setSerialNumberList(null);
			List<DevanningStockResponse> devanningStockList = BeanUtil.copy(stockList, DevanningStockResponse.class);
			response.setStockList(devanningStockList);
		}
		return response;
	}

	private String generateNewBoxCode(DevanningSubmitRequest request) {
		LpnTypeCodeEnum lpnTypeCodeEnum = lpnTypeBiz.tryParseBoxCode(request.getBoxCode());
		String skuName = null;
		String spec = null;
		if (request.getIsSn()) {
			Serial serial = serialBiz.findSerialSerialNo(request.getSerialNumberList().get(0));
			Stock stock = stockQueryBiz.findStockById(serial.getStockId());
			skuName = stock.getSkuName();
			spec = stock.getSkuLot2();
		}

		return lpnTypeBiz.generateLpnCode(lpnTypeCodeEnum.getCode(), skuName, spec);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void devanning(DevanningSubmitRequest request) {
		// 根据前端输入的locCod查询出对应的目标库位
		Location location = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		String oldBoxCode = request.getBoxCode();
		// 是否生成新箱码 是true进入下面方法生成新箱码
		if (request.getNewBoxCode()) {
			generateNewBoxCode(request);
			request.setBoxCode(generateNewBoxCode(request));
		}

		// 首先判断是否序列号管理执行序列号拆箱
		if (request.getIsSn()) {
			List<String> serialNumberList = request.getSerialNumberList();
			AssertUtil.notNull(serialNumberList, "根据序列号拆箱时,未采集序列号");
			List<Serial> serialList = stockQueryBiz.findSerialBySerialNo(serialNumberList);
			AssertUtil.notNull(serialList, "根据序列号拆箱时,查找不到对应在库的序列号");
			// 根据stockId对序列号进行分组
			List<Long> stockIds = serialList.stream()
				.map(Serial::getStockId)
				.distinct()
				.collect(Collectors.toList());
			List<Stock> stockList = stockQueryBiz.findStockById(stockIds);
			AssertUtil.notNull(stockList, "采集的序列号找不到对应库存");
			AtomicInteger serialNumberListSize = new AtomicInteger(serialNumberList.size());
			BigDecimal sumSplitQty = stockList.stream()
				.map(Stock::getStockBalance)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			List<Stock> oldStockList = stockQueryBiz.findEnableStockByBoxCode(oldBoxCode);
			BigDecimal maxSumSplitQty = oldStockList.stream()
				.map(Stock::getStockBalance)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			stockList.forEach(stock -> {
				serialList.forEach(serial -> {
					if (Func.equals(stock.getStockId(), serial.getStockId())) {
						AssertUtil.notNull(stock, "根据序列号拆箱时,根据序列号集合找不到对应库存");
						serialNumberListSize.set(serialNumberListSize.get() - stock.getStockBalance().intValue());
						if (serialNumberListSize.get() >= 0) {
							if (BigDecimalUtil.gt(stock.getOccupyQty(), BigDecimal.ZERO)) {
								//有任务的拆箱
								taskDevanning(oldBoxCode, stock, location, sumSplitQty, maxSumSplitQty, request, stock.getStockBalance(), request.getSerialNumberList());
							} else {
								// 库存移动
								Stock targetStock = stockBiz.moveStock(stock, serialNumberList, stock.getStockBalance(), request.getBoxCode(), location.getLocCode(),
									location, StockLogTypeEnum.STOCK_DEVANNING_BY_PDA, null, null, null);
								serialBiz.updateSerialStockIdBySerialNo(stock.getStockId(), targetStock.getStockId(), serial.getSerialNumber());

							}
						} else {
							if (BigDecimalUtil.gt(stock.getOccupyQty(), BigDecimal.ZERO)) {
								//有任务的拆箱
								taskDevanning(oldBoxCode, stock, location, sumSplitQty, maxSumSplitQty, request, stock.getStockBalance(), request.getSerialNumberList());
							} else {
								// 库存移动
								Stock targetStock = stockBiz.moveStock(stock, serialNumberList, new BigDecimal(serialNumberList.size()), request.getBoxCode(), location.getLocCode(),
									location, StockLogTypeEnum.STOCK_DEVANNING_BY_PDA, null, null, null);
								serialBiz.updateSerialStockIdBySerialNo(stock.getStockId(), targetStock.getStockId(), serial.getSerialNumber());

							}
						}
					}
				});

			});

		} else {
			BigDecimal sumSplitQty = request.getStockList().stream()
				.map(DevanningStockResponse::getSplitQty)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal maxSumSplitQty = BigDecimal.ZERO;
			for (DevanningStockResponse devanningStock : request.getStockList()) {
				Stock stock = stockQueryBiz.findStockById(devanningStock.getStockId());
				maxSumSplitQty.add(stock.getStockBalance());
			}
			AssertUtil.notNull(request.getStockList(), "根据物品拆箱时,未选择物品");
			// 如果序列号不为空执行 按库存拆箱
			request.getStockList().forEach(stockDeva -> {
				// 根据每一个库位id获取库位
				Stock stock = stockQueryBiz.findStockById(stockDeva.getStockId());
				AssertUtil.notNull(stock, "根据物品拆箱时,找不到对应库存");
				// 如果用户输入的数量大于0则进行拆箱
				if (BigDecimalUtil.gt(stockDeva.getSplitQty(), BigDecimal.ZERO)) {
					if (BigDecimalUtil.gt(stock.getOccupyQty(), BigDecimal.ZERO)) {
						//有任务的拆箱
						taskDevanning(oldBoxCode, stock, location, sumSplitQty, maxSumSplitQty, request, stockDeva.getSplitQty(), null);
					} else {
						//不是库存占用直接拆箱
						stockBiz.moveStock(stock, null, stockDeva.getSplitQty(), request.getBoxCode(), location.getLocCode(),
							location, StockLogTypeEnum.STOCK_DEVANNING_BY_PDA, null, null, null);
					}

				}
			});
		}

	}


	private void taskDevanning(String oldBoxCode, Stock stock, Location location, BigDecimal sumSplitQty,
							   BigDecimal maxSumSplitQty, DevanningSubmitRequest request,
							   BigDecimal splitQty, List<String> serialNoList) {
		Zone zone = zoneBiz.findById(location.getZoneId());
		//是库存占用
		WmsTask task = wmsTaskBiz.findPickTaskByBoxCode(oldBoxCode, null);
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(task.getTaskId());
		AssertUtil.notNull(soPickPlanList, "拆箱失败,根据任务查询拣货计划失败");
		if (soPickPlanList.size() < 1) {
			throw new ServiceException("拆箱失败,根据任务查询拣货计划失败");
		}
		SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlanList.get(0).getSoDetailId());
		if (BigDecimalUtil.ne(soDetail.getSurplusQty(), sumSplitQty) ||
			BigDecimalUtil.ne(soDetail.getSurplusQty(), maxSumSplitQty.subtract(sumSplitQty))) {
			throw new ServiceException("拆箱失败,拣货数量和发货单不匹配");
		}
		//发货单详情等于页面要拣货数量
		if (BigDecimalUtil.eq(soDetail.getSurplusQty(), sumSplitQty)) {
			oldBoxCode = request.getBoxCode();
		}
		//清除库存占用
		stock.setOccupyQty(BigDecimal.ZERO);
		Stock moveStock = stockBiz.moveStock(stock, serialNoList, splitQty, request.getBoxCode(), location.getLocCode(),
			location, StockLogTypeEnum.STOCK_DEVANNING_BY_PDA, null, null, null);
		moveStock.setOccupyQty(moveStock.getStockBalance());
		stockDao.upateOccupyQty(moveStock);
		wmsTaskBiz.updateWmsTaskByPartParam(task.getTaskId(), WmsTaskProcTypeEnum.BY_BOX, location, oldBoxCode);
		for (SoPickPlan soPickPlan : soPickPlanList) {
			soPickPlanBiz.updatePickByPartParam(soPickPlan.getPickPlanId(), moveStock.getStockId(), location, zone, oldBoxCode);
		}
	}

}
