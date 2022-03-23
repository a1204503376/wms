package org.nodes.core.base.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.base.entity.NodesCurdColumn;

import java.util.List;

/**
 * 列显隐表数据传输对象实体类
 *
 * @author wangYunan
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NodesCurdColumnDTO extends NodesCurdColumn {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货单明细
	 */
	@ApiModelProperty(value = "列显隐数据")
	private List<NodesCurdColumnDTO> columnList;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer order;

}
