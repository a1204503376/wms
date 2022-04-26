package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Asn单编辑参数
 *
 * @author 彭永程
 * @date 2022-04-26 15:29
 **/
@Data
public class EditAsnBillRequest implements Serializable {

	/**
	 * Asn单id
	 */
	@NotNull(message = "Asn单id不能为空")
	private Long asnBillId;

	/**
	 * 单据类型
	 */
	@NotNull(message = "单据类型不能为空")
	private Integer createType;

	/**
	 * 供应商编码
	 */
	@NotNull(message = "供应商编码不能为空")
	private String sCode;

	/**
	 * 供应商名称
	 */
	@NotNull(message = "供应商名称不能为空")
	private String sName;

	/**
	 * 仓库编码
	 */
	private String whCode;

	/**
	 * 仓库名称
	 */
	private String whName;

	/**
	 * 备注
	 */
	private String asnBillRemark;

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
	private BigDecimal planQty;

	/**
	 * 备注
	 */
	private String remark;
}
