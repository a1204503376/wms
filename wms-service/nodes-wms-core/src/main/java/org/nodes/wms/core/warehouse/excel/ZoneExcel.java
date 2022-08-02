package org.nodes.wms.core.warehouse.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class ZoneExcel {
	/**
	 * 库区编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库区编码"})
	private String zoneCode;
	/**
	 * 库区名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库区名称"})
	private String zoneName;
	/**
	 * 仓库编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库房编码"})
	private String whCode;
	/**
	 * 仓库名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库房名称"})
	private String whName;
	/**
	 * 库区类型编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库区类型编码"})
	private String zoneType;
	/**
	 * 是否启用
	 */
	@ColumnWidth(15)
	@ExcelProperty({"是否启用"})
	private String status;
}
