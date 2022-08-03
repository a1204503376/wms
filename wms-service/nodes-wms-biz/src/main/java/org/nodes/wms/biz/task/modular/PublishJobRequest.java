package org.nodes.wms.biz.task.modular;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 发送任务到调度系统请求类
 **/
@Data
public class PublishJobRequest {
	private Long wmsBillId;
	private String wmsBillNo;
	private String wmsBillType;
	private Long wmsDetailId;
	private String wmsLineNo;
	private String wmsSkuCode;
	private String wmsSkuName;
	private BigDecimal wmsQty;
	private String wmsUmCode;
	private String wmsUmName;
	private Integer jobType;
	private String locationNameFrom;
	private String locationNameTo;
}
