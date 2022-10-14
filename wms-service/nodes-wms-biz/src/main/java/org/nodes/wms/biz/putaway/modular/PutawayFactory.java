package org.nodes.wms.biz.putaway.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.putaway.dto.input.PutwayByBoxRequest;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.secure.utils.AuthUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 上架日志工厂
 *
 * @author T
 */
@Component
@RequiredArgsConstructor
public class PutawayFactory {
	public PutawayLog create(PutwayByBoxRequest request, Stock sourceStock, Location targetLocation) {
		PutawayLog putawayLog = new PutawayLog();
		putawayLog.setLpnCode(sourceStock.getLpnCode());
		putawayLog.setBoxCode(sourceStock.getBoxCode());
		putawayLog.setTargetLocCode(targetLocation.getLocCode());
		putawayLog.setWhId(request.getWhId());
		putawayLog.setUserName(AuthUtil.getUserName());
		putawayLog.setUserCode(AuthUtil.getUserAccount());
		putawayLog.setAplTime(LocalDateTime.now());

		putawayLog.setSkuId(sourceStock.getSkuId());
		putawayLog.setSkuCode(sourceStock.getSkuCode());
		putawayLog.setSkuName(sourceStock.getSkuName());
		putawayLog.setQty(sourceStock.getStockBalance());
		SkuLotUtil.setAllSkuLot(sourceStock, putawayLog);
		return putawayLog;
	}

	public PutawayLog create(Stock stock, Location targetLoc) {
		PutawayLog putawayLog = new PutawayLog();
		putawayLog.setLpnCode(stock.getLpnCode());
		putawayLog.setBoxCode(stock.getBoxCode());
		putawayLog.setTargetLocCode(targetLoc.getLocCode());
		putawayLog.setWhId(targetLoc.getWhId());
		putawayLog.setUserName("AGV");
		putawayLog.setUserCode("AGV");
		putawayLog.setAplTime(LocalDateTime.now());
		putawayLog.setSkuId(stock.getSkuId());
		putawayLog.setSkuCode(stock.getSkuCode());
		putawayLog.setSkuName(stock.getSkuName());
		putawayLog.setQty(stock.getStockBalance());
		SkuLotUtil.setAllSkuLot(stock, putawayLog);
		return putawayLog;
	}

}
