package com.nodes.project.api.service;

import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;

/**
 * 特殊处理器 业务
 **/
public interface SpecialProcessorService {

    /**
     * 同步wms任务状态
     *
     * @param context 任务上下文
     * @return 处理器执行结果
     */
    ProcessResult syncWmsTaskState(TaskContext context);
}
