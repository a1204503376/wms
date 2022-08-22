package org.nodes.wms.biz.outstock.so.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.biz.outstock.so.modular.SoBillFactory;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.log.dto.output.LogDetailPageResponse;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.findSoHeaderByNoRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.SoHeaderDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoDetailAndStockRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.*;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;
import org.nodes.wms.dao.stock.dto.output.SerialSelectResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发货单业务接口实现类
 *
 * @author nodesc
 **/
@Service
@RequiredArgsConstructor
public class SoBillBizImpl implements SoBillBiz {

	private final SoHeaderDao soHeaderDao;
	private final SoDetailDao soDetailDao;
	private final StockQueryBiz stockQueryBiz;
	private final SoBillFactory soBillFactory;
	private final SoPickPlanDao soPickPlanDao;
	private final LogBiz logBiz;

	@Override
	public Page<SoHeaderPageResponse> getPage(Query query, SoHeaderPageQuery soHeaderPageQuery) {
		return soHeaderDao.page(Condition.getPage(query), soHeaderPageQuery);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SoHeader add(SoBillAddOrEditRequest soBillAddOrEditRequest) {
		if (Func.isNotEmpty(soBillAddOrEditRequest.getSoBillId())) {
			SoHeader soHeader = soHeaderDao.getById(soBillAddOrEditRequest.getSoBillId());
			if (!SoBillStateEnum.CREATE.equals(soHeader.getSoBillState())) {
				throw new ServiceException("编辑失败，该发货单目前不可编辑");
			}
		}
		SoHeader soHeader = soBillFactory.createSoHeader(soBillAddOrEditRequest);
		if (!soHeaderDao.saveOrUpdateSoHeader(soHeader)) {
			throw new ServiceException("新增发货单头表信息失败，请稍后再试");
		}
		List<SoDetail> soDetailList = soBillFactory.createSoDetailList(soHeader,
			soBillAddOrEditRequest.getSoDetailList());
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
			if (!SoBillStateEnum.CREATE.getCode().equals(soHeader.getSoBillState().getCode())) {
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
		List<SoDetail> soDetailList = soBillFactory.createSoDetailList(soHeader,
			soBillAddOrEditRequest.getSoDetailList());
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
		soHeaderMap.put("soBillState", SoBillStateEnum.COMPLETED.getCode());
		SoHeader soHeader = soBillFactory.createSoHeaderByCustom(soHeaderMap);
		if (!soHeaderDao.updateSoHeaderById(soHeader)) {
			throw new ServiceException("关闭发货单失败，请稍后再试");
		}
		logBiz.auditLog(AuditLogType.OUTSTOCK_BILL, soHeader.getSoBillId(), soHeader.getSoBillNo(), "关闭发货单");
	}

	@Override
	public void export(SoHeaderPageQuery soHeaderPageQuery, HttpServletResponse response) {
		List<SoHeaderExcelResponse> soHeaderExcelList = soHeaderDao.listByQuery(soHeaderPageQuery);
		soHeaderExcelList.forEach(soHeader -> {
			soHeader.setSoBillStateValue(soHeader.getSoBillState().getDesc());
		});
		ExcelUtil.export(response, "", "", soHeaderExcelList, SoHeaderExcelResponse.class);
	}

	@Override
	public Page<LogDetailPageResponse> pageLogById(IPage<?> page, Long soBillId) {
		return logBiz.pageLogByBillId(page, soBillId);
	}

	@Override
	public PickByPcSoHeaderResponse getSoHeaderByPickPc(SoBillIdRequest soBillIdRequest) {
		return soHeaderDao.getSoHeaderResponseById(soBillIdRequest.getSoBillId());
	}

	@Override
	public SoBillDistributedResponse findSoBillForDistributeBySoBillId(Long soBillId) {
		SoBillDistributedResponse soBill = new SoBillDistributedResponse();
		SoHeader soHeader = soHeaderDao.getById(soBillId);
		soBill.setSoBillId(soHeader.getSoBillId());
		soBill.setSoBillNo(soHeader.getSoBillNo());
		soBill.setOrderNo(soHeader.getOrderNo());
		List<SoDetailForDistResponse> details = soDetailDao.getSoDetailForDistribute(soBillId);
		soBill.setSoDetailList(details);
		return soBill;
	}

	@Override
	public IPage<FindAllPickingResponse> getAllPickingByNo(IPage<?> page, findSoHeaderByNoRequest request) {
		return soHeaderDao.getAllPickingPage(page, request);
	}

	@Override
	public SoDetailAndStockResponse getSoDetailAndStock(SoDetailAndStockRequest soDetailAndStockRequest) {
		//new一个返回对象
		SoDetailAndStockResponse soDetailAndStockResponse = new SoDetailAndStockResponse();
		//获取明细行的返回对象
		PickByPcSoDetailResponse pickByPcSoDetailResponse = soDetailDao.getPickByPcDetail(soDetailAndStockRequest);
		soDetailAndStockResponse.setPickByPcSoDetailResponse(pickByPcSoDetailResponse);
		//创建库存返回对象集合
		List<PickByPcStockDto> stockResponseList = new ArrayList<>();
		//构造查询库存集合条件
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		SkuLotUtil.setAllSkuLot(pickByPcSoDetailResponse, skuLot);
		skuLot.setSkuLot1(pickByPcSoDetailResponse.getSkuLot1());
		//根据查询条件获取库存集合
		List<Stock> stockList = stockQueryBiz.findEnableStockByZoneAndSkuLot(soDetailAndStockRequest.getWhId(), pickByPcSoDetailResponse.getSkuId(),
			StockStatusEnum.NORMAL, null, skuLot);
		for (Stock stock : stockList) {
			BigDecimal stockEnableQty = stock.getStockEnable();
			if (stockEnableQty.compareTo(BigDecimal.ZERO) == -1) {
				continue;
			}
			BigDecimal stockBalanceQty = stock.getStockBalance();
			PickByPcStockDto pickByPcStockDto = new PickByPcStockDto();
			Func.copy(stock, pickByPcStockDto);
			pickByPcStockDto.setStockEnable(stockEnableQty);
			pickByPcStockDto.setStockBalance(stockBalanceQty);
			pickByPcStockDto.setSoDetailId(pickByPcSoDetailResponse.getSoDetailId());
			pickByPcStockDto.setHasSerail(stockQueryBiz.getSerialCountByStockId(stock.getStockId()) > 0);
			stockResponseList.add(pickByPcStockDto);
		}
		soDetailAndStockResponse.setStockResponseList(stockResponseList);
		return soDetailAndStockResponse;
	}

	@Override
	public SoHeader getSoHeaderById(Long soBillId) {
		return soHeaderDao.getById(soBillId);
	}

	@Override
	public void updateSoBillState(SoHeader soHeader) {
		List<SoDetail> soDetailList = soDetailDao.getBySoBillId(soHeader.getSoBillId());
		SoBillStateEnum soBillStateEnum = soHeader.getSoBillState();
		int sizeOfNormal = 0;
		int sizeOfPart = 0;
		int allOutStock = 0;
		int deleted = 0;
		for (SoDetail soDetail : soDetailList) {
			if (soDetail.getBillDetailState().equals(SoDetailStateEnum.NORMAL)) {
				sizeOfNormal++;
			} else if (soDetail.getBillDetailState().equals(SoDetailStateEnum.PART)) {
				sizeOfPart++;
			} else if (soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
				allOutStock++;
			} else if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)) {
				deleted++;
			}
		}
		if (sizeOfPart > 0) {
			soHeader.setSoBillState(SoBillStateEnum.PART);
		} else if (sizeOfNormal + deleted == soDetailList.size()) {
			soHeader.setSoBillState(SoBillStateEnum.CREATE);
		} else if (allOutStock + deleted == soDetailList.size()) {
			soHeader.setSoBillState(SoBillStateEnum.ALL_OUT_STOCK);
		}
		if (!soHeader.getSoBillState().equals(soBillStateEnum)) {
			soHeaderDao.saveOrUpdateSoHeader(soHeader);
		}
	}

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
		return soDetailDao.getSoDetailBySoHeaderId(soBillId);
	}

	@Override
	public boolean isFinish(SoHeader soHeader) {
		return SoBillStateEnum.COMPLETED.equals(soHeader.getSoBillState())
			|| SoBillStateEnum.ALL_OUT_STOCK.equals(soHeader.getSoBillState())
			|| SoBillStateEnum.CANCELED.equals(soHeader.getSoBillState());
	}

	@Override
	public void updateState(Long soBillId, SoBillStateEnum soBillStateEnum) {
		soHeaderDao.updateStateBySoBillId(soBillId, soBillStateEnum);
	}

}
