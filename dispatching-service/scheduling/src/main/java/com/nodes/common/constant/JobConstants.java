package com.nodes.common.constant;

/**
 * 工作流中JOB相关常量
 */
public class JobConstants {

    /**
     * 调度JOB默认优先级
     */
    public static final Integer DEFAULT_PRIORITY = 100;

    /**
     * 继续执行JOB优先级
     */
    public static final Integer CONTINUE_JOB_PRIORITY = 10;

    /**
     * 工作流上下文参数key
     */
    public static final String WORKFLOW_JOB_QUEUE_KEY = "jobQueueKey";

    /**
     * 传送给AGV的订单属性key
     */
    public static final String AGV_PROPERTY_JOB_ID = "jobId";
}
