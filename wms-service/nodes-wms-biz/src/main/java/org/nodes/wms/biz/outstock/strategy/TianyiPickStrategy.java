package org.nodes.wms.biz.outstock.strategy;

import java.util.List;

import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * 天宜定制出库策略
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class TianyiPickStrategy {

    private final StockQueryBiz stockQueryBiz;

    public List<SoPickPlan> run(SoHeader soHeader, SoDetail soDetail,
            List<SoDetail> soDetailList, List<SoPickPlan> existPickPlans) {

        // 根据发货单明细中的批属性查找所有可用的库存
        //List<Stock> stockList = stockQueryBiz.findEnableStockBySkuLot(skuLot)

        return null;
    }

}
