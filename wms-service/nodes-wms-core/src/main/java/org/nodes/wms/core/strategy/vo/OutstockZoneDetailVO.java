
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.OutstockZoneDetail;

/**
 * 分配策略货源库区视图实体类
 *
 * @author pengwei
 * @since 2020-02-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockZoneDetailVO对象", description = "分配策略货源库区")
public class OutstockZoneDetailVO extends OutstockZoneDetail {
	private static final long serialVersionUID = 1L;
	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 货源库区名称
	 */
	@ApiModelProperty(value = "货源库区名称")
	private String zoneName;
	/**
	 * 层级描述
	 */
	private String skuLevelDesc;
}
