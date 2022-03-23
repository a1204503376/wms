package org.nodes.wms.core.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.strategy.entity.OutstockLog;
import org.springframework.context.annotation.Primary;

/**
 * 分配记录 Mapper 接口
 *
 * @author pengwei
 * @since 2020-05-06
 */
@Primary
public interface OutstockLogMapper extends BaseMapper<OutstockLog> {
}
