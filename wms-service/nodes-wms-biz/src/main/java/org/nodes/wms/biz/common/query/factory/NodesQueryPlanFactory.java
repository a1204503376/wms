package org.nodes.wms.biz.common.query.factory;

import org.nodes.wms.dao.common.query.dto.input.AddNodesQueryPlanRequest;
import org.nodes.wms.dao.common.query.entities.NodesQueryPlan;
import org.springframework.stereotype.Service;

/**
 * @author nodes
 */
@Service
public class NodesQueryPlanFactory {
	public NodesQueryPlan createNodesQueryPlan(AddNodesQueryPlanRequest request) {
		NodesQueryPlan queryPlan = new NodesQueryPlan();
		queryPlan.setName(request.getName());
		queryPlan.setIsDefault(request.getIsDefault());
		queryPlan.setIsInitData(request.getIsInitData());
		queryPlan.setPageUrl(request.getPageUrl());
		queryPlan.setQueryData(request.getQueryData());
		return queryPlan;
	}
}
