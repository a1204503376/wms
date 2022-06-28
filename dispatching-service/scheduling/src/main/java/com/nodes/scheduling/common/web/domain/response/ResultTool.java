package com.nodes.scheduling.common.web.domain.response;

/**
 * JSON返回工具类
 */
public class ResultTool {
    public static JsonResult<Object> success() {
        return new JsonResult<>(true);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(true, data);
    }

    public static JsonResult<Object> fail() {
        return new JsonResult<>(false);
    }

    public static JsonResult<Object> fail(ResultCode resultEnum) {
        return new JsonResult<>(false, resultEnum);
    }
}

