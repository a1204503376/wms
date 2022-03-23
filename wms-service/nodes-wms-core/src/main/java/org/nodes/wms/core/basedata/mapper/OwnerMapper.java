package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.Owner;
import org.springframework.context.annotation.Primary;

/**
 *  Mapper 接口
 *
 * @author NodeX
 * @since 2019-12-05
 */
@Primary
public interface OwnerMapper extends BaseMapper<Owner> {
}
