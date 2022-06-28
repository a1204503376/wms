package org.nodes.wms.biz.stock;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存业务接口
 **/
public interface StockBiz {

	/**
	 * 根据库位冻结
	 *
	 * @param type  库存移动类型
	 * @param locId 库位id
	 */
	void freezeByLoc(StockLogTypeEnum type, Long locId);

	/**
	 * 根据库位解冻
	 *
	 * @param type  库存移动类型
	 * @param locId 库位id
	 */
	void unfreezeByLoc(StockLogTypeEnum type, Long locId);

	/**
	 * 收货入库
	 *
	 * @param type       入库来源
	 * @param receiveLog 清点记录
	 * @return 目标库存
	 */
	Stock inStock(StockLogTypeEnum type, ReceiveLog receiveLog);

	/**
	 * 库存移动
	 *
	 * @param sourceStock    原库存
	 * @param serialNoList   移动的序列号，可能为空
	 * @param qty            移动数量
	 * @param targetLocation 目标库位
	 * @param type           库存移动类型
	 * @return 目标库存
	 */
	Stock moveStock(Stock sourceStock, List<String> serialNoList, BigDecimal qty,
					Location targetLocation, StockLogTypeEnum type);

	/**
	 * 根据想码查询库存
	 *
	 * @param boxCode
	 * @return
	 */
	List<Stock> findStockByBoxCode(String boxCode);

	/**
	 * 首页的库存数据统计
	 *
	 * @return 统计数据
	 */
	StockIndexResponse staticsStockDataOnIndexPage();

	/**
	 * @param skuCode 物品编码
	 * @param skuName 物品名称
	 * @param locCode 库位编码
	 * @return 库存信息
	 */
	Stock findStockBySku(String skuCode, String skuName, String locCode);

}
