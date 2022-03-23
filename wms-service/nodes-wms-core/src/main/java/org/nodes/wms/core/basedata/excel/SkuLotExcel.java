package org.nodes.wms.core.basedata.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class SkuLotExcel extends SkuLotBaseExcel {
	/**
	 * 批属性编码编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"基本信息", "编码"})
	private String skuLotCode;
	/**
	 * 批属性名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"基本信息", "名称"})
	private String skuLotName;
	/**
	 * 货主编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"基本信息", "货主编码"})
	private String ownerCode;

	/**
	 * 货主名称
	 */
	@ColumnWidth(25)
	@ExcelProperty({"基本信息", "货主名称"})
	private String ownerName;
}
