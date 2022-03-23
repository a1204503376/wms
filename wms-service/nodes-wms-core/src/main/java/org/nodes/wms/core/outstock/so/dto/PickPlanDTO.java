package org.nodes.wms.core.outstock.so.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.core.outstock.so.entity.PickPlan;

import java.math.BigDecimal;
import java.util.List;

/**
 * 按件拣货DTO
 *
 * @Author zx
 * @Date 2020/3/4
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class PickPlanDTO extends PickPlan {

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
	private String wellenNo;
	/**
	 * 目标容器编码
	 */
	private String targetLpnCode;
	/**
	 * 落放位置:目标库位
	 */
	private String targetLocCode;
	/**
	 * 源容器编码
	 */
	private String sourceLpnCode;
	/**
	 * 源库位编码
	 */
	private String sourceLocCode;
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
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 拣货量
	 */
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 包装id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	/**
	 * 是否为序列号管理
	 */
	private int isSn;

	/**
	 * 序列号列表
	 */
	private List<String> serialList;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 用户姓名
	 */
	private String userName;


	/**
	 * 发货单单据ID
	 */
	private Long soBillId;

	/**
	 * 出库单单据编码
	 */
	private String soBillNo;

	/**
	 * 发货单明细ID
	 */
	private Long soDetailId;

	/**
	 * 批属性
	 */
	private SkuLotBaseEntity skuLot;

	/**
	 * 系统日志id
	 */
	private Long systemProcId;

	/**
	 * 箱号
	 */
	private String boxCode;
}
