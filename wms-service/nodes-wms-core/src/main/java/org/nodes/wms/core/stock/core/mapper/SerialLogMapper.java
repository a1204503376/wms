package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.SerialLog;
import org.springframework.context.annotation.Primary;

/**
 * 序列号日志 Mapper 接口
 *
 * @author zx
 * @since 2020-02-24
 */
@Primary
public interface SerialLogMapper extends BaseMapper<SerialLog> {

}
