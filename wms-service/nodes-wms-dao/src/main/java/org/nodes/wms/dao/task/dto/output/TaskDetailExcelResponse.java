package org.nodes.wms.dao.task.dto.output;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.nodes.wms.dao.task.enums.ProcTypeEnum;
import org.nodes.wms.dao.task.enums.TaskDetailStatusEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分页返回
 */
@Data
@ExcelIgnoreUnannotated
public class TaskDetailExcelResponse implements Serializable {

	private static final long serialVersionUID = -488493501493432542L;
	/**
	 * 任务类别(1上架2拣货3盘点)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"任务类别"})
	private String typeCdValue;
	private WmsTaskTypeEnum typeCd;
	/**
	 * 任务执行方式
	 */
	@ColumnWidth(15)
	@ExcelProperty({"任务执行方式"})
	private String procTypeValue;
	private ProcTypeEnum procType;
	/**
	 * 任务状态(0=正常1=关闭2=已取消3=任务异常)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"任务状态"})
	private String taskStateValue;
	private WmsTaskStateEnum taskState;
	/**
	 * 任务明细状态
	 */
	@ColumnWidth(15)
	@ExcelProperty({"任务明细状态"})
	private String taskDetailStatusValue;
	private TaskDetailStatusEnum taskDetailStatus;
	/**
	 * 单据编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"单据编码"})
	private String billNo;
	/**
	 * 单据行号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"单据行号"})
	private String billLineNo;
	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"物品编码"})
	private String skuCode;
	/**
	 * 层级
	 */
	@ColumnWidth(15)
	@ExcelProperty({"层级"})
	private Integer skuLevel;
	/**
	 * 计量单位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"计量单位编码"})
	private String umCode;
	/**
	 * 计量单位名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"计量单位名称"})
	private String umName;
	/**
	 * 计划数量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"计划数量"})
	private BigDecimal planQty;
	/**
	 * 实际任务执行数量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"实际任务执行数量"})
	private BigDecimal scanQty;
	/**
	 * 库存状态(0正常,1冻结)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存状态"})
	private String stockStatusValue;
	private StockStatusEnum stockStatus;
	/**
	 * 目标库位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"目标库位编码"})
	private String targetLocCode;
	/**
	 * 托盘号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"托盘号"})
	private String lpnCode;
	/**
	 * 箱号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"箱号"})
	private String boxCode;
	/**
	 * 序列号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"序列号"})
	private String snCode;
	/**
	 * 任务来源
	 */
	@ColumnWidth(15)
	@ExcelProperty({"任务来源"})
	private String source;
	/**
	 * 任务执行者code
	 */
	@ColumnWidth(15)
	@ExcelProperty({"任务执行者编码"})
	private String executorUserCode;
	@ColumnWidth(15)
	@ExcelProperty({"货主名称"})
	private String ownerName;
	@ColumnWidth(15)
	@ExcelProperty({"库房名称"})
	private String whName;
	/**
	 * 任务执行开始时间
	 */
	@ColumnWidth(15)
	@ExcelProperty({"任务执行开始时间"})
	private Date beginTime;
	/**
	 * 备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"备注"})
	private String remark;

	/**
	 * 生产批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"生产批次"})
	private String skuLot1;
	/**
	 * 规格型号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"规格型号"})
	private String skuLot2;
	/**
	 * 收货日期
	 */
	@ColumnWidth(15)
	@ExcelProperty({"收货日期"})
	private String skuLot3;
	/**
	 * 专用客户
	 */
	@ColumnWidth(15)
	@ExcelProperty({"专用客户"})
	private String skuLot4;
	/**
	 * 钢背批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"钢背批次"})
	private String skuLot5;
	/**
	 * 摩擦块批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"摩擦块批次"})
	private String skuLot6;
	/**
	 * 产品标识代码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"产品标识代码"})
	private String skuLot7;
	/**
	 * CRCC
	 */
	@ColumnWidth(15)
	@ExcelProperty({"CRCC"})
	private String skuLot8;
}
