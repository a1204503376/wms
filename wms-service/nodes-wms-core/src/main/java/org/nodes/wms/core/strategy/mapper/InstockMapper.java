
package org.nodes.wms.core.strategy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.strategy.entity.Instock;
import org.springframework.context.annotation.Primary;

/**
 * 上架策略 Mapper 接口
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Primary
public interface InstockMapper extends BaseMapper<Instock> {

}
