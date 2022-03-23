
package org.nodes.wms.core.warehouse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.Location;

/**
 * 库位管理 视图实体类
 *
 * @author zhongls
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LocationVO对象", description = "LocationVO对象")
public class LocationVO extends Location {
	private static final long serialVersionUID = 1L;
	/**
	 * 库区名称
	 */
	private String zoneName;
	/**
	 * 仓库名称
	 */
	private String whName;

	/**
	 * 应用类型描述
	 */
	@ApiModelProperty("应用类型描述")
	private String locTypeDesc;

	/**
	 * 库位种类描述
	 */
	@ApiModelProperty("库位种类描述")
	private String locCategoryDesc;

	/**
	 * 库位处理描述
	 */
	@ApiModelProperty("库位处理描述")
	private String locHandlingDesc;

	/**
	 * 使用状态描述
	 */
	@ApiModelProperty("使用状态描述")
	private String locFlagDesc;

	/**
	 * ABC分类描述
	 */
	@ApiModelProperty("ABC分类描述")
	private String abcDesc;

	/**
	 * 启用状态描述
	 */
	@ApiModelProperty("启用状态描述")
	private String isActiveDesc;

}
