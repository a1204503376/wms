package org.nodes.wms.dao.basics.carrier.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 承运商管理接收类
 **/
@Data
public class NewCarrierRequest {
	/**
	 * 承运商编码
	 */
	@NotNull(message = "承运商编码不能为空")
	private String code;
	/**
	 * 承运商名称
	 */
	@NotNull(message = "承运商名称不能为空")
	private String name;
	/**
	 * 承运商简称
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
	 * 备注
	 */
	private String  remark;
}
