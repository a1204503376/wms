package org.nodes.wms.biz.task.factory;

import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WmsTaskFactory {
	public WmsTask create(List<Stock> stockList) {
		WmsTask wmsTask = new WmsTask();
		// 任务id
		wmsTask.setTaskId(stockList.get(0).getStockId());
		// 关联单据id
		wmsTask.setBillId(stockList.get(0).getStockId());
		// 关联明细id
		wmsTask.setBillDetailId(stockList.get(0).getStockId());
		// 单据编码为空
		// 任务类型： AGV上架
		wmsTask.setTaskTypeCd(WmsTaskTypeEnum.AGV_PUTAWAY);
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
		// 批次号为空
		// 来源库位id为空
		// 来源库位编码为空
		// 箱码
		wmsTask.setBoxCode(stockList.get(0).getBoxCode());
		// 工作任务包ID为空
		// 库房id
		wmsTask.setWhId(stockList.get(0).getWhId());
		// 工作区ID为空
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
