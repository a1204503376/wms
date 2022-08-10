package org.nodes.wms.biz.task.factory;

import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author nodesc
 */
@Service
public class WmsTaskFactory {

	/**
	 * 创建AGV上架任务
	 *
	 * @param stockList 库存
	 * @return 上架任务
	 */
	public WmsTask createPutwayTask(List<Stock> stockList) {
		WmsTask wmsTask = getWmsTask(stockList);
		// 任务类型： AGV上架
		wmsTask.setTaskTypeCd(WmsTaskTypeEnum.AGV_PUTAWAY);
		return wmsTask;
	}

	/**
	 * 创建AGV库内移位任务
	 *
	 * @param sourceStock 库存
	 * @param targetLocId 目标库位id
	 * @return 库内移位任务
	 */
	public WmsTask createMoveTask(List<Stock> sourceStock, Long targetLocId) {
		WmsTask wmsTask = getWmsTask(sourceStock);
		// 任务类型： AGV库内移位
		wmsTask.setTaskTypeCd(WmsTaskTypeEnum.AGV_STOCK_MOVE);
		// 目标库位
		wmsTask.setToLocId(targetLocId);
		return wmsTask;
	}

	/**
	 * 创建拣货任务，目标库位为空。只有调度系统通过接口查询可用库位时才清楚目标库位
	 *
	 * @param sourceStock 拣货的库存
	 * @param so          出库单
	 * @param soDetail    出库单明细
	 * @return 拣货任务
	 */
	public WmsTask createPickTask(List<Stock> sourceStock, SoHeader so, SoDetail soDetail) {
		WmsTask wmsTask = getWmsTask(sourceStock);
		// 单据id、编码，明细id
		wmsTask.setBillId(so.getSoBillId());
		wmsTask.setBillNo(so.getSoBillNo());
		wmsTask.setBillDetailId(soDetail.getSoDetailId());
		// 任务类型： AGV拣货
		wmsTask.setTaskTypeCd(WmsTaskTypeEnum.AGV_PICKING);
		return wmsTask;
	}

	/**
	 * 赋值
	 */
	private WmsTask getWmsTask(List<Stock> stockList) {
		WmsTask wmsTask = new WmsTask();
		// 任务id
		wmsTask.setTaskId(stockList.get(0).getStockId());
		// 关联单据id
		wmsTask.setBillId(stockList.get(0).getStockId());
		// 关联明细id
		wmsTask.setBillDetailId(stockList.get(0).getStockId());
		// 任务执行方式
		wmsTask.setTaskProcType(0);
		// 任务状态： 未下发
		wmsTask.setTaskState(WmsTaskStateEnum.NOT_ISSUED);
		// 物品编码
		wmsTask.setSkuCode(stockList.get(0).getSkuCode());
		// 数量
		wmsTask.setTaskQty(StockUtil.getStockBalance(stockList));
		// 实际数量
		wmsTask.setScanQty(StockUtil.getStockBalance(stockList));
		// 计量单位编码
		wmsTask.setUmCode(stockList.get(0).getWsuCode());
		//来源库位ID
		wmsTask.setFromLocId(stockList.get(0).getLocId());
		//来源库位编码
		wmsTask.setFromLocCode(stockList.get(0).getLocCode());
		// 批次号为空
		// 箱码
		wmsTask.setBoxCode(stockList.get(0).getBoxCode());
		// 工作任务包ID
		wmsTask.setTtpId(stockList.get(0).getStockId());
		// 库房id
		wmsTask.setWhId(stockList.get(0).getWhId());
		// lpn
		wmsTask.setLpnCode(stockList.get(0).getLpnCode());
		// 工作区ID为空
		wmsTask.setWwaId(stockList.get(0).getStockId());
		// 用户ID为空
		// 用户编码为空
		// 用户名称为空
		// 任务分组编码为空
		// 任务描述为空
		// 任务分派时间
		wmsTask.setAllotTime(LocalDateTime.now());
		// 任务优先时间为空
		// 任务开始执行事件为空
		// 任务关闭时间为空
		return wmsTask;
	}
}
