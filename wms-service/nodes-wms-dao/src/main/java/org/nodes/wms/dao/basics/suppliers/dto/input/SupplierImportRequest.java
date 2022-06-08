package org.nodes.wms.dao.basics.suppliers.dto.input;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.Serializable;

/**
 * 供应商导入Excel模板类
 **/
@Data
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
	fillForegroundColor = 44)
@HeadFontStyle(fontName="等线" ,bold = false, fontHeightInPoints=16)
@HeadRowHeight(21)
@ColumnWidth(21)
public class SupplierImportRequest implements Serializable {

	private static final long serialVersionUID = 8048740360278948493L;

	/**
	 * 供应商编码
	 */
	@ColumnWidth(21)
	@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
		fillForegroundColor = 10)
	@ExcelProperty({"供应商编码"})
	private String code;

	/**
	 * 供应商名称
	 */
	@ColumnWidth(21)
	@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
		fillForegroundColor = 10)
	@ExcelProperty({"供应商名称"})
	private String name;

	/**
	 * 供应商简称
	 */
	@ColumnWidth(21)
	@ExcelProperty({"供应商简称"})
	private String simpleName;

	/**
	 * 货主编码
	 */
	@ColumnWidth(21)
	@ExcelProperty({"货主"})
	private String ownerCode;

	/**
	 * 备注
	 */
	@ColumnWidth(21)
	@ExcelProperty({"备注"})
	private String remark;

	/**
	 * 是否启用(1:启用,-1:未启用)
	 */
	@ColumnWidth(21)
	@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
		fillForegroundColor = 10)
	@ExcelProperty({"是否启用"})
	private Integer status;
}
