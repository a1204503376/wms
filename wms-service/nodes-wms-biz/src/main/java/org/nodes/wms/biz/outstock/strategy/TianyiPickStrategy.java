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
        // 分别查询人工区和自动区的库存，并分别进行按入库日期进行排序 findEnableStockByZoneTypeAndSkuLot
        // 优先人工区，再自动化区，尽量不拆箱
        // 如果是自动区的则需要根据箱码中生成其它物品的拣货计划

        List<Stock> stockList = stockQueryBiz.findEnableStockBySkuAndSkuLot(soDetail.getSkuId(), skuLot);
        if (Func.isEmpty(stockList)) {
            return null;
        }

        

        return null;
    }

}
