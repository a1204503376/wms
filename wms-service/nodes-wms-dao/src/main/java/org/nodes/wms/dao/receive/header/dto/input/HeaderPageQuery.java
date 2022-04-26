package org.nodes.wms.dao.receive.header.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * 收货单头表 分页参数
 **/
@Data
public class HeaderPageQuery implements Serializable {
	/**
	 * 收货单编码
	 */
	private String receiveNo;
	/**
	 * 单据状态
	 */
	private Integer billState;
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
	private String whCode;
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
	private String sCode;
}
