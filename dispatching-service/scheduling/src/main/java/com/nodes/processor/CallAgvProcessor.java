package com.nodes.processor;

import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * 呼叫AGV
 */
public class CallAgvProcessor implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {

        // 1.查询上下文中的JOB实例
        // 2.组装相关参数呼叫AGV API
        // 2.1更新JOB的状态，默认成功状态
        // 3.呼叫AGV失败：记录到失败日志表,记录失败状态到JOB
        // 4.呼叫AGV成功：更新JOB相关状态


        return null;
    }
}
