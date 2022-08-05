package org.nodes.wms.biz.putway.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putway.PutwayBiz;
import org.nodes.wms.biz.putway.modular.PutwayFactory;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.PutawayLogDao;
import org.nodes.wms.dao.putway.dto.input.PutwayByBoxRequest;
import org.nodes.wms.dao.putway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putway.dto.output.BoxDto;
import org.nodes.wms.dao.putway.dto.output.LocResponse;
import org.nodes.wms.dao.putway.entities.PutawayLog;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
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
public class PutwayBizImpl implements PutwayBiz {
	private final LocationBiz locationBiz;
	private final StockBiz stockBiz;
	private final PutawayLogDao putawayLogDao;
	private final StockQueryBiz stockQueryBiz;
	private final PutwayFactory putwayFactory;
	private final AgvTask agvTask;

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void putwayByBox(PutwayByBoxRequest request) {
		// TODO bug
		// request中的stockId应改为list
		// 判断库存是否在入库暂存区，如果不是入库暂存区应报异常
		// 如果是整托：原库中的lpn为空白字符，需要抛异常
		// 如果是整托：按托移动
		// 如果不是整托：按箱移动
		// 生成上架记录


		// 调用库存移动
		Stock sourceStock = stockQueryBiz.findStockById(request.getStockId());
		AssertUtil.notNull(sourceStock, "没有该库存信息");
		if (request.getIsAllLpnPutaway() && Func.isNotEmpty(sourceStock.getLpnCode())) {
			List<Stock> stockList = stockQueryBiz.findStockByLpnCode(sourceStock.getLpnCode());
			AssertUtil.notNull(stockList, "暂无与此托盘号相关库存的信息");
			Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
			stockBiz.moveStockByLpnCode(sourceStock.getLpnCode(), sourceStock.getLpnCode(), targetLocation, StockLogTypeEnum.INSTOCK_BY_PUTAWAY, null, null, null);
			// 生成上架记录
			stockList.forEach(stock -> {
				PutawayLog putawayLog = putwayFactory.create(request, stock, targetLocation);
				putawayLogDao.save(putawayLog);
			});
			return;
		}

		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Stock> stockList = stockQueryBiz.findStockOnStageByBoxCode(request.getWhId(), request.getBoxCode());
		stockList.forEach(stock -> {
			//获取序列号 如果库存关联了序列号需要获取序列号
			List<Serial> serialList = stockQueryBiz.findSerialByStock(stock.getStockId());
			List<String> serialNoList = new ArrayList<>();
			if (Func.isNotEmpty(serialList)) {
				serialNoList = serialList.stream()
					.map(Serial::getSerialNumber)
					.collect(Collectors.toList());
			}
			if (BigDecimalUtil.gt((request.getQty().subtract(stock.getStockEnable())), BigDecimal.ZERO)) {
				request.setQty(request.getQty().subtract(stock.getStockEnable()));
				stockBiz.moveStock(stock, serialNoList, stock.getStockEnable(), targetLocation, StockLogTypeEnum.INSTOCK_BY_PUTAWAY, null, null, null);
			} else {
				stockBiz.moveStock(stock, serialNoList, request.getQty(), targetLocation, StockLogTypeEnum.INSTOCK_BY_PUTAWAY, null, null, null);
			}

		});
		// 生成上架记录
		PutawayLog putawayLog = putwayFactory.create(request, sourceStock, targetLocation);
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
		agvTask.putwayToSchedule(targetStockList);


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
