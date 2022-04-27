package org.nodes.wms.biz.receive.header.modular;


import org.nodes.wms.dao.receive.header.dto.input.NewHeaderRequest;
import org.nodes.wms.dao.receive.header.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.receive.header.entities.ReceiveHeader;
import org.springframework.stereotype.Service;

/**
 * 收货单工厂
 * 主要用于创建收货单实体
 */
@Service
public class ReceiveFactory {
	public ReceiveHeader createReceiveHeader(NewHeaderRequest newHeaderRequest) {
		ReceiveHeader receiveHeader = new ReceiveHeader();
		receiveHeader.setWhId(newHeaderRequest.getWhId());
		receiveHeader.setWhCode(newHeaderRequest.getWhCode());
		receiveHeader.setBillTypeCd(newHeaderRequest.getBillTypeCd());
		receiveHeader.setSCode(newHeaderRequest.getSCode());
		receiveHeader.setSName(newHeaderRequest.getSName());
		receiveHeader.setInstoreType(newHeaderRequest.getInstoreType());
		receiveHeader.setWoId(newHeaderRequest.getWoId());
		receiveHeader.setOwnerCode(newHeaderRequest.getOwnerCode());
		receiveHeader.setRemark(newHeaderRequest.getRemark());
       return receiveHeader;
	}
}
