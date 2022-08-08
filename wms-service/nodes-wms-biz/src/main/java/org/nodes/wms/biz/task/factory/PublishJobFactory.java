package org.nodes.wms.biz.task.factory;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.task.modular.PublishJobRequest;
import org.nodes.wms.dao.basics.sku.entities.Sku;
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
}
