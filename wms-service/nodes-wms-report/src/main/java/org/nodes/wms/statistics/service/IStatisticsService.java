package org.nodes.wms.statistics.service;

import org.nodes.wms.core.stock.core.vo.LocRateRltVO;
import org.nodes.wms.core.stock.core.vo.UnsafeStockSkuVO;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.outstock.so.vo.OutstockSkuRltVO;
import org.nodes.wms.statistics.dto.IdleSkuDTO;
import org.nodes.wms.statistics.vo.*;


import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 统计服务接口
 * @create 20200402
 */
public interface IStatisticsService {
	/**
	 * 当日出库单量/昨天出库单量，当日出库单的物品总数
	 *
	 * @return 统计结果
	 */
	BillCountRltVO soBillCount();

	/**
	 * 当日入库单量/昨天入库单量，当日入库单的物品总数
	 *
	 * @return 统计结果
	 */
	BillCountRltVO asnBillCount();

	/**
	 * 获取当前未发货并且未生成拣货计划的订单中缺货的物品数量
	 *
	 * @return 缺货物品数量
	 */
	SkuCountRltVO outstockSkuCount();

	/**
	 * 获取呆滞物品信息
	 *
	 * @return 统计结果
	 */
	IdleSkuRltVO getIdleSkuInfo(IdleSkuDTO idleSkuDTO);

	/**
	 * 获取一周内订单待征
	 *
	 * @return 统计结果
	 */
	BillTraitRltVO getBillTrait();

	/**
	 * 获取库存物品数量信息
	 *
	 * @return
	 */
	StockSkuCountRltVO getStockSkuCount();

	/**
	 * 获取库位占用信息
	 *
	 * @return
	 */
	LocOccupyRltVO getLocOccupyInfo();

	/**
	 * 获取一周内出库频率前10的物品
	 *
	 * @return
	 */
	List<OutstockSkuRltVO> getOutstockSku();

	/**
	 * 获取一周所有内库位使用频率
	 *
	 * @return
	 */
	List<LocRateRltVO> getLocRate();

	/**
	 * 获取未执行任务信息
	 *
	 * @return
	 */
	List<TaskVO> unExecTaskInfo();

	/**
	 * 获取不在安全库存内的物品列表
	 *
	 * @return
	 */
	List<UnsafeStockSkuVO> getUnsafeStockSkuList();

	/**
	 * 获取临期物品列表
	 *
	 * @return
	 */
	List<AdventSkuVO> getAdventSkuList();

	/**
	 * 获取物品出库总数
	 *
	 * @return 所有物品出库总数
	 */
	List<SkuOutstockSummaryVO> getSkuOutstockSummary();

	/**
	 * 获取所有统计信息
	 *
	 * @return
	 */
	StatisicsAll all();
}
