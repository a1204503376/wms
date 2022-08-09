package org.nodes.wms.biz.outstock.so.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.biz.outstock.so.modular.SoBillFactory;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.log.dto.output.LogDetailPageResponse;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.FindAllPickingRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
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
import java.util.*;

/**
 * 发货单业务接口实现类
 **/
@Service
@RequiredArgsConstructor
public class SoHeaderBizImpl implements SoHeaderBiz {

	private final SoHeaderDao soHeaderDao;

	private final SoDetailDao soDetailDao;

	private final SoBillFactory soBillFactory;
	private final StockQueryBiz stockQueryBiz;
	private final ZoneBiz zoneBiz;

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
			if (!SoBillStateEnum.CREATE.getCode().equals(soHeader.getSoBillState())) {
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
	public IPage<FindAllPickingResponse> getAllPickingByNo(IPage<?> page, FindAllPickingRequest request) {
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
		List<String> zoneNameList = new ArrayList<>();
		Collections.addAll(zoneNameList, "虚拟库区", "D箱人工拣货区", "人工货架区", "入库质检区", "入库暂存区");
		List<Long> zoneIdList = zoneBiz.getZoneIdListByName(zoneNameList);
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		SkuLotUtil.setAllSkuLot(pickByPcSoDetailResponse, skuLot);
		skuLot.setSkuLot1(pickByPcSoDetailResponse.getSkuLot1());
		//根据查询条件获取库存集合
		List<Stock> stockList = stockQueryBiz.findEnableStockByZone(soDetailAndStockRequest.getWhId(), pickByPcSoDetailResponse.getSkuId(),
			StockStatusEnum.NORMAL, zoneIdList, skuLot);
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
		for (SoDetail soDetail : soDetailList) {
			if (!soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
				if (!soHeader.getSoBillState().equals(SoBillStateEnum.PART)) {
					soHeader.setSoBillState(SoBillStateEnum.PART);
					soHeaderDao.saveOrUpdateSoHeader(soHeader);
				}
				return;
			}
		}
		soHeader.setSoBillState(SoBillStateEnum.ALL_OUT_STOCK);
		soHeaderDao.saveOrUpdateSoHeader(soHeader);
	}
}
