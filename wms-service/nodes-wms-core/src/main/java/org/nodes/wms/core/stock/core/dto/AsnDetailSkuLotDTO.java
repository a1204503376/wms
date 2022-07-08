package org.nodes.wms.core.stock.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.validation.Add;
import org.nodes.core.tool.validation.Update;

/**
 * 收货单明细批属性dto
 * @Author zx
 * @Date 2020/1/16
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class AsnDetailSkuLotDTO extends SkuLotBaseEntity {
	/**
	 * 物品批属性1
	 */
	@ApiModelProperty(value = "物品批属性1")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot1;
	/**
	 * 物品批属性2
	 */
	@ApiModelProperty(value = "物品批属性2")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot2;
	/**
	 * 物品批属性3
	 */
	@ApiModelProperty(value = "物品批属性3")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot3;
	/**
	 * 物品批属性4
	 */
	@ApiModelProperty(value = "物品批属性4")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot4;
	/**
	 * 物品批属性5
	 */
	@ApiModelProperty(value = "物品批属性5")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot5;
	/**
	 * 物品批属性6
	 */
	@ApiModelProperty(value = "物品批属性6")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot6;
	/**
	 * 物品批属性7
	 */
	@ApiModelProperty(value = "物品批属性7")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot7;
	/**
	 * 物品批属性8
	 */
	@ApiModelProperty(value = "物品批属性8")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot8;
	/**
	 * 物品批属性9
	 */
	@ApiModelProperty(value = "物品批属性9")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot9;
	/**
	 * 物品批属性10
	 */
	@ApiModelProperty(value = "物品批属性10")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot10;
	/**
	 * 物品批属性11
	 */
	@ApiModelProperty(value = "物品批属性11")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot11;
	/**
	 * 物品批属性12
	 */
	@ApiModelProperty(value = "物品批属性12")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot12;
	/**
	 * 物品批属性13
	 */
	@ApiModelProperty(value = "物品批属性13")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot13;
	/**
	 * 物品批属性14
	 */
	@ApiModelProperty(value = "物品批属性14")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot14;
	/**
	 * 物品批属性15
	 */
	@ApiModelProperty(value = "物品批属性15")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot15;
	/**
	 * 物品批属性16
	 */
	@ApiModelProperty(value = "物品批属性16")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot16;
	/**
	 * 物品批属性17
	 */
	@ApiModelProperty(value = "物品批属性17")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot17;
	/**
	 * 物品批属性18
	 */
	@ApiModelProperty(value = "物品批属性18")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot18;
	/**
	 * 物品批属性19
	 */
	@ApiModelProperty(value = "物品批属性19")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot19;
	/**
	 * 物品批属性20
	 */
	@ApiModelProperty(value = "物品批属性20")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot20;
	/**
	 * 物品批属性21
	 */
	@ApiModelProperty(value = "物品批属性21")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot21;
	/**
	 * 物品批属性22
	 */
	@ApiModelProperty(value = "物品批属性22")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot22;
	/**
	 * 物品批属性23
	 */
	@ApiModelProperty(value = "物品批属性23")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot23;
	/**
	 * 物品批属性24
	 */
	@ApiModelProperty(value = "物品批属性24")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot24;
	/**
	 * 物品批属性25
	 */
	@ApiModelProperty(value = "物品批属性25")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot25;
	/**
	 * 物品批属性26
	 */
	@ApiModelProperty(value = "物品批属性26")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot26;
	/**
	 * 物品批属性27
	 */
	@ApiModelProperty(value = "物品批属性27")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot27;
	/**
	 * 物品批属性28
	 */
	@ApiModelProperty(value = "物品批属性28")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot28;
	/**
	 * 物品批属性29
	 */
	@ApiModelProperty(value = "物品批属性29")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot29;
	/**
	 * 物品批属性30
	 */
	@ApiModelProperty(value = "物品批属性30")
	@Length(max = 200,message = "最大长度为200位", groups = {Add.class, Update.class})
	private String skuLot30;

}
