package com.nodes.project.api.service;

import com.nodes.project.api.enums.ProcessorEnum;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;

/**
 * 执行器业务
 */
public interface ProcessorService {

    ProcessResult selectJobQueue(TaskContext context, ProcessorEnum processorEnum);

    ProcessResult validWorkflowData(TaskContext context);

    ProcessResult outboundAllowed(TaskContext context);

    ProcessResult callAgv(TaskContext context);

    ProcessResult syncJobTimeout(TaskContext context);
}
