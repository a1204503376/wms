package org.nodes.wms.dao.basics.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.warehouse.entites.Location;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LocationRepository")
public interface LocationMapper extends BaseMapper<Location> {
	List<LocationSelectResponse> listTop10ByCode(String code);
}
