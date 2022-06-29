package com.nodes.scheduling.common.enums;

/**
 * 记录前后台异常类型
 */
public enum ExceptionEnum {
    /**
     * restful
     */
    CLIENT(1, "客户端访问异常"),
    /**
     * jsp
     */
    MANAGE(2, "后台管理端访问异常");

    private Integer code;
    private String msg;

    private ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
