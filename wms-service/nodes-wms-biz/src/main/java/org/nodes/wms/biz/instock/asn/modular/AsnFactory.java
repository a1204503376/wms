package org.nodes.wms.biz.instock.asn.modular;

import org.nodes.wms.dao.instock.asn.dto.input.AsnRequest;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springframework.stereotype.Service;

/**
 * ASN单工厂
 * 主要用于创建ASN单实体
 */
@Service
public class AsnFactory {

	public AsnHeader createAsnHeader(AsnRequest asnRequest) {
		AsnHeader asnHeader = new AsnHeader();
		asnHeader.setAsnBillNo(asnRequest.getSkuCode());
		asnHeader.setAsnBillNo(asnRequest.getSkuName());
		return asnHeader;
	}

	public AsnDetail createAsnDetail(AsnRequest asnRequest) {
		AsnDetail asnDetail = new AsnDetail();
		asnDetail.setAsnBillNo(asnRequest.getSkuCode());
		asnDetail.setAsnBillNo(asnRequest.getSkuName());
		return asnDetail;
	}
}
