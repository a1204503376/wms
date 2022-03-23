package org.nodes.wms.core.stock.transfer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库内移动明细实体类
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Data
@TableName("wms_transfer_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TransferDetail对象", description = "库内移动明细")
public class TransferDetail extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 库内移动明细ID
	 */
	@ApiModelProperty(value = "库内移动明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "transfer_detail_id", type = IdType.ASSIGN_ID)
	private Long transferDetailId;
	/**
	 * 库内移动表头ID
	 */
	@ApiModelProperty(value = "库内移动表头ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long transferBillId;
	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 货主ID
	 */
	@ApiModelProperty("货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 包装层级
	 */
	@ApiModelProperty(value = "包装层级")
	private Integer skuLevel;
	/**
	 * 源库位ID
	 */
	@ApiModelProperty(value = "源库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sourceLocId;
	/**
	 * 目标库位ID
	 */
	@ApiModelProperty(value = "目标库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long targetLocId;
	/**
	 * 计划移库数量
	 */
	@ApiModelProperty(value = "计划移库数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal planQty;
	/**
	 * 实际移库数量
	 */
	@ApiModelProperty(value = "实际移库数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal transQty;
	/**
	 * 单位编码
	 */
	@ApiModelProperty(value = "单位编码")
	private String wsuCode;
	/**
	 * 单位名称
	 */
	@ApiModelProperty(value = "单位名称")
	private String wsuName;


}
