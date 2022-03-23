
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.Instock;

import java.util.List;

/**
 * 上架策略 视图实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InstockVO对象", description = "InstockVO对象")
public class InstockVO extends Instock {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 *上架策略明细
	 */
	@ApiModelProperty("上架策略明细")
	private List<InstockDetailVO> instockDetailList;

}
