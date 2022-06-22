
package org.nodes.wms.core.stock.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.stock.entities.Stock;

/**
 * 库存数据传输对象实体类
 *
 * @author pengwei
 * @since 2019-12-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StockDTO extends Stock {
	private static final long serialVersionUID = 1L;

	/**
	 * 库位编码
	 */
	@ApiModelProperty(value = "库位编码")
	private String locCode;

	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;

	/**
	 * 序列号
	 */
	@ApiModelProperty(value = "序列号")
	private String serialNumber;

	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;

	/**
	 * 最近出库时间范围
	 */
	@ApiModelProperty("最近入库时间范围")
	private String lastInRange;

	/**
	 * 最近入库开始时间
	 */
	@ApiModelProperty("最近入库开始时间")
	private String lastInStart;

	/**
	 * 最近入库结束时间
	 */
	@ApiModelProperty("最近入库结束时间")
	private String lastInEnd;

	/**
	 * 最近出库时间范围
	 */
	@ApiModelProperty("最近出库时间范围")
	private String lastOutRange;

	/**
	 * 最近出库开始时间
	 */
	@ApiModelProperty("最近出库开始时间")
	private String lastOutStart;

	/**
	 * 最近出库结束时间
	 */
	@ApiModelProperty("最近出库结束时间")
	private String lastOutEnd;

	/**
	 * 是否查询待补货的库存
	 */
	@ApiModelProperty("是否查询待补货的库存")
	private String isRelenish;
	/**
	 * 是否条件查询
	 */
	@ApiModelProperty("是否条件查询")
	private String isSearch;
}
