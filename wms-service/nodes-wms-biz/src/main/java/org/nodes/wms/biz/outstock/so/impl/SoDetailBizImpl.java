package org.nodes.wms.biz.outstock.so.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.outstock.so.SoDetailBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.nodes.wms.dao.outstock.so.dto.output.LineNoAndSkuSelectResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.stock.dto.output.SerialSelectResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.RequiredArgsConstructor;

/**
 *
 **/
@Service
@RequiredArgsConstructor
public class SoDetailBizImpl implements SoDetailBiz {

	private final SoDetailDao soDetailDao;
	private final StockQueryBiz stockQueryBiz;

	@Override
	public Page<SoDetailForDetailResponse> pageSoDetailForDetailBySoBillId(Query query,
			SoBillIdRequest soBillIdRequest) {
		return soDetailDao.pageForSoDetailBySoBillId(Condition.getPage(query), soBillIdRequest.getSoBillId());
	}

	@Override
	public Page<NotSoPickPageResponse> pageNotSoPick(Query query, NotSoPickPageQuery notSoPickPageQuery) {
		return soDetailDao.pageNotSoPick(Condition.getPage(query), notSoPickPageQuery);
	}

	@Override
	public void exportNotSoPick(NotSoPickPageQuery notSoPickPageQuery, HttpServletResponse response) {
		List<NotSoPickExcelResponse> notSoPickList = soDetailDao.notSoPickListByQuery(notSoPickPageQuery);
		ExcelUtil.export(response, notSoPickList, NotSoPickExcelResponse.class);
	}

	@Override
	public List<LineNoAndSkuSelectResponse> getLineNoAndSkuSelectList(Long soBillId) {
		return soDetailDao.getLineNoAndSkuCodeById(soBillId);
	}

	@Override
	public List<SerialSelectResponse> getSerialSelectResponseList(Long stockId) {
		List<Serial> serialList = stockQueryBiz.findSerialByStock(stockId);
		List<SerialSelectResponse> serialSelectResponseList = new ArrayList<>();
		for (Serial serial : serialList) {
			SerialSelectResponse serialSelectResponse = new SerialSelectResponse();
			Func.copy(serial, serialSelectResponse);
			serialSelectResponseList.add(serialSelectResponse);
		}
		return serialSelectResponseList;
	}

	@Override
	public IPage<SoDetail> getPickingBySoBillId(Long soBillId, Query query) {
		IPage<SoDetail> page = Condition.getPage(query);
		return soDetailDao.getSoDetailPage(soBillId, page);
	}

	@Override
	public SoDetail getSoDetailById(Long soDetailId) {
		return soDetailDao.getSoDetailById(soDetailId);
	}

	@Override
	public void update(SoDetail soDetail) {
		soDetailDao.update(soDetail);
	}

	@Override
	public void updateSoDetailStatus(SoDetail soDetail, BigDecimal pickQty) {
		soDetail.setScanQty(soDetail.getScanQty().add(pickQty));
		soDetail.setSurplusQty(soDetail.getSurplusQty().subtract(pickQty));
		if (BigDecimalUtil.eq(soDetail.getSurplusQty(), BigDecimal.ZERO)) {
			soDetail.setBillDetailState(SoDetailStateEnum.ALL_OUT_STOCK);
		} else if (BigDecimalUtil.eq(soDetail.getScanQty(), BigDecimal.ZERO)) {
			soDetail.setBillDetailState(SoDetailStateEnum.NORMAL);
		} else if (BigDecimalUtil.gt(soDetail.getScanQty(), BigDecimal.ZERO)) {
			soDetail.setBillDetailState(SoDetailStateEnum.PART);
		}
		soDetailDao.update(soDetail);
	}

	@Override
	public List<SoDetail> getEnableSoDetailBySoHeaderId(Long soBillId) {
		// TODO Auto-generated method stub
		return null;
	}
}
