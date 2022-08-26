package com.nodes.project.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nodes.framework.web.domain.SimpleEntity;
import com.nodes.project.api.enums.JobFlagSyncWmsEnum;
import com.nodes.project.api.enums.JobStatusEnum;
import com.nodes.project.api.enums.JobTypeEnum;
import com.nodes.project.api.enums.WmsBoxTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * job_timeout
 */
@TableName(value = "job_timeout")
@Data
public class JobTimeout extends SimpleEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * WMS任务ID
     */
    private Long wmsTaskId;

    /**
     * WMS任务明细ID不能为空
     */
    private Long wmsTaskDetailId;

    /**
     * WMS系统单号
     */
    private String wmsBillNo;

    /**
     * WMS单据类型
     */
    private String wmsBillType;

    /**
     * WMS行号
     */
    private String wmsLineNo;

    /**
     * WMS物料编码
     */
    private String wmsSkuCode;

    /**
     * WMS物料名称
     */
    private String wmsSkuName;

    /**
     * WMS数量
     */
    private BigDecimal wmsQty;

    /**
     * WMS计量单位编码
     */
    private String wmsUmCode;

    /**
     * WMS计量单位名称
     */
    private String wmsUmName;

    /**
     * wms箱子类型
     */
    private WmsBoxTypeEnum wmsBoxType;

    /**
     * 名称
     */
    private String name;

    /**
     * 优先级：数字越小优先级越高
     */
    private Integer priority;

    /**
     * 类型编码
     */
    private JobTypeEnum typeCode;

    /**
     * 状态
     */
    private JobStatusEnum status;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 位置起点(库位)
     */
    private String locationNameFrom;

    /**
     * 位置终点(库位)
     */
    private String locationNameTo;

    /**
     * 同步给WMS标志
     */
    private JobFlagSyncWmsEnum flagSyncWms;

    /**
     * 同步消息
     */
    private String syncMsg;

}