package org.nodes.wms.dao.instock.receive.dto.output;


import lombok.Data;
import org.nodes.wms.dao.instock.receive.enums.ReceiveDetailStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查看详情页面收货单明细表返回前端视图类
 **/
@Data
public class DetailReceiveDetailResponse implements Serializable {
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
	private ReceiveDetailStatusEnum detailStatus;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 客户
	 */
	private String skuLot4;
	/**
	 * 钢背批次
	 */
	private String skuLot5;
	/**
	 * 摩擦块批次
	 */
	private String  skuLot6;
	/**
	 * CRCC
	 */
	private String skuLot8;
}
