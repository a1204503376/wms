package org.nodes.wms.dao.outstock.logSoPick.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;

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
	private String lsopId;

	/**
	 * 业务发生时间
	 */
	private String procTime;

	/**
	 * 库位id
	 */
	private String locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 任务id
	 */
	private String taskId;

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
	 * 波次id
	 */
	private String wellenId;

	/**
	 * 波次编号
	 */
	private String wellenNo;

	/**
	 * 拣货量
	 */
	private String pickRealQty;

	/**
	 * 包装id
	 */
	private String wspId;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 层级
	 */
	private String skuLevel;

	/**
	 * 计量单位名称
	 */
	private String wsuName;

	/**
	 * 换算倍率
	 */
	private String convertQty;

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
	private String soBillId;

	/**
	 * 单据编码
	 */
	private String soBillNo;

	/**
	 * 发货清单id
	 */
	private String inventoryId;

	/**
	 * 明细id
	 */
	private String soDetailId;

	/**
	 * 拣货计划id
	 */
	private String pickPlanId;

	/**
	 * 序列号
	 */
	private String soDetailCode;

	/**
	 * 库房id
	 */
	private String whId;
}
