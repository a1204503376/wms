package org.nodes.wms.biz.instock.asn.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.biz.instock.asn.modular.AsnFactory;
import org.nodes.wms.dao.instock.asn.AsnDetailDao;
import org.nodes.wms.dao.instock.asn.AsnHeaderDao;
import org.nodes.wms.dao.instock.asn.dto.input.AddOrEditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillIdRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnDetailRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.*;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
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
		pageResponsePage.getRecords().forEach(item -> {
			item.setAsnBillStateValue(item.getAsnBillState().getDesc());
		});
		return pageResponsePage;
	}

	@Override
	public AsnDetailResponse getAsnContactDetail(AsnBillIdRequest asnBillIdRequest) {
		return asnHeaderDao.selectAsnContactDetailByAsnBillId(asnBillIdRequest.getAsnBillId());
	}

	@Override
	public boolean removeAsnBillById(List<Long> asnBillIdList) {
		return asnHeaderDao.deleteAsnHeaderById(asnBillIdList);
	}

	@Override
	public boolean removeAsnDetailByAsnBillId(List<Long> asnBillIdList) {
		return asnDetailDao.deleteAsnDetailByAsnBillId(asnBillIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AsnHeader add(AddOrEditAsnBillRequest addOrEditAsnBillRequest) {
		// 创建ASN单头表实体，新增ASN单头表数据
		AsnHeader asnHeader = asnFactory.createAsnHeader(addOrEditAsnBillRequest);
		asnHeaderDao.saveOrUpdateAsnHeader(asnHeader);

		// 从请数中获取ASN单明细数据，并创建多个ASN单明细实体
		List<AsnDetailRequest> asnDetailList = addOrEditAsnBillRequest.getAsnDetailList();
		List<AsnDetail> details = new ArrayList<>();
		for (AsnDetailRequest asnDetail : asnDetailList) {
			AsnDetail detail = asnFactory.createAsnDetail(asnHeader, asnDetail);
			details.add(detail);
		}
		// 新增ASN单明细数据
		asnDetailDao.saveOrUpdateAsnDetail(details);
		return asnHeader;
	}

	@Override
	public AsnBillByEditResponse findAsnHeaderAndAsnDetail(Long asnBillId) {
		// 获取ASN头表信息
		AsnHeader asnHeader = asnHeaderDao.getAsnHeaderByAsnBillId(asnBillId);
		// 获取ASN明细信息
		List<AsnDetail> asnDetailList = asnDetailDao.getAsnDetailByAsnBillId(asnBillId);
		// 获取头表dto
		AsnHeaderEditResponse headerEditResponse = asnFactory.createAsnHeaderEditResponse(asnHeader);
		// 获取明细dto
		List<AsnDetailEditResponse> detailEditResponse = asnFactory.createAsnDetailEditResponse(asnDetailList);

		AsnBillByEditResponse result = new AsnBillByEditResponse();
		result.setAsnHeaderEditResponse(headerEditResponse);
		result.setAsnDetailEditResponseList(detailEditResponse);
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AsnHeader edit(AddOrEditAsnBillRequest addOrEditAsnBillRequest) {
		// 根据ASN单id 获取ASN单实体对象，验证该ASN单状态是否 是已收货
		AsnHeader header = asnHeaderDao.getAsnHeaderByAsnBillId(addOrEditAsnBillRequest.getAsnBillId());
		if (header.getAsnBillState().getCode() != 10) {
			throw new ServiceException("编辑ASN单失败，该ASN单已收货，单据编码:"+header.getAsnBillNo());
		}

		// 创建ASN单头表实体，修改ASN单头表数据
		AsnHeader asnHeader = asnFactory.createAsnHeader(addOrEditAsnBillRequest);
		asnHeaderDao.saveOrUpdateAsnHeader(asnHeader);

		// 从参数中获取ASN单明细数据，并创建多个ASN单明细实体
		List<AsnDetailRequest> asnDetailList = addOrEditAsnBillRequest.getAsnDetailList();
		List<AsnDetail> details = new ArrayList<>();
		for (AsnDetailRequest asnDetail : asnDetailList) {
			AsnDetail detail = asnFactory.createAsnDetail(asnHeader, asnDetail);
			details.add(detail);
		}
		// 新增/修改 ASN单明细数据
		asnDetailDao.saveOrUpdateAsnDetail(details);

		//删除ASN单明细数据
		asnDetailDao.deleteByIds(addOrEditAsnBillRequest.getRemoveIdList());

		//设置ASN单编码用于前端编码提示
		asnHeader.setAsnBillNo(header.getAsnBillNo());
		return asnHeader;
	}

	@Override
	public void exportAsnBill(PageParamsQuery pageParamsQuery, HttpServletResponse response) {
		List<AsnBillExportResponse> asnBillList = asnHeaderDao.listByParamsQuery(pageParamsQuery);
		asnBillList.forEach(item -> {
			item.setAsnBillStateValue(item.getAsnBillState().getDesc());
		});
		ExcelUtil.export(response, "ASD单", "ASN单数据报表", asnBillList, AsnBillExportResponse.class);
	}
}
