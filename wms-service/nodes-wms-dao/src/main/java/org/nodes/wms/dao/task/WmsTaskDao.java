package org.nodes.wms.dao.task;

import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.mp.base.BaseService;

/**
 * @author nodesc
 */
public interface WmsTaskDao extends BaseService<WmsTask> {

    /**
     * 修改任务状态
     * 
     * @param taskId 任务id
     * @param state  状态
     */
    void updateState(Long taskId, WmsTaskStateEnum state);
}
