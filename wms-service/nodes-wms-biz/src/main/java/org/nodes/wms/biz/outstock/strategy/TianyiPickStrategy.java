package org.nodes.wms.biz.outstock.strategy;

import java.util.List;

import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
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
        SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
        SkuLotUtil.setAllSkuLot(soDetail, skuLot);
        List<Stock> stockList = stockQueryBiz.findEnableStockBySkuAndSkuLot(soDetail.getSkuId(), skuLot);
        if (Func.isEmpty(stockList)) {
            return null;
        }

        

        return null;
    }

}
