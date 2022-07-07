
package org.nodes.wms.core.count.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuLog;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.ISkuLogService;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.stock.core.dto.StockDTO;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.ILotService;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.count.dto.CountRecordDTO;
import org.nodes.wms.core.count.dto.RandomCheckSubmitDTO;
import org.nodes.wms.core.count.dto.RandomCountDTO;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.entity.CountReport;
import org.nodes.wms.core.count.enums.CountByEnum;
import org.nodes.wms.core.count.enums.CountRecordStateEnum;
import org.nodes.wms.core.count.enums.StockCountStateEnum;
import org.nodes.wms.core.count.mapper.CountHeaderMapper;
import org.nodes.wms.core.count.mapper.CountRecordMapper;
import org.nodes.wms.core.count.service.ICountDetailService;
import org.nodes.wms.core.count.service.ICountRecordService;
import org.nodes.wms.core.count.service.ICountReportService;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.nodes.wms.core.count.vo.RandomCountRltVO;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.nodes.wms.core.warehouse.cache.LocationCache.LOCATION_CACHE;

/**
 * 盘点单记录表 服务实现类
 *
 * @author NodeX
 * @since 2020-02-20
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class CountRecordServiceImpl<M extends CountRecordMapper, T extends CountRecord>
	extends BaseServiceImpl<CountRecordMapper, CountRecord>
	implements ICountRecordService {

	@Autowired
	ILocationService locationService;
	@Autowired
	ILotService lotService;
	@Autowired
	IStockService stockService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ITaskService taskService;
	@Autowired
	CountHeaderMapper countHeaderMapper;
	@Autowired
	ICountDetailService countDetailService;
	@Autowired
	ISerialService serialService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	ICountReportService countReportService;
	@Autowired
	ISkuLogService skuLogService;

	@Override
	public RandomCountRltVO randomCountSubmit(RandomCountDTO randomCountDTO) {
		RandomCountRltVO randomCountRlt = new RandomCountRltVO();

		User user = UserCache.getById(AuthUtil.getUserId());
		if (ObjectUtil.isEmpty(user)) {
			throw new ServiceException("用户不存在（ID：" + AuthUtil.getUserId() + "）！");
		}
		if (ObjectUtil.isEmpty(randomCountDTO.getLocCode())) {
			throw new ServiceException("库位编码不能为空！");
		}
		Location location = LocationCache.getValid(randomCountDTO.getLocCode());

		randomCountDTO.setWhId(location.getWhId());
		if (ObjectUtil.isEmpty(randomCountDTO.getSkuId())) {
			throw new ServiceException("物品ID不能为空！");
		}
		Sku sku = SkuCache.getById(randomCountDTO.getSkuId());
		if (ObjectUtil.isEmpty(sku)) {
			throw new ServiceException("物品不存在（ID：" + randomCountDTO.getSkuId() + "）！");
		}
		if (Func.isEmpty(randomCountDTO.getCountQty()) || randomCountDTO.getCountQty().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServiceException("盘点数量必须大于或等于 0！");
		}
		if (ObjectUtil.isEmpty(randomCountDTO.getWspdId())) {
			throw new ServiceException("包装明细ID不能为空！");
		}
		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(randomCountDTO.getWspdId());
		if (ObjectUtil.isEmpty(skuPackageDetail)) {
			throw new ServiceException("包装明细不存在（ID：" + randomCountDTO.getWspdId() + "）！");
		}
		Task task = null;
		CountHeader countHeader = null;
		if (Func.isNotEmpty(randomCountDTO.getTaskId())) {
			// 获取任务信息
			task = taskService.getById(randomCountDTO.getTaskId());
			if (Func.isEmpty(task)) {
				throw new ServiceException("指定任务不存在(任务ID：" + randomCountDTO.getTaskId() + ")！");
			}
			// 获取盘点单
			countHeader = countHeaderMapper.selectById(task.getBillId());
			if (Func.isEmpty(countHeader)) {
				throw new ServiceException("指定盘点单不存在(" + task.getBillId() + ")！");
			}
			// 获取任务明细
			List<CountDetail> detailList = countDetailService.list(
				new QueryWrapper<CountDetail>()
					.lambda()
					.eq(CountDetail::getTaskGroup, task.getTaskGroup())
					.eq(CountDetail::getLocId, location.getLocId()));
			if (Func.isNotEmpty(detailList)) {
				throw new ServiceException("盘点库位不在当前任务中！");
			}
		}
		// 验证物品是否为序列号管理
		if (sku.getIsSn() == 1) {
			if (ObjectUtil.isEmpty(randomCountDTO.getSerialNumberList())) {
				throw new ServiceException("物品：" + sku.getSkuName() + " 为序列号管理物品，序列号不能为空！");
			}
			// 序列号不为空的情况下，验证序列号的数量与盘点数量是否一致
			if (Func.isNotEmpty(randomCountDTO.getSerialNumberList())) {
				if (randomCountDTO.getSerialNumberList().size() != randomCountDTO.getCountQty().intValueExact()) {
					throw new ServiceException("提供的序列号数量与盘点数量不匹配！");
				}
				// 修改盘点数量为1
				randomCountDTO.setCountQty(BigDecimal.ONE);
			}
			for (String serialNumber : randomCountDTO.getSerialNumberList()) {
				// 查询序列号表，并找到对应的库存信息
				Serial serial = serialService.getOne(Wrappers.lambdaQuery(Serial.class).eq(Serial::getSerialNumber, serialNumber));
				Stock stock = null;
				if (Func.isNotEmpty(serial)) {
					stock = stockService.getById(serial.getStockId());
				}
				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location, serialNumber,
					StockWrapper.build().entityDTO(stock));
			}
		} else {
			// 获取库存信息
			QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
			stockQueryWrapper.eq("wh_id", randomCountDTO.getWhId());
			stockQueryWrapper.eq("loc_id", location.getLocId());
			stockQueryWrapper.eq("sku_id", randomCountDTO.getSkuId());
			stockQueryWrapper.eq("wsp_id", sku.getWspId());
			stockQueryWrapper.eq("sku_level", skuPackageDetail.getSkuLevel());

			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (!randomCountDTO.skuLotChk(i)) {
					continue;
				}
				stockQueryWrapper.eq("sku_lot" + i, randomCountDTO.skuLotGet(i));
			}
			List<Stock> stockList = stockService.list(stockQueryWrapper);
			// 去除托盘后，查询到的库存可能批次号会重复，容器、箱号可能不一样
			// 1. 创建储存处理后最终库存信息的对象
			List<Stock> resultStockList = new ArrayList<>();
			// 2. 对查询到的库存按批次号分组，对批次号重复的库存取数量差最接近的
			stockList.stream().collect(Collectors.groupingBy(Stock::getLotNumber)).forEach(
				(lot, list) -> {
					// 库存只有一条记录，直接保存
					if (list.size() == 1) {
						resultStockList.addAll(list);
					} else {
						// 存在多条记录的情况下，取数量差最接近的库存(目的是为了减小差异)
						Map<Long, BigDecimal> stockDiffMap = new HashMap<>();
						for (Stock stock : list) {
							BigDecimal diffQty = randomCountDTO.getCountQty().subtract(stock.getStockQty()
								.subtract(stock.getPickQty()));
							stockDiffMap.put(stock.getStockId(), diffQty.abs());
						}
						Long findStockId = null;
						BigDecimal diffQty = BigDecimal.ZERO;
						// 按顺序，取在记录中不存在的，如果没有则替换第一条
						for (Long stockId : stockDiffMap.keySet()) {
							CountRecord countRecord = super.list(Condition.getQueryWrapper(new CountRecord()).lambda()
								.eq(CountRecord::getWhId, location.getWhId())
								.eq(CountRecord::getCountBillId, -1L)
								.eq(CountRecord::getStockId, stockId)
								.eq(CountRecord::getRecordState, CountRecordStateEnum.Create.getIndex()))
								.stream().findFirst().orElse(null);
							if (Func.isNotEmpty(countRecord)) {
								// 该库存ID已经盘点了，则跳过
								continue;
							}
							// 从库存里找到与盘点数量差距最小的
							BigDecimal stockQty = stockDiffMap.getOrDefault(stockId, BigDecimal.ZERO);
							if (BigDecimalUtil.eq(diffQty, BigDecimal.ZERO) || BigDecimalUtil.lt(stockQty, diffQty)) {
								diffQty = stockQty;
								findStockId = stockId;
							}
						}
						if (Func.isEmpty(findStockId)) {
							resultStockList.add(list.get(0));
						} else {
							final Long finalStockId = findStockId;
							resultStockList.addAll(list.stream().filter(stock -> {
								return stock.getStockId().equals(finalStockId);
							}).collect(Collectors.toList()));
						}
					}
				}
			);
			randomCountRlt.setStockList(resultStockList);
			// 当库存只存在一条记录时，记录盘点
			if (ObjectUtil.isEmpty(randomCountRlt.getStockList()) || randomCountRlt.getStockList().size() == 1) {

				StockDTO stock = ObjectUtil.isEmpty(randomCountRlt.getStockList()) ?
					null : StockWrapper.build().entityDTO(randomCountRlt.getStockList().get(0));

				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location, null, stock);
			}
		}
		// 更新上次盘点时间
		location.setLastLocCountDate(LocalDateTime.now());
		CacheUtil.clear(LOCATION_CACHE);
		locationService.updateById(location);

		return randomCountRlt;
	}

	@Override
	public boolean randomCheckTaskSubmit(RandomCheckSubmitDTO randomCheckSubmitDTO) {
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		User user = UserCache.getById(AuthUtil.getUserId());
		if (ObjectUtil.isEmpty(user)) {
			throw new ServiceException("用户不存在（ID：" + AuthUtil.getUserId() + "）！");
		}
		Location location = LocationCache.getValid(randomCheckSubmitDTO.getLocCode(), randomCheckSubmitDTO.getCountNo());
		if (Func.isEmpty(randomCheckSubmitDTO.getItems())) {
			throw new ServiceException("盘点明细不能为空！");
		}
		// 获取系统设置::启用容器=
		Task task = null;
		CountHeader countHeader = null;
		List<CountDetail> countDetailList = null;

		if (Func.isNotEmpty(randomCheckSubmitDTO.getTaskId())) {
			// 获取任务信息
			task = taskService.getById(Long.valueOf(randomCheckSubmitDTO.getTaskId()));
			if (Func.isEmpty(task)) {
				throw new ServiceException("指定任务不存在(任务ID：" + randomCheckSubmitDTO.getTaskId() + ")！");
			}
			// 获取盘点单
			countHeader = countHeaderMapper.selectById(task.getBillId());
			// 获取任务明细
			countDetailList = countDetailService.list(new QueryWrapper<CountDetail>()
				.lambda()
				.eq(CountDetail::getTaskGroup, task.getTaskGroup())
				.eq(CountDetail::getLocId, location.getLocId())
			);
		} else if (Func.isNotEmpty(randomCheckSubmitDTO.getCountNo())) {
			countHeader = countHeaderMapper.selectOne(Condition.getQueryWrapper(new CountHeader())
				.lambda()
				.eq(CountHeader::getCountBillNo, randomCheckSubmitDTO.getCountNo()));
			// 获取任务明细
			countDetailList = countDetailService.list(new QueryWrapper<CountDetail>()
				.lambda()
				.eq(CountDetail::getCountBillNo, randomCheckSubmitDTO.getCountNo())
				.eq(CountDetail::getLocId, location.getLocId())
			);
		}
		if (Func.isEmpty(countDetailList)) {
			throw new ServiceException("盘点库位或物品不在当前任务中！");
		}
		// 更新盘点明细的处理时间
		List<Long> countDetailIdList = NodesUtil.toList(countDetailList, CountDetail::getCountDetailId);
		UpdateWrapper<CountDetail> countDetailUpdateWrapper = new UpdateWrapper<>();
		countDetailUpdateWrapper.lambda()
			.set(CountDetail::getProcTime, LocalDateTime.now())
			.eq(CountDetail::getCountDetailId, countDetailIdList)
			.isNull(CountDetail::getProcTime);
		if (!countHeader.getCountBillState().equals(StockCountStateEnum.COUNTING.getIndex())) {
			throw new ServiceException(String.format(
				"盘点单：%s 状态为 ",
				countHeader.getCountBillNo(),
				DictCache.getValue(DictCodeConstant.STOCK_COUNT_STATE, countHeader.getCountBillState())));
		}
		countDetailService.update(countDetailUpdateWrapper);
		// 获取库存信息
		CountHeader finalCountHeader = countHeader;
		List<CountDetail> finalCountDetailList = countDetailList;
		QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
		LambdaQueryWrapper<Stock> lambda = queryWrapper.lambda();
		lambda.eq(Stock::getLocId, location.getLocId())
			.func(sql -> {
				if (CountByEnum.SKU.getIndex().equals(finalCountHeader.getCountBy())) {
					sql.in(Stock::getSkuId, NodesUtil.toList(finalCountDetailList, CountDetail::getSkuId));
				}
			});

		List<Stock> stockList = stockService.list(queryWrapper);
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		for (RandomCheckSubmitDTO.RandomCheckItem randomCheckItem : randomCheckSubmitDTO.getItems()) {
			Sku sku = skuService.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getSkuCode, randomCheckItem.getSkuCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(sku)) {
				throw new ServiceException(String.format("物品编码[%s]不存在！", randomCheckItem.getSkuCode()));
			}

			SkuPackageDetail skuPackageDetail = skuPackageDetailService.getOne(
				Condition.getQueryWrapper(new SkuPackageDetail())
					.lambda()
					.eq(SkuPackageDetail::getWspId, sku.getWspId())
					.eq(SkuPackageDetail::getWsuName, randomCheckItem.getUmName())
					.last("limit 1"));
			if (Func.isEmpty(skuPackageDetail)) {
				throw new ServiceException(String.format("物品编码[%s]不存在基础计量单位！", randomCheckItem.getSkuCode()));
			}
			RandomCountDTO randomCountDTO = new RandomCountDTO();
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (randomCheckItem.skuLotChk(i)) {
					randomCountDTO.skuLotSet(i, randomCheckItem.skuLotGet(i));
				}
			}
			randomCountDTO.setWhId(location.getWhId());
			randomCountDTO.setLpnCode(randomCheckSubmitDTO.getLpnCode());
			randomCountDTO.setLocCode(location.getLocCode());
			randomCountDTO.setSkuId(sku.getSkuId());
			Stock stock1 = stockList.stream().filter(stock -> {
				List<Boolean> flags = new ArrayList<>();
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (!randomCheckItem.skuLotChk(i)) {
						randomCheckItem.skuLotSet(i, "");
					}
					if (!stock.skuLotChk(i) || " ".equals(stock.skuLotGet(i))) {
						stock.skuLotSet(i, "");
					}
					if (stock.skuLotGet(i).equals(randomCheckItem.skuLotGet(i))) {
						flags.add(true);
					}
				}
//				if (lpnEnable == 0) {
//					return flags.size() == ParamCache.LOT_COUNT&&stock.getLpnCode().equals(randomCheckItem.getLpnCode());
//				}else{
//					return flags.size() == ParamCache.LOT_COUNT;
//				}
				return true;
			}).findFirst().orElse(null);
			if (Func.isNotEmpty(stock1)) {
				randomCountDTO.setLotNumber(stock1.getLotNumber());
				randomCountDTO.setCountQty(randomCheckItem.getQty());
				randomCountDTO.setWspdId(skuPackageDetail.getWspdId());
//				randomCountDTO.setLpnCode(stock1.getLpnCode());
				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location,
					null, StockWrapper.build().entityDTO(stock1));
			} else {
				randomCountDTO.setCountQty(randomCheckItem.getQty());
				randomCountDTO.setWspdId(skuPackageDetail.getWspdId());
				randomCountDTO.setLpnCode(randomCheckItem.getLpnCode());
				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location,
					null, null);
			}
		}
		//更改任务开始时间
		taskService.updateBeginTime(randomCheckSubmitDTO.getCountNo(), TaskTypeEnum.Count);
		// 更新上次盘点时间
		location.setLastLocCountDate(LocalDateTime.now());
		CacheUtil.clear(LOCATION_CACHE);
		return locationService.updateById(location);
	}

	@Override
	public boolean randomCheckSubmit(RandomCheckSubmitDTO randomCheckSubmitDTO) {
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		User user = UserCache.getById(AuthUtil.getUserId());
		if (ObjectUtil.isEmpty(user)) {
			throw new ServiceException("用户不存在（ID：" + AuthUtil.getUserId() + "）！");
		}
		Location location = LocationCache.getValid(randomCheckSubmitDTO.getLocCode());
		if (Func.isEmpty(location)) {
			throw new ServiceException(String.format("库位[%s]不存在！", randomCheckSubmitDTO.getLocCode()));
		}
		List<Stock> occupyStockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
			.in(Stock::getLocId, location.getLocId())
			.and(sql -> {
				sql.gt(Stock::getOccupyQty, BigDecimal.ZERO);
			})
		);
		if (Func.isNotEmpty(occupyStockList)) {
			throw new ServiceException(String.format("库位[%s]存在库存占用，不允许创建盘点盘！", randomCheckSubmitDTO.getLocCode()));
		}
		if (Func.isEmpty(randomCheckSubmitDTO.getItems())) {
			throw new ServiceException("盘点明细不能为空！");
		}
		CountHeader countHeader = null;
		// 获取库存信息
		List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
			.eq(Stock::getLocId, location.getLocId()));
		for (RandomCheckSubmitDTO.RandomCheckItem randomCheckItem : randomCheckSubmitDTO.getItems()) {
			ISkuService skuService = SpringUtil.getBean(ISkuService.class);
			//Sku sku = SkuCache.listByCode(randomCheckItem.getSkuCode()).stream().findFirst().orElse(null);
			Sku sku = skuService.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getSkuCode, randomCheckItem.getSkuCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(sku)) {
				throw new ServiceException(String.format("物品编码[%s]不存在！", randomCheckItem.getSkuCode()));
			}
			SkuPackageDetail skuPackageDetail = skuPackageDetailService.getOne(
				Condition.getQueryWrapper(new SkuPackageDetail())
					.lambda()
					.eq(SkuPackageDetail::getWspId, sku.getWspId())
					.eq(SkuPackageDetail::getWsuName, randomCheckItem.getUmName())
					.last("limit 1"));
			if (Func.isEmpty(skuPackageDetail)) {
				throw new ServiceException(String.format("物品编码[%s]不存在基础计量单位！", randomCheckItem.getSkuCode()));
			}
			Stock stock1 = stockList.stream().filter(stock -> {
				List<Boolean> flags = new ArrayList<>();
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (!randomCheckItem.skuLotChk(i)) {
						randomCheckItem.skuLotSet(i, "");
					}
					if (!stock.skuLotChk(i) || " ".equals(stock.skuLotGet(i))) {
						stock.skuLotSet(i, "");
					}
					if (stock.skuLotGet(i).equals(randomCheckItem.skuLotGet(i))) {
						flags.add(true);
					}
				}
//				if (lpnEnable == 0) {
//					return flags.size() == ParamCache.LOT_COUNT&&stock.getLpnCode().equals(randomCheckItem.getLpnCode());
//				}else{
//					return flags.size() == ParamCache.LOT_COUNT;
//				}
				return true;
			}).findFirst().orElse(null);
			if (Func.isNotEmpty(stock1) && BigDecimalUtil.gt(stock1.getOccupyQty(), BigDecimal.ZERO)) {
				throw new ServiceException(String.format("物品[%s]存在库存占用，不允许创建盘点盘！", randomCheckItem.getSkuCode()));
			}
			RandomCountDTO randomCountDTO = new RandomCountDTO();
			randomCountDTO.setWhId(location.getWhId());
			randomCountDTO.setLpnCode(randomCheckSubmitDTO.getLpnCode());
			randomCountDTO.setLocCode(location.getLocCode());
			randomCountDTO.setSkuId(sku.getSkuId());
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (randomCheckItem.skuLotChk(i)) {
					randomCountDTO.skuLotSet(i, randomCheckItem.skuLotGet(i));
				}
			}
			if (Func.isNotEmpty(stock1)) {
				randomCountDTO.setLotNumber(stock1.getLotNumber());
				randomCountDTO.setCountQty(randomCheckItem.getQty());
				randomCountDTO.setWspdId(skuPackageDetail.getWspdId());
//				randomCountDTO.setLpnCode(stock1.getLpnCode());
				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location,
					null, StockWrapper.build().entityDTO(stock1));
			} else {
				randomCountDTO.setCountQty(randomCheckItem.getQty());
				randomCountDTO.setWspdId(skuPackageDetail.getWspdId());
				randomCountDTO.setLpnCode(randomCheckItem.getLpnCode());
				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location,
					null, null);
			}
		}
		// 更新上次盘点时间
		location.setLastLocCountDate(LocalDateTime.now());
		CacheUtil.clear(LOCATION_CACHE);
		return locationService.updateById(location);
	}

	@Override
	public RandomCountRltVO billCountSubmit(@Validated RandomCountDTO randomCountDTO) {
		RandomCountRltVO randomCountRlt = new RandomCountRltVO();

		User user = UserCache.getById(AuthUtil.getUserId());
		if (ObjectUtil.isEmpty(user)) {
			throw new ServiceException("用户不存在（ID：" + AuthUtil.getUserId() + "）！");
		}

		Location location = LocationCache.getValid(randomCountDTO.getLocCode(), randomCountDTO.getCountBillNo());

		randomCountDTO.setWhId(location.getWhId());
		if (ObjectUtil.isEmpty(randomCountDTO.getSkuId())) {
			throw new ServiceException("");
		}
		Sku sku = SkuCache.getById(randomCountDTO.getSkuId());
		if (ObjectUtil.isEmpty(sku)) {
			throw new ServiceException("物品不存在（ID：" + randomCountDTO.getSkuId() + "）！");
		}
		if (BigDecimalUtil.le(randomCountDTO.getCountQty(), BigDecimal.ZERO)) {
			throw new ServiceException("盘点数量必须大于或等于 0！");
		}
		if (ObjectUtil.isEmpty(randomCountDTO.getWspdId())) {
			throw new ServiceException("包装明细ID不能为空！");
		}
		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(randomCountDTO.getWspdId());
		if (ObjectUtil.isEmpty(skuPackageDetail)) {
			throw new ServiceException("包装明细不存在（ID：" + randomCountDTO.getWspdId() + "）！");
		}
		Task task = null;
		CountHeader countHeader = null;
		List<CountDetail> countDetailList = null;
		if (Func.isNotEmpty(randomCountDTO.getTaskId())) {
			// 获取任务信息
			task = taskService.getById(randomCountDTO.getTaskId());
			if (Func.isEmpty(task)) {
				throw new ServiceException("指定任务不存在(任务ID：" + randomCountDTO.getTaskId() + ")！");
			}
			// 获取盘点单
			countHeader = countHeaderMapper.selectById(task.getBillId());
			// 获取任务明细
			countDetailList = countDetailService.list(new QueryWrapper<CountDetail>()
				.lambda()
				.eq(CountDetail::getTaskGroup, task.getTaskGroup())
				.eq(CountDetail::getLocId, location.getLocId())
				.and(and -> {
					and.or().isNull(CountDetail::getSkuId).or().eq(CountDetail::getSkuId, sku.getSkuId());
				})
			);
		} else if (Func.isNotEmpty(randomCountDTO.getCountBillNo())) {
			countHeader = countHeaderMapper.selectOne(Condition.getQueryWrapper(new CountHeader())
				.lambda()
				.eq(CountHeader::getCountBillNo, randomCountDTO.getCountBillNo()));
			// 获取任务明细
			countDetailList = countDetailService.list(new QueryWrapper<CountDetail>()
				.lambda()
				.eq(CountDetail::getCountBillNo, randomCountDTO.getCountBillNo())
				.eq(CountDetail::getLocId, location.getLocId())
				.and(and -> {
					and.or().isNull(CountDetail::getSkuId).or().eq(CountDetail::getSkuId, sku.getSkuId());
				})
			);
		}
		if (Func.isEmpty(countDetailList)) {
			throw new ServiceException("盘点库位或物品不在当前任务中！");
		}
		// 更新盘点明细的处理时间
		List<Long> countDetailIdList = NodesUtil.toList(countDetailList, CountDetail::getCountDetailId);
		UpdateWrapper<CountDetail> countDetailUpdateWrapper = new UpdateWrapper<>();
		countDetailUpdateWrapper.lambda()
			.set(CountDetail::getProcTime, LocalDateTime.now())
			.eq(CountDetail::getCountDetailId, countDetailIdList)
			.isNull(CountDetail::getProcTime);
		countDetailService.update(countDetailUpdateWrapper);
		if (Func.isEmpty(countHeader)) {
			throw new ServiceException("指定盘点单不存在(" + task.getBillId() + ")！");
		}
		if (!countHeader.getCountBillState().equals(StockCountStateEnum.COUNTING.getIndex())) {
			throw new ServiceException(String.format(
				"盘点单：%s 状态为 %s",
				countHeader.getCountBillNo(),
				DictCache.getValue(DictCodeConstant.STOCK_COUNT_STATE, countHeader.getCountBillState())));
		}
		// 验证物品是否为序列号管理
		if (sku.getIsSn() == 1) {
			if (ObjectUtil.isEmpty(randomCountDTO.getSerialNumberList())) {
				throw new ServiceException("物品：" + sku.getSkuName() + " 为序列号管理物品，序列号不能为空！");
			}
			// 序列号不为空的情况下，验证序列号的数量与盘点数量是否一致
			if (Func.isNotEmpty(randomCountDTO.getSerialNumberList())) {
				if (randomCountDTO.getSerialNumberList().size() != randomCountDTO.getCountQty().intValueExact()) {
					throw new ServiceException("提供的序列号数量与盘点数量不匹配！");
				}
				// 修改盘点数量为1
				randomCountDTO.setCountQty(BigDecimal.ONE);
			}
			for (String serialNumber : randomCountDTO.getSerialNumberList()) {
				// 查询序列号表，并找到对应的库存信息
				Serial serial = serialService.getOne(Wrappers.lambdaQuery(Serial.class).eq(Serial::getSerialNumber, serialNumber));
				Stock stock = null;
				if (Func.isNotEmpty(serial)) {
					stock = stockService.getById(serial.getStockId());
				}
				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location,
					serialNumber, StockWrapper.build().entityDTO(stock));
			}
		} else {
			// 获取库存信息
			QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
			stockQueryWrapper.eq("wh_id", randomCountDTO.getWhId());
			stockQueryWrapper.eq("loc_id", location.getLocId());
			stockQueryWrapper.eq("sku_id", randomCountDTO.getSkuId());
			stockQueryWrapper.eq("wsp_id", sku.getWspId());
			stockQueryWrapper.eq("sku_level", skuPackageDetail.getSkuLevel());
			if (Func.isNotEmpty(randomCountDTO.getLotNumber())) {
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (!randomCountDTO.skuLotChk(i)) {
						continue;
					}
					stockQueryWrapper.eq("sku_lot" + i, randomCountDTO.skuLotGet(i));
				}
			}

			List<Stock> stockList = stockService.list(stockQueryWrapper);
			// 去除托盘后，查询到的库存可能批次号会重复，容器、箱号可能不一样
			// 1. 创建储存处理后最终库存信息的对象
			List<Stock> resultStockList = new ArrayList<>();
			// 2. 对查询到的库存按批次号分组，对批次号重复的库存取数量差最接近的
			stockList.stream().collect(Collectors.groupingBy(Stock::getLotNumber)).forEach(
				(lot, list) -> {
					// 库存只有一条记录，直接保存
					if (list.size() == 1) {
						resultStockList.addAll(list);
					} else {
						// 存在多条记录的情况下，取数量差最接近的库存(目的是为了减小差异)
						Map<Long, BigDecimal> stockDiffMap = new HashMap<>();
						for (Stock stock : list) {
							BigDecimal diffQty = randomCountDTO.getCountQty().subtract(stock.getStockQty()
								.subtract(stock.getPickQty()));
							stockDiffMap.put(stock.getStockId(), diffQty.abs());
						}
						stockDiffMap.values().stream().sorted();
						Long findStockId = null;
						BigDecimal diffQty = BigDecimal.ZERO;
						// 按顺序，取在记录中不存在的，如果没有则替换第一条
						for (Long stockId : stockDiffMap.keySet()) {
							CountRecord countRecord = super.list(Condition.getQueryWrapper(new CountRecord()).lambda()
								.eq(CountRecord::getWhId, location.getWhId())
								.eq(CountRecord::getCountBillId, -1L)
								.eq(CountRecord::getStockId, stockId)
								.eq(CountRecord::getRecordState, CountRecordStateEnum.Create.getIndex()))
								.stream().findFirst().orElse(null);
							if (Func.isNotEmpty(countRecord)) {
								// 该库存ID已经盘点了，则跳过
								continue;
							}
							// 从库存里找到与盘点数量差距最小的
							BigDecimal stockQty = stockDiffMap.getOrDefault(stockId, BigDecimal.ZERO);
							if (BigDecimalUtil.eq(diffQty, BigDecimal.ZERO) || BigDecimalUtil.lt(stockQty, diffQty)) {
								diffQty = stockQty;
								findStockId = stockId;
							}
						}
						if (Func.isEmpty(findStockId)) {
							resultStockList.add(list.get(0));
						} else {
							final Long finalStockId = findStockId;
							resultStockList.addAll(list.stream().filter(stock -> {
								return stock.getStockId().equals(finalStockId);
							}).collect(Collectors.toList()));
						}
					}
				}
			);
			randomCountRlt.setStockList(resultStockList);
			// 当库存只存在一条记录时，记录盘点
			if (ObjectUtil.isEmpty(randomCountRlt.getStockList()) || randomCountRlt.getStockList().size() == 1) {

				StockDTO stock = ObjectUtil.isEmpty(randomCountRlt.getStockList()) ?
					null : StockWrapper.build().entityDTO(randomCountRlt.getStockList().get(0));


				this.save(randomCountDTO, countHeader, sku, skuPackageDetail, location, null, stock);
			}
		}
		//更改任务开始时间
		taskService.updateBeginTime(randomCountDTO.getCountBillNo(), TaskTypeEnum.Count);
		// 更新上次盘点时间
		location.setLastLocCountDate(LocalDateTime.now());
		locationService.updateById(location);

		return randomCountRlt;
	}

	private boolean save(RandomCountDTO randomCountDTO, CountHeader countHeader, Sku sku,
						 SkuPackageDetail skuPackageDetail, Location location,
						 String serialNumber, StockDTO stock) {
		// 更新物品盘点时间
		SkuLog skuLog = skuLogService.getOne(Condition.getQueryWrapper(new SkuLog()).lambda()
			.eq(SkuLog::getSkuId, sku.getSkuId())
			.last("limit 1"));
		if (Func.isEmpty(skuLog)) {
			skuLog = BeanUtil.copy(sku, SkuLog.class);
			skuLog.setOutCount(0);
		}
		skuLog.setLastCountTime(LocalDateTime.now());
		skuLogService.saveOrUpdate(skuLog);
		// 查询记录表中是否存在该记录
		QueryWrapper<CountRecord> recordQueryWrapper = new QueryWrapper<>();
		recordQueryWrapper.eq("wh_id", randomCountDTO.getWhId());
		recordQueryWrapper.eq("count_bill_id", -1);

		if (StringUtil.isNotBlank(serialNumber)) {
			recordQueryWrapper.eq("serial_number", serialNumber);
		}
		if (Func.isNotEmpty(stock)) {
			recordQueryWrapper.eq("stock_id", stock.getStockId());
		}
		if (Func.isNotEmpty(sku)) {
			recordQueryWrapper.lambda().eq(CountRecord::getSkuId, sku.getSkuId());
		}
		if (Func.isNotEmpty(location)) {
			recordQueryWrapper.lambda().eq(CountRecord::getLocId, location.getLocId());
		}
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (randomCountDTO.skuLotChk(i)) {
				recordQueryWrapper.eq("sku_lot" + i, randomCountDTO.skuLotGet(i));
			}
		}
		// 操作数据库中已存在的盘点记录
		List<CountRecord> records = super.list(recordQueryWrapper);
		if (ObjectUtil.isNotEmpty(records)) {
			records.stream().forEach(record -> {
				// 记录表已存在：修改状态 = 已撤销
				record.setRecordState(CountRecordStateEnum.Repeal.getIndex());
				super.updateById(record);
			});
		}
		// 存储盘点记录
		CountRecordDTO countRecord = new CountRecordDTO();
		countRecord.setWhId(randomCountDTO.getWhId());
		if (Func.isNotEmpty(countHeader)) {
			countRecord.setCountBillId(countHeader.getCountBillId());
			countRecord.setCountBillNo(countHeader.getCountBillNo());
		} else {
			countRecord.setCountBillId(-1L);
			countRecord.setCountBillNo("RANDOM");
		}
		countRecord.setLocId(location.getLocId());
		countRecord.setLocCode(location.getLocCode());
		countRecord.setLpnCode(randomCountDTO.getLpnCode());
		countRecord.setSkuId(sku.getSkuId());
		countRecord.setSkuCode(sku.getSkuCode());
		countRecord.setSkuName(sku.getSkuName());
		countRecord.setCountQty(randomCountDTO.getCountQty());
		countRecord.setWspId(sku.getWspId());
		SkuPackageDetail one = SkuPackageDetailCache.getOne(sku.getWspId(), SkuLevelEnum.Base.getIndex());
		countRecord.setWsuName(one.getWsuName());
		countRecord.setSkuLevel(one.getSkuLevel());
		countRecord.setConvertQty(one.getConvertQty());
		countRecord.setSerialNumber(serialNumber);
		countRecord.setProcTime(LocalDateTime.now());
		countRecord.setUserId(AuthUtil.getUserId());
		countRecord.setUserName(AuthUtil.getNickName());
		countRecord.setRecordState(CountRecordStateEnum.Create.getIndex());
		if (Func.isNotEmpty(stock)) {
			countRecord.setStockId(stock.getStockId());
			countRecord.setLotNumber(stock.getLotNumber());
			countRecord.setStockQty(stock.getStockQty().subtract(stock.getPickQty()));
			// 验证当前库存有没有占用信息
			QueryWrapper<StockOccupy> stockOccupyQueryWrapper = Condition.getQueryWrapper(new StockOccupy());
			stockOccupyQueryWrapper.lambda()
				.eq(StockOccupy::getStockId, stock.getStockId())
				.eq(StockOccupy::getOccupyType, StockOccupyTypeEnum.Count.getIndex());
			List<StockOccupy> stockOccupyList = stockOccupyService.list(stockOccupyQueryWrapper);
			if (Func.isNotEmpty(stockOccupyList)) {
				StockOccupy stockOccupy = stockOccupyList.get(0);
				// 根据盘点报告ID获取盘点单
				CountReport countReport = countReportService.getById(stockOccupy.getWcrId());
				if (Func.isNotEmpty(countReport)) {
					throw new ServiceException(
						"当前盘点库存信息已被盘点单：" + countReport.getCountBillNo() + "占用，请先处理后再执行盘点！"
					);
				} else {
					throw new ServiceException(
						"当前盘点库存信息已被占用，请先处理后再执行盘点！"
					);
				}
			}
		}
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			// 批属性信息都为空的情况下跳过~~~
			if (!randomCountDTO.skuLotChk(i) && ObjectUtil.isNotEmpty(stock) && !stock.skuLotChk(i)) {
				continue;
			}
			if (randomCountDTO.skuLotChk(i)) {
				countRecord.skuLotSet(i, randomCountDTO.skuLotGet(i));
			} else if (ObjectUtil.isNotEmpty(stock) && stock.skuLotChk(i)) {
				countRecord.skuLotSet(i, stock.skuLotGet(i));
			}
		}
		if (Func.isNull(countRecord.getLpnCode())) {
			countRecord.setLpnCode(StringPool.EMPTY);
		}
		boolean result = super.save(countRecord);
		// 创建系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		if (Func.isNotEmpty(countHeader)) {
			systemProcParam.setProcType(SystemProcTypeEnum.COUNT);
			systemProcParam.setAction(ActionEnum.COUNT_STATIC);
		} else {
			systemProcParam.setProcType(SystemProcTypeEnum.COUNT);
			systemProcParam.setAction(ActionEnum.COUNT_RANDOM);
		}
		systemProcParam.setDataType(DataTypeEnum.CountBillNo);
		systemProcParam.setBillNo(countRecord.getCountBillNo());
//		if (ObjectUtil.isNotEmpty(stock)) {
//			systemProcParam.setLpnCode(stock.getLpnCode());
//			systemProcParam.setBoxCode(stock.getBoxCode());
//		}
		systemProcParam.setOperationQty(randomCountDTO.getCountQty());
		systemProcParam.setOperationUnit(skuPackageDetail.getWsuName());
		systemProcParam.setWhId(randomCountDTO.getWhId());
		systemProcService.create(systemProcParam);
		return result;
	}

	@Override
	public List<CountRecord> listAll(CountRecordVO countRecordVO, int type) {
		CountRecord countRecord = BeanUtil.copy(countRecordVO, CountRecord.class);
		if (type == 2) {
			if (countRecord.getWhId() == null) throw new ServiceException("必须选择一个库房查询！");
			countRecord.setCountBillId(-1L);
			countRecord.setCountBillNo("RANDOM");
			countRecord.setRecordState(1);
		}
		if (countRecordVO.getStartTimeArray() == null)//防止前台传空又有时后传0
		{
			String[] strings = new String[0];
			countRecordVO.setStartTimeArray(strings);
		}
		List<Long> longListLocId = new ArrayList<>();
		if (Func.isNotEmpty(countRecordVO.getZoneId())) {
			//List<Location> locationList = LocationCache.listByZoneId(countRecordVO.getZoneId());
			ILocationService locationService = SpringUtil.getBean(ILocationService.class);
			List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.in(Location::getZoneId, countRecordVO.getZoneId())
			).stream().collect(Collectors.toList());
			for (Location location : locationList) {
				longListLocId.add(location.getLocId());
			}
		}
		if (longListLocId.size() > 0 && countRecordVO.getStartTimeArray().length > 0) {
			return this.list(Condition.getQueryWrapper(countRecord).lambda().in(CountRecord::getLocId, longListLocId)
				.between(CountRecord::getProcTime, countRecordVO.getStartTimeArray()[0], countRecordVO.getStartTimeArray()[1]));
		} else if (countRecordVO.getStartTimeArray().length > 0 && longListLocId.size() <= 0) {
			return this.list(Condition.getQueryWrapper(countRecord).lambda().between(CountRecord::getProcTime, countRecordVO.getStartTimeArray()[0], countRecordVO.getStartTimeArray()[1]));
		} else if (countRecordVO.getStartTimeArray().length <= 0 && longListLocId.size() > 0) {
			return this.list(Condition.getQueryWrapper(countRecord).lambda().in(CountRecord::getLocId, longListLocId));
		} else {
			return this.list(Condition.getQueryWrapper(countRecord));
		}
	}

	@Override
	public void exportCountLogs(IPage<CountRecordVO> countRecordVOIPage, HttpServletResponse response) {
//		final String fileName = "盘点日志";
//		Path path = attachmentService.getTemplatePath(fileName);
//		List<CountRecordVO> records = countRecordVOIPage.getRecords();
//		if (Func.isNotEmpty(records)) {
//			ExcelUtil.writeExcelToResponseStream(fileName, path.toString(), records, response);
//		} else {
//			ExcelUtil.download(path.toString(), fileName, response);
//		}
	}

}
