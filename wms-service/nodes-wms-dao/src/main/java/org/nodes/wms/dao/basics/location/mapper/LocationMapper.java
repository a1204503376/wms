package org.nodes.wms.dao.basics.location.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.output.*;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putaway.dto.input.LpnTypeRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LocationRepository")
public interface LocationMapper extends BaseMapper<Location> {
	List<LocationSelectResponse> listTop10ByCode(String code);

	/**
	 * 分页
	 *
	 * @param page               分页参数
	 * @param locationPageQuery: 分页查询条件
	 * @return Page<LocationPageResponse>
	 */
	Page<LocationPageResponse> listByPage(IPage<?> page, @Param("param") LocationPageQuery locationPageQuery);

	/**
	 * 根据条件查询库位信息
	 *
	 * @param locationPageQuery: 条件
	 * @return List<Location>
	 */
	List<LocationExcelResponse> listByQuery(@Param("param") LocationPageQuery locationPageQuery);

	/**
	 * 根据库位id查找库位详情信息
	 *
	 * @param id: 库位id
	 * @return LocationDetailResponse
	 */
	LocationDetailResponse selectDetailById(Long id);

	/**
	 * 根据库房id和箱型获取库位信息
	 *
	 * @param request@return 库位信息
	 */
	List<Location> selectLoctionByLpnType(@Param("param") LpnTypeRequest request);

	/**
	 * 根据库区类型查询查询库位，如果zongType可以为空
	 *
	 * @param lpnTypeId 适用的容器类型
	 * @param zoneType  库区分类
	 * @return 符合条件的库位
	 */
	List<Location> getLocationByLpnTypeIdAndZoneType(Long lpnTypeId, String zoneType);

	/**
	 * 根据库区类型查找相对应的库位
	 *
	 * @param locIdList locIdList
	 * @param zoneType  zoneType
	 * @return 符合条件的库位
	 */
	List<Location> getLocationByZoneType(List<Long> locIdList, Long whId, Integer zoneType);

	/**
	 * 根据库区类型查找相对应的库位
	 *
	 * @param locIdList locIdList
	 * @param zoneType  zoneType
	 * @return 符合条件的库位
	 */
	List<Location> getLocationByZoneTypeAndWhId(List<Long> locIdList, Long whId, Integer zoneType);

	/**
	 * 根据库区类型查找相对应的库位
	 *
	 * @param whId     whId
	 * @param locId    locId
	 * @param zoneType zoneType
	 * @return 符合条件的库位
	 */
	Location getLocationByZoneTypeAndLocId(Long whId, Long locId, Integer zoneType);

	/**
	 * 根据库位id获取所属库区的库区类型
	 *
	 * @param locId 库位id
	 * @return 库区类型
	 */
	Integer selectZoneTypeByLocId(Long locId);

    List<Location3dResponse> select3dLocData();
}
