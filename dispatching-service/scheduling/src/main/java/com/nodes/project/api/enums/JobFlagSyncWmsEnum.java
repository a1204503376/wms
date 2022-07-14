package com.nodes.project.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.nodes.common.enums.IPairs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * job超时信息同步给WMS的标识
 */
@Getter
@RequiredArgsConstructor
public enum JobFlagSyncWmsEnum implements IPairs<Integer, String, JobFlagSyncWmsEnum> {

    /**
     * 未同步
     */
    NOT_SYNCHRONIZED(1, "未同步"),
    /**
     * 同步成功
     */
    SYNCHRONIZATION_SUCCESSFUL(2, "同步成功"),
    /**
     * 同步失败
     */
    SYNCHRONIZATION_FAILED(3, "同步失败"),
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public JobFlagSyncWmsEnum get() {
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
