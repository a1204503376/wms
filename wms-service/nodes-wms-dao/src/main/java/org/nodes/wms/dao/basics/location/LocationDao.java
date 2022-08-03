package org.nodes.wms.dao.basics.location;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.output.LocationDetailResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationExcelResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;

import java.util.List;

/**
 * 库位管理 Dao接口
 */
public interface LocationDao {
	List<LocationSelectResponse> listTop10ByCode(String code);

	/**
	 * 导入
	 *
	 * @param locationList: 库位数据集合
	 * @return true: 导入成功，false: 导入失败
	 */
	boolean importData(List<Location> locationList);

	/**
	 * 根据库位编码
	 *
	 * @param locCode:
	 * @return true: 存在 ，false: 不存在
	 */
	boolean isExistLocationCode(String locCode);

	/**
	 * 分页
	 *
	 * @param page               分页参数
	 * @param locationPageQuery: 分页查询条件
	 * @return Page<LocationPageResponse>
	 */
	Page<LocationPageResponse> selectPage(IPage<?> page, LocationPageQuery locationPageQuery);

	/**
	 * 根据若干条件查询库位信息
	 *
	 * @param locationPageQuery: 条件
	 * @return List<Location>
	 */
	List<LocationExcelResponse> selectListByQuery(LocationPageQuery locationPageQuery);

	/**
	 * 保存
	 *
	 * @param location: 库位对象
	 */
	void saveOrUpdateLocation(Location location);

	/**
	 * 根据id查找库位信息
	 *
	 * @param id: 库位id
	 * @return Location
	 */
	Location getLocationById(Long id);

	/**
	 * 根据库位id查找库位详情信息
	 *
	 * @param id: 库位id
	 * @return LocationDetailResponse
	 */
	LocationDetailResponse getDetailById(Long id);

	/**
	 * 根据id集合删除库位信息
	 *
	 * @param idList: 库位id集合
	 * @return true: 删除成功, false: 删除失败
	 */
	boolean removeByIdList(List<Long> idList);

	/**
	 * 获取库位总数量
	 *
	 * @return int
	 */
	int countAll();

	/**
	 * 根据库位编码获取库位
	 *
	 * @param locCode 库位编码
	 * @return 库位信息
	 */
	Location getLocationByLocCode(Long whId, String locCode);

	/**
	 * 根据库位编码获取库位信息
	 *
	 * @param locCode 库位编码
	 * @return List<Location>
	 */
	List<Location> findLocation(List<String> locCode);

	/**
	 * 根据库房id查询库位
	 *
	 * @param whId: 库房id
	 * @return List<Location>
	 */
	List<Location> getLocationByWhId(Long whId);

	/**
	 * 根据容器id获取agv可用的库位，按照上架顺序返回
	 *
	 * @param lpnTypeId 容器id
	 * @param zoneType  库区类型
	 * @return List<Location>
	 */
	List<Location> getLocationByLpnTypeId(Long lpnTypeId, String zoneType);

	/**
	 * 根据箱型和库房id获取库位信息VV
	 *
	 * @param request 包含库房id和箱型
	 * @return 库位信息
	 */
	List<Location> getLocationByLpnType(LpnTypeRequest request);

	/**
	 * 根据当前货架列获取同一列的库位
	 *
	 * @param locColumn 货架列
	 * @return 同一列的库位
	 */
	List<Location> getLocationByLocColumn(String locColumn);

	/**
	 * 查找库位列表中指定类型的库位
	 *
	 * @param locIdList
	 * @param zoneType
	 * @return
	 */
	List<Location> getLocationByZoneType(List<Long> locIdList, Integer zoneType);

	/**
	 * 根据库区ID获取该区域的库位
	 *
	 * @param zoneId 库区ID
	 * @return 库位信息
	 */
	List<Location> getLocationByZoneId(Long zoneId);

	/**
	 * 根据库位id更新库位状态
	 *
	 * @param locId   库位id，必填
	 * @param locFlag 库位使用标记，必填
	 * @param taskId  占用的任务id
	 */
	void updateLocFlag(Long locId, Integer locFlag, String taskId);

	/**
	 * 根据taskid更新库位状态
	 *
	 * @param taskId             占用的任务id，必填
	 * @param locFlag            库位使用标记，必填
	 * @param isResetLocFlagDesc true:重置库位标记为空
	 */
	void updateLocFlag(String taskId, Integer locFlag, boolean isResetLocFlagDesc);
}
