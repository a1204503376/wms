package org.nodes.wms.core.stock.core.mapper;

import org.nodes.wms.core.stock.core.entity.StockSnapshoot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;

/**
 * 库存快照 Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-28
 */
@Primary
public interface StockSnapshootMapper extends BaseMapper<StockSnapshoot> {

}
