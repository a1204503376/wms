package org.nodes.wms.biz.task;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putway.PutwayStrategyActuator;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.task.factory.PublishJobFactory;
import org.nodes.wms.biz.task.util.SendToScheduleUtil;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingGlobalResponse;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * AGV定时任务
 */
@Service
@RequiredArgsConstructor
public class AgvTimmerTask {

	private static final String POST_JOB_API = "/api/wms/publishJob";

	private final WmsTaskBiz wmsTaskBiz;
	private final WmsTaskDao wmsTaskDao;
	private final LocationBiz locationBiz;
	private final SendToScheduleUtil sendToScheduleUtil;
	private final SystemParamBiz systemParamBiz;
	private final PublishJobFactory publishJobFactory;
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;
	private final PutwayStrategyActuator putwayStrategyActuator;

	protected static Logger logger = LoggerFactory.getLogger(AgvTimmerTask.class);
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * AGV上架拣货移库定时任务
	 */
	public void agvTimmerTask()
	{
		//查询未下发的任务数据
		List<WmsTask> wmsTaskList=wmsTaskDao.getTaskByState(WmsTaskStateEnum.NOT_ISSUED.getCode());
		for (WmsTask wmsTask:wmsTaskList) {
			if(wmsTask.getTaskTypeCd().equals(WmsTaskTypeEnum.AGV_PUTAWAY))//AGV上架
			{
				putSelfTask(wmsTask);
			}
			else if(wmsTask.getTaskTypeCd().equals(WmsTaskTypeEnum.AGV_PICKING) && Func.isNull(wmsTask.getToLocId()))//AGV拣货，目标库位为空
			{
				pickingTask(wmsTask);
			}
			else if(wmsTask.getTaskTypeCd().equals(WmsTaskTypeEnum.AGV_STOCK_MOVE) && Func.isNull(wmsTask.getToLocId()))//AGV库内移位，目标库位为空
			{
				transferLibraryTask(wmsTask);
			}
		}

	}

	/**
	 * AGV上架
	 */
	public void putSelfTask(WmsTask wmsTask)
	{
		if (locationBiz.getUnknowLocation(wmsTask.getWhId())!=null && Func.notNull(wmsTask.getToLocId())) {
			logger.info("定时任务启动,预计启动AGV上架任务开始1=任务ID:"+wmsTask.getTaskId()+"; time="+sdf.format(new Date()));
			// 如果计算得到了目标库位，则发送到调度系统
			if (sendToSchedule(Collections.singletonList(wmsTask))) {
				wmsTask.setTaskState(WmsTaskStateEnum.ISSUED);
			}
			// 更新任务
			wmsTaskDao.updateById(wmsTask);
			//业务日志
			wmsTaskBiz.log(wmsTask);
			logger.info("定时任务启动,预计启动AGV上架任务结束1=任务ID:"+wmsTask.getTaskId()+";"+ WmsTaskStateEnum.ISSUED.getDesc()+"; time="+sdf.format(new Date()));
		}
		else
		{
			List<Stock> stocks=stockQueryBiz.findStockByLocation(wmsTask.getFromLocId());
			if(stocks==null || stocks.size()<=0)
			{
				logger.info("定时任务启动,预计启动AGV上架任务2=任务ID:"+wmsTask.getTaskId()+";库存条数:"+stocks.size()+"; time="+sdf.format(new Date()));
			}
			else {
				logger.info("定时任务启动,预计启动AGV上架任务开始2=任务ID:"+wmsTask.getTaskId()+";库存条数:"+stocks.size()+"; time="+sdf.format(new Date()));
				// 调用上架策略生成目标库位，并把目标库位保存到任务表中
				Location targetLoc = putwayStrategyActuator.run(BigDecimal.ZERO, stocks);
				if (targetLoc != null && !targetLoc.getLocId().equals(locationBiz.getUnknowLocation(stocks.get(0).getWhId()).getLocId())) {
					wmsTask.setToLocId(targetLoc.getLocId());
					wmsTask.setToLocCode(targetLoc.getLocCode());
					// 如果计算得到了目标库位，则发送到调度系统
					if (sendToSchedule(Collections.singletonList(wmsTask))) {
						wmsTask.setTaskState(WmsTaskStateEnum.ISSUED);
					}
					// 调度系统接收成功之后冻结目标库位和冻结原库位的库存
					locationBiz.freezeLocByTask(targetLoc.getLocId(), wmsTask.getTaskId().toString());
					stockBiz.freezeStockByDropId(stocks, wmsTask.getTaskId());
				}
				// 更新任务
				wmsTaskDao.updateById(wmsTask);
				//业务日志
				wmsTaskBiz.log(wmsTask);
				logger.info("定时任务启动,预计启动AGV上架任务结束2=任务ID:"+wmsTask.getTaskId()+";"+ WmsTaskStateEnum.ISSUED.getDesc()+"; time="+sdf.format(new Date()));
			}
		}
	}

	/**
	 * AGV拣货
	 */
		public void pickingTask(WmsTask wmsTask) {
		logger.info("定时任务启动,预计启动AGV拣货任务开始=任务ID:" + wmsTask.getTaskId() + "; time=" + sdf.format(new Date()));
		// 如果计算得到了目标库位，则发送到调度系统
		if (sendToSchedule(Collections.singletonList(wmsTask))) {
			wmsTask.setTaskState(WmsTaskStateEnum.ISSUED);
		}
		// 更新任务
		wmsTaskDao.updateById(wmsTask);
		//业务日志
		wmsTaskBiz.log(wmsTask);
		logger.info("定时任务启动,预计启动AGV拣货任务结束=任务ID:" + wmsTask.getTaskId() + ";" + WmsTaskStateEnum.ISSUED.getDesc() + "; time=" + sdf.format(new Date()));
	}

	/**
		* AGV库内移位
	 */
	public void transferLibraryTask(WmsTask wmsTask) {
		logger.info("定时任务启动,预计启动AGV库内移位任务开始=任务ID:" + wmsTask.getTaskId() + "; time=" + sdf.format(new Date()));
		// 如果计算得到了目标库位，则发送到调度系统
		if (sendToSchedule(Collections.singletonList(wmsTask))) {
			wmsTask.setTaskState(WmsTaskStateEnum.ISSUED);
		}
		// 更新任务
		wmsTaskDao.updateById(wmsTask);
		//业务日志
		wmsTaskBiz.log(wmsTask);
		logger.info("定时任务启动,预计启动AGV库内移位任务结束=任务ID:" + wmsTask.getTaskId() + ";" + WmsTaskStateEnum.ISSUED.getDesc() + "; time=" + sdf.format(new Date()));
	}

	/**
	 * 发送任务到调度系统
	 *
	 * @param putwayTask
	 * @return true:发送成功
	 */
	public boolean sendToSchedule(List<WmsTask> putwayTask) {
		String url = systemParamBiz.findScheduleUrl().concat(POST_JOB_API);

		SchedulingGlobalResponse schedulingGlobalResponse = sendToScheduleUtil.sendPost(url, publishJobFactory.createPublishJobRequestList(putwayTask));
		SchedulingResponse schedulingResponse = schedulingGlobalResponse.getSchedulingResponse();
		return schedulingResponse.hasFailed();
	}
}
