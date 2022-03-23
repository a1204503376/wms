
package org.nodes.wms.core.warehouse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.Lpn;

/**
 * 容器视图实体类
 *
 * @author liangmei
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LpnVO对象", description = "LpnVO对象")
public class LpnVO extends Lpn {
	private static final long serialVersionUID = 1L;

	/**
	 * 容器类型描述
	 */
	@ApiModelProperty(value = "容器类型描述")
	private String lpnTypeDesc;
}
