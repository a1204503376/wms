package com.nodes.project.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.nodes.common.enums.IPairs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JOB 状态
 */
@Getter
@RequiredArgsConstructor
public enum JobStatusEnum implements IPairs<Integer, String, JobStatusEnum> {
    /**
     * 未开始
     */
    NOT_STARTED(0, "未开始"),
    /**
     * 已开始
     */
    STARTED(1, "已开始"),
    /**
     * 已结束
     */
    ENDED(2, "已结束"),

    /**
     * 异常终止
     */
    ABNORMAL_TERMINATION(3, "异常终止"),
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

    public static JobStatusEnum getByKey(Integer key) {
        for (JobStatusEnum value : values()) {
            if (value.key().equals(key)) {
                return value;
            }
        }
        throw new IllegalArgumentException("枚举KEY非法");
    }
}
