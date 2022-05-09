package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 收货单头表 分页参数
 **/
@Data
public class ReceiveHeaderPageQuery implements Serializable {
	private static final long serialVersionUID = 3344143975728396364L;
	/**
	 * 收货单编码
	 */
	private String receiveNo;
	/**
	 * 单据状态
	 */
	private List<Integer> billStateList;
	/**
	 * 物品编码
	 */
	private String  skuCode;
	/**
	 * 上位系统单编号
	 */
	private String externalOrderNo;
	/**
	 * ASN单据编码
	 */
	private String asnBillNo;
	/**
	 * 库房编码
	 */
	private List<String> whCodes;

	/**
	 * 创建时间开始
	 */
	private Date createTimeBegin;
	/**
	 * 创建时间结束
	 */
	private Date createTimeEnd;
	/**
	 * 上位系统单据创建人
	 */
	private String externalCreateUser;
	/**
	 * 供应商编码
	 */
	private String supplierCode;
}
