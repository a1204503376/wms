package org.nodes.wms.dao.instock.receive.dto.output;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查看详情页面收货单明细表返回前端视图类
 **/
@Data
public class ReceiveDetailByReceiveIdPdaResponse implements Serializable {
	private static final long serialVersionUID = -2558857301381666186L;
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
	 * 计量单位
	 */
	private String umName;
	/**
	 * 是否序列号管理 （1：序列号管理  0：非序列号管理）
	 */
	private Integer isSn;
}
