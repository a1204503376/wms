
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.putway.entities.StInstockConfigLot;

/**
 * 上架策略批属性设定 视图实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InstockConfigLotVO对象", description = "InstockConfigLotVO对象")
public class InstockConfigLotVO extends StInstockConfigLot {
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
