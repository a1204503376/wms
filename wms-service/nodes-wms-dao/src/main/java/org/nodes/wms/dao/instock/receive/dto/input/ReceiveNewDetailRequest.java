package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新建收货单明细接收类
 **/
@Data
public class ReceiveNewDetailRequest implements Serializable {
	private static final long serialVersionUID = 8039767137023774339L;
	/**
	 * 订单行号
	 */
	private String  lineNo;
	/**
	 * 物品编码
	 */
	@NotNull(message = "物品编码不能为空")
	private String  skuCode;
	/**
	 * 计划数量
	 */
	@NotNull(message = "计划数量不能为空")
	private BigDecimal planQty;
	/**
	 * 计量单位编码
	 */
	private String umCode;
	/**
	 * 计量单位名称
	 */
	private String umName;
	/**
	 * 规格
	 */
	private BigDecimal skuSpec;
	/**
	 * 备注
	 */
	private String remark;
}