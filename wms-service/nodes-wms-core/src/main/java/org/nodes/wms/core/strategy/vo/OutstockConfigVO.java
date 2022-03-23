
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.OutstockConfig;

/**
 * 分配策略执行条件视图实体类
 *
 * @author pengwei
 * @since 2020-02-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockConfigVO对象", description = "分配策略执行条件")
public class OutstockConfigVO extends OutstockConfig {
	private static final long serialVersionUID = 1L;

	/**
	 * 单据类型描述
	 */
	@ApiModelProperty("单据种类编码描述")
	private String billTypeCdDesc;

	/**
	 * 物品分类描述
	 */
	@ApiModelProperty("物品分类描述")
	private String skuTypeIdDesc;
}
