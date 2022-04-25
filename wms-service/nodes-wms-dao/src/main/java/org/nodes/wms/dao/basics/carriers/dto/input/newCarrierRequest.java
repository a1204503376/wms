package org.nodes.wms.dao.basics.carriers.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 承运商管理接收类
 **/
@Data
public class newCarrierRequest {
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
	private String  woId;
	/**
	 * 备注
	 */
	private String  remark;
}
