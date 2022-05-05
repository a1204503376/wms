package org.nodes.wms.dao.basics.crudcolumn.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.wms.dao.basics.crudcolumn.CrudColumnDao;
import org.nodes.wms.dao.basics.crudcolumn.entities.NodesCurdColumn;
import org.nodes.wms.dao.basics.crudcolumn.mapper.NodesCurdColumnMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 列显隐 DAO实现类
 */
@Repository
public class CrudColumnDaoImpl
	extends BaseServiceImpl<NodesCurdColumnMapper, NodesCurdColumn>
	implements CrudColumnDao {

	@Override
	public boolean insert(Collection<NodesCurdColumn> nodesCurdColumnCollection) {
		return super.saveBatch(nodesCurdColumnCollection, nodesCurdColumnCollection.size());
	}

	@Override
	public boolean delete(Long userId, Long menuId) {
		return super.remove(getWrapperByUserIdMenuId(userId, menuId));
	}


	@Override
	public List<NodesCurdColumn> listByUserIdMenuId(Long userId, Long menuId) {
		return super.list(getWrapperByUserIdMenuId(userId, menuId)
			.select(NodesCurdColumn::getMenuId,
				NodesCurdColumn::getProp,
				NodesCurdColumn::getLabel,
				NodesCurdColumn::getAliasName,
				NodesCurdColumn::getHide,
				NodesCurdColumn::getFixed,
				NodesCurdColumn::getWidth,
				NodesCurdColumn::getAlign,
				NodesCurdColumn::getSort));
	}

	/**
	 * 获取根据用户ID和菜单ID的条件对象
	 *
	 * @param userId 用户ID
	 * @param menuId 菜单ID
	 * @return LambdaQueryChainWrapper<T>
	 */
	private LambdaQueryWrapper<NodesCurdColumn> getWrapperByUserIdMenuId(Long userId, Long menuId) {
		return Wrappers.lambdaQuery(NodesCurdColumn.class)
			.eq(NodesCurdColumn::getUserId, userId)
			.eq(NodesCurdColumn::getMenuId, menuId);
	}
}
