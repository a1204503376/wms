package org.nodes.wms.core.count.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author wangYuNan
 * @Date 2021/7/1 15:00
 * @Description 差异报告导出模板
 */
@Data
public class CountReportExcel {
	/**
	 * 序列号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"序列号"})
	private String serialNumber;

	/**
	 * 库位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库位编码"})
	private String locCode;
	/**
	 * 容器编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器编码"})
	private String lpnCode;
	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"物品编码"})
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"物品名称"})
	private String skuName;
	/**
	 * 计量单位
	 */
	@ColumnWidth(15)
	@ExcelProperty({"计量单位"})
	private String wsuName;
	/**
	 * 系统批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"系统批次"})
	private String systemLot;
	/**
	 * 系统数量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"系统数量"})
	private BigDecimal wmsQty;
	/**
	 * 盘点批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"盘点批次"})
	private String countLot;
	/**
	 * 盘点数量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"盘点数量"})
	private BigDecimal countQty;
	/**
	 * 差异数量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"差异数量"})
	private BigDecimal 	differentNum;

}
