package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;

import java.util.Date;

/**
 * 查看详情-ASN单日志
 **/
@Data
public class AsnLogActionViewResponse {
	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 操作人员账号
	 */
	private String userAccount;

	/**
	 * 操作人员真实名称
	 */
	private String userRealName;

	/**
	 * 操作类型
	 */
	private Integer type;

	/**
	 * 目标单据id,可能为空
	 */
	private Long billId;

	/**
	 * 目标单据编码,可能为空
	 */
	private String billNo;

	/**
	 * 操作内容
	 */
	private String log;

	/**
	 * 变更时间
	 */
	private Date updateTime;
}
