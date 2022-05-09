package org.nodes.wms.dao.instock.asn.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * Asn单导出模板类
 **/
@Data
public class AsnBillExportResponse implements Serializable {

	private static final long serialVersionUID = -3447577647511116979L;
	/**
	 * ASN单编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"ASN单编码"})
	private String asnBillNo;

	/**
	 * ASN单状态value
	 */
	@ColumnWidth(15)
	@ExcelProperty({"单据状态"})
	private String asnBillStateValue;

	/**
	 * ASN单状态枚举
	 */
	@ExcelIgnore
	private AsnBillStateEnum asnBillState;

	/**
	 * 单据类型
	 */
	@ColumnWidth(15)
	@ExcelProperty({"单据类型"})
	private String billTypeName;

	/**
	 * 供应商编码
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = {"供应商编码"})
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商名称"})
	private String supplierName;

	/**
	 * 上位系统单据编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"上位系统单据编码"})
	private String externalOrderNo;

	/**
	 * 上位系统单据创建人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"上位系统单据创建人"})
	private String externalCreateUser;

	/**
	 * 库房
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库房"})
	private String whName;

	/**
	 * ASN单备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"ASN单备注"})
	private String asnBillRemark;

	/**
	 * 创建时间
	 */
	@ColumnWidth(15)
	@ExcelProperty({"创建时间"})
	private Date createTime;

	/**
	 * 创建人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"创建人"})
	private String createUser;

	/**
	 * 更新时间
	 */
	@ColumnWidth(15)
	@ExcelProperty({"更新时间"})
	private Date updateTime;
}
