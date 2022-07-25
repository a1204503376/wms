package org.nodes.wms.dao.picking.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 判断当前物品是否是序列号管理响应对象
 **/
@Data
public class EsitSerialNumberResponse implements Serializable {
	private static final long serialVersionUID = -2209406001214384991L;
	/**
	 * 是否序列号管理
	 */
	private Boolean isSn;

}
