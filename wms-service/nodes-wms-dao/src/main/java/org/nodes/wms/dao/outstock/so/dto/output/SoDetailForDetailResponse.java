package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发货单查看明细明细信息响应类
 **/
@Data
public class SoDetailForDetailResponse implements Serializable {

	private static final long serialVersionUID = -23923000137015441L;

	/**
	 * 行号
	 */
	private String soLineNo;

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
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 实发数量
	 */
	private BigDecimal scanQty;

	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;

	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 型号
	 */
	private String skuLot2;


	/**
	 * 专用客户
	 */
	private String skuLot4;

	/**
	 * 备注
	 */
	private String remark;
}
