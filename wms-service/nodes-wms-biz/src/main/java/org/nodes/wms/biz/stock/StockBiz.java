package org.nodes.wms.biz.stock;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.dto.input.StockImportRequest;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 库存业务接口
 **/
public interface StockBiz {

	/**
	 * 根据库位冻结
	 *
	 * @param type       库存移动类型
	 * @param locId      库位id
	 * @param occupyFlag 库位占用标记
	 */
	void freezeByLoc(StockLogTypeEnum type, Long locId, String occupyFlag);

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
	 * 撤销收货时下架库存
	 *
	 * @param type       移动类型 必填
	 * @param receiveLog 撤销记录，qty必须是负数
	 * @param stock      下架库存
	 * @return 下架后库存
	 */
	Stock outStockByCancelReceive(StockLogTypeEnum type, ReceiveLog receiveLog, Stock stock);

	/**
	 * 库存移动
	 *
	 * @param sourceStock    原库存,必填
	 * @param serialNoList   移动的序列号，可能为空
	 * @param qty            移动数量,必填
	 * @param targetLocation 目标库位,必填
	 * @param type           库存移动类型,必填
	 * @param billId         操作单id，可为空
	 * @param billNo         操作单编码，可为空
	 * @param lineNo         操作单行号，可为空
	 * @return 目标库存
	 */
	Stock moveStock(Stock sourceStock, List<String> serialNoList, BigDecimal qty,
					Location targetLocation, StockLogTypeEnum type,
					Long billId, String billNo, String lineNo);

	/**
	 * 库存移动
	 *
	 * @param sourceStock    原库存,必填
	 * @param serialNoList   移动的序列号，可能为空
	 * @param qty            移动数量,必填
	 * @param targetBoxCode  目标箱码
	 * @param targetLpnCode  目标托盘号
	 * @param targetLocation 目标库位 必填
	 * @param type           库存移动类型,必填
	 * @param billId         操作单id，可为空
	 * @param billNo         操作单编码，可为空
	 * @param lineNo         操作单行号，可为空
	 * @return 目标库存
	 */
	Stock moveStock(Stock sourceStock, List<String> serialNoList, BigDecimal qty,
					String targetBoxCode, String targetLpnCode,
					Location targetLocation, StockLogTypeEnum type,
					Long billId, String billNo, String lineNo);

	/**
	 * 整箱移动
	 *
	 * @param boxCode        需要移动的箱码，必填
	 * @param targetBoxCode  目标箱码，必填
	 * @param targetLpnCode  目标托盘号
	 * @param targetLocation 目标库位 必填
	 * @param type           移动类型 必填
	 * @param billId         单据id
	 * @param billNo         单据编码
	 * @param lineNo         单据明细行号
	 * @return 目标库存
	 */
	List<Stock> moveStockByBoxCode(String boxCode, String targetBoxCode, String targetLpnCode,
								   Location targetLocation, StockLogTypeEnum type,
								   Long billId, String billNo, String lineNo);

	/**
	 * 整托移动
	 *
	 * @param lpnCode        需要移动的托盘号，必填
	 * @param targetLpnCode  目标托盘号，必填
	 * @param targetLocation 目标库位 必填
	 * @param type           移动类型 必填
	 * @param billId         单据id
	 * @param billNo         单据编码
	 * @param lineNo         单据明细行号
	 * @return 目标库存
	 */
	List<Stock> moveStockByLpnCode(String lpnCode, String targetLpnCode, Location targetLocation, StockLogTypeEnum type,
								   Long billId, String billNo, String lineNo);

	/**
	 * 判断该库位是否有库存或被冻结
	 *
	 * @param location
	 * @return true：可用
	 */
	boolean judgeEnableOnLocation(Location location);

	/**
	 * 导入库存
	 *
	 * @param importDataList 导入的数据集合
	 */
	boolean importStockByExcel(List<StockImportRequest> importDataList);

	/**
	 * 库存列表导出
	 *
	 * @param stockPageQuery 查询参数
	 * @param response       返回对象
	 */
	void exportStockToExcel(StockPageQuery stockPageQuery, HttpServletResponse response);

	/**
	 * 库存日志导出
	 *
	 * @param stockLogPageQuery: 导出条件dto对象
	 * @param response:          响应对象
	 */
	void exportStockLogToExcel(StockLogPageQuery stockLogPageQuery, HttpServletResponse response);

}
