package org.nodes.wms.biz.outstock.so.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.biz.outstock.so.modular.SoBillFactory;
import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.SoHeaderDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoBillEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 发货单业务接口实现类
 **/
@Service
@RequiredArgsConstructor
public class SoHeaderBizImpl implements SoHeaderBiz {

	private final SoHeaderDao soHeaderDao;

	private final SoDetailDao soDetailDao;

	private final SoBillFactory soBillFactory;

	@Override
	public Page<SoHeaderPageResponse> getPage(Query query, SoHeaderPageQuery soHeaderPageQuery) {
		return soHeaderDao.page(Condition.getPage(query), soHeaderPageQuery);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SoHeader add(SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soBillFactory.createSoHeader(soBillAddOrEditRequest);
		if (!soHeaderDao.saveOrUpdateSoHeader(soHeader)) {
			throw new ServiceException("新增出库单头表信息失败，请稍后再试");
		}
		List<SoDetail> soDetailList = soBillFactory.createSoDetailList(soHeader, soBillAddOrEditRequest.getSoDetailList());
		if (!soDetailDao.saveOrUpdateBatch(soDetailList)) {
			throw new ServiceException("新增出库单明细信息失败，请稍后再试");
		}

		return soHeader;
	}

	@Override
	public boolean remove(List<Long> soBillIdList) {
		soBillIdList.forEach(item -> {
			SoHeader soHeader = soHeaderDao.getById(item);
			if (!soHeader.getSoBillState().equals(SoBillStateEnum.NOT.getIndex())) {
				throw new ServiceException("删除失败,只能删除单据状态为未出库的出库单");
			}
		});
		return soHeaderDao.removeByIdList(soBillIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SoHeader edit(SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = soBillFactory.createSoHeader(soBillAddOrEditRequest);
		if (!soHeaderDao.saveOrUpdateSoHeader(soHeader)) {
			throw new ServiceException("编辑出库单头表信息失败，请稍后再试");
		}
		List<SoDetail> soDetailList = soBillFactory.createSoDetailList(soHeader, soBillAddOrEditRequest.getSoDetailList());
		if (!soDetailDao.saveOrUpdateBatch(soDetailList)) {
			throw new ServiceException("编辑出库单明细信息失败，请稍后再试");
		}

		if (Func.isNotEmpty(soBillAddOrEditRequest.getRemoveIdList())
			&& !soDetailDao.removeByIdList(soBillAddOrEditRequest.getRemoveIdList())) {
			throw new ServiceException("删除出库单明细信息失败，请稍后再试");
		}
		return soHeader;
	}

	@Override
	public SoBillEditResponse findSoBillByEdit(Long soBillId) {
		SoBillEditResponse soBillEditResponse = new SoBillEditResponse();
		soBillEditResponse.setSoHeader(soHeaderDao.getSoHeaderEditBySoBillId(soBillId));
		soBillEditResponse.setSoDetailList(soDetailDao.getSoDetailEditBySoBillId(soBillId));
		return soBillEditResponse;
	}
}
