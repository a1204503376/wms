
package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.StockOccupy;

/**
 * 库存占用表视图实体类
 *
 * @author pengwei
 * @since 2020-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockOccupyVO对象", description = "库存占用表")
public class StockOccupyVO extends StockOccupy {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("占用类型名称")
	private String occupyTypeName;

	@ApiModelProperty("库房名称")
	private String whName;

}
