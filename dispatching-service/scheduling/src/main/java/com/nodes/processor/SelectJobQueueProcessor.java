package com.nodes.processor;

import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * 获取JOB队列
 */
@Component
public class SelectJobQueueProcessor implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {

        // 前置条件：
        // 1.硬件资源是否可用
        // 2.出库：出库接驳去的库位资源是否可用

        // 获取JOB
        // 1.按照【优先级】，【创建时间】升序
        // 2.条件：【】

        // 推送到工作流的上下文
        // 更新JOB状态：工作流队列

//        OmsLogger logger = context.getOmsLogger();
//        logger.info("current:" + context.getJobParams());
//        System.out.println("jobParams: " + context.getJobParams());
//        System.out.println("currentContext:"+ JSON.toJSONString(context));
//
//        // 尝试获取上游任务
//        Map<String, String> workflowContext = context.getWorkflowContext().fetchWorkflowContext();
//        System.out.println("工作流上下文数据：");
//        System.out.println(workflowContext);
//
        return new ProcessResult(true, context.getJobId() + " process successfully.");
    }
}
