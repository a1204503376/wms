package org.nodes.wms.dao.task.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工作任务返回前端分页dto
 */
@Data
public class TaskPageResponse {
	/**
	 * 任务ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long taskId;
	/**
	 * 单据编码
	 */
	private String billNo;
	/**
	 * 任务类型
	 */
	private WmsTaskTypeEnum taskTypeCd;
	/**
	 * 任务状态
	 */
	private WmsTaskStateEnum taskState;
	/**
	 * 来源库位编码
	 */
	private String fromLocCode;
	/**
	 * 目标库位code
	 */
	private String toLocCode;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 数量
	 */
	private BigDecimal taskQty;
	/**
	 * 实际数量
	 */
	private BigDecimal scanQty;
	/**
	 * 计量单位编码
	 */
	private String umCode;
	/**
	 * 批次号
	 */
	private String lot;
	/**
	 * LPN编码
	 */
	private String lpnCode;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 任务分派时间
	 */
	private LocalDateTime allotTime;
	/**
	 * AGV接收时间
	 */
	private LocalDateTime confirmDate;
	/**
	 * 任务开始执行的时间
	 */
	private LocalDateTime beginTime;
	/**
	 * 任务结束执行时间
	 */
	private LocalDateTime closeTime;
	/**
	 * AGV指派时间
	 */
	private String udf1;
}
