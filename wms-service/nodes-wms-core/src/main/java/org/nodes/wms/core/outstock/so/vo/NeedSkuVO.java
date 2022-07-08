package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description 当前订单量Vo类
 *
 * @author pengwei
 * @since 2020-07-30
 */
@Data
public class NeedSkuVO extends SkuLotBaseEntity {

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 货主ID
	 */
	@ApiModelProperty("货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;
	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
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
	@ApiModelProperty("包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String spec;
	/**
	 * 订单数量
	 */
	@ApiModelProperty("订单数量")
	private Integer billCount;
	/**
	 * 出库总量
	 */
	@ApiModelProperty("出库总量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal totalNeedQty;
	/**
	 * 可出库量
	 */
	@ApiModelProperty("可出库量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal sourceZoneQty;
	/**
	 * 库存总量
	 */
	@ApiModelProperty("库存总量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal totalStockQty;
	/**
	 * 缺货量
	 */
	@ApiModelProperty("缺货量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal wantage;
	/**
	 * 计量单位
	 */
	@ApiModelProperty("计量单位")
	private String wsuName;
	/**
	 * 需要补货
	 */
	@ApiModelProperty("需要补货")
	private String isFlagDesc;
	/**
	 * 涉及的出库单ID
	 */
	@ApiModelProperty("涉及的出库单ID")
	private List<String> soBillIdList;
	/**
	 * 涉及的出库单ID字符串(英文逗号分隔)
	 */
	@ApiModelProperty("涉及的出库单ID字符串(英文逗号分隔)")
	private String soBillIdsStr;
}
