package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ASN单创建请求对象
 */
@Data
public class AsnRequest implements Serializable {

	/**
	 * asn单据编码
	 */
	private String asnBillNo;

	/**
	 * asn单据状态
	 */
	private String asnBillState;

	/**
	 * 创建日期 开始
	 */
	private Date createTimeBegin;

	/**
	 * 创建时间 结束
	 */
	private Date createTimeEnd;

	/**
	 * 供应商（支持名称和编码的模糊查找）
	 */
	private String suppliers;

	/**
	 * 上游编码
	 */
	private String externalOrderNo;

	/**
	 * 上游创建人
	 */
	private String externalCreateUser;

	/**
	 * 仓库编码
	 */
	private String whCode;

	/**
	 * 物品编码
	 */
	private String skuCode;

}
