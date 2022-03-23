package org.nodes.wms.statistics.vo;

import lombok.Data;
import org.nodes.wms.core.stock.core.vo.LocRateRltVO;
import org.nodes.wms.core.stock.core.vo.UnsafeStockSkuVO;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.outstock.so.vo.OutstockSkuRltVO;


import java.util.List;

/**
 * @description 所有统计
 *
 * @author pengwei
 * @since 2020-06-22
 */
@Data
public class StatisicsAll {

	private BillCountRltVO soBillCount;

	private BillCountRltVO asnBillCount;

	private SkuCountRltVO outstockSkuCount;

	private IdleSkuRltVO idleSku;

	private BillTraitRltVO billTrait;

	private StockSkuCountRltVO stockSkuCount;

	private LocOccupyRltVO locOccupy;

	private List<OutstockSkuRltVO> outstockSkuList;

	private List<LocRateRltVO> locRate;

	private List<TaskVO> taskList;

	private List<UnsafeStockSkuVO> unsafeStockSkuList;

	private List<AdventSkuVO> adventSkuList;

	private List<SkuOutstockSummaryVO> skuOutstockSummary;

}
