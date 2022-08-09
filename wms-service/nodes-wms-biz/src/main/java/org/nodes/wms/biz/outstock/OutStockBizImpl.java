package org.nodes.wms.biz.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.logSoPick.modular.LogSoPickFactory;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.so.SoDetailBiz;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.*;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.PickingByBoxResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillDistributedRequest;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;
import org.nodes.wms.dao.stock.dto.output.StockSoPickPlanResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 出库业务实现类
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class OutStockBizImpl implements OutStockBiz {

	private final SoPickPlanDao soPickPlanDao;
	private final LogSoPickDao logSoPickDao;
	private final SoHeaderBiz soHeaderBiz;
	private final SoDetailBiz soDetailBiz;
	private final SoPickPlanBiz soPickPlanBiz;
	private final LogSoPickFactory logSoPickFactory;
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;
	private final LocationBiz locationBiz;
	private final LogBiz logBiz;

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void pickByPc(PickByPcRequest request) {
		SoHeader soHeader = soHeaderBiz.getSoHeaderById(request.getSoBillId());
		SoDetail soDetail = soDetailBiz.getSoDetailById(request.getPickByPcStockDtoList().get(0).getSoDetailId());
		// 1 业务判断：
		// 1.1 如果单据有拣货计划则不能使用PC拣货
		if (soPickPlanBiz.hasPickPlan(request.getSoBillId())) {
			throw new ServiceException("拣货失败,收货单已存在拣货计划");
		}
		// 1.2 单据和单据明细行的状态如果为终结状态，则不能进行拣货
		if (soHeader.getSoBillState().equals(SoBillStateEnum.COMPLETED)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.ALL_OUT_STOCK)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.CANCELED)) {
			throw new ServiceException("拣货失败,收货单状态为" + soHeader.getSoBillState() + "不能进行拣货");
		}
		if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)
			|| soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
			throw new ServiceException("拣货失败,收货单明细状态为" + soDetail.getBillDetailState() + "不能进行拣货");
		}

		// 1.3 拣货数量是否超过剩余数量
		List<PickByPcStockDto> pickByPcStockDtoList = request.getPickByPcStockDtoList();
		BigDecimal sum = BigDecimal.ZERO;
		for (PickByPcStockDto pickByPcStockDto : pickByPcStockDtoList) {
			sum.add(pickByPcStockDto.getOutStockQty());
		}
		if (sum.compareTo(soDetail.getSurplusQty()) == 1) {
			throw new ServiceException("拣货失败,收货数量大于剩余数量");
		}
		// 2 生成拣货记录，需要注意序列号（log_so_pick)
		for (PickByPcStockDto pickByPcStockDto : pickByPcStockDtoList) {
			Stock stock = stockQueryBiz.findStockById(pickByPcStockDto.getStockId());
			LogSoPick logSoPick = logSoPickFactory.createLogSoPick(pickByPcStockDto, soHeader, soDetail, stock);
			Location location = locationBiz.findLocationByLocCode(stock.getWhId(), pickByPcStockDto.getLocCode());
			// 3 移动库存到出库集货区
			stockBiz.moveStock(stock, pickByPcStockDto.getSerailList(), pickByPcStockDto.getOutStockQty(),
				location, StockLogTypeEnum.OUTSTOCK_BY_PC, soHeader.getSoBillId(), soHeader.getSoBillNo(), soDetail.getSoLineNo());
		}
		// 4 更新出库单明细中的状态和数量
		soDetail.setScanQty(soDetail.getScanQty().add(sum));
		soDetail.setSurplusQty(soDetail.getSurplusQty().subtract(sum));
		if (soDetail.getSurplusQty() == BigDecimal.ZERO) {
			soDetail.setBillDetailState(SoDetailStateEnum.ALL_OUT_STOCK);
		} else {
			soDetail.setBillDetailState(SoDetailStateEnum.PART);
		}
		soDetailBiz.update(soDetail);
		// 5 更新发货单状态
		soHeaderBiz.updateSoBillState(soHeader);
		// 6 记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, "PC拣货");
	}


	@Override
	public List<SoPickPlanForDistributionResponse> getSoPickPlanBySoBillIdAndSoDetailId(Long soBillId,
																						Long soDetailId) {
		AssertUtil.notNull(soBillId.toString(), "查询拣货计划失败，发货单id为空");
		List<SoPickPlanForDistributionResponse> soPickPlanList = soPickPlanDao.getBySoBillIdAndSoDetailId(soBillId,
			soDetailId);
		soPickPlanList.forEach(item -> {
			item.setStockStatusValue(item.getStockStatus().getDesc());
		});
		return soPickPlanList;
	}

	@Override
	public IPage<FindAllPickingResponse> selectAllPickingByNo(FindAllPickingRequest request, Query query) {
		// request.setBillDetailState(SoDetailStateEnum.Allocated.getIndex());
		request.setBillDetailState(SoDetailStateEnum.UnAlloc.getCode());
		return soHeaderBiz.getAllPickingByNo(Condition.getPage(query), request);
	}

	@Override
	public PickingByBoxResponse pickByPcs(PickByPcsRequest request) {
		// 1 业务判断：
		// 1.1 单据和单据明细行的状态如果为终结状态，则不能进行拣货
		// 1.1 拣货数量是否超过剩余数量
		// 2 生成拣货记录，需要注意序列号（log_so_pick)
		// 3 调用拣货计划中相应的函数
		// 3 移动库存到出库集货区
		// 4 更新出库单明细中的状态和数量
		// 5 更新发货单状态
		// 6 记录业务日志
		return null;
	}

	@Override
	public IPage<FindPickingBySoBillIdResponse> selectPickingBySoBillId(FindPickingBySoBillIdRequest request,
																		Query query) {
		IPage<SoDetail> page = soDetailBiz.getPickingBySoBillId(request.getSoBillId(), query);
		AssertUtil.notNull(page, "查询结果为空");
		return page.convert(result -> {
			return BeanUtil.copy(result, FindPickingBySoBillIdResponse.class);
		});
	}

	@Override
	public PickingByBoxResponse pickByBox(PickByPcsRequest request) {

		// 1 业务判断：
		// 1.1 单据和单据明细行的状态如果为终结状态，则不能进行拣货
		// 1.1 拣货数量是否超过剩余数量
		// 2 生成拣货记录，需要注意序列号（log_so_pick)
		// 3 调用拣货计划中相应的函数
		// 3 移动库存到出库集货区
		// 4 更新出库单明细中的状态和数量
		// 5 更新发货单状态
		// 6 记录业务日志
		return null;
	}

	@Override
	public IPage<OutboundAccessAreaLocationQueryResponse> selectLocationByConnectionArea(
		OutboundAccessAreaLocationQueryRequest request, Query query) {
		return null;
	}

	@Override
	public void connectionAreaPick(ConnectionAreaPickingRequest request) {

	}

	@Override
	public void connectionAreaMove(ConnectionAreaPickingRequest request) {

	}

	@Override
	public boolean automaticAssign(Long soBillId) {
		return false;
	}

	@Override
	public boolean cancelAssign(Long soBillId) {
		return false;
	}

	@Override
	public boolean issued(Long soBillId) {
		return false;
	}

	@Override
	public List<StockSoPickPlanResponse> getEnableStockBySkuId(Long skuId) {
		return null;
	}

	@Override
	public boolean saveAssign(SoBillDistributedRequest soBillDistributedRequest) {
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelOutstock(List<Long> logSoPickIdList) {

		// 根据拣货记录id查找所有的拣货记录
		List<LogSoPick> logSoPickList = logSoPickDao.getByIds(logSoPickIdList);
		// 生成一笔反向的拣货记录
		logSoPickList.forEach(logSoPick -> {
//			logSoPickDao.
		});
		// 根据拣货记录下架库存
		// 更新发货单明细状态与实收数量
		// 更新发货单头表状态
		// 记录业务日志
	}
}
