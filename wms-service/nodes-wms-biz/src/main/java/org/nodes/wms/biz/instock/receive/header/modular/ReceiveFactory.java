package org.nodes.wms.biz.instock.receive.header.modular;


import org.nodes.wms.dao.instock.receive.header.dto.input.NewReceiveHeaderRequest;
import org.nodes.wms.dao.instock.receive.header.entities.ReceiveHeader;
import org.springframework.stereotype.Service;

/**
 * 收货单工厂
 * 主要用于创建收货单实体
 */
@Service
public class ReceiveFactory {
	public ReceiveHeader createReceiveHeader(NewReceiveHeaderRequest newReceiveHeaderRequest) {
		ReceiveHeader receiveHeader = new ReceiveHeader();
		receiveHeader.setWhId(newReceiveHeaderRequest.getWhId());
		receiveHeader.setWhCode(newReceiveHeaderRequest.getWhCode());
		receiveHeader.setBillTypeCd(newReceiveHeaderRequest.getBillTypeCd());
		receiveHeader.setSCode(newReceiveHeaderRequest.getSCode());
		receiveHeader.setSName(newReceiveHeaderRequest.getSName());
		receiveHeader.setInstoreType(newReceiveHeaderRequest.getInstoreType());
		receiveHeader.setWoId(newReceiveHeaderRequest.getWoId());
		receiveHeader.setOwnerCode(newReceiveHeaderRequest.getOwnerCode());
		receiveHeader.setRemark(newReceiveHeaderRequest.getRemark());
       return receiveHeader;
	}
}
