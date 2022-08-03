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
	 * LPN编码
	 */
	private String lpnCode;
	/**
	 * 任务分派时间
	 */
	private LocalDateTime allotTime;
}
