package org.nodes.wms.biz.stock.merge;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.stock.entities.Stock;

/**
 * 库存合并策略
 *
 * @author nodesc
 */
public interface StockMergeStrategy {

	/**
	 * 从数据库中查询可以合并的库存，如果不需要合并返回空的库存，如果需要合并返回旧的库存
	 *
	 * @param newStock 新的库存数据,尚未保存到数据库中
	 * @return 可以合并的数据库中已有的库存
	 */
	Stock matchCanMergeStock(Stock newStock);

	/**
	 * 匹配相同的库存
	 *
	 * @param receiveLog 清点记录
	 * @return 和清点记录相同的库存
	 */
	Stock matchSameStock(ReceiveLog receiveLog);

	/**
	 * 匹配相同的库存
	 *
	 * @param pickLog   拣货记录
	 * @param pickToLoc 出库集货区
	 * @param matchLpnCode true表示需要匹配lpnCode，false表示不需要匹配lpnCode
	 * @return 和清点记录相同的库存
	 */
	Stock matchSameStock(LogSoPick pickLog, Location pickToLoc, boolean matchLpnCode);

	/**
	 * 生成预期待合并的库存
	 *
	 * @param sourceStock    原库存
	 * @param targetLocation 目标库位
	 * @param targetBoxCode  目标箱码
	 * @param targetLpnCode  目标LPN
	 * @param dropId         dropId
	 * @return 预期库存
	 */
	Stock newExpectedStock(Stock sourceStock, Location targetLocation, String targetBoxCode,
						   String targetLpnCode, String dropId);
}
