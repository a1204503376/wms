package org.nodes.wms.biz.instock.asn.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.biz.instock.asn.modular.AsnFactory;
import org.nodes.wms.dao.instock.asn.AsnDao;
import org.nodes.wms.dao.instock.asn.dto.input.AsnRequest;
import org.nodes.wms.dao.instock.asn.dto.input.DeleteRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ASN单 业务类
 */
@Service
@RequiredArgsConstructor
public class AsnBizImpl implements AsnBiz {

	private final AsnDao asnDao;
	private final AsnFactory asnFactory;

	@Override
	public Page<PageResponse> getPageAsnBill(IPage<?> page,
											 PageParamsQuery pageParamsQuery) {
		return asnDao.selectPageAsnBill(page, pageParamsQuery);
	}

	@Override
	public void save(AsnRequest asnRequest) {
		AsnHeader asnHeader = asnFactory.createAsnHeader(asnRequest);
		AsnDetail asnDetail = asnFactory.createAsnDetail(asnRequest);
		asnDao.addAsnHeaderAndAsnDetail(asnHeader, asnDetail);
	}

	@Override
	public AsnDetailResponse getAsnDetail(DeleteRequest deleteRequest) {
		return asnDao.selectAsnDetailByAsnBillId(deleteRequest);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeAsnBillAndReceiveBill(DeleteRequest deleteRequest) {
		AsnDetailResponse asnDetailResponse = asnDao.selectAsnDetailByAsnBillId(deleteRequest);

		Boolean hasAsnBillIdFlag = asnDao.hasAsnBillIdForReceiveBill(deleteRequest.getAsnBillId());

		Boolean delAsnHeaderFlag = asnDao.deleteAsnHeaderById(deleteRequest.getAsnBillId());
		Boolean delAsnDetailFlag = asnDao.deleteAsnDetailById(deleteRequest.getAsnBillId());
		Boolean delReceiveHeaderFlag = asnDao.deleteReceiveHeaderById(deleteRequest.getAsnBillId());
		Boolean delReceiveDetailFlag = asnDao.deleteReceiveDetailById(deleteRequest.getAsnBillId());

		return true;
	}
}
