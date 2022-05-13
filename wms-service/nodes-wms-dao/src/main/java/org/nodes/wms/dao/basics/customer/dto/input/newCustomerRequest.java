package org.nodes.wms.dao.basics.customer.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 客户管理接收类
 **/
@Data
public class newCustomerRequest implements Serializable {
	private static final long serialVersionUID = 3518961513626458788L;
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
	private Long  woId;
	/**
	 * 邮编
	 */
	private String  zipCode;
	/**
	 * 备注
	 */
	private String  remark;
}
