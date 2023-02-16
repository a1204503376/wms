package com.nodes.processor;

import com.nodes.project.api.service.CallAgvService;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.Resource;

/**
 * 发邮件 处理器
 */
@Component
public class CallEmailProcessor implements BasicProcessor {

    @Resource
    private CallAgvService callAgvService;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        return callAgvService.agvVehicles();
    }
}
