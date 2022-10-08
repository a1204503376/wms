package com.nodes.project.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nodes.framework.web.domain.SimpleEntity;
import com.nodes.project.api.enums.JobStatusEnum;
import com.nodes.project.api.enums.JobTypeEnum;
import com.nodes.project.api.enums.WmsBoxTypeEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业队列
 * 存放待处理的作业，处理完成移动到作业历史表
 */
@TableName(value = "job_queue")
@Data
@XmlRootElement
public class JobQueue extends SimpleEntity implements Serializable {

    private static final long serialVersionUID = -8959443772258439794L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * WMS任务ID
     */
    private Long wmsTaskId;

    /**
     * WMS任务明细ID
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
     * 用于区分C1,C2箱码的标志
     * 1:C1
     * 2:C2
     */
    private Integer wmsCBifurcate;

    /**
     * 名称
     */
    private String name;

    /**
     * 优先级
     * 数字越小优先级越高
     */
    private Integer priority;

    /**
     * 类型
     */
    private JobTypeEnum typeCode;

    /**
     * 状态
     */
    private JobStatusEnum status;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 位置起点(库位)
     */
    private String locationNameFrom;

    /**
     * 位置终点(库位)
     */
    private String locationNameTo;

}