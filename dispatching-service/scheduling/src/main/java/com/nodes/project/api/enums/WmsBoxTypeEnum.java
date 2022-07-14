package com.nodes.project.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.nodes.common.enums.IPairs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * WMS箱子类型
 */
@Getter
@RequiredArgsConstructor
public enum WmsBoxTypeEnum implements IPairs<String, String, WmsBoxTypeEnum> {

    /**
     * A类箱子
     */
    A("A", "A类箱子"),
    /**
     * B类箱子
     */
    B("B", "B类箱子"),
    /**
     * C类箱子
     */
    C("C", "C类箱子"),
    /**
     * D类箱子
     */
    D("D", "D类箱子"),
    ;

    @EnumValue
    private final String code;

    private final String desc;

    @Override
    public WmsBoxTypeEnum get() {
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
}
