package org.nodes.wms.dao.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.application.dto.output.WarehouseResponse;

import java.util.List;

public interface WarehousesMapper extends BaseMapper {
	List<WarehouseResponse> getWarehouseSelectResponseList();
}
