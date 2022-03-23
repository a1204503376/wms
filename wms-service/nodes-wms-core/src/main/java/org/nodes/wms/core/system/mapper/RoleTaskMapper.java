package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.system.entity.RoleTask;
import org.springframework.context.annotation.Primary;

/**
 * 任务角色关联表 Mapper 接口
 *
 * @author NodeX
 * @since 2020-06-08
 */
@Primary
public interface RoleTaskMapper extends BaseMapper<RoleTask> {


}
