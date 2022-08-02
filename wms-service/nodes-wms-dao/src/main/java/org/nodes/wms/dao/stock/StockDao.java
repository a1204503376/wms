package org.nodes.wms.dao.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
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
 * @author caiyun
 */
public interface StockDao {

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
	 * @param boxCodes
	 * @param excludeLocIdList
	 * @return
	 */
	List<Stock> getStockByBoxCodeExcludeLoc(List<String> boxCodes, List<Long> excludeLocIdList);

	/**
	 * 根据boxCode获取指定库存
	 *
	 * @param boxCode   必填，不能为空
	 * @param locIdList 可为空
	 * @return
	 */
	List<Stock> getStockByBoxCode(String boxCode, List<Long> locIdList);

	List<Stock> getStockByBoxCode(List<String> boxCodes, List<Long> locIdList);

	/**
	 * 根据箱号左模糊查找
	 *
	 * @param boxCode   箱号的后几位
	 * @param locIdList 库位id
	 * @return 库存
	 */
	List<Stock> getStockLeftLikeByBoxCode(String boxCode, List<Long> locIdList);

	/**
	 * 获取所有库存数据，含冻结的库存。如果出库暂存区为空则表示查询的库存数据包含出库暂存区的
	 *
	 * @param lpnCode
	 * @param excludeLocIdList
	 * @return
	 */
	List<Stock> getStockByLpnCodeExcludeLoc(String lpnCode, List<Long> excludeLocIdList);

	/**
	 * 根据lpn和loc获取指定库存
	 *
	 * @param lpnCode   必填，不能为空
	 * @param locIdList 可为空
	 * @return
	 */
	List<Stock> getStockByLpnCode(String lpnCode, List<Long> locIdList);

	List<Stock> getStockByLpnCode(List<String> lpnCodes, List<Long> locIdList);

	/**
	 * 根据箱码查询该LPN上所有的库存，含自身
	 *
	 * @param boxCode 必填，不能为空
	 * @return
	 */
	List<Stock> getStockOnLpnByBoxCode(String boxCode);

	List<Stock> getStock(StockStatusEnum status, Long woId, Long locId,
			Long skuId, String boxCode, String lpnCode);

	List<Stock> getStockByLocId(Long locId);

	Stock saveNewStock(Stock stock);

	Stock updateStock(Stock stock);

	void updateStock(Long stockId, BigDecimal stockQty, BigDecimal stayStockQty,
			BigDecimal pickQty, LocalDateTime lastInTime, LocalDateTime lastOutTime);

	void updateStock(List<Long> stockIds, StockStatusEnum status);

	List<Stock> getStockByLocIdList(List<Long> locIdList);

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
	 * @return
	 */
	List<StockPageResponse> getStockResponseByQuery(StockPageQuery stockPageQuery);

	List<Stock> findEnableStockByZone(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
			List<Long> zoneIdList, SkuLotBaseEntity skuLot,
			List<Long> excludeZoneIdList);

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
	 * @param exculdeLocId 排除的库位id,非必填
	 * @param skuLot       批属性
	 * @return 库存数据
	 */
	List<Stock> getEnableStockBySkuLotAndExculdeLoc(List<Long> exculdeLocId, SkuLotBaseEntity skuLot);

	/**
	 * 库存余额按序列号显示获取分页信息
	 *
	 * @param page                   分页条件
	 * @param stockBySerialPageQuery 查询参数
	 * @return 分页对象
	 */
	Page<StockBySerialPageResponse> page(IPage<?> page, StockBySerialPageQuery stockBySerialPageQuery);

	/**
	 * 根据系统任务，更新库存状态
	 *
	 * @param stockIds    库存数据
	 * @param status      库存状态
	 * @param isUpadteLpn true:表示用taskId更新lpn的值
	 * @param taskId      用来替换lpn的值
	 */
	void updateStock(List<Long> stockIds, StockStatusEnum status, boolean isUpadteLpn, Long taskId);

	/**
	 * 根据任务id获取库存
	 * 
	 * @param taskId
	 * @return
	 */
	List<Stock> getStockByTaskId(Long taskId);
}
