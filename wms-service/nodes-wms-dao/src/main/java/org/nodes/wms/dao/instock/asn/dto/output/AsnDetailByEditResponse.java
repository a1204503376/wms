package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 编辑返回对象
 **/
@Data
public class AsnDetailByEditResponse implements Serializable {

	private static final long serialVersionUID = 2926311540120578861L;

	/**
	 * Asn单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;

	/**
	 * Asn单编码
	 */
	private String asnBillNo;

	/**
	 * 供应商编码
	 */
	private String sCode;

	/**
	 * 供应商名称
	 */
	private String sName;

	/**
	 * 仓库编码
	 */
	private String a;

	/**
	 * 仓库名称
	 */
	private String b;

	/**
	 * 备注
	 */
	private String asnBillRemark;

	/**
	 * 行号
	 */
	private Integer asnLineNo;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 计量单位编码
	 */
	private String umCode;

	/**
	 * 计量单位名称
	 */
	private String umName;

	/**
	 * 基础计量单位编码
	 */
	private String baseUmCode;

	/**
	 * 基础计量单位名称
	 */
	private String baseUmName;

	/**
	 * 计划数量
	 */
	private String planQty;

	/**
	 * 实收数量
	 */
	private String scanQty;

	/**
	 * 备注
	 */
	private String remark;
}
