package org.nodes.wms.biz.stock.merge;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认库存合并策略
 * 合并的原则：
 * 货主、物品、库位、状态、箱码、LpnCode、dropId、30个批属性相同的才合并
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class DefaultStockMergeStrategy implements StockMergeStrategy {

	private final StockDao stockDao;

	@Override
	public Stock matchCanMergeStock(Stock newStock) {
		return match(newStock.getStockStatus(),
			newStock.getWoId(), newStock.getLocId(), newStock.getSkuId(),
			newStock.getBoxCode(), newStock.getLpnCode(), newStock.getDropId(), newStock);
	}

	@Override
	public Stock matchSameStock(ReceiveLog receiveLog) {
		AssertUtil.notNull(receiveLog, "根据清点记录查询库存失败,清点记录为空");
		return match(StockStatusEnum.NORMAL, receiveLog.getWoId(),
			receiveLog.getLocId(), receiveLog.getSkuId(), receiveLog.getBoxCode(),
			receiveLog.getLpnCode(), null, receiveLog);
	}

	@Override
	public Stock matchSameStock(LogSoPick pickLog, Location pickToLoc) {
		AssertUtil.notNull(pickLog, "根据清点记录查询库存失败,拣货记录为空");
		AssertUtil.notNull(pickToLoc, "根据清点记录查询库存失败,出库集货区为空");

		return match(pickLog.getStockStatus(), pickLog.getWoId(), pickToLoc.getLocId(),
			pickLog.getSkuId(), pickLog.getBoxCode(), pickLog.getLpnCode(), null, pickLog);
	}

	@Override
	public Stock newExpectedStock(Stock sourceStock, Location targetLocation, String targetBoxCode,
								  String targetLpnCode, String dropId) {
		Stock tempTargetStock = new Stock();
		BeanUtil.copy(sourceStock, tempTargetStock);
		// 匹配是否存在能合并的库存，此处必须更新locId, lpnCode, boxCode,dropId
		tempTargetStock.setLocId(targetLocation.getLocId());
		tempTargetStock.setLpnCode(targetLpnCode);
		tempTargetStock.setBoxCode(targetBoxCode);
		tempTargetStock.setDropId(dropId);
		if (StockStatusEnum.FREEZE.equals(targetLocation.defaultStockStatus())) {
			tempTargetStock.setStockStatus(StockStatusEnum.FREEZE);
		}

		return tempTargetStock;
	}

	private <T> Stock match(StockStatusEnum stockStatus, Long woId, Long locId,
							Long skuId, String boxCode, String lpnCode, String dorpId, T source) {
		if (!SkuLotUtil.hasSkuLot(source)) {
			throw new ServiceException("库存匹配失败,匹配原对象没有批属性信息");
		}

		List<Stock> existStockList = stockDao.matchStock(stockStatus, woId, locId,
			skuId, boxCode, lpnCode, dorpId);
		if (Func.isEmpty(existStockList)) {
			return null;
		}

		Stock existStock = null;
		int existStockNum = 0;
		for (Stock item : existStockList) {
			if (SkuLotUtil.compareAllSkuLot(item, source)) {
				existStockNum++;
				existStock = item;
			}
		}

		if (existStockNum > 1) {
			throw new ServiceException(String.format("库存匹配失败,到多条相同的库存,skuId[%d]-locId[%d]",
				skuId, locId));
		}

		return existStock;
	}
}
