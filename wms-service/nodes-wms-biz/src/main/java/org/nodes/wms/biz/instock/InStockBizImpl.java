package org.nodes.wms.biz.instock;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receive.modular.ReceiveFactory;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaMultiRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.PdaByPcsReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnItemDto;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InStockBizImpl implements InStockBiz {
	private final ReceiveBiz receiveBiz;
	private final ReceiveFactory receiveFactory;
	private final StockBiz stockBiz;
	private final ReceiveLogBiz receiveLogBiz;


	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	@Override
	public void receiveByBoxCode(ReceiveDetailLpnPdaRequest request) {
		boolean hasReceiveHeaderId = Func.isNotEmpty(request.getReceiveHeaderId());
		ReceiveHeader receiveHeader = new ReceiveHeader();
		// 判断业务参数（无单收货除外），是否可以正常收货、超收
		if (hasReceiveHeaderId) {
			for (ReceiveDetailLpnItemDto item : request.getReceiveDetailLpnItemDtoList()) {
				ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
				ReceiveHeader header = receiveBiz.selectReceiveHeaderById(detail.getReceiveId());
				receiveBiz.canReceive(header, detail, item.getPlanQty());
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
				receiveBiz.updateReceiveDetailLpn(lpn);
				//设置request的明细id
				item.setReceiveDetailId(receiveDetail.getReceiveDetailId());
			}

		}
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
			receiveBiz.updateReceiveDetailLpn(lpn);
			// 生成清点记录
			ReceiveLog receiveLog = receiveLogBiz.newReceiveLog(request, item, lpn, header, detail);
			// 调用库存函数（for begin：一条执行一次）
			stockBiz.inStock(StockLogTypeEnum.INSTOCK_BY_BOX, receiveLog);
			//有单收货更新头表和明细信息
			if (hasReceiveHeaderId) {
				// 更新收货单明细状态（for end：一条执行一次）
				receiveBiz.updateReceiveDetail(detail, item.getPlanQty());
				// 更新收货单状态
				receiveBiz.updateReciveHeader(header, detail);
				// 记录业务日志
				receiveBiz.log(request.getReceiveHeaderId(), item.getReceiveDetailId(), item.getPlanQty(), request.getSkuLot1(), header, detail);
			} else {
				// 记录业务日志
				receiveBiz.log(receiveHeader.getReceiveId(), item.getReceiveDetailId(), item.getPlanQty(), request.getSkuLot1(), header, detail);
			}

		}


	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public PdaByPcsReceiveResponse receiptByPcs(PdaByPieceReceiveRequest request) {
		ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(request.getReceiveDetailId());
		ReceiveHeader receiveHeader = receiveBiz.selectReceiveHeaderById(request.getReceiveId());
		// 判断业务参数，是否可以正常收货、超收
		receiveBiz.canReceive(receiveHeader, detail, request.getSurplusQty());
		// 生成清点记录
		ReceiveLog receiveLog = receiveLogBiz.newReceiveLog(request, receiveHeader, detail);
		// 调用库存函数
		stockBiz.inStock(StockLogTypeEnum.INSTOCK_BY_PCS, receiveLog);
		// 更新收货单明细状态
		receiveBiz.updateReceiveDetail(detail, request.getSurplusQty());
		// 更新收货单状态
		receiveBiz.updateReciveHeader(receiveHeader, detail);
		// 记录业务日志
		receiveBiz.log(request.getReceiveId(), request.getReceiveDetailId(), request.getSurplusQty(), request.getSkuLot1(), receiveHeader, detail);
		//检查收货是否完成 并返回
		return receiveBiz.checkByPcsReceive(request.getReceiveDetailId(), receiveLog.getReceiveId());
	}

	@Override
	public void receiveByMultiBoxCode(ReceiveDetailLpnPdaMultiRequest receiveDetailLpnPdaMultiRequest) {
		// 判断lpnCode是否为空，如果为空则随机生成一个lpnCode
		if (Func.isEmpty(receiveDetailLpnPdaMultiRequest.getLpnCode())) {
			receiveDetailLpnPdaMultiRequest.setLpnCode(Func.randomUUID());
		}
		// 循环调用按箱收货业务方法
		for (ReceiveDetailLpnPdaRequest item : receiveDetailLpnPdaMultiRequest.getReceiveDetailLpnPdaRequestList()) {
			item.setLpnCode(receiveDetailLpnPdaMultiRequest.getLpnCode());
			item.setLocCode(receiveDetailLpnPdaMultiRequest.getLocCode());
			item.setSkuLot1(receiveDetailLpnPdaMultiRequest.getSkuLot1());
			item.setSkuLot2(receiveDetailLpnPdaMultiRequest.getSkuLot2());
			item.setWhId(receiveDetailLpnPdaMultiRequest.getWhId());
			receiveByBoxCode(item);
		}


	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelReceive(List<Long> receiveIdList) {
		// 是否可以撤销
		List<ReceiveLog> receiveLogList = receiveLogBiz.canCancelReceive(receiveIdList);
		// 生成撤销的清点记录
		List<ReceiveLog> newReceiveLogList = receiveLogBiz.newReceiveLog(receiveLogList);
		newReceiveLogList.forEach(item -> {
//			List<ReceiveLog> receiveLogList = receiveLogBiz.findReceiveLog(receiveIdList);
			// 下架库存
			Stock stock = stockBiz.findStockOnStage(item);
			stockBiz.outStockByCancleReceive(StockLogTypeEnum.OUTSTOCK_BY_CANCEL_RECEIVE, item, stock);
			// 更新收货单明细
			ReceiveDetail receiveDetail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
			receiveBiz.updateReceiveDetail(receiveDetail, item.getQty());
			// 更新收货单头表信息
			ReceiveHeader receiveHeader = receiveBiz.getReceiveHeaderById(item.getReceiveId());
			receiveBiz.updateReciveHeader(receiveHeader, receiveDetail);
			// 生成业务日志
			receiveBiz.log(
				receiveHeader.getReceiveId(), receiveDetail.getReceiveDetailId(),
				item.getQty(), receiveDetail.getSkuLot1(), receiveHeader, receiveDetail);
		});
	}
}
