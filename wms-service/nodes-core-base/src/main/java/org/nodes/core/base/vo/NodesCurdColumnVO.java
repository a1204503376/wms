package org.nodes.core.base.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.core.base.entity.NodesCurdColumn;

/**
 * 列显隐表视图实体类
 *
 * @author wangYunan
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "NodesCurdColumnVO对象", description = "列显隐表")
public class NodesCurdColumnVO extends NodesCurdColumn {
	private static final long serialVersionUID = 1L;

}
