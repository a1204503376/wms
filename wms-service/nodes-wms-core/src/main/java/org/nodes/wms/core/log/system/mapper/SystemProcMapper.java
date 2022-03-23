package org.nodes.wms.core.log.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.springframework.context.annotation.Primary;

/**
 * 系统日志 Mapper 接口
 *
 * @author NodeX
 * @since 2020-02-12
 */
@Primary
public interface SystemProcMapper extends BaseMapper<SystemProc> {

}
