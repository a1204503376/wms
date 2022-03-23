package org.nodes.wms.core.warehouse.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class PlatformInfoExcel {

	/**
	 * 月台编号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"月台编号"})
	private String platformCode;
	/**
	 * 月台名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"月台名称"})
	private String platformName;
	/**
	 * 仓库编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"仓库编码"})
	private String whCode;
	/**
	 * 仓库名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"仓库名称"})
	private String whName;
	@ExcelIgnore
	private Integer platformType;
	/**
	 * 月台类型
	 */
	@ColumnWidth(15)
	@ExcelProperty({"月台类型"})
	private String platformTypeDesc;
	/**
	 * 月台容量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"月台容量"})
	private Integer platformCapacity;

}
