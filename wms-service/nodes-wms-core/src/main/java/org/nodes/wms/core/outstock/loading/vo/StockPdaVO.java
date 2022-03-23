package org.nodes.wms.core.outstock.loading.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 库存视图实体类PDA
 *
 * @author chz
 * @since 2020-03-06
 */
@Data
@ApiModel(value = "StockVOPDA对象", description = "库存")
public class StockPdaVO {
	private static final long serialVersionUID = 1L;


	/**
	 * 物品总数量-给手持用LPN
	 */
	@ApiModelProperty("物品总数量")
	private Integer skuCount;

	/**
	 * 库房名称-给手持用LPN
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 库位名称-给手持用LPN
	 */
	@ApiModelProperty("库位编码")
	private String locCode;

	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 单据明细ID
	 */
	@ApiModelProperty(value = "单据明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;

	/**
	 * 分配数量
	 */
	@ApiModelProperty(value = "分配数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal occupyQty;
	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;

	/**
	 * 实际数量
	 */
	@ApiModelProperty(value = "实际数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal scanQty;
	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long StockId;
	/**
	 * 包装规格描述-给手持用LPN
	 */
	@ApiModelProperty("包装规格描述")
	private String skuSpec;
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
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	private String wsuName;

}
