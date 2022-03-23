
package org.nodes.wms.core.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.strategy.entity.InstockConfig;
import org.springframework.context.annotation.Primary;

/**
 * 上架策略执行条件 Mapper 接口
 *
 * @author NodeX
 * @since 2020-02-14
 */
@Primary
public interface InstockConfigMapper extends BaseMapper<InstockConfig> {

}
