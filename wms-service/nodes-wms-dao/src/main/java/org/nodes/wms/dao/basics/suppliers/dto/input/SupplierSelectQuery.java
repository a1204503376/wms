package org.nodes.wms.dao.basics.suppliers.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应商下拉选择 请求对象
 */
@Data
public class SupplierSelectQuery implements Serializable {

	private static final long serialVersionUID = 3624806961644254008L;
	/**
	 * 关键词
	 * 一般为：code,name
	 */
	private String key;
}
