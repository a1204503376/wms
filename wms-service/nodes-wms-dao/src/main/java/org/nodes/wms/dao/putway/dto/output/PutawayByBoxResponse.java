package org.nodes.wms.dao.putway.dto.output;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 按箱上架请求对象
 **/
@Data
public class PutawayByBoxResponse extends BaseSkuLot implements Serializable {
	private static final long serialVersionUID = 6229009210855257224L;
	/**
	 * 箱码
	 */
	private String boxCode;

	private BigDecimal qty;
}
