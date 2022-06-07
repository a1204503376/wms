package org.nodes.wms.dao.basics.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageResponse;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.warehouse.entites.Location;
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
}
