
package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.TaskPackage;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 工作任务包 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Primary
public interface TaskPackageMapper extends BaseMapper<TaskPackage> {

}
