
package org.nodes.wms.core.warehouse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.WorkArea;

/**
 * 工作区视图实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkAreaVO对象", description = "WorkAreaVO对象")
public class WorkAreaVO extends WorkArea {
	private static final long serialVersionUID = 1L;
	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;

	/**
	 * 是否启用描述
	 */
	@ApiModelProperty(value = "是否启用描述")
	private String statusDesc;
}
