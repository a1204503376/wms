package org.nodes.wms.core.basedata.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @Author pengwei
 * @Date 2021/4/8 12:14
 * @Description 货主导出模板
 */
@Data
public class OwnerExcel extends ContactInfoExcel {

	/**
	 * 货主编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"货主信息", "货主编码"})
	private String ownerCode;

	/**
	 * 货主名称
	 */
	@ColumnWidth(25)
	@ExcelProperty({"货主信息", "货主名称"})
	private String ownerName;

	/**
	 * 货主简称
	 */
	@ColumnWidth(20)
	@ExcelProperty({"货主信息", "货主简称"})
	private String ownerNameS;

	/**
	 * 城市
	 */
	@ColumnWidth(15)
	@ExcelProperty({"货主信息", "城市"})
	private String ownerCity;

	/**
	 * 省
	 */
	@ColumnWidth(15)
	@ExcelProperty({"货主信息", "省"})
	private String ownerProvince;

	/**
	 * 邮政编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"货主信息", "邮政编码"})
	private String ownerZipCode;

	/**
	 * 国家
	 */
	@ColumnWidth(10)
	@ExcelProperty({"货主信息", "国家"})
	private String ownerCountry;
}
