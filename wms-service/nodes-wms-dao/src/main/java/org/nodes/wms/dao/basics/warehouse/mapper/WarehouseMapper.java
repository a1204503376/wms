package org.nodes.wms.dao.basics.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("WarehouseRepository")
public interface WarehouseMapper extends BaseMapper<Warehouse> {
	List<WarehouseResponse> getWarehouseSelectResponseList();
}
