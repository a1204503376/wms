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
import java.util.Date;

/**
 * 工作任务导出Dto类
 */
@Data
public class TaskExcelResponse {
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
	 * 备注
	 */
	@ColumnWidth(50)
	@ExcelProperty("备注")
	private String remark;
	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("物品编码")
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
	 * LPN编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("LPN")
	private String lpnCode;
	/**
	 * 创建时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("创建时间")
	private Date createTime;
	/**
	 * 任务下发时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("任务下发时间")
	private Date allotTime;
	/**
	 * AGV接收时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("AGV接收时间")
	private Date confirmDate;
	/**
	 * AGV指派时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("AGV指派时间")
	private String udf1;
	/**
	 * 任务开始执行的时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("任务开始执行的时间")
	private Date beginTime;
	/**
	 * 任务结束执行时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("任务结束执行时间")
	private Date closeTime;
}
