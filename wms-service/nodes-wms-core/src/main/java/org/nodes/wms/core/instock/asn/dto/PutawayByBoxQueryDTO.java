package org.nodes.wms.core.instock.asn.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.core.tool.validation.Query;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 按箱上架查询VO
 *
 * @Author zx
 * @Date 2020/6/29
 **/
@Data
public class PutawayByBoxQueryDTO {
	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@NotNull(message = "库房ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	@ApiModelProperty("物品ID")
	@NotNull(groups = {Query.class}, message = "物品ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	@ApiModelProperty("库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;


	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	@NotEmpty(groups = {Query.class}, message = "物品编码不能为空")
	private String skuCode;
	/**
	 * 库位编码
	 */
	@ApiModelProperty("库位编码")
	@NotNull(groups = {Query.class}, message = "库位编码不能为空")
	private String locCode;

	/**
	 * 物品数量
	 */
	@ApiModelProperty("物品数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@Range(min = 1, message = "请输入有效的数量,数量大于0")
	private BigDecimal qty;

	/**
	 * 批属性
	 */
	@ApiModelProperty("批属性")
	private AsnDetailSkuLotDTO skuLots;

}
