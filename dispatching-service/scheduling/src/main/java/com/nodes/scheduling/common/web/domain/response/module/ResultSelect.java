package com.nodes.scheduling.common.web.domain.response.module;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 前端下拉树
 */
@Data
public class ResultSelect implements Serializable {

    /**
     * 数据值字段名称
     */
    private String v;

    /**
     * 数据标题字段名称
     */
    private String n;

    /**
     * 子集数据字段名称
     */
    private List<ResultSelect> s;

    /**
     * 构造方法
     */
    public ResultSelect() {
    }

    /**
     * 构造方法
     */
    public ResultSelect(String v, String n) {
        this.v = v;
        this.n = n;
    }
}
