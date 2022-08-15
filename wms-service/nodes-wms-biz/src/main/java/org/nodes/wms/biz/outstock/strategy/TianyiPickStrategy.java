package org.nodes.wms.biz.outstock.strategy;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.springframework.stereotype.Service;

/**
 * 天宜定制出库策略
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class TianyiPickStrategy {

    public void run(SoHeader soHeader, List<SoDetail> soDetials, List<SoPickPlan> existPickPlans) {

        // 匹配出合适的库存

        // 获取当前可用的拣货计划，根据拣货计划计算还需要分配的数量

        // 从匹配的库存中筛选出合适的库存

        // 形成拣货计划
    }

    private void run(SoHeader soHeader, SoDetail soDetail, List<SoPickPlan> existPickPlans) {

    }
}
