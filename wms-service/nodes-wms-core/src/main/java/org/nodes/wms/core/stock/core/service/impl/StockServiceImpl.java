package org.nodes.wms.core.stock.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.dto.*;
import org.nodes.wms.core.stock.core.entity.*;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockProcTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.mapper.ErpStockMapper;
import org.nodes.wms.core.stock.core.mapper.StockMapper;
import org.nodes.wms.core.stock.core.service.*;
import org.nodes.wms.core.stock.core.vo.AccountsVo;
import org.nodes.wms.core.stock.core.vo.ErpStockVO;
import org.nodes.wms.core.stock.core.vo.StockSubtractVO;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.stock.core.wrapper.ErpStockWrapper;
import org.nodes.wms.core.stock.core.wrapper.LotWrapper;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.nodes.wms.core.strategy.service.IRelenishmentDetailService;
import org.nodes.wms.core.strategy.service.IRelenishmentService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.enums.ZoneTypeEnum;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * ?????? ???????????????
 *
 * @author pengwei
 * @since 2019-12-24
 */
@Slf4j
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockServiceImpl<M extends StockMapper, T extends Stock>
	extends BaseServiceImpl<StockMapper, Stock>
	implements IStockService {

	@Autowired
	ILotService lotService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ErpStockMapper erpStockMapper;
	@Autowired
	ISkuInstockService skuInstockService;
	@Autowired
	IStockDetailService stockDetailService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	IStockLogService stockLogService;
	@Autowired
	ISerialService serialService;

	@Override
	public List<Stock> list(StockQueryDTO stockQueryDTO) {

		QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());

		if (Func.isNotEmpty(stockQueryDTO)) {
			if (Func.isNotEmpty(stockQueryDTO.getWhId())) {
				queryWrapper.lambda().eq(Stock::getWhId, stockQueryDTO.getWhId());
			}
			if (Func.isNotEmpty(stockQueryDTO.getLocCode())) {
				Location location = LocationCache.getValid(stockQueryDTO.getLocCode());
				if (Func.isNotEmpty(location)) {
					queryWrapper.lambda().eq(Stock::getLocId, location.getLocId());
				}
			}
//			if (Func.isNotEmpty(stockQueryDTO.getLpnCode())) {
//				queryWrapper.lambda().eq(Stock::getLpnCode, stockQueryDTO.getLpnCode());
//			}
			if (Func.isNotEmpty(stockQueryDTO.getSkuId())) {
				queryWrapper.lambda().eq(Stock::getSkuId, stockQueryDTO.getSkuId());
			}
			if (Func.isNotEmpty(stockQueryDTO.getLotNumber())) {
				queryWrapper.lambda().eq(Stock::getLotNumber, stockQueryDTO.getLotNumber());
			}

			if (Func.isNotEmpty(stockQueryDTO.getSerialNumberList())) {
				ISerialService serialService = SpringUtil.getBean(ISerialService.class);
				List<Serial> serialList = serialService.list(Condition.getQueryWrapper(new Serial())
					.lambda()
					.in(Serial::getSerialNumber, stockQueryDTO.getSerialNumberList()));
				List<Long> stockIdList = NodesUtil.toList(serialList, Serial::getStockId);
				if (Func.isNotEmpty(stockIdList)) {
					queryWrapper.lambda().in(Stock::getStockId, stockIdList);
				}
			}
		}
		if (!NodesUtil.isAllNull(stockQueryDTO) && queryWrapper.isEmptyOfWhere()) {
			queryWrapper.apply("1 <> 1");
		}
		return super.list(queryWrapper);
	}

	@Override
	public List<StockVO> selectList(StockDTO stockDTO) {
		List<Stock> list = super.list(this.getQueryWrapper(stockDTO));
		return StockWrapper.build().listVO(list);
	}

	@Override
	public IPage<StockVO> selectPage(StockDTO stockDTO, Query query) {
		IPage<Stock> stockIPage = super.page(Condition.getPage(query), this.getQueryWrapper(stockDTO));
		List<Stock> list = stockIPage.getRecords();
		IRelenishmentService relenishmentService = SpringUtil.getBean(IRelenishmentService.class);

		IRelenishmentDetailService relenishmentDetailService = SpringUtil.getBean(IRelenishmentDetailService.class);
		if ("1".equals(stockDTO.getIsRelenish())) {
			list = stockIPage.getRecords().stream().filter(stock -> {
				//List<Relenishment> relenishments = RelenishmentCache.listByWhId(stock.getWhId());
				List<Relenishment> relenishments = relenishmentService.list(Condition.getQueryWrapper(new Relenishment())
					.lambda()
					.eq(Relenishment::getWhId, stock.getWhId())
				);
				if (Func.isNotEmpty(relenishments)) {
					Relenishment relenishment = relenishments.get(0);
					//List<RelenishmentDetail> relenishmentDetails = RelenishmentDetailCache.list(relenishment.getSsiId());
					List<RelenishmentDetail> relenishmentDetails = relenishmentDetailService.list(Condition.getQueryWrapper(new RelenishmentDetail())
						.lambda()
						.eq(RelenishmentDetail::getSsiId, relenishment.getSsiId())
					);
					String toZoneIdstr = NodesUtil.join(relenishmentDetails, RelenishmentDetail::getToZoneId);
					List<Long> toZoneIds = Func.toLongList(toZoneIdstr);
					SkuInstock skuInstock = skuInstockService.getOne(Wrappers.lambdaQuery(SkuInstock.class)
						.eq(SkuInstock::getWhId, stock.getWhId())
						.eq(SkuInstock::getSkuId, stock.getSkuId())
					);
					if (Func.isEmpty(skuInstock)) {
						return false;
					}
					Integer lowStorage = skuInstock.getLowStorage();
					if (Func.isEmpty(lowStorage)) {
						return false;
					}
					return BigDecimalUtil.le(stock.getStockQty(), BigDecimal.valueOf(lowStorage))
						&& toZoneIds.contains(stock.getZoneId());
				}
				return false;
			}).collect(Collectors.toList());
		}

		IPage<StockVO> stockVOIPage = StockWrapper.build().pageVO(stockIPage);
		BigDecimal reduce = list.stream().map(Stock::getStockQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal reduce1 = list.stream().map(Stock::getPickQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal reduce2 = list.stream().map(Stock::getOccupyQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal subtract = reduce.subtract(reduce1).subtract(reduce2);
		stockVOIPage.getRecords().forEach(v -> {
			v.setTotalQty(subtract.stripTrailingZeros().toPlainString());
			if (Func.isNotEmpty(StringUtil.trimAllWhitespace(v.getSkuLot1()))) {
				LocalDate from = LocalDate.parse(StringUtil.trimAllWhitespace(v.getSkuLot1()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				Period period = Period.between(from, LocalDate.now());
				StringBuilder sb = new StringBuilder();
				if (period.getYears() > 0) {
					sb.append(period.getYears()).append("???");
				}
				if (period.getMonths() > 0) {
					sb.append(period.getMonths()).append("???");
				}
				if (period.getDays() >= 0) {
					sb.append(period.getDays()).append("???");
				}
				v.setKuLing(sb.toString());
			} else {
				v.setKuLing("0");
			}
		});
		return stockVOIPage;
	}

	@Override
	public QueryWrapper<Stock> getQueryWrapper(StockDTO stock) {
		QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
		if (Func.isNotEmpty(stock.getWhId())) {
			queryWrapper.lambda().eq(Stock::getWhId, stock.getWhId());
		}
		if (Func.isNotEmpty(stock.getZoneId())) {
			queryWrapper.lambda().eq(Stock::getZoneId, stock.getZoneId());
		}
		// ??????????????????
		if (Func.isNotEmpty(stock.getLocCode())) {
			ILocationService locationService = SpringUtil.getBean(ILocationService.class);
			/*List<Location> locationList = LocationCache.list().stream().filter(u -> {
				return u.getLocCode().indexOf(stock.getLocCode()) > -1;
			}).collect(Collectors.toList());*/
			List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.like(Location::getLocCode, stock.getLocCode())
			);
			if (Func.isNotEmpty(locationList)) {
				queryWrapper.lambda().in(Stock::getLocId, NodesUtil.toList(locationList, Location::getLocId));
			} else {
				queryWrapper.lambda().in(Stock::getLocId, stock.getLocCode());
			}
		}
		if (Func.isNotEmpty(stock.getWoId())) {
			queryWrapper.lambda().eq(Stock::getWoId, stock.getWoId());
		}
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		//????????????,????????????
		if (Func.isNotEmpty(stock.getSkuCode()) && Func.isNotEmpty(stock.getSkuName())) {
			Sku sku = skuService.list(Condition.getQueryWrapper(new Sku()).lambda()
					.eq(Sku::getSkuCode, stock.getSkuCode())
					.eq(Sku::getSkuName, stock.getSkuName()))
				.stream().findFirst().orElse(null);
			if (Func.isNotEmpty(sku)) {
				queryWrapper.lambda().eq(Stock::getSkuId, sku.getSkuId());
			} else {
				queryWrapper.lambda().eq(Stock::getSkuId, "XXX");
			}
		}
		if (Func.isNotEmpty(stock.getSkuCode()) && Func.isEmpty(stock.getSkuName())) {
			Sku sku = skuService.list().stream().filter(u -> {
				return u.getSkuCode().equals(stock.getSkuCode());
			}).findFirst().orElse(null);
			if (Func.isNotEmpty(sku)) {
				queryWrapper.lambda().eq(Stock::getSkuId, sku.getSkuId());
			}
		}
		if (Func.isEmpty(stock.getSkuCode()) && Func.isNotEmpty(stock.getSkuName())) {
			Sku sku = skuService.list().stream().filter(u -> {
				return u.getSkuName().equals(stock.getSkuName());
			}).findFirst().orElse(null);
			if (Func.isNotEmpty(sku)) {
				queryWrapper.lambda().eq(Stock::getSkuId, sku.getSkuId());
			}
		}
		if (Func.isNotEmpty(stock.getWspName())) {
			ISkuPackageService skuPackageService = SpringUtil.getBean(ISkuPackageService.class);
			SkuPackage skuPackage = skuPackageService.list().stream().filter(u -> {
				return u.getWspName().equals(stock.getWspName());
			}).findFirst().orElse(null);
			if (Func.isNotEmpty(skuPackage)) {
				queryWrapper.lambda().eq(Stock::getWspId, skuPackage.getWspId());
			} else {
				queryWrapper.lambda().eq(Stock::getWspId, "XXX");
			}
		}

		if (Func.isNotEmpty(stock.getLotNumber())) {
			queryWrapper.lambda().like(Stock::getLotNumber, stock.getLotNumber());
		}
		if (Func.isNotEmpty(stock.getLastInRange())) {
			String filter = stock.getLastInRange()
				.replace(StringPool.LEFT_SQ_BRACKET, StringPool.EMPTY)
				.replace(StringPool.RIGHT_SQ_BRACKET, StringPool.EMPTY)
				.replace(StringPool.SINGLE_QUOTE, StringPool.EMPTY);
			if (Func.isNotEmpty(filter)) {
				List<String> time = Func.toStrList(filter);
				queryWrapper.lambda()
					.ge(Stock::getLastInTime, DateTimeUtil.parseDateTime(time.get(0)))
					.le(Stock::getLastInTime, DateTimeUtil.parseDateTime(time.get(1)));
			}
		}
		if (Func.isNotEmpty(stock.getLastOutRange())) {
			String filter = stock.getLastInRange()
				.replace(StringPool.LEFT_SQ_BRACKET, StringPool.EMPTY)
				.replace(StringPool.RIGHT_SQ_BRACKET, StringPool.EMPTY)
				.replace(StringPool.SINGLE_QUOTE, StringPool.EMPTY);
			if (Func.isNotEmpty(filter)) {
				List<String> time = Func.toStrList(filter);
				queryWrapper.lambda()
					.ge(Stock::getLastOutTime, DateTimeUtil.parseDateTime(time.get(0)))
					.le(Stock::getLastOutTime, DateTimeUtil.parseDateTime(time.get(1)));
			}
		}
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (stock.skuLotChk(i)) {
				queryWrapper.like("sku_lot" + i, stock.skuLotGet(i));
			}
		}
		return queryWrapper;
	}

	@Override
	public Stock add(@Validated StockAddDTO stockAdd) {
		// ????????????
		if (Func.isEmpty(stockAdd.getLocId()) && Func.isEmpty(stockAdd.getStockId())) {
			throw new ServiceException("????????????????????????ID?????????ID?????????????????????");
		}
		StockVO stock = null;
		// ?????? ??????ID ???????????????????????????????????????
		Sku sku = SkuCache.getById(stockAdd.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException(String.format("??????????????????????????????ID???%s??????", stockAdd.getSkuId()));
		}
		// ????????????
		Long locationId = null;
		if (Func.isNotEmpty(stockAdd.getStockId())) {
			stock = StockWrapper.build().entityVO(super.getById(stockAdd.getStockId()));
			if (Func.isNotEmpty(stock.getLocId())) {
				locationId = stock.getLocId();
			}
		} else {
			locationId = stockAdd.getLocId();
		}
		// ?????????????????????
		Location location = LocationCache.getValid(locationId, stockAdd.getBillNo());

		if (Func.isEmpty(stock)) {
			// ?????? ??????????????? ??????
			ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
			SkuLot skuLot = skuLotService.getById(sku.getWslId());
			if (ObjectUtil.isEmpty(skuLot)) {
				throw new ServiceException(
					String.format("??????[??????:%s, ??????:%s]???????????????????????????", sku.getSkuCode(), sku.getSkuName()));
			}
			// ????????????????????? ????????? ?????? LotDTO ??????
			LotDTO lotDTO = LotWrapper.build().entityDTO(sku, stockAdd);
			lotDTO.setWhId(location.getWhId());
			// ???????????????
			Lot lot = lotService.chkLotNumber(lotDTO);
			if (Func.isEmpty(lot)) {
				throw new ServiceException("???????????????????????????????????????");
			}
			stockAdd.setWhId(location.getWhId());
			stockAdd.setLotNumber(lot.getLotNumber());
			Stock targetStock = super.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getSkuId, stockAdd.getSkuId())
					.eq(Stock::getLocId, stockAdd.getLocId())
					.eq(Stock::getWspId, stockAdd.getWspId())
					.eq(Stock::getLotNumber, stockAdd.getLotNumber()))
				.stream().findFirst().orElse(null);
			stock = StockWrapper.build().entityVO(targetStock);
		}
//		if (Func.isNotEmpty(stock)) {
//			// ???????????????????????????
//			if (!StockStatusEnum.LOCK_FILL.getIndex().equals(stock.getStockStatus())) {
//				throw new ServiceException(
//					String.format("???????????????ID??? %s ???????????????ID???%s??????", stock.getStockStatusDesc(), stock.getStockId()));
//			}
//		}


		return this.stockAdd(stock, stockAdd);
	}

	@Override
	public List<StockSubtractVO> subtract(StockSubtractDTO stockSubtract) {
		// ????????????
		if (Func.isEmpty(stockSubtract.getStockId()) && Func.isEmpty(stockSubtract.getLocId()) &&
			Func.isEmpty(stockSubtract.getLpnCode()) && Func.isEmpty(stockSubtract.getBoxCode())) {
			throw new ServiceException("??????ID?????????ID???????????????????????? ?????????1???????????????");
		}
		if (Func.isEmpty(stockSubtract.getStockId()) && Func.isEmpty(stockSubtract.getSkuId())) {
			throw new ServiceException("??????ID???????????????");
		}
		// ????????????ID??????????????????
		Sku sku = SkuCache.getById(stockSubtract.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException(String.format("??????????????????????????????ID???%s??????", stockSubtract.getSkuId()));
		}
		// ????????????????????????
		if (Func.isNotEmpty(stockSubtract.getLocId())) {
			// ????????????????????????
			LocationCache.getValid(stockSubtract.getLocId(), stockSubtract.getBillNo());
		}

		return stockSubtract(sku, stockSubtract);
	}

	@Override
	public synchronized boolean lock(List<Long> stockIdList) {
		List<Stock> stockList = super.listByIds(stockIdList);
		for (Stock stock : stockList) {
			if (BigDecimalUtil.ne(stock.getOccupyQty(), BigDecimal.ZERO)) {
				throw new ServiceException("????????????????????????, ????????????????????????????????????! ");
			}
			UpdateWrapper<Stock> updateWrapper = new UpdateWrapper();
			updateWrapper.lambda()
				.eq(Stock::getStockId, stock.getStockId())
				.set(Stock::getStockStatus, StockStatusEnum.LOCK_FILL.getIndex())
				.eq(Stock::getStockId, stock.getStockId());
			super.update(updateWrapper);
			// ??????????????????
			List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
				.lambda()
				.eq(StockDetail::getStockId, stock.getStockId()));
			if (Func.isNotEmpty(stockDetailList)) {
				List<Long> stockDetailIdList = NodesUtil.toList(stockDetailList, StockDetail::getId);
				stockDetailService.lock(stockDetailIdList);
			}
		}
		return true;
	}

	@Override
	public synchronized boolean unlock(List<Long> stockIdList) {
		List<Stock> stockList = super.listByIds(stockIdList);
		for (Stock stock : stockList) {
			if (BigDecimalUtil.ne(stock.getOccupyQty(), BigDecimal.ZERO)) {
				throw new ServiceException("????????????????????????, ????????????????????????????????????! ");
			}
			UpdateWrapper<Stock> updateWrapper = new UpdateWrapper();
			updateWrapper.lambda()
				.eq(Stock::getStockId, stock.getStockId())
				.set(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
				.eq(Stock::getStockId, stock.getStockId());
			super.update(updateWrapper);
			// ??????????????????
			List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
				.lambda()
				.eq(StockDetail::getStockId, stock.getStockId()));
			if (Func.isNotEmpty(stockDetailList)) {
				List<Long> stockDetailIdList = NodesUtil.toList(stockDetailList, StockDetail::getId);
				stockDetailService.unlock(stockDetailIdList);
			}
		}
		return true;
	}

	@Override
	public List<AccountsVo> getAccountsWithAsn(List<Long> skuIds, String startTime, String endTime) {
		return baseMapper.getAccountsWithAsn(skuIds, startTime, endTime);
	}

	@Override
	public List<AccountsVo> getAccountsWithPick(List<Long> skuIds, String startTime, String endTime) {
		return baseMapper.getAccountsWithPick(skuIds, startTime, endTime);
	}

	@Override
	public synchronized boolean lockByStock(List<Long> stockIdList, String lockFlag) {
		List<Stock> stockList = super.listByIds(stockIdList);
		for (Stock stock : stockList) {
			if (BigDecimalUtil.ne(stock.getOccupyQty(), BigDecimal.ZERO)) {
				throw new ServiceException("????????????????????????, ????????????????????????! ");
			}
			// ??????????????????
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.LOCK_STOCK);
			systemProcParam.setDataType(DataTypeEnum.StockId);

			systemProcParam.setBillNo(Func.toStr(stock.getStockId()));
			Location location = LocationCache.getById(stock.getLocId());
			if (Func.isNotEmpty(location)) {
				systemProcParam.setLocCode(location.getLocCode());
			}
			Sku sku = SkuCache.getById(stock.getSkuId());
			if (Func.isNotEmpty(sku)) {
				systemProcParam.setSkuCode(sku.getSkuCode());
				systemProcParam.setSkuName(sku.getSkuName());
			}
			systemProcParam.setOperationQty(stock.getStockQty().subtract(stock.getPickQty()));
			SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(stock.getWspId(), stock.getSkuLevel());
			if (Func.isNotEmpty(skuPackageDetail)) {
				systemProcParam.setOperationUnit(skuPackageDetail.getWsuName());
			}
			systemProcParam.setWhId(stock.getWhId());

			UpdateWrapper<Stock> stockUpdateWrapper = new UpdateWrapper<>();
			stockUpdateWrapper.lambda().eq(Stock::getStockId, stock.getStockId());
			if ("1".equals(lockFlag)) {
				stockUpdateWrapper.lambda().set(Stock::getStockStatus, StockStatusEnum.LOCK_FILL.getIndex());
				systemProcParam.setRemark(StockStatusEnum.LOCK_FILL.getName());
				systemProcParam.setAction(ActionEnum.LOCK_STOCK);
			} else {
				stockUpdateWrapper.lambda().set(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex());
				systemProcParam.setRemark(StockStatusEnum.NORMAL.getName());
				systemProcParam.setAction(ActionEnum.UNLOCK_STOCK);
			}
			SystemProc systemProc = systemProcService.create(systemProcParam);
			super.update(stockUpdateWrapper);
//			stockLogService.create(stock, StockProcTypeEnum.Update, systemProc.getSystemProcId());
		}
		return true;
	}

	@Override
	public synchronized boolean lockByLot(List<Long> stockIdList) {
		ILotService lotService = SpringUtil.getBean(ILotService.class);
		List<Stock> stockList = super.listByIds(stockIdList);
		List<String> lotNumberList = NodesUtil.toList(stockList, Stock::getLotNumber);
		List<Stock> stockListAll = new ArrayList<>();
		if (Func.isNotEmpty(lotNumberList)) {
			stockListAll = super.list(Condition.getQueryWrapper(new Stock()).lambda()
				.in(Stock::getLotNumber, lotNumberList));
		}
		// ??????????????????
		stockListAll.stream().collect(Collectors.groupingBy(Stock::getLotNumber)).forEach((lotNumber, list) -> {
			List<Stock> occupyStockList = list.stream().filter(u -> {
				return BigDecimalUtil.ne(u.getOccupyQty(), BigDecimal.ZERO);
			}).collect(Collectors.toList());
			if (Func.isNotEmpty(occupyStockList)) {
				throw new ServiceException("?????????[" + lotNumber + "]??????????????????, ????????????????????????! ");
			}
			// ??????????????????
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.LOCK_LOT);
			systemProcParam.setDataType(DataTypeEnum.LotId);
			systemProcParam.setAction(ActionEnum.LOCK_LOT);
			systemProcParam.setBillNo(lotNumber);

			//Lot lot = LotCache.getByLotNumber(lotNumber);
			Lot lot = lotService.list(Condition.getQueryWrapper(new Lot())
				.lambda()
				.eq(Lot::getLotNumber, lotNumber)
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(lot)) {
				throw new ServiceException("?????????[" + lotNumber + "] ?????????! ");
			}
			if (Func.isEmpty(lot.getLotStatus()) || StockStatusEnum.NORMAL.getIndex().equals(lot.getLotStatus())) {
				lot.setLotStatus(StockStatusEnum.LOCK_FILL.getIndex());
				systemProcParam.setRemark(StockStatusEnum.LOCK_FILL.getName());
			} else {
				lot.setLotStatus(StockStatusEnum.NORMAL.getIndex());
				systemProcParam.setRemark(StockStatusEnum.NORMAL.getName());
			}
			systemProcService.create(systemProcParam);
			lotService.updateById(lot);
		});
		return true;
	}

	/**
	 * ??????????????????
	 *
	 * @param stock    ????????????????????????????????????
	 * @param stockAdd ??????????????????
	 * @return ????????????????????????
	 */
	public synchronized Stock stockAdd(Stock stock, StockAddDTO stockAdd) {
		// ??????????????????????????????????????????????????????????????????????????????????????????????????????stock ??? null
		if (Func.isNotEmpty(stock)) {
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone zone = zoneService.getById(stock.getZoneId());
			if (Func.isEmpty(zone)) {
				throw new ServiceException("????????????[ID:" + stock.getZoneId() + "]?????????!");
			}
			if (ZoneTypeEnum.SHIPPING_OUTSTOCK.getIndex().equals(zone.getZoneType())) {
				stockAdd.setLotNumber(stock.getLotNumber());
				stock = null;
			}
		}
		// ?????????????????????????????????(?????????????????????????????????????????????????????????????????????
		if (ObjectUtil.isNotEmpty(stock)) {
			// ??????????????????
			stock.setStockQty(stock.getStockQty().add(stockAdd.getStockQty()));
			UpdateWrapper<Stock> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				//.set(Stock::getValidTime, stock.getValidTime())
				.set(Stock::getStockQty, stock.getStockQty())
				.set(Stock::getLastInTime, LocalDateTime.now())
				.set(Stock::getUpdateTime, LocalDateTime.now())
				.eq(Stock::getStockId, stock.getStockId())
				.eq(Stock::getWhId, stockAdd.getWhId());
			super.update(updateWrapper);
		} else {
			stock = StockWrapper.build().entity(stockAdd);
			stock.setStockQty(stockAdd.getStockQty());
			stock.setLastInTime(LocalDateTime.now());
			//stock.setValidTime(stockAdd.getValidTime());
			// ??????????????????
			super.save(stock);
		}
		// ??????????????????
		StockDetailAddDTO stockDetailAddDTO = new StockDetailAddDTO();
		stockDetailAddDTO.setWhId(stockAdd.getWhId());
		stockDetailAddDTO.setStockId(stock.getStockId());
		stockDetailAddDTO.setLpnCode(stockAdd.getLpnCode());
		stockDetailAddDTO.setBoxCode(stockAdd.getBoxCode());
		stockDetailAddDTO.setBillDetailId(stockAdd.getBillDetailId());
		stockDetailAddDTO.setSoBillId(stockAdd.getBillId());
		stockDetailAddDTO.setWellenId(stockAdd.getWellenId());
		stockDetailAddDTO.setStockQty(stockAdd.getStockQty());
		stockDetailAddDTO.setSerialList(stockAdd.getSerialList());
		stockDetailAddDTO.setStock(stock);
		stockDetailAddDTO.setId(stockAdd.getSdId());
		stockDetailAddDTO.setSystemProcId(stockAdd.getSystemProcId());
		stockDetailService.add(stockDetailAddDTO);

		return stock;
	}

	/**
	 * ??????????????????
	 *
	 * @param sku           ????????????
	 * @param stockSubtract ??????????????????
	 * @return ??????????????????????????? List<StockReduceRltDTO>
	 */
	@Override
	public synchronized List<StockSubtractVO> stockSubtract(Sku sku, StockSubtractDTO stockSubtract) {
		List<StockSubtractVO> rltList = new ArrayList<>();
		// ????????????????????????????????????
		List<Stock> stockList = super.list(Condition.getQueryWrapper(new Stock())
			.lambda()
			.func(sql -> {
				if (Func.isNotEmpty(stockSubtract.getStockId())) {
					sql.eq(Stock::getStockId, stockSubtract.getStockId());
				} else {
					if (Func.isNotEmpty(stockSubtract.getLocId())) {
						sql.eq(Stock::getLocId, stockSubtract.getLocId());
					}
					if (Func.isNotEmpty(stockSubtract.getSkuId())) {
						sql.eq(Stock::getSkuId, stockSubtract.getSkuId());
					}
					if (Func.isNotEmpty(stockSubtract.getLotNumber())) {
						sql.eq(Stock::getLotNumber, stockSubtract.getLotNumber());
					}
				}
			})
			.apply("stock_qty <> pick_qty")
		);
		if (ObjectUtil.isEmpty(stockList)) {
			throw new ServiceException(
				String.format("?????????%s ??????????????????%s???", sku.getSkuName(), stockSubtract.toSimpleString()));
		}
		// ?????????????????????????????????
		List<Lot> lotList = lotService.list(Condition.getQueryWrapper(new Lot()).lambda()
			.in(Lot::getLotNumber, NodesUtil.toList(stockList, Stock::getLotNumber)));
		for (Lot lot : lotList) {
			if (!StockStatusEnum.NORMAL.getIndex().equals(lot.getLotStatus())) {
				throw new ServiceException("?????????[" + lot.getLotNumber() + "]???????????????????????????????????????");
			}
		}
		// ????????????
		BigDecimal pickQty = stockSubtract.getPickQty();
		// ???????????????????????? / ??????????????????
		List<StockOccupy> stockOccupyListAll = new ArrayList<>();
		if (stockSubtract.getEventType().equals(EventTypeEnum.OutStock) ||
			stockSubtract.getEventType().equals(EventTypeEnum.Move)) {

			stockOccupyListAll = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
				.lambda());
//				.eq(StockOccupy::getPickPlanId, stockSubtract.getDataId()));
		} else if (stockSubtract.getEventType().equals(EventTypeEnum.Count)) {
			stockOccupyListAll = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
				.lambda()
//				.eq(StockOccupy::getWcrId, stockSubtract.getDataId())
			);
		}
		List<StockDetail> stockDetailListAll = new ArrayList<>();
		if (Func.isNotEmpty(stockList)) {
			stockDetailListAll = stockDetailService.list(Condition.getQueryWrapper(new StockDetail()).lambda()
				.in(StockDetail::getStockId, NodesUtil.toList(stockList, Stock::getStockId)));
		}
		for (Stock stock : stockList) {
			// ????????????????????????????????????????????????????????????
			if (!StockStatusEnum.NORMAL.getIndex().equals(stock.getStockStatus())) {
				continue;
			}
			BigDecimal qty = BigDecimal.ZERO;
			// ??????ID??????????????????????????????
			if (ObjectUtil.isEmpty(stockSubtract.getDataId())) {
				// ????????????????????? = ????????? - ???????????? - ????????? - ????????????
				qty = stock.getStockQty()
					.subtract(stock.getPickQty())
					.subtract(stock.getOccupyQty());
				if (BigDecimalUtil.le(qty, BigDecimal.ZERO)) {
					continue;
				}
				if (BigDecimalUtil.ge(pickQty, qty)) {    // ???????????? ?????? ????????????
					stock.setPickQty(stock.getPickQty().add(qty));
					pickQty = pickQty.subtract(qty);
				} else {                                  // ???????????? ?????? ????????????
					stock.setPickQty(stock.getPickQty().add(pickQty));
					pickQty = pickQty.subtract(pickQty);
				}
				// ???????????????
				StockSubtractVO stockReduceRltDTO = new StockSubtractVO();
				stockReduceRltDTO.setStock(stock);
				rltList.add(stockReduceRltDTO);
			} else {
				// ?????????????????????????????????????????????????????????
				List<StockOccupy> stockOccupyList = stockOccupyListAll.stream().filter(u -> {
					return stock.getStockId().equals(u.getStockId());
				}).collect(Collectors.toList());
				// ????????????????????? = ??????????????????.????????????
				if (ObjectUtil.isEmpty(stockOccupyList)) {
					continue;
				}
				for (StockOccupy stockOccupy : stockOccupyList) {
//					qty = stockOccupy.getOccupyQty();
					if (BigDecimalUtil.ge(pickQty, qty)) {    // ???????????? ???????????? ????????????
						if (stockSubtract.getEventType().equals(EventTypeEnum.OutStock) ||
							stockSubtract.getEventType().equals(EventTypeEnum.Move)) {
							// ???????????????????????????????????????
							stock.setPickQty(stock.getPickQty().add(qty));
							stock.setOccupyQty(stock.getOccupyQty().subtract(qty));
						} else if (stockSubtract.getEventType().equals(EventTypeEnum.Count)) {
							// ???????????????????????????????????????
							stock.setStockQty(stock.getStockQty().subtract(qty));
						}
						pickQty = pickQty.subtract(qty);
						if (ObjectUtil.isNotEmpty(stockOccupy)) {
//							stockOccupy.setOccupyQty(stockOccupy.getOccupyQty().subtract(qty));
//							if (stockOccupy.getOccupyQty().compareTo(BigDecimal.ZERO) <= 0) {
//								// ????????????????????????
//								stockOccupyService.removeById(stockOccupy.getWsoId());
//							} else {
//								stockOccupyService.updateOccupyQty(stockOccupy);
//							}
						}
					} else {                // ???????????? ?????? ????????????
						if (stockSubtract.getEventType().equals(EventTypeEnum.OutStock) ||
							stockSubtract.getEventType().equals(EventTypeEnum.Move)) {
							// ???????????????????????????????????????
							stock.setPickQty(stock.getPickQty().add(pickQty));
							stock.setOccupyQty(stock.getOccupyQty().subtract(pickQty));
						} else if (stockSubtract.getEventType().equals(EventTypeEnum.Count)) {
							// ???????????????????????????????????????
							stock.setStockQty(stock.getStockQty().subtract(pickQty));
						}
						if (ObjectUtil.isNotEmpty(stockOccupy)) {
							// ????????????????????????
//							stockOccupy.setOccupyQty(stockOccupy.getOccupyQty().subtract(pickQty));
							stockOccupyService.updateOccupyQty(stockOccupy);
						}
						pickQty = pickQty.subtract(pickQty);
					}
					StockSubtractVO stockReduceRltDTO = new StockSubtractVO();
//					stockReduceRltDTO.setSoBillId(stockOccupy.getSoBillId());
//					stockReduceRltDTO.setSoDetailId(stockOccupy.getSoDetailId());
					stockReduceRltDTO.setStock(stock);
					rltList.add(stockReduceRltDTO);
					if (BigDecimalUtil.le(pickQty, BigDecimal.ZERO)) {
						break;
					}
				}
			}
			final BigDecimal[] surplus_qty = {stockSubtract.getPickQty()};
			stockDetailListAll.stream().filter(u -> Func.equals(u.getStockId(), stock.getStockId()))
				.forEach(stockDetail -> {
					if (BigDecimalUtil.le(surplus_qty[0], BigDecimal.ZERO)) {
						return;
					}
					StockDetailSubtractDTO stockDetailSubtract = new StockDetailSubtractDTO();
					stockDetailSubtract.setStockId(stock.getStockId());
					stockDetailSubtract.setStock(stock);
					if (Func.isNotEmpty(stockSubtract.getStockId())) {
						stockDetailSubtract.setLpnCode(stockDetail.getLpnCode());
						stockDetailSubtract.setBoxCode(stockDetail.getBoxCode());
					} else {
						if (Func.isNotEmpty(stockSubtract.getLpnCode())) {
							stockDetailSubtract.setLpnCode(stockSubtract.getLpnCode());
						}
						if (Func.isNotEmpty(stockSubtract.getBoxCode())) {
							stockDetailSubtract.setBoxCode(stockSubtract.getBoxCode());
						}
					}
					if (BigDecimalUtil.le(surplus_qty[0], stockDetail.getStockQty().subtract(stockDetail.getPickQty()))) {
						stockDetailSubtract.setPickQty(surplus_qty[0]);
						surplus_qty[0] = BigDecimal.ZERO;
					} else {
						stockDetailSubtract.setPickQty(stockDetail.getStockQty().subtract(stockDetail.getPickQty()));
						surplus_qty[0] = surplus_qty[0].subtract(stockDetail.getStockQty().subtract(stockDetail.getPickQty()));
					}
					stockDetailSubtract.setPickQty(stockSubtract.getPickQty());
					stockDetailSubtract.setBillDetailId(stockSubtract.getBillDetailId());
					stockDetailSubtract.setSoBillId(stockSubtract.getBillId());
					stockDetailSubtract.setWellenId(stockSubtract.getWellenId());
					stockDetailSubtract.setSerialList(stockSubtract.getSerialList());
					stockDetailSubtract.setSystemProcId(stockSubtract.getSystemProcId());
					stockDetailService.substract(stockDetailSubtract);
					// ??????????????????
					stockLogService.create(stock, stockDetail, StockProcTypeEnum.Update, stockSubtract.getSystemProcId());
				});
			// ??????????????????
			if (BigDecimalUtil.eq(stock.getStockQty(), stock.getPickQty())) {
				// ????????????
				super.removeById(stock.getStockId());
			} else {
				// ????????????
				UpdateWrapper<Stock> updateWrapper = new UpdateWrapper<>();
				updateWrapper.lambda()
					.set(Stock::getOccupyQty, stock.getOccupyQty())
					.set(Stock::getPickQty, stock.getPickQty())
					.set(Stock::getStockQty, stock.getStockQty())
					.set(Stock::getLastOutTime, LocalDateTime.now())
					.eq(Stock::getStockId, stock.getStockId());
				super.update(updateWrapper);
			}
			if (BigDecimalUtil.eq(pickQty, BigDecimal.ZERO)) {
				break;
			}
		}
		if (BigDecimalUtil.gt(pickQty, BigDecimal.ZERO)) {
			throw new ServiceException(
				String.format("?????????%s ??????????????????%s???", sku.getSkuName(), stockSubtract.toSimpleString()));
		}

		return rltList;
	}

	@Override
	public List<Stock> getStockListBySkuAndLoc(Long skuId, String locCode) {
		return baseMapper.getStockListBySkuAndLoc(skuId, locCode);
	}

	@Override
	public List<StockDetail> getStockListBylpnAndbox(Long skuId, String locCode, String lpnCode, String boxCode) {
		return baseMapper.getStockListBylpnAndbox(skuId, locCode, lpnCode, boxCode);
	}

	@Override
	public List<Stock> selectStockByLoc(List<Long> locIds, Integer abc, List<Long> skuIds) {
		return baseMapper.selectStockByLoc(abc, locIds, skuIds);
	}

	/**
	 * ????????????????????????
	 *
	 * @param stockMove ???????????? StockMoveDTO
	 * @return ????????????????????????
	 */
	@Override
	public List<Stock> stockMove(StockMoveDTO stockMove) {
		// ????????????
		if (Func.isEmpty(stockMove.getSourceLocId())) {
			if (ObjectUtil.isEmpty(stockMove.getSourceLocId())) {
				throw new ServiceException("???????????????ID???????????????");
			}
		}
		if (Func.isEmpty(stockMove.getTargetStockId())) {
			if (ObjectUtil.isEmpty(stockMove.getTargetLocId())) {
				throw new ServiceException("??????????????????ID???????????????");
			}
		}
		if (ObjectUtil.isEmpty(stockMove.getSkuId())) {
			throw new ServiceException("????????????ID???????????????");
		}
		if (ObjectUtil.isEmpty(stockMove.getMoveQty())) {
			throw new ServiceException("?????????????????????????????????");
		}
		if (BigDecimalUtil.eq(stockMove.getMoveQty(), BigDecimal.ZERO)) {
			throw new ServiceException("?????????????????????????????? 0 ???");
		}
		if (ObjectUtil.isEmpty(stockMove.getEventType())) {
			throw new ServiceException("???????????????????????????");
		}
		// ????????????
		Location sourceLoc = null;
		if (Func.isEmpty(stockMove.getSourceStockId())) {
			sourceLoc = LocationCache.getById(stockMove.getSourceLocId());
		} else {
			Stock stock = super.getById(stockMove.getSourceStockId());
			if (Func.isEmpty(stock)) {
				throw new ServiceException("???????????????????????????");
			}
			sourceLoc = LocationCache.getById(stock.getLocId());
		}
		Location targetLoc = null;
		if (Func.isEmpty(stockMove.getTargetStockId())) {
			targetLoc = LocationCache.getById(stockMove.getTargetLocId());
		} else {
			Stock stock = super.getById(stockMove.getTargetStockId());
			if (Func.isEmpty(stock)) {
				throw new ServiceException("??????????????????????????????");
			}
			targetLoc = LocationCache.getById(stock.getLocId());
		}
		if (sourceLoc.getLocId().equals(targetLoc.getLocId())) {
			throw new ServiceException("???????????????????????????????????????");
		}
		// ????????????
		Sku sku = SkuCache.getById(stockMove.getSkuId());
		if (ObjectUtil.isEmpty(sku)) {
			throw new ServiceException(String.format("??????????????????????????????ID???%s??????", stockMove.getSkuId()));
		}
		// ????????????????????????????????????
		StockSubtractDTO stockReduce = StockWrapper.build().moveToReduce(stockMove);
		if (ObjectUtil.isEmpty(stockReduce)) {
			throw new ServiceException("?????????????????????StockMoveDTO ??? StockReduceDTO??????");
		}
		stockReduce.setLocCode(sourceLoc.getLocCode());
		List<StockSubtractVO> stockReduceRltList = this.stockSubtract(sku, stockReduce);
		if (stockReduceRltList.size() == 0) {
			throw new ServiceException("????????????????????????");
		}
		List<Stock> resultStock = new ArrayList<>();
		// ????????????????????????????????????????????????
		for (StockSubtractVO stockReduceRlt : stockReduceRltList) {
			if (ObjectUtil.isEmpty(stockReduceRlt.getStock())) {
				continue;
			}
			// ????????????????????????????????????
			StockAddDTO stockProc = StockWrapper.build().entityProc(stockReduceRlt.getStock(), stockMove);
			if (Func.isNotEmpty(stockMove.getTargetStockId())) {
				List<Serial> serialList = serialService.listByStockId(stockMove.getTargetStockId());
				List<String> serialNumbers = NodesUtil.toList(serialList, Serial::getSerialNumber);
				stockProc.setSerialList(serialNumbers);
			}
			if (ObjectUtil.isEmpty(stockProc)) {
				throw new ServiceException("?????????????????????Stock, StockMoveDTO ??? StockProcDTO??????");
			}
			stockProc.setBillId(stockReduceRlt.getSoBillId());
			stockProc.setBillDetailId(stockReduceRlt.getSoDetailId());
			stockProc.setBoxCode(stockMove.getTargetBoxCode());
			Stock targetStock = super.list(Condition.getQueryWrapper(new Stock()).lambda()
				.eq(Stock::getSkuId, stockProc.getSkuId())
				.eq(Stock::getLocId, stockProc.getLocId())
//				.func(sql -> {
//					if (StringPool.ZERO.equals(ParamCache.getValue(ParamEnum.SYSTEM_LPN_ENABLE))) {
//						sql.eq(Stock::getLpnCode, stockProc.getLpnCode());
//					}
//				})
//				.func(sql -> {
//					if (Func.isNotEmpty(stockProc.getBoxCode())) {
//						sql.eq(Stock::getBoxCode, stockProc.getBoxCode());
//					}
//				})
				.eq(Stock::getWspId, stockProc.getWspId())
				.eq(Stock::getLotNumber, stockProc.getLotNumber())).stream().findFirst().orElse(null);
			StockVO targetStockVO = StockWrapper.build().entityVO(targetStock);
			if (ObjectUtil.isNotEmpty(targetStockVO)) {
				// ???????????????????????????
				if (!StockStatusEnum.NORMAL.getIndex().equals(targetStockVO.getStockStatus())) {
					throw new ServiceException(
						String.format("???????????????ID??? %s ???????????????ID???%s??????",
							targetStockVO.getStockStatusDesc(), targetStockVO.getStockId()));
				}
			}
			// ????????????
			resultStock.add(stockAdd(targetStockVO, stockProc));
		}

		return resultStock;
	}

	/**
	 * ??????????????????
	 *
	 * @param stockMove
	 */
	@Override
	public void stockMoveByLpn(StockMoveDTO stockMove) {
		if (Func.isNotEmpty(stockMove.getSourceStockId()) || Func.isNotEmpty(stockMove.getTargetStockId())) {
			throw new ServiceException("?????????????????????????????????????????????ID???");
		}
		if (ObjectUtil.isEmpty(stockMove.getTargetLocId())) {
			throw new ServiceException("??????????????????ID???????????????");
		}
		if (ObjectUtil.isEmpty(stockMove.getEventType())) {
			throw new ServiceException("???????????????????????????");
		}
		if (ObjectUtil.isEmpty(stockMove.getSystemProcId())) {
			throw new ServiceException("????????????id???????????????");
		}
		// ????????????
		Location sourceLoc = LocationCache.getValid(stockMove.getSourceLocId());
		Location targetLoc = LocationCache.getValid(stockMove.getTargetLocId());
		if (sourceLoc.getLocId().equals(targetLoc.getLocId())) {
			throw new ServiceException("???????????????????????????????????????");
		}
		// ????????????????????????????????????
		StockSubtractDTO stockReduce = StockWrapper.build().moveToReduce(stockMove);
		if (ObjectUtil.isEmpty(stockReduce)) {
			throw new ServiceException("?????????????????????StockMoveDTO ??? StockReduceDTO??????");
		}
		stockReduce.setLocCode(sourceLoc.getLocCode());
		// ??????????????????????????????
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.func(sql -> {
				if (Func.isNotEmpty(stockReduce.getStockId())) {
					sql.eq(StockDetail::getStockId, stockReduce.getStockId());
				} else {
					if (Func.isNotEmpty(stockReduce.getLocId())) {
						sql.eq(StockDetail::getLocId, stockReduce.getLocId());
					}
					if (Func.isNotEmpty(stockReduce.getLpnCode())) {
						sql.eq(StockDetail::getLpnCode, stockReduce.getLpnCode());
					}
					if (Func.isNotEmpty(stockReduce.getBoxCode())) {
						sql.eq(StockDetail::getBoxCode, stockReduce.getBoxCode());
					}
					if (Func.isNotEmpty(stockReduce.getSkuId())) {
						sql.eq(StockDetail::getSkuId, stockReduce.getSkuId());
					}
					if (Func.isNotEmpty(stockReduce.getLotNumber())) {
						sql.eq(StockDetail::getLotNumber, stockReduce.getLotNumber());
					}
				}
			}));
		// ??????????????????????????????
		if (ObjectUtil.isEmpty(stockDetailList)) {
			throw new ServiceException(String.format("?????????%s ?????????%s ???????????????????????????????????????????????????",
				sourceLoc.getLocCode(), stockMove.getSourceLpnCode()));
		}
		List<Stock> stockList = super.list(Condition.getQueryWrapper(new Stock()).lambda()
			.in(Stock::getStockId, NodesUtil.toList(stockDetailList, StockDetail::getStockId)));
//		Stock occupyStock = stockList.stream().filter(u -> BigDecimalUtil.gt(u.getOccupyQty(), BigDecimal.ZERO))
//			.findFirst().orElse(null);
//		if (Func.isNotEmpty(occupyStock)) {
//			throw new ServiceException(String.format("?????????%s, ?????????%s  ?????????????????????????????????????????????????????????",
//				occupyStock.getLocCode(), stockMove.getSourceLpnCode()));
//		}
		// ???????????????????????????????????????
		for (StockDetail stockDetail : stockDetailList) {
			// ????????????????????? = ????????? - ????????????
			stockReduce.setPickQty(stockDetail.getStockQty().subtract(stockDetail.getPickQty()));
			stockReduce.setStockId(stockDetail.getStockId());
			stockReduce.setLpnCode(stockDetail.getLpnCode());
			stockReduce.setBoxCode(stockDetail.getBoxCode());
			// ?????????????????????????????????
			List<Serial> serialList = serialService.listByStockDetailId(stockDetail.getId());
			stockReduce.getSerialList().clear();
			if (ObjectUtil.isNotEmpty(serialList)) {
				serialList.forEach(serial -> {
					stockReduce.getSerialList().add(serial.getSerialNumber());
				});
			}
			// ????????????????????????????????????
			Sku sku = SkuCache.getById(stockDetail.getSkuId());
			List<StockSubtractVO> stockReduceRltList = this.stockSubtract(sku, stockReduce);
			if (stockReduceRltList.size() == 0) {
				throw new ServiceException("????????????????????????");
			}
			List<Stock> resultStock = new ArrayList<>();
			// ????????????????????????????????????????????????
			for (StockSubtractVO stockReduceRlt : stockReduceRltList) {
				if (ObjectUtil.isEmpty(stockReduceRlt.getStock())) {
					continue;
				}
				// ????????????????????????????????????
				StockAddDTO stockProc = StockWrapper.build().entityProc(stockReduceRlt.getStock(), stockMove);
				stockProc.setStockId(stockReduce.getStockId());
				stockProc.setStockQty(stockReduce.getPickQty());
				stockProc.setSerialList(stockReduce.getSerialList());
				stockProc.setLpnCode(stockReduce.getLpnCode());
				stockProc.setBoxCode(stockReduce.getBoxCode());
				stockProc.setBillId(stockReduceRlt.getSoBillId());
				stockProc.setBillDetailId(stockReduceRlt.getSoDetailId());
				if (ObjectUtil.isEmpty(stockProc)) {
					throw new ServiceException("?????????????????????Stock, StockMoveDTO ??? StockProcDTO??????");
				}
				Stock targetStock = super.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getSkuId, stockProc.getSkuId())
					.eq(Stock::getLocId, stockProc.getLocId())
					.eq(Stock::getWspId, stockProc.getWspId())
					.eq(Stock::getLotNumber, stockProc.getLotNumber())).stream().findFirst().orElse(null);
				StockVO targetStockVO = StockWrapper.build().entityVO(targetStock);
				if (ObjectUtil.isNotEmpty(targetStockVO)) {
					// ???????????????????????????
					if (!StockStatusEnum.NORMAL.getIndex().equals(targetStockVO.getStockStatus())) {
						throw new ServiceException(
							String.format("???????????????ID??? %s ???????????????ID???%s??????",
								targetStockVO.getStockStatusDesc(), targetStockVO.getStockId()));
					}
				}
				// ????????????
				Stock stockAdd = stockAdd(targetStockVO, stockProc);
				resultStock.add(stockAdd);
			}
		}
	}

	@Override
	public List<ErpStockVO> listErpCompare() {
		// ????????????
		List<ErpStockVO> list = new ArrayList<>();
		// ????????????????????????
		List<Stock> stockList = this.list();
		List<Stock> stockListBackup = new ArrayList<>();
		// ??????ERP??????
		List<ErpStockVO> erpStockList = ErpStockWrapper.build().listVO(erpStockMapper.selectList(null));

		for (ErpStockVO erpStock : erpStockList) {
			List<Stock> findList = stockList.stream().filter(stock -> {
				return erpStock.getWhId().equals(stock.getWhId())
					&& erpStock.getWoId().equals(stock.getWoId())
					&& erpStock.getSkuId().equals(stock.getSkuId())
					&& erpStock.getWspId().equals(stock.getWspId())
					&& erpStock.getSkuLevel().equals(stock.getSkuLevel());
			}).collect(Collectors.toList());
			if (ObjectUtil.isNotEmpty(findList)) {
				Stock stock = findList.get(0);
				erpStock.setWmsStockQty(stock.getStockQty());
				stockListBackup.add(stock);
				// ???????????????
				BigDecimal erpStockQty = ObjectUtil
					.isNotEmpty(erpStock.getStockQty()) ? erpStock.getStockQty() : BigDecimal.ZERO;
				BigDecimal wmsStockQty = ObjectUtil
					.isNotEmpty(stock.getStockQty()) ? stock.getStockQty() : BigDecimal.ZERO;
				erpStock.setDiffQty(erpStockQty.subtract(wmsStockQty));
			}
			list.add(erpStock);
		}
		for (Stock stock : stockList) {
			if (stockListBackup.contains(stock)) {
				continue;
			}
			ErpStockVO erpStock = ErpStockWrapper.build().entityVO(stock);
			// ???????????????
			BigDecimal erpStockQty = ObjectUtil
				.isNotEmpty(erpStock.getStockQty()) ? erpStock.getStockQty() : BigDecimal.ZERO;
			BigDecimal wmsStockQty = ObjectUtil
				.isNotEmpty(stock.getStockQty()) ? stock.getStockQty() : BigDecimal.ZERO;
			erpStock.setDiffQty(erpStockQty.subtract(wmsStockQty));
			list.add(erpStock);
		}

		return list;
	}

}
