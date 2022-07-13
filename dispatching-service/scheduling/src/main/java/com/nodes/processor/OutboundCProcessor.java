package com.nodes.processor;

import com.nodes.project.api.enums.ProcessorEnum;
import com.nodes.project.api.service.ProcessorService;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.Resource;

/**
 * C箱出库
 */
public class OutboundCProcessor implements BasicProcessor {

    @Resource
    private ProcessorService processorService;


    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        return processorService.selectJobQueue(context, ProcessorEnum.OUTBOUND_C);
    }
}