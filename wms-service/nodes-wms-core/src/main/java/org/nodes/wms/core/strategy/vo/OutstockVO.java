
package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.Outstock;

import java.util.List;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockVO对象", description = "OutstockVO对象")
public class OutstockVO extends Outstock {
	private static final long serialVersionUID = 1L;
	//创建人姓名
	private String createByName;
	//更新人姓名
	private String updateByName;

	@ApiModelProperty(value = "分配策略明细信息")
	private List<OutstockDetailVO> outstockDetailVO;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
}
