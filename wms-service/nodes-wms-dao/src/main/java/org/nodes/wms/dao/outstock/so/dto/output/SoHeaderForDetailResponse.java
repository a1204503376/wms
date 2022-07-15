package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 发货单查看明细头表信息响应类
 **/
@Data
public class SoHeaderForDetailResponse implements Serializable {

	private static final long serialVersionUID = -6117291554836588836L;

	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 单据类型
	 */
	private String billType;

	/**
	 * 所属库房
	 */
	private String whName;

	/**
	 * 所属货主
	 */
	private String ownerName;

	/**
	 * 客户
	 */
	private String customerName;

	/**
	 * 出库方式
	 */
	private String outstockType;

	/**
	 * 发货方式
	 */
	private String transportType;

	/**
	 * 备注
	 */
	private String soBillRemark;
}
