package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 库存与包装的VO
 * @Author zx
 * @Date 2020/7/30
 **/
@Data
public class StockAndPackageVO {

	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;


	/**
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String skuSpec;

	/**
	 * 计量单位
	 */
	@ApiModelProperty("计量单位")
	private String wsuName;
	/**
	 * 出库单ID
	 */
	@ApiModelProperty("出库单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * WMS出库单编码
	 */
	@ApiModelProperty("WMS出库单编码")
	private String soBillNo;
	/**
	 * 上位系统单号
	 */
	@ApiModelProperty("上位系统单号")
	private String orderNo;
	/**
	 * 客户名称
	 */
	@ApiModelProperty("客户名称")
	private String enterpriseName;
	/**
	 * 订单明细ID
	 */
	@ApiModelProperty("订单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;
	/**
	 * 波次ID
	 */
	@ApiModelProperty("波次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
}
