
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.OutstockConfigLot;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockConfigLotVO对象", description = "OutstockConfigLotVO对象")
public class OutstockConfigLotVO extends OutstockConfigLot {
	private static final long serialVersionUID = 1L;

	/**
	 * 批属性索引描述
	 */
	@ApiModelProperty("批属性索引描述")
	private String skuLotNumberDesc;

	/**
	 * 批属性操作符描述
	 */
	@ApiModelProperty("批属性操作符描述")
	private String skuLotOperationDesc;
}
