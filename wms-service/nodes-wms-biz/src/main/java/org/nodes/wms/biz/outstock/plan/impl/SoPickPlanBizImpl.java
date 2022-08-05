package org.nodes.wms.biz.outstock.plan.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.springframework.stereotype.Service;

/**
 * 拣货计划相关业务
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class SoPickPlanBizImpl implements SoPickPlanBiz {

	@Override
	public boolean hasPickPlan(Long soHeaderId) {
		return false;
	}
}
