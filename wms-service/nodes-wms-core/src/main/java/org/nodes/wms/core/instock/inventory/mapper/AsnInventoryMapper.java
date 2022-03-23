package org.nodes.wms.core.instock.inventory.mapper;

import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;

/**
 * 收货清单头表 Mapper 接口
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Primary
public interface AsnInventoryMapper extends BaseMapper<AsnInventory> {

}
