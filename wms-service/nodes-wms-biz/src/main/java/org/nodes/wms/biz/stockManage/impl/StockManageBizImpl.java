package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.dao.stock.dto.input.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockManageBizImpl implements StockManageBiz {


	@Override
	public void freezeByLocCodeAction(String locCode) {

	}

	@Override
	public void freezeByLotNumberAction(String lotNumber) {

	}

	@Override
	public void freezeBySerialNumberAction(String serialNumber) {

	}

	@Override
	public void portionFreezeAction(PortionFreezeRequest request) {

	}

	@Override
	public void unFreezeByLocCodeAction(String locCode) {

	}

	@Override
	public void unFreezeByLotNumberAction(String lotNumber) {

	}

	@Override
	public void unFreezeBySerialNumberAction(String serialNumber) {

	}

	@Override
	public void portionUnFreezeAction(PortionUnFreezeRequest request) {

	}

	@Override
	public void stockMove(StockMoveRequest request) {
		//根据请求对象获取对应库存移动需要的值
		//根据库存ID获取原库存
		//获取序列号集合 移动数量,库存移动类型
		//获取目标库位信息
	}


	@Override
	public void stockMoveByLpn(StockMoveByLpnRequest request) {

	}


	@Override
	public void stockMoveByBox(StockMoveByBoxCodeRequest request) {

	}
}
