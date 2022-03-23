
package org.nodes.wms.core.stock.core.entity;

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

/**
 * 批次号实体类
 *
 * @author pengwei
 * @since 2019-12-25
 */
@Data
@TableName("wms_lot")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Lot对象", description = "批次号")
public class Lot extends SkuLotBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 批次号ID
	 */
	@ApiModelProperty(value = "批次号ID")
	@TableId(value = "lot_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long lotId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
	/**
	 * 批次状态
	 */
	@ApiModelProperty("批次状态")
	private Integer lotStatus;
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
	 * 物品批属性1
	 */
	@ApiModelProperty(value = "物品批属性1")
	private String skuLot1;
	/**
	 * 物品批属性2
	 */
	@ApiModelProperty(value = "物品批属性2")
	private String skuLot2;
	/**
	 * 物品批属性3
	 */
	@ApiModelProperty(value = "物品批属性3")
	private String skuLot3;
	/**
	 * 物品批属性4
	 */
	@ApiModelProperty(value = "物品批属性4")
	private String skuLot4;
	/**
	 * 物品批属性5
	 */
	@ApiModelProperty(value = "物品批属性5")
	private String skuLot5;
	/**
	 * 物品批属性6
	 */
	@ApiModelProperty(value = "物品批属性6")
	private String skuLot6;
	/**
	 * 物品批属性7
	 */
	@ApiModelProperty(value = "物品批属性7")
	private String skuLot7;
	/**
	 * 物品批属性8
	 */
	@ApiModelProperty(value = "物品批属性8")
	private String skuLot8;
	/**
	 * 物品批属性9
	 */
	@ApiModelProperty(value = "物品批属性9")
	private String skuLot9;
	/**
	 * 物品批属性10
	 */
	@ApiModelProperty(value = "物品批属性10")
	private String skuLot10;
	/**
	 * 物品批属性11
	 */
	@ApiModelProperty(value = "物品批属性11")
	private String skuLot11;
	/**
	 * 物品批属性12
	 */
	@ApiModelProperty(value = "物品批属性12")
	private String skuLot12;
	/**
	 * 物品批属性13
	 */
	@ApiModelProperty(value = "物品批属性13")
	private String skuLot13;
	/**
	 * 物品批属性14
	 */
	@ApiModelProperty(value = "物品批属性14")
	private String skuLot14;
	/**
	 * 物品批属性15
	 */
	@ApiModelProperty(value = "物品批属性15")
	private String skuLot15;
	/**
	 * 物品批属性16
	 */
	@ApiModelProperty(value = "物品批属性16")
	private String skuLot16;
	/**
	 * 物品批属性17
	 */
	@ApiModelProperty(value = "物品批属性17")
	private String skuLot17;
	/**
	 * 物品批属性18
	 */
	@ApiModelProperty(value = "物品批属性18")
	private String skuLot18;
	/**
	 * 物品批属性19
	 */
	@ApiModelProperty(value = "物品批属性19")
	private String skuLot19;
	/**
	 * 物品批属性20
	 */
	@ApiModelProperty(value = "物品批属性20")
	private String skuLot20;
	/**
	 * 物品批属性21
	 */
	@ApiModelProperty(value = "物品批属性21")
	private String skuLot21;
	/**
	 * 物品批属性22
	 */
	@ApiModelProperty(value = "物品批属性22")
	private String skuLot22;
	/**
	 * 物品批属性23
	 */
	@ApiModelProperty(value = "物品批属性23")
	private String skuLot23;
	/**
	 * 物品批属性24
	 */
	@ApiModelProperty(value = "物品批属性24")
	private String skuLot24;
	/**
	 * 物品批属性25
	 */
	@ApiModelProperty(value = "物品批属性25")
	private String skuLot25;
	/**
	 * 物品批属性26
	 */
	@ApiModelProperty(value = "物品批属性26")
	private String skuLot26;
	/**
	 * 物品批属性27
	 */
	@ApiModelProperty(value = "物品批属性27")
	private String skuLot27;
	/**
	 * 物品批属性28
	 */
	@ApiModelProperty(value = "物品批属性28")
	private String skuLot28;
	/**
	 * 物品批属性29
	 */
	@ApiModelProperty(value = "物品批属性29")
	private String skuLot29;
	/**
	 * 物品批属性30
	 */
	@ApiModelProperty(value = "物品批属性30")
	private String skuLot30;
	/**
	 * 系统日志ID
	 */
	@ApiModelProperty(value = "系统日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;


}
