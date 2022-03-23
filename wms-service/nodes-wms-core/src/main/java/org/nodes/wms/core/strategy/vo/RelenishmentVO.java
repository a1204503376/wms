
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.Relenishment;

import java.util.List;

/**
 * 补货策略 视图实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InstockVO对象", description = "InstockVO对象")
public class RelenishmentVO extends Relenishment {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 *补货策略明细
	 */
	@ApiModelProperty("补货策略明细")
	private List<RelenishmentDetailVO> relenishmentDetailList;

}
