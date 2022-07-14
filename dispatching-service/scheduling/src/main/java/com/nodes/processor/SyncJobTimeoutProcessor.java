package com.nodes.processor;

import com.nodes.project.api.service.ProcessorService;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.Resource;

/**
 * 同步JOB超时信息给WMS
 */
@Component
public class SyncJobTimeoutProcessor implements BasicProcessor {

    @Resource
    private ProcessorService processorService;


    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        return processorService.syncJobTimeout(context);
    }
}
