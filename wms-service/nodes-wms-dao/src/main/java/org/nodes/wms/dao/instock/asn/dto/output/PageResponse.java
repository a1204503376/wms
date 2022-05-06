package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ASN单分页结果
 */
@Data
public class PageResponse implements Serializable {

	private static final long serialVersionUID = 8705509654116950416L;

	/**
	 * ASN单主键id
	 */
	private Long asnBillId;

	/**
	 * ASN单编码
	 */
	private String asnBillNo;

	/**
	 * 单据类型
	 */
	private Integer createType;

	/**
	 * 单据状态
	 */
	private Integer asnBillState;

	/**
	 * 供应商编码
	 */
	private String sCode;

	/**
	 * 供应商名称
	 */
	private String sName;

	/**
	 * 上游编码
	 */
	private String externalOrderNo;

	/**
	 * 上游创建人
	 */
	private String externalCreateUser;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 仓库编码
	 */
	private String whCode;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 备注
	 */
	private String asnBillRemark;

}
