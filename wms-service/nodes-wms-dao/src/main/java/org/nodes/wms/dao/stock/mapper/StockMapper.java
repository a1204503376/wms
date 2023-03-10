package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.count.dto.output.PdaBoxQtyResponse;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.input.StockBySerialPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.FindAllStockByNoResponse;
import org.nodes.wms.dao.stock.dto.output.StockBySerialPageResponse;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.nodes.wms.dao.stock.dto.report.ReportAgeOfInventoryDto;
import org.nodes.wms.dao.stock.dto.report.ReportCountStockDto;
import org.nodes.wms.dao.stock.dto.report.ReportStockDto;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存mapper接口
 **/
@Repository("stockRepository")
public interface StockMapper extends BaseMapper<Stock> {

	/**
	 * 根据库位id查询库存数量
	 *
	 * @param locIdList:
	 * @return BigDecimal
	 */
	HashMap<String, Object> selectStockQtyByLocIdList(List<Long> locIdList);

	/**
	 * 根据库位id查询物品总数
	 *
	 * @param locIdList:
	 * @return Integer
	 */
	Integer selectStockSkuCountByLocIdList(List<Long> locIdList);

	/**
	 * 获取库存分页
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param page    分页条件
	 * @return Pda根据编码查询库存-响应对象
	 */
	Page<FindAllStockByNoResponse> getList(@Param("query") FindAllStockByNoRequest request, IPage<Stock> page);

	/**
	 * 获取分页列表
	 *
	 * @param page           分页参数
	 * @param stockPageQuery 查询参数
	 */
	Page<StockPageResponse> getPage(IPage<StockPageResponse> page, @Param("query") StockPageQuery stockPageQuery);

	/**
	 * 获取库存统计列表
	 *
	 * @param stockPageQuery 查询参数
	 * @return 库存统计列表
	 */
	List<StockPageResponse> getStockCountList(@Param("query") StockPageQuery stockPageQuery);

	/**
	 * 获取导出数据集合
	 *
	 * @param stockPageQuery 查询参数
	 * @return
	 */
	List<StockPageResponse> getStockResponseByQuery(@Param("query") StockPageQuery stockPageQuery);

	/**
	 * 获取库存余额按箱显示信息
	 *
	 * @param stockPageQuery 查询参数
	 * @return 库存信息
	 */
	List<StockPageResponse> getStockResponseByBoxOrByLpn(@Param("query") StockPageQuery stockPageQuery);

	/**
	 * 库存余额按序列号显示获取分页
	 *
	 * @param page                   分页对象
	 * @param stockBySerialPageQuery 查询参数
	 * @return 分页
	 */
	Page<StockBySerialPageResponse> getSerialPage(IPage<?> page, @Param("query") StockBySerialPageQuery stockBySerialPageQuery);

	/**
	 * 库存统计
	 *
	 * @param locCode 库位
	 * @param boxCode 箱码
	 * @return PdaBoxQtyResponse
	 */
	List<PdaBoxQtyResponse> getStockCountByLocCode(String locCode, String boxCode, String skuCode);

	/**
	 * 物理删除无效库存
	 */
	void deleteStockByStockIdList();

	/**
	 * 报表:盘点表,根据库位、型号、产品进行数量汇总
	 *
	 * @param excludeZoneIdList 排除的库区id
	 * @param parameters        搜索条件
	 * @return 盘点报表数据
	 **/
	List<ReportCountStockDto> getStockBalanceTotalByReportParams(@Param("excludeZoneIdList") List<Long> excludeZoneIdList, @Param("params") Map<String, Object> parameters);

	/**
	 * 报表: 实时库存
	 *
	 * @param excludeZoneIdList 排除的库区id
	 * @param parameters        搜索条件
	 * @return 实时库存数据
	 */
	List<ReportStockDto> getCurrentTimeStockByReportParams(@Param("excludeZoneIdList") List<Long> excludeZoneIdList, @Param("params") Map<String, Object> parameters);

	/**
	 * 报表: 库龄
	 *
	 * @param excludeZoneIdList 排除的库区id
	 * @return 库龄数据
	 */
	List<ReportAgeOfInventoryDto> getAgeOfInventory(@Param("excludeZoneIdList") List<Long> excludeZoneIdList);

	List<Stock> selectListByZoneTypeList(@Param("whId") Long whId, @Param("boxCode") String boxCode);


}

