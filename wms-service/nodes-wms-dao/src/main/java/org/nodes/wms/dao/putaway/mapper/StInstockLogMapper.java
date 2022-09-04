package org.nodes.wms.dao.putaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.putaway.entities.StInstockLog;
import org.springframework.context.annotation.Primary;

/**
 * 上架记录 Mapper 接口
 *
 * @author NodeX
 * @since 2020-04-27
 */
@Primary
public interface StInstockLogMapper extends BaseMapper<StInstockLog> {

}
