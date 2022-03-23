package org.nodes.wms.core.basedata.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class EnterpriseExcel extends ContactInfoExcel {
	/**
	 * 企业编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业编码"})
	private String enterpriseCode;

	/**
	 * 企业名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业名称"})
	private String enterpriseName;

	/**
	 * 企业简称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业简称"})
	private String enterpriseNameS;

	/**
	 * 企业类型
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业类型"})
	private String enterpriseType;

	/**
	 * 货主编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "货主编码"})
	private String ownerCode;

	/**
	 * 货主名称
	 */
	@ColumnWidth(25)
	@ExcelProperty({"企业信息", "货主名称"})
	private String ownerName;


	/**
	 * 城市
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "城市"})
	private String enterpriseCity;

	/**
	 * 省
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "省"})
	private String enterpriseProvince;

	/**
	 * 邮政编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "邮政编码"})
	private String enterpriseZipCode;

	/**
	 * 国家
	 */
	@ColumnWidth(10)
	@ExcelProperty({"企业信息", "国家"})
	private String enterpriseCountry;
	/**
	 * 备注
	 */
	@ColumnWidth(10)
	@ExcelProperty({"企业信息", "备注"})
	private String remark;
}
