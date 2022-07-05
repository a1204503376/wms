package com.nodes.project.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.nodes.common.enums.IPairs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JOB 类型
 */
@Getter
@RequiredArgsConstructor
public enum JobTypeEnum implements IPairs<Integer, String, JobTypeEnum> {
    /**
     * 入库
     */
    STORAGE(1, "入库"),
    /**
     * 库内
     */
    INSIDE_LIBRARY(2, "库内"),
    /**
     * 出库
     */
    OUTBOUND(3, "出库"),
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public JobTypeEnum get() {
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

    public static JobTypeEnum getByKey(Integer key) {
        for (JobTypeEnum value : values()) {
            if (value.key().equals(key)) {
                return value;
            }
        }
        throw new IllegalArgumentException("枚举KEY非法");
    }
}
