package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Pda根据发货单ID查询出库单明细-响应对象
 **/
@Data
public class FindPickingBySoBillIdResponse extends BaseSkuLotEntity implements Serializable {
	private static final long serialVersionUID = 1486941751771336282L;
	/**
	 * 发货单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 行号
	 */
	private String soLineNo;

	/**
	 * 实际数量
	 */
	private BigDecimal scanQty;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 基础计量单位名称
	 */
	private String baseUmName;

	/**
	 * 物品编码
	 */
	private String skuCode;

}
