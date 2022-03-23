
package org.nodes.wms.core.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.strategy.entity.OutstockConfig;
import org.springframework.context.annotation.Primary;

/**
 * 分配策略执行条件 Mapper 接口
 *
 * @author pengwei
 * @since 2020-02-12
 */
@Primary
public interface OutstockConfigMapper extends BaseMapper<OutstockConfig> {
}
