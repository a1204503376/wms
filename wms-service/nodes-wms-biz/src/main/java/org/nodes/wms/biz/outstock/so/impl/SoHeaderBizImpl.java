package org.nodes.wms.biz.outstock.so.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.biz.outstock.so.modular.SoBillFactory;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.SoHeaderDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发货单业务接口实现类
 **/
@Service
@RequiredArgsConstructor
public class SoHeaderBizImpl implements SoHeaderBiz {

	private final SoHeaderDao soHeaderDao;

	private final SoDetailDao soDetailDao;

	private final SoBillFactory soBillFactory;

	private final LogBiz logBiz;

	@Override
	public Page<SoHeaderPageResponse> getPage(Query query, SoHeaderPageQuery soHeaderPageQuery) {
		return soHeaderDao.page(Condition.getPage(query), soHeaderPageQuery);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SoHeader add(SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soBillFactory.createSoHeader(soBillAddOrEditRequest);
		if (!soHeaderDao.saveOrUpdateSoHeader(soHeader)) {
			throw new ServiceException("新增发货单头表信息失败，请稍后再试");
		}
		List<SoDetail> soDetailList = soBillFactory.createSoDetailList(soHeader, soBillAddOrEditRequest.getSoDetailList());
		if (!soDetailDao.saveOrUpdateBatch(soDetailList)) {
			throw new ServiceException("新增发货单明细信息失败，请稍后再试");
		}
		logBiz.auditLog(AuditLogType.OUTSTOCK_BILL, soHeader.getSoBillId(), soHeader.getSoBillNo(), "新建发货单");
		return soHeader;
	}

	@Override
	public boolean remove(List<Long> soBillIdList) {
		soBillIdList.forEach(item -> {
			SoHeader soHeader = soHeaderDao.getById(item);
			if (!soHeader.getSoBillState().equals(SoBillStateEnum.NOT.getIndex())) {
				throw new ServiceException("删除失败,只能删除单据状态为未出库的发货单");
			}
			logBiz.auditLog(AuditLogType.OUTSTOCK_BILL, soHeader.getSoBillId(), "删除发货单");
		});
		return soHeaderDao.removeByIdList(soBillIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SoHeader edit(SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soBillFactory.createSoHeader(soBillAddOrEditRequest);
		if (!soHeaderDao.saveOrUpdateSoHeader(soHeader)) {
			throw new ServiceException("编辑发货单头表信息失败，请稍后再试");
		}
		List<SoDetail> soDetailList = soBillFactory.createSoDetailList(soHeader, soBillAddOrEditRequest.getSoDetailList());
		if (!soDetailDao.saveOrUpdateBatch(soDetailList)) {
			throw new ServiceException("编辑发货单明细信息失败，请稍后再试");
		}
		if (Func.isNotEmpty(soBillAddOrEditRequest.getRemoveIdList())
			&& !soDetailDao.removeByIdList(soBillAddOrEditRequest.getRemoveIdList())) {
			throw new ServiceException("编辑发货单明细信息失败，请稍后再试");
		}
		logBiz.auditLog(AuditLogType.OUTSTOCK_BILL, soHeader.getSoBillId(), soHeader.getSoBillNo(), "编辑发货单");
		return soHeader;
	}

	@Override
	public SoBillEditResponse findSoBillByEdit(Long soBillId) {
		SoBillEditResponse soBillEditResponse = new SoBillEditResponse();
		soBillEditResponse.setSoHeader(soHeaderDao.getSoHeaderEditBySoBillId(soBillId));
		soBillEditResponse.setSoDetailList(soDetailDao.getSoDetailEditBySoBillId(soBillId));
		return soBillEditResponse;
	}

	@Override
	public SoHeaderForDetailResponse findSoHeaderForDetailBySoBillId(Long soBillId) {
		return soHeaderDao.getSoHeaderForDetailById(soBillId);
	}

	@Override
	public void closeById(Long soBillId) {
		Map<String, Object> soHeaderMap = new HashMap<String, Object>();
		soHeaderMap.put("soBillId", soBillId);
		soHeaderMap.put("soBillState", SoBillStateEnum.CLOSED.getIndex());
		SoHeader soHeader = soBillFactory.createSoHeaderByCustom(soHeaderMap);
		if (!soHeaderDao.updateSoHeaderById(soHeader)) {
			throw new ServiceException("关闭发货单失败，请稍后再试");
		}
		logBiz.auditLog(AuditLogType.OUTSTOCK_BILL, soHeader.getSoBillId(), "关闭发货单");
	}

	@Override
	public void export(SoHeaderPageQuery soHeaderPageQuery, HttpServletResponse response) {
		List<SoHeaderExcelResponse> soHeaderExcelList = soHeaderDao.listByQuery(soHeaderPageQuery);
		ExcelUtil.export(response, "", "", soHeaderExcelList, SoHeaderExcelResponse.class);
	}

	@Override
	public Page<LogForSoDetailResponse> pageLogById(Query query, Long soBillId) {
		return logBiz.pageLogBySoBillId(Condition.getPage(query), soBillId);
	}

    @Override
    public SoBillDistributedResponse findSoBillForDistBySoBillId(Long soBillId) {
		SoHeader soHeader = soHeaderDao.getById(soBillId);
		List<SoDetail> soDetailList = soDetailDao.getBySoBillId(soBillId);
		SoBillDistributedResponse soBill = new SoBillDistributedResponse();
		soBill.setSoBillId(soHeader.getSoBillId());
		soBill.setSoBillNo(soHeader.getSoBillNo());
		soBill.setOrderNo(soHeader.getOrderNo());
		List<SoDetailForDistResponse> details = Func.copy(soDetailList, SoDetailForDistResponse.class);
		soBill.setSoDetailList(details);
		return soBill;
    }
}
