package org.nodes.wms.dao.basics.suppliers.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商导出模板类
 *
 * @author 彭永程
 * @date 2022-04-29 13:26
 **/
@Data
public class SupplierExportResponse implements Serializable {

	private static final long serialVersionUID = 269871453535063854L;

	/**
	 * 供应商编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商信息", "供应商编码"})
	private String code;

	/**
	 * 供应商名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商信息", "供应商名称"})
	private String name;

	/**
	 * 供应商简称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商信息", "供应商简称"})
	private String simpleName;

	/**
	 * 货主名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商信息", "货主"})
	private String ownerName;

	/**
	 * 备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商信息", "备注"})
	private String remark;

	/**
	 * 是否启用(0:启用,-1:未启用)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商信息", "是否启用"})
	private Integer status;

	/**
	 * 创建人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"供应商信息", "创建人"})
	private Long createUser;

	/**
	 * 创建时间
	 */
	@ColumnWidth(20)
	@ExcelProperty({"供应商信息", "创建时间"})
	private Date createTime;

	/**
	 * 更新时间
	 */
	@ColumnWidth(20)
	@ExcelProperty({"供应商信息", "更新时间"})
	private Date updateTime;
}
