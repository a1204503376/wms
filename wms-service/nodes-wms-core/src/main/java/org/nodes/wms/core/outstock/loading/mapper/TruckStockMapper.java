package org.nodes.wms.core.outstock.loading.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.outstock.loading.entity.TruckStock;
import org.springframework.context.annotation.Primary;

/**
 *  Mapper 接口
 *
 * @author pengwei
 * @since 2020-04-16
 */
@Primary
public interface TruckStockMapper extends BaseMapper<TruckStock> {

}
