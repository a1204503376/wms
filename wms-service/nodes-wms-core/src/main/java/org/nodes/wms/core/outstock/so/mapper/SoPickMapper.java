package org.nodes.wms.core.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.springframework.context.annotation.Primary;

/**
 * 拣货记录日志 Mapper 接口
 *
 * @author zx
 * @since 2020-03-04
 */
@Primary
public interface SoPickMapper extends BaseMapper<SoPick> {


}
