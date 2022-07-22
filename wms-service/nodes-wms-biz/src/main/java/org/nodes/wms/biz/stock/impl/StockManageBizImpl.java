package org.nodes.wms.biz.stock.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockManageBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveByBoxCodeResponse;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveByLpnCodeResponse;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockManageBizImpl implements StockManageBiz {
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;

	@Override
	public EstimateStockMoveResponse estimateStockMoveAction(EstimateStockMoveRequest request) {
		//需要把原库存ID返回给前端
		return null;
	}

	@Override
	public void stockMoveAction(StockMoveRequest request) {
		//根据请求对象获取对应库存移动需要的值
		//根据库存ID获取原库存
		//获取序列号集合 移动数量,库存移动类型
		//获取目标库位信息
	}

	@Override
	public EstimateStockMoveByLpnCodeResponse estimateStockMoveByLpnAction(EstimateStockMoveByLpnCodeRequest request) {
		return null;
	}

	@Override
	public void stockMoveByLpnAction(StockMoveByLpnRequest request) {

	}

	@Override
	public EstimateStockMoveByBoxCodeResponse estimateStockMoveByBoxCodeAction(EstimateStockMoveByBoxCodeRequest request) {
		//根据提交上来的箱码集合，查询对应的
		return null;
	}

	@Override
	public void stockMoveByBoxCodeAction(StockMoveByBoxCodeRequest request) {

	}
}
