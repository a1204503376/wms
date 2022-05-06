package org.nodes.wms.biz.instock.asn.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.biz.instock.asn.modular.AsnFactory;
import org.nodes.wms.dao.instock.asn.AsnDetailDao;
import org.nodes.wms.dao.instock.asn.AsnHeaderDao;
import org.nodes.wms.dao.instock.asn.dto.input.AddAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillIdRequest;
import org.nodes.wms.dao.instock.asn.dto.input.EditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailByEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnExportResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springblade.core.excel.util.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ASN单 业务类
 */
@Service
@RequiredArgsConstructor
public class AsnBizImpl implements AsnBiz {

	private final AsnHeaderDao asnHeaderDao;
	private final AsnDetailDao asnDetailDao;
	private final AsnFactory asnFactory;

	@Override
	public Page<PageResponse> getPageAsnBill(IPage<?> page,
											 PageParamsQuery pageParamsQuery) {
		return asnHeaderDao.selectPageAsnBill(page, pageParamsQuery);
	}

	@Override
	public AsnDetailResponse getAsnContactDetail(AsnBillIdRequest asnBillIdRequest) {
		return asnHeaderDao.selectAsnContactDetailByAsnBillId(asnBillIdRequest.getAsnBillId());
	}

	@Override
	public Boolean removeAsnBillById(List<Long> asnBillIdList) {
		return asnHeaderDao.deleteAsnHeaderById(asnBillIdList);
	}

	@Override
	public Boolean removeAsnDetailByAsnBillId(List<Long> asnBillIdList) {
		return asnDetailDao.deleteAsnDetailByAsnBillId(asnBillIdList);
	}

	@Override
	public List<Long> getAsnDetailIdList(List<Long> asnBillIdList) {
		return asnDetailDao.selectAsnDetailIdListByAsnBillId(asnBillIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean add(AddAsnBillRequest addAsnBillRequest) {
		AsnHeader asnHeader = asnFactory.createAsnHeader(addAsnBillRequest);
		boolean header = asnHeaderDao.insertAsnHeader(asnHeader);
		AsnDetail asnDetail = asnFactory.createAsnDetail(addAsnBillRequest, asnHeader.getAsnBillId());
		boolean detail = asnDetailDao.addAsnDetail(asnDetail);
		return true;
	}

	@Override
	public AsnDetailByEditResponse getAsnHeaderAndAsnDetail(AsnBillIdRequest asnBillIdRequest) {
		return new AsnDetailByEditResponse();
	}

	@Override
	public boolean edit(EditAsnBillRequest editAsnBillRequest) {
		return false;
	}

	@Override
	public void exportAsnBill(PageParamsQuery pageParamsQuery, HttpServletResponse response) {
		List<AsnExportResponse> asnBillList = asnHeaderDao.listByParamsQuery(pageParamsQuery);
		ExcelUtil.export(response, "ASD单", "ASN单数据报表", asnBillList,AsnExportResponse.class);
	}
}
