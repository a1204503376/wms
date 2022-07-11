package com.nodes.project.api.dto;

import com.nodes.common.enums.EnumValid;
import com.nodes.project.api.enums.JobTypeEnum;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 下发JOB请求对象
 * 针对WMS单据明细
 */
@Data
public class PublishJobRequest {

    /**
     * WMS任务ID
     */
    @NotNull(message = "WMS任务ID不能为空")
    private Long wmsTaskId;

    /**
     * WMS任务明细ID
     */
    @NotNull(message = "WMS任务明细ID不能为空")
    private Long wmsTaskDetailId;

    /**
     * WMS单据编码
     */
    @NotBlank(message = "WMS单据编码不能为空")
    private String wmsBillNo;

    /**
     * WMS单据类型
     */
    @NotBlank(message = "WMS单据类型不能为空")
    private String wmsBillType;

    /**
     * WMS单据行号
     */
    @NotBlank(message = "WMS单据行号不能为空")
    private String wmsLineNo;

    /**
     * WMS物料编码
     */
    @NotBlank(message = "WMS物料编码不能为空")
    private String wmsSkuCode;

    /**
     * WMS物料名称
     */
    @NotBlank(message = "WMS物料名称不能为空")
    private String wmsSkuName;

    /**
     * WMS数量
     */
    @Digits(integer = 10, fraction = 6, message = "整数位上限10位，小数位上限6位")
    @NotNull(message = "WMS数量不能为空")
    private BigDecimal wmsQty;

    /**
     * WMS计量单位编码
     */
    @NotBlank(message = "WMS计量单位编码不能为空")
    private String wmsUmCode;

    /**
     * WMS计量单位名称
     */
    @NotBlank(message = "WMS计量单位名称不能为空")
    private String wmsUmName;

    /**
     * 类型
     */
    @EnumValid(target = JobTypeEnum.class, message = "JOB类型非法")
    @NotNull(message = "JOB类型不能为空")
    private String jobTypeCode;

    /**
     * 位置起点(库位)
     */
    @NotBlank(message = "位置起点(库位)不能为空")
    private String locationNameFrom;

    /**
     * 位置终点(库位)
     */
    @NotBlank(message = "位置终点(库位)不能为空")
    private String locationNameTo;
}
