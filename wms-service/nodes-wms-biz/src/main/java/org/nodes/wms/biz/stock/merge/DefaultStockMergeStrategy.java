package org.nodes.wms.biz.stock.merge;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认库存合并策略
 * 合并的原则：
 * 货主、物品、库位、状态、箱码、LPNCode、30个批属性相同的才合并
 */
@Service
@RequiredArgsConstructor
public class DefaultStockMergeStrategy implements StockMergeStrategy {

	private final StockDao stockDao;

	@Override
	public Stock apply(Stock newStock) {
		List<Stock> existStockList = stockDao.getStock(newStock.getStockStatus(),
			newStock.getWoId(), newStock.getLocId(), newStock.getSkuId(),
			newStock.getBoxCode(), newStock.getLpnCode());
		if (Func.isEmpty(existStockList)) {
			return null;
		}

		Stock existStock = null;
		int existStockNum = 0;
		for (Stock item : existStockList) {
			if (SkuLotUtil.compareAllSkuLot(item, newStock)) {
				existStockNum++;
				existStock = item;
			}
		}

		if (existStockNum > 1) {
			throw new ServiceException(String.format("库存合并失败，%s在%s存在多条库存-%s",
				newStock.getSkuCode(), newStock.getLocCode(), newStock.getSkuLot1()));
		}

		return existStock;
	}
}
