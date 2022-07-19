package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发货单分配页面拣货记录响应类
 **/
@Data
public class LogSoPickDistResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -829789935189414879L;

	/**
	 * 库存id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 可用量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal stockEnable;

	/**
	 * 余额
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal stockBalance;

	/**
	 * 库位
	 */
	private String locName;

	/**
	 * 库区
	 */
	private String zoneName;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * lpn
	 */
	private String lpnCode;

	/**
	 * 库存状态
	 */
	private String stockStatus;
}
