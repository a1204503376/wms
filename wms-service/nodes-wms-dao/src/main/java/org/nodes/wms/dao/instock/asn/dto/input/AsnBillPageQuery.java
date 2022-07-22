package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ASN单据 分页参数
 */
@Data
public class AsnBillPageQuery implements Serializable {

	private static final long serialVersionUID = 1599695543698915713L;

	/**
	 * Asn单据编码
	 */
	private String asnBillNo;

	/**
	 * Asn单据状态
	 */
	private List<Integer> asnBillStateList;

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
	private String supplier;

	/**
	 * 上游编码
	 */
	private String externalOrderNo;

	/**
	 * 上游创建人
	 */
	private String externalCreateUser;

	/**
	 * 仓库id
	 */
	private List<String> whIdList;

	/**
	 * 物品id集合
	 *
	 */
	private List<Long> skuIdList;
}
