package org.nodes.wms.biz.stockControl.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.stockControl.StockControlBiz;
import org.nodes.wms.dao.stock.dto.input.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockControlBizImpl implements StockControlBiz {
	@Override
	public void freezeByLocCodeAction(FreezeByLocCodeRequest request) {

	}

	@Override
	public void freezeByLotNumberAction(FreezeByLotNumberRequest request) {

	}

	@Override
	public void freezeBySerialNumberAction(FreezeBySerialNumberRequest request) {

	}

	@Override
	public void portionFreezeAction(PortionFreezeRequest request) {

	}

	@Override
	public void unFreezeByLocCodeAction(UnFreezeByLocCodeRequest request) {

	}

	@Override
	public void unFreezeByLotNumberAction(UnFreezeByLotNumberRequest request) {

	}

	@Override
	public void unFreezeBySerialNumberAction(UnFreezeBySerialNumberRequest request) {

	}

	@Override
	public void portionUnFreezeAction(PortionUnFreezeRequest request) {

	}
}
