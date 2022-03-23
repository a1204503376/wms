package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 按箱移动提交VO
 * @Author zx
 * @Date 2020/7/30
 **/
@Data
public class StockMoveByBoxSubmitDTO {

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
	 * 单位名称
	 */
	@ApiModelProperty("单位名称")
	private String wsuName;

	/**
	 * 移动数量
	 */
	@ApiModelProperty("移动数量")
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal moveQty;

	/**
	 * 源库位ID
	 */
	@ApiModelProperty("源库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sourceLocId;

	/**
	 * 源容器编码
	 */
	@ApiModelProperty("源容器编码")
	private String sourceLpnCode;

	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 目的库位编码
	 */
	@ApiModelProperty("目的库位编码")
	private String targetLocCode;

	/**
	 * 目的容器编码
	 */
	@ApiModelProperty("目的容器编码")
	private String targetLpnCode;


	/**
	 * 系统日志用唯一标识
	 */
	private String billNo;

}
