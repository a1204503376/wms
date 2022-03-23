package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.stock.core.entity.StockSnapshoot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 库存快照视图实体类
 *
 * @author NodeX
 * @since 2021-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockSnapshootVO对象", description = "库存快照")
public class StockSnapshootVO extends StockSnapshoot {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "库存快照物品层级名称")
	private String skuLevelName;
}
