package org.nodes.wms.dao.receive.detail.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收货明细返回前端视图类
 **/
@Data
public class DetailResponse implements Serializable {
	/**
	 * 订单行号
	 */
	private String  lineNo;
	/**
	 * 物品编码
	 */
	private String  skuCode;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
	/**
	 * 实收数量
	 */
	private BigDecimal scanQty;
	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;
	/**
	 * 计量单位名称
	 */
	private String umName;
	/**
	 * 状态
	 */
	private Integer  status;
	/**
	 * 规格
	 */
	private BigDecimal skuSpec;
	/**
	 * 备注
	 */
	private String remark;

}
