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

	@ColumnWidth(15)
	@ExcelProperty("批属性1")
	private String skuLot1;

	@ColumnWidth(15)
	@ExcelProperty("批属性2")
	private String skuLot2;

	@ColumnWidth(15)
	@ExcelProperty("批属性3")
	private String skuLot3;

	@ColumnWidth(15)
	@ExcelProperty("批属性4")
	private String skuLot4;

	@ColumnWidth(15)
	@ExcelProperty("批属性5")
	private String skuLot5;

	@ColumnWidth(15)
	@ExcelProperty("批属性6")
	private String skuLot6;

	@ColumnWidth(15)
	@ExcelProperty("批属性7")
	private String skuLot7;

	@ColumnWidth(15)
	@ExcelProperty("批属性8")
	private String skuLot8;

	@ColumnWidth(15)
	@ExcelProperty("批属性9")
	private String skuLot9;

	@ColumnWidth(15)
	@ExcelProperty("批属性10")
	private String skuLot10;

	@ColumnWidth(15)
	@ExcelProperty("批属性11")
	private String skuLot11;

	@ColumnWidth(15)
	@ExcelProperty("批属性12")
	private String skuLot12;

	@ColumnWidth(15)
	@ExcelProperty("批属性13")
	private String skuLot13;

	@ColumnWidth(15)
	@ExcelProperty("批属性14")
	private String skuLot14;

	@ColumnWidth(15)
	@ExcelProperty("批属性15")
	private String skuLot15;

	@ColumnWidth(15)
	@ExcelProperty("批属性16")
	private String skuLot16;

	@ColumnWidth(15)
	@ExcelProperty("批属性17")
	private String skuLot17;

	@ColumnWidth(15)
	@ExcelProperty("批属性18")
	private String skuLot18;

	@ColumnWidth(15)
	@ExcelProperty("批属性19")
	private String skuLot19;

	@ColumnWidth(15)
	@ExcelProperty("批属性20")
	private String skuLot20;

	@ColumnWidth(15)
	@ExcelProperty("批属性21")
	private String skuLot21;

	@ColumnWidth(15)
	@ExcelProperty("批属性22")
	private String skuLot22;

	@ColumnWidth(15)
	@ExcelProperty("批属性23")
	private String skuLot23;

	@ColumnWidth(15)
	@ExcelProperty("批属性24")
	private String skuLot24;

	@ColumnWidth(15)
	@ExcelProperty("批属性25")
	private String skuLot25;

	@ColumnWidth(15)
	@ExcelProperty("批属性26")
	private String skuLot26;

	@ColumnWidth(15)
	@ExcelProperty("批属性27")
	private String skuLot27;

	@ColumnWidth(15)
	@ExcelProperty("批属性28")
	private String skuLot28;

	@ColumnWidth(15)
	@ExcelProperty("批属性29")
	private String skuLot29;

	@ColumnWidth(15)
	@ExcelProperty("批属性30")
	private String skuLot30;
}
