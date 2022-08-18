package org.nodes.wms.dao.outstock.soPickPlan.entities;

import java.math.BigDecimal;

import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import lombok.Data;

/**
 * 拣货计划实体类
 *
 * @author nodesc
 **/
@Data
@TableName("so_pick_plan")
public class SoPickPlan extends BaseSkuLotEntity {

	private static final long serialVersionUID = -4706292026022708402L;

	/**
	 * 拣货计划ID
	 */
	@TableId(value = "pick_plan_id", type = IdType.ASSIGN_ID)
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
	 * 库存状态(0正常,1冻结)
	 */
	private String stockStatus;

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
	 * 层级
	 */
	private Integer skuLevel;

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
	 * 货主ID
	 */
	private Long woId;

	/**
	 * 乐观锁
	 */
	@Version
	private Integer version;

	public BigDecimal getSurplusQty(){
		return pickPlanQty.subtract(pickRealQty);
	}
}
