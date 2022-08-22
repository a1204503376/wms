package org.nodes.wms.biz.task.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
		WmsTask wmsTask = createWmsTask(stockList, WmsTaskProcTypeEnum.BY_LPN_AGV);
		// 任务类型： AGV上架
		wmsTask.setTaskTypeCd(WmsTaskTypeEnum.AGV_PUTAWAY);
		return wmsTask;
	}

	/**
	 * 创建AGV库内移位任务
	 *
	 * @param sourceStock    库存
	 * @param targetLocation 目标库位
	 * @return 库内移位任务
	 */
	public WmsTask createMoveTask(List<Stock> sourceStock, Location targetLocation) {
		WmsTask wmsTask = createWmsTask(sourceStock, WmsTaskProcTypeEnum.BY_LOC_AGV);
		// 任务类型： AGV库内移位
		wmsTask.setTaskTypeCd(WmsTaskTypeEnum.AGV_STOCK_MOVE);
		// 目标库位
		wmsTask.setToLocId(targetLocation.getLocId());
		wmsTask.setToLocCode(targetLocation.getLocCode());
		return wmsTask;
	}

	/**
	 * 创建agv拣货任务，目标库位为空。只有调度系统通过接口查询可用库位时才清楚目标库位
	 *
	 * @param sourceStock 拣货的库存
	 * @param so          出库单
	 * @param soDetail    出库单明细
	 * @return 拣货任务
	 */
	public WmsTask createPickTask(List<Stock> sourceStock, SoHeader so, SoDetail soDetail) {
		WmsTask wmsTask = createWmsTask(sourceStock, WmsTaskProcTypeEnum.BY_LOC_AGV);
		// 单据id、编码，明细id
		wmsTask.setBillId(so.getSoBillId());
		wmsTask.setBillNo(so.getSoBillNo());
		if (Func.notNull(soDetail)) {
			wmsTask.setBillDetailId(soDetail.getSoDetailId());
		}

		// 任务类型： AGV拣货
		wmsTask.setTaskTypeCd(WmsTaskTypeEnum.AGV_PICKING);
		return wmsTask;
	}

	public WmsTask create(WmsTaskTypeEnum taskType, WmsTaskProcTypeEnum procType,
			List<SoPickPlan> soPickPlanList, SoHeader soHeader, BigDecimal planQty) {
		WmsTask wmsTask = new WmsTask();
		wmsTask.setTaskId(IdWorker.getId());
		wmsTask.setLot(soPickPlanList.get(0).getSkuLot1());
		wmsTask.setBillId(soHeader.getSoBillId());
		if (procType.equals(WmsTaskProcTypeEnum.BY_PCS) || procType.equals(WmsTaskProcTypeEnum.BY_PCS_AGV){
			wmsTask.setBillDetailId(soPickPlanList.get(0).getSoDetailId());
		}
		wmsTask.setTaskProcType(procType);
		wmsTask.setTaskState(WmsTaskStateEnum.ISSUED);
		wmsTask.setSkuCode(soPickPlanList.get(0).getSkuCode());
		wmsTask.setTaskQty(planQty);
		wmsTask.setScanQty(BigDecimal.ZERO);
		wmsTask.setUmCode("");
		wmsTask.setFromLocId(soPickPlanList.get(0).getLocId());
		wmsTask.setFromLocCode(soPickPlanList.get(0).getLocCode());
		if (Func.isNotEmpty(soPickPlanList.get(0).getBoxCode())){
			wmsTask.setBoxCode(soPickPlanList.get(0).getBoxCode());
		}
		if (Func.isNotEmpty(soPickPlanList.get(0).getLpnCode())){
			wmsTask.setLpnCode(soPickPlanList.get(0).getLpnCode());
		}
		wmsTask.setTtpId(soPickPlanList.get(0).getPickPlanId());
		wmsTask.setWhId(soHeader.getWhId());
		wmsTask.setWwaId(0L);
		wmsTask.setAllotTime(LocalDateTime.now());
		return wmsTask;
	}

	private WmsTask createWmsTask(List<Stock> stockList, WmsTaskProcTypeEnum proc) {
		WmsTask wmsTask = new WmsTask();
		// 任务id
		wmsTask.setTaskId(IdWorker.getId());
		// 批次号
		wmsTask.setLot(stockList.get(0).getSkuLot1());
		// 关联单据id
		wmsTask.setBillId(stockList.get(0).getStockId());
		// 关联明细id
		wmsTask.setBillDetailId(stockList.get(0).getStockId());
		// 任务执行方式
		wmsTask.setTaskProcType(proc);
		// 任务状态： 未下发
		wmsTask.setTaskState(WmsTaskStateEnum.NOT_ISSUED);
		// 物品编码
		wmsTask.setSkuCode(stockList.get(0).getSkuCode());
		// 数量
		wmsTask.setTaskQty(StockUtil.getStockBalance(stockList));
		// 实际数量
		wmsTask.setScanQty(BigDecimal.ZERO);
		// 计量单位编码
		wmsTask.setUmCode(stockList.get(0).getWsuCode());
		// 来源库位ID
		wmsTask.setFromLocId(stockList.get(0).getLocId());
		// 来源库位编码
		wmsTask.setFromLocCode(stockList.get(0).getLocCode());
		// 批次号
		wmsTask.setLot(stockList.get(0).getSkuLot1());
		// 箱码
		List<String> boxCodes = stockList.stream()
				.map(Stock::getBoxCode).distinct().collect(Collectors.toList());
		wmsTask.setBoxCode(String.join(",", boxCodes));
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
