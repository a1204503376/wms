package org.nodes.wms.core.basedata.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author pengwei
 * @Date 2021/4/8 12:14
 * @Description 联系信息导出模板
 */
@Data
public class ContactInfoExcel {

	/**
	 * 详细地址
	 */
	@ColumnWidth(30)
	@ExcelProperty({"地址信息", "详细地址"})
	private String address;

	/**
	 * 地址类型
	 */
	@ColumnWidth(20)
	@ExcelProperty({"地址信息", "地址类型"})
	private String addressTypeDesc;

	/**
	 * 地址经度
	 */
	@ColumnWidth(15)
	@ExcelProperty({"地址信息", "地址经度"})
	private BigDecimal longitude;

	/**
	 * 地址纬度
	 */
	@ColumnWidth(15)
	@ExcelProperty({"地址信息", "地址纬度"})
	private BigDecimal latitude;

	/**
	 * 地址默认标记
	 */
	@ColumnWidth(15)
	@ExcelProperty({"地址信息", "地址默认标记"})
	private String addressDefaultFlag;

	/**
	 * 联系人姓名
	 */
	@ColumnWidth(15)
	@ExcelProperty({"联系人信息", "联系人姓名"})
	private String contactsName;

	/**
	 * 联系人Email
	 */
	@ColumnWidth(20)
	@ExcelProperty({"联系人信息", "联系人Email"})
	private String contactsEmail;

	/**
	 * 联系人电话
	 */
	@ColumnWidth(20)
	@ExcelProperty({"联系人信息", "联系人电话"})
	private String contactsNumber;

	/**
	 * 联系人传真
	 */
	@ColumnWidth(20)
	@ExcelProperty({"联系人信息", "联系人传真"})
	private String contactsFax;

	/**
	 * 联系人默认标记
	 */
	@ColumnWidth(20)
	@ExcelProperty({"联系人信息", "联系人默认标记"})
	private String contactsDefaultFlag;

}
