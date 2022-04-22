package org.nodes.wms.dao.basics.carriers.dto.input;

import lombok.Data;
/**
 * 承运商管理接收类
 **/
@Data
public class CarriersRequest {
	/**
	 * 承运商ID
	 */
	private Long id;
	/**
	 * 承运商编码
	 */
	private String code;
	/**
	 * 承运商名称
	 */
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
