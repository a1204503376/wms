package org.nodes.wms.biz.instock.asn.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.biz.instock.asn.modular.AsnFactory;
import org.nodes.wms.dao.common.log.dto.output.LogDetailPageResponse;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.instock.asn.AsnDetailDao;
import org.nodes.wms.dao.instock.asn.AsnHeaderDao;
import org.nodes.wms.dao.instock.asn.dto.input.AddOrEditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnDetailRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.*;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.nodes.wms.dao.instock.receive.ReceiveHeaderDao;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
	private final ReceiveHeaderDao receiveHeaderDao;

	private final LogBiz logBiz;

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
	public AsnHeaderForDetailResponse findAsnHeaderForDetailByAsnBillId(Long asnBillId) {
		return asnHeaderDao.getAsnHeaderForDetailById(asnBillId);
	}

	@Override
	public Page<AsnDetailForDetailResponse> findAsnDetailForDetailByAsnBillId(IPage<?> page, Long asnBillId) {
		return asnDetailDao.getAsnDetailForDetailByAsnBillId(page, asnBillId);
	}

	@Override
	public Page<ReceiveHeaderResponse> findReceiveHeaderForDetailByAsnBillId(IPage<?> page, Long asnBillId) {
		return receiveHeaderDao.getReceiveHeaderForDetailByAsnBillId(page, asnBillId);
	}

	@Override
	public Page<LogDetailPageResponse> findAsnLogForDetailByAsnBillId(IPage<?> page, Long asnBillId) {

		return logBiz.pageLogByBillId(page, asnBillId);
	}

	@Override
	public boolean removeAsnBillById(List<Long> asnBillIdList) {
		asnBillIdList.forEach(item -> {
			AsnHeader asnHeader = asnHeaderDao.getById(item);
			logBiz.auditLog(AuditLogType.ASN_BILL, asnHeader.getAsnBillId(), "删除ASN单");
		});
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

		// 从参数中获取ASN单明细数据，并创建多个ASN单明细实体
		List<AsnDetailRequest> asnDetailList = addOrEditAsnBillRequest.getAsnDetailList();
		List<AsnDetail> details = new ArrayList<>();
		for (AsnDetailRequest asnDetail : asnDetailList) {
			AsnDetail detail = asnFactory.createAsnDetail(asnHeader, asnDetail);
			details.add(detail);
		}
		// 新增ASN单明细数据
		asnDetailDao.saveOrUpdateAsnDetail(details);

		logBiz.auditLog(AuditLogType.ASN_BILL, asnHeader.getAsnBillId(), asnHeader.getAsnBillNo(),"新建ASN单");
		return asnHeader;
	}

	@Override
	public AsnBillByEditResponse findAsnHeaderAndAsnDetail(Long asnBillId) {
		// 获取ASN头表信息
		AsnHeader asnHeader = asnHeaderDao.getById(asnBillId);
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
		AsnHeader header = asnHeaderDao.getById(addOrEditAsnBillRequest.getAsnBillId());
		if (!header.getAsnBillState().getCode().equals(AsnBillStateEnum.NOT_RECEIPT.getCode())) {
			throw new ServiceException("编辑ASN单失败，该ASN单已收货，单据编码:" + header.getAsnBillNo());
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
		logBiz.auditLog(AuditLogType.ASN_BILL, asnHeader.getAsnBillId(), asnHeader.getAsnBillNo(), "编辑ASN单");
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

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public boolean remove(List<Long> asnBillIdList) {
		// 1.删除ASN头表
		boolean delAsnBillFlag = removeAsnBillById(asnBillIdList);
		// 2.删除ASN明细表
		boolean delAsnDetailFlag = removeAsnDetailByAsnBillId(asnBillIdList);

		//3+4+5 = 在收货管理那边 删除收货头表+明细表
		// 3.删除收货单头表 (传asnBillIdList)

		return true;
	}
}
