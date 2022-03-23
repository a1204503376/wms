
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.OutstockConfigUdf;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockConfigUdfVO对象", description = "OutstockConfigUdfVO对象")
public class OutstockConfigUdfVO extends OutstockConfigUdf {
	private static final long serialVersionUID = 1L;

	/**
	 * 自定义属性索引
	 */
	@ApiModelProperty(value = "自定义属性索引描述")
	private String billUdfNumberDesc;
	/**
	 * 自定义属性操作符
	 */
	@ApiModelProperty(value = "自定义属性操作符描述")
	private String billUdfOperationDesc;
}
