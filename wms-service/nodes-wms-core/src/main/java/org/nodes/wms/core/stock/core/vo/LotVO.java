
package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.Lot;

/**
 * 批次号视图实体类
 *
 * @author pengwei
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LotVO对象", description = "批次号")
public class LotVO extends Lot {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	public int hasStock;
}
