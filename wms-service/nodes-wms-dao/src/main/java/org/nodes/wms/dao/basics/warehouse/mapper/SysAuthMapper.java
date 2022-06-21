package org.nodes.wms.dao.basics.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.SysAuth;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface SysAuthMapper extends BaseMapper<SysAuth> {
}
