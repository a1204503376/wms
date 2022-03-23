package org.nodes.wms.core.basedata.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class SkuUmExcel {
	/**
	 * 计量单位编码
	 */
	@ColumnWidth(23)
	@ExcelProperty({"计量单位编码"})
	private String wsuCode;

	/**
	 * 计量单位名称
	 */
	@ColumnWidth(23)
	@ExcelProperty({"计量单位名称"})
	private String wsuName;
}
