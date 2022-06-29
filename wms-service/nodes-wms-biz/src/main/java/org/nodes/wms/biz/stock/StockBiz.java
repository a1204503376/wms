package org.nodes.wms.biz.stock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogPageResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.entities.StockSerial;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 库存业务接口
 **/
public interface StockBiz {

	/**
	 * 根据库位冻结
	 * @param type  库存移动类型
	 * @param locId 库位id
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
	 * 根据序列号编码获取在库的序列号信息
	 * @param serialNoList 序列号编码
	 * @return 在库的序列号
	 */
	StockSerial findSerialBySerialNo(List<String> serialNoList);

	/**
	 * 根据箱码查询库存
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
	 * 库存日志分页查询
	 *
	 * @param query:             分页参数
	 * @param stockLogPageQuery: 查询条件dto对象
	 * @return Page<StockLogPageResponse>
	 */
	Page<StockLogPageResponse> pageStockLog(Query query, StockLogPageQuery stockLogPageQuery);

	/**
	 * 导出
	 *
	 * @param stockLogPageQuery: 导出条件dto对象
	 * @param response:          响应对象
	 */
	void export(StockLogPageQuery stockLogPageQuery, HttpServletResponse response);
}
