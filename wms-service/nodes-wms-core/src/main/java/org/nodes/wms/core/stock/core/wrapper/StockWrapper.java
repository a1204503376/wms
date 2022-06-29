
package org.nodes.wms.core.stock.core.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.basedata.vo.SkuPackageVO;
import org.nodes.wms.core.basedata.wrapper.SkuPackageWrapper;
import org.nodes.wms.core.stock.core.dto.StockAddDTO;
import org.nodes.wms.core.stock.core.dto.StockDTO;
import org.nodes.wms.core.stock.core.dto.StockMoveDTO;
import org.nodes.wms.core.stock.core.dto.StockSubtractDTO;
import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.ILotService;
import org.nodes.wms.core.stock.core.service.IStockMoveService;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * 库存包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-24
 */
public class StockWrapper extends BaseEntityWrapper<Stock, StockVO> {

	public static StockWrapper build() {
		return new StockWrapper();
	}

	@Override
	public StockVO entityVO(Stock stock) {
		ILotService lotService = SpringUtil.getBean(ILotService.class);
		StockVO stockVO = BeanUtil.copy(stock, StockVO.class);
		if (Func.isNotEmpty(stockVO)) {
			//库房信息
			Warehouse warehouse = WarehouseCache.getById(stockVO.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				stockVO.setWhName(warehouse.getWhName());
			}
			//库区信息
			if (Func.isNotEmpty(stockVO.getZoneId())) {
				IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
				Zone zone = zoneService.getById(stockVO.getZoneId());
				if (Func.isNotEmpty(zone)) {
					stockVO.setZoneName(zone.getZoneName());
				}
			}
			//库位信息
			if (Func.isNotEmpty(stockVO.getLocId())) {
				Location location = LocationCache.getById(stockVO.getLocId());
				if (Func.isNotEmpty(location)) {
					stockVO.setLocCode(location.getLocCode());
					if (StringPool.N.toUpperCase().equals(location.getLockFlag())) {
						stockVO.setLockFlag("1");
					} else {
						stockVO.setLockFlag("0");
					}
				}
			}
			//对应包装明细
			List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(stockVO.getWspId());
			if (Func.isNotEmpty(skuPackageDetailList)) {
				stockVO.setSkuPackageDetailList(skuPackageDetailList);
			}
			//物品信息
			if (Func.isNotEmpty(stockVO.getSkuId())) {
				Sku sku = SkuCache.getById(stockVO.getSkuId());
				if (Func.isNotEmpty(sku)) {
					stockVO.setSkuCode(sku.getSkuCode());
					stockVO.setSkuName(sku.getSkuName());
					stockVO.setIsSn(sku.getIsSn());
					// 物品分类信息
					ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
					SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
					if (Func.isNotEmpty(skuType)) {
						stockVO.setSkuTypeCd(skuType.getTypeCode());
						stockVO.setSkuTypeName(skuType.getTypeName());
					}
				}
			}
			if (Func.isNotEmpty(stockVO.getWoId())) {
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(stockVO.getWoId());
				if (Func.isNotEmpty(owner)) {
					stockVO.setOwnerName(owner.getOwnerName());
				}
			}
			//包装信息
			if (Func.isNotEmpty(stockVO.getWspId())) {
				SkuPackageVO skuPackageVO = SkuPackageWrapper.build().entityVO(SkuPackageCache.getById(stockVO.getWspId()));
				if (Func.isNotEmpty(skuPackageVO)) {
					stockVO.setWspName(skuPackageVO.getWspName());
					stockVO.setSkuSpec(skuPackageVO.getSpec());
				}
				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(stockVO.getWspId(), stockVO.getSkuLevel());
				if (Func.isNotEmpty(skuPackageDetail)) {
					stockVO.setWsuName(skuPackageDetail.getWsuName());
				}
			}
			//可用量
			if (Func.isNotEmpty(stockVO.getStockQty())) {
				stockVO.setAvailableQty(stockVO.getStockQty().subtract(stockVO.getOccupyQty())
					.subtract(stockVO.getPickQty()));
			}
			stockVO.setActualQty(stockVO.getStockQty().subtract(stockVO.getPickQty()));
			//stockVO.setStockStatusDesc(DictCache.getValue(Stock.STOCK_STATUS, stock.getStockStatus()));
			//Lot lot = LotCache.getByLotNumber(stock.getLotNumber());
			Lot lot = lotService.list(Condition.getQueryWrapper(new Lot())
				.lambda()
				.eq(Lot::getLotNumber, stock.getLotNumber())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(lot)) {
				stockVO.setLotStatus(lot.getLotStatus());
				//stockVO.setLotStatusDesc(DictCache.getValue(Stock.STOCK_STATUS, lot.getLotStatus()));
			}
		}
		return stockVO;
	}

	public StockVO entity(StockAddDTO stockAdd) {
		Stock stock = BeanUtil.copy(stockAdd, StockVO.class);
		stock.setStockId(null);
		stock.setSkuLevel(1);
		stock.setPickQty(BigDecimal.ZERO);
		stock.setOccupyQty(BigDecimal.ZERO);
		Location location = LocationCache.getById(stockAdd.getLocId());
		if (Func.isNotEmpty(location)) {
			stock.setWhId(location.getWhId());
			stock.setZoneId(location.getZoneId());
		}
		Sku sku = SkuCache.getById(stockAdd.getSkuId());
		if (Func.isNotEmpty(sku)) {
			stock.setWoId(sku.getWoId());
		}

		return this.entityVO(stock);
	}

	public StockDTO entityDTO(Stock stock) {
		return BeanUtil.copy(stock, StockDTO.class);
	}

	public StockSubtractDTO moveToReduce(StockMoveDTO stockMove) {
		StockSubtractDTO stockReduceDTO = BeanUtil.copy(stockMove, StockSubtractDTO.class);
		if (ObjectUtil.isNotEmpty(stockReduceDTO)) {
			stockReduceDTO.setLocId(stockMove.getSourceLocId());
			stockReduceDTO.setLpnCode(stockMove.getSourceLpnCode());
			stockReduceDTO.setBoxCode(stockMove.getSourceBoxCode());
			stockReduceDTO.setPickQty(stockMove.getMoveQty());
			stockReduceDTO.setStockId(stockMove.getSourceStockId());
		}
		return stockReduceDTO;
	}

	public StockSubtractDTO moveToReduce(IStockMoveService.MoveParam stockMove) {
		StockSubtractDTO stockReduceDTO = BeanUtil.copy(stockMove, StockSubtractDTO.class);
		if (ObjectUtil.isNotEmpty(stockReduceDTO)) {
			stockReduceDTO.setLocId(stockMove.getSourceLocId());
			stockReduceDTO.setLpnCode(stockMove.getSourceLpnCode());
			stockReduceDTO.setLotNumber(stockMove.getLottNumber());
			stockReduceDTO.setBoxCode(stockMove.getSourceBoxCode());
			stockReduceDTO.setPickQty(stockMove.getMoveQty());
			stockReduceDTO.setStockId(stockMove.getSourceStockId());
		}
		return stockReduceDTO;
	}

	public StockAddDTO entityProc(Stock stock, StockMoveDTO stockMove) {
		StockAddDTO stockProcDTO = BeanUtil.copy(stock, StockAddDTO.class);
		if (ObjectUtil.isNotEmpty(stockProcDTO)) {
			stockProcDTO.setLocId(stockMove.getTargetLocId());
			stockProcDTO.setLpnCode(stockMove.getTargetLpnCode());
			stockProcDTO.setStockQty(stockMove.getMoveQty());
			stockProcDTO.setEventType(stockMove.getEventType());
			stockProcDTO.setSystemProcId(stockMove.getSystemProcId());
			stockProcDTO.setSerialList(stockMove.getSerialList());
			stockProcDTO.setSdId(stockMove.getSdId());
			stockProcDTO.setDataId(stockMove.getDataId());
			stockProcDTO.setStockId(stockMove.getTargetStockId());
		}
		return stockProcDTO;
	}

	public StockAddDTO entityProc(Stock stock, IStockMoveService.MoveParam stockMove) {
		StockAddDTO stockProcDTO = BeanUtil.copy(stock, StockAddDTO.class);
		if (ObjectUtil.isNotEmpty(stockProcDTO)) {
			stockProcDTO.setLocId(stockMove.getTargetLocId());
			stockProcDTO.setLpnCode(stockMove.getTargetLpnCode());
			stockProcDTO.setStockQty(stockMove.getMoveQty());
			stockProcDTO.setEventType(stockMove.getEventType());
			stockProcDTO.setSystemProcId(stockMove.getSystemProcId());
			stockProcDTO.setSerialList(stockMove.getSerialList());
			stockProcDTO.setDataId(stockMove.getDataId());
			stockProcDTO.setStockId(stockMove.getTargetStockId());
		}
		return stockProcDTO;
	}
}
