package org.nodes.wms.core.stock.core.vo;

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
 * 库存明细表实体类
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Data
@ApiModel(value = "StockDetailVo对象", description = "库存明细表")
public class StockDetailVo extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 库存状态
	 */
	@ApiModelProperty(value = "库存状态")
	private Integer stockStatus;

	@ApiModelProperty("主键id")
//	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 库存id
	 */
	@ApiModelProperty(value = "库存id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 库位id
	 */
	@ApiModelProperty("库位id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 物品id
	 */
	@ApiModelProperty("物品id")
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
	 * 包装id
	 */
	@ApiModelProperty("包装id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 计量单位
	 */
	@ApiModelProperty("计量单位")
	private String wsuName;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 箱码
	 */
	@ApiModelProperty(value = "箱码")
	private String boxCode;
	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
	/**
	 * 下架数量
	 */
	@ApiModelProperty(value = "下架数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;
	/**
	 * 订单明细id
	 */
	@ApiModelProperty("订单明细id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;
	/**
	 * 出库单id
	 */
	@ApiModelProperty("出库单id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty("单据编码")
	private String soBillNo;
	/**
	 * 波次id
	 */
	@ApiModelProperty("波次id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
}
