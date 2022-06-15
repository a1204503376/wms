package org.nodes.wms.core.stock.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.dto.StockDetailAddDTO;
import org.nodes.wms.core.stock.core.dto.StockDetailSubtractDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.enums.StockProcTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.mapper.StockDetailMapper;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockLogService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.StockDetailVo;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 库存明细表 服务实现类
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockDetailServiceImpl<M extends StockDetailMapper, T extends StockDetail>
	extends BaseServiceImpl<StockDetailMapper, StockDetail>
	implements IStockDetailService {

	@Autowired
	ISerialService serialService;
	@Autowired
	IStockLogService stockLogService;
	@Autowired
	ISystemProcService systemProcService;

	@Override
	public boolean add(StockDetailAddDTO stockDetailProc) {
		if (Func.isEmpty(stockDetailProc.getStock())) {
			throw new ServiceException("对应库存信息不能为空！");
		}
		StockDetail stockDetail = super.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.func(sql -> {
				sql.eq(StockDetail::getLpnCode, stockDetailProc.getLpnCode());
				sql.eq(StockDetail::getBoxCode, stockDetailProc.getBoxCode());
			})
		).stream().findFirst().orElse(null);
		boolean result = false;
		StockProcTypeEnum stockProcTypeEnum = StockProcTypeEnum.New;
		if (Func.isNotEmpty(stockDetail)) {
			// 库存存在的情况下，看看是否存在未接收数量
			if (BigDecimalUtil.gt(stockDetail.getUnreceivedQty(), BigDecimal.ZERO)) {
				if (BigDecimalUtil.gt(stockDetailProc.getStockQty(), stockDetail.getUnreceivedQty())) {
					throw new ServiceException("库存增加数量超出未接收数量！");
				}
				if (Func.isEmpty(stockDetailProc.getStock())) {
					IStockService stockService = SpringUtil.getBean(IStockService.class);
					stockDetailProc.setStock(stockService.getById(stockDetailProc.getStockId()));
				}
				stockDetail.setStockId(stockDetailProc.getStockId());
				stockDetail.setZoneId(stockDetailProc.getStock().getZoneId());
				stockDetail.setLocId(stockDetailProc.getStock().getLocId());
				stockDetail.setUnreceivedQty(stockDetail.getUnreceivedQty().subtract(stockDetailProc.getStockQty()));
				stockDetail.setStockQty(stockDetail.getStockQty().add(stockDetailProc.getStockQty()));
				result = super.updateById(stockDetail);
				stockProcTypeEnum = StockProcTypeEnum.Update;
			}else{
				if (Func.isEmpty(stockDetailProc.getStock())) {
					IStockService stockService = SpringUtil.getBean(IStockService.class);
					stockDetailProc.setStock(stockService.getById(stockDetailProc.getStockId()));
				}
				stockDetail = BeanUtil.copy(stockDetailProc.getStock(), StockDetail.class);
				stockDetail.setStockQty(stockDetailProc.getStockQty());
				stockDetail.setPickQty(BigDecimal.ZERO);
				stockDetail.setLpnCode(stockDetailProc.getLpnCode());
				stockDetail.setBoxCode(stockDetailProc.getBoxCode());
				stockDetail.setSoBillId(stockDetailProc.getSoBillId());
				stockDetail.setBillDetailId(stockDetailProc.getBillDetailId());
				stockDetail.setWellenId(stockDetailProc.getWellenId());
				result = super.save(stockDetail);
			}
		} else {
			if (Func.isEmpty(stockDetailProc.getStock())) {
				IStockService stockService = SpringUtil.getBean(IStockService.class);
				stockDetailProc.setStock(stockService.getById(stockDetailProc.getStockId()));
			}
			stockDetail = BeanUtil.copy(stockDetailProc.getStock(), StockDetail.class);
			stockDetail.setStockQty(stockDetailProc.getStockQty());
			stockDetail.setPickQty(BigDecimal.ZERO);
			stockDetail.setLpnCode(stockDetailProc.getLpnCode());
			stockDetail.setBoxCode(stockDetailProc.getBoxCode());
			stockDetail.setSoBillId(stockDetailProc.getSoBillId());
			stockDetail.setBillDetailId(stockDetailProc.getBillDetailId());
			stockDetail.setWellenId(stockDetailProc.getWellenId());
			result = super.save(stockDetail);
		}
		// 处理序列号
		if (ObjectUtil.isNotEmpty(stockDetailProc.getSerialList())) {
			serialService.saveBatch(stockDetailProc.getSerialList(), stockDetailProc.getStock(), stockDetail,
				stockDetailProc.getSystemProcId());
		}
		// 记录库存日志
		stockLogService.create(
			stockDetailProc.getStock(), stockDetail, stockProcTypeEnum, stockDetailProc.getSystemProcId());
		return result;
	}

	@Override
	public boolean substract(StockDetailSubtractDTO stockDetailSubtract) {
		List<StockDetail> stockDetailList = super.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.eq(StockDetail::getStockId, stockDetailSubtract.getStockId())
			.func(sql -> {
				sql.eq(StockDetail::getLpnCode, stockDetailSubtract.getLpnCode());
				sql.eq(StockDetail::getBoxCode, stockDetailSubtract.getBoxCode());
				if (Func.isNotEmpty(stockDetailSubtract.getBillDetailId())) {
					sql.eq(StockDetail::getBillDetailId, stockDetailSubtract.getBillDetailId());
				}
				if (Func.isNotEmpty(stockDetailSubtract.getSoBillId())) {
					sql.eq(StockDetail::getSoBillId, stockDetailSubtract.getSoBillId());
				}
				if (Func.isNotEmpty(stockDetailSubtract.getWellenId())) {
					sql.eq(StockDetail::getWellenId, stockDetailSubtract.getWellenId());
				}
			})
		);
		BigDecimal surplus_qty = stockDetailSubtract.getPickQty();
		List<Long> remove_list = new ArrayList<>();
		List<StockDetail> update_list = new ArrayList<>();
		for (StockDetail stockDetail : stockDetailList) {
			StockProcTypeEnum stockProcTypeEnum = null;
			if (BigDecimalUtil.le(stockDetail.getStockQty().subtract(stockDetail.getPickQty()), surplus_qty)) {
				remove_list.add(stockDetail.getId());
				stockProcTypeEnum = StockProcTypeEnum.Delete;
			} else {
				stockDetail.setPickQty(stockDetail.getPickQty().add(surplus_qty));
				update_list.add(stockDetail);
				stockProcTypeEnum = StockProcTypeEnum.Update;
			}
			// 记录库存日志
			stockLogService.create(
				stockDetailSubtract.getStock(), stockDetail, stockProcTypeEnum, stockDetailSubtract.getSystemProcId());
			if (Func.isNotEmpty(stockDetailSubtract.getSerialList())) {
				serialService.removeBatch(
					stockDetailSubtract.getSerialList(),
					stockDetailSubtract.getStock(),
					stockDetail,
					stockDetailSubtract.getSystemProcId());
			}
		}
		if (Func.isNotEmpty(remove_list)) {
			super.removeByIds(remove_list);
		}
		if (Func.isNotEmpty(update_list)) {
			super.updateBatchById(update_list, update_list.size());
		}
		return true;
	}

	@Override
	public boolean lock(List<Long> stockDetailIdList) {
		IStockService stockService = SpringUtil.getBean(IStockService.class);
		List<StockDetail> stockDetailList = super.listByIds(stockDetailIdList);
		if (Func.isEmpty(stockDetailList)) {
			throw new ServiceException("库存明细不能为空");
		}
		List<Long> stockIds = NodesUtil.toList(stockDetailList, StockDetail::getStockId);
		List<Stock> stockList = stockService.listByIds(stockIds);
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("库存为空无法更新库存状态");
		}
		stockDetailList.stream().collect(Collectors.groupingBy(StockDetail::getStockId))
			.forEach((stockId, list) -> {
				Stock stock = stockList.stream().filter(Stock -> {
					return Func.equals(Stock.getStockId(), stockId);
				}).collect(Collectors.toList()).get(0);
				Location location = LocationCache.getById(stock.getLocId());
				Sku sku = SkuCache.getById(stock.getSkuId());
				for (StockDetail stockDetail : stockDetailList) {
					stockDetail.setStockStatus(StockStatusEnum.LOCK_BLOCK.getIndex());
					// 插入系统日志
					SystemProcDTO systemProcParam = new SystemProcDTO();
					systemProcParam.setProcType(SystemProcTypeEnum.LOCK_STOCK);
					systemProcParam.setDataType(DataTypeEnum.STOCK_DETAIL_ID);
					systemProcParam.setBillNo(Func.toStr(stockDetail.getId()));
					if (Func.isNotEmpty(location)) {
						systemProcParam.setLocCode(location.getLocCode());
					}
					if (Func.isNotEmpty(sku)) {
						systemProcParam.setSkuCode(sku.getSkuCode());
						systemProcParam.setSkuName(sku.getSkuName());
					}
					systemProcParam.setOperationQty(stockDetail.getStockQty().subtract(stockDetail.getPickQty()));
					if (Func.isNotEmpty(stock)) {
						SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
							stock.getWspId(), stock.getSkuLevel());
						if (Func.isNotEmpty(skuPackageDetail)) {
							systemProcParam.setOperationUnit(skuPackageDetail.getWsuName());
						}
						systemProcParam.setWhId(stock.getWhId());
					}
					SystemProc systemProc = systemProcService.create(systemProcParam);
					stockLogService.create(stock, stockDetail, StockProcTypeEnum.Update, systemProc.getSystemProcId());
				}
				super.updateBatchById(stockDetailList, stockDetailList.size());

				List<StockDetail> stockDetailListAll = super.list(Condition.getQueryWrapper(new StockDetail())
					.lambda()
					.eq(StockDetail::getStockId, stockId));
				List<StockDetail> normalStockDetailList = stockDetailListAll.stream().filter(stockDetail -> {
					return Func.equals(stockDetail.getStockStatus(), StockStatusEnum.NORMAL.getIndex());
				}).collect(Collectors.toList());
				if (normalStockDetailList.size() == stockDetailListAll.size()) {
					stock.setStockStatus(StockStatusEnum.NORMAL.getIndex());
				} else if (normalStockDetailList.size() == 0) {
					stock.setStockStatus(StockStatusEnum.LOCK_FILL.getIndex());
				} else {
					stock.setStockStatus(StockStatusEnum.LOCK_BLOCK.getIndex());
				}
				stockService.updateById(stock);
			});
		return true;
	}

	@Override
	public boolean unlock(List<Long> stockDetailIdList) {
		IStockService stockService = SpringUtil.getBean(IStockService.class);
		List<StockDetail> stockDetailList = super.listByIds(stockDetailIdList);
		if (Func.isEmpty(stockDetailList)) {
			throw new ServiceException("库存明细不能为空");
		}
		List<Long> stockIds = NodesUtil.toList(stockDetailList, StockDetail::getStockId);
		List<Stock> stockList = stockService.listByIds(stockIds);
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("库存为空无法更新库存状态");
		}
		stockDetailList.stream().collect(Collectors.groupingBy(StockDetail::getStockId))
			.forEach((stockId, list) -> {
				Stock stock = stockList.stream().filter(Stock -> {
					return Func.equals(Stock.getStockId(), stockId);
				}).collect(Collectors.toList()).get(0);
				Location location = LocationCache.getById(stock.getLocId());
				Sku sku = SkuCache.getById(stock.getSkuId());
				for (StockDetail stockDetail : stockDetailList) {
					stockDetail.setStockStatus(StockStatusEnum.NORMAL.getIndex());
					// 插入系统日志
					SystemProcDTO systemProcParam = new SystemProcDTO();
					systemProcParam.setProcType(SystemProcTypeEnum.UNLOCK_STOCK);
					systemProcParam.setDataType(DataTypeEnum.STOCK_DETAIL_ID);
					systemProcParam.setBillNo(Func.toStr(stockDetail.getId()));
					if (Func.isNotEmpty(location)) {
						systemProcParam.setLocCode(location.getLocCode());
					}
					if (Func.isNotEmpty(sku)) {
						systemProcParam.setSkuCode(sku.getSkuCode());
						systemProcParam.setSkuName(sku.getSkuName());
					}
					systemProcParam.setOperationQty(stockDetail.getStockQty().subtract(stockDetail.getPickQty()));
					if (Func.isNotEmpty(stock)) {
						SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
							stock.getWspId(), stock.getSkuLevel());
						if (Func.isNotEmpty(skuPackageDetail)) {
							systemProcParam.setOperationUnit(skuPackageDetail.getWsuName());
						}
						systemProcParam.setWhId(stock.getWhId());
					}
					SystemProc systemProc = systemProcService.create(systemProcParam);
					stockLogService.create(stock, stockDetail, StockProcTypeEnum.Update, systemProc.getSystemProcId());
				}
				super.updateBatchById(stockDetailList, stockDetailList.size());

				List<StockDetail> stockDetailListAll = super.list(Condition.getQueryWrapper(new StockDetail())
					.lambda()
					.eq(StockDetail::getStockId, stockId));
				List<StockDetail> normalStockDetailList = stockDetailListAll.stream().filter(stockDetail -> {
					return Func.equals(stockDetail.getStockStatus(), StockStatusEnum.NORMAL.getIndex());
				}).collect(Collectors.toList());
				if (normalStockDetailList.size() == stockDetailListAll.size()) {
					stock.setStockStatus(StockStatusEnum.NORMAL.getIndex());
				} else if (normalStockDetailList.size() == 0) {
					stock.setStockStatus(StockStatusEnum.LOCK_FILL.getIndex());
				} else {
					stock.setStockStatus(StockStatusEnum.LOCK_BLOCK.getIndex());
				}
				stockService.updateById(stock);
			});

		return true;
	}

	@Override
	public IPage<StockDetailVo> selectStockDetailsByStockId(IPage<StockDetailVo> page, Long stockId) {
		return page.setRecords(baseMapper.selectStockDetailsByStockId(page, stockId));
	}
}
