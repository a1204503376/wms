package org.nodes.wms.core.stock.core.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.stock.core.dto.StockAddDTO;
import org.nodes.wms.core.stock.core.dto.StockSubtractDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.service.IStockMoveService;
import org.nodes.wms.core.stock.core.vo.StockSubtractVO;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品移动
 */
@Service("skuStockMoveService")
@Slf4j
@Primary
public class SkuStockMoveServiceImpl implements IStockMoveService<SkuStockMoveServiceImpl.SkuStockMoveParam>{
	@Override
	public List<Stock> move(SkuStockMoveParam moveParam) {
		// 参数验证
		if (Func.isEmpty(moveParam.getSourceLocId())) {
			if (ObjectUtil.isEmpty(moveParam.getSourceLocId())) {
				throw new ServiceException("指定原库位ID不能为空！");
			}
		}
		if (Func.isEmpty(moveParam.getTargetStockId())) {
			if (ObjectUtil.isEmpty(moveParam.getTargetLocId())) {
				throw new ServiceException("指定目标库位ID不能为空！");
			}
		}
		if (ObjectUtil.isEmpty(moveParam.getSkuId())) {
			throw new ServiceException("指定物品ID不能为空！");
		}
		if (ObjectUtil.isEmpty(moveParam.getMoveQty())) {
			throw new ServiceException("指定移动数量不能为空！");
		}
		if (BigDecimalUtil.eq(moveParam.getMoveQty(), BigDecimal.ZERO)) {
			throw new ServiceException("指定移动数量必须大于 0 ！");
		}
		if (ObjectUtil.isEmpty(moveParam.getEventType())) {
			throw new ServiceException("事务类型不能为空！");
		}
		// 验证库位
		Location sourceLoc = null;
		if (Func.isEmpty(moveParam.getSourceStockId())) {
			sourceLoc = LocationCache.getById(moveParam.getSourceLocId());
		} else {
			Stock stock = stockService.getById(moveParam.getSourceStockId());
			if (Func.isEmpty(stock)) {
				throw new ServiceException("指定源库存不存在！");
			}
			sourceLoc = LocationCache.getById(stock.getLocId());
		}
		Location targetLoc = null;
		if (Func.isEmpty(moveParam.getTargetStockId())) {
			targetLoc = LocationCache.getById(moveParam.getTargetLocId());
		} else {
			Stock stock = stockService.getById(moveParam.getTargetStockId());
			if (Func.isEmpty(stock)) {
				throw new ServiceException("指定目标库存不存在！");
			}
			targetLoc = LocationCache.getById(stock.getLocId());
		}
		if (sourceLoc.getLocId().equals(targetLoc.getLocId())) {
			throw new ServiceException("原库位不能与目标库位一样！");
		}
		// 验证物品
		Sku sku = SkuCache.getById(moveParam.getSkuId());
		if (ObjectUtil.isEmpty(sku)) {
			throw new ServiceException(String.format("指定物品不存在（物品ID：%s）！", moveParam.getSkuId()));
		}
		// 封装一个库存扣减参数对象
		StockSubtractDTO stockReduce = StockWrapper.build().moveToReduce(moveParam);
		if (ObjectUtil.isEmpty(stockReduce)) {
			throw new ServiceException("类型转换失败（StockMoveDTO 转 StockReduceDTO）！");
		}
		stockReduce.setLocCode(sourceLoc.getLocCode());
		List<StockSubtractVO> stockReduceRltList = stockService.stockSubtract(sku, stockReduce);
		if (stockReduceRltList.size() == 0) {
			throw new ServiceException("库存信息不存在！");
		}
		List<Stock> resultStock = new ArrayList<>();
		// 循环扣减后得库存，添加到目标货位
		for (StockSubtractVO stockReduceRlt : stockReduceRltList) {
			if (ObjectUtil.isEmpty(stockReduceRlt.getStock())) {
				continue;
			}
			// 封装一个库存增加参数对象
			StockAddDTO stockProc = StockWrapper.build().entityProc(stockReduceRlt.getStock(), moveParam);
			if (ObjectUtil.isEmpty(stockProc)) {
				throw new ServiceException("类型转换失败（Stock, StockMoveDTO 转 StockProcDTO）！");
			}
			stockProc.setBillId(stockReduceRlt.getSoBillId());
			stockProc.setLotNumber(moveParam.getLottNumber());
			stockProc.setBillDetailId(stockReduceRlt.getSoDetailId());
			// 增加库存
			resultStock.add(stockService.add(stockProc));
		}

		return resultStock;
	}

	@Data
	public static class SkuStockMoveParam extends IStockMoveService.MoveParam {

	}
}
