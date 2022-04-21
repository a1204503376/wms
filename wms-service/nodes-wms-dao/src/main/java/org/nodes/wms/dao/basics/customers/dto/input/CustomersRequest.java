package org.nodes.wms.dao.basics.customers.dto.input;

import lombok.Data;
/**
 * 客户管理接收类
 **/
@Data
public class CustomersRequest {
	/**
	 * 客户ID
	 */
	private Long id;
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
	 * 业务状态
	 */
	private Integer status;
	/**
	 * 货主ID
	 */
	private String  woId;
	/**
	 * 邮编
	 */
	private String  zipCode;
	/**
	 * 备注
	 */
	private String  remark;
}
