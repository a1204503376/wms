package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发货单分配页面可分配库存响应类
 **/
@Data
public class StockSoPickPlanResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -829789935189414879L;

	/**
	 * 库存id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 库位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库区id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;

	/**
	 * 库区
	 */
	private String zoneCode;

	/**
	 * lpn
	 */
	private String lpnCode;

	/**
	 * 物品id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 可用量
	 */
	private BigDecimal stockEnable;

	/**
	 * 余额
	 */
	private BigDecimal stockBalance;

	/**
	 * 分配量
	 */
	private BigDecimal pickRealQty;

	/**
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;
}
