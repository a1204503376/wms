package org.nodes.wms.dao.instock.receive.dto.output;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查看详情页面收货单明细表返回前端视图类
 **/
@Data
public class ReceiveDetailByReceiveIdPdaResponse implements Serializable {
	private static final long serialVersionUID = -589778228328274800L;
	/**
	 * 物料编码
	 */
	private String skuName;
	/**
	 * 物料名称
	 */
	private String skuCode;
	/**
	 * 型号
	 */
	private String skuLot2;
	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
	/**
	 * 实收数量
	 */
	private BigDecimal scanQty;

	/**
	 * 计量单位
	 */
	private String umName;
	/**
	 * 规格
	 */
	private String skuSpec;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 行号
	 */
	private String lineNo;
	/**
	 * 状态
	 */
	private Integer detailStatus;
	/**
	 * 是否序列号管理 （1：序列号管理  0：非序列号管理）
	 */
	private Integer isSn;
}
