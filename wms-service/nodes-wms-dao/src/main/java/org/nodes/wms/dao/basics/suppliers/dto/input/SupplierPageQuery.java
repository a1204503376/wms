package org.nodes.wms.dao.basics.suppliers.dto.input;

import lombok.Data;

import java.util.Date;

/**
 * 供应商 分页参数
 */
@Data
public class SupplierPageQuery {
	/**
	 * 供应商编码
	 */
	private String code;

	/**
	 * 供应商名称
	 */
	private String name;

	/**
	 * 供应商简称
	 */
	private String simpleName;

	/**
	 * 创建时间 开始
	 */
	private Date createTimeBegin;

	/**
	 * 创建时间 结束
	 */
	private Date createTimeEnd;

	/**
	 * 更新时间 开始
	 */
	private Date updateTimeBegin;

	/**
	 * 更新时间 结束
	 */
	private Date updateTimeEnd;
}
