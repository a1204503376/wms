package org.nodes.wms.core.instock.asn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 清点记录实体类
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Data
@TableName("asn_container_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ContainerLog对象", description = "清点记录")
public class ContainerLog extends SkuLotBaseEntity implements Serializable  {

	private static final long serialVersionUID = 1L;

	/**
	 * 清点记录id
	 */
	@TableId(value = "acl_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "清点记录id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long aclId;
	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String asnBillNo;
	/**
	 * 收货清单id
	 */
	@ApiModelProperty("收货清单id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long inventoryId;
	/**
	 * 订单明细id
	 */
	@ApiModelProperty(value = "订单明细id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnDetailId;
	/**
	 * 序列号
	 */
	@ApiModelProperty(value = "序列号")
	private String snDetailCode;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 箱号
	 */
	@ApiModelProperty(value = "箱号")
	private String boxCode;
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
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal aclQty;
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
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 包装层级
	 */
	@ApiModelProperty(value = "包装层级")
	private Integer skuLevel;
	/**
	 * 清点状态
	 */
	@ApiModelProperty(value = "清点状态")
	private Integer aclStatus;
	/**
	 * 批次号编号
	 */
	@ApiModelProperty(value = "批次号编号")
	private String lotNumber;
	/**
	 * 规格
	 */
	@ApiModelProperty(value = "规格")
	private String skuSpec;
	/**
	 * 计划数量
	 */
	@ApiModelProperty(value = "计划数量")
	private BigDecimal planQty;

}
