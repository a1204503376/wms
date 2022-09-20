package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 拣货计划-响应对象
 *
 * @author nodesc
 **/
@Data
public class FindPickToSoPickPlanResponse extends BaseSkuLotEntity implements Serializable {

	private static final long serialVersionUID = -4426590095009774864L;
	/**
	 * 拣货计划ID
	 */
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
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库区id
	 */
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
	 * 包装ID
	 */
	private Long wspId;

	/**
	 * 计划量
	 */
	private BigDecimal pickPlanQty;

	/**
	 * 拣货量
	 */
	private BigDecimal pickRealQty;

	/**
	 * 任务ID
	 */
	private Long taskId;

	/**
	 * 库房ID
	 */
	private Long whId;

	/**
	 * 获取剩余可拣量
	 *
	 * @return 获取剩余可拣量
	 */
	public BigDecimal getSurplusQty() {
		return pickPlanQty.subtract(pickRealQty);
	}

}
