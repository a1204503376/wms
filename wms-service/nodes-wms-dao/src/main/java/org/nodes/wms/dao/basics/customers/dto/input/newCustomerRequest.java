package org.nodes.wms.dao.basics.customers.dto.input;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 客户管理接收类
 **/
@Data
public class newCustomerRequest {
	/**
	 * 客户ID
	 */
	private Long id;
	/**
	 * 客户编码
	 */
	@NotNull(message = "客户编码不能为空")
	private String code;
	/**
	 * 客户名称
	 */
	@NotNull(message = "客户名称不能为空")
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
