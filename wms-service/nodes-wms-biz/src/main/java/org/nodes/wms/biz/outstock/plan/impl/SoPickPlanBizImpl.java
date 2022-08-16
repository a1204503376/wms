package org.nodes.wms.biz.outstock.plan.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<SoPickPlan> findBySoHeaderId(Long soHeaderId) {
        return null;
    }
}
