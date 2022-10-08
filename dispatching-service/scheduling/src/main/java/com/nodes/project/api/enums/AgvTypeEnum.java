package com.nodes.project.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.nodes.common.enums.IPairs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AGV通知类型
 */
@Getter
@RequiredArgsConstructor
public enum AgvTypeEnum implements IPairs<Integer, String, AgvTypeEnum> {

    /**
     * 开始执行
     */
    BEGIN(1, "开始执行"),
    /**
     * 结束执行
     */
    END(2, "结束执行"),
    /**
     * 异常中断
     */
    EXCEPTION(3, "异常中断"),
    /**
     * 人工终止
     */
    MANUAL_TERMINATION(31, "人工终止"),
    /**
     * 双重入库
     */
    DOUBLE_WAREHOUSING(40, "双重入库"),
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public AgvTypeEnum get() {
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
}
