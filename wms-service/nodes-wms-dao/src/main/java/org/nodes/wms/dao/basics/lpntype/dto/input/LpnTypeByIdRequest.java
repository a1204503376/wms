package org.nodes.wms.dao.basics.lpntype.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 容器管理根据id查询接收类
 **/
@Data
public class LpnTypeByIdRequest implements Serializable {
	private static final long serialVersionUID = 1968426677200511256L;

	/**
	 * 要查询的容器id
	 */
	private Long id;
}
