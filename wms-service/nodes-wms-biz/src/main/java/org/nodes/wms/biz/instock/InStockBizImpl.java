package org.nodes.wms.biz.instock;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receive.modular.ReceiveFactory;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.PdaByPieceReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnItemDto;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnPdaResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class InStockBizImpl implements InStockBiz {
    private  final ReceiveBiz receiveBiz;
	private final ReceiveFactory receiveFactory;
	private final StockBiz stockBiz;

	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	@Override
	public void receiveByBoxCode(ReceiveDetailLpnPdaRequest request) {
		boolean hasReceiveHeaderId = Func.isNotEmpty(request.getReceiveHeaderId());
		// 判断业务参数（无单收货除外），是否可以正常收货、超收
        if(hasReceiveHeaderId){
          for(ReceiveDetailLpnItemDto item: request.getReceiveDetailLpnItemDtoList()){
			  receiveBiz.canReceive(item.getReceiveDetailId(),item.getPlanQty());
		  }
		}else{
			// 是否无单收货：判断当前用户是否有无单收货且未关闭的单据，如果有则用这个收货单（如果多个取最后一个）并新建收货单明细
			List<ReceiveHeader> receiveHeaderList = receiveBiz.getReceiveListByNonOrder(AuthUtil.getUserId());
			// 订单行号
			String lineNo = "0";
			int lineNum = 0;
			ReceiveHeader receiveHeader = new ReceiveHeader();
			if(Func.isNotEmpty(receiveHeaderList)){
				//取无单收货最后一条收货单
				  receiveHeader = receiveHeaderList.get(receiveHeaderList.size()-1);
				//获取当前收货单关联的最新一条明细的行号
				lineNo = receiveBiz.getReceiveDetailLinNo(receiveHeader.getReceiveId());
			}else{
				//否则新建一个收货单
				 receiveHeader = receiveFactory.createReceiveHeader(request);
				receiveBiz.newReceiveHeader(receiveHeader);
			}
			if(Func.isNotEmpty(lineNo)){
				lineNum = Integer.parseInt(lineNo);
			}
			for(ReceiveDetailLpnItemDto item: request.getReceiveDetailLpnItemDtoList()){
				lineNum+=10;
				//根据id获取lpn实体
				ReceiveDetailLpn lpn = receiveBiz.getReceiveDetailLpnById(item.getReceiveDetailLpnId());
				//创建收货单明细
				ReceiveDetail receiveDetail = receiveFactory.createReceiveDetail(request,item,lpn,receiveHeader,lineNum);
				receiveBiz.newReceiveDetail(receiveDetail);
				//更新lpn表的明细id
				lpn.setReceiveDetailId(receiveDetail.getReceiveDetailId());
				receiveBiz.updateReceiveDetailLpn(lpn);
			}

		}

		// 调用库存函数（for begin：一条执行一次）
		// 生成清点记录
		// 更新收货单明细状态（for end：一条执行一次）

		// 更新收货单（无单收货除外）状态
		// 记录业务日志
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public PdaByPieceReceiveResponse receiptByPiece(PdaByPieceReceiveRequest request) {
		// 判断业务参数，是否可以正常收货、超收
		receiveBiz.canReceive(request.getReceiveDetailId(), request.getSurplusQty());
		// 调用库存函数

		// 生成清点记录
		// 更新收货单明细状态
		receiveBiz.updateReceiveDetail(request.getReceiveDetailId(), request.getSurplusQty());
		// 更新收货单状态
		receiveBiz.updateReciveHeader(request.getReceiveDetailId());
		// 记录业务日志
		receiveBiz.log(request.getReceiveId(),request.getReceiveDetailId(),request.getSurplusQty(),request.getSkuLot1());
		return null;
	}
}
