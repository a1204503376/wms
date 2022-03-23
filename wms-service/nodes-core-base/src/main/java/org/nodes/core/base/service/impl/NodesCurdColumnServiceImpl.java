package org.nodes.core.base.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.dto.NodesCurdColumnDTO;
import org.nodes.core.base.entity.NodesCurdColumn;
import org.nodes.core.base.mapper.NodesCurdColumnMapper;
import org.nodes.core.base.service.INodesCurdColumnService;
import org.nodes.core.base.wrapper.NodesCurdColumnWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 列显隐表 服务实现类
 *
 * @author wangYuNan
 * @since 2021-07-06
 */
@Slf4j
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class NodesCurdColumnServiceImpl<M extends NodesCurdColumnMapper, T extends NodesCurdColumn>
	extends BaseServiceImpl<NodesCurdColumnMapper, NodesCurdColumn>
	implements INodesCurdColumnService {


	@Override
	public List<NodesCurdColumn> list(BladeUser bladeUser) {
		//开发者列隐藏数据
		List<NodesCurdColumn> developerColumnList = super.list(new LambdaQueryWrapper<NodesCurdColumn>()
			.eq(NodesCurdColumn::getUserId, "1123598821738675202")
			.eq(NodesCurdColumn::getHide, 0));
		//开发者账号
		if (bladeUser.getRoleName().equals("developer")) {
			//开发者未设置列隐藏
			if (Func.isEmpty(developerColumnList)) {
				return null;
			}
			return super.list(new LambdaQueryWrapper<NodesCurdColumn>()
				.eq(NodesCurdColumn::getUserId, "1123598821738675202"));
		}
		//用户列显示数据
		List<NodesCurdColumn> userColumnList = super.list(new LambdaQueryWrapper<NodesCurdColumn>()
			.eq(NodesCurdColumn::getUserId, bladeUser.getUserId()));
		//开发者未设置列隐藏
		if (Func.isEmpty(developerColumnList)) {
			return userColumnList;
		}
		//排除开发者设置的列隐藏
		developerColumnList.forEach(
			dc -> userColumnList.forEach(
				uc ->
				{
					if (dc.getLabel().equals(uc.getLabel())) {
						userColumnList.remove(uc);
					}
				}
			)
		);
		return userColumnList;
	}

	//用户,当前菜单
	public List<NodesCurdColumn> list1(NodesCurdColumnDTO nodesCurdColumn, BladeUser bladeUser) {
		//开发者列隐藏数据
		List<NodesCurdColumn> developerColumnList = super.list(new LambdaQueryWrapper<NodesCurdColumn>()
			.eq(NodesCurdColumn::getUserId, "1123598821738675202")
			.eq(NodesCurdColumn::getMenuId, nodesCurdColumn.getMenuId())
			.eq(NodesCurdColumn::getHide, 0));
		//开发者账号
		if (bladeUser.getRoleName().equals("developer")) {
			//开发者未设置列隐藏
			if (Func.isEmpty(developerColumnList)) {
				return null;
			}
			return super.list(new LambdaQueryWrapper<NodesCurdColumn>()
				.eq(NodesCurdColumn::getUserId, "1123598821738675202")
				.eq(NodesCurdColumn::getMenuId, nodesCurdColumn.getMenuId()));
		}
		//用户列显示数据
		List<NodesCurdColumn> userColumnList = super.list(new LambdaQueryWrapper<NodesCurdColumn>()
			.eq(NodesCurdColumn::getUserId, nodesCurdColumn.getUserId())
			.eq(NodesCurdColumn::getMenuId, nodesCurdColumn.getMenuId()));
		//开发者未设置列隐藏
		if (Func.isEmpty(developerColumnList)) {
			return userColumnList;
		}
		//排除开发者设置的列隐藏
		developerColumnList.forEach(
			dc -> userColumnList.forEach(
				uc ->
				{
					if (dc.getLabel().equals(uc.getLabel())) {
						userColumnList.remove(uc);
					}
				}
			)
		);
		return userColumnList;
	}

	@Override
	public boolean saveOrUpdate(NodesCurdColumnDTO nodesCurdColumnDTO, BladeUser bladeUser) {
		if (Func.isEmpty(bladeUser.getUserId())) {
			throw new ServiceException("用户id不能为空！");
		}
		List<NodesCurdColumnDTO> columnList = nodesCurdColumnDTO.getColumnList();
		List<NodesCurdColumn> nodesCurdColumns = new ArrayList<>();
		columnList.stream().forEach(
			c -> {
				NodesCurdColumn nodesCurdColumn = NodesCurdColumnWrapper.build().dtoEntity(c);
				nodesCurdColumn.setMenuId(nodesCurdColumnDTO.getMenuId());
				nodesCurdColumn.setUserId(bladeUser.getUserId());
				nodesCurdColumns.add(nodesCurdColumn);
//				super.saveOrUpdate(nodesCurdColumn, new LambdaQueryWrapper<NodesCurdColumn>()
//					.eq(NodesCurdColumn::getUserId, nodesCurdColumn.getUserId())
//					.eq(NodesCurdColumn::getMenuId, nodesCurdColumn.getMenuId())
//					.eq(NodesCurdColumn::getLabel, nodesCurdColumn.getLabel()));
			}
		);
		super.saveOrUpdateBatch(nodesCurdColumns);
		return true;
	}

}
