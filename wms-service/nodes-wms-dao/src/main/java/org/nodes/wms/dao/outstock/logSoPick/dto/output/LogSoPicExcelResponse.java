package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 拣货记录日志导出Excel响应类
 **/
@Data
@ColumnWidth(15)
public class LogSoPicExcelResponse implements Serializable {

	private static final long serialVersionUID = -5353512635959427303L;

	/**
	 * 发货单编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("发货单编码")
	private String soBillNo;

	/**
	 * 单据类型
	 */
	@ColumnWidth(15)
	@ExcelProperty("单据类型")
	private String billTypeName;

	/**
	 * 行号
	 */
	@ColumnWidth(15)
	@ExcelProperty("行号")
	private String soLineNo;

	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ColumnWidth(15)
	@ExcelProperty("物品名称")
	private String skuName;

	/**
	 * 计量单位
	 */
	@ColumnWidth(15)
	@ExcelProperty("计量单位")
	private String wsuName;

	/**
	 * 箱号
	 */
	@ColumnWidth(15)
	@ExcelProperty("箱号")
	private String boxCode;

	/**
	 * 序列号
	 */
	@ColumnWidth(15)
	@ExcelProperty("序列号")
	private String snCode;

	/**
	 * 序列号
	 */
	@ColumnWidth(15)
	@ExcelProperty("LPN")
	private String lpnCode;

	/**
	 * 拣货量
	 */
	@ColumnWidth(15)
	@ExcelProperty("拣货量")
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal pickRealQty;

	/**
	 * 拣货人
	 */
	@ColumnWidth(15)
	@ExcelProperty("拣货人")
	private String createUserName;

	/**
	 * 拣货时间
	 */
	@ColumnWidth(20)
	@ExcelProperty("拣货时间")
	private Date createTime;

	/**
	 * 生产批次
	 */
	@ExcelProperty("生产批次")
	private String skuLot1;

	/**
	 * 规格型号
	 */
	@ExcelProperty("规格型号")
	private String skuLot2;

	/**
	 * 发货日期
	 */
	@ExcelProperty("发货时间")
	private String skuLot3;

	/**
	 * 专用客户
	 */
	@ExcelProperty("专用客户")
	private String skuLot4;

	/**
	 * 钢背批次
	 */
	@ExcelProperty("钢背批次")
	private String skuLot5;

	/**
	 * 摩擦快批次
	 */
	@ExcelProperty("摩擦块批次")
	private String skuLot6;

	/**
	 * 产品标识代码
	 */
	@ExcelProperty("产品标识代码")
	private String skuLot7;

	/**
	 * 适用速度登记
	 */
	@ExcelProperty("适用速度等级")
	private String skuLot8;
}
