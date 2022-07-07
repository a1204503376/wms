package org.nodes.wms.dao.basics.location.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.output.LocationDetailResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationExcelResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LocationRepository")
public interface LocationMapper extends BaseMapper<Location> {
	List<LocationSelectResponse> listTop10ByCode(String code);

	/**
	 * 分页
	 *
	 * @param page 分页参数
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
}
