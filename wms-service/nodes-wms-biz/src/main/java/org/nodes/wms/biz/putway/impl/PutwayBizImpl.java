package org.nodes.wms.biz.putway.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putway.PutwayBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.putway.PutawayLogDao;
import org.nodes.wms.dao.putway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putway.dto.input.NewPutawayByBoxlRequest;
import org.nodes.wms.dao.putway.dto.output.BoxDto;
import org.nodes.wms.dao.putway.dto.output.LocResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PutwayBizImpl implements PutwayBiz {
	private LocationBiz locationBiz;
	private StockBiz stockBiz;
	private PutawayLogDao putawayLogDao;

	@Override
	public void addByBoxShelf(NewPutawayByBoxlRequest request) {
		// 调用库存移动，如果关联了序列号需要获取序列号
		// 生成上架记录
	}

    @Override
    public void callAgv(CallAgvRequest request) {
		List<BoxDto> boxDtoList = request.getBoxList();
		for(BoxDto boxDto:boxDtoList){
			List<Long> stockIdList = boxDto.getStockIdList();
			for(Long stockId:stockIdList){
				// 根据id获取库存实体
              Stock stock = stockBiz.findStockById(stockId);
			  //获取数量
			  BigDecimal qty = StockUtil.getStockBalance(stock);
			    //获取序列号
				List<Serial> serialList = stockBiz.findSerialByStock(stockId);
				List<String> serialNoList = new ArrayList<>();
				if(Func.isNotEmpty(serialList)){
					serialNoList = serialList.stream()
						.map(Serial::getSerialNumber)
						.collect(Collectors.toList());
				}
				//获取库位信息
				Location targetLocation = locationBiz.findByLocId(stock.getLocId());
				// 调用库存移动
				stockBiz.moveStock(stock,serialNoList,qty,targetLocation, StockLogTypeEnum.STOCK_TO_INSTOCK_RECE,null,null,null);
				// 生成上架记录

			}
		}


    }

	@Override
	public List<LocResponse> findLocByLpnType(LpnTypeRequest request) {
		List<LocResponse> locResponseList = new ArrayList<>();
		// 根据箱型和库房id获取库位信息
		List<Location> locationList = locationBiz.findLocationByLpnType(request);
		for(Location location:locationList){
			LocResponse locResponse = new LocResponse();
			locResponse.setLocId(location.getLocId());
			locResponse.setLocCode(location.getLocCode());
			// 库位是否为空
			locResponse.setIsEmpty(stockBiz.judgeEnableOnLocation(location));
			locResponseList.add(locResponse);
		}
		return locResponseList;

	}
}
