package org.nodes.wms.dao.basics.billType.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 单据类型下拉选择 请求对象
 */
@Data
public class BillTypeSelectQuery implements Serializable {

	private static final long serialVersionUID = -3741525849869946135L;

	/**
	 * 关键词
	 * 一般为：billTypeCd,billTypeName
	 */
	private String key;

	/**
	 * 模式类型 I:入库、 O:出库、 '':全部
	 */
	private String ioType;
}
