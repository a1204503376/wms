package org.nodes.wms.core.stock.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * 序列号日志实体类
 *
 * @author zx
 * @since 2020-02-24
 */
@Data
@TableName("wms_serial_log")
@ApiModel(value = "SerialLog对象", description = "序列号日志")
public class SerialLog extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String serialStateDict="serial_state";
	public static final String proTypeDict="pro_type";

	/**
	 * 序列号日志ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "序列号日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wlsnlId;
	/**
	 * 序列号ID
	 */
	@ApiModelProperty(value = "序列号ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long serialId;
	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 库存明细ID
	 */
	@ApiModelProperty(value = "库存明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockDetailId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 序列号
	 */
	@ApiModelProperty(value = "序列号")
	private String serialNumber;
	/**
	 * 序列号状态（1：在库； 2：出库）
	 */
	@ApiModelProperty(value = "序列号状态（1：在库； 2：出库）")
	private Integer serialState;
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
	 * 处理类型（0:新增 1:更新 2:删除）
	 */
	@ApiModelProperty(value = "处理类型（0:新增 1:更新 2:删除）")
	private Integer proType;
	/**
	 * 系统日志ID
	 */
	@ApiModelProperty(value = "系统日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;

}
