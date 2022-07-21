package org.nodes.wms.biz.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.putway.dto.output.CallAgvResponse;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.FindAllStockByNoResponse;
import org.nodes.wms.dao.stock.dto.output.StockIndexResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogPageResponse;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.mp.support.Query;

import java.util.List;

public interface StockQueryBiz {
	/**
	 * 根据序列号编码获取在库的序列号信息
	 *
	 * @param serialNoList 序列号编码
	 * @return 在库的序列号
	 */
	List<Serial> findSerialBySerialNo(List<String> serialNoList);

	/**
	 * 根据库存查询所有的序列号
	 *
	 * @param stockId 库存主键
	 * @return 序列号
	 */
	List<Serial> findSerialByStock(Long stockId);

	/**
	 * 根据箱码查询库存
	 *
	 * @param boxCode 箱码，必填
	 * @return 库存对象
	 */
	List<Stock> findStockByBoxCode(String boxCode);

	/**
	 * 根据箱码获取入库暂存区的库存
	 *
	 * @param whId
	 * @param boxCode
	 * @return
	 */
	List<Stock> findStockOnStageByBoxCode(Long whId, String boxCode);

	/**
	 * 根据清点记录查询入库暂存区的库存,如果查询的库存超过两个会报异常
	 *
	 * @param receiveLog
	 * @return
	 */
	Stock findStockOnStage(ReceiveLog receiveLog);

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
	 * 根据库位获取库位的所有库存
	 *
	 * @param locationList
	 * @return
	 */
	List<Stock> findStockByLocation(List<Location> locationList);

	/**
	 * 根据Id获取库存实体
	 */
	Stock findStockById(Long stockId);

	/**
	 * 天宜定制：根据箱号模糊查询所在LPN上的库存
	 *
	 * @param whId    库房id
	 * @param boxCode 箱码的后几位
	 * @return key:lpn编码
	 */
	List<CallAgvResponse> findLpnStockOnStageLeftByCallAgv(Long whId, String boxCode);

	/**
	 * 获取库存分页
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return Pda根据编码查询库存-响应对象
	 */
	IPage<FindAllStockByNoResponse> selectStockList(FindAllStockByNoRequest request, Query query);

	/**
	 * 获取库存分页
	 *
	 * @param query          分页参数
	 * @param stockPageQuery 查询参数
	 * @return 分页对象
	 */
	Page<StockPageResponse> getStockPage(Query query, StockPageQuery stockPageQuery);
}
