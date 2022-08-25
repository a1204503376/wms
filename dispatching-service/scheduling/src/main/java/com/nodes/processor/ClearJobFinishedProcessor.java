package com.nodes.processor;

import com.nodes.project.api.service.JobQueueService;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.Resource;

/**
 * 清理已结束的任务到历史表
 */
@Component
public class ClearJobFinishedProcessor implements BasicProcessor {

    @Resource
    private JobQueueService jobQueueService;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        jobQueueService.batchDeleteJobCopyToHistory();
        return ProcessResultUtils.success("成功移动");
    }
}
