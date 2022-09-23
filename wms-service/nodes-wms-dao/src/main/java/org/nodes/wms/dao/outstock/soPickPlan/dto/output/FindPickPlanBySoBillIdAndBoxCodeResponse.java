package org.nodes.wms.dao.outstock.soPickPlan.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.math.BigDecimal;

@Data
public class FindPickPlanBySoBillIdAndBoxCodeResponse {
	/**
	 * 拣货计划ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickPlanId;

	/**
	 * 波次ID
	 */
	private Long wellenId;

	/**
	 * 波次编码
	 */
	private Long wellenNo;

	/**
	 * 出库单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 出库单编码(多个用逗号分隔)
	 */
	private String soBillNo;

	/**
	 * 出库单明细id
	 */
	private Long soDetailId;

	/**
	 * 库存ID
	 */
	private Long stockId;

	/**
	 * 库位ID
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
	 * 库区编码
	 */
	private String zoneCode;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * lpn编码
	 */
	private String lpnCode;

	/**
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;

	/**
	 * 物品ID
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
	 * 被替代物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long repSkuId;

	/**
	 * 被替代物品编码
	 */
	private String repSkuCode;

	/**
	 * 被替代物品名称
	 */
	private String repSkuName;

	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 计划量
	 */
	private BigDecimal pickPlanQty;

	/**
	 * 拣货量
	 */
	private BigDecimal pickRealQty;
	/**
	 * 当前对象是否不为空
	 */
	private Boolean isNotEmpty;

	/**
	 * 获取剩余可拣量
	 *
	 * @return
	 */
	public BigDecimal getSurplusQty() {
		return pickPlanQty.subtract(pickRealQty);
	}
}
