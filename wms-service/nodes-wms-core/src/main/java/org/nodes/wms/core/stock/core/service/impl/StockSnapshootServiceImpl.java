package org.nodes.wms.core.stock.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.common.entity.FunctionCount;
import org.nodes.wms.core.common.service.IFunctionCountService;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.mapper.StockSnapshootMapper;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.service.IStockSnapshootService;
import org.nodes.wms.core.stock.core.entity.StockSnapshoot;

import org.nodes.wms.core.stock.core.wrapper.StockSnapshootWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存快照 服务实现类
 *
 * @author NodeX
 * @since 2021-05-28
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockSnapshootServiceImpl<M extends SkuMapper, T extends Sku> extends BaseServiceImpl<StockSnapshootMapper, StockSnapshoot> implements IStockSnapshootService<T> {

	@Autowired
	private IStockService stockService;
	@Autowired
	private IFunctionCountService functionCountService;

	@Override
	public boolean stockSnapshoot() {
		DateTimeFormatter fmDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.now();

		//查询出当前的库存
		List<Stock> stockList = stockService.list();
		//查询出前一天的库存快照
		List<StockSnapshoot> stockSnapshootList = super.list(new LambdaQueryWrapper<StockSnapshoot>()
			.eq(StockSnapshoot::getSnapshootDate, today.plusDays(-1).format(fmDate)));
		//待保存的库存快照集合
		List<StockSnapshoot> list = new ArrayList<>();
		//计算并存储当天的库存
		for (Stock stock : stockList) {
			StockSnapshoot currentSnapshoot = StockSnapshootWrapper.build().entity(stock);
			// 找到当前库存前一天的快照
			StockSnapshoot stockSnapshoot = stockSnapshootList.stream().filter(u -> {
				return u.getStockId().equals(stock.getStockId());
			}).findFirst().orElse(null);
			//结余数量=库存数量-下架数量
			currentSnapshoot.setQty(currentSnapshoot.getStockQty().subtract(currentSnapshoot.getPickQty()));
			if (Func.isEmpty(stockSnapshoot)) {
				currentSnapshoot.setDiffQty(new BigDecimal(0));
			} else {
				currentSnapshoot.setDiffQty(currentSnapshoot.getQty().subtract(stockSnapshoot.getQty()));
			}
			list.add(currentSnapshoot);
		}
		return super.saveBatch(list, list.size());

	}

	@Override
	public boolean initializeStockSnapshoot() {
		FunctionCount initializeStockSnapshoot = functionCountService.getOne(new LambdaQueryWrapper<FunctionCount>().eq(FunctionCount::getMethod, "initializeStockSnapshoot"));
		if (Func.isNotEmpty(initializeStockSnapshoot)){
			throw new ServiceException("快照已经初始化");
		}else {
			FunctionCount functionCount = new FunctionCount();
			functionCount.setCount(1);
			functionCount.setMethod("initializeStockSnapshoot");
			functionCountService.saveOrUpdate(functionCount);
		}
		//带保存的库存快照
		List<StockSnapshoot> list = new ArrayList<>();
		//查询出当前的库存
		List<Stock> stockList = stockService.list();
		for (Stock stock : stockList) {
			StockSnapshoot currentSnapshoot = StockSnapshootWrapper.build().entity(stock);
			currentSnapshoot.setQty(currentSnapshoot.getStockQty().subtract(currentSnapshoot.getPickQty()));
			currentSnapshoot.setDiffQty(new BigDecimal(0));
			list.add(currentSnapshoot);
		}
		super.saveBatch(list, stockList.size());
		return true;
	}
}
