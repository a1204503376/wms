package org.nodes.wms.core.warehouse.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.nodes.wms.core.basedata.excel.ContactInfoExcel;

@Data
public class WarehouseExcel extends ContactInfoExcel {
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
	/**
	 * 所属机构编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"所属机构编码"})
	private String deptCode;
	/**
	 * 所属机构名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"所属机构名称"})
	private String deptName;
	/**
	 * 城市
	 */
	@ColumnWidth(15)
	@ExcelProperty({"城市"})
	private String city;
	/**
	 * 省
	 */
	@ColumnWidth(15)
	@ExcelProperty({"省"})
	private String province;
	/**
	 * 邮政编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"邮政编码"})
	private String zipCode;
	/**
	 * 国家
	 */
	@ColumnWidth(15)
	@ExcelProperty({"国家"})
	private String country;
	/**
	 * 备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"备注"})
	private String remark;
}
