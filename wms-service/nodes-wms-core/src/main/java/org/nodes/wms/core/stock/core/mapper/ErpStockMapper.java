package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.ErpStock;
import org.springframework.context.annotation.Primary;

/**
 * Erp库存 Mapper 接口
 *
 * @author pengwei
 * @since 2019-03-31
 */
@Primary
public interface ErpStockMapper extends BaseMapper<ErpStock> {
}
