package org.nodes.wms.core.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.strategy.entity.RelenishmentLog;
import org.springframework.context.annotation.Primary;

/**
 * 补货记录 Mapper 接口
 *
 * @author NodeX
 * @since 2020-04-27
 */
@Primary
public interface RelenishmentLogMapper extends BaseMapper<RelenishmentLog> {

}
