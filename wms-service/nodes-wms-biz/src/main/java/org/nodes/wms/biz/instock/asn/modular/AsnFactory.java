package org.nodes.wms.biz.instock.asn.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.dao.instock.asn.dto.input.AddAsnBillRequest;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springframework.stereotype.Service;

/**
 * ASN单工厂
 * 主要用于创建ASN单实体
 */
@Service
@RequiredArgsConstructor
public class AsnFactory {

	private final NoGeneratorUtil noGeneratorUtil;

	public AsnHeader createAsnHeader(AddAsnBillRequest addAsnBillRequest) {
		AsnHeader asnHeader = new AsnHeader();
		asnHeader.setAsnBillNo(noGeneratorUtil.createAsnBillNo());
		asnHeader.setBillTypeCd(addAsnBillRequest.getBillTypeCd());
		asnHeader.setSupplierCode(addAsnBillRequest.getSupplierCode());
		asnHeader.setSupplierName(addAsnBillRequest.getSupplierName());
		asnHeader.setWhId(addAsnBillRequest.getWhId());
		asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT);
		asnHeader.setAsnBillRemark(addAsnBillRequest.getAsnBillRemark());
		return asnHeader;

	}

	public AsnDetail createAsnDetail(AddAsnBillRequest addAsnBillRequest,Long asnBillId) {
		AsnDetail asnDetail = new AsnDetail();
		asnDetail.setAsnBillId(asnBillId);
		asnDetail.setAsnLineNo(addAsnBillRequest.getAsnLineNo());
		asnDetail.setSkuCode(addAsnBillRequest.getSkuCode());
		asnDetail.setUmCode(addAsnBillRequest.getUmCode());
		asnDetail.setBaseUmCode(addAsnBillRequest.getBaseUmCode());
		asnDetail.setBaseUmName(addAsnBillRequest.getUmName());
		asnDetail.setPlanQty(addAsnBillRequest.getPlanQty());
		asnDetail.setRemark(addAsnBillRequest.getRemark());
		return asnDetail;
	}
}
