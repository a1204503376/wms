package org.nodes.wms.core.warehouse.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LpnExcel {
	/**
	 * 容器编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器编码"})
	private String lpnCode;
	/**
	 * 容器名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器名称"})
	private String lpnName;
	/**
	 * 容器类型
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器类型"})
	private Integer lpnType;
	/**
	 * 容器重量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器重量"})
	private BigDecimal lpnWeight;
}
