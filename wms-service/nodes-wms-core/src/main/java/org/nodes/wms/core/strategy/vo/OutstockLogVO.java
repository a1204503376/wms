package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.OutstockLog;

/**
 * 分配记录视图实体类
 *
 * @author NodeX
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockLogVO对象", description = "分配记录")
public class OutstockLogVO extends OutstockLog {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 分配计算描述
	 */
	@ApiModelProperty("分配计算描述")
	private String outstockFunctionDesc;

	/**
	 * 是否成功描述
	 */
	@ApiModelProperty("是否成功描述")
	private String successDesc;
}
