package com.nodes.processor;

import com.nodes.project.api.service.SpecialProcessorService;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.Resource;

/**
 * 呼叫WMS 处理器
 **/
@Component
public class CallWmsProcessor implements BasicProcessor {

    @Resource
    private SpecialProcessorService specialProcessorService;

    /**
     * 重发给WMS，同步任务状态
     *
     * @param context 任务上下文
     * @return 处理器执行结果
     */
    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        return specialProcessorService.syncWmsTaskState(context);
    }
}
