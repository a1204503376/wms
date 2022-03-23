
package org.nodes.wms.core.basedata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.nodes.core.tool.entity.SkuLotBaseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 物品批属性实体类
 *
 * @author chenhz
 * @since 2019-12-10
 */
@Data
@TableName("wms_sku_lot")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuLot对象", description = "物品批属性")
public class SkuLot extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 物品批属性设置ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wsl_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "物品批属性设置ID")
	private Long wslId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	@NotNull(message = "货主id不能为空")
	private Long woId;
	/**
	 * 物品批属性编码
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品批属性编码")
	@Length(max = 50, message = "物品批属性编码最大长度为50位")
	@NotNull(message = "物品批属性编码不能为空")
	private String skuLotCode;
	/**
	 * 物品批属性名称
	 */
	@ApiModelProperty(value = "物品批属性名称")
	@Length(max = 200, message = "物品批属性名称最大长度为200位")
	@NotNull(message = "物品批属性名称不能为空")
	private String skuLotName;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@Length(max = 300, message = "备注最大长度为300位")
	private String remark;
	/**
	 * 物品批属性1
	 */
	@ApiModelProperty(value = "物品批属性1")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot1;
	/**
	 * 物品批属性2
	 */
	@ApiModelProperty(value = "物品批属性2")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot2;
	/**
	 * 物品批属性3
	 */
	@ApiModelProperty(value = "物品批属性3")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot3;
	/**
	 * 物品批属性4
	 */
	@ApiModelProperty(value = "物品批属性4")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot4;
	/**
	 * 物品批属性5
	 */
	@ApiModelProperty(value = "物品批属性5")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot5;
	/**
	 * 物品批属性6
	 */
	@ApiModelProperty(value = "物品批属性6")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot7;
	/**
	 * 物品批属性7
	 */
	@ApiModelProperty(value = "物品批属性7")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot6;
	/**
	 * 物品批属性8
	 */
	@ApiModelProperty(value = "物品批属性8")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot8;
	/**
	 * 物品批属性9
	 */
	@ApiModelProperty(value = "物品批属性9")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot9;
	/**
	 * 物品批属性10
	 */
	@ApiModelProperty(value = "物品批属性10")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot10;
	/**
	 * 物品批属性11
	 */
	@ApiModelProperty(value = "物品批属性11")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot12;
	/**
	 * 物品批属性12
	 */
	@ApiModelProperty(value = "物品批属性12")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot11;
	/**
	 * 物品批属性13
	 */
	@ApiModelProperty(value = "物品批属性13")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot13;
	/**
	 * 物品批属性14
	 */
	@ApiModelProperty(value = "物品批属性14")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot14;
	/**
	 * 物品批属性15
	 */
	@ApiModelProperty(value = "物品批属性15")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot15;
	/**
	 * 物品批属性16
	 */
	@ApiModelProperty(value = "物品批属性16")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot16;
	/**
	 * 物品批属性17
	 */
	@ApiModelProperty(value = "物品批属性17")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot17;
	/**
	 * 物品批属性18
	 */
	@ApiModelProperty(value = "物品批属性18")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot18;
	/**
	 * 物品批属性19
	 */
	@ApiModelProperty(value = "物品批属性19")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot19;
	/**
	 * 物品批属性20
	 */
	@ApiModelProperty(value = "物品批属性20")
	@Length(max = 200, message = "物品批属性最大长度为200位")
	private String skuLot20;
	/**
	 * 物品批属性21
	 */
	@ApiModelProperty(value = "物品批属性21")
	@Length(max = 200, message = "物品批属性最大长度为200位")
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
	 * 物品批属性标签1
	 */
	@ApiModelProperty(value = "物品批属性标签1")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel1;
	/**
	 * 物品批属性标签2
	 */
	@ApiModelProperty(value = "物品批属性标签2")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel2;
	/**
	 * 物品批属性标签3
	 */
	@ApiModelProperty(value = "物品批属性标签3")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel3;
	/**
	 * 物品批属性标签4
	 */
	@ApiModelProperty(value = "物品批属性标签4")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel4;
	/**
	 * 物品批属性标签5
	 */
	@ApiModelProperty(value = "物品批属性标签5")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel5;
	/**
	 * 物品批属性标签6
	 */
	@ApiModelProperty(value = "物品批属性标签6")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel6;
	/**
	 * 物品批属性标签7
	 */
	@ApiModelProperty(value = "物品批属性标签7")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel7;
	/**
	 * 物品批属性标签8
	 */
	@ApiModelProperty(value = "物品批属性标签8")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel8;
	/**
	 * 物品批属性标签9
	 */
	@ApiModelProperty(value = "物品批属性标签9")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel9;
	/**
	 * 物品批属性标签10
	 */
	@ApiModelProperty(value = "物品批属性标签10")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel10;
	/**
	 * 物品批属性标签11
	 */
	@ApiModelProperty(value = "物品批属性标签11")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel11;
	/**
	 * 物品批属性标签12
	 */
	@ApiModelProperty(value = "物品批属性标签12")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel12;
	/**
	 * 物品批属性标签13
	 */
	@ApiModelProperty(value = "物品批属性标签13")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel13;
	/**
	 * 物品批属性标签14
	 */
	@ApiModelProperty(value = "物品批属性标签14")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel14;
	/**
	 * 物品批属性标签15
	 */
	@ApiModelProperty(value = "物品批属性标签15")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel15;
	/**
	 * 物品批属性标签16
	 */
	@ApiModelProperty(value = "物品批属性标签16")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel16;
	/**
	 * 物品批属性标签17
	 */
	@ApiModelProperty(value = "物品批属性标签17")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel17;
	/**
	 * 物品批属性标签18
	 */
	@ApiModelProperty(value = "物品批属性标签18")
	@Length(max = 200, message = "物品批属性标签最大长度为200位")
	private String skuLotLabel18;
	/**
	 * 物品批属性标签19
	 */
	@ApiModelProperty(value = "物品批属性标签19")
	private String skuLotLabel19;
	/**
	 * 物品批属性标签20
	 */
	@ApiModelProperty(value = "物品批属性标签20")
	private String skuLotLabel20;
	/**
	 * 物品批属性标签21
	 */
	@ApiModelProperty(value = "物品批属性标签21")
	private String skuLotLabel21;
	/**
	 * 物品批属性标签22
	 */
	@ApiModelProperty(value = "物品批属性标签22")
	private String skuLotLabel22;
	/**
	 * 物品批属性标签23
	 */
	@ApiModelProperty(value = "物品批属性标签23")
	private String skuLotLabel23;
	/**
	 * 物品批属性标签24
	 */
	@ApiModelProperty(value = "物品批属性标签24")
	private String skuLotLabel24;
	/**
	 * 物品批属性标签25
	 */
	@ApiModelProperty(value = "物品批属性标签25")
	private String skuLotLabel25;
	/**
	 * 物品批属性标签26
	 */
	@ApiModelProperty(value = "物品批属性标签26")
	private String skuLotLabel26;
	/**
	 * 物品批属性标签27
	 */
	@ApiModelProperty(value = "物品批属性标签27")
	private String skuLotLabel27;
	/**
	 * 物品批属性标签28
	 */
	@ApiModelProperty(value = "物品批属性标签28")
	private String skuLotLabel28;
	/**
	 * 物品批属性标签29
	 */
	@ApiModelProperty(value = "物品批属性标签29")
	private String skuLotLabel29;
	/**
	 * 物品批属性标签30
	 */
	@ApiModelProperty(value = "物品批属性标签30")
	private String skuLotLabel30;


}
