package org.nodes.wms.biz.stockManage.module;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.stock.dto.input.DevanningSubmitRequest;
import org.nodes.wms.dao.stock.dto.output.DevanningStockResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DevanningActionFactory {
	private final StockQueryBiz stockQueryBiz;

	public List<DevanningAction> create(DevanningSubmitRequest request) {
		if (request.getIsSn()) {
			return createOnSerial(request);
		}

		AssertUtil.notEmpty(request.getStockList(), "拆箱失败(按物品)，物品参数为空");
		List<DevanningAction> result = new ArrayList<>();
		for (DevanningStockResponse item : request.getStockList()) {
			Stock stock = stockQueryBiz.findStockById(item.getStockId());
			AssertUtil.notNull(stock, "拆箱失败(按物品)，{}查询不到库存", item.getSkuCode());
			DevanningAction devanningAction = new DevanningAction();
			devanningAction.setStock(stock);
			devanningAction.setQty(item.getSplitQty());

			result.add(devanningAction);
		}

		return result;
	}

	private List<DevanningAction> createOnSerial(DevanningSubmitRequest request) {
		if (Func.isEmpty(request.getSerialNumberList())) {
			throw ExceptionUtil.mpe("拆箱失败（按序列号）,序列参数为空");
		}

		List<Serial> serialList = stockQueryBiz.findSerialBySerialNo(request.getSerialNumberList());
		AssertUtil.notEmpty(serialList, "拆箱失败（按序列号）,库存中查询不到序列号信息");

		List<Long> stockIdList = serialList.stream()
			.map(Serial::getStockId)
			.distinct()
			.collect(Collectors.toList());

		List<Stock> stockList = stockQueryBiz.findStockById(stockIdList);
		List<DevanningAction> result = new ArrayList<>();
		for (Stock stockItem : stockList) {
			DevanningAction devanningAction = new DevanningAction();
			devanningAction.setStock(stockItem);
			List<String> serialNoList = serialList.stream()
				.filter(i -> i.getStockId().equals(stockItem.getStockId()))
				.map(Serial::getSerialNumber)
				.collect(Collectors.toList());
			devanningAction.setSerialNoList(serialNoList);
			devanningAction.setQty(BigDecimal.valueOf(serialNoList.size()));

			result.add(devanningAction);
		}

		return result;
	}
}
