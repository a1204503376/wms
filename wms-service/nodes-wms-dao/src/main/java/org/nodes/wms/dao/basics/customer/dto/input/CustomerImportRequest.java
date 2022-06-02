package org.nodes.wms.dao.basics.customer.dto.input;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户导入Excel模板类
 **/
@Data
public class CustomerImportRequest implements Serializable {

	private static final long serialVersionUID = 8048740360278948493L;

	/**
	 * 客户编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户编码"})
	private String code;

	/**
	 * 客户名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户名称"})
	private String name;

	/**
	 * 客户简称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户简称"})
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
