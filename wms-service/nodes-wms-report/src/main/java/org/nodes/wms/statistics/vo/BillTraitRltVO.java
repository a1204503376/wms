package org.nodes.wms.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pengwei
 * @program WmsCore
 * @description 订单特征
 * @create 20200407
 */
@Data
public class BillTraitRltVO {

	/**
	 * 序列号管理物品数量
	 */
	@ApiModelProperty("序列号管理物品数量")
	private Integer snCount;

	/**
	 * 非序列号管理物品数量
	 */
	@ApiModelProperty("非序列号管理物品数量")
	private Integer notSnCount;
}
