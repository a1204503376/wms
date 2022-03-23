package org.nodes.core.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.base.entity.DataScope;

import java.util.List;

/**
 * @description DataScopeDTO
 *
 * @author pengwei
 * @since 2020-07-15
 */
@Data
public class DataScopeDTO {

	/**
	 * 菜单ID
	 */
	@ApiModelProperty("菜单ID")
	private Long menuId;

	/**
	 * 数据权限集合
	 */
	@ApiModelProperty("数据权限集合")
	private List<DataScope> dataScopeList;
}
