package org.nodes.wms.dao.basics.suppliers.dto.input;


import lombok.Data;

import java.io.Serializable;


/**
 * 供应商下拉选择 接收对象
 */
@Data
public class SupplierSelectRequest implements Serializable {

	private static final long serialVersionUID = 2725629154167889829L;
	private Long id;

	/**
	 * 供应商编码
	 */
	private String code;

	/**
	 * 供应商名称
	 */
	private String name;
}
