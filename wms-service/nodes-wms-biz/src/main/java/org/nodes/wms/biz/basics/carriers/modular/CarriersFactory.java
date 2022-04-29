package org.nodes.wms.biz.basics.carriers.modular;

import org.nodes.wms.dao.basics.carriers.dto.input.NewCarrierRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.UpdateStatusRequest;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;
import org.springframework.stereotype.Service;

/**
 * 承运商管理工厂类
 */
@Service
public class CarriersFactory {
	public BasicsCarriers createCarriers(NewCarrierRequest newCarrierRequest) {
		BasicsCarriers basicsCarriers = new BasicsCarriers();
		basicsCarriers.setCode(newCarrierRequest.getCode());
		basicsCarriers.setName(newCarrierRequest.getName());
		basicsCarriers.setSimpleName(newCarrierRequest.getSimpleName());
		basicsCarriers.setStatus(newCarrierRequest.getStatus());
		basicsCarriers.setWoId(newCarrierRequest.getWoId());
		basicsCarriers.setRemark(newCarrierRequest.getRemark());
		return  basicsCarriers;
	}
	public BasicsCarriers createCarriers(UpdateStatusRequest updateStatusRequest) {
		BasicsCarriers basicsCarriers = new BasicsCarriers();
		basicsCarriers.setStatus(updateStatusRequest.getStatus());
        basicsCarriers.setId(updateStatusRequest.getId());
		return  basicsCarriers;
	}
}
