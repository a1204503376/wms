package org.nodes.wms.dao.basics.warehouse.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * abd分类枚举
 **/
@Getter
@RequiredArgsConstructor
public enum AbcEnum {
    /**
     * 周转较快货品
     */
    TURNOVER_FAST_GOODS(10,"周转较快货品"),
    /**
     * 普通搬运工
     */
    GENERAL_PORTER(20,"普通搬运工"),
    /**
     * 周转较慢货品
     */
    TURNOVER_SLOW_GOODS(30,"周转较慢活泼"),
    ;

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;
}
