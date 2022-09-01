package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存id和分配数量请求类
 **/
@Data
public class StockIdAndSoPickPlanQtyRequest implements Serializable {

	private static final long serialVersionUID = -8856705420425858522L;

	/**
	 * 库存id
	 */
	private Long stockId;

	/**
	 * 计划数量
	 */
	private BigDecimal soPickPlanQty;
}
