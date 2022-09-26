package org.nodes.wms.biz.putaway.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putaway.PutawayBiz;
import org.nodes.wms.biz.putaway.modular.PutawayFactory;
import org.nodes.wms.biz.putaway.strategy.TianYiPutawayStrategy;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putaway.PutawayLogDao;
import org.nodes.wms.dao.putaway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putaway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putaway.dto.input.PutwayByBoxRequest;
import org.nodes.wms.dao.putaway.dto.output.BoxDto;
import org.nodes.wms.dao.putaway.dto.output.LocResponse;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PutawayBizImpl implements PutawayBiz {
	private final LocationBiz locationBiz;
	private final StockBiz stockBiz;
	private final PutawayLogDao putawayLogDao;
	private final StockQueryBiz stockQueryBiz;
	private final PutawayFactory putawayFactory;
	private final AgvTask agvTask;
	private final TianYiPutawayStrategy tianYiPutawayStrategy;
	private final StockManageBiz stockManageBiz;

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void putawayByBox(PutwayByBoxRequest request) {
		// 判断库存是否在入库暂存区，如果不是入库暂存区应报异常
		AssertUtil.notNull(request.getStockId(), "按箱上架失败,原库存信息查询错误，请稍后重试");
		Stock sourceStock = stockQueryBiz.findStockById(request.getStockId());
		AssertUtil.notNull(sourceStock, "按箱上架失败,没有原库存信息");
		Location location = locationBiz.findLocationByLocCode(request.getWhId(), sourceStock.getLocCode());
		boolean stageLocation = locationBiz.isStageLocation(location);
		if (!stageLocation) {
			throw new ServiceException("按箱上架失败,原库存不是入库暂存区的库存");
		}

		// 如果是整托：按托移动
		if (request.getIsAllLpnPutaway()) {
			// 如果是整托：原库中的lpn为空白字符，需要抛异常
			AssertUtil.notEmpty(sourceStock.getLpnCode(), "按箱上架失败,原库中的lpn为空白字符");
			List<Stock> stockList = stockQueryBiz.findStockByLpnCode(sourceStock.getLpnCode());
			AssertUtil.notNull(stockList, "按箱上架失败,暂无与此托盘号相关库存的信息");
			List<Stock> sourceStockList = stockList.stream()
				.filter(stockPrams -> Func.equals(stockPrams.getBoxCode(), request.getBoxCode()))
				.collect(Collectors.toList());
			AssertUtil.notNull(sourceStockList, "按箱上架失败,暂无与此托盘号上的箱码的相关库存的信息");
			Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
			boolean pickLocation = locationBiz.isPickLocation(targetLocation);
			if (!pickLocation) {
				throw new ServiceException("按箱上架失败，目标库位不是拣货区/人工区的库位");
			}
			Location sourceLocation = locationBiz.findLocationByLocCode(sourceStockList.get(0).getWhId(), sourceStockList.get(0).getLocCode());
			stockManageBiz.canMove(sourceLocation, targetLocation, sourceStockList, request.getBoxCode(), true);
			if (locationBiz.isAgvLocation(targetLocation)) {
				//AGV移动任务生成
				agvTask.moveStockToSchedule(sourceStockList, targetLocation);
				return;
			}
			stockBiz.moveStockByLpnCode(sourceStockList.get(0).getLpnCode(), sourceStockList.get(0).getLpnCode(), targetLocation, StockLogTypeEnum.INSTOCK_BY_PUTAWAY_PDA, null, null, null);
			// 生成上架记录
			sourceStockList.forEach(stock -> {
				PutawayLog putawayLog = putawayFactory.create(request, stock, targetLocation);
				putawayLogDao.save(putawayLog);
			});
			return;
		}

		// 如果不是整托：按箱移动
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		boolean pickLocation = locationBiz.isPickLocation(targetLocation);
		if (!pickLocation) {
			throw new ServiceException("按箱上架失败，目标库位不是拣货区/人工区的库位");
		}
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		AssertUtil.notNull(stockList, "按箱上架失败，根据箱码查询不到对应库存");
		Location sourceLocation = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
		stockManageBiz.canMove(sourceLocation, targetLocation, stockList, request.getBoxCode(), true);
		if (locationBiz.isAgvLocation(targetLocation)) {
			//AGV移动任务生成
			agvTask.moveStockToSchedule(stockList, targetLocation);
			return;
		}
		stockBiz.moveStockByBoxCode(request.getBoxCode(), request.getBoxCode(), sourceStock.getLpnCode(), targetLocation, StockLogTypeEnum.INSTOCK_BY_PUTAWAY_PDA, null, null, null);
		// 生成上架记录
		PutawayLog putawayLog = putawayFactory.create(request, sourceStock, targetLocation);
		putawayLogDao.save(putawayLog);
	}


	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void callAgv(CallAgvRequest request) {
		//获取目标库位信息
		Location targetLocation = locationBiz.findByLocId(request.getLocId());
		List<BoxDto> boxDtoList = request.getBoxList();
		List<Stock> targetStockList = new ArrayList<>();
		for (BoxDto boxDto : boxDtoList) {
			List<Long> stockIdList = boxDto.getStockIdList();
			for (Long stockId : stockIdList) {
				// 根据id获取库存实体
				Stock stock = stockQueryBiz.findStockById(stockId);
				//获取数量
				BigDecimal qty = stock.getStockBalance();
				//获取序列号
				List<Serial> serialList = stockQueryBiz.findSerialByStock(stockId);
				List<String> serialNoList = new ArrayList<>();
				if (Func.isNotEmpty(serialList)) {
					serialNoList = serialList.stream()
						.map(Serial::getSerialNumber)
						.collect(Collectors.toList());
				}
				// 调用库存移动
				Stock targetStock = stockBiz.moveStock(stock, serialNoList, qty, targetLocation, StockLogTypeEnum.STOCK_TO_INSTOCK_RECE, null, null, null);
				targetStockList.add(targetStock);
				// 生成上架记录
				PutawayLog putawayLog = new PutawayLog();
				putawayLog.setLpnCode(stock.getLpnCode());
				putawayLog.setTargetLocCode(targetLocation.getLocCode());
				putawayLog.setWhId(request.getWhId());
				putawayLog.setUserName(AuthUtil.getUserName());
				putawayLog.setUserCode(AuthUtil.getUserAccount());
				putawayLog.setAplTime(LocalDateTime.now());
				putawayLogDao.save(putawayLog);
			}
		}
		agvTask.putawayToSchedule(targetStockList);


	}

	@Override
	public List<LocResponse> findLocByLpnType(LpnTypeRequest request) {
		List<LocResponse> locResponseList = new ArrayList<>();
		// 根据箱型和库房id获取库位信息
		List<Location> locationList = locationBiz.findLocationByLpnType(request);
		for (Location location : locationList) {
			LocResponse locResponse = new LocResponse();
			locResponse.setLocId(location.getLocId());
			String locCode = location.getLocCode();
			locResponse.setLocCode(locCode);
			//设置前端显示的库位编码 从第二次出现“-”的位置后的第一个位置截取到最后一位
			locResponse.setLocCodeView(locCode.substring(locCode.indexOf("-", locCode.indexOf("-") + 1) + 1));
			// 库位是否为空
			locResponse.setIsEmpty(stockBiz.judgeEnableOnLocation(location));
			locResponseList.add(locResponse);
		}
		return locResponseList;

	}
}
