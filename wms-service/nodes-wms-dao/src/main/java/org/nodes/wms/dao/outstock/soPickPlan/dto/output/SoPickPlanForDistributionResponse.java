package org.nodes.wms.dao.outstock.soPickPlan.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分配页面-拣货计划信息响应对象
 **/
@Data
public class SoPickPlanForDistributionResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -845376913346559673L;

	/**
	 * 拣货计划id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickPlanId;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 计划量
	 */
	private BigDecimal pickPlanQty;

	/**
	 * 分配量
	 */
	private BigDecimal pickRealQty;

	/**
	 * 库区id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;

	/**
	 * 库区编码
	 */
	private String zoneCode;

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
	 * lpn
	 */
	private String lpnCode;

	/**
	 * 物品id
	 */
	private String skuId;

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
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;
}
