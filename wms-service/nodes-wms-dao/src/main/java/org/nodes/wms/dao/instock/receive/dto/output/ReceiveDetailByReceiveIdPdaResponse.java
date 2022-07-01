package org.nodes.wms.dao.instock.receive.dto.output;


import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查看详情页面收货单明细表返回前端视图类
 **/
@Data
public class ReceiveDetailByReceiveIdPdaResponse extends BaseSkuLot implements Serializable {
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
	 * 剩余数量
	 */
	private BigDecimal surplusQty;
	/**
	 * 计量单位
	 */
	private String umName;
	/**
	 * 是否序列号管理 （true：序列号管理  false：非序列号管理）
	 */
	private Boolean isSn;
	/**
	 * 计量单位编码
	 */
	private String wsuCode;
}
