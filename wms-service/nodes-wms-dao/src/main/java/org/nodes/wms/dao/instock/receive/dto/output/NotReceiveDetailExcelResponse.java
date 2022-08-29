package org.nodes.wms.dao.instock.receive.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 未收货明细导出Excel类
 **/
@Data
@ColumnWidth(15)
public class NotReceiveDetailExcelResponse implements Serializable {

	private static final long serialVersionUID = 6830929237147310867L;

	/**
	 * 收货单编码
	 */
	@ExcelProperty("收货单编码")
	private String receiveNo;

	/**
	 * 单据类型
	 */
	@ExcelProperty("单据类型")
	private String billTypeName;

	/**
	 * 上游编码
	 */
	@ExcelProperty("上游编码")
	private String externalOrderNo;

	/**
	 * 行号
	 */
	@ExcelProperty("行号")
	private String lineNo;

	/**
	 * 物品编码
	 */
	@ExcelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ExcelProperty("物品名称")
	private String skuName;

	/**
	 * 计划数量
	 */
	@ExcelProperty("计划数量")
	private BigDecimal planQty;

	/**
	 * 实收数量
	 */
	@ExcelProperty("实收数量")
	private BigDecimal scanQty;

	/**
	 * 剩余数量
	 */
	@ExcelProperty("剩余数量")
	private BigDecimal surplusQty;

	/**
	 * 计量单位
	 */
	@ExcelProperty("计量单位")
	private String umName;

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
	 * 收货日期
	 */
	@ExcelProperty("收货日期")
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
	 * 摩擦块批次
	 */
	@ExcelProperty("摩擦块批次")
	private String skuLot6;

	/**
	 * 产品标识代码
	 */
	@ExcelProperty("产品标识代码")
	private String skuLot7;

	/**
	 * 适用速度等级
	 */
	@ExcelProperty("适用速度等级")
	private String skuLot8;

	/**
	 * 创建人
	 */
	@ExcelProperty("创建人")
	private String createUser;

	/**
	 * 创建时间
	 */
	@ExcelProperty("创建时间")
	private Date createTime;
}
