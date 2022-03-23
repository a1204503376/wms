
package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.TaskHistory;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 任务履历 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Primary
public interface TaskHistoryMapper extends BaseMapper<TaskHistory> {

}
