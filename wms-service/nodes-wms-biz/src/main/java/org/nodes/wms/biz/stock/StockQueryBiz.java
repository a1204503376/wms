package org.nodes.wms.biz.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.count.dto.output.PdaBoxQtyResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.putaway.dto.output.CallAgvResponse;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.input.StockBySerialPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.*;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 库存查询的相关业务
 *
 * @author nodesc
 */
public interface StockQueryBiz {

	/**
	 * 根据Id获取库存实体,包含了出库暂存区
	 *
	 * @param stockId 必填
	 * @return Stock
	 */
	Stock findStockById(Long stockId);

	/**
	 * 根据 传过来的stockIds集合获取库存集合
	 *
	 * @param stockIdList 库存id集合
	 * @return 库存集合
	 */
	List<Stock> findStockById(List<Long> stockIdList);

	/**
	 * 根据清点记录查询入库暂存区的库存,如果查询的库存超过两个会报异常
	 *
	 * @param receiveLog 清点记录，必填
	 * @return Stock
	 */
	Stock findStockOnStage(ReceiveLog receiveLog);

	/**
	 * 根据拣货记录在出库集货区查找库存
	 *
	 * @param pickLog 拣货记录
	 * @return stock
	 */
	Stock findStockOnPickTo(LogSoPick pickLog);

	/**
	 * 根据落放id获取库存
	 *
	 * @param dropId 落放id
	 * @return 库存集合
	 */
	List<Stock> findStockByDropId(Long dropId);

	/**
	 * 根据库位获取库位的所有库存
	 *
	 * @param locationList 必填，库位集合
	 * @return Stock集合
	 */
	List<Stock> findStockByLocation(List<Location> locationList);

	/**
	 * 根据库位id查询库存
	 *
	 * @param locationId 必填，库位id
	 * @return Stock集合
	 */
	List<Stock> findStockByLocation(Long locationId);

	/**
	 * 判断库位是否有库存
	 *
	 * @param locationId 库位id
	 * @return true表示空库位（无库存）
	 */
	boolean isEmptyLocation(Long locationId);

	/**
	 * 根据lpn code查询库存，含出库暂存区的
	 *
	 * @param lpnCode 必填
	 * @return Stock集合
	 */
	List<Stock> findStockByLpnCode(String lpnCode);

	/**
	 * 根据箱码查询库存,排除出库暂存区
	 *
	 * @param boxCode 箱码，必填
	 * @return Stock集合
	 */
	List<Stock> findEnableStockByBoxCode(String boxCode);

	/**
	 * 根据箱码集合查询库存,排除出库暂存区
	 *
	 * @param boxCodeList 箱码集合，必填
	 * @return Stock集合
	 */
	List<Stock> findEnableStockByBoxCode(List<String> boxCodeList);

	/**
	 * 根据箱码获取入库暂存区的库存
	 *
	 * @param whId    必填，库房id
	 * @param boxCode 必填，箱码
	 * @return Stock集合
	 */
	List<Stock> findStockOnStageByBoxCode(Long whId, String boxCode);

	/**
	 * 查找可用库存,排除出库暂存区
	 *
	 * @param skuLot 必填，如果批属性不为空，则需要匹配
	 * @return Stock集合
	 */
	List<Stock> findEnableStockByZoneTypeAndSkuLot(SkuLotBaseEntity skuLot);

	/**
	 * 查找可用库存,排除出库暂存区
	 *
	 * @param whId            必填，库房id
	 * @param skuId           必填，物品id
	 * @param stockStatusEnum 非必填，如果为空则表示查询所有状态
	 * @param zoneTypeList    非必填，如果为空则表示不限制库区类型
	 * @param skuLot          非必填，如果批属性不为空，则需要匹配
	 * @return Stock集合
	 */
	List<Stock> findEnableStockByZoneTypeAndSkuLot(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
												   List<String> zoneTypeList, SkuLotBaseEntity skuLot);

	/**
	 * 根据库区id查询可用库存,排除出库暂存区
	 *
	 * @param whId            必填，库房id
	 * @param skuId           必填，物品id
	 * @param stockStatusEnum 非必填，如果为空则表示查询所有状态
	 * @param zoneIdList      非必填，如果为空则表示不限制库区
	 * @param skuLot          非必填，如果批属性不为空，则需要匹配
	 * @return Stock集合
	 */
	List<Stock> findEnableStockByZoneAndSkuLot(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
											   List<Long> zoneIdList, SkuLotBaseEntity skuLot);

	/**
	 * 根据库位id查询可用库存,排除出库暂存区
	 *
	 * @param whId            必填，库房id
	 * @param skuId           必填，物品id
	 * @param stockStatusEnum 非必填，如果为空则表示查询所有状态
	 * @param locationIdList  非必填，如果为空则表示不限制库位
	 * @param skuLot          非必填，如果批属性不为空，则需要匹配
	 * @return Stock集合
	 */
	List<Stock> findEnableStockByLocationAndSkuLot(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
												   List<Long> locationIdList, SkuLotBaseEntity skuLot);

	/**
	 * 根据物品和批次信息查找库存，排除出库暂存区
	 *
	 * @param skuId  必填，物品id
	 * @param skuLot 非必填，如果批属性不为空，则需要匹配
	 * @return Stock集合
	 */
	List<Stock> findEnableStockBySkuAndSkuLot(Long skuId, SkuLotBaseEntity skuLot);

	/**
	 * 根据序列号编码获取在库的序列号信息
	 *
	 * @param serialNoList 序列号编码
	 * @return 在库的序列号
	 */
	List<Serial> findSerialBySerialNo(List<String> serialNoList);

	/**
	 * 根据库存查询所有在库的序列号
	 *
	 * @param stockId 库存主键
	 * @return 序列号
	 */
	List<Serial> findSerialByStock(Long stockId);

	/**
	 * 根据库存id获取序列号数量
	 *
	 * @param stockId 库存id
	 * @return 序列号数量
	 */
	int getSerialCountByStockId(Long stockId);

	/**
	 * 首页的库存数据统计
	 *
	 * @return 统计数据
	 */
	StockIndexResponse staticsStockDataOnIndexPage();

	/**
	 * PDA：获取库存分页
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return Pda根据编码查询库存-响应对象
	 */
	IPage<FindAllStockByNoResponse> selectStockList(FindAllStockByNoRequest request, Query query);

	/**
	 * PC：获取库存分页
	 *
	 * @param query          分页参数
	 * @param stockPageQuery 查询参数
	 * @return 分页对象
	 */
	Page<StockPageResponse> getStockPage(Query query, StockPageQuery stockPageQuery);

	/**
	 * 库存日志分页查询
	 *
	 * @param query:             分页参数
	 * @param stockLogPageQuery: 查询条件dto对象
	 * @return Page<StockLogPageResponse>
	 */
	Page<StockLogPageResponse> pageStockLog(Query query, StockLogPageQuery stockLogPageQuery);

	/**
	 * 天宜定制：根据箱号模糊查询所在LPN上的库存
	 *
	 * @param whId    库房id
	 * @param boxCode 箱码的后几位
	 * @return key:lpn编码
	 */
	List<CallAgvResponse> findLpnStockOnStageLeftByCallAgv(Long whId, String boxCode);

	/**
	 * 库存余额按序列号显示分页信息
	 *
	 * @param query                  分页参数
	 * @param stockBySerialPageQuery 查询条件
	 * @return 分页对象
	 */
	IPage<StockBySerialPageResponse> getStockBySerialPage(Query query, StockBySerialPageQuery stockBySerialPageQuery);

	/**
	 * 获取库存统计
	 *
	 * @param locCode 库位编码
	 * @param boxCode 箱码
	 * @param skuCode 物品编码
	 * @return 库存
	 */
	List<PdaBoxQtyResponse> getStockCountByLocCode(String locCode, String boxCode, String skuCode);
}
