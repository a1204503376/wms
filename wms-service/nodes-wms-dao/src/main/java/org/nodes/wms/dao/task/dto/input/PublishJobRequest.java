package org.nodes.wms.dao.task.dto.input;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 发送任务到调度系统请求类
 **/
@Data
public class PublishJobRequest {
	private Long wmsTaskId;
	private String wmsBillNo;
	private String wmsBillType;
	private Long wmsTaskDetailId;
	private String wmsLineNo;
	private String wmsSkuCode;
	private String wmsSkuName;
	private BigDecimal wmsQty;
	private String wmsUmCode;
	private String wmsUmName;
	private Integer jobTypeCode;
	private String locationNameFrom;
	private String locationNameTo;
	private String boxCode;
	/**
	 * 用于区分C1,C2箱码的标志
	 * 1:C1
	 * 2:C2
	 */
	private Integer cBifurcate;
}
