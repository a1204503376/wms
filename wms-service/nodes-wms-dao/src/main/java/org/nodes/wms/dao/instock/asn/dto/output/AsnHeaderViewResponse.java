package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查看明细-ASN单头表返回对象
 **/
@Data
public class AsnHeaderViewResponse implements Serializable {

	private static final long serialVersionUID = 9035568602666392913L;

	/**
	 * ASN单头表id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;

	/**
	 * 单据编码
	 */
	private String asnBillNo;

	/**
	 * 单据类型名称
	 */
	private String billTypeName;

	/**
	 * 供应商编码
	 */
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 仓库编码
	 */
	private String whCode;

	/**
	 * 仓库名称
	 */
	private String whName;

	/**
	 * 货主名称
	 */
	private String ownerName;

	/**
	 * 备注
	 */
	private String asnBillRemark;
}

