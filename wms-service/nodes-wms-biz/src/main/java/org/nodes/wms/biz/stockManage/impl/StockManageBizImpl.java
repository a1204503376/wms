package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockManageBizImpl implements StockManageBiz {
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final LocationBiz locationBiz;
	private final SkuBiz skuBiz;


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
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void stockMove(StockMoveRequest request) {
		// TODO库存标准移动没写全
		//根据skuCode获取SKU对象
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		//根据locCode获取Location对象
		Location location = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Long> locationIdList=new ArrayList<>();
		locationIdList.add(location.getLocId());
		//批属性赋值
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getLotNumber());
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocation(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		AssertUtil.notNull(stockList, "根据您输入的数据查询不到对应的库存，请输入后重试");
       stockList.forEach(stock -> {
		   //获取序列号 如果库存关联了序列号需要获取序列号
		   List<Serial> serialList = stockQueryBiz.findSerialByStock(stock.getStockId());
		   List<String> serialNoList = new ArrayList<>();
		   if (Func.isNotEmpty(serialList)) {
			   serialNoList = serialList.stream()
				   .map(Serial::getSerialNumber)
				   .collect(Collectors.toList());
		   }
//		   stockBiz.moveStock(stock,S);
	   });

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

	@Override
	public EstimateStockMoveResponse skuIsSn(EstimateStockMoveRequest request) {
		//根据skuCode获取SKU对象
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		EstimateStockMoveResponse response=new EstimateStockMoveResponse();
		response.setIsSn(sku.getIsSn()>0);
		return response;
	}
}
