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
public enum JobTypeEnum implements IPairs<String, String, JobTypeEnum> {
    /**
     * 入库
     */
    STORAGE("101", "入库"),
    /**
     * 出库
     */
    OUTBOUND("102", "出库"),
    /**
     * 库内D箱 补货
     */
    REPLENISHMENT_D_BOXES_IN_WAREHOUSE("301", "库内D箱 补货"),
    /**
     * 库内盘点
     */
    IN_LIBRARY_INVENTORY("302", "库内盘点"),
    /**
     * 库内移位
     */
    IN_LIBRARY_SHIFT("303", "库内移位"),
    ;

    @EnumValue
    private final String code;

    private final String desc;

    @Override
    public JobTypeEnum get() {
        return this;
    }

    @Override
    public String key() {
        return code;
    }

    @Override
    public String value() {
        return desc;
    }

    public static JobTypeEnum getByKey(String key) {
        for (JobTypeEnum value : values()) {
            if (value.key().equals(key)) {
                return value;
            }
        }
        throw new IllegalArgumentException("枚举KEY非法");
    }
}
