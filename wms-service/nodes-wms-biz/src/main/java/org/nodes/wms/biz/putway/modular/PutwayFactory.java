package org.nodes.wms.biz.putway.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.AddByBoxShelfRequest;
import org.nodes.wms.dao.putway.entities.PutawayLog;
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
public class PutwayFactory {
	public PutawayLog create(AddByBoxShelfRequest request, Stock sourceStock, Location targetLocation) {
		PutawayLog putawayLog = new PutawayLog();
		putawayLog.setLpnCode(sourceStock.getLpnCode());
		putawayLog.setTargetLocCode(targetLocation.getLocCode());
		putawayLog.setWhId(request.getWhId());
		putawayLog.setUserName(AuthUtil.getUserName());
		putawayLog.setUserCode(AuthUtil.getUserAccount());
		putawayLog.setAplTime(LocalDateTime.now());
		return putawayLog;
	}
}
