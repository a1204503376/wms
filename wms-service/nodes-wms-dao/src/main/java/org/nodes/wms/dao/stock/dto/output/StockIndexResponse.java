package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页周转分析dto类
 **/
@Data
public class StockIndexResponse implements Serializable {

	private static final long serialVersionUID = -4146870668045188194L;

	/**
	 * 待检区物料数量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal qcSkuQty;

	/**
	 * 入库暂存区数量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal stageSkuQty;

	/**
	 * 待检区物料存放天数
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Integer qcSkuStoreDay;

	/**
	 * 入库暂存区物料存放天数
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Integer stageSkuStoreDay;

	/**
	 * 库存物品总数
	 */
	private Integer stockSkuCount;

	/**
	 * 库位占比
	 */
	private Double locOccupy;
}
