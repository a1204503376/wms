package org.nodes.wms.biz.stock;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.stock.dto.input.StockBySerialPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockImportRequest;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 库存业务接口
 *
 * @author nodesc
 */
public interface StockBiz {

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
	 * 撤销拣货时库存操作，如果已经发运（出库集货区没有对应的库存）则会抛异常
	 *
	 * @param type 移动类型 必填
	 * @param pickLog 撤销记录，qty必须是负数
	 */
	void outStockByCancelPick(StockLogTypeEnum type, LogSoPick pickLog);

	/**
	 * 库存移动,可能会发生库存合并;如果目标库位为冻结状态，则目标库存会自动变为冻结状态
	 * 原库存状态为系统冻结在移动时抛异常；目标库位状态(locFlag)如果不是正常或冻结时抛异常
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
	 * 库存移动,可能会发生库存合并;如果目标库位为冻结状态，则目标库存会自动变为冻结状态
	 * 原库存状态为系统冻结在移动时抛异常；目标库位状态(locFlag)如果不是正常或冻结时抛异常
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
	 * 整箱移动,可能会发生库存合并;如果目标库位为冻结状态，则目标库存会自动变为冻结状态
	 * 原库存状态为系统冻结在移动时抛异常；目标库位状态(locFlag)如果不是正常或冻结时抛异常
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
	 * 整托移动,可能会发生库存合并;如果目标库位为冻结状态，则目标库存会自动变为冻结状态
	 * 原库存状态为系统冻结在移动时抛异常；目标库位状态(locFlag)如果不是正常或冻结时抛异常
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
	 * 冻结
	 *
	 * @param stockIds 必填
	 */
	void freezeStock(List<Long> stockIds);

	/**
	 * 解冻
	 *
	 * @param stockIds 必填
	 */
	void unfreezeStock(List<Long> stockIds);

	/**
	 * 按库位冻结
	 *
	 * @param locIds 必填
	 */
	void freezeStockByLoc(List<Long> locIds);

	/**
	 * 按库位解冻
	 *
	 * @param locIds 必填
	 */
	void unfreezeStockByLoc(List<Long> locIds);

	/**
	 * 按箱冻结
	 *
	 * @param boxCodes 必填
	 */
	void freezeStockByBoxCode(List<String> boxCodes);

	/**
	 * 按箱解冻
	 *
	 * @param boxCodes 必填
	 */
	void unFreezeStockByBoxCode(List<String> boxCodes);

	/**
	 * 按lpn冻结
	 *
	 * @param lpnCodes 必填
	 */
	void freezeStockByLpnCode(List<String> lpnCodes);

	/**
	 * 按lpn解冻
	 *
	 * @param lpnCodes 必填
	 */
	void unfreezeStockByLpnCode(List<String> lpnCodes);

	/**
	 * 校验库存的序列号
	 * 如果库存有关联序列号，但serialNoList为空，则抛异常
	 * 如果库存有关联序列号，则要求serialNoList的序列号必须是该库存的，否则抛异常
	 *
	 * @param stock        库存
	 * @param serialNoList 序列号编码
	 */
	void checkSerialOnStock(Stock stock, List<String> serialNoList);

	/**
	 * 检查库存的状态是否全部相等，并且等于指定的状态。可用根据isThrow参数指定方法是否抛异常
	 *
	 * @param stockList 库存对象
	 * @param status    状态
	 * @param isThrow   true表示如果不相同则抛异常，false表示不会抛异常
	 * @return true：表示库存的批属性都等于status
	 */
	boolean equalStockStatus(List<Stock> stockList, StockStatusEnum status, boolean isThrow);

	/**
	 * 天宜定制：判断该库位是否有库存或被冻结
	 *
	 * @param location 库位
	 * @return true：可用
	 */
	boolean judgeEnableOnLocation(Location location);

	/**
	 * 导入库存
	 *
	 * @param importDataList 导入的数据集合
	 * @return true:正常执行
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

	/**
	 * 系统任务冻结库存，该类冻结的库存不能执行移动等库内操作
	 *
	 * @param stocks 目标库存
	 * @param taskId 任务id
	 */
	void freezeStockByTask(List<Stock> stocks, Long taskId);
	/**
	 * 按序列号显示库存导出
	 *
	 * @param stockBySerialPageQuery 查询对象
	 * @param response               响应对象
	 */
	void exportBySerial(StockBySerialPageQuery stockBySerialPageQuery, HttpServletResponse response);
}
