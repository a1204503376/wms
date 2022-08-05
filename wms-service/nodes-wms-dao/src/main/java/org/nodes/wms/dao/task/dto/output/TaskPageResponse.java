package org.nodes.wms.dao.task.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.esayExcel.EnumConverter;
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
	@ColumnWidth(15)
	@ExcelProperty("任务ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long taskId;
	/**
	 * 单据编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("单据编码")
	private String billNo;
	/**
	 * 任务类型
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "任务类型", converter = EnumConverter.class)
	private WmsTaskTypeEnum taskTypeCd;

	/**
	 * 任务状态
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "任务状态", converter = EnumConverter.class)
	private WmsTaskStateEnum taskState;
	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("单据编码")
	private String skuCode;

	/**
	 * 数量
	 */
	@ColumnWidth(15)
	@ExcelProperty("数量")
	private BigDecimal taskQty;

	/**
	 * 实际数量
	 */
	@ColumnWidth(15)
	@ExcelProperty("实际数量")
	private BigDecimal scanQty;
	/**
	 * 计量单位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("计量单位")
	private String umCode;

	/**
	 * 批次号
	 */
	@ColumnWidth(15)
	@ExcelProperty("批次号")
	private String lot;
	/**
	 * 来源库位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("来源库位")
	private String fromLocCode;

	/**
	 * 目标库位code
	 */
	@ColumnWidth(15)
	@ExcelProperty("目标库位")
	private String toLocCode;

	/**
	 * 箱码
	 */
	@ColumnWidth(15)
	@ExcelProperty("箱码")
	private String boxCode;

	/**
	 * LPN编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("LPN")
	private String lpnCode;
	/**
	 * 任务分派时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("任务下发时间")
	private LocalDateTime allotTime;
}
