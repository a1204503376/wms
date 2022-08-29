package org.nodes.wms.biz.instock;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receive.modular.ReceiveFactory;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.instock.receiveLog.modular.ReceiveLogFactory;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.instock.receive.ReceiveDetailDao;
import org.nodes.wms.dao.instock.receive.ReceiveHeaderDao;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.PdaByPcsReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnItemDto;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveDetailStatusEnum;
import org.nodes.wms.dao.instock.receive.enums.ReceiveHeaderStateEnum;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class InStockBizImpl implements InStockBiz {
	private final ReceiveBiz receiveBiz;
	private final ReceiveFactory receiveFactory;
	private final StockBiz stockBiz;
	private final ReceiveLogBiz receiveLogBiz;
	private final StockQueryBiz stockQueryBiz;
	private final AgvTask agvTask;
	private final LogBiz logBiz;
	private final ReceiveLogFactory receiveLogFactory;
	private final ReceiveHeaderDao receiveHeaderDao;
	private final ReceiveDetailDao receiveDetailDao;
	private final LocationBiz locationBiz;

	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	@Override
	public void receiveByBoxCode(ReceiveDetailLpnPdaRequest request, String logType) {
		boolean hasReceiveHeaderId = Func.isNotEmpty(request.getReceiveHeaderId());
		if (Func.isNotEmpty(request.getLpnCode())) {
			List<Stock> stocks = stockQueryBiz.findStockByLpnCode(request.getLpnCode());
			if (Func.notNull(stocks) && stocks.size() > 0) {
				throw new ServiceException("LPN[" + request.getLpnCode() + "]已存在库存数据！");
			}
		}
		ReceiveHeader receiveHeader;
		// 判断业务参数（无单收货除外），是否可以正常收货、超收
		if (hasReceiveHeaderId) {
			for (ReceiveDetailLpnItemDto item : request.getReceiveDetailLpnItemDtoList()) {
				ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
				ReceiveHeader header = receiveBiz.selectReceiveHeaderById(detail.getReceiveId());
				receiveBiz.canReceive(header, detail, item.getPlanQty());
				//查询收货库位，如果是发货接驳区或者出库集货区则抛异常
				canReceiveByLocation(request.getWhId(), request.getLocCode());
			}
		} else {
			// 是否无单收货：判断当前用户是否有无单收货且未关闭的单据，如果有则用这个收货单（如果多个取最后一个）并新建收货单明细
			List<ReceiveHeader> receiveHeaderList = receiveBiz.getReceiveListByNonOrder(AuthUtil.getUserId());
			// 订单行号
			String lineNo = "0";
			int lineNum = 0;
			if (Func.isNotEmpty(receiveHeaderList)) {
				//取无单收货最后一条收货单
				receiveHeader = receiveHeaderList.get(receiveHeaderList.size() - 1);
				//获取当前收货单关联的最新一条明细的行号
				lineNo = receiveBiz.getReceiveDetailLinNo(receiveHeader.getReceiveId());
			} else {
				//否则新建一个收货单
				receiveHeader = receiveFactory.createReceiveHeader(request);
				receiveBiz.newReceiveHeader(receiveHeader);
			}
			// 设置request的头表id
			request.setReceiveHeaderId(receiveHeader.getReceiveId());
			if (Func.isNotEmpty(lineNo)) {
				lineNum = Integer.parseInt(lineNo);
			}
			//循环生成明细
			for (ReceiveDetailLpnItemDto item : request.getReceiveDetailLpnItemDtoList()) {
				lineNum += 10;
				//根据id获取lpn实体
				ReceiveDetailLpn lpn = receiveBiz.getReceiveDetailLpnById(item.getReceiveDetailLpnId());
				//创建收货单明细
				ReceiveDetail receiveDetail = receiveFactory.createReceiveDetail(request, item, lpn, receiveHeader, lineNum);
				receiveBiz.newReceiveDetail(receiveDetail);
				//更新lpn表的明细id
				lpn.setReceiveDetailId(receiveDetail.getReceiveDetailId());
				//更新lpn表头表id
				lpn.setReceiveHeaderId(receiveHeader.getReceiveId());
				receiveBiz.updateReceiveDetailLpn(lpn);
				//设置request的明细id
				item.setReceiveDetailId(receiveDetail.getReceiveDetailId());
			}

		}
		List<Stock> stockList = new ArrayList<>();
		for (ReceiveDetailLpnItemDto item : request.getReceiveDetailLpnItemDtoList()) {
			ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
			ReceiveHeader header = receiveBiz.selectReceiveHeaderById(detail.getReceiveId());
			//根据id获取lpn实体
			ReceiveDetailLpn lpn = receiveBiz.getReceiveDetailLpnById(item.getReceiveDetailLpnId());
			//更新lpn表批属性信息
			lpn.setSkuLot1(request.getSkuLot1());
			lpn.setSkuLot2(request.getSkuLot2());
			//更新lpn实收数量
			lpn.setScanQty(lpn.getScanQty().add(item.getPlanQty()));
			//更新lpn状态
			lpn.setDetailStatus(ReceiveDetailStatusEnum.COMPLETED);
			receiveBiz.updateReceiveDetailLpn(lpn);
			// 生成清点记录
			ReceiveLog receiveLog = receiveLogBiz.newReceiveLog(request, item, lpn, header, detail);
			// 调用库存函数（for begin：一条执行一次）
			stockList.add(stockBiz.inStock(StockLogTypeEnum.INSTOCK_BY_BOX, receiveLog));
			//有单收货更新头表和明细信息
			if (hasReceiveHeaderId) {
				// 更新收货单明细状态（for end：一条执行一次）
				receiveBiz.updateReceiveDetail(detail, item.getPlanQty());
				// 更新收货单状态
				receiveBiz.updateReceiveHeader(header, detail);
			}
			// 记录业务日志
			if (Func.isEmpty(logType)) {
				//传入参数为空设置为按箱收货类型
				logType = StockLogTypeEnum.INSTOCK_BY_BOX.getDesc();
			}
			receiveBiz.log(logType, header, detail, receiveLog);

		}
		agvTask.putwayToSchedule(stockList);

	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public PdaByPcsReceiveResponse receiptByPcs(PdaByPieceReceiveRequest request) {
		if (Func.isNotEmpty(request.getBoxCode())) {
			List<Stock> stocks = stockQueryBiz.findStockByLpnCode(request.getBoxCode());
			if (Func.notNull(stocks) && stocks.size() > 0) {
				throw new ServiceException("箱码[" + request.getBoxCode() + "]已存在库存数据！");
			}
		}
		ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(request.getReceiveDetailId());
		ReceiveHeader receiveHeader = receiveBiz.selectReceiveHeaderById(request.getReceiveId());
		// 判断业务参数，是否可以正常收货、超收
		receiveBiz.canReceive(receiveHeader, detail, request.getSurplusQty());
		//查询收货库位，如果是发货接驳区或者出库集货区则抛异常
		canReceiveByLocation(request.getWhId(), request.getLocCode());
		// 生成清点记录
		ReceiveLog receiveLog = receiveLogBiz.newReceiveLog(request, receiveHeader, detail);
		// 调用库存函数
		stockBiz.inStock(StockLogTypeEnum.INSTOCK_BY_PCS, receiveLog);
		// 更新收货单明细状态
		receiveBiz.updateReceiveDetail(detail, request.getSurplusQty());
		// 更新收货单状态
		receiveBiz.updateReceiveHeader(receiveHeader, detail);
		// 记录业务日志
		receiveBiz.log(StockLogTypeEnum.INSTOCK_BY_PCS.getDesc(), receiveHeader, detail, receiveLog);
		//检查收货是否完成 并返回
		return receiveBiz.checkByPcsReceive(request.getReceiveDetailId(), receiveLog.getReceiveId());
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void receiveByMultiBoxCode(ReceiveDetailLpnPdaMultiRequest receiveDetailLpnPdaMultiRequest) {
		//查询收货库位，如果是发货接驳区或者出库集货区则抛异常
		canReceiveByLocation(receiveDetailLpnPdaMultiRequest.getWhId(), receiveDetailLpnPdaMultiRequest.getLocCode());
		if (Func.isNotEmpty(receiveDetailLpnPdaMultiRequest.getLpnCode())) {
			List<Stock> stocks = stockQueryBiz.findStockByLpnCode(receiveDetailLpnPdaMultiRequest.getLpnCode());
			if (Func.notNull(stocks) && stocks.size() > 0) {
				throw new ServiceException("LPN[" + receiveDetailLpnPdaMultiRequest.getLpnCode() + "]已存在库存数据！");
			}
		}
		// 判断lpnCode是否为空，如果为空则随机生成一个lpnCode
		if (Func.isEmpty(receiveDetailLpnPdaMultiRequest.getLpnCode())) {
			receiveDetailLpnPdaMultiRequest.setLpnCode(receiveDetailLpnPdaMultiRequest.getReceiveDetailLpnPdaRequestList().get(0).getBoxCode());
		}
		// 循环调用按箱收货业务方法
		for (ReceiveDetailLpnPdaRequest item : receiveDetailLpnPdaMultiRequest.getReceiveDetailLpnPdaRequestList()) {
			item.setLpnCode(receiveDetailLpnPdaMultiRequest.getLpnCode());
			item.setLocCode(receiveDetailLpnPdaMultiRequest.getLocCode());
			item.setSkuLot1(receiveDetailLpnPdaMultiRequest.getSkuLot1());
			item.setSkuLot2(receiveDetailLpnPdaMultiRequest.getSkuLot2());
			item.setSkuLot4(receiveDetailLpnPdaMultiRequest.getSkuLot4());
			item.setWhId(receiveDetailLpnPdaMultiRequest.getWhId());
			receiveByDuoBoxCode(item, StockLogTypeEnum.INSTOCK_BY_MULTI_BOX.getDesc());
		}


	}

	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	@Override
	public void receiveByDuoBoxCode(ReceiveDetailLpnPdaRequest request, String logType) {
		boolean hasReceiveHeaderId = Func.isNotEmpty(request.getReceiveHeaderId());
		ReceiveHeader receiveHeader;
		// 判断业务参数（无单收货除外），是否可以正常收货、超收
		if (hasReceiveHeaderId) {
			for (ReceiveDetailLpnItemDto item : request.getReceiveDetailLpnItemDtoList()) {
				ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
				ReceiveHeader header = receiveBiz.selectReceiveHeaderById(detail.getReceiveId());
				receiveBiz.canReceive(header, detail, item.getPlanQty());
				//查询收货库位，如果是发货接驳区或者出库集货区则抛异常
				canReceiveByLocation(request.getWhId(), request.getLocCode());
			}
		} else {
			// 是否无单收货：判断当前用户是否有无单收货且未关闭的单据，如果有则用这个收货单（如果多个取最后一个）并新建收货单明细
			List<ReceiveHeader> receiveHeaderList = receiveBiz.getReceiveListByNonOrder(AuthUtil.getUserId());
			// 订单行号
			String lineNo = "0";
			int lineNum = 0;
			if (Func.isNotEmpty(receiveHeaderList)) {
				//取无单收货最后一条收货单
				receiveHeader = receiveHeaderList.get(receiveHeaderList.size() - 1);
				//获取当前收货单关联的最新一条明细的行号
				lineNo = receiveBiz.getReceiveDetailLinNo(receiveHeader.getReceiveId());
			} else {
				//否则新建一个收货单
				receiveHeader = receiveFactory.createReceiveHeader(request);
				receiveBiz.newReceiveHeader(receiveHeader);
			}
			// 设置request的头表id
			request.setReceiveHeaderId(receiveHeader.getReceiveId());
			if (Func.isNotEmpty(lineNo)) {
				lineNum = Integer.parseInt(lineNo);
			}
			//循环生成明细
			for (ReceiveDetailLpnItemDto item : request.getReceiveDetailLpnItemDtoList()) {
				lineNum += 10;
				//根据id获取lpn实体
				ReceiveDetailLpn lpn = receiveBiz.getReceiveDetailLpnById(item.getReceiveDetailLpnId());
				//创建收货单明细
				ReceiveDetail receiveDetail = receiveFactory.createReceiveDetail(request, item, lpn, receiveHeader, lineNum);
				receiveBiz.newReceiveDetail(receiveDetail);
				//更新lpn表的明细id
				lpn.setReceiveDetailId(receiveDetail.getReceiveDetailId());
				//更新lpn表头表id
				lpn.setReceiveHeaderId(receiveHeader.getReceiveId());
				receiveBiz.updateReceiveDetailLpn(lpn);
				//设置request的明细id
				item.setReceiveDetailId(receiveDetail.getReceiveDetailId());
			}

		}
		List<Stock> stockList = new ArrayList<>();
		for (ReceiveDetailLpnItemDto item : request.getReceiveDetailLpnItemDtoList()) {
			ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
			ReceiveHeader header = receiveBiz.selectReceiveHeaderById(detail.getReceiveId());
			//根据id获取lpn实体
			ReceiveDetailLpn lpn = receiveBiz.getReceiveDetailLpnById(item.getReceiveDetailLpnId());
			//更新lpn表批属性信息
			lpn.setSkuLot1(request.getSkuLot1());
			lpn.setSkuLot2(request.getSkuLot2());
			//更新lpn实收数量
			lpn.setScanQty(lpn.getScanQty().add(item.getPlanQty()));
			//更新lpn状态
			lpn.setDetailStatus(ReceiveDetailStatusEnum.COMPLETED);
			receiveBiz.updateReceiveDetailLpn(lpn);
			// 生成清点记录
			ReceiveLog receiveLog = receiveLogBiz.newReceiveLog(request, item, lpn, header, detail);
			// 调用库存函数（for begin：一条执行一次）
			stockList.add(stockBiz.inStock(StockLogTypeEnum.INSTOCK_BY_BOX, receiveLog));
			//有单收货更新头表和明细信息
			if (hasReceiveHeaderId) {
				// 更新收货单明细状态（for end：一条执行一次）
				receiveBiz.updateReceiveDetail(detail, item.getPlanQty());
				// 更新收货单状态
				receiveBiz.updateReceiveHeader(header, detail);
			}
			// 记录业务日志
			if (Func.isEmpty(logType)) {
				//传入参数为空设置为按箱收货类型
				logType = StockLogTypeEnum.INSTOCK_BY_BOX.getDesc();
			}
			receiveBiz.log(logType, header, detail, receiveLog);

		}
		agvTask.putwayToSchedule(stockList);

	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelReceive(List<Long> receiveIdList) {
		// 是否可以撤销
		List<ReceiveLog> receiveLogList = receiveLogBiz.canCancelReceive(receiveIdList);
		// 生成撤销的清点记录
		List<ReceiveLog> newReceiveLogList = receiveLogBiz.newReceiveLog(receiveLogList);
		newReceiveLogList.forEach(item -> {
			// 下架库存
			Stock stock = stockQueryBiz.findStockOnStage(item);
			stockBiz.outStockByCancelReceive(StockLogTypeEnum.OUTSTOCK_BY_CANCEL_RECEIVE, item, stock);
			// 更新收货单明细
			ReceiveDetail receiveDetail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
			receiveBiz.updateReceiveDetail(receiveDetail, item.getQty());
			// 更新收货单头表信息
			ReceiveHeader receiveHeader = receiveBiz.getReceiveHeaderById(item.getReceiveId());
			receiveBiz.updateReceiveHeader(receiveHeader, receiveDetail);
			// 生成业务日志
			receiveBiz.log(StockLogTypeEnum.OUTSTOCK_BY_CANCEL_RECEIVE.getDesc(), receiveHeader, receiveDetail, item);
		});
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public String receiveByPc(ReceiveByPcRequest receiveByPcRequest) {
		//根据id获取头表信息
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(receiveByPcRequest.getReceiveId());
		List<ReceiveByPcDetailRequest> detailList = receiveByPcRequest.getDetailRequestList();
		//前端传入明细集合按明细id分组
		Map<Long, List<ReceiveByPcDetailRequest>> detailMap = detailList.stream().collect(Collectors.groupingBy(ReceiveByPcDetailRequest::getReceiveDetailId));
		//所有明细全部完成收货标志
		boolean isCompleted = true;
		for (Map.Entry<Long, List<ReceiveByPcDetailRequest>> entry : detailMap.entrySet()) {
			//获取每个分组的明细对象
			ReceiveDetail receiveDetail = receiveDetailDao.getDetailByReceiveDetailId(entry.getKey());
			//获取每个分组的集合
			List<ReceiveByPcDetailRequest> detailRequestList = entry.getValue();
			//统计总数
			BigDecimal sum = new BigDecimal(0);
			for (ReceiveByPcDetailRequest detailRequest : detailRequestList) {
				//参数校验
				receiveBiz.canReceive(receiveHeader, receiveDetail, detailRequest.getScanQty());
				Location targetLocation = locationBiz.findByLocId(detailRequest.getLocId());
				//查询收货库位，如果是发货接驳区或者出库集货区则抛异常
				canReceiveByLocation(targetLocation.getWhId(), targetLocation.getLocCode());
				//生成清点记录
				ReceiveLog receiveLog = receiveLogFactory.createReceiveLog(detailRequest, receiveHeader, receiveDetail);
				receiveLogBiz.saveReceiveLog(receiveLog);
				//调用库存函数
				stockBiz.inStock(StockLogTypeEnum.INSTOCK_BY_PC, receiveLog);
				//总数累加
				sum = sum.add(detailRequest.getScanQty());
			}
			//修改收货单明细状态和剩余数量
			receiveDetail.setSurplusQty(receiveDetail.getSurplusQty().subtract(sum));
			receiveDetail.setScanQty(receiveDetail.getScanQty().add(sum));
			if (BigDecimalUtil.gt(receiveDetail.getSurplusQty(), BigDecimal.ZERO)) {
				receiveDetail.setDetailStatus(ReceiveDetailStatusEnum.PART);
				isCompleted = false;
			} else {
				receiveDetail.setDetailStatus(ReceiveDetailStatusEnum.COMPLETED);
			}
			receiveDetailDao.updateReceiveDetail(receiveDetail);

		}
		//更新头表状态
		receiveHeader.setBillState(isCompleted
			? ReceiveHeaderStateEnum.COMPLETED
			: ReceiveHeaderStateEnum.PART);

		receiveHeaderDao.updateReceive(receiveHeader);

		//记录日志
		logBiz.auditLog(AuditLogType.INSTOCK, receiveHeader.getReceiveId(), receiveHeader.getReceiveNo(), "PC收货");
		return receiveHeader.getReceiveNo();
	}

	/**
	 * 查询收货库位，如果是发货接驳区或者出库集货区则抛异常
	 *
	 * @param targetLocCode targetLocCode
	 */
	private void canReceiveByLocation(Long whId, String targetLocCode) {
		Location targetLocation = locationBiz.findLocationByLocCode(whId, targetLocCode);
		if (locationBiz.isPickToLocation(targetLocation)) {
			throw new ServiceException("收货失败，收货不能收到发货接驳区");
		}
		if (locationBiz.isAgvTempOfZoneType(targetLocation.getLocId())) {
			throw new ServiceException("收货失败，收货不能收到出库集货区");
		}
	}
}
