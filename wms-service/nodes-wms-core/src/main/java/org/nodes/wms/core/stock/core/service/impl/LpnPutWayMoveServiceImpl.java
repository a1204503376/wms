package org.nodes.wms.core.stock.core.service.impl;

import lombok.Data;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.service.IStockMoveService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 按托上架库存移动
 */
@Primary
@Service("lpnPutWayMoveService")
public class LpnPutWayMoveServiceImpl implements IStockMoveService<LpnPutWayMoveServiceImpl.LpnPutWayMoveParam> {
	@Override
	public List<Stock> move(LpnPutWayMoveParam moveParam) {
		if (Func.isNotEmpty(moveParam.getSourceStockId()) || Func.isNotEmpty(moveParam.getTargetStockId())) {
			throw new ServiceException("按托移动接口目前不支持指定库存ID！");
		}
		if (ObjectUtil.isEmpty(moveParam.getTargetLocId())) {
			throw new ServiceException("指定目标库位ID不能为空！");
		}
		if (ObjectUtil.isEmpty(moveParam.getEventType())) {
			throw new ServiceException("事务类型不能为空！");
		}
		if (ObjectUtil.isEmpty(moveParam.getSystemProcId())) {
			throw new ServiceException("系统日志id不能为空！");
		}
		// 验证库位
		Location sourceLoc = LocationCache.getValid(moveParam.getSourceLocId());
		Location targetLoc = LocationCache.getValid(moveParam.getTargetLocId());
		if (sourceLoc.getLocId().equals(targetLoc.getLocId())) {
			throw new ServiceException("原库位不能与目标库位一样！");
		}
		List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
//			.eq(Stock::getLpnCode, moveParam.getSourceLpnCode())
			.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
			.apply("stock_qty - pick_qty > 0"));
		// 验证待移动的库存信息
//		if (ObjectUtil.isEmpty(stockList)) {
//			throw new ServiceException(String.format("库位：%s 容器：%s 上没有库存信息，不允许执行此操作！",
//				sourceLoc.getLocCode(), moveParam.getSourceLpnCode()));
//		}
//		List<Stock> filterStockList = stockList.stream().filter(stock -> {
//			return stock.getOccupyQty().compareTo(BigDecimal.ZERO) > 0;
//		}).collect(Collectors.toList());
//		if (ObjectUtil.isNotEmpty(filterStockList)) {
//			StringBuffer expSf = new StringBuffer();
//			if (Func.isNotEmpty(sourceLoc.getLocCode())) {
//				expSf.append("库位：" + sourceLoc.getLocCode());
//			}
//			if (Func.isNotEmpty(moveParam.getSourceLpnCode())) {
//				expSf.append(" 容器：" + moveParam.getSourceLpnCode());
//			}
//			expSf.append(" 上的库存有占用数量，不允许执行此操作！");
//			throw new ServiceException(expSf.toString());
//		}
//		// 数据没问题了，循环移动库存
//		for (Stock stock : stockList) {
//			List<Serial> serialList = serialService.listByStockId(stock.getStockId());
//			List<String> serialNumbers = NodesUtil.toList(serialList, Serial::getSerialNumber);
//			// 更新库存信息
//			UpdateWrapper<Stock> updateWrapper = new UpdateWrapper<>();
//			updateWrapper.lambda()
//				.set(Stock::getZoneId, targetLoc.getZoneId())
//				.set(Stock::getLocId, targetLoc.getLocId())
//				.set(Stock::getLastInTime, LocalDateTime.now())
//				.set(Stock::getUpdateTime, LocalDateTime.now())
//				.eq(Stock::getStockId, stock.getStockId());
//			stockService.update(updateWrapper);
//			if (ObjectUtil.isNotEmpty(serialNumbers)) {
//				for (String serialNumber : serialNumbers) {
//					serialService.save(serialNumber, stock, moveParam.getSystemProcId());
//				}
//			}
//			stockLogService.create(stock, StockProcTypeEnum.Update, moveParam.getSystemProcId());
//		}
		return stockList;
	}
	@Data
	public static class LpnPutWayMoveParam extends IStockMoveService.MoveParam{

	}

}
