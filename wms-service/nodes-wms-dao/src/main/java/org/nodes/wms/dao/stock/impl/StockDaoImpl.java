package org.nodes.wms.dao.stock.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.count.dto.output.PdaBoxQtyResponse;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.input.StockBySerialPageQuery;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.FindAllStockByNoResponse;
import org.nodes.wms.dao.stock.dto.output.StockBySerialPageResponse;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
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
import java.util.stream.Collectors;

/**
 * 库存Dao接口实现类
 *
 * @author nodesc
 **/
@Repository
public class StockDaoImpl
		extends BaseServiceImpl<StockMapper, Stock> implements StockDao {

	@Override
	public List<Stock> getStockById(List<Long> stockIds) {
		AssertUtil.notEmpty(stockIds, "库存查询失败，stockIds is null");
		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		queryWrapper.in(Stock::getStockId, stockIds);
		return super.list(queryWrapper);
	}

	@Override
	public Map<String, Object> getStockQtyByLocIdList(List<Long> locIdList) {
		return super.baseMapper.selectStockQtyByLocIdList(locIdList);
	}

	@Override
	public Integer getStockSkuCountByLocIdList(List<Long> locIdList) {
		return super.baseMapper.selectStockSkuCountByLocIdList(locIdList);
	}

	@Override
	public List<Stock> getStockByBoxCodeExcludeLoc(List<String> boxCodes, List<Long> excludeLocIdList) {
		if (Func.isEmpty(boxCodes)) {
			throw new NullArgumentException("库存查询失败，按箱码查询库存时箱码为空");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(excludeLocIdList)) {
			queryWrapper.notIn(Stock::getLocId, excludeLocIdList);
		}
		queryWrapper.in(Stock::getBoxCode, boxCodes);
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
	public List<Stock> getStockByBoxCode(List<String> boxCodes, List<Long> locIdList) {
		if (Func.isEmpty(boxCodes)) {
			throw new NullArgumentException("库存查询失败，按箱码查询库存时箱码为空");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(locIdList)) {
			queryWrapper.in(Stock::getLocId, locIdList);
		}
		queryWrapper.in(Stock::getBoxCode, boxCodes);
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
	public List<Stock> getStockByLpnCode(List<String> lpnCodes, List<Long> locIdList) {
		if (Func.isEmpty(lpnCodes)) {
			throw new NullArgumentException("库存查询失败，按LPN查询库存时LPN为空");
		}

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		if (Func.isNotEmpty(locIdList)) {
			queryWrapper.in(Stock::getLocId, locIdList);
		}
		queryWrapper.in(Stock::getLpnCode, lpnCodes);
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
		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		return getStock(status, woId, locId, skuId, boxCode, lpnCode, null, queryWrapper);
	}

	@Override
	public List<Stock> matchStock(StockStatusEnum status, Long woId, Long locId,
			Long skuId, String boxCode, String lpnCode, String dropId) {
		LambdaQueryWrapper<Stock> queryWrapper = Wrappers.lambdaQuery(Stock.class);
		return getStock(status, woId, locId, skuId, boxCode, lpnCode, dropId, queryWrapper);
	}

	private List<Stock> getStock(StockStatusEnum status, Long woId,
			Long locId, Long skuId, String boxCode, String lpnCode,
			String dropId, LambdaQueryWrapper<Stock> queryWrapper) {
		if (Func.isNull(woId) || Func.isNull(skuId) || Func.isNull(locId)) {
			throw new ServiceException("库存查询失败,缺失必要参数");
		}
		if (Func.isNull(dropId)) {
			dropId = "";
		}

		queryWrapper
				.eq(Stock::getStockStatus, status.getCode())
				.eq(Stock::getWoId, woId)
				.eq(Stock::getLocId, locId)
				.eq(Stock::getSkuId, skuId)
				.eq(Stock::getDropId, dropId);

		if (Func.isEmpty(boxCode)) {
			queryWrapper.apply("(box_code is null or box_code = '')");
		} else {
			queryWrapper.eq(Stock::getBoxCode, boxCode);
		}

		if (Func.isEmpty(lpnCode)) {
			queryWrapper.apply("(lpn_code is null or lpn_code = '')");
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
			BigDecimal pickQty, BigDecimal occupyQty, LocalDateTime lastInTime, LocalDateTime lastOutTime) {
		UpdateWrapper<Stock> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
				.eq(Stock::getStockId, stockId);
		// 更新对象
		Stock stock = new Stock();
		stock.setStockQty(stockQty);
		stock.setStayStockQty(stayStockQty);
		stock.setPickQty(pickQty);
		stock.setOccupyQty(occupyQty);
		if (Func.notNull(lastInTime)) {
			stock.setLastInTime(lastInTime);
		}
		if (Func.notNull(lastOutTime)) {
			stock.setLastOutTime(lastOutTime);
		}

		if (!super.update(stock, updateWrapper)) {
			throw new ServiceException("库存保存失败,请再次重试");
		}
	}

	@Override
	public void updateStock(List<Long> stockIds, StockStatusEnum status) {
		AssertUtil.notEmpty(stockIds, "update stock status error");

		Stock stock = new Stock();
		stock.setStockStatus(status);

		UpdateWrapper<Stock> updateWrapper = Wrappers.update();
		updateWrapper.lambda().in(Stock::getStockId, stockIds);
		if (!super.update(stock, updateWrapper)) {
			throw new ServiceException("库存保存失败,请再次重试");
		}
	}

	@Override
	public List<Stock> getStockByLocIdList(List<Long> locIdList) {
		if (Func.isEmpty(locIdList)) {
			throw new NullArgumentException("StockDaoImpl.getStockByLocIdList方法的参数为空");
		}
		LambdaQueryWrapper<Stock> lambdaQueryWrapper = getStockQuery();
		lambdaQueryWrapper.in(Stock::getLocId, locIdList);
		return super.list(lambdaQueryWrapper);
	}

	@Override
	public Stock getStockById(Long stockId) {
		AssertUtil.notNull(stockId, "库存查询失败，stockId不能为空");
		return super.getById(stockId);
	}

	@Override
	public IPage<FindAllStockByNoResponse> getStockList(FindAllStockByNoRequest request, IPage<Stock> page) {
		if (Func.isEmpty(request.getNo())) {
			throw new NullArgumentException("编码不能为空");
		}
		return baseMapper.getList(request, page);
	}

	private LambdaQueryWrapper<Stock> getStockQuery() {
		LambdaQueryWrapper<Stock> queryWrapper = Wrappers.lambdaQuery(Stock.class);
		queryWrapper.apply("stay_stock_qty + stock_qty > pick_qty");
		return queryWrapper;
	}

	@Override
	public Page<StockPageResponse> page(IPage<StockPageResponse> page, StockPageQuery stockPageQuery) {
		return super.baseMapper.getPage(page, stockPageQuery);
	}

	@Override
	public List<StockPageResponse> getStockResponseByQuery(StockPageQuery stockPageQuery) {
		return super.baseMapper.getStockResponseByQuery(stockPageQuery);
	}

	private void checkByFindEnableStock(Long whId, Long skuId, List<Long> excludeZoneIdList) {
		AssertUtil.notNull(whId, "查询可用库存时库房id不能位空");
		AssertUtil.notNull(skuId, "查询可用库存时物品id不能位空");
		AssertUtil.notNull(excludeZoneIdList, "查询可用库存时出库暂存区id不能位空");
	}

	@Override
	public List<Stock> findEnableStockByZone(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
			List<Long> zoneIdList, SkuLotBaseEntity skuLot,
			List<Long> excludeZoneIdList) {
		checkByFindEnableStock(whId, skuId, excludeZoneIdList);

		LambdaQueryWrapper<Stock> stockQuery = getStockQuery();
		stockQuery.eq(Stock::getWhId, whId)
				.eq(Stock::getSkuId, skuId)
				.in(Func.isNotEmpty(zoneIdList), Stock::getZoneId, zoneIdList)
				.notIn(Stock::getZoneId, excludeZoneIdList);
		if (!Func.isNull(stockStatusEnum)) {
			stockQuery.eq(Stock::getStockStatus, stockStatusEnum.getCode());
		}
		SkuLotUtil.applySql(stockQuery, skuLot);

		return super.list(stockQuery);
	}

	@Override
	public List<Stock> findEnableStockByLocation(Long whId, Long skuId, StockStatusEnum stockStatusEnum,
			List<Long> locationIdList, SkuLotBaseEntity skuLot, List<Long> excludeZoneIdList) {
		checkByFindEnableStock(whId, skuId, excludeZoneIdList);

		LambdaQueryWrapper<Stock> stockQuery = getStockQuery();
		stockQuery.eq(Stock::getWhId, whId)
				.eq(Stock::getSkuId, skuId)
				.in(Func.isNotEmpty(locationIdList), Stock::getLocId, locationIdList)
				.notIn(Stock::getZoneId, excludeZoneIdList);
		if (!Func.isNull(stockStatusEnum)) {
			stockQuery.eq(Stock::getStockStatus, stockStatusEnum.getCode());
		}
		SkuLotUtil.applySql(stockQuery, skuLot);

		return super.list(stockQuery);
	}

	@Override
	public List<StockPageResponse> getStockResponseByBoxOrByLpn(StockPageQuery stockPageQuery) {
		return super.baseMapper.getStockResponseByBoxOrByLpn(stockPageQuery);
	}

	@Override
	public List<Stock> getEnableStockBySkuLotAndExcludeLoc(List<Long> excludeLocId, SkuLotBaseEntity skuLot) {
		LambdaQueryWrapper<Stock> stockQuery = getStockQuery();
		if (Func.isNotEmpty(excludeLocId)) {
			stockQuery.notIn(Stock::getLocId, excludeLocId);
		}

		SkuLotUtil.applySql(stockQuery, skuLot);
		return super.list(stockQuery);
	}

	@Override
	public List<Stock> getEnableStockBySkuLotAndExcludeLoc(Long skuId,
			List<Long> excludeLocId, SkuLotBaseEntity skuLot) {
		AssertUtil.notNull(skuId, "查询库存失败，skuid参数为空");

		LambdaQueryWrapper<Stock> stockQuery = getStockQuery();
		stockQuery.eq(Stock::getSkuId, skuId);
		if (Func.isNotEmpty(excludeLocId)) {
			stockQuery.notIn(Stock::getLocId, excludeLocId);
		}
		SkuLotUtil.applySql(stockQuery, skuLot);
		return super.list(stockQuery);
	}

	@Override
	public Page<StockBySerialPageResponse> page(IPage<?> page, StockBySerialPageQuery stockBySerialPageQuery) {
		return super.baseMapper.getSerialPage(page, stockBySerialPageQuery);
	}

	@Override
	public void updateStockByDropId(List<Stock> stocks, StockStatusEnum status, String dropId) {
		AssertUtil.notEmpty(stocks, "update stock status error, stock list is empty");

		Stock stock = new Stock();
		stock.setStockStatus(status);
		if (StockStatusEnum.SYSTEM_FREEZE.equals(status)) {
			stock.setDropId(dropId);
		}

		List<Long> stockIds = stocks.stream()
				.map(Stock::getStockId)
				.collect(Collectors.toList());
		UpdateWrapper<Stock> updateWrapper = Wrappers.update();
		updateWrapper.lambda().in(Stock::getStockId, stockIds);
		if (!super.update(stock, updateWrapper)) {
			throw new ServiceException("库存状态更新失败,请再次重试");
		}
	}

	@Override
	public void upateOccupyQty(Stock stock) {
		AssertUtil.notNull(stock, "更新占用失败,stock参数为空");
		Stock newStock = new Stock();
		newStock.setStockId(stock.getStockId());
		newStock.setOccupyQty(stock.getOccupyQty());

		UpdateWrapper<Stock> updateWrapper = Wrappers.update();
		updateWrapper.lambda().eq(Stock::getStockId, stock.getStockId());
		if (!super.update(stock, updateWrapper)) {
			throw new ServiceException("更新占用失败,请再次重试");
		}
	}

	@Override
	public List<Stock> getStockByDropId(Long dropId) {
		AssertUtil.notNull(dropId, "根据任务id查询库存失败,taskId不能为空");

		LambdaQueryWrapper<Stock> queryWrapper = getStockQuery();
		queryWrapper.eq(Stock::getDropId, dropId);
		return super.list(queryWrapper);
	}

	@Override
	public List<PdaBoxQtyResponse> getStockCountByLocCode(String locCode, String boxCode, String skuCode) {
		AssertUtil.notEmpty(locCode, "根据库位查询库存数据失败，locCode不能为空");
		return super.baseMapper.getStockCountByLocCode(locCode, boxCode, skuCode);
	}

}
