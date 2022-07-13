package com.nodes.processor;

import com.nodes.project.api.enums.ProcessorEnum;
import com.nodes.project.api.service.ProcessorService;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.Resource;

/**
 * 非出库 处理器
 */
@Component
public class NonOutboundProcessor implements BasicProcessor {

    @Resource
    private ProcessorService processorService;


    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        return processorService.selectJobQueue(context, ProcessorEnum.NON_OUTBOUND);
    }
}
