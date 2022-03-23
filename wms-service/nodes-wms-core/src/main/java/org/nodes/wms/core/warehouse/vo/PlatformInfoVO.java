
package org.nodes.wms.core.warehouse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;

/**
 * 月台视图实体类
 *
 * @author liangmei
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PlatformInfoVO对象", description = "PlatformInfoVO对象")
public class PlatformInfoVO extends PlatformInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;

	/**
	 * 月台分类描述
	 */
	@ApiModelProperty(value = "月台分类描述")
	private String platformTypeDesc;

}
