
package org.nodes.wms.core.allot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.allot.entity.AllotHeader;
import org.springframework.context.annotation.Primary;

/**
 * 调拨单头表 Mapper 接口
 *
 * @author Wangjw
 * @since 2020-01-23
 */
@Primary
public interface AllotHeaderMapper extends BaseMapper<AllotHeader> {
}
