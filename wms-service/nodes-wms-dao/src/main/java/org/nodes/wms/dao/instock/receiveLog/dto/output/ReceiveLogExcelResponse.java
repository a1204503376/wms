package org.nodes.wms.dao.instock.receiveLog.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收货单清点记录导出excel类
 **/
@Data
public class ReceiveLogExcelResponse implements Serializable {

	private static final long serialVersionUID = -4270898178645936584L;

	/**
	 * 收货单编码
	 */
	@ExcelProperty("收货单编码")
	@ColumnWidth(15)
	private String receiveNo;

	/**
	 * 行号
	 */
	@ExcelProperty("行号")
	@ColumnWidth(15)
	private String lineNo;

	/**
	 * 物品编码
	 */
	@ExcelProperty("物品编码")
	@ColumnWidth(15)
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ExcelProperty("物品名称")
	@ColumnWidth(15)
	private String skuName;

	/**
	 * 收货数量
	 */
	@ExcelProperty("收货数量")
	@ColumnWidth(15)
	private BigDecimal qty;

	/**
	 * 包装单位名称
	 */
	@ExcelProperty("包装单位")
	@ColumnWidth(15)
	private String wsuName;

	/**
	 * 箱号
	 */
	@ExcelProperty("箱号")
	@ColumnWidth(15)
	private String boxCode;

	/**
	 * LPN
	 */
	@ExcelProperty("LPN")
	@ColumnWidth(15)
	private String lpnCode;

	/**
	 * 序列号
	 */
	@ExcelProperty("序列号")
	@ColumnWidth(15)
	private String snCode;

	/**
	 * 库位编码
	 */
	@ExcelProperty("库位编码")
	@ColumnWidth(15)
	private String locCode;

	/**
	 * 库房名称
	 */
	@ExcelProperty("库房")
	@ColumnWidth(15)
	private String whName;

	/**
	 * 货主
	 */
	@ExcelProperty("货主")
	@ColumnWidth(15)
	private String ownerName;

	/**
	 * 生产批次
	 */
	@ExcelProperty("生产批次")
	@ColumnWidth(15)
	private String skuLot1;

	/**
	 * 规格型号
	 */
	@ExcelProperty("规格型号")
	@ColumnWidth(15)
	private String skuLot2;

	/**
	 * 收货日期
	 */
	@ExcelProperty("收货日期")
	@ColumnWidth(15)
	private String skuLot3;

	/**
	 * 专用客户
	 */
	@ExcelProperty("专用客户")
	@ColumnWidth(15)
	private String skuLot4;

	/**
	 * 钢背批次
	 */
	@ExcelProperty("钢背批次")
	@ColumnWidth(15)
	private String skuLot5;

	/**
	 * 摩擦块批次
	 */
	@ExcelProperty("摩擦块批次")
	@ColumnWidth(15)
	private String skuLot6;

	/**
	 * 产品标识代码
	 */
	@ExcelProperty("产品标识代码")
	@ColumnWidth(15)
	private String skuLot7;

	/**
	 * 是否CRCC验证
	 */
	@ExcelProperty("是否CRCC验证")
	@ColumnWidth(15)
	private String skuLot8;

	/**
	 * 收货人
	 */
	@ExcelProperty("收货人")
	@ColumnWidth(15)
	private String createUserName;

	/**
	 * 收货时间
	 */
	@ExcelProperty("收货时间")
	@ColumnWidth(15)
	private Date createTime;
}
