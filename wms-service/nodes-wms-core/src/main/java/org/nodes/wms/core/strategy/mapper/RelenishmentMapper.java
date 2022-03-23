
package org.nodes.wms.core.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.springframework.context.annotation.Primary;

/**
 * 补货策略 Mapper 接口
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Primary
public interface RelenishmentMapper extends BaseMapper<Relenishment> {

}
