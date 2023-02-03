package org.nodes.wms.dao.common.query.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.common.query.NodesQueryPlanDao;
import org.nodes.wms.dao.common.query.entities.NodesQueryPlan;
import org.nodes.wms.dao.common.query.mapper.NodesQueryPlanMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.utils.AuthUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author nodes
 * 查询任务daoImpl
 */
@Repository
@RequiredArgsConstructor
public class NodesQueryPlanDaoImpl extends BaseServiceImpl<NodesQueryPlanMapper, NodesQueryPlan> implements NodesQueryPlanDao {
	@Override
	public List<NodesQueryPlan> getAll(Long createUser, String pageUrl) {
		LambdaQueryChainWrapper<NodesQueryPlan> lambdaQuery = super.lambdaQuery()
			.eq(NodesQueryPlan::getCreateUser, createUser)
			.eq(NodesQueryPlan::getPageUrl, pageUrl);
		return lambdaQuery.list();
	}

	@Override
	public void insert(NodesQueryPlan nodesQueryPlan) {
		if (!super.save(nodesQueryPlan)) {
			throw new ServiceException("新增查询方案失败,请再次重试");
		}
	}

	@Override
	public void delete(Long id) {
		if (!super.removeById(id)) {
			throw new ServiceException("删除查询方案失败,请再次重试");
		}
	}

	@Override
	public void update(Long id, Integer isDefault) {
		UpdateWrapper<NodesQueryPlan> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(NodesQueryPlan::getId, id);

		NodesQueryPlan nodesQueryPlan = new NodesQueryPlan();
		nodesQueryPlan.setIsDefault(isDefault);
		if (!super.update(nodesQueryPlan, updateWrapper)) {
			throw new ServiceException("更新当前查询方案为默认方案失败,请再次重试");
		}
	}

	@Override
	public void update(String pageUrl, Integer isDefault) {
		UpdateWrapper<NodesQueryPlan> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(NodesQueryPlan::getPageUrl, pageUrl)
			.eq(NodesQueryPlan::getCreateUser, AuthUtil.getUserId());

		NodesQueryPlan nodesQueryPlan = new NodesQueryPlan();
		nodesQueryPlan.setIsDefault(isDefault);
		if (!super.update(nodesQueryPlan, updateWrapper)) {
			throw new ServiceException("更新查询方案失败,请再次重试");
		}
	}
}
