package org.nodes.wms.dao.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.count.dto.output.PdaBoxQtyResponse;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.input.StockBySerialPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.FindAllStockByNoResponse;
import org.nodes.wms.dao.stock.dto.output.StockBySerialPageResponse;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存Dao接口
 *
 * @author nodesc
 */
public interface StockDao {

	/**
	 * 根据stockId获取库存
	 *
	 * @param stockIds stock id list
	 * @return stock集合
	 */
	List<Stock> getStockById(List<Long> stockIds);

	/**
	 * 根据库位id查询库存数量
	 *
	 * @param locIdList: 库位id集合
	 * @return Map<String, Object>
	 */
	Map<String, Object> getStockQtyByLocIdList(List<Long> locIdList);

	/**
	 * 根据库位id获取库存物品总数
	 *
	 * @param locIdList:
	 * @return Integer
	 */
	Integer getStockSkuCountByLocIdList(List<Long> locIdList);

	/**
	 * 获取所有库存数据，含冻结的库存。如果出库暂存区为空则表示查询的库存数据包含出库暂存区的
	 *
	 * @param boxCodes         箱码集合
	 * @param excludeLocIdList 排除的库位id
	 * @return stock集合
	 */
	List<Stock> getStockByBoxCodeExcludeLoc(List<String> boxCodes, List<Long> excludeLocIdList);

	/**
	 * 根据boxCode获取指定库存
	 *
	 * @param boxCode   必填，不能为空
	 * @param locIdList 可为空
	 * @return stock集合
	 */
	List<Stock> getStockByBoxCode(String boxCode, List<Long> locIdList);

	/**
	 * 根据箱码查询指定库位的库存
	 *
	 * @param boxCodes  箱码，必填
	 * @param locIdList 库位id集合，如果为空则查询所有的库位
	 * @return stock集合
	 */
	List<Stock> getStockByBoxCode(List<String> boxCodes, List<Long> locIdList);

	/**
	 * 根据箱号左模糊查找
	 *
	 * @param boxCode   箱号的后几位
	 * @param locIdList 库位id
	 * @return stock集合
	 */
	List<Stock> getStockLeftLikeByBoxCode(String boxCode, List<Long> locIdList);

	/**
	 * 获取所有库存数据，含冻结的库存。如果出库暂存区为空则表示查询的库存数据包含出库暂存区的
	 *
	 * @param lpnCode          lpn编码
	 * @param excludeLocIdList 排除的库位id
	 * @return stock集合
	 */
	List<Stock> getStockByLpnCodeExcludeLoc(String lpnCode, List<Long> excludeLocIdList);

	/**
	 * 根据lpn和loc获取指定库存
	 *
	 * @param lpnCode   必填，不能为空
	 * @param locIdList 可为空
	 * @return stock集合
	 */
	List<Stock> getStockByLpnCode(String lpnCode, List<Long> locIdList);

	/**
	 * 根据lpn编码查询指定库位的库存
	 *
	 * @param lpnCodes  lpn编码，必填
	 * @param locIdList 库位id，如果为空则查询所有库位的库存
	 * @return stock集合
	 */
	List<Stock> getStockByLpnCode(List<String> lpnCodes, List<Long> locIdList);

	/**
	 * 根据箱码查询该LPN上所有的库存，含自身
	 *
	 * @param boxCode 必填，不能为空
	 * @return stock集合
	 */
	List<Stock> getStockOnLpnByBoxCode(String boxCode);

	/**
	 * 查询库存
	 *
	 * @param status  库存状态，必填
	 * @param woId    货主id，必填
	 * @param locId   库位id， 必填
	 * @param skuId   物品id，必填
	 * @param boxCode 箱码，为空时则查询空箱码的库存，必填
	 * @param lpnCode lpn编码，为空时则查询空lpn的库存，必填
	 * @return stock集合
	 */
	List<Stock> getStock(StockStatusEnum status, Long woId, Long locId,
			Long skuId, String boxCode, String lpnCode);

	/**
	 * 匹配库存，即使库存余额为0也会被匹配到
	 *
	 * @param status  库存状态，必填
	 * @param woId    货主id，必填
	 * @param locId   库位id， 必填
	 * @param skuId   物品id，必填
	 * @param boxCode 箱码，为空时则查询空箱码的库存，必填
	 * @param lpnCode lpn编码，为空时则查询空lpn的库存，必填
	 * @param dropId  落放id，为空时采用空白字符替代
	 * @return stock集合
	 */
	List<Stock> matchStock(StockStatusEnum status, Long woId, Long locId,
			Long skuId, String boxCode, String lpnCode, String dropId);

	/**
	 * 根据库位id查找库存
	 *
	 * @param locId 库位id，必填
	 * @return stock集合
	 */
	List<Stock> getStockByLocId(Long locId);

	/**
	 * 保存新的库存
	 *
	 * @param stock 库存
	 * @return stock
	 */
	Stock saveNewStock(Stock stock);

	/**
	 * 根据stockId更新库存信息，stock中如果stockId为空则会抛异常
	 *
	 * @param stock 需要更新的库存信息
	 * @return stock
	 */
	Stock updateStock(Stock stock);

	/**
	 * 更新库存数量
	 *
	 * @param stockId      stock id，必填
	 * @param stockQty     新的上架数量，必填
	 * @param stayStockQty 新的待上架数量，必填
	 * @param pickQty      新的下架数量，必填
	 * @param lastInTime   最近入库时间，非必填
	 * @param lastOutTime  最近出库时间，分必填
	 */
	void updateStock(Long stockId, BigDecimal stockQty, BigDecimal stayStockQty,
			BigDecimal pickQty, LocalDateTime lastInTime, LocalDateTime lastOutTime);

	/**
	 * 批量更新库存状态
	 *
	 * @param stockIds stock id，必填
	 * @param status   库存状态，必填
	 */
	void updateStock(List<Long> stockIds, StockStatusEnum status);

	/**
	 * 根据库位查找库存
	 *
	 * @param locIdList 库位id，必填
	 * @return stock集合
	 */
	List<Stock> getStockByLocIdList(List<Long> locIdList);

	/**
	 * 根据stock id获取库存
	 *
	 * @param stockId stock id，必填
	 * @return stock
	 */
	Stock getStockById(Long stockId);

	/**
	 * 获取库存分页
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param page    分页条件
	 * @return Pda根据编码查询库存-响应对象
	 */
	IPage<FindAllStockByNoResponse> getStockList(FindAllStockByNoRequest request, IPage<Stock> page);

	/**
	 * 获取库存分页 --pc端
	 *
	 * @param page           分页参数
	 * @param stockPageQuery 查询参数
	 * @return 分页对象
	 */
	Page<StockPageResponse> page(IPage<StockPageResponse> page, StockPageQuery stockPageQuery);

	/**
	 * 获取导出数据集合
	 *
	 * @param stockPageQuery 查询参数
	 * @return stock集合
	 */
	List<StockPageResponse> getStockResponseByQuery(StockPageQuery stockPageQuery);

	/**
	 * 查找物品可用库存
	 *
	 * @param whId              库房id，必填
	 * @param skuId             物品id，必填
	 * @param stockStatusEnum   库存状态，为空标识查询所有状态的库存
	 * @param zoneIdList        库区id，为空则查询所有库区的库存
	 * @param skuLot            批属性信息，可为空
	 * @param excludeZoneIdList 排除的库区id
	 * @return stock集合
	 */
	List<Stock> findEnableStockByZone(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
			List<Long> zoneIdList, SkuLotBaseEntity skuLot,
			List<Long> excludeZoneIdList);

	/**
	 * 查询物品可用库存
	 *
	 * @param whId              库房id，必填
	 * @param skuId             物品id，必填
	 * @param stockStatusEnum   库存状态，为空标识查询所有状态的库存
	 * @param locationIdList    库位id，为空标识查所有库位的库存
	 * @param skuLot            批属性信息，非必填
	 * @param excludeZoneIdList 排除的库区id
	 * @return stock集合
	 */
	List<Stock> findEnableStockByLocation(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
			List<Long> locationIdList, SkuLotBaseEntity skuLot,
			List<Long> excludeZoneIdList);

	/**
	 * 库存余额按箱显示
	 *
	 * @param stockPageQuery 查询参数
	 * @return 库存信息
	 */
	List<StockPageResponse> getStockResponseByBoxOrByLpn(StockPageQuery stockPageQuery);

	/**
	 * 按照批属性查找排除库位中的库存
	 *
	 * @param excludeLocId 排除的库位id,非必填
	 * @param skuLot       批属性
	 * @return stock集合
	 */
	List<Stock> getEnableStockBySkuLotAndExcludeLoc(List<Long> excludeLocId, SkuLotBaseEntity skuLot);

	/**
	 * 库存余额按序列号显示获取分页信息
	 *
	 * @param page                   分页条件
	 * @param stockBySerialPageQuery 查询参数
	 * @return 分页对象
	 */
	Page<StockBySerialPageResponse> page(IPage<?> page, StockBySerialPageQuery stockBySerialPageQuery);

	/**
	 * 根据任务id获取库存
	 *
	 * @param dropId 落放id
	 * @return stock集合
	 */
	List<Stock> getStockByDropId(Long dropId);

	/**
	 * 库存统计
	 *
	 * @param locCode 库位
	 * @return PdaBoxQtyResponse
	 */
	List<PdaBoxQtyResponse> getStockCountByLocCode(String locCode, String boxCode);

	/**
	 * 根据stock更新库存状态和落放id
	 *
	 * @param stocks stocks只需要其中的stockId作为更新的条件
	 * @param status 库存状态
	 * @param dropId 落放id
	 */
	void updateStockByDropId(List<Stock> stocks, StockStatusEnum status, String dropId);

	/**
	 * 更新库存占用量
	 *
	 * @param stock stockId不能为空，其中occupyQty需要是更新之后的量
	 */
    void upateOccupyQty(Stock stock);
}
