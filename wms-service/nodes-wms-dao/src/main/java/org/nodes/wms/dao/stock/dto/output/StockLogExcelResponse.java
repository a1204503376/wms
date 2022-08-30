package org.nodes.wms.dao.stock.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.nodes.wms.dao.common.esayExcel.EnumConverter;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存日志导出dto类
 **/
@Data
public class StockLogExcelResponse implements Serializable {

	private static final long serialVersionUID = -5772628298310059589L;

	/**
	 * 库存日志类型
	 */
	@ColumnWidth(15)
	@ExcelProperty("库存日志类型")
	private String logType;

	/**
	 * 来源的单据编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("来源的单据编码")
	private String sourceBillNo;

	/**
	 * 本次操作待上架数量
	 */
	@ColumnWidth(15)
	@ExcelProperty("本次操作待上架数量")
	private BigDecimal currentStayStockQty;

	/**
	 * 本次操作上架数量
	 */
	@ColumnWidth(15)
	@ExcelProperty("本次操作上架数量")
	private BigDecimal currentStockQty;

	/**
	 * 本次操作待下架数量
	 */
	@ColumnWidth(15)
	@ExcelProperty("本次操作待下架数量")
	private BigDecimal currentPickQty;

	/**
	 * 库存状态
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "库存状态", converter = EnumConverter.class)
	private StockStatusEnum stockStatus;

	/**
	 * 层级
	 */
	@ColumnWidth(15)
	@ExcelProperty("层级")
	private String skuLevel;

	/**
	 * 包装名称
	 */
	@ColumnWidth(15)
	@ExcelProperty("包装名称")
	private String wspName;

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
	 * 待上架数量(操作之前)
	 */
	@ColumnWidth(15)
	@ExcelProperty("待上架数量(操作之前)")
	private BigDecimal stayStockQty;

	/**
	 * 上架数量(操作之前)
	 */
	@ColumnWidth(15)
	@ExcelProperty("上架数量(操作之前)")
	private BigDecimal stockQty;

	/**
	 * 下架数量(操作之前)
	 */
	@ColumnWidth(15)
	@ExcelProperty("下架数量(操作之前)")
	private BigDecimal pickQty;

	/**
	 * 箱号
	 */
	@ColumnWidth(15)
	@ExcelProperty("箱号")
	private String boxCode;

	/**
	 * 托盘号
	 */
	@ColumnWidth(15)
	@ExcelProperty("托盘号")
	private String lpnCode;

	/**
	 * 库位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("库位编码")
	private String locCode;

	/**
	 * 库区编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("库区编码")
	private String zoneCode;

	/**
	 * 货主
	 */
	@ColumnWidth(15)
	@ExcelProperty("货主")
	private String ownerName;

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
	 * 收货日期
	 */
	@ColumnWidth(15)
	@ExcelProperty("收货日期")
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
	@ExcelProperty("库存日志类型")
	private String skuLot7;

	/**
	 * 适用速度等级
	 */
	@ColumnWidth(15)
	@ExcelProperty("适用速度等级")
	private String skuLot8;
}
