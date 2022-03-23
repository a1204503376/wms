package org.nodes.core.base.wrapper;

import org.nodes.core.base.dto.NodesCurdColumnDTO;
import org.nodes.core.base.entity.NodesCurdColumn;
import org.nodes.core.base.vo.NodesCurdColumnVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;


import org.springblade.core.tool.utils.Func;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 列显隐表包装类,返回视图层所需的字段
 *
 * @author wangYunan
 * @since 2021-07-06
 */
public class NodesCurdColumnWrapper extends BaseEntityWrapper<NodesCurdColumn, NodesCurdColumnVO> {

	public static NodesCurdColumnWrapper build() {
		return new NodesCurdColumnWrapper();
 	}

	@Override
	public NodesCurdColumnVO entityVO(NodesCurdColumn nodesCurdColumn) {
		NodesCurdColumnVO nodesCurdColumnVO = BeanUtil.copy(nodesCurdColumn, NodesCurdColumnVO.class);
        if (Func.isNotEmpty(nodesCurdColumnVO)) {

        }
		return nodesCurdColumnVO;
	}

	public NodesCurdColumn dtoEntity(NodesCurdColumnDTO nodesCurdColumnDTO) {
		NodesCurdColumn nodesCurdColumn = BeanUtil.copy(nodesCurdColumnDTO, NodesCurdColumn.class);
		if (Func.isNotEmpty(nodesCurdColumn)) {
			if ("true".equals(nodesCurdColumnDTO.getHide()) ){
				nodesCurdColumn.setHide("0");
			}else {
				nodesCurdColumn.setHide("1");
			}
			if ("true".equals(nodesCurdColumnDTO.getFixed()) ){
				nodesCurdColumn.setFixed("0");
			}else {
				nodesCurdColumn.setFixed("1");
			}
			nodesCurdColumn.setSort(nodesCurdColumnDTO.getOrder());
		}
		return nodesCurdColumn;
	}


	public List<NodesCurdColumn> listDtoEntity(List<NodesCurdColumnDTO> list) {
		return (List)list.stream().map(this::dtoEntity).collect(Collectors.toList());
	}
}
