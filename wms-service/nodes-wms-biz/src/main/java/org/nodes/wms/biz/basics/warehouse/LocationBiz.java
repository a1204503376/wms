package org.nodes.wms.biz.basics.warehouse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.location.dto.input.LocationAddOrEditRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.location.dto.output.LocationDetailResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationEditResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.springblade.core.mp.support.Query;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LocationBiz {
	/**
	 * 获取库位下拉列表最近十条数据
	 *
	 * @param locationSelectQuery 前端传传入查询条件
	 * @return 库位下拉列表集合
	 */
	List<LocationSelectResponse> getLocationSelectResponseTop10List(LocationSelectQuery locationSelectQuery);

	/**
	 * 导入
	 *
	 * @param file: excel二进制文件
	 * @return true: 导入成功，false：导入失败
	 */
	boolean importData(MultipartFile file);

	/**
	 * 分页
	 *
	 * @param query              页参数
	 * @param locationPageQuery: 分页查询条件
	 * @return IPage<LocationPageResponse>
	 */
	Page<LocationPageResponse> page(Query query, LocationPageQuery locationPageQuery);

	/**
	 * 导出
	 *
	 * @param locationPageQuery: 条件
	 * @param response:          响应信息
	 */
	void exportExcel(LocationPageQuery locationPageQuery, HttpServletResponse response);

	/**
	 * 新增库位
	 *
	 * @param locationAddOrEditRequest: 新增请求对象
	 * @return Location
	 */
	Location add(LocationAddOrEditRequest locationAddOrEditRequest);

	/**
	 * 根据库位id查找库位信息
	 *
	 * @param locId: 库位id
	 * @return LocationPageResponse
	 */
	LocationEditResponse findLocationById(Long locId);

	Location findByLocId(long locId);

	/**
	 * 编辑
	 *
	 * @param locationAddOrEditRequest: 编辑请求对象
	 * @return Location
	 */
	Location edit(LocationAddOrEditRequest locationAddOrEditRequest);

	/**
	 * 查看详情
	 *
	 * @param locId: 库位id
	 * @return LocationDetailResponse
	 */
	LocationDetailResponse getLocationDetailById(Long locId);

	/**
	 * 批量删除
	 *
	 * @param idList: 库位id集合
	 * @return true: 删除成功, false: 删除失败
	 */
	boolean remove(List<Long> idList);

	/**
	 * 返回所有的库房的入库集货区库位
	 *
	 * @return List<Location>
	 */
	List<Location> getAllStageLocation();

	/**
	 * 返回所有的库房的入库检验区库位
	 *
	 * @return List<Location>
	 */
	List<Location> getAllQcLocation();

	/**
	 * 返回所有的库房的出库暂存区库位
	 *
	 * @return List<Location>
	 */
	List<Location> getAllPickToLocation();

	/**
	 * 返回所有的库房的打包暂存区库位
	 *
	 * @return List<Location>
	 */
	List<Location> getAllPackLocation();

	/**
	 * 返回所有的库房的未知库位
	 *
	 * @return List<Location>
	 */
	List<Location> getAllUnknownLocation();

	/**
	 * 返回所有的库房的在途暂存库位
	 *
	 * @return List<Location>
	 */
	List<Location> getAllInTransitLocation();

	/**
	 * 获取指定库房的入库暂库位
	 *
	 * @param whId
	 * @return
	 */
	Location getStageLocation(Long whId);

	/**
	 * 获取指定库房的质检库位
	 *
	 * @param whId
	 * @return
	 */
	Location getQcLocation(Long whId);

	/**
	 * 获取指定库房的出库暂库位
	 *
	 * @param whId
	 * @return
	 */
	Location getPickToLocation(Long whId);

	/**
	 * 获取指定库房的打包库位
	 *
	 * @param whId
	 * @return
	 */
	Location getPackLocation(Long whId);

	/**
	 * 获取指定库房的未知库位
	 *
	 * @param whId
	 * @return
	 */
	Location getUnknowLocation(Long whId);

	/**
	 * 获取指定库房指定的在途库位
	 *
	 * @param whId
	 * @return
	 */
	Location getInTransitLocation(Long whId);

	/**
	 * 获取库位总数量
	 *
	 * @return int
	 */
	int countAll();

	/**
	 * 根据库位编码获取库位
	 *
	 * @param whId    库房id
	 * @param locCode 库位编码
	 * @return 库位信息
	 */
	Location findLocationByLocCode(Long whId, String locCode);

	/**
	 * 判断库位是否冻结
	 *
	 * @param location
	 * @return true：冻结
	 */
	boolean isFrozen(Location location);

	/**
	 * 判断是否允许混放物品
	 *
	 * @param location
	 * @return true：允许
	 */
	boolean isMixSku(Location location);

	/**
	 * 判断是否允许混放批次
	 *
	 * @param location
	 * @return true：允许
	 */
	boolean isMixSkuLot(Location location);

	/**
	 * 冻结库位
	 *
	 * @param locId
	 * @param occupyFlag 自动任务的时候为任务id
	 */
	void freezeByOccupyFlag(Long locId, String occupyFlag);

	/**
	 * 解冻由freezeByOccupyFlag冻结的库位
	 *
	 * @param locId
	 */
	void unfreezeByOccupyFlag(Long locId);

	/**
	 * 判断库位是否为出库暂存区
	 *
	 * @param location
	 * @return true：是出库暂存区
	 */
	boolean isPickToLocation(Location location);

	/**
	 * 天宜定制：根据容器列别获取agv可用的库位，按照上架顺序返回
	 *
	 * @param lpnType  容器类别
	 * @param zoneType 库区类型
	 * @return
	 */
	List<Location> findEnableAgvLocation(LpnType lpnType, String zoneType);

	/**
	 * 获取同列的所有库位
	 *
	 * @param location
	 * @return
	 */
	List<Location> getLocationByColumn(Location location);

	/**
	 * 根据箱型和库房id获取库位集合
	 *
	 * @param request@return 库位信息
	 */
	List<Location> findLocationByLpnType(LpnTypeRequest request);

	/**
	 * 判断是否存在虚拟库位
	 *
	 * @param locationList
	 * @return true:表示存在虚拟库位
	 */
	boolean isVirtualLocation(List<Location> locationList);

	/**
	 * 根据库区ID获取该区域的库位
	 *
	 * @param zoneId 库区ID
	 * @return 库位信息
	 */
	List<Location> findLocationByZoneId(Long zoneId);
}
