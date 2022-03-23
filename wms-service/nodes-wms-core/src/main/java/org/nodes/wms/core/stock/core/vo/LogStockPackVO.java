package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.LogStockPack;

import java.math.BigDecimal;

/**
 * @description 尾箱打包日志视图类
 *
 * @author pengwei
 * @since 2020-07-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogStockPackVO对象", description = "打包日志")
public class LogStockPackVO extends LogStockPack {

	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 库区
	 */
	@ApiModelProperty("库区")
	private String zoneName;
	/**
	 * 库位
	 */
	@ApiModelProperty("库位")
	private String locCode;

	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String wsuName;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;

	/**
	 * 操作类型描述
	 */
	@ApiModelProperty("操作类型描述")
	private String procTypeDesc;

	/**
	 * 操作人员名称
	 */
	@ApiModelProperty("操作人员名称")
	private String procUserName;

	/**
	 * 库存数量
	 */
	@ApiModelProperty("库存数量")
	private BigDecimal stockQty;
}
