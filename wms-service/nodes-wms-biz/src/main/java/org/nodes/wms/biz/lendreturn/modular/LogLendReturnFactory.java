package org.nodes.wms.biz.lendreturn.modular;

import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.input.LogLendReturnRequest;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 借出归还工厂
 * 主要用于创建借出归还实体
 */
@Service
public class LogLendReturnFactory {

	public List<LogLendReturn> createLendList(List<LogLendReturnRequest> logLendReturnRequestList) {
		AssertUtil.notEmpty(logLendReturnRequestList, "借出请求对象集合为空");
		List<LogLendReturn> logLendReturnList = Func.copy(logLendReturnRequestList, LogLendReturn.class);
		logLendReturnList.forEach(this::setLendType);
		return logLendReturnList;
	}

	public List<LogLendReturn> createReturnList(List<LogLendReturnRequest> returnRequestList) {
		AssertUtil.notEmpty(returnRequestList, "归还请求对象集合为空");
		List<LogLendReturn> logLendReturnList = Func.copy(returnRequestList, LogLendReturn.class);
		logLendReturnList.forEach(this::setReturnType);
		return logLendReturnList;
	}

	private void setReturnType(LogLendReturn logLendReturn) {
		logLendReturn.setType(LendReturnTypeEnum.RETURN);
	}

	private void setLendType(LogLendReturn logLendReturn) {
		logLendReturn.setType(LendReturnTypeEnum.LEND);
	}

	public LendReturnRequest createLendRequest(SoHeader soHeader, List<LogSoPick> logSoPickList) {
		List<LogLendReturnRequest> logLendReturnList = new ArrayList<>();
		logSoPickList.forEach(item -> {
			LogLendReturnRequest logLendReturnRequest = new LogLendReturnRequest();
			logLendReturnRequest.setType(LendReturnTypeEnum.LEND);
			logLendReturnRequest.setLendReturnName(soHeader.getContact());
			logLendReturnRequest.setLocId(item.getLocId());
			logLendReturnRequest.setLocCode(item.getLocCode());
			logLendReturnRequest.setSkuId(item.getSkuId());
			logLendReturnRequest.setSkuCode(item.getSkuCode());
			logLendReturnRequest.setSkuName(item.getSkuName());
			logLendReturnRequest.setQty(item.getPickRealQty());
			logLendReturnRequest.setWsuCode(item.getWsuCode());
			logLendReturnRequest.setWsuName(item.getWsuName());
			logLendReturnRequest.setBoxCode(item.getBoxCode());
			logLendReturnRequest.setSnCode(item.getSnCode());
			logLendReturnRequest.setBillId(item.getSoBillId());
			logLendReturnRequest.setBillNo(item.getSoBillNo());
			logLendReturnRequest.setDetailId(item.getSoDetailId());
			logLendReturnRequest.setWoId(item.getWoId());
			logLendReturnRequest.setWhId(item.getWhId());
			SkuLotUtil.setAllSkuLot(item, logLendReturnRequest);
			logLendReturnList.add(logLendReturnRequest);
		});
		LendReturnRequest lendReturnRequest = new LendReturnRequest();
		lendReturnRequest.setType(LendReturnTypeEnum.LEND);
		lendReturnRequest.setLogLendReturnRequestList(logLendReturnList);
		lendReturnRequest.setBillTypeCd(WmsAppConstant.BILL_TYPE_LEND);
		return lendReturnRequest;
	}

	public LendReturnRequest createReturnRequest(ReceiveHeader receiveHeader, List<ReceiveLog> receiveLogList) {
		List<LogLendReturnRequest> logLendReturnList = new ArrayList<>();
		receiveLogList.forEach(item -> {
			LogLendReturnRequest logLendReturnRequest = new LogLendReturnRequest();
			logLendReturnRequest.setType(LendReturnTypeEnum.RETURN);
			logLendReturnRequest.setLendReturnName(receiveHeader.getSupplierContact());
			logLendReturnRequest.setLocId(item.getLocId());
			logLendReturnRequest.setLocCode(item.getLocCode());
			logLendReturnRequest.setSkuId(item.getSkuId());
			logLendReturnRequest.setSkuCode(item.getSkuCode());
			logLendReturnRequest.setSkuName(item.getSkuName());
			logLendReturnRequest.setQty(item.getQty());
			logLendReturnRequest.setWsuCode(item.getWsuCode());
//			logLendReturnRequest.setWsuName(item.getWsuName());
			logLendReturnRequest.setBoxCode(item.getBoxCode());
			logLendReturnRequest.setSnCode(item.getSnCode());
			logLendReturnRequest.setBillId(item.getReceiveId());
			logLendReturnRequest.setBillNo(item.getReceiveNo());
			logLendReturnRequest.setDetailId(item.getReceiveDetailId());
			logLendReturnRequest.setWoId(item.getWoId());
			logLendReturnRequest.setWhId(item.getWhId());
			SkuLotUtil.setAllSkuLot(item, logLendReturnRequest);
			logLendReturnList.add(logLendReturnRequest);
		});
		LendReturnRequest lendReturnRequest = new LendReturnRequest();
		lendReturnRequest.setType(LendReturnTypeEnum.RETURN);
		lendReturnRequest.setLogLendReturnRequestList(logLendReturnList);
		lendReturnRequest.setBillTypeCd(WmsAppConstant.BILL_TYPE_RETURN);
		return lendReturnRequest;
	}
}
