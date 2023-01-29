package org.nodes.wms.biz.common.query.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.query.NodesQueryPlanBiz;
import org.nodes.wms.biz.common.query.factory.NodesQueryPlanFactory;
import org.nodes.wms.dao.common.query.NodesQueryPlanDao;
import org.nodes.wms.dao.common.query.dto.input.AddNodesQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.input.DeleteQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.input.FindAllQueryPlan;
import org.nodes.wms.dao.common.query.dto.input.UpdateQueryPlanRequest;
import org.nodes.wms.dao.common.query.dto.output.FindAllNodesQueryPlan;
import org.nodes.wms.dao.common.query.entities.NodesQueryPlan;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author nodes
 * 查询方案BizImpl
 */
@Service
@RequiredArgsConstructor
public class NodesQueryPlanBizImpl implements NodesQueryPlanBiz {
	private final NodesQueryPlanDao queryPlanDao;
	private final NodesQueryPlanFactory queryPlanFactory;

	@Override
	public List<FindAllNodesQueryPlan> findAll(FindAllQueryPlan queryPlan) {
		List<NodesQueryPlan> queryPlanList = queryPlanDao.getAll(AuthUtil.getUserId(), queryPlan.getPageUrl());
		return BeanUtil.copy(queryPlanList, FindAllNodesQueryPlan.class);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void insert(AddNodesQueryPlanRequest request) {
		NodesQueryPlan nodesQueryPlan = queryPlanFactory.createNodesQueryPlan(request);
		List<NodesQueryPlan> nodesQueryPlans = queryPlanDao.getAll(AuthUtil.getUserId(), nodesQueryPlan.getPageUrl());
		if (nodesQueryPlans.size() >= 5) {
			throw new ServiceException("新增查询方案失败,最多支持5种方案，请删除方案后重试");
		}
		if (nodesQueryPlan.getIsDefault() == 1 && nodesQueryPlans.size() > 0) {
			queryPlanDao.update(request.getPageUrl(), 0);
		}
		queryPlanDao.insert(nodesQueryPlan);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void delete(DeleteQueryPlanRequest request) {
		queryPlanDao.delete(request.getId());
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void update(UpdateQueryPlanRequest request) {
		queryPlanDao.update(request.getPageUrl(), 0);
		queryPlanDao.update(request.getId(), 1);
	}

	@Override
	public void cancel(UpdateQueryPlanRequest request) {
		queryPlanDao.update(request.getPageUrl(), 0);
	}
}
