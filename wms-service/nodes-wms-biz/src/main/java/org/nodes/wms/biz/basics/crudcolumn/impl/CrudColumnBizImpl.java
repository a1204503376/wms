package org.nodes.wms.biz.basics.crudcolumn.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.crudcolumn.CrudColumnBiz;
import org.nodes.wms.dao.basics.crudcolumn.CrudColumnDao;
import org.nodes.wms.dao.basics.crudcolumn.dto.CrudColumnRequest;
import org.nodes.wms.dao.basics.crudcolumn.dto.CrudColumnResponse;
import org.nodes.wms.dao.basics.crudcolumn.entities.NodesCurdColumn;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 列显隐 业务实现类
 */
@Service
@RequiredArgsConstructor
public class CrudColumnBizImpl implements CrudColumnBiz {

	private final CrudColumnDao crudColumnDao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean deleteBeforeSaving(Collection<CrudColumnRequest> crudColumnRequestCollection) {
		AssertUtil.notEmpty(crudColumnRequestCollection);
		List<NodesCurdColumn> nodesCurdColumnList = Func.copy(crudColumnRequestCollection, NodesCurdColumn.class);
		Long userId = AuthUtil.getUserId();
		nodesCurdColumnList.forEach(item -> item.setUserId(userId));
		Set<Long> menuIdSet = nodesCurdColumnList.stream()
			.collect(Collectors.groupingBy(NodesCurdColumn::getMenuId))
			.keySet();
		AssertUtil.isFalse(menuIdSet.size() != 1, "只允许存在唯一的菜单ID");

		crudColumnDao.delete(userId, menuIdSet.iterator().next());
		crudColumnDao.insert(nodesCurdColumnList);

		return true;
	}

	@Override
	public List<CrudColumnResponse> getCrudColumnResponseList(Long menuId) {
		List<NodesCurdColumn> nodesCurdColumnList = crudColumnDao.listByUserIdMenuId(AuthUtil.getUserId(), menuId);
		return Func.copy(nodesCurdColumnList, CrudColumnResponse.class);
	}
}
