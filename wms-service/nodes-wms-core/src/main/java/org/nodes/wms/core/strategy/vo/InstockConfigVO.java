
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.InstockConfig;

/**
 * 上架策略执行条件视图实体类
 *
 * @author chz
 * @since 2020-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InstockConfigVO对象", description = "上架策略执行条件")
public class InstockConfigVO extends InstockConfig {
	private static final long serialVersionUID = 1L;
	/**
	 * 单据类型描述
	 */
	@ApiModelProperty("单据类型描述")
	private String billTypeIdDesc;

	/**
	 * 物品分类描述
	 */
	@ApiModelProperty("物品分类描述")
	private String skuTypeIdDesc;

}
