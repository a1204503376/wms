package org.nodes.wms.core.outstock.so.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 拣货记录日志实体类
 *
 * @author zx
 * @since 2020-03-04
 */
@Data
@TableName("log_so_pick")
@ApiModel(value = "SoPick对象", description = "拣货记录日志")
public class SoPick extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 拣货记录ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "拣货记录ID")
	private Long lsopId;
	/**
	 * 业务发生时间
	 */
	@ApiModelProperty(value = "业务发生时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime procTime;
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	@ApiModelProperty(value = "库位编码")
	private String locCode;
	/**
	 * 任务ID
	 */
	@ApiModelProperty(value = "任务ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long taskId;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
	/**
	 * 波次id
	 */
	@ApiModelProperty("波次id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
	/**
	 * 波次编号
	 */
	@ApiModelProperty(value = "波次编号")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenNo;
	/**
	 * 拣货量
	 */
	@ApiModelProperty(value = "拣货量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickRealQty;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	private String wsuName;
	/**
	 * 换算倍数
	 */
	@ApiModelProperty(value = "换算倍数")
	private Integer convertQty;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 目标容器
	 */
	@ApiModelProperty(value = "目标容器")
	private String targetLpnCode;
	/**
	 * 单据ID
	 */
	@ApiModelProperty(value = "单据ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String soBillNo;
	/**
	 * 发货清单id
	 */
	@ApiModelProperty("发货清单id")
	private Long inventoryId;
	/**
	 * 明细ID
	 */
	@ApiModelProperty(value = "明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
	/**
	 * 拣货计划ID
	 */
	@ApiModelProperty(value = "拣货计划ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickPlanId;
//	/**
//	 * 用户ID
//	 */
//	@ApiModelProperty(value = "用户ID")
//	@JsonSerialize(using = ToStringSerializer.class)
//	private Long userId;
//	/**
//	 * 用户名称
//	 */
//	@ApiModelProperty(value = "用户名称")
//	private String userName;

	/**
	 * 序列号
	 */
	@ApiModelProperty("序列号")
	private String soDetailCode;

	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

}
