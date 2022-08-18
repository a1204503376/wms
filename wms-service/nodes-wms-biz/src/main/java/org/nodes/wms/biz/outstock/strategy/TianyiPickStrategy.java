package org.nodes.wms.biz.outstock.strategy;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

/**
 * 天宜定制出库策略
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class TianyiPickStrategy {

    public List<SoPickPlan> run(SoHeader soHeader, SoDetail soDetail,
								List<SoDetail> soDetailList, List<SoPickPlan> existPickPlans) {


    	return null;
    }


}
