package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.wms.core.instock.asn.vo.LpnItemVo;
import org.nodes.wms.core.outstock.so.entity.PickPlan;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 按件拣货提交VO
 *
 * @Author zx
 * @Date 2020/3/4
 **/
@Data
public class PickTaskSubmitVO {

	/**
	 * 波次编码
	 */
	@ApiModelProperty(value = "波次编码")
	private String wellenNo;

	@ApiModelProperty(value = "出库单编码")
	private String soBillNo;
	/**
	 * 任务ID
	 */
	@ApiModelProperty(value = "任务ID")
	private String taskId;

	/**
	 * 拣货计划ID
	 */
	@NotNull
	@ApiModelProperty(value = "拣货计划ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickPlanId;
	/**
	 * 目标容器编码
	 */
	@ApiModelProperty(value = "拣货容器编码: 目标容器编码")
	private String targetLpnCode;

	/**
	 * 待拣货托盘
	 */
	@ApiModelProperty(value = "拣货容器编码: 目标容器编码")
	private List<LpnItemVo> targetLpnCodes = new ArrayList<>();
	/**
	 * 落放位置:目标库位
	 */
	@NotNull
	@ApiModelProperty(value = "落放位置:目标库位")
	private String targetLocCode;
	/**
	 * 源容器编码
	 */
	@ApiModelProperty(value = "源容器编码")
	private String sourceLpnCode;
	/**
	 * 源库位编码
	 */
	@NotNull
	@ApiModelProperty(value = "源库位编码")
	private String sourceLocCode;

	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@NotNull
	@ApiModelProperty(value = "物品编码")
	private String skuCode;

	/**
	 * 批次号
	 */
	@NotNull
	@ApiModelProperty(value = "批次号")
	private String lotNumber;

	/**
	 * 拣货量
	 */
	@NotNull
	@Range(min = 1)
	@ApiModelProperty(value = "拣货数量")
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 包装明细id
	 */
	@NotNull
	@ApiModelProperty(value = "包装明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	/**
	 * 是否为序列号管理
	 */
	@ApiModelProperty(value = "是否为序列号管理")
	private Integer isSn;

	/**
	 * 序列号列表
	 */
	@ApiModelProperty(value = "序列号列表")
	private List<String> serialList;

	/**
	 * 按单拣货计划列表
	 */
	@ApiModelProperty(value = "按单拣货计划列表")
	private List<PickPlan> pickPlans = new ArrayList<>();

	@ApiModelProperty(value = "批属性")
	private SkuLotBaseEntity lots;

	@ApiModelProperty("箱号")
	private String boxCode;
}
