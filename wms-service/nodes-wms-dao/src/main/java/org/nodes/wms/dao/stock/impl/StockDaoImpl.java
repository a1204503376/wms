package org.nodes.wms.dao.stock.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.nodes.wms.dao.stock.mapper.StockMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存Dao接口实现类
 **/
@Repository
public class StockDaoImpl
	extends BaseServiceImpl<StockMapper, Stock> implements StockDao {

	@Override
	public Map<String, Object> getStockQtyByLocIdList(List<Long> locIdList) {
		return super.baseMapper.selectStockQtyByLocIdList(locIdList);
	}

	@Override
	public Integer getStockSkuCountByLocIdList(List<Long> locIdList) {
		return super.baseMapper.selectStockSkuCountByLocIdList(locIdList);
	}

	@Override
	public List<Stock> getStockByBoxCodeExcludeLoc(String boxCode, List<Long> excludeLocIdList) {
		if (Func.isEmpty(boxCode)) {
			throw new NullArgumentException("库存查询失败，按箱码查询库存时箱码为空");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(excludeLocIdList)) {
			queryWrapper.notIn(Stock::getLocId, excludeLocIdList);
		}
		queryWrapper.eq(Stock::getBoxCode, boxCode);
		return super.list(queryWrapper);
	}

	@Override
	public List<Stock> getStockByBoxCode(String boxCode, List<Long> locIdList) {
		if (Func.isEmpty(boxCode)) {
			throw new NullArgumentException("库存查询失败，按箱码查询库存时箱码为空");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(locIdList)) {
			queryWrapper.in(Stock::getLocId, locIdList);
		}
		queryWrapper.eq(Stock::getBoxCode, boxCode);
		return super.list(queryWrapper);
	}

	@Override
	public List<Stock> getStockLeftLikeByBoxCode(String boxCode, List<Long> locIdList) {
		if (Func.isEmpty(boxCode)) {
			throw new NullArgumentException("库存查询失败，getStockLeftLikeByBoxCode");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(locIdList)) {
			queryWrapper.in(Stock::getLocId, locIdList);
		}
		queryWrapper.likeLeft(Stock::getBoxCode, boxCode);
		return super.list(queryWrapper);
	}

	@Override
	public List<Stock> getStockByLpnCode(String lpnCode, List<Long> locIdList) {
		if (Func.isEmpty(lpnCode)) {
			throw new NullArgumentException("库存查询失败，按LPN查询库存时LPN为空");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(locIdList)) {
			queryWrapper.in(Stock::getLocId, locIdList);
		}
		queryWrapper.eq(Stock::getLpnCode, lpnCode);
		return super.list(queryWrapper);
	}

	@Override
	public List<Stock> getStockOnLpnByBoxCode(String boxCode) {
		if (Func.isEmpty(boxCode)) {
			throw new NullArgumentException("库存查询失败，按箱码查询库存时箱码为空");
		}
		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		queryWrapper.eq(Stock::getBoxCode, boxCode);
		Stock stock = super.getOne(queryWrapper);
		if (Func.isEmpty(stock.getLpnCode())) {
			throw new NullArgumentException("库存查询失败，按箱码查询库存时LPN为空");
		}
		LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = getStockQuery();
		stockLambdaQueryWrapper.eq(Stock::getLpnCode, stock.getLpnCode());
		return super.list(stockLambdaQueryWrapper);
	}

	@Override
	public List<Stock> getStockByLpnCodeExcludeLoc(String lpnCode, List<Long> excludeLocIdList) {
		if (Func.isEmpty(lpnCode)) {
			throw new NullArgumentException("库存查询失败，按LPN查询库存时LPN为空");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(excludeLocIdList)) {
			queryWrapper.notIn(Stock::getLocId, excludeLocIdList);
		}
		queryWrapper.eq(Stock::getLpnCode, lpnCode);
		return super.list(queryWrapper);
	}

	@Override
	public List<Stock> getStock(StockStatusEnum status, Long woId,
								Long locId, Long skuId, String boxCode, String lpnCode) {
		if (Func.isNull(woId) || Func.isNull(skuId) || Func.isNull(locId)) {
			throw new ServiceException("库存查询失败,缺失必要参数");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		queryWrapper
			.eq(Stock::getStockStatus, status.getCode())
			.eq(Stock::getWoId, woId)
			.eq(Stock::getLocId, locId)
			.eq(Stock::getSkuId, skuId);

		if (Func.isEmpty(boxCode)) {
			queryWrapper.apply("(box_code is null)");
		} else {
			queryWrapper.eq(Stock::getBoxCode, boxCode);
		}

		if (Func.isEmpty(lpnCode)) {
			queryWrapper.apply("(lpn_code is null)");
		} else {
			queryWrapper.eq(Stock::getLpnCode, lpnCode);
		}

		return super.list(queryWrapper);
	}

	@Override
	public List<Stock> getStockByLocId(Long locId) {
		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		queryWrapper.eq(Stock::getLocId, locId);
		return super.list(queryWrapper);
	}

	@Override
	public Stock saveNewStock(Stock stock) {
		if (!super.save(stock)) {
			throw new ServiceException("库存保存失败,请再次重试");
		}
		return stock;
	}

	@Override
	public Stock updateStock(Stock stock) {
		if (Func.isEmpty(stock.getStockId())) {
			throw new ServiceException("库存保存失败,库位的唯一标识为空");
		}

		if (!super.updateById(stock)) {
			throw new ServiceException("库存保存失败,请再次重试");
		}

		return stock;
	}

	@Override
	public void updateStock(Long stockId, BigDecimal stockQty, BigDecimal stayStockQty,
							BigDecimal pickQty, LocalDateTime lastInTime, LocalDateTime lastOutTime) {
		UpdateWrapper<Stock> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(Stock::getStockId, stockId);
		Stock stock = new Stock();
		stock.setStockQty(stockQty);
		stock.setStayStockQty(stayStockQty);
		stock.setPickQty(pickQty);
		if (Func.isNotEmpty(lastInTime)) {
			stock.setLastInTime(lastInTime);
		}
		if (Func.isNotEmpty(lastOutTime)) {
			stock.setLastOutTime(lastOutTime);
		}

		if (!super.update(stock, updateWrapper)) {
			throw new ServiceException("库存保存失败,请再次重试");
		}
	}

	private LambdaQueryWrapper<Stock> getStockQuery() {
		LambdaQueryWrapper<Stock> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.apply("stay_stock_qty + stock_qty > pick_qty");
		return queryWrapper;
	}
}
