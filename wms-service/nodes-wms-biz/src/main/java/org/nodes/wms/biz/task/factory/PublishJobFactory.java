package org.nodes.wms.biz.task.factory;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.task.dto.input.AgvJobRequest;
import org.nodes.wms.dao.task.dto.input.PublishJobRequest;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送任务工厂类
 **/
@Service
@RequiredArgsConstructor
public class PublishJobFactory {

	private final SkuBiz skuBiz;

	/**
	 * 创建发送任务请求对象
	 *
	 * @param wmsTasks AGV任务
	 * @return 发送任务请求对象
	 */
	public List<PublishJobRequest> createPublishJobRequestList(List<WmsTask> wmsTasks) {
		List<PublishJobRequest> publishJobRequestList = new ArrayList<>();
		wmsTasks.forEach(task -> {
			Sku sku = skuBiz.findByCode(task.getSkuCode());
			PublishJobRequest publishJob = new PublishJobRequest();
			publishJob.setWmsTaskId(task.getTaskId());
			publishJob.setWmsBillNo(task.getBillNo());
			publishJob.setWmsBillType(task.getTaskTypeCd().getCode().toString());
			publishJob.setWmsTaskDetailId(task.getTaskId());
			publishJob.setWmsLineNo("0");
			publishJob.setWmsSkuCode(task.getSkuCode());
			publishJob.setWmsSkuName(sku.getSkuName());
			publishJob.setWmsQty(task.getTaskQty());
			publishJob.setWmsUmCode(task.getUmCode());
			publishJob.setWmsUmName(task.getUmCode());
			publishJob.setJobTypeCode(task.getTaskTypeCd().getCode());
			publishJob.setLocationNameFrom(task.getFromLocCode());
			publishJob.setLocationNameTo(task.getToLocCode());
			publishJob.setBoxCode(task.getBoxCode());
			publishJob.setCBifurcate(task.getCBifurcate());
			publishJobRequestList.add(publishJob);
		});
		return publishJobRequestList;
	}

	/**
	 * 创建AGV任务请求对象
	 *
	 * @param task 工作任务实体
	 * @return AGV继续执行任务请求对象
	 */
	public AgvJobRequest createContinueJobRequest(WmsTask task) {
		AgvJobRequest agvJobRequest = new AgvJobRequest();
		agvJobRequest.setWmsTaskId(task.getTaskId());
		agvJobRequest.setWmsTaskDetailId(task.getTaskId());
		return agvJobRequest;
	}


	/**
	 * @param task 工作任务实体
	 * @return AGV取消任务请求对象
	 */
	public AgvJobRequest createCancelJobRequest(WmsTask task) {
		AgvJobRequest agvJobRequest = new AgvJobRequest();
		agvJobRequest.setWmsTaskId(task.getTaskId());
		agvJobRequest.setWmsTaskDetailId(task.getTaskId());
		return agvJobRequest;
	}
}
