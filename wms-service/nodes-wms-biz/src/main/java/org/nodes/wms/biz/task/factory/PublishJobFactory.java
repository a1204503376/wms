package org.nodes.wms.biz.task.factory;

import org.nodes.wms.biz.task.modular.PublishJobRequest;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送任务工厂类
 **/
@Service
public class PublishJobFactory {

	public List<PublishJobRequest> createPublishJobRequestList(List<WmsTask> putwayTask){
		List<PublishJobRequest> publishJobRequestList = new ArrayList<>();
		putwayTask.forEach(task -> {
			PublishJobRequest publishJob = new PublishJobRequest();
			// TODO 赋值未写全
			publishJob.setWmsBillId(task.getBillId());
			publishJob.setWmsBillNo(task.getBillNo());
//			publishJob.setWmsBillType(task.getbi); wmsBillType
			publishJob.setWmsDetailId(task.getBillDetailId());
//			publishJob.setWmsLineNo(task.getl);  wmsLineNo
			publishJob.setWmsSkuCode(task.getSkuCode());
//			publishJob.setWmsSkuName(task.); wmsSkuName
			publishJob.setWmsQty(task.getTaskQty());// scanQty
			publishJob.setWmsUmCode(task.getUmCode());
//			publishJob.setWmsUmName(task.getum); //wmsUmName
			publishJob.setJobType(task.getTaskState().getCode());// ?
			publishJob.setLocationNameFrom(null);
			publishJob.setLocationNameTo(null);

			publishJobRequestList.add(publishJob);
		});
		return publishJobRequestList;
	}
}
