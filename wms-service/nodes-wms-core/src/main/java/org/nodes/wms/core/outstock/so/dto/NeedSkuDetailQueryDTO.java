package org.nodes.wms.core.outstock.so.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;

/**
 * @author pengwei
 * @program WmsCore
 * @description 当前订单量明细查询参数
 * @since 2020-08-06
 */
@Data
public class NeedSkuDetailQueryDTO extends SkuLotBaseEntity {

	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	private Long skuId;
	/**
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	private Long wspId;
}
