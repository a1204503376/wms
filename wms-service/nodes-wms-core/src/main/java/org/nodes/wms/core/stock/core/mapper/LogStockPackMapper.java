package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.LogStockPack;
import org.springframework.context.annotation.Primary;

/**
 * 尾箱打包日志表 Mapper 接口
 *
 * @author NodeX
 * @since 2020-07-13
 */
@Primary
public interface LogStockPackMapper extends BaseMapper<LogStockPack> {

}
