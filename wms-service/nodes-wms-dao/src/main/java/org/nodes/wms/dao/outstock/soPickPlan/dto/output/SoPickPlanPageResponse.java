package org.nodes.wms.dao.outstock.soPickPlan.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分配记录返回前端分页对象
 */
@Data
public class SoPickPlanPageResponse implements Serializable {

	private static final long serialVersionUID = 1827763470592494866L;
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
	 * 箱号
	 */
	@ColumnWidth(15)
	@ExcelProperty("箱号")
	private String boxCode;

	/**
	 * 拣货量
	 */
	@ColumnWidth(15)
	@ExcelProperty("拣货量")
	private BigDecimal pickRealQty;

	/**
	 * lpn编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("LPN编码")
	private String lpnCode;

	/**
	 * 分配时间
	 */
	@ColumnWidth(15)
	@ExcelProperty("拣货时间")
	private Date createTime;

	/**
	 * 分配人
	 */
	@ColumnWidth(15)
	@ExcelProperty("分配人")
	private String createUserName;
	/**
	 * 生产批次
	 */
	@ColumnWidth(15)
	@ExcelProperty("生产批次")
	private String skuLot1;
	/**
	 * 规格型号
	 */
	@ColumnWidth(15)
	@ExcelProperty("规格型号")
	private String skuLot2;
	/**
	 * 发货日期
	 */
	@ColumnWidth(15)
	@ExcelProperty("发货日期")
	private String skuLot3;
	/**
	 * 专用客户
	 */
	@ColumnWidth(15)
	@ExcelProperty("专用客户")
	private String skuLot4;
	/**
	 * 钢背批次
	 */
	@ColumnWidth(15)
	@ExcelProperty("钢背批次")
	private String skuLot5;
	/**
	 * 摩擦块批次
	 */
	@ColumnWidth(15)
	@ExcelProperty("摩擦块批次")
	private String skuLot6;
	/**
	 * 产品标识代码
	 */
	@ColumnWidth(15)
	@ExcelProperty("产品标识代码")
	private String skuLot7;
	/**
	 * 是否CRCC验证
	 */
	@ColumnWidth(15)
	@ExcelProperty("是否CRCC验证")
	private String skuLot8;
}
