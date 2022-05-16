package org.nodes.wms.dao.basics.carrier.dto.input;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarrierDropDownRequest implements Serializable {
	private static final long serialVersionUID = -7404715687837632881L;
	/**
	 * 承运商名称或编码
	 */
	private String nameOrCode;
}
