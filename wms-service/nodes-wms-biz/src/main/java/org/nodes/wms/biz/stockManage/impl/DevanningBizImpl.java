package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.DevanningBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.stock.dto.input.DevanningSubmitRequest;
import org.nodes.wms.dao.stock.dto.input.FindAllSerialNumberManageRequest;
import org.nodes.wms.dao.stock.dto.output.DevanningStockResponse;
import org.nodes.wms.dao.stock.dto.output.FindAllSerialNumberManageResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Service
@RequiredArgsConstructor
public class DevanningBizImpl implements DevanningBiz {
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final SerialBiz serialBiz;
	private final LpnTypeBiz lpnTypeBiz;
	private final LocationBiz locationBiz;

	@Override
	public FindAllSerialNumberManageResponse getAllSerialNumberManage(FindAllSerialNumberManageRequest request) {
		FindAllSerialNumberManageResponse response = new FindAllSerialNumberManageResponse();
		//根据箱码查询库存
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		//返回是否是序列号管理----根据序列号列表是否为空进行判断，如果不是则需要查询出所有跟当前托盘有关的库存进行返回
		List<String> serialNumberList = new ArrayList<>();
		for (Stock stock : stockList) {
			List<String> serialBizSerialNoByStockId = serialBiz.findSerialNoByStockId(stock.getStockId());
			serialNumberList.addAll(serialBizSerialNoByStockId);
		}

		if (Func.isNotEmpty(serialNumberList)) {
			//序列号不为空赋值响应对象-序列号集合赋值、并且将序列号集合判断变为true、将库存响应对象置空
			response.setIsSn(true);
			response.setSerialNumberList(serialNumberList);
			response.setStockList(null);
		} else {
			//序列号为空赋值响应对象-序列号集合置空、并且将序列号集合判断变为false、库存响应对象集合
			response.setIsSn(false);
			response.setSerialNumberList(null);
			List<DevanningStockResponse> devanningStockList = BeanUtil.copy(stockList, DevanningStockResponse.class);
			response.setStockList(devanningStockList);
		}
		return response;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void devanning(DevanningSubmitRequest request) {
		//根据前端输入的locCod查询出对应的目标库位
		Location location = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		//是否生成新箱码 是true进入下面方法生成新箱码
		if (request.getNewBoxCode()) {
			LpnTypeCodeEnum lpnTypeCodeEnum = lpnTypeBiz.tryParseBoxCode(request.getBoxCode());
			request.setBoxCode(lpnTypeBiz.generateLpnCode(lpnTypeCodeEnum.getCode()));
		}
		//提交二合一
		//首先判断是否序列号管理执行序列号拆箱
		if (request.getIsSn()) {
			List<String> serialNumberList = request.getSerialNumberList();
			AssertUtil.notNull(serialNumberList, "根据序列号拆箱时,未采集序列号");
			//根据页面传过来的String序列号转化成 在库的序列号实体 集合
			List<Serial> serialList = stockQueryBiz.findSerialBySerialNo(serialNumberList);
			//用在库的序列号实体包含的库存ID获取多个库存对象
			List<Long> stockIds = serialList.stream().map(Serial::getStockId).collect(Collectors.toList());
			List<Stock> stockList = stockQueryBiz.findStockByIds(stockIds);
			AssertUtil.notNull(serialNumberList, "采集的序列号找不到对应库存");
			stockList.forEach(stock -> {
				stock.setBoxCode(request.getBoxCode());
				stockBiz.moveStock(stock, null, stock.getStockBalance(), location, StockLogTypeEnum.STOCK_DEVANNING_BY_PDA, null, null, null);
			});
		} else {
			AssertUtil.notNull(request.getStockList(), "根据物品拆箱时,未选择物品");
			//如果序列号不为空执行 按库存拆箱
			request.getStockList().forEach(stockDeva -> {
				Stock stock = stockQueryBiz.findStockById(stockDeva.getStockId());
				stock.setBoxCode(request.getBoxCode());
				if (BigDecimalUtil.gt(stockDeva.getSplitQty(), BigDecimal.ZERO)) {
					stockBiz.moveStock(stock, null, stockDeva.getSplitQty(), location, StockLogTypeEnum.STOCK_DEVANNING_BY_PDA, null, null, null);
				}
			});
		}


	}
}
