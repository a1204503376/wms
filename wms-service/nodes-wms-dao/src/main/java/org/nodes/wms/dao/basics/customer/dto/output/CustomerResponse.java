package org.nodes.wms.dao.basics.customer.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户表 返回前端视图类
 **/
@Data
public class CustomerResponse implements Serializable {

	private static final long serialVersionUID = 7095933485346837309L;

	/**
	 * 客户ID
	 */
	@ExcelIgnore
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 客户编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "客户编码"})
	private String code;
	/**
	 * 客户名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "客户名称"})
	private String name;
	/**
	 * 客户简称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "客户简称"})
	private String  simpleName;
	/**
	 * 货主ID
	 */
	@ExcelIgnore
	private Long  woId;
	/**
	 * 货主名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "货主"})
	private String ownerName;
	/**
	 * 备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "备注"})
	private String  remark;
	/**
	 * 创建人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "创建人"})
	private String createUser;
	/**
	 * 创建时间
	 */
	@ColumnWidth(20)
	@ExcelProperty({"客户信息", "创建时间"})
	private Date createTime;
	/**
	 * 更新人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "更新人"})
	private String updateUser;
	/**
	 * 更新时间
	 */
	@ColumnWidth(20)
	@ExcelProperty({"客户信息", "更新时间"})
	private Date updateTime;
	/**
	 * 是否启用(1:启用,-1:未启用)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"客户信息", "是否启用"})
	private String status;
}
