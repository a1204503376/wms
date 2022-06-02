package org.nodes.wms.dao.basics.carrier.dto.input;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 承运商导入Excel模板类
 **/
@Data
public class CarrierExcelRequest {

	private static final long serialVersionUID = 8048740360278948493L;

	/**
	 * 承运商编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"承运商编码"})
	private String code;

	/**
	 * 承运商名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"承运商名称"})
	private String name;

	/**
	 * 承运商简称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"承运商简称"})
	private String simpleName;

	/**
	 * 货主编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"货主"})
	private String ownerCode;

	/**
	 * 备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"备注"})
	private String remark;

	/**
	 * 是否启用(1:启用,-1:未启用)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"是否启用"})
	private Integer status;
}
