package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * PDA按件拣货请求参数
 **/
@Data
public class PickByPcsRequest extends BaseSkuLot implements Serializable {
	private static final long serialVersionUID = 5276644540482132625L;

	/**
	 * 发货单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 发货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 箱码
	 */
	private String skuCode;

	/**
	 * 库房ID
	 */
	private Long whId;

	/**
	 * 拣货数量
	 */
	private BigDecimal qty;

	/**
	 * 序列号集合
	 */
	private List<String> serailList;
}
