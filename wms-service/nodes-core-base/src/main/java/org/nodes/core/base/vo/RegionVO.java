package org.nodes.core.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.aspectj.weaver.Dump;
import org.nodes.core.base.entity.Region;
import org.springblade.core.tool.node.INode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "regionVO对象", description = "区域VO对象")
public class RegionVO extends Region {
	@ApiModelProperty(value = "是否有子节点")
	private boolean hasChildren;
}
