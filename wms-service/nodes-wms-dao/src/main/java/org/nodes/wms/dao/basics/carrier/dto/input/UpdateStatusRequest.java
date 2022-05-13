package org.nodes.wms.dao.basics.carrier.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 承运商管理根据id修改类
 * **/
@Data
public class UpdateStatusRequest implements Serializable {
	private static final long serialVersionUID = -8189134246065994643L;
	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 业务状态
	 */
	private Integer status;
}
