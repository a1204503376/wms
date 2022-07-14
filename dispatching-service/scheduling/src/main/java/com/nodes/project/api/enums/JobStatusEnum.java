package com.nodes.project.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.google.common.collect.Lists;
import com.nodes.common.enums.IPairs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * JOB 状态
 */
@Getter
@RequiredArgsConstructor
public enum JobStatusEnum implements IPairs<Integer, String, JobStatusEnum> {

    /**
     * 未开始
     */
    NOT_STARTED(1, "未开始"),

    /**
     * 工作流占用
     */
    WORKFLOW_OCCUPY(2, "工作流占用"),

    /**
     * 准备呼叫AGV
     */
    PREPARE_CALL_AGV(3, "准备呼叫AGV"),

    /**
     * AGV返回成功
     */
    AGV_RETURN_SUCCESSFUL(4, "AGV返回成功"),

    /**
     * AGV开始执行
     */
    AGV_BEGIN(5, "AGV开始执行"),

    /**
     * AGV结束执行
     */
    AGV_END(6, "AGV结束执行"),


    /**
     * AGV返回失败
     */
    AGV_RETURN_FAILED(31, "AGV返回失败"),
    /**
     * 呼叫AGV异常
     */
    CALL_AGV_EXCEPTION(32, "呼叫AGV异常"),


    /**
     * AGV异常中断
     */
    AGV_EXCEPTION(51, "AGV异常中断"),
    /**
     * 继续执行
     */
    CONTINUE_EXECUTE(511, "继续执行"),
    /**
     * 异常人工终止
     */
    END_EXCEPTION(512, "异常人工终止"),


    /**
     * 人工取消
     */
    CANCEL(0, "人工取消"),


    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public JobStatusEnum get() {
        return this;
    }

    @Override
    public Integer key() {
        return code;
    }

    @Override
    public String value() {
        return desc;
    }

    public static List<JobStatusEnum> listByAllow() {
        return Lists.newArrayList(
                JobStatusEnum.NOT_STARTED,
                JobStatusEnum.AGV_RETURN_FAILED,
                JobStatusEnum.CALL_AGV_EXCEPTION,
                JobStatusEnum.CONTINUE_EXECUTE
        );
    }
}
