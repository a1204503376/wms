package org.nodes.wms.core.outstock.inventory.mapper;

import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;

/**
 * 发货清单主表
 Mapper 接口
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Primary
public interface SoInventoryMapper extends BaseMapper<SoInventory> {

}
