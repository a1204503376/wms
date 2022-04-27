package org.nodes.wms.dao.basics.customers.dto.input;

import lombok.Data;

import java.util.Date;

/**
 * 客户表 分页参数
 **/
@Data
public class CustomerPageQuery {
	/**
	 * 客户编码
	 */
	private String code;
	/**
	 * 客户名称
	 */
	private String name;
	/**
	 * 客户简称
	 */
	private String  simpleName;
	/**
	 * 创建时间开始
	 */
	private Date  createTimeBegin;
	/**
	 * 创建时间结束
	 */
	private Date createTimeEnd;
	/**
	 * 更新时间开始
	 */
	private Date updateTimeBegin;
	/**
	 * 更新时间结束
	 */
	private Date  updateTimeEnd;
}
