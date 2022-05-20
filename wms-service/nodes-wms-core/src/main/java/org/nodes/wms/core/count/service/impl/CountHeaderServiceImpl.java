package org.nodes.wms.core.count.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.enums.SnEnum;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.common.enums.CreateTypeEnum;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.entity.CountReport;
import org.nodes.wms.core.count.enums.*;
import org.nodes.wms.core.count.mapper.CountHeaderMapper;
import org.nodes.wms.core.count.service.*;
import org.nodes.wms.core.count.vo.CountDetailVO;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.nodes.wms.core.count.vo.CountReportVO;
import org.nodes.wms.core.count.wrapper.CountHeaderWrapper;
import org.nodes.wms.core.count.wrapper.CountReportWrapper;
import org.nodes.wms.core.count.wrapper.CountTypeQueryFactory;
import org.nodes.wms.core.instock.asn.cache.AsnCache;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.enums.AsnDetailStatusEnum;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.instock.asn.vo.AsnDetailVO;
import org.nodes.wms.core.instock.asn.vo.AsnHeaderVO;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.cache.SoCache;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.enums.*;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.vo.SoDetailVO;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.nodes.wms.core.stock.core.dto.StockAddDTO;
import org.nodes.wms.core.stock.core.dto.StockOccupyDTO;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.dto.StockSubtractDTO;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.CountSkuQtyVO;
import org.nodes.wms.core.system.dto.TaskDTO;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.entity.TaskPackage;
import org.nodes.wms.core.system.enums.TaskProcTypeEnum;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskPackageService;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.utils.SkuLotUtil;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.nodes.wms.dao.instock.asn.enums.InStorageTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 盘点单头表 服务实现类
 *
 * @author CHZ
 * @since 2020-01-02
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class CountHeaderServiceImpl<M extends CountHeaderMapper, T extends CountHeader>
	extends BaseServiceImpl<CountHeaderMapper, CountHeader>
	implements ICountHeaderService {

	@Autowired
	private ILocationService locationService;
	@Autowired
	private IStockService stockService;
	@Autowired
	private IStockOccupyService stockOccupyService;
	@Autowired
	private ICountDetailService countDetailService;
	@Autowired
	private ITaskPackageService taskPackageService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private ICountRecordService countRecordService;
	@Autowired
	private ICountReportService countReportService;
	@Autowired
	private ISystemProcService systemProcService;
	@Autowired
	private ISerialService serialService;
	@Autowired
	private IAsnHeaderService asnHeaderService;
	@Autowired
	private IAsnDetailService asnDetailService;
	@Autowired
	private ISoHeaderService soHeaderService;
	@Autowired
	private ISoDetailService soDetailService;


	@Override
	public IPage<CountHeader> selectCountHeaderPage(IPage<CountHeader> page, CountHeaderVO countHeader) {
		return page.setRecords(baseMapper.selectCountHeaderPage(page, countHeader));
	}

	//获取盘点货位
	@Override
	public List<CountHeaderVO> selectLocation(@Validated CountHeaderVO countHeader) {
		ICountTypeQuery countTypeQuery = CountTypeQueryFactory.getCountTypeQuery(countHeader.getSearchType(), countHeader);
		List<CountHeaderVO> countHeaderVOS = countTypeQuery.queryByCountType();
		return countHeaderVOS;
	}

	@Override
	public boolean add(CountHeaderVO countHeaderVO) {
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		if (Func.isEmpty(countHeaderVO.getCountDetailList())) {
			throw new ServiceException("盘点单库位明细不能为空！");
		}
		List<Long> skuIds = NodesUtil.toList(countHeaderVO.getCountDetailList(), CountDetail::getSkuId);
		List<Long> locIds = NodesUtil.toList(countHeaderVO.getCountDetailList(), CountDetail::getLocId);
		if (CountByEnum.SKU.getIndex() == countHeaderVO.getCountBy()) {
			if (Func.isEmpty(skuIds)) {
				throw new ServiceException("传参中获取skuId为空");
			}
			QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
			queryWrapper.lambda().in(Stock::getSkuId, skuIds)
				.eq(Stock::getStockStatus, StockStatusEnum.LOCK_FILL.getIndex());
			List<Stock> stocklist = stockService.list(queryWrapper);

			if (Func.isNotEmpty(stocklist)) {
				List<Sku> collect = skuService.listByIds(stocklist
					.stream().map(Stock::getSkuId).collect(Collectors.toList()));
				throw new ServiceException(String.format("物品【%s】库存已被冻结不允许创建盘点单！"
					, NodesUtil.join(collect, "skuCode")
				));
			}
			//判断库位是否有被占用
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailBySkuCodes(skuIds);
			if (Func.isNotEmpty(countDetails)) {
				List<CountDetail> collect = countDetails.stream().filter(countDetail -> locIds.contains(countDetail.getLocId())).collect(Collectors.toList());
				if (Func.isNotEmpty(collect)) {
					throw new ServiceException(String.format("物品【%s】被盘点单【%s】占用中不允许创建盘点单！"
						, NodesUtil.join(countDetails, "skuCode"),
						NodesUtil.join(collect, "countBillNo")
					));
				}
			}

		}
		if (CountByEnum.LOCATION.getIndex() == countHeaderVO.getCountBy()) {
			if (Func.isEmpty(locIds)) {
				throw new ServiceException("传参中获取locId为空");
			}
			QueryWrapper<Location> queryWrapper = Condition.getQueryWrapper(new Location());
			queryWrapper.lambda().in(Location::getLocId, locIds)
				.eq(Location::getLockFlag, StringPool.N.toUpperCase());
			List<Location> list = locationService.list(queryWrapper);
			if (Func.isNotEmpty(list)) {
				throw new ServiceException(String.format("库位【%s】已被冻结不允许创建盘点单！"
					, NodesUtil.join(list, "locCode")
				));
			}
			//判断库位是否有被占用
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailByLocCodes(locIds);
			if (Func.isNotEmpty(countDetails)) {
				throw new ServiceException(String.format("库位【%s】被盘点单【%s】占用中不允许创建盘点单！"
					, NodesUtil.join(countDetails, "locCode"),
					NodesUtil.join(countDetails, "countBillNo")
				));
			}

		}
		if (this.save(countHeaderVO)) {
			// 查询当前选中的库位所有库存信息
			List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
				.eq(Stock::getWhId, countHeaderVO.getWhId())
				.in(Stock::getLocId, locIds)
				.and(sql -> {
					sql.gt(Stock::getOccupyQty, BigDecimal.ZERO);
				})
			);
			if (Func.isNotEmpty(stockList)) {
				List<Long> locIdList = NodesUtil.toList(stockList, Stock::getLocId);
				//List<Location> locationList = LocationCache.listByIds(locIdList);
				ILocationService locationService = SpringUtil.getBean(ILocationService.class);
				List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
					.lambda()
					.in(Location::getLocId, locIdList)
				).stream().collect(Collectors.toList());
				throw new ServiceException(
					"库位[" + NodesUtil.join(locationList, "locCode") + "]存在库存占用，不允许创建盘点盘！");
			}
			// 新增盘点明细
			for (CountDetail countDetail : countHeaderVO.getCountDetailList()) {
				// 保存明细
				countDetail.setCountBillId(countHeaderVO.getCountBillId());
				countDetail.setCountBillNo(countHeaderVO.getCountBillNo());
				countDetail.setUserName(countHeaderVO.getCreator());
				countDetail.setLocState(StockCountStateEnum.CREATE.getIndex());
			}
			countDetailService.saveBatch(countHeaderVO.getCountDetailList());
			List<Long> locIdList = NodesUtil.toList(countHeaderVO.getCountDetailList(), CountDetail::getLocId);
			if (Func.isNotEmpty(locIdList)) {
				locationService.updateCountBillNo(locIdList, countHeaderVO.getCountBillNo());

			}
		}
		return true;
	}

	@Override
	public CountHeaderVO getDetail(CountHeader countHeader) {
		//查询盘点主表
		CountHeader countHeaderGet = this.getOne(Condition.getQueryWrapper(countHeader));
		CountHeaderVO countHeaderVO = CountHeaderWrapper.build().entityVO(countHeaderGet);
		countHeaderVO.setCountBillNo(countHeaderGet.getCountBillNo());
		countHeaderVO.setCountTag(countHeaderGet.getCountTag());
		countHeaderVO.setCountRemark(countHeaderGet.getCountRemark());
		countHeaderVO.setCountBillId(countHeaderGet.getCountBillId());
		countHeaderVO.setCountBillState(countHeaderGet.getCountBillState());

		//查询盘点从表
		countHeaderVO.setCountDetailList(countDetailService.list(Condition.getQueryWrapper(new CountDetail())
			.lambda()
			.eq(CountDetail::getCountBillId, countHeaderGet.getCountBillId())));
		List<CountDetailVO> countDetailVOList = new ArrayList<>();
		for (CountDetail countDetail : countHeaderVO.getCountDetailList()) {
			boolean exist = countDetailVOList.stream().filter(detail -> {
				return countDetail.getLocId().equals(detail.getLocId());
			}).collect(Collectors.toList()).size() > 0;
			if (exist) {
				continue;
			}
			CountDetailVO countDetailVO = new CountDetailVO();
			countDetailVO.setWhName(countHeaderVO.getWhName());
			countDetailVO.setCountBillNo(countDetail.getCountBillNo());
			countDetailVO.setCountDetailId(countDetail.getCountDetailId());
			countDetailVO.setCountBillId(countDetail.getCountBillId());
			countDetailVO.setSkuId(countDetail.getSkuId());
			countDetailVO.setSkuCode(countDetail.getSkuCode());
			countDetailVO.setSkuName(countDetail.getSkuName());
			countDetailVO.setLocCode(countDetail.getLocCode());
			countDetailVO.setLocState(countDetail.getLocState());
			countDetailVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, countDetail.getLocState()));
			countDetailVO.setCountTagDesc(DictCache.getValue(DictConstant.STOCK_COUNT_TYPE, countHeaderGet.getCountTag()));
			countDetailVO.setCountBillStateDesc(DictCache.getValue(DictConstant.STOCK_COUNT_STATE, countHeaderGet.getCountBillState()));
			countDetailVO.setUserName(countDetail.getUserName());
			countDetailVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, countDetail.getLocState()));

			countDetailVO.setProcTime(countDetail.getProcTime());
			if (Func.isNotEmpty(countDetail.getProcTime())) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME);
				countDetailVO.setProcTimeDesc(dateTimeFormatter.format(countDetail.getProcTime()));
			}
			countDetailVOList.add(countDetailVO);
		}
		countHeaderVO.setCountDetailVOList(countDetailVOList);
		if (countHeaderVO.getCountDetailList().size() > 0) {
			countHeaderVO.setUserName(countHeaderVO.getCountDetailList().get(0).getUserName());
		}
		List<CountReport> reportList = countReportService.list(Wrappers.lambdaQuery(CountReport.class)
			.eq(CountReport::getCountBillId, countHeaderGet.getCountBillId())
			.apply("wms_qty <> count_qty"));
		if (reportList.size() > 0) {
			List<CountReportVO> countReportVOList = new ArrayList<>();
			for (CountReport countReportAdd : reportList) {
				CountReportVO countReportVO = BeanUtil.copy(countReportAdd, CountReportVO.class);
				countReportVO.setDifferentNum(countReportVO.getCountQty().subtract(countReportVO.getWmsQty()));
				countReportVOList.add(countReportVO);

			}
			countHeaderVO.setCountReportVOList(countReportVOList);
		}

		return countHeaderVO;
	}

	@Override
	public CountHeaderVO getDetailPda(CountHeader countHeader) {
		//查询盘点主表
		CountHeader countHeaderGet = baseMapper.selectOne(Wrappers.lambdaQuery(CountHeader.class)
			.eq(CountHeader::getCountBillNo, countHeader.getCountBillNo()));
		if (Func.isNull(countHeaderGet)) {
			throw new ServiceException("单据不存在");
		}
		CountHeaderVO countHeaderVO = new CountHeaderVO();
		countHeaderVO.setCountBillNo(countHeaderGet.getCountBillNo());
		countHeaderVO.setCountTag(countHeaderGet.getCountTag());
		countHeaderVO.setCountRemark(countHeaderGet.getCountRemark());
		countHeaderVO.setCountBillId(countHeaderGet.getCountBillId());
		countHeaderVO.setCountBillState(countHeaderGet.getCountBillState());
		// 获取库房详细信息
		Warehouse warehouse = WarehouseCache.getById(countHeaderGet.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在");
		}

		//查询盘点从表
		CountDetail countDetail = new CountDetail();
		countDetail.setCountBillId(countHeaderGet.getCountBillId());
		//countDetail.setLocState(ELocStatus.UnComplate.getIndex());
		List<CountDetail> countDetailList = countDetailService.list(Condition.getQueryWrapper(countDetail));
		List<Long> skuIds = NodesUtil.toList(countDetailList, CountDetail::getSkuId);
		Map<String, String> sqlSkuLotMap = SkuLotUtil.createSqlSkuLotMap(CountSkuQtyVO.class, new CountSkuQtyVO());
		List<CountSkuQtyVO> countSkuQtyVOS = countDetailService.querySkuQty(sqlSkuLotMap, skuIds);
		countSkuQtyVOS.forEach(countSkuQtyVO -> countSkuQtyVO.setMode(countHeaderGet.getMode()));
		if (countDetailList.size() <= 0) {
			throw new ServiceException("当前单据没有可盘点的库位");
		}
		Map<Long, List<CountDetail>> collect1 = countDetailList.stream().collect(Collectors.groupingBy(CountDetail::getLocId));
		Iterator<Map.Entry<Long, List<CountDetail>>> iterator = collect1.entrySet().iterator();
		List<CountDetailVO> temp = new ArrayList<>();
		while (iterator.hasNext()) {
			Map.Entry<Long, List<CountDetail>> next = iterator.next();
			CountDetail source = next.getValue().get(0);
			CountDetailVO countDetailVO = BeanUtil.copyWithConvert(source, CountDetailVO.class);
			countDetailVO.setWhName(warehouse.getWhName());
			countDetailVO.setLocState(source.getLocState());
			countDetailVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, source.getLocState()));
			countDetailVO.setCountBy(countHeaderGet.getCountBy());
			countDetailVO.setMode(countHeaderGet.getMode());
			if (CountByEnum.SKU.getIndex().equals(countHeaderGet.getCountBy())) {
				countDetailVO.setSkuType(next.getValue().size());
				countDetailVO.setCountSkuDetails(next.getValue());
				countDetailVO.setSkuQtyVOS(countSkuQtyVOS);
			} else {
				Set<Long> types = new HashSet<>();
				//查询出来库存中在当前容器里的所有状态正常的物品
				List<Stock> list = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getLocId, next.getKey())
					.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
					.apply("stock_qty - pick_qty > 0"));
				for (Stock stock : list) {
					//种类
					types.add(stock.getSkuId());
				}
				countDetailVO.setSkuType(types.size());
			}
			temp.add(countDetailVO);
			countHeaderVO.setCountDetailVOList(temp);
		}
		countHeaderVO.getCountDetailVOList().sort(Comparator.comparing(CountDetailVO::getLocCode));
		return countHeaderVO;
	}

	@Override
	public String getCNo() {
		return SerialNoCache.getCNo();
	}

	@Override
	public boolean removeByIdsNew(String ids) {
		CountDetail countDetail = new CountDetail();
		countDetail.setCountBillId(Func.toLong(ids));
		//先查询出来以前的记录数据
		List<CountDetail> countDetailList = countDetailService.selectCountDetailList(countDetail);
		if (this.removeById(ids)) {
			//删除从表
			for (CountDetail countDetaildelete : countDetailList) {
				locationService.updateCountBillNo(countDetaildelete.getLocId(), null);
			}
			countDetailService.removeByIdsNew(ids);

		}
		return true;
	}

	//分配盘点任务
	@Override
	public boolean countTask(CountHeaderVO countHeaderVO) {
		CountHeader countHeader = super.getById(countHeaderVO.getCountBillId());
		if (ObjectUtil.isEmpty(countHeader)) {
			throw new ServiceException("盘点单不存在！");
		}
		if (countHeader.getCountBillState() != StockCountStateEnum.CREATE.getIndex()) {
			throw new ServiceException("当前订单状态不能分配任务");
		}
		BladeUser user = AuthUtil.getUser();
		CountDetail countDetail = new CountDetail();
		countDetail.setCountBillId(countHeader.getCountBillId());
		//先查询出来所有的记录数据
		List<CountDetail> countDetailList = countDetailService.selectCountDetailList(countDetail);
		if (ObjectUtil.isEmpty(countHeaderVO.getCountTask())) {
			throw new RuntimeException("任务分配数量不能为空！");
		}
		if (countHeaderVO.getCountTask() <= 0) {
			throw new ServiceException("任务分配数量必须大于零！");
		}
		if (countHeaderVO.getCountTask() > countDetailList.size()) {
			throw new ServiceException("任务分配数量不能大于总库位数量");
		}
		//先保存taskPackage表
		TaskPackage taskPackage = new TaskPackage();
		taskPackage.setWhId(countHeader.getWhId());
		taskPackage.setTaskTypeCd(TaskTypeEnum.Count.getIndex());
		taskPackage.setTaskProcType(TaskProcTypeEnum.Cooperation.getIndex());
		taskPackage.setTaskDataId(countHeader.getCountBillId());
		taskPackage.setTtpCreateTime(LocalDateTime.now());
		if (!taskPackageService.save(taskPackage)) {
			throw new ServiceException("保存任务头表失败");
		}
		// 计算每个任务的库位数量
		int taskLocCount = countDetailList.size() / countHeaderVO.getCountTask();
		// 记录每个任务包含的库位数量
		Integer[] taskSize = new Integer[countHeaderVO.getCountTask()];
		if (countDetailList.size() % countHeaderVO.getCountTask() == 0) {
			for (int i = 0; i < countHeaderVO.getCountTask(); i++) {
				taskSize[i] = taskLocCount;
			}
		} else {
			int allotCount = 0;
			for (int i = 0; i < taskLocCount + 1; i++) {
				if (i < countHeaderVO.getCountTask() - 1) {
					taskSize[i] = taskLocCount + 1;
				} else {
					taskSize[i] = countDetailList.size() - allotCount;
				}
				allotCount += taskLocCount + 1;
			}
		}
		// 创建任务
		for (int i = 0; i < taskSize.length; i++) {
			TaskDTO taskDTO = new TaskDTO();
			taskDTO.setBillId(countHeader.getCountBillId());
			taskDTO.setBillNo(countHeader.getCountBillNo());
			taskDTO.setTaskPackage(taskPackage);
			taskDTO.setTaskType(TaskTypeEnum.Count.getIndex());
			taskDTO.setWhId(countHeader.getWhId());
			taskDTO.setBillTypeCd("909");
			taskDTO.setTaskQty(countDetailService.list(countHeader.getCountBillId()).size());
			taskDTO.setAllotTime(LocalDateTime.now());
			taskDTO.setConfirmDate(LocalDateTime.now());
			Task task = taskService.create(taskDTO);
			String taskGroup = countHeaderVO.getCountBillNo() + String.format("%02d", i + 1);

			for (int j = 0; j < taskSize[i]; j++) {
				CountDetail detail = countDetailList.get(i * taskSize[i] + j);
				detail.setTaskGroup(taskGroup);
				detail.setLocState(LocStatusEnum.UnComplate.getIndex());
				countDetailService.updateById(detail);
			}
			task.setTaskQty(taskSize[i]);
			task.setTtpId(taskPackage.getTtpId());
			task.setTaskGroup(taskGroup);
			taskService.updateById(task);
		}
		return this.updateBillState(countHeader, StockCountStateEnum.COUNTING);
	}

	/**
	 * 完成盘点
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public boolean completeTask(String ids) {
		List<Long> idList = Func.toLongList(ids);
		List<CountHeader> countHeaders = baseMapper.selectList(Wrappers.lambdaQuery(CountHeader.class)
			.in(CountHeader::getCountBillId, idList));
		countHeaders.forEach(countHeader -> {
			if (countHeader.getCountBillState() >= StockCountStateEnum.COUNT_COMPLATED.getIndex()) {
				throw new ServiceException("[" + countHeader.getCountBillNo() + "]盘点单状态不是等待分派任务和正在盘点的状态");
			}
		});
		this.updateBillState(countHeaders, StockCountStateEnum.COUNT_COMPLATED);

		List<String> countBillNoList = new ArrayList<>();
		countHeaders.stream().forEach(it -> countBillNoList.add(it.getCountBillNo()));
//		List<Location> locationList = LocationCache.list().stream().filter(u -> {
//			return countBillNoList.contains(u.getCountBillNo());
//		}).collect(Collectors.toList());
//		QueryWrapper<Location> queryWrapper = Condition.getQueryWrapper(new Location());
//		queryWrapper.in("count_bill_no",countBillNoList);
//		List<Location> locationList = locationService.list(queryWrapper);
//		locationService.updateCountBillNo(NodesUtil.toList(locationList, "locId", Long.class), null);

		countDetailService.update(Wrappers.<CountDetail>update().lambda()
			.set(CountDetail::getLocState, LocStatusEnum.Complated.getIndex())
			.in(CountDetail::getCountBillId, idList));

		List<Task> taskList = taskService.list(Condition.getQueryWrapper(new Task()).lambda()
			.in(Task::getBillId, idList));
		if (taskList.size() > 0) {
			for (Task taskClose : taskList) {
				taskService.close(taskClose.getTaskId().toString());
			}
			taskPackageService.removeById(taskList.get(0).getTtpId());
		}
		return true;
	}

	@Override
	public CountHeaderVO completeTaskPda(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		QueryWrapper<CountDetail> ahqwm = new QueryWrapper<>();
		ahqwm.lambda().in(CountDetail::getCountDetailId, idList);
		List<CountDetail> countDetailList = countDetailService.list(ahqwm);
		if (Func.isEmpty(countDetailList)) {
			throw new ServiceException("盘点明细不存在，请检查盘点任务！");
		}
		CountDetail countDetail = countDetailList.get(0);
		CountHeader countHeader = baseMapper.selectOne(Wrappers.lambdaQuery(CountHeader.class)
			.eq(CountHeader::getCountBillId, countDetail.getCountBillId()));
		if (CountByEnum.SKU.getIndex().equals(countHeader.getCountBy())) {
			countDetailService.update(Wrappers.lambdaUpdate(CountDetail.class)
				.set(CountDetail::getLocState, LocStatusEnum.Complated.getIndex())
				.eq(CountDetail::getCountBillId, countDetail.getCountBillId())
				.in(CountDetail::getLocCode, NodesUtil.toList(countDetailList, CountDetail::getLocCode)));
		} else {
			countDetailList.forEach(countDetail1 -> countDetail1.setLocState(LocStatusEnum.Complated.getIndex()));
			countDetailService.updateBatchById(countDetailList);
		}
		List<CountDetail> list = countDetailService.list(countHeader.getCountBillId());
		List<CountDetail> list1 = countDetailService.list(Wrappers.lambdaQuery(CountDetail.class)
			.eq(CountDetail::getCountBillId, countHeader.getCountBillId())
			.eq(CountDetail::getLocState, LocStatusEnum.Complated.getIndex()));
		if (Func.isNotEmpty(list) && Func.isNotEmpty(list1)) {
			if (list.size() == list1.size()) {
				completeTask(String.valueOf(countHeader.getCountBillId()));
			}
		}
		CountHeaderVO countHeaderVO = getDetailPda(countHeader);
		return countHeaderVO;
	}

	/**
	 * 创建差异单据
	 *
	 * @param ids 盘点单ID
	 * @return
	 */
	@Override
	public boolean differenceOrder(String ids) {
		List<Long> idList = Func.toLongList(ids);

		// 获取所有盘点单信息
		List<CountHeader> countHeaderList = this.list(Condition.getQueryWrapper(new CountHeader())
			.lambda()
			.in(CountHeader::getCountBillId, idList));
		// 获取所有盘点报告
		List<CountReport> countReportList = countReportService.list(Condition.getQueryWrapper(new CountReport())
			.lambda()
			.in(CountReport::getCountBillId, idList));

		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMinimumIntegerDigits(8);
		numberFormat.setGroupingUsed(false);

		// 记录待保存的入库单集合
		List<AsnHeaderVO> asnHeaderList = new ArrayList<>();
		// 记录待保存的出库单集合
		List<SoHeaderVO> soHeaderList = new ArrayList<>();
		List<Long> locationIdList = new ArrayList<>();
		for (CountHeader countHeader : countHeaderList) {

			if (countHeader.getCountBillState() != StockCountStateEnum.CREATE_COUNT_DIFF.getIndex()) {
				throw new ServiceException(String.format(
					"盘点单[%s]当前状态状态[%s] 不允许创建差异单据！",
					countHeader.getCountBillNo(), StockCountStateEnum.valueOf(countHeader.getCountBillState())));
			}
			// 创建系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.COUNT);
			systemProcParam.setDataType(DataTypeEnum.CountBillNo);
			systemProcParam.setAction(ActionEnum.COUNT_DIFF_CREATE);
			systemProcParam.setBillNo(countHeader.getCountBillNo());
			systemProcParam.setWhId(countHeader.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);

			Warehouse warehouse = WarehouseCache.getById(countHeader.getWhId());
			if (Func.isEmpty(warehouse)) {
				throw new ServiceException(String.format("盘点单:%s 指定库房(ID:%s)不存在！",
					countHeader.getCountBillNo(), countHeader.getWhId()));
			}
			// 获取当前盘点单所有差异报告
			List<CountReport> reportList = countReportList.stream().filter(u -> {
				return u.getCountBillId().equals(countHeader.getCountBillId())
					&& BigDecimalUtil.ne(u.getCountQty(), u.getWmsQty());
			}).collect(Collectors.toList());
			if (Func.isEmpty(reportList)) {
				throw new ServiceException("盘点单:" + countHeader.getCountBillNo() + " 没有查到差异报告！");
			}
			// 将报告根据 货主ID 进行分组
			Map<Long, List<CountReport>> woIdMap = new HashMap<>();
			for (CountReport report : reportList) {
				// 获取物品详细信息
				Sku sku = SkuCache.getById(report.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("物品不存在(ID:" + report.getSkuId() + ")");
				}
				if (!woIdMap.containsKey(sku.getWoId())) {
					woIdMap.put(sku.getWoId(), new ArrayList<>());
				}
				woIdMap.get(sku.getWoId()).add(report);
				locationIdList.add(report.getLocId());
			}
			// 开始创建出入库单据
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			for (Long woId : woIdMap.keySet()) {
				Owner owner = ownerService.getById(woId);
				if (Func.isEmpty(owner)) {
					throw new ServiceException("指定货主不存在(ID:" + woId + ")!");
				}
				List<CountReport> list = woIdMap.get(woId);
				// 从差异报告中抽取 盘盈、盘亏 的差异报告；
				List<CountReport> instockReportList = list.stream().filter(u -> {
					return BigDecimalUtil.gt(u.getCountQty(), u.getWmsQty());
				}).collect(Collectors.toList());
				List<CountReport> outstockReportList = list.stream().filter(u -> {
					return BigDecimalUtil.gt(u.getWmsQty(), u.getCountQty());
				}).collect(Collectors.toList());

				// 创建入库单
				if (Func.isNotEmpty(instockReportList)) {
					LocalDateTime now = LocalDateTime.now();
					// 封装入库单表头信息
					AsnHeaderVO asnHeader = new AsnHeaderVO();
					asnHeader.setWhId(countHeader.getWhId());
					asnHeader.setWoId(woId);
					asnHeader.setWhCode(warehouse.getWhCode());
					// 循环验证单号是否存在
					while (true) {
						asnHeader.setAsnBillNo(AsnCache.getAsnBillNo());
						int count = asnHeaderService.count(Condition.getQueryWrapper(new AsnHeader()).lambda()
							.eq(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo()));
						if (count == 0) {
							break;
						}
					}
//					asnHeader.setBillKey(countHeader.getCountBillNo());
					asnHeader.setAsnBillState(AsnBillStateEnum.COMPLETED.getCode());
					asnHeader.setBillTypeCd(ParamCache.getValue(ParamEnum.COUNT_PROFIT_TYPECD.getKey()));
					asnHeader.setInstoreType(InStorageTypeEnum.Normal.getCode());
					asnHeader.setScheduledArrivalDate(LocalDateTime.now());
//					asnHeader.setSCode(warehouse.getWhCode());
//					asnHeader.setSName(warehouse.getWhName());
					asnHeader.setAsnDetailList(new ArrayList<>());
					asnHeader.setFinishDate(now);
					asnHeader.setScheduledArrivalDate(now);
					asnHeader.setActualArrivalDate(now);
//					asnHeader.setLastUpdateDate(now);
//					asnHeader.setPreCreateDate(now);
//					asnHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
					asnHeader.setCreateType(CreateTypeEnum.INNER.getIndex());

					Integer lineNo = 1;

					// 封装入库单明细信息
					for (CountReport countReport : instockReportList) {
						// 封装库存增加参数对象
						StockAddDTO stockProc = BeanUtil.copy(countReport, StockAddDTO.class);
						stockProc.setLotNumber(countReport.getCountLot());
						stockProc.setSystemProcId(systemProc.getSystemProcId());
						stockProc.setEventType(EventTypeEnum.Count);
						stockProc.setBillNo(countHeader.getCountBillNo());
						// 获取物品详细信息
						Sku sku = SkuCache.getById(countReport.getSkuId());
						if (Func.isEmpty(sku)) {
							throw new ServiceException("物品不存在(ID:" + countReport.getSkuId() + ")");
						}
						// 获取当前物品的包装信息
						SkuPackage skuPackage = SkuPackageCache.getById(countReport.getWspId());
						if (Func.isEmpty(skuPackage)) {
							throw new ServiceException("物品包装不存在或已删除(ID:" + countReport.getWspId() + ")!");
						}
						// 获取基础计量单位信息
						SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
							skuPackage.getWspId(), SkuLevelEnum.Base.getIndex());
						if (Func.isEmpty(skuPackageDetail)) {
							throw new ServiceException("物品包装不存在基础计量单位(包装名称:" + skuPackage.getWspName() + ")");
						}
						AsnDetailVO asnDetail = new AsnDetailVO();
						asnDetail.setAsnLineNo(numberFormat.format(lineNo));
//						asnDetail.setAsnBillDetailKey(asnDetail.getAsnLineNo());
						asnDetail.setSkuId(countReport.getSkuId());
						asnDetail.setSkuCode(countReport.getSkuCode());
						asnDetail.setSkuName(countReport.getSkuName());
						asnDetail.setWspId(countReport.getWspId());
						asnDetail.setSkuLevel(SkuLevelEnum.Base.getIndex());
						asnDetail.setSkuSpec(skuPackage.getSpec());
						asnDetail.setConvertQty(skuPackageDetail.getConvertQty());
						asnDetail.setUmCode(skuPackageDetail.getWsuCode());
						asnDetail.setUmName(skuPackageDetail.getWsuName());
						asnDetail.setBaseUmCode(skuPackageDetail.getWsuCode());
						asnDetail.setBaseUmName(skuPackageDetail.getWsuName());
						asnDetail.setPlanQty(countReport.getCountQty().subtract(countReport.getWmsQty()));
						asnDetail.setScanQty(asnDetail.getPlanQty());
						asnDetail.setSurplusQty(asnDetail.getPlanQty().subtract(asnDetail.getScanQty()));
						asnDetail.setImportSn(StringPool.N.toUpperCase());
						asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
						// 处理明细批属性
						for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
							if (countReport.skuLotChk(i)) {
								asnDetail.skuLotSet(i, countReport.skuLotGet(i));
							}
						}
						if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
							// 如果为序列号物品，则需要判断同包装、同批次的明细是否已经存在
							AsnDetailVO existAsnDetail = asnHeader.getAsnDetailList().stream().filter(u -> {
								return u.getSkuId().equals(countReport.getSkuId())
									&& u.getWspId().equals(countReport.getWspId())
									&& u.skuLotCompare(countReport);
							}).findFirst().orElse(null);
							if (Func.isNotEmpty(existAsnDetail)) {
								existAsnDetail.setPlanQty(existAsnDetail.getPlanQty().add(BigDecimal.ONE));
							} else {
								asnHeader.getAsnDetailList().add(asnDetail);
							}
							stockProc.getSerialList().add(countReport.getSerialNumber());
							stockProc.setStockQty(BigDecimal.ONE);
						} else {
							asnHeader.getAsnDetailList().add(asnDetail);
							stockProc.setStockQty(asnDetail.getPlanQty());
						}
						// 增加库存
						stockService.add(stockProc);
						lineNo++;
					}
					asnHeaderList.add(asnHeader);

				}
				// 创建出库单
				if (Func.isNotEmpty(outstockReportList)) {
					LocalDateTime now = LocalDateTime.now();
					// 封装出库单表头信息
					SoHeaderVO soHeader = new SoHeaderVO();
					soHeader.setWhId(countHeader.getWhId());
					soHeader.setWoId(woId);
					soHeader.setWhCode(warehouse.getWhCode());
					// 循环验证单号是否存在
					while (true) {
						soHeader.setSoBillNo(SoCache.getSoBillNo());
						int count = soHeaderService.count(Condition.getQueryWrapper(new SoHeader()).lambda()
							.eq(SoHeader::getSoBillNo, soHeader.getSoBillNo()));
						if (count == 0) {
							break;
						}
					}

					soHeader.setBillKey(countHeader.getCountBillNo());
					soHeader.setSoBillState(SoBillStateEnum.COMPLETED.getIndex());
					soHeader.setBillTypeCd(ParamCache.getValue(ParamEnum.COUNT_LOSS_TYPECD.getKey()));
					soHeader.setBillKey(soHeader.getSoBillNo());
					soHeader.setTransportCode(SoBillTransportCodeEnum.SelfTaking.getIndex());
//					soHeader.setZoneType(ZoneTypeEnum.VIRTUAL.getIndex());
					soHeader.setOutstockType(OutstockTypeEnum.Normal.getIndex());
					soHeader.setCreateType(CreateTypeEnum.INNER.getIndex());
					soHeader.setCCode(warehouse.getWhCode());
					soHeader.setCName(warehouse.getWhName());
					soHeader.setDetailList(new ArrayList<>());
//					soHeader.setArriveDate(now);
					soHeader.setFinishDate(now);
//					soHeader.setTransportDate(now);
//					soHeader.setOutStockDate(now);
					soHeader.setLastUpdateDate(now);
					soHeader.setPreCreateDate(now);
					soHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
					soHeader.setBillCreator(AuthUtil.getNickName());

					Integer lineNo = 1;

					// 封装出库单明细信息
					for (CountReport countReport : outstockReportList) {
						// 封装库存扣减参数对象
						StockSubtractDTO stockReduce = BeanUtil.copy(countReport, StockSubtractDTO.class);
						stockReduce.setLotNumber(countReport.getCountLot());
						stockReduce.setSystemProcId(systemProc.getSystemProcId());
						stockReduce.setEventType(EventTypeEnum.Count);
						stockReduce.setDataId(countReport.getWcrepId());
						stockReduce.setBillNo(countHeader.getCountBillNo());
						// 获取物品详细信息
						Sku sku = SkuCache.getById(countReport.getSkuId());
						if (Func.isEmpty(sku)) {
							throw new ServiceException("物品不存在(ID:" + countReport.getSkuId() + ")");
						}
						// 获取当前物品的包装信息
						SkuPackage skuPackage = SkuPackageCache.getById(countReport.getWspId());
						if (Func.isEmpty(skuPackage)) {
							throw new ServiceException("物品包装不存在或已删除(ID:" + countReport.getWspId() + ")!");
						}
						// 获取基础计量单位信息
						SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
							skuPackage.getWspId(), SkuLevelEnum.Base.getIndex());
						if (Func.isEmpty(skuPackageDetail)) {
							throw new ServiceException("物品包装不存在基础计量单位(包装名称:" + skuPackage.getWspName() + ")");
						}
						SoDetailVO soDetail = new SoDetailVO();
						soDetail.setSoLineNo(numberFormat.format(lineNo));
						soDetail.setBillTypeCd(soHeader.getBillTypeCd());
						soDetail.setBillDetailKey(soDetail.getSoLineNo());
						soDetail.setSkuId(countReport.getSkuId());
						soDetail.setSkuCode(countReport.getSkuCode());
						soDetail.setSkuName(countReport.getSkuName());
						soDetail.setWspId(countReport.getWspId());
						soDetail.setSkuLevel(SkuLevelEnum.Base.getIndex());
						soDetail.setSkuSpec(skuPackage.getSpec());
						soDetail.setConvertQty(skuPackageDetail.getConvertQty());
						soDetail.setUmCode(skuPackageDetail.getWsuCode());
						soDetail.setUmName(skuPackageDetail.getWsuName());
						soDetail.setBaseUmCode(skuPackageDetail.getWsuCode());
						soDetail.setBaseUmName(skuPackageDetail.getWsuName());
						soDetail.setPlanQty(countReport.getWmsQty().subtract(countReport.getCountQty()));
						soDetail.setScanQty(soDetail.getPlanQty());
						soDetail.setSurplusQty(soDetail.getPlanQty().subtract(soDetail.getScanQty()));
						soDetail.setBillDetailState(SoDetailStateEnum.Allocated.getIndex());
						// 处理明细批属性
						for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
							if (countReport.skuLotChk(i)) {
								soDetail.skuLotSet(i, countReport.skuLotGet(i));
							}
						}
						if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
							// 如果为序列号物品，则需要判断同包装、同批次的明细是否已经存在
							SoDetailVO existSoDetail = soHeader.getDetailList().stream().filter(u -> {
								return u.getSkuId().equals(countReport.getSkuId())
									&& u.getWspId().equals(countReport.getWspId())
									&& u.skuLotCompare(countReport);
							}).findFirst().orElse(null);
							if (Func.isNotEmpty(existSoDetail)) {
								existSoDetail.setPlanQty(existSoDetail.getPlanQty().add(BigDecimal.ONE));
							} else {
								soHeader.getDetailList().add(soDetail);
							}
							stockReduce.getSerialList().add(countReport.getSerialNumber());
							stockReduce.setPickQty(BigDecimal.ONE);
						} else {
							soHeader.getDetailList().add(soDetail);
							stockReduce.setPickQty(soDetail.getPlanQty());
						}
						// 扣减库存
						stockService.subtract(stockReduce);
						lineNo++;
					}
					soHeaderList.add(soHeader);
				}
			}
		}
		// 批量创建入库单
		if (Func.isNotEmpty(asnHeaderList)) {
			// 保存表头数据
			List<AsnHeader> headerList = new ArrayList<>();
			asnHeaderList.forEach(headerList::add);
			asnHeaderService.saveBatch(headerList, asnHeaderList.size());

			// 批量保存明细
			List<AsnDetail> asnDetailList = new ArrayList<>();
			asnHeaderList.forEach(asnHeader -> {
				for (AsnDetailVO asnDetail : asnHeader.getAsnDetailList()) {
					asnDetail.setAsnBillId(asnHeader.getAsnBillId());
					asnDetail.setAsnBillNo(asnHeader.getAsnBillNo());
					asnDetailList.add(asnDetail);
				}
			});
			asnDetailService.saveBatch(asnDetailList, asnDetailList.size());
		}
		// 批量创建出库单
		if (Func.isNotEmpty(soHeaderList)) {
			// 保存表头数据
			List<SoHeader> headerList = new ArrayList<>();
			soHeaderList.forEach(headerList::add);
			soHeaderService.saveBatch(headerList, soHeaderList.size());

			// 批量保存明细
			List<SoDetail> soDetailList = new ArrayList<>();
			soHeaderList.forEach(soHeader -> {
				for (SoDetail soDetail : soHeader.getDetailList()) {
					soDetail.setSoBillId(soHeader.getSoBillId());
					soDetail.setSoBillNo(soHeader.getSoBillNo());
					soDetailList.add(soDetail);
				}
			});
			soDetailService.saveBatch(soDetailList, soDetailList.size());
		}
		// 批量释放库位占用
		this.locationService.updateCountBillNo(locationIdList, null);
		// 批量更新盘点单状态
		return this.updateBillState(countHeaderList, StockCountStateEnum.ADJUSTMENT);
	}

	/**
	 * 盘点单差异报告
	 *
	 * @param ids 盘点单ID
	 * @return
	 */
	@Override
	public boolean differenceReport(String ids) {
		List<CountHeader> countHeaderList = super.listByIds(Func.toLongList(ids));
		for (CountHeader countHeader : countHeaderList) {
			if (!countHeader.getCountBillState().equals(StockCountStateEnum.COUNT_COMPLATED.getIndex())) {
				throw new ServiceException("当前盘点单状态不是完成盘点状态");
			}
			// 修改盘点单状态 = 生成差异报告
			this.updateBillState(countHeader, StockCountStateEnum.CREATE_COUNT_DIFF);
			// 获取该盘点单下所有明细
			List<CountDetail> countDetailList = countDetailService.list(Condition.getQueryWrapper(new CountDetail())
				.lambda().eq(CountDetail::getCountBillId, countHeader.getCountBillId()));
			if (Func.isEmpty(countDetailList)) {
				throw new ServiceException("盘点单：" + countHeader.getCountBillNo() + " 下无明细，无法生成差异！");
			}

			// 用于存储差异报告ID和对应的库存
			Map<Long, Stock> reportStockMap = new HashMap<>();
			// 用于存储序列号
			List<String> serialNumberList = new ArrayList<>();

			// 遍历盘点单明细，生成差异
			for (CountDetail countDetail : countDetailList) {
				// 用于存储库存盘亏的差异
				List<CountReport> reportList = new ArrayList<>();
				// 获取库位详细信息
				Location location = LocationCache.getById(countDetail.getLocId());
				if (Func.isEmpty(location)) {
					throw new ServiceException("库存指定库位不存在(库位ID：" + countDetail.getLocId() + ")！");
				}
				// 获取该库位下，所有盘点记录
				List<CountRecord> countRecordList = countRecordService.list(
					Condition.getQueryWrapper(new CountRecord())
						.lambda()
						.eq(CountRecord::getCountBillId, countHeader.getCountBillId())
						.eq(CountRecord::getLocId, countDetail.getLocId()));
				// 获取库位上的库存信息
				List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getWhId, countHeader.getWhId())
					.eq(Stock::getLocId, countDetail.getLocId())
					.apply("pick_qty <> stock_qty"));
				// 先生成该库位下所有库存的盘点(盘亏)差异(如果是按物品盘点，则只生成该物品的差异报告就可以了)
				stockList.stream().forEach(stock -> {
					// 获取物品信息
					Sku sku = SkuCache.getById(stock.getSkuId());
					if (Func.isEmpty(sku)) {
						throw new ServiceException("库存关联物品不存在(物品ID：" + stock.getSkuId() + ")！");
					}
					if (Func.isNotEmpty(countDetail.getSkuId()) &&
						!Func.equals(countDetail.getSkuId(), sku.getSkuId())) {
						// 如果是按物品盘点，不是盘点的物品则返回
						return;
					}
					// 验证该库存是否存在盘点占用
					List<StockOccupy> stockOccupyList = this.stockOccupyService.list(
						Condition.getQueryWrapper(new StockOccupy()).lambda()
							.eq(StockOccupy::getStockId, stock.getStockId()));
					if (Func.isNotEmpty(stockOccupyList)) {
						throw new ServiceException(String.format(
							"在库位：%s 上物品：%s 存在库存占用，不允许生成差异报告！",
							location.getLocCode(), sku.getSkuName()));
					}
					// 验证物品是否 = 序列号管理
					List<Serial> serialList = null;
					if (sku.getIsSn() == 1) {
						// 获取该物品所有库存的序列号
						Serial serialQuery = new Serial();
						serialQuery.setStockId(stock.getStockId());
						serialList = serialService.list(Condition.getQueryWrapper(serialQuery));
					}
					if (Func.isNotEmpty(serialList)) {
						// 当前库存物品 = 序列号管理
						for (Serial serial : serialList) {
							CountReport countReport = CountReportWrapper.build().entity(
								countHeader, null, location, sku, stock, serial.getSerialNumber());
							countReport.setWmsQty(BigDecimal.ONE);
							this.countReportService.save(countReport);
							reportList.add(countReport);
							reportStockMap.put(countReport.getWcrepId(), stock);
						}
					} else {
						// 当前库存物品 = 非序列号管理
						CountReport countReport = CountReportWrapper.build().entity(
							countHeader, null, location, sku, stock, null);
						this.countReportService.save(countReport);
						reportList.add(countReport);
						reportStockMap.put(countReport.getWcrepId(), stock);
					}
				});

				// 遍历盘点记录，生成 或者 修改 盘点差异
				for (CountRecord countRecord : countRecordList) {
					// 验证是否存在重复的序列号
					if (serialNumberList.stream().filter(u -> u.equals(countRecord.getSerialNumber())).count() > 0) {
						throw new ServiceException("异常：序列号：" + countRecord.getSerialNumber() + " 同时存在多个库位！");
					}
					// 验证该盘点记录在差异列表中是否存在
					List<CountReport> reportFilter = reportList.stream().filter(report -> {
						return
							Func.isNotEmpty(report.getStockId()) &&
								report.getStockId().equals(countRecord.getStockId()) &&
								((Func.isEmpty(report.getSerialNumber()) && Func.isEmpty(countRecord.getSerialNumber())) ||
									(Func.isNotEmpty(report.getSerialNumber()) &&
										report.getSerialNumber().equals(countRecord.getSerialNumber())));
					}).collect(Collectors.toList());
					// 处理差异
					if (Func.isNotEmpty(reportFilter)) {
						// 存在：更新现有的盘点差异
						reportFilter.stream().forEach(report -> {
							report.setCountQty(countRecord.getCountQty());
							report.setCountLot(countRecord.getLotNumber());
							this.countReportService.saveOrUpdate(report);
						});
					} else {
						// 不存在：新增盘点(盘盈)差异
						CountReport countReport = BeanUtil.copy(countRecord, CountReport.class);
						countReport.setWmsQty(BigDecimal.ZERO);
//						countReport.setLocFlag(location.getLocFlag());
						countReport.setCountLot(countRecord.getLotNumber());
						countReport.setSystemLot(StringPool.SPACE);
						this.countReportService.saveOrUpdate(countReport);
					}
					if (Func.isNotEmpty(countRecord.getSerialNumber())) {
						serialNumberList.add(countRecord.getSerialNumber());
					}
				}
				// 生成差异报告(顺便删除 盘点数量=系统数量 的差异报告)
				reportList.stream().forEach(report -> {
					// 删除没有差异的 盘点差异报告
					if (report.getWmsQty().equals(report.getCountQty())) {
//						this.countReportService.removeById(report);
						return;
					}
					// 创建系统日志
					SystemProcDTO systemProcParam = new SystemProcDTO();
					systemProcParam.setProcType(SystemProcTypeEnum.COUNT);
					systemProcParam.setDataType(DataTypeEnum.CountBillNo);
					systemProcParam.setAction(ActionEnum.COUNT_DIFF_CREATE);
					systemProcParam.setWhId(report.getWhId());
					systemProcParam.setBillNo(report.getCountBillNo());
//					systemProcParam.setLocId(report.getLocId());
//					systemProcParam.setSkuId(report.getSkuId());
					systemProcParam.setOperationQty(report.getCountQty());
					systemProcParam.setOperationUnit(report.getWsuName());
					systemProcParam.setLpnCode(report.getLpnCode());
					Stock stock = reportStockMap.getOrDefault(report.getWcrepId(), null);
					if (Func.isNotEmpty(stock)) {
//						systemProcParam.setBoxCode(stock.getBoxCode());
					}
					SystemProc systemProc = systemProcService.create(systemProcParam);
					// 生成差异报告(盘亏的情况下才生成)
					if (report.getWmsQty().compareTo(report.getCountQty()) > 0) {
						StockOccupyDTO stockOccupyDTO = new StockOccupyDTO();
						stockOccupyDTO.setTransId(report.getCountBillId());
						stockOccupyDTO.setOccupyType(StockOccupyTypeEnum.Count.getIndex());
						stockOccupyDTO.setWhId(report.getWhId());
						if (Func.isNotEmpty(stock)) {
							stockOccupyDTO.setStockId(stock.getStockId());
						}
						stockOccupyDTO.setSkuId(report.getSkuId());
						stockOccupyDTO.setSkuCode(report.getSkuCode());
						stockOccupyDTO.setSkuName(report.getSkuName());
						stockOccupyDTO.setSystemProcId(systemProc.getSystemProcId());
						stockOccupyDTO.setOccupyTime(LocalDateTime.now());
						Sku sku = SkuCache.getById(report.getSkuId());
						if (sku.getIsSn() == 1) {
							stockOccupyDTO.setOccupyQty(BigDecimal.ONE);
						} else {
							stockOccupyDTO.setOccupyQty(report.getWmsQty().subtract(report.getCountQty()));
						}
						stockOccupyDTO.setWcrId(report.getWcrepId());
						stockOccupyDTO.setCreateUser(report.getCreateUser());
						stockOccupyDTO.setUpdateUser(report.getCreateUser());
						stockOccupyDTO.setSoBillNo(report.getCountBillNo());
						stockOccupyDTO.setSoBillId(report.getCountBillId());
						stockOccupyService.add(stockOccupyDTO);
					}

				});
			}
//			CallbackManager.instance.countDiff(countHeader.getCountBillNo());
		}
		return true;
	}

	/**
	 * 随机盘点差异报告生成
	 *
	 * @param countRecordList 随机盘点记录集合
	 * @return
	 */
	@Override
	public boolean randomCountDifferenceReport(List<CountRecordVO> countRecordList) {
		if (Func.isEmpty(countRecordList)) {
			throw new ServiceException("必须选择一条数据，在生成差异报告");
		}
		// 验证盘点记录是否已经生成了差异报告
		List<Long> wcrIdList = NodesUtil.toList(countRecordList, CountRecord::getWcrId);
		if (Func.isNotEmpty(wcrIdList)) {
			Integer complated = countRecordService.count(new QueryWrapper<CountRecord>().lambda()
				.in(CountRecord::getWcrId, wcrIdList)
				.eq(CountRecord::getRecordState, CountRecordStateEnum.Complated.getIndex()));
			if (complated > 0) {
				throw new ServiceException("提交盘点记录中包含已生成差异报告数据，请重新查询后再操作！");
			}
		}
		// 生成盘点单
		CountHeaderVO countHeaderVO = new CountHeaderVO();
		countHeaderVO.setCountBillNo(getCNo());
		while (super.count(Condition.getQueryWrapper(countHeaderVO)) > 0) {
			countHeaderVO.setCountBillNo(this.getCNo());
		}
		countHeaderVO.setCountBillState(StockCountStateEnum.CREATE_COUNT_DIFF.getIndex());
		countHeaderVO.setUserName(AuthUtil.getNickName());
		countHeaderVO.setCountTag(StockCountTypeEnum.RANDOM.getIndex());
		countHeaderVO.setWhId(countRecordList.get(0).getWhId());
		// 生成盘点单明细
		List<CountDetail> countDetailList = new ArrayList<>();
		for (CountRecord countRecord : countRecordList) {
			CountDetail countDetail = new CountDetail();
			countDetail.setLocId(countRecord.getLocId());
			countDetail.setLocCode(countRecord.getLocCode());
			countDetail.setLocState(LocStatusEnum.Complated.getIndex());
			countDetail.setProcTime(countRecord.getProcTime());
			countDetailList.add(countDetail);
		}
		countHeaderVO.setCountDetailList(countDetailList);
		// 存储盘点单
		if (!this.add(countHeaderVO)) {
			throw new ServiceException("创建盘点单头表信息失败");
		}
		// 修改盘点记录的盘点单ID、编码
		List<CountRecord> recordList = new ArrayList<>();
		for (CountRecord countRecord : countRecordList) {
			CountRecord item = new CountRecord();
			item.setWcrId(countRecord.getWcrId());
			item.setCountBillId(countHeaderVO.getCountBillId());
			item.setCountBillNo(countHeaderVO.getCountBillNo());
			item.setRecordState(CountRecordStateEnum.Complated.getIndex());
			recordList.add(item);
		}
		countRecordService.updateBatchById(recordList, countRecordList.size());
		// 完成盘点
//		this.completeTask(countHeaderVO.getCountBillId().toString());
		// 修改盘点单状态 = 生成差异报告
//		this.updateBillState(countHeaderVO, StockCountStateEnum.CREATE_COUNT_DIFF);
		List<CountReport> reportList = new ArrayList<>();
		Map<Long, Stock> reportStockMap = new HashMap<>();
		List<Long> skuIdList = new ArrayList<>();          // 记录已生成序列号盘亏报告的物品，避免重复生成
		List<String> serialNumberList = new ArrayList<>(); // 记录盘点记录中的序列号

		// 循环盘点记录，生成差异报告
		for (CountRecordVO countRecord : countRecordList) {
			// 验证是否存在重复的序列号
			if (serialNumberList.stream().filter(u -> u.equals(countRecord.getSerialNumber())).count() > 0) {
				throw new ServiceException("异常：序列号：" + countRecord.getSerialNumber() + " 同时存在多个库位！");
			}
			// 获取库位详细信息
			Location location = LocationCache.getById(countRecord.getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException("库存指定库位不存在(库位ID：" + countRecord.getLocId() + ")！");
			}
			// 查询库存(排除零库存)
			QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
			queryWrapper.lambda()
				.eq(Stock::getWhId, countHeaderVO.getWhId())
				.eq(Stock::getLocId, countRecord.getLocId())
				.eq(Stock::getSkuId, countRecord.getSkuId())
				.eq(Stock::getWspId, countRecord.getWspId())
				.apply("pick_qty <> stock_qty");
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				queryWrapper.eq("sku_lot" + i, countRecord.skuLotGet(i));
			}
			Stock stock = stockService.list(queryWrapper).stream().findFirst().orElse(null);
			// 获取物品信息
			Sku sku = SkuCache.getById(countRecord.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException("指定物品不存(物品ID：" + countRecord.getSkuId() + ")！");
			}
			// 获取该物品是否为序列号管理
			List<Serial> serialList = null;
			if (sku.getIsSn() == 1 && Func.isNotEmpty(stock)) {
				// 获取该物品所有库存的序列号
				Serial serialQuery = new Serial();
				serialQuery.setStockId(stock.getStockId());
				serialList = serialService.list(Condition.getQueryWrapper(serialQuery));
			}
			if (sku.getIsSn() == 1) {
				// 当前库存物品 = 序列号管理
				// 1. 查询该序列号是否已经存在，如果存在且库存ID不一致，需要对该库位进行盘亏处理
				Serial existSerial = serialService.list(Condition.getQueryWrapper(new Serial())
					.lambda()
					.eq(Serial::getSerialNumber, countRecord.getSerialNumber())
				).stream().findFirst().orElse(null);

				if (Func.isNotEmpty(existSerial) && Func.isNotEmpty(stock)
					&& !existSerial.getStockId().equals(stock.getStockId())) {
					// 生成盘亏差异
					Stock sourceStock = stockService.getById(existSerial.getStockId());
					CountReport countReport = CountReportWrapper.build().entity(
						countHeaderVO, countRecord, location, sku, sourceStock, existSerial.getSerialNumber());
					countReport.setWmsQty(BigDecimal.ONE);
					countReport.setCountQty(BigDecimal.ZERO);
					this.countReportService.save(countReport);
					reportList.add(countReport);
					reportStockMap.put(countReport.getWcrepId(), sourceStock);
				}
				// 2. 先循环存储库存中所有序列号盘亏的报告记录
				if (Func.isNotEmpty(serialList)) {
					if (skuIdList.stream().filter(skuId -> sku.getSkuId().equals(skuId)).count() == 0) {
						for (Serial serial : serialList) {
							CountReport countReport = CountReportWrapper.build().entity(
								countHeaderVO, countRecord, location, sku, stock, serial.getSerialNumber());
							countReport.setWmsQty(BigDecimal.ONE);
							countReport.setCountQty(BigDecimal.ZERO);
							countReport.setLpnCode(countRecord.getLpnCode());
							this.countReportService.save(countReport);
							reportList.add(countReport);
							reportStockMap.put(countReport.getWcrepId(), stock);
						}
						skuIdList.add(sku.getSkuId());
					}
				}
				// 3. 找到当前盘点记录对应的盘点报告
				//    存在：修改现有报告的盘点数量
				//    不存在：新增盘点盘盈的报告
				List<CountReport> filterList = this.countReportService.list(
					Condition.getQueryWrapper(new CountReport()).lambda()
						.eq(CountReport::getCountBillId, countHeaderVO.getCountBillId())
						.eq(CountReport::getStockId, countRecord.getStockId())
						.eq(CountReport::getSerialNumber, countRecord.getSerialNumber()));
				if (Func.isNotEmpty(filterList)) {
					filterList.forEach(report -> {
						reportList.stream().filter(countReport -> {
							return countReport.getWcrepId().equals(report.getWcrepId());
						}).forEach(countReport -> {
							countReport.setCountQty(BigDecimal.ONE);
						});
						report.setCountQty(BigDecimal.ONE);
						this.countReportService.updateById(report);
					});
				} else {
					CountReport countReport = CountReportWrapper.build().entity(
						countHeaderVO, countRecord, location, sku, stock, countRecord.getSerialNumber());
					countReport.setWmsQty(BigDecimal.ZERO);
					countReport.setCountQty(BigDecimal.ONE);
					countReport.setLpnCode(countRecord.getLpnCode());
					countReport.setSerialNumber(countRecord.getSerialNumber());
					this.countReportService.save(countReport);
					reportList.add(countReport);
					reportStockMap.put(countReport.getWcrepId(), stock);
				}
			} else {
				// 当前库存物品 = 非序列号管理
				CountReport countReport = CountReportWrapper.build().entity(
					countHeaderVO, countRecord, location, sku, stock, null);
				countReport.setCountQty(countRecord.getCountQty());
				countReport.setLpnCode(countRecord.getLpnCode());
				this.countReportService.save(countReport);
				reportList.add(countReport);
				reportStockMap.put(countReport.getWcrepId(), stock);
			}
			if (Func.isNotEmpty(countRecord.getSerialNumber())) {
				serialNumberList.add(countRecord.getSerialNumber());
			}
		}
		for (CountReport report : reportList) {
			// 删除没有差异的 盘点差异报告
			if (report.getWmsQty() == report.getCountQty()) {
				this.countReportService.removeById(report);
				continue;
			}
			// 创建系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.COUNT);
			systemProcParam.setDataType(DataTypeEnum.CountBillNo);
			systemProcParam.setAction(ActionEnum.COUNT_RANDOM);
			systemProcParam.setWhId(report.getWhId());
			systemProcParam.setBillNo(report.getCountBillNo());
//			systemProcParam.setLocId(report.getLocId());
//			systemProcParam.setSkuId(report.getSkuId());
			systemProcParam.setOperationQty(report.getCountQty());
			systemProcParam.setOperationUnit(report.getWsuName());
			systemProcParam.setLpnCode(report.getLpnCode());
			Stock stock = reportStockMap.getOrDefault(report.getWcrepId(), null);
			if (Func.isNotEmpty(stock)) {
//				systemProcParam.setBoxCode(stock.getBoxCode());
			}
			SystemProc systemProc = systemProcService.create(systemProcParam);
			// 盘点占用(盘亏的情况下才生成)
			if (report.getWmsQty().compareTo(report.getCountQty()) > 0) {
				StockOccupyDTO stockOccupyDTO = new StockOccupyDTO();
				stockOccupyDTO.setTransId(report.getCountBillId());
				stockOccupyDTO.setOccupyType(StockOccupyTypeEnum.Count.getIndex());
				stockOccupyDTO.setWhId(report.getWhId());
				if (Func.isNotEmpty(stock)) {
					stockOccupyDTO.setStockId(stock.getStockId());
				}
				stockOccupyDTO.setSkuId(report.getSkuId());
				stockOccupyDTO.setSkuCode(report.getSkuCode());
				stockOccupyDTO.setSkuName(report.getSkuName());
				stockOccupyDTO.setSystemProcId(systemProc.getSystemProcId());
				stockOccupyDTO.setOccupyTime(LocalDateTime.now());
				Sku sku = SkuCache.getById(report.getSkuId());
				if (sku.getIsSn() == 1) {
					stockOccupyDTO.setOccupyQty(BigDecimal.ONE);
				} else {
					stockOccupyDTO.setOccupyQty(report.getWmsQty().subtract(report.getCountQty()));
				}
				stockOccupyDTO.setWcrId(report.getWcrepId());
				stockOccupyDTO.setCreateUser(report.getCreateUser());
				stockOccupyDTO.setUpdateUser(report.getCreateUser());
				stockOccupyDTO.setSoBillNo(report.getCountBillNo());
				stockOccupyDTO.setSoBillId(report.getCountBillId());
				stockOccupyService.add(stockOccupyDTO);
			}
		}
//		CallbackManager.instance.countDiff(countHeaderVO.getCountBillNo());
		return true;
	}

	@Override
	public boolean reportCancel(List<Long> idList) {
		if (ObjectUtil.isEmpty(idList)) {
			return false;
		}
		for (Long id : idList) {
			CountHeader countHeader = super.getById(id);
			if (ObjectUtil.isEmpty(countHeader)) {
				throw new ServiceException("盘点单不存在（ID：" + id + "）！");
			}
			if (!countHeader.getCountBillState().equals(StockCountStateEnum.CREATE_COUNT_DIFF.getIndex())) {
				throw new ServiceException("只有生成差异报告的盘点单才允许取消差异！");
			}
			// 更新盘点单状态
			this.updateBillState(countHeader, StockCountStateEnum.INVALID);
			// 获取当前盘点单下生成的差异报告
			List<CountReport> reportList = countReportService.listByBillId(id);
			if (ObjectUtil.isEmpty(reportList)) {
				continue;
			}
			// 取消库位占用
			ILocationService locationService = SpringUtil.getBean(ILocationService.class);
			List<Location> locationList = locationService.list().stream().filter(u -> {
				return countHeader.getCountBillNo().equals(u.getCountBillNo());
			}).collect(Collectors.toList());
			if (Func.isNotEmpty(locationList)) {
				locationList.stream().forEach(location -> {
					locationService.updateCountBillNo(location.getLocId(), null);
				});
			}
			// 取消盘点占用
			StockOccupySubtractDTO occupyReduce = new StockOccupySubtractDTO();
			occupyReduce.setTransId(countHeader.getCountBillId());
			occupyReduce.setOccupyType(StockOccupyTypeEnum.Count.getIndex());
			for (CountReport report : reportList) {
				occupyReduce.setWcrId(report.getWcrepId());

				stockOccupyService.subtract(occupyReduce);
			}
		}
		return true;
	}

	@Override
	public String repeat(String ids) {
		BladeUser user = AuthUtil.getUser();
		if (Func.isEmpty(user)) {
			throw new ServiceException("登录超时，请重新登录！");
		}
		List<Long> idList = Func.toLongList(ids);
		List<CountHeader> countHeaderList = new ArrayList<>();
		for (Long id : idList) {
			CountHeader countHeader = super.getById(id);
			if (Func.isEmpty(countHeader)) {
				throw new ServiceException("盘点单不存在（ID：" + id + "）！");
			}
			if (Func.isEmpty(countHeader.getCountBillState())) {
				throw new ServiceException("盘点单状态异常（ID：" + id + "）！");
			}
			if (StockCountStateEnum.COUNT_COMPLATED.getIndex() != countHeader.getCountBillState().intValue()) {
				throw new ServiceException(
					"盘点单状态异常（只允许状态 = " + StockCountStateEnum.COUNT_COMPLATED.getName() + " 执行此操作！");
			}
			CountHeader newHeader = new CountHeader();
			newHeader.setCountBillNo(SerialNoCache.getCNo());
			// 循环验证订单号，保证单号的唯一性
			boolean exist = false;
			while (super.count(Condition.getQueryWrapper(newHeader)) > 0) {
				newHeader.setCountBillNo(SerialNoCache.getCNo());
			}
			newHeader.setWhId(countHeader.getWhId());
			newHeader.setCountBillState(StockCountStateEnum.CREATE.getIndex());
			newHeader.setCountRemark(countHeader.getCountRemark());
			newHeader.setCountTag(countHeader.getCountTag());
			newHeader.setCreator(user.getNickName());
			newHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
			newHeader.setProcTime(LocalDateTime.now());
			newHeader.setCreateTime(DateUtil.now());
			super.save(newHeader);
			// 获取原盘点单明细
			List<CountDetail> detailList = countDetailService.list(countHeader.getCountBillId());
			if (Func.isEmpty(detailList)) {
				throw new ServiceException("原盘点单明细为空（盘点单编码：" + countHeader.getCountBillNo() + "）！");
			}
			// 清除并修改原盘点单明细的重要信息，并存储
			detailList.forEach(detail -> {
				detail.setCountDetailId(null);
				detail.setCountBillId(newHeader.getCountBillId());
				detail.setCountBillNo(newHeader.getCountBillNo());
				detail.setLocState(LocStatusEnum.UnAllot.getIndex());
				detail.setTaskGroup(null);
				detail.setUserName(null);
				detail.setProcTime(null);
				detail.setCreateTime(null);
				detail.setCreateUser(newHeader.getCreateUser());
				detail.setCreateTime(null);
				detail.setUpdateTime(null);
				detail.setUpdateUser(null);
				countDetailService.save(detail);
			});
			countHeaderList.add(newHeader);
		}
		return NodesUtil.join(countHeaderList, "countBillNo");
	}

	/**
	 * 盘点单批量作废
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public boolean abolishCountHeader(String ids) {
		List<Long> countHeaderIdList = Func.toLongList(ids);
		List<CountHeader> countHeaderList = super.listByIds(countHeaderIdList);
		List<CountReport> countReportListAll = countReportService.list(Condition.getQueryWrapper(new CountReport())
			.lambda()
			.in(CountReport::getCountBillId, countHeaderIdList));
		for (CountHeader header : countHeaderList) {
			if (StockCountStateEnum.ADJUSTMENT.getIndex() != header.getCountBillState()) {
				this.updateBillState(header, StockCountStateEnum.INVALID);
				// 取消库位占用
				ILocationService locationService = SpringUtil.getBean(ILocationService.class);
				List<Location> locationList = locationService.list().stream().filter(u -> {
					return header.getCountBillNo().equals(u.getCountBillNo());
				}).collect(Collectors.toList());
				if (Func.isNotEmpty(locationList)) {
					locationList.stream().forEach(location -> {
						locationService.updateCountBillNo(location.getLocId(), null);
					});
				}
				List<CountReport> countReportList = countReportListAll.stream().filter(u -> {
					return u.getCountBillId().equals(header.getCountBillId());
				}).collect(Collectors.toList());
				for (CountReport countReport : countReportList) {
					StockOccupySubtractDTO stockOccupyReduceDTO = new StockOccupySubtractDTO();
					stockOccupyReduceDTO.setTransId(countReport.getCountBillId());
					stockOccupyReduceDTO.setOccupyType(StockOccupyTypeEnum.Count.getIndex());
					stockOccupyReduceDTO.setWcrId(countReport.getWcrepId());
					stockOccupyService.subtract(stockOccupyReduceDTO);
				}
				taskService.closeTask(header.getCountBillId(), TaskTypeEnum.Count);
			} else {
				throw new ServiceException(String.format(
					"单据[%s]状态为[%s]不能作废",
					header.getCountBillNo(), StockCountStateEnum.valueOf(header.getCountBillState())));
			}
		}
		return true;
	}

	/**
	 * 随机盘点作废
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public synchronized boolean abolishRandomCount(String ids) {
		String[] idList = ids.split(",");
		for (String id : idList) {
			CountRecord countRecord = new CountRecord();
			countRecord.setWcrId(Long.valueOf(id));
			countRecord.setRecordState(2);
			countRecordService.updateById(countRecord);
		}
		return true;
	}

	/**
	 * 修改盘点单状态
	 *
	 * @param countHeaderId       盘点单表头ID
	 * @param stockCountStateEnum 盘点单状态枚举
	 * @return 是否成功
	 */
	private boolean updateBillState(Long countHeaderId, StockCountStateEnum stockCountStateEnum) {
		CountHeader countHeader = super.getById(countHeaderId);
		if (Func.isEmpty(countHeader)) {
			throw new ServiceException("盘点单不存在(ID:" + countHeaderId + ")");
		}
		return updateBillState(countHeader, stockCountStateEnum);
	}

	/**
	 * 修改盘点单状态
	 *
	 * @param countHeader         盘点单表头
	 * @param stockCountStateEnum 盘点单状态枚举
	 * @return 是否成功
	 */
	@Override
	public boolean updateBillState(CountHeader countHeader, StockCountStateEnum stockCountStateEnum) {
		List<CountHeader> list = new ArrayList() {{
			add(countHeader);
		}};
		return updateBillState(list, stockCountStateEnum);
	}

	@Override
	public boolean cycleCount(String whId, String cycle, String countBy, String abc, String searchType) {
		CountHeaderVO countHeaderVO = new CountHeaderVO();
		countHeaderVO.setWhId(Func.toLong(whId));
		countHeaderVO.setCountBy(Func.toInt(countBy));
		countHeaderVO.setAbc(Func.toInt(abc));
		countHeaderVO.setSearchType(Func.toInt(searchType));

		List<CountHeaderVO> countHeaderList = this.selectLocation(countHeaderVO);/*.stream().filter(u -> {
			return Func.isEmpty(u.getLastLocCountDate()) || u.getLastLocCountDate().isBefore(LocalDateTime.parse(cycle));
		}).collect(Collectors.toList());*/
		if (Func.isEmpty(countHeaderList)) {
			return true;
		}
		CountHeaderVO countHeader = new CountHeaderVO();
		BeanUtil.copyProperties(countHeaderVO, countHeader);
		countHeader.setCountBillNo(this.getCNo());
		countHeader.setCountTag(CountTagEnum.OTHER.getIndex());
		countHeader.setCreator("系统");
		countHeader.setProcTime(LocalDateTime.now());
		countHeader.setCreateTime(DateUtil.now());
		countHeader.setCountRemark("循环盘点");
		List<CountDetail> countDetailList = new ArrayList<>();
		countHeaderList.forEach(u -> {
			countDetailList.add(BeanUtil.copy(u, CountDetail.class));
		});
		countHeader.setCountDetailList(countDetailList);

		return this.add(countHeader);
	}

	@Override
	public CountHeader getCountHeaderByNo(String billNo) {
		return baseMapper.getCountHeaderByNo(billNo);
	}

	/**
	 * 修改盘点单状态
	 *
	 * @param countHeaderList     盘点单表头集合
	 * @param stockCountStateEnum 盘点单状态枚举
	 * @return 是否成功
	 */
	public synchronized boolean updateBillState(Collection<CountHeader> countHeaderList,
												StockCountStateEnum stockCountStateEnum) {
		if (Func.isEmpty(countHeaderList)) {
			return false;
		}
		for (CountHeader countHeader : countHeaderList) {
			if (countHeader.getCountBillState() <= stockCountStateEnum.getIndex()) {
				continue;
			}
			throw new ServiceException(String.format(
				"盘点单状态不可逆向操作! 当前状态:%s, 期望修改状态:%s。",
				DictCache.getValue(DictConstant.STOCK_COUNT_STATE, countHeader.getCountBillState()),
				stockCountStateEnum.getName()));
		}
		List<Long> countBillIdList = NodesUtil.toList(countHeaderList, CountHeader::getCountBillId);
		if (Func.isEmpty(countBillIdList)) {
			return false;
		}
		// 修改盘点单状态
		UpdateWrapper<CountHeader> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(CountHeader::getCountBillState, stockCountStateEnum.getIndex())
			.in(CountHeader::getCountBillId, countBillIdList);

		return super.update(updateWrapper);
	}
}
