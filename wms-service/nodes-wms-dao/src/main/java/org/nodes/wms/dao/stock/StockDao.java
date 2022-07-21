package org.nodes.wms.dao.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.FindAllStockByNoResponse;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存Dao接口
 **/
public interface StockDao {

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
	 * @param boxCode
	 * @param excludeLocIdList
	 * @return
	 */
	List<Stock> getStockByBoxCodeExcludeLoc(String boxCode, List<Long> excludeLocIdList);

	/**
	 * 根据boxCode获取指定库存
	 *
	 * @param boxCode   必填，不能为空
	 * @param locIdList 可为空
	 * @return
	 */
	List<Stock> getStockByBoxCode(String boxCode, List<Long> locIdList);

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

}
