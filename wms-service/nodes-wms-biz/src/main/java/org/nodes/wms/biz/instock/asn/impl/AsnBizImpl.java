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
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillExportResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailByEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springblade.core.excel.util.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
		Page<PageResponse> pageResponsePage = asnHeaderDao.selectPageAsnBill(page, pageParamsQuery);
		pageResponsePage.getRecords().forEach(item ->{
			item.setAsnBillStateValue(item.getAsnBillState().getDesc());
		});
		return pageResponsePage;
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
		// 创建ASN单头表实体，新增ASN单头表数据
		AsnHeader asnHeader = asnFactory.createAsnHeader(addAsnBillRequest);
		boolean header = asnHeaderDao.insertAsnHeader(asnHeader);

		// 从请求参数中获取ASN单明细数据，并创建多个ASN单明细实体
		List<AsnDetail> asnDetailList = addAsnBillRequest.getAsnDetailList();
		List<AsnDetail> details = new ArrayList<>();
		for (AsnDetail asnDetail : asnDetailList) {
			AsnDetail detail = asnFactory.createAsnDetail(addAsnBillRequest, asnHeader, asnDetail);
				details.add(detail);
		}
		// 新增ASN单明细数据
		boolean detail = asnDetailDao.addAsnDetail(details);
		return header && detail;
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
		List<AsnBillExportResponse> asnBillList = asnHeaderDao.listByParamsQuery(pageParamsQuery);
		asnBillList.forEach(item ->{
			item.setAsnBillStateValue(item.getAsnBillState().getDesc());
		});
		ExcelUtil.export(response, "ASD单", "ASN单数据报表", asnBillList, AsnBillExportResponse.class);
	}
}
