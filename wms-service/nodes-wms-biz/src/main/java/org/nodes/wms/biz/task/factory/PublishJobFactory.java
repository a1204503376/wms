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
	 * @param putwayTask AGV任务
	 * @return 发送任务请求对象
	 */
	public List<PublishJobRequest> createPublishJobRequestList(List<WmsTask> putwayTask){
		List<PublishJobRequest> publishJobRequestList = new ArrayList<>();
		putwayTask.forEach(task -> {
			Sku sku  = skuBiz.findByCode(task.getSkuCode());
			PublishJobRequest publishJob = new PublishJobRequest();
			publishJob.setWmsBillId(task.getBillId());
			publishJob.setWmsBillNo(task.getBillNo());
			publishJob.setWmsBillType("上架");
			publishJob.setWmsDetailId(task.getBillDetailId());
			publishJob.setWmsLineNo("0");
			publishJob.setWmsSkuCode(task.getSkuCode());
			publishJob.setWmsSkuName(sku.getSkuName());
			publishJob.setWmsQty(task.getTaskQty());
			publishJob.setWmsUmCode(task.getUmCode());
			publishJob.setWmsUmName(task.getUmCode());
			publishJob.setJobType(task.getTaskTypeCd().getCode());
			publishJob.setLocationNameFrom(task.getFromLocCode());
			publishJob.setLocationNameTo(task.getToLocCode());

			publishJobRequestList.add(publishJob);
		});
		return publishJobRequestList;
	}

	/**
	 * 创建AGV任务请求对象
	 *
	 * @param tasks AGV任务
	 * @return AGV继续执行任务请求对象
	 */
	public AgvJobRequest createContinueJobRequest(List<WmsTask> tasks) {
		AgvJobRequest agvJobRequest = new AgvJobRequest();
		agvJobRequest.setWmsTaskId(tasks.get(0).getTaskId());
		agvJobRequest.setWmsTaskDetailId(tasks.get(0).getTaskId());
		return agvJobRequest;
	}

	/**
	 * 创建AGV取消任务请求对象
	 *
	 * @return AGV取消任务请求对象
	 */
    public AgvJobRequest createCancelJobRequest() {
		AgvJobRequest agvJobRequest = new AgvJobRequest();
		agvJobRequest.setWmsTaskId(0L);
		agvJobRequest.setWmsTaskDetailId(0L);
		return agvJobRequest;
    }
}
