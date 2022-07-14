package com.nodes.project.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * call_api_data
 */
@TableName(value = "call_api_log")
@Data
public class CallApiLog {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求数据
     */
    private Object requestData;

    /**
     * 返回数据
     */
    private Object responseData;

    /**
     * 异常消息
     */
    private String exceptionMsg;

    /**
     * 创建时间
     */
    private Date createTime;
}