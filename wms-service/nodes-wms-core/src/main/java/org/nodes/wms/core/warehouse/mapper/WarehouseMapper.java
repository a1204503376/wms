package org.nodes.wms.core.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springframework.context.annotation.Primary;

/**
 * 仓库 Mapper 接口
 *
 * @author Wangjw
 * @since 2019-12-06
 */
@Primary
public interface WarehouseMapper extends BaseMapper<Warehouse> {
}
