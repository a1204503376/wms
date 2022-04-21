package org.nodes.wms.dao.basics.customers.dto.input;

import lombok.Data;

/**
 * 客户表 分页参数
 **/
@Data
public class CustomersPageQuery {
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
	private String  createTimeBegin;
	/**
	 * 创建时间结束
	 */
	private String  createTimeEnd;
	/**
	 * 更新时间开始
	 */
	private String  updateTimeBegin;
	/**
	 * 更新时间结束
	 */
	private String  updateTimeEnd;






}
