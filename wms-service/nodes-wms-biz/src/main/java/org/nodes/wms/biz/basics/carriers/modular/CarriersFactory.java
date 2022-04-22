package org.nodes.wms.biz.basics.carriers.modular;

import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;
import org.springframework.stereotype.Service;

/**
 * 承运商管理工厂类
 */
@Service
public class CarriersFactory {
	public BasicsCarriers createCarriers(CarriersRequest carriersRequest) {
		BasicsCarriers basicsCarriers = new BasicsCarriers();
		basicsCarriers.setId(carriersRequest.getId());
		basicsCarriers.setCode(carriersRequest.getCode());
		basicsCarriers.setName(carriersRequest.getName());
		basicsCarriers.setSimpleName(carriersRequest.getSimpleName());
		basicsCarriers.setStatus(carriersRequest.getStatus());
		basicsCarriers.setWoId(carriersRequest.getWoId());
		basicsCarriers.setRemark(carriersRequest.getRemark());
		return  basicsCarriers;
	}
}
