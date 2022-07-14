package org.nodes.wms.dao.outstock.logSoPick.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 拣货记录日志
 **/
@Data
@TableName("log_so_pick")
public class LogSoPick extends SkuLotBaseEntity {

	private static final long serialVersionUID = -8493915096346174881L;

	/**
	 * 拣货记录id
	 */
	@TableId(value = "lsop_id", type = IdType.ASSIGN_ID)
	private Long lsopId;

	/**
	 * 业务发生时间
	 */
	private Date procTime;

	/**
	 * 库位id
	 */
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 任务id
	 */
	private Long taskId;

	/**
	 * 物品id
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
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 波次id
	 */
	private Long wellenId;

	/**
	 * 波次编号
	 */
	private Long wellenNo;

	/**
	 * 拣货量
	 */
	private BigDecimal pickRealQty;

	/**
	 * 包装id
	 */
	private Long wspId;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 计量单位编码
	 */
	private String wsuCode;

	/**
	 * 计量单位名称
	 */
	private String wsuName;

	/**
	 * 换算倍率
	 */
	private Integer convertQty;

	/**
	 * 容器编码
	 */
	private String lpnCode;

	/**
	 * 目标容器
	 */
	private String targetLpnCode;

	/**
	 * 单据id
	 */
	private Long soBillId;

	/**
	 * 单据编码
	 */
	private String soBillNo;

	/**
	 * 发货清单id
	 */
	private Long inventoryId;

	/**
	 * 明细id
	 */
	private Long soDetailId;

	/**
	 * 拣货计划id
	 */
	private Long pickPlanId;

	/**
	 * 序列号
	 */
	private String soDetailCode;

	/**
	 * 库房id
	 */
	private Long whId;
}
