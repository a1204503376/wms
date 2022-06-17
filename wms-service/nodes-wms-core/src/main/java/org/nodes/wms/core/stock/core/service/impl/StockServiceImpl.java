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
import org.nodes.wms.core.basedata.entity.*;
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
import org.nodes.wms.dao.basics.zone.entities.Zone;
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
 * 库存 服务实现类
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
					sb.append(period.getYears()).append("年");
				}
				if (period.getMonths() > 0) {
					sb.append(period.getMonths()).append("月");
				}
				if (period.getDays() >= 0) {
					sb.append(period.getDays()).append("天");
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
		// 库位编码条件
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
		//物品编码,物品名称
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
		// 参数验证
		if (Func.isEmpty(stockAdd.getLocId()) && Func.isEmpty(stockAdd.getStockId())) {
			throw new ServiceException("库存增加时，库位ID和库存ID不能同时为空！");
		}
		StockVO stock = null;
		// 根据 物品ID 查询该物品设定的批属性验证
		Sku sku = SkuCache.getById(stockAdd.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException(String.format("物品信息不存在（物品ID：%s）！", stockAdd.getSkuId()));
		}
		// 验证库位
		Long locationId = null;
		if (Func.isNotEmpty(stockAdd.getStockId())) {
			stock = StockWrapper.build().entityVO(super.getById(stockAdd.getStockId()));
			if (Func.isNotEmpty(stock.getLocId())) {
				locationId = stock.getLocId();
			}
		} else {
			locationId = stockAdd.getLocId();
		}
		// 获取并验证库位
		Location location = LocationCache.getValid(locationId, stockAdd.getBillNo());

		if (Func.isEmpty(stock)) {
			// 获取 物品批属性 设置
			ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
			SkuLot skuLot = skuLotService.getById(sku.getWslId());
			if (ObjectUtil.isEmpty(skuLot)) {
				throw new ServiceException(
					String.format("物品[编码:%s, 名称:%s]未设置批属性信息！", sku.getSkuCode(), sku.getSkuName()));
			}
			// 根据物品设定的 批属性 封装 LotDTO 对象
			LotDTO lotDTO = LotWrapper.build().entityDTO(sku, stockAdd);
			lotDTO.setWhId(location.getWhId());
			// 创建批次号
			Lot lot = lotService.chkLotNumber(lotDTO);
			if (Func.isEmpty(lot)) {
				throw new ServiceException("创建或查找批次号发生异常！");
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
//			// 验证库存的锁定状态
//			if (!StockStatusEnum.LOCK_FILL.getIndex().equals(stock.getStockStatus())) {
//				throw new ServiceException(
//					String.format("指定的库存ID为 %s 状态（库存ID：%s）！", stock.getStockStatusDesc(), stock.getStockId()));
//			}
//		}


		return this.stockAdd(stock, stockAdd);
	}

	@Override
	public List<StockSubtractVO> subtract(StockSubtractDTO stockSubtract) {
		// 参数验证
		if (Func.isEmpty(stockSubtract.getStockId()) && Func.isEmpty(stockSubtract.getLocId()) &&
			Func.isEmpty(stockSubtract.getLpnCode()) && Func.isEmpty(stockSubtract.getBoxCode())) {
			throw new ServiceException("库存ID，库位ID，容器编码，箱号 必须有1个不为空！");
		}
		if (Func.isEmpty(stockSubtract.getStockId()) && Func.isEmpty(stockSubtract.getSkuId())) {
			throw new ServiceException("物品ID不能为空！");
		}
		// 根据物品ID找到物品信息
		Sku sku = SkuCache.getById(stockSubtract.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException(String.format("指定物品不存在（物品ID：%s）！", stockSubtract.getSkuId()));
		}
		// 验证货位是否存在
		if (Func.isNotEmpty(stockSubtract.getLocId())) {
			// 库位锁定状态验证
			LocationCache.getValid(stockSubtract.getLocId(), stockSubtract.getBillNo());
		}

		return stockSubtract(sku, stockSubtract);
	}

	@Override
	public synchronized boolean lock(List<Long> stockIdList) {
		List<Stock> stockList = super.listByIds(stockIdList);
		for (Stock stock : stockList) {
			if (BigDecimalUtil.ne(stock.getOccupyQty(), BigDecimal.ZERO)) {
				throw new ServiceException("库存存在占用信息, 请解除占用后再执行此操作! ");
			}
			UpdateWrapper<Stock> updateWrapper = new UpdateWrapper();
			updateWrapper.lambda()
				.eq(Stock::getStockId, stock.getStockId())
				.set(Stock::getStockStatus, StockStatusEnum.LOCK_FILL.getIndex())
				.eq(Stock::getStockId, stock.getStockId());
			super.update(updateWrapper);
			// 锁定库存明细
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
				throw new ServiceException("库存存在占用信息, 请解除占用后再执行此操作! ");
			}
			UpdateWrapper<Stock> updateWrapper = new UpdateWrapper();
			updateWrapper.lambda()
				.eq(Stock::getStockId, stock.getStockId())
				.set(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
				.eq(Stock::getStockId, stock.getStockId());
			super.update(updateWrapper);
			// 锁定库存明细
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
				throw new ServiceException("库存存在占用信息, 不允许执行此操作! ");
			}
			// 插入系统日志
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
		// 按批次号分组
		stockListAll.stream().collect(Collectors.groupingBy(Stock::getLotNumber)).forEach((lotNumber, list) -> {
			List<Stock> occupyStockList = list.stream().filter(u -> {
				return BigDecimalUtil.ne(u.getOccupyQty(), BigDecimal.ZERO);
			}).collect(Collectors.toList());
			if (Func.isNotEmpty(occupyStockList)) {
				throw new ServiceException("批次号[" + lotNumber + "]存在占用信息, 不允许执行此操作! ");
			}
			// 插入系统日志
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
				throw new ServiceException("批次号[" + lotNumber + "] 不存在! ");
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
	 * 增加库存数量
	 *
	 * @param stock    待增加库存数量的库存信息
	 * @param stockAdd 库存增加参数
	 * @return 更新后得库存信息
	 */
	public synchronized Stock stockAdd(Stock stock, StockAddDTO stockAdd) {
		// 如果目标库区属于出库暂存区，保证一个拣货记录为一条库存记录，直接修改stock 为 null
		if (Func.isNotEmpty(stock)) {
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone zone = zoneService.getById(stock.getZoneId());
			if (Func.isEmpty(zone)) {
				throw new ServiceException("指定库区[ID:" + stock.getZoneId() + "]不存在!");
			}
			if (ZoneTypeEnum.SHIPPING_OUTSTOCK.getIndex().equals(zone.getZoneType())) {
				stockAdd.setLotNumber(stock.getLotNumber());
				stock = null;
			}
		}
		// 查询当前最新的库存信息(序列号在库存信息里不存在的情况下生成新的库存）
		if (ObjectUtil.isNotEmpty(stock)) {
			// 更新库存信息
			stock.setStockQty(stock.getStockQty().add(stockAdd.getStockQty()));
			UpdateWrapper<Stock> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				.set(Stock::getValidTime, stock.getValidTime())
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
			stock.setValidTime(stockAdd.getValidTime());
			// 插入库存信息
			super.save(stock);
		}
		// 处理库存明细
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
	 * 增加下架数量
	 *
	 * @param sku           物品信息
	 * @param stockSubtract 下架参数信息
	 * @return 库存扣减返回值集合 List<StockReduceRltDTO>
	 */
	@Override
	public synchronized List<StockSubtractVO> stockSubtract(Sku sku, StockSubtractDTO stockSubtract) {
		List<StockSubtractVO> rltList = new ArrayList<>();
		// 通过查询条件找到库存信息
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
				String.format("物品：%s 库存量不足（%s）", sku.getSkuName(), stockSubtract.toSimpleString()));
		}
		// 查询需要扣减的批次信息
		List<Lot> lotList = lotService.list(Condition.getQueryWrapper(new Lot()).lambda()
			.in(Lot::getLotNumber, NodesUtil.toList(stockList, Stock::getLotNumber)));
		for (Lot lot : lotList) {
			if (!StockStatusEnum.NORMAL.getIndex().equals(lot.getLotStatus())) {
				throw new ServiceException("批次号[" + lot.getLotNumber() + "]已被冻结，请解冻后再操作！");
			}
		}
		// 下架数量
		BigDecimal pickQty = stockSubtract.getPickQty();
		// 获取库存占用信息 / 拣货计划信息
		List<StockOccupy> stockOccupyListAll = new ArrayList<>();
		if (stockSubtract.getEventType().equals(EventTypeEnum.OutStock) ||
			stockSubtract.getEventType().equals(EventTypeEnum.Move)) {

			stockOccupyListAll = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
				.lambda()
				.eq(StockOccupy::getPickPlanId, stockSubtract.getDataId()));
		} else if (stockSubtract.getEventType().equals(EventTypeEnum.Count)) {
			stockOccupyListAll = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
				.lambda()
				.eq(StockOccupy::getWcrId, stockSubtract.getDataId()));
		}
		List<StockDetail> stockDetailListAll = new ArrayList<>();
		if (Func.isNotEmpty(stockList)) {
			stockDetailListAll = stockDetailService.list(Condition.getQueryWrapper(new StockDetail()).lambda()
				.in(StockDetail::getStockId, NodesUtil.toList(stockList, Stock::getStockId)));
		}
		for (Stock stock : stockList) {
			// 判断库存状态，如果非“正常”则忽略该库存
			if (!StockStatusEnum.NORMAL.getIndex().equals(stock.getStockStatus())) {
				continue;
			}
			BigDecimal qty = BigDecimal.ZERO;
			// 数据ID为空时，忽略库存占用
			if (ObjectUtil.isEmpty(stockSubtract.getDataId())) {
				// 最大能下架数量 = 库存量 - 下架数量 - 占用量 - 盘点占用
				qty = stock.getStockQty()
					.subtract(stock.getPickQty())
					.subtract(stock.getOccupyQty());
				if (BigDecimalUtil.le(qty, BigDecimal.ZERO)) {
					continue;
				}
				if (BigDecimalUtil.ge(pickQty, qty)) {    // 下架数量 大于 库存数量
					stock.setPickQty(stock.getPickQty().add(qty));
					pickQty = pickQty.subtract(qty);
				} else {                                  // 下架数量 小于 库存数量
					stock.setPickQty(stock.getPickQty().add(pickQty));
					pickQty = pickQty.subtract(pickQty);
				}
				// 封装返回值
				StockSubtractVO stockReduceRltDTO = new StockSubtractVO();
				stockReduceRltDTO.setStock(stock);
				rltList.add(stockReduceRltDTO);
			} else {
				// 获取所有库存占用（分配占用、盘点占用）
				List<StockOccupy> stockOccupyList = stockOccupyListAll.stream().filter(u -> {
					return stock.getStockId().equals(u.getStockId());
				}).collect(Collectors.toList());
				// 最大能下架数量 = 库存占用明细.分配数量
				if (ObjectUtil.isEmpty(stockOccupyList)) {
					continue;
				}
				for (StockOccupy stockOccupy : stockOccupyList) {
					qty = stockOccupy.getOccupyQty();
					if (BigDecimalUtil.ge(pickQty, qty)) {    // 下架数量 大于等于 占用数量
						if (stockSubtract.getEventType().equals(EventTypeEnum.OutStock) ||
							stockSubtract.getEventType().equals(EventTypeEnum.Move)) {
							// 更新下架数量、分配占用数量
							stock.setPickQty(stock.getPickQty().add(qty));
							stock.setOccupyQty(stock.getOccupyQty().subtract(qty));
						} else if (stockSubtract.getEventType().equals(EventTypeEnum.Count)) {
							// 更新库存数量、盘点占用数量
							stock.setStockQty(stock.getStockQty().subtract(qty));
						}
						pickQty = pickQty.subtract(qty);
						if (ObjectUtil.isNotEmpty(stockOccupy)) {
							stockOccupy.setOccupyQty(stockOccupy.getOccupyQty().subtract(qty));
							if (stockOccupy.getOccupyQty().compareTo(BigDecimal.ZERO) <= 0) {
								// 删除库存占用明细
								stockOccupyService.removeById(stockOccupy.getWsoId());
							} else {
								stockOccupyService.updateOccupyQty(stockOccupy);
							}
						}
					} else {                // 下架数量 小于 占用数量
						if (stockSubtract.getEventType().equals(EventTypeEnum.OutStock) ||
							stockSubtract.getEventType().equals(EventTypeEnum.Move)) {
							// 更新下架数量、分配占用数量
							stock.setPickQty(stock.getPickQty().add(pickQty));
							stock.setOccupyQty(stock.getOccupyQty().subtract(pickQty));
						} else if (stockSubtract.getEventType().equals(EventTypeEnum.Count)) {
							// 更新库存数量、盘点占用数量
							stock.setStockQty(stock.getStockQty().subtract(pickQty));
						}
						if (ObjectUtil.isNotEmpty(stockOccupy)) {
							// 更新库存占用明细
							stockOccupy.setOccupyQty(stockOccupy.getOccupyQty().subtract(pickQty));
							stockOccupyService.updateOccupyQty(stockOccupy);
						}
						pickQty = pickQty.subtract(pickQty);
					}
					StockSubtractVO stockReduceRltDTO = new StockSubtractVO();
					stockReduceRltDTO.setSoBillId(stockOccupy.getSoBillId());
					stockReduceRltDTO.setSoDetailId(stockOccupy.getSoDetailId());
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
					// 记录库存日志
					stockLogService.create(stock, stockDetail, StockProcTypeEnum.Update, stockSubtract.getSystemProcId());
				});
			// 更新库存信息
			if (BigDecimalUtil.eq(stock.getStockQty(), stock.getPickQty())) {
				// 删除库存
				super.removeById(stock.getStockId());
			} else {
				// 更新库存
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
				String.format("物品：%s 库存量不足（%s）", sku.getSkuName(), stockSubtract.toSimpleString()));
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
	 * 库存移动（按件）
	 *
	 * @param stockMove 接口参数 StockMoveDTO
	 * @return 移动后得库存信息
	 */
	@Override
	public List<Stock> stockMove(StockMoveDTO stockMove) {
		// 参数验证
		if (Func.isEmpty(stockMove.getSourceLocId())) {
			if (ObjectUtil.isEmpty(stockMove.getSourceLocId())) {
				throw new ServiceException("指定原库位ID不能为空！");
			}
		}
		if (Func.isEmpty(stockMove.getTargetStockId())) {
			if (ObjectUtil.isEmpty(stockMove.getTargetLocId())) {
				throw new ServiceException("指定目标库位ID不能为空！");
			}
		}
		if (ObjectUtil.isEmpty(stockMove.getSkuId())) {
			throw new ServiceException("指定物品ID不能为空！");
		}
		if (ObjectUtil.isEmpty(stockMove.getMoveQty())) {
			throw new ServiceException("指定移动数量不能为空！");
		}
		if (BigDecimalUtil.eq(stockMove.getMoveQty(), BigDecimal.ZERO)) {
			throw new ServiceException("指定移动数量必须大于 0 ！");
		}
		if (ObjectUtil.isEmpty(stockMove.getEventType())) {
			throw new ServiceException("事务类型不能为空！");
		}
		// 验证库位
		Location sourceLoc = null;
		if (Func.isEmpty(stockMove.getSourceStockId())) {
			sourceLoc = LocationCache.getById(stockMove.getSourceLocId());
		} else {
			Stock stock = super.getById(stockMove.getSourceStockId());
			if (Func.isEmpty(stock)) {
				throw new ServiceException("指定源库存不存在！");
			}
			sourceLoc = LocationCache.getById(stock.getLocId());
		}
		Location targetLoc = null;
		if (Func.isEmpty(stockMove.getTargetStockId())) {
			targetLoc = LocationCache.getById(stockMove.getTargetLocId());
		} else {
			Stock stock = super.getById(stockMove.getTargetStockId());
			if (Func.isEmpty(stock)) {
				throw new ServiceException("指定目标库存不存在！");
			}
			targetLoc = LocationCache.getById(stock.getLocId());
		}
		if (sourceLoc.getLocId().equals(targetLoc.getLocId())) {
			throw new ServiceException("原库位不能与目标库位一样！");
		}
		// 验证物品
		Sku sku = SkuCache.getById(stockMove.getSkuId());
		if (ObjectUtil.isEmpty(sku)) {
			throw new ServiceException(String.format("指定物品不存在（物品ID：%s）！", stockMove.getSkuId()));
		}
		// 封装一个库存扣减参数对象
		StockSubtractDTO stockReduce = StockWrapper.build().moveToReduce(stockMove);
		if (ObjectUtil.isEmpty(stockReduce)) {
			throw new ServiceException("类型转换失败（StockMoveDTO 转 StockReduceDTO）！");
		}
		stockReduce.setLocCode(sourceLoc.getLocCode());
		List<StockSubtractVO> stockReduceRltList = this.stockSubtract(sku, stockReduce);
		if (stockReduceRltList.size() == 0) {
			throw new ServiceException("库存信息不存在！");
		}
		List<Stock> resultStock = new ArrayList<>();
		// 循环扣减后得库存，添加到目标货位
		for (StockSubtractVO stockReduceRlt : stockReduceRltList) {
			if (ObjectUtil.isEmpty(stockReduceRlt.getStock())) {
				continue;
			}
			// 封装一个库存增加参数对象
			StockAddDTO stockProc = StockWrapper.build().entityProc(stockReduceRlt.getStock(), stockMove);
			if (Func.isNotEmpty(stockMove.getTargetStockId())) {
				List<Serial> serialList = serialService.listByStockId(stockMove.getTargetStockId());
				List<String> serialNumbers = NodesUtil.toList(serialList, Serial::getSerialNumber);
				stockProc.setSerialList(serialNumbers);
			}
			if (ObjectUtil.isEmpty(stockProc)) {
				throw new ServiceException("类型转换失败（Stock, StockMoveDTO 转 StockProcDTO）！");
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
				// 验证库存的锁定状态
				if (!StockStatusEnum.NORMAL.getIndex().equals(targetStockVO.getStockStatus())) {
					throw new ServiceException(
						String.format("指定的库存ID为 %s 状态（库存ID：%s）！",
							targetStockVO.getStockStatusDesc(), targetStockVO.getStockId()));
				}
			}
			// 增加库存
			resultStock.add(stockAdd(targetStockVO, stockProc));
		}

		return resultStock;
	}

	/**
	 * 按托移动库位
	 *
	 * @param stockMove
	 */
	@Override
	public void stockMoveByLpn(StockMoveDTO stockMove) {
		if (Func.isNotEmpty(stockMove.getSourceStockId()) || Func.isNotEmpty(stockMove.getTargetStockId())) {
			throw new ServiceException("按托移动接口目前不支持指定库存ID！");
		}
		if (ObjectUtil.isEmpty(stockMove.getTargetLocId())) {
			throw new ServiceException("指定目标库位ID不能为空！");
		}
		if (ObjectUtil.isEmpty(stockMove.getEventType())) {
			throw new ServiceException("事务类型不能为空！");
		}
		if (ObjectUtil.isEmpty(stockMove.getSystemProcId())) {
			throw new ServiceException("系统日志id不能为空！");
		}
		// 验证库位
		Location sourceLoc = LocationCache.getValid(stockMove.getSourceLocId());
		Location targetLoc = LocationCache.getValid(stockMove.getTargetLocId());
		if (sourceLoc.getLocId().equals(targetLoc.getLocId())) {
			throw new ServiceException("原库位不能与目标库位一样！");
		}
		// 封装一个库存扣减参数对象
		StockSubtractDTO stockReduce = StockWrapper.build().moveToReduce(stockMove);
		if (ObjectUtil.isEmpty(stockReduce)) {
			throw new ServiceException("类型转换失败（StockMoveDTO 转 StockReduceDTO）！");
		}
		stockReduce.setLocCode(sourceLoc.getLocCode());
		// 查询待移动的库存信息
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
		// 验证待移动的库存信息
		if (ObjectUtil.isEmpty(stockDetailList)) {
			throw new ServiceException(String.format("库位：%s 容器：%s 上没有库存信息，不允许执行此操作！",
				sourceLoc.getLocCode(), stockMove.getSourceLpnCode()));
		}
		List<Stock> stockList = super.list(Condition.getQueryWrapper(new Stock()).lambda()
			.in(Stock::getStockId, NodesUtil.toList(stockDetailList, StockDetail::getStockId)));
//		Stock occupyStock = stockList.stream().filter(u -> BigDecimalUtil.gt(u.getOccupyQty(), BigDecimal.ZERO))
//			.findFirst().orElse(null);
//		if (Func.isNotEmpty(occupyStock)) {
//			throw new ServiceException(String.format("库位：%s, 容器：%s  上的库存有占用数量，不允许执行此操作！",
//				occupyStock.getLocCode(), stockMove.getSourceLpnCode()));
//		}
		// 数据没问题了，循环移动库存
		for (StockDetail stockDetail : stockDetailList) {
			// 修改可移动数量 = 库存量 - 下架数量
			stockReduce.setPickQty(stockDetail.getStockQty().subtract(stockDetail.getPickQty()));
			stockReduce.setStockId(stockDetail.getStockId());
			stockReduce.setLpnCode(stockDetail.getLpnCode());
			stockReduce.setBoxCode(stockDetail.getBoxCode());
			// 获取该库存的序列号信息
			List<Serial> serialList = serialService.listByStockDetailId(stockDetail.getId());
			stockReduce.getSerialList().clear();
			if (ObjectUtil.isNotEmpty(serialList)) {
				serialList.forEach(serial -> {
					stockReduce.getSerialList().add(serial.getSerialNumber());
				});
			}
			// 获取当前库存上的物品信息
			Sku sku = SkuCache.getById(stockDetail.getSkuId());
			List<StockSubtractVO> stockReduceRltList = this.stockSubtract(sku, stockReduce);
			if (stockReduceRltList.size() == 0) {
				throw new ServiceException("库存信息不存在！");
			}
			List<Stock> resultStock = new ArrayList<>();
			// 循环扣减后得库存，添加到目标货位
			for (StockSubtractVO stockReduceRlt : stockReduceRltList) {
				if (ObjectUtil.isEmpty(stockReduceRlt.getStock())) {
					continue;
				}
				// 封装一个库存增加参数对象
				StockAddDTO stockProc = StockWrapper.build().entityProc(stockReduceRlt.getStock(), stockMove);
				stockProc.setStockId(stockReduce.getStockId());
				stockProc.setStockQty(stockReduce.getPickQty());
				stockProc.setSerialList(stockReduce.getSerialList());
				stockProc.setLpnCode(stockReduce.getLpnCode());
				stockProc.setBoxCode(stockReduce.getBoxCode());
				stockProc.setBillId(stockReduceRlt.getSoBillId());
				stockProc.setBillDetailId(stockReduceRlt.getSoDetailId());
				if (ObjectUtil.isEmpty(stockProc)) {
					throw new ServiceException("类型转换失败（Stock, StockMoveDTO 转 StockProcDTO）！");
				}
				Stock targetStock = super.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getSkuId, stockProc.getSkuId())
					.eq(Stock::getLocId, stockProc.getLocId())
					.eq(Stock::getWspId, stockProc.getWspId())
					.eq(Stock::getLotNumber, stockProc.getLotNumber())).stream().findFirst().orElse(null);
				StockVO targetStockVO = StockWrapper.build().entityVO(targetStock);
				if (ObjectUtil.isNotEmpty(targetStockVO)) {
					// 验证库存的锁定状态
					if (!StockStatusEnum.NORMAL.getIndex().equals(targetStockVO.getStockStatus())) {
						throw new ServiceException(
							String.format("指定的库存ID为 %s 状态（库存ID：%s）！",
								targetStockVO.getStockStatusDesc(), targetStockVO.getStockId()));
					}
				}
				// 增加库存
				Stock stockAdd = stockAdd(targetStockVO, stockProc);
				resultStock.add(stockAdd);
			}
		}
	}

	@Override
	public List<ErpStockVO> listErpCompare() {
		// 比对结果
		List<ErpStockVO> list = new ArrayList<>();
		// 查询当前库存汇总
		List<Stock> stockList = this.list();
		List<Stock> stockListBackup = new ArrayList<>();
		// 获取ERP库存
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
				// 计算差异值
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
			// 计算差异值
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
