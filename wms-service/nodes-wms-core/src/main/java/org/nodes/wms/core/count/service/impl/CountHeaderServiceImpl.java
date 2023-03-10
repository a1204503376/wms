package org.nodes.wms.core.count.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.biz.count.StockCountBiz;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.common.enums.CreateTypeEnum;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.entity.CountReport;
import org.nodes.wms.core.count.enums.*;
import org.nodes.wms.core.count.mapper.CountHeaderMapper1;
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
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SyncStateEnum;
import org.nodes.wms.dao.stock.entities.Stock;
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
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.sku.enums.SnEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
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
 * ??????????????? ???????????????
 *
 * @author CHZ
 * @since 2020-01-02
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class CountHeaderServiceImpl<M extends CountHeaderMapper1, T extends CountHeader>
	extends BaseServiceImpl<CountHeaderMapper1, CountHeader>
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
	@Autowired
	private StockCountBiz stockCountBiz;


	@Override
	public IPage<CountHeader> selectCountHeaderPage(IPage<CountHeader> page, CountHeaderVO countHeader) {
		return page.setRecords(baseMapper.selectCountHeaderPage(page, countHeader));
	}

	//??????????????????
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
			throw new ServiceException("????????????????????????????????????");
		}
		List<Long> skuIds = NodesUtil.toList(countHeaderVO.getCountDetailList(), CountDetail::getSkuId);
		List<Long> locIds = NodesUtil.toList(countHeaderVO.getCountDetailList(), CountDetail::getLocId);
		if (CountByEnum.SKU.getIndex() == countHeaderVO.getCountBy()) {
			if (Func.isEmpty(skuIds)) {
				throw new ServiceException("???????????????skuId??????");
			}
			QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
			queryWrapper.lambda().in(Stock::getSkuId, skuIds)
				.eq(Stock::getStockStatus, StockStatusEnum.LOCK_FILL.getIndex());
			List<Stock> stocklist = stockService.list(queryWrapper);

			if (Func.isNotEmpty(stocklist)) {
				List<Sku> collect = skuService.listByIds(stocklist
					.stream().map(Stock::getSkuId).collect(Collectors.toList()));
				throw new ServiceException(String.format("?????????%s????????????????????????????????????????????????"
					, NodesUtil.join(collect, "skuCode")
				));
			}
			//??????????????????????????????
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailBySkuCodes(skuIds);
			if (Func.isNotEmpty(countDetails)) {
				List<CountDetail> collect = countDetails.stream().filter(countDetail -> locIds.contains(countDetail.getLocId())).collect(Collectors.toList());
				if (Func.isNotEmpty(collect)) {
					throw new ServiceException(String.format("?????????%s??????????????????%s???????????????????????????????????????"
						, NodesUtil.join(countDetails, "skuCode"),
						NodesUtil.join(collect, "countBillNo")
					));
				}
			}

		}
		if (CountByEnum.LOCATION.getIndex() == countHeaderVO.getCountBy()) {
			if (Func.isEmpty(locIds)) {
				throw new ServiceException("???????????????locId??????");
			}
			QueryWrapper<Location> queryWrapper = Condition.getQueryWrapper(new Location());
			queryWrapper.lambda().in(Location::getLocId, locIds)
				.eq(Location::getLockFlag, StringPool.N.toUpperCase());
			List<Location> list = locationService.list(queryWrapper);
			if (Func.isNotEmpty(list)) {
				throw new ServiceException(String.format("?????????%s??????????????????????????????????????????"
					, NodesUtil.join(list, "locCode")
				));
			}
			//??????????????????????????????
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailByLocCodes(locIds);
			if (Func.isNotEmpty(countDetails)) {
				throw new ServiceException(String.format("?????????%s??????????????????%s???????????????????????????????????????"
					, NodesUtil.join(countDetails, "locCode"),
					NodesUtil.join(countDetails, "countBillNo")
				));
			}

		}
		if (this.save(countHeaderVO)) {
			// ?????????????????????????????????????????????
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
					"??????[" + NodesUtil.join(locationList, "locCode") + "]????????????????????????????????????????????????");
			}
			// ??????????????????
			for (CountDetail countDetail : countHeaderVO.getCountDetailList()) {
				// ????????????
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
		//??????????????????
		CountHeader countHeaderGet = this.getOne(Condition.getQueryWrapper(countHeader));
		CountHeaderVO countHeaderVO = CountHeaderWrapper.build().entityVO(countHeaderGet);
		countHeaderVO.setCountBillNo(countHeaderGet.getCountBillNo());
		countHeaderVO.setCountTag(countHeaderGet.getCountTag());
		countHeaderVO.setCountRemark(countHeaderGet.getCountRemark());
		countHeaderVO.setCountBillId(countHeaderGet.getCountBillId());
		countHeaderVO.setCountBillState(countHeaderGet.getCountBillState());

		//??????????????????
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
			countDetailVO.setCountTagDesc(DictCache.getValue(DictCodeConstant.STOCK_COUNT_TYPE, countHeaderGet.getCountTag()));
			countDetailVO.setCountBillStateDesc(DictCache.getValue(DictCodeConstant.STOCK_COUNT_STATE, countHeaderGet.getCountBillState()));
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
			.eq(CountReport::getCountBillId, countHeaderGet.getCountBillId()));
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
		//??????????????????
		CountHeader countHeaderGet = baseMapper.selectOne(Wrappers.lambdaQuery(CountHeader.class)
			.eq(CountHeader::getCountBillNo, countHeader.getCountBillNo()));
		if (Func.isNull(countHeaderGet)) {
			throw new ServiceException("???????????????");
		}
		CountHeaderVO countHeaderVO = new CountHeaderVO();
		countHeaderVO.setCountBillNo(countHeaderGet.getCountBillNo());
		countHeaderVO.setCountTag(countHeaderGet.getCountTag());
		countHeaderVO.setCountRemark(countHeaderGet.getCountRemark());
		countHeaderVO.setCountBillId(countHeaderGet.getCountBillId());
		countHeaderVO.setCountBillState(countHeaderGet.getCountBillState());
		// ????????????????????????
		Warehouse warehouse = WarehouseCache.getById(countHeaderGet.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("???????????????");
		}

		//??????????????????
		CountDetail countDetail = new CountDetail();
		countDetail.setCountBillId(countHeaderGet.getCountBillId());
		//countDetail.setLocState(ELocStatus.UnComplate.getIndex());
		List<CountDetail> countDetailList = countDetailService.list(Condition.getQueryWrapper(countDetail));
		List<Long> skuIds = NodesUtil.toList(countDetailList, CountDetail::getSkuId);
		Map<String, String> sqlSkuLotMap = SkuLotUtil.createSqlSkuLotMap(CountSkuQtyVO.class, new CountSkuQtyVO());
		List<CountSkuQtyVO> countSkuQtyVOS = countDetailService.querySkuQty(sqlSkuLotMap, skuIds);
		countSkuQtyVOS.forEach(countSkuQtyVO -> countSkuQtyVO.setMode(countHeaderGet.getMode()));
		if (countDetailList.size() <= 0) {
			throw new ServiceException("????????????????????????????????????");
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
				//?????????????????????????????????????????????????????????????????????
				List<Stock> list = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getLocId, next.getKey())
					.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
					.apply("stock_qty - pick_qty > 0"));
				for (Stock stock : list) {
					//??????
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
		//????????????????????????????????????
		List<CountDetail> countDetailList = countDetailService.selectCountDetailList(countDetail);
		if (this.removeById(ids)) {
			//????????????
			for (CountDetail countDetaildelete : countDetailList) {
				locationService.updateCountBillNo(countDetaildelete.getLocId(), null);
			}
			countDetailService.removeByIdsNew(ids);

		}
		return true;
	}

	//??????????????????
	@Override
	public boolean countTask(CountHeaderVO countHeaderVO) {
		CountHeader countHeader = super.getById(countHeaderVO.getCountBillId());
		if (ObjectUtil.isEmpty(countHeader)) {
			throw new ServiceException("?????????????????????");
		}
		if (countHeader.getCountBillState() != StockCountStateEnum.CREATE.getIndex()) {
			throw new ServiceException("????????????????????????????????????");
		}
		BladeUser user = AuthUtil.getUser();
		CountDetail countDetail = new CountDetail();
		countDetail.setCountBillId(countHeader.getCountBillId());
		//????????????????????????????????????
		List<CountDetail> countDetailList = countDetailService.selectCountDetailList(countDetail);
		if (ObjectUtil.isEmpty(countHeaderVO.getCountTask())) {
			throw new RuntimeException("?????????????????????????????????");
		}
		if (countHeaderVO.getCountTask() <= 0) {
			throw new ServiceException("????????????????????????????????????");
		}
		if (countHeaderVO.getCountTask() > countDetailList.size()) {
			throw new ServiceException("?????????????????????????????????????????????");
		}
		//?????????taskPackage???
		TaskPackage taskPackage = new TaskPackage();
		taskPackage.setWhId(countHeader.getWhId());
		taskPackage.setTaskTypeCd(TaskTypeEnum.Count.getIndex());
		taskPackage.setTaskProcType(TaskProcTypeEnum.Cooperation.getIndex());
		taskPackage.setTaskDataId(countHeader.getCountBillId());
		taskPackage.setTtpCreateTime(LocalDateTime.now());
		if (!taskPackageService.save(taskPackage)) {
			throw new ServiceException("????????????????????????");
		}
		// ?????????????????????????????????
		int taskLocCount = countDetailList.size() / countHeaderVO.getCountTask();
		// ???????????????????????????????????????
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
		// ????????????
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
	 * ????????????
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
				throw new ServiceException("[" + countHeader.getCountBillNo() + "]???????????????????????????????????????????????????????????????");
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
			throw new ServiceException("????????????????????????????????????????????????");
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
	 * ??????????????????
	 *
	 * @param ids ?????????ID
	 * @return
	 */
	@Override
	public boolean differenceOrder(String ids) {
		List<Long> idList = Func.toLongList(ids);

		// ???????????????????????????
		List<CountHeader> countHeaderList = this.list(Condition.getQueryWrapper(new CountHeader())
			.lambda()
			.in(CountHeader::getCountBillId, idList));
		// ????????????????????????
		List<CountReport> countReportList = countReportService.list(Condition.getQueryWrapper(new CountReport())
			.lambda()
			.in(CountReport::getCountBillId, idList));

		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMinimumIntegerDigits(8);
		numberFormat.setGroupingUsed(false);

		// ?????????????????????????????????
		List<AsnHeaderVO> asnHeaderList = new ArrayList<>();
		// ?????????????????????????????????
		List<SoHeaderVO> soHeaderList = new ArrayList<>();
		List<Long> locationIdList = new ArrayList<>();
		for (CountHeader countHeader : countHeaderList) {

			if (countHeader.getCountBillState() != StockCountStateEnum.CREATE_COUNT_DIFF.getIndex()) {
				throw new ServiceException(String.format(
					"?????????[%s]??????????????????[%s] ??????????????????????????????",
					countHeader.getCountBillNo(), StockCountStateEnum.valueOf(countHeader.getCountBillState())));
			}
			// ??????????????????
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.COUNT);
			systemProcParam.setDataType(DataTypeEnum.CountBillNo);
			systemProcParam.setAction(ActionEnum.COUNT_DIFF_CREATE);
			systemProcParam.setBillNo(countHeader.getCountBillNo());
			systemProcParam.setWhId(countHeader.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);

			Warehouse warehouse = WarehouseCache.getById(countHeader.getWhId());
			if (Func.isEmpty(warehouse)) {
				throw new ServiceException(String.format("?????????:%s ????????????(ID:%s)????????????",
					countHeader.getCountBillNo(), countHeader.getWhId()));
			}
			// ???????????????????????????????????????
			List<CountReport> reportList = countReportList.stream().filter(u -> {
				return u.getCountBillId().equals(countHeader.getCountBillId())
					&& BigDecimalUtil.ne(u.getCountQty(), u.getWmsQty());
			}).collect(Collectors.toList());
			if (Func.isEmpty(reportList)) {
				throw new ServiceException("?????????:" + countHeader.getCountBillNo() + " ???????????????????????????");
			}
			// ??????????????? ??????ID ????????????
			Map<Long, List<CountReport>> woIdMap = new HashMap<>();
			for (CountReport report : reportList) {
				// ????????????????????????
				Sku sku = SkuCache.getById(report.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("???????????????(ID:" + report.getSkuId() + ")");
				}
				if (!woIdMap.containsKey(sku.getWoId())) {
					woIdMap.put(sku.getWoId(), new ArrayList<>());
				}
				woIdMap.get(sku.getWoId()).add(report);
				locationIdList.add(report.getLocId());
			}
			// ???????????????????????????
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			for (Long woId : woIdMap.keySet()) {
				Owner owner = ownerService.getById(woId);
				if (Func.isEmpty(owner)) {
					throw new ServiceException("?????????????????????(ID:" + woId + ")!");
				}
				List<CountReport> list = woIdMap.get(woId);
				// ???????????????????????? ??????????????? ??????????????????
				List<CountReport> instockReportList = list.stream().filter(u -> {
					return BigDecimalUtil.gt(u.getCountQty(), u.getWmsQty());
				}).collect(Collectors.toList());
				List<CountReport> outstockReportList = list.stream().filter(u -> {
					return BigDecimalUtil.gt(u.getWmsQty(), u.getCountQty());
				}).collect(Collectors.toList());

				// ???????????????
				if (Func.isNotEmpty(instockReportList)) {
					LocalDateTime now = LocalDateTime.now();
					// ???????????????????????????
					AsnHeaderVO asnHeader = new AsnHeaderVO();
					asnHeader.setWhId(countHeader.getWhId());
					asnHeader.setWoId(woId);
					asnHeader.setWhCode(warehouse.getWhCode());
					// ??????????????????????????????
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

					// ???????????????????????????
					for (CountReport countReport : instockReportList) {
						// ??????????????????????????????
						StockAddDTO stockProc = BeanUtil.copy(countReport, StockAddDTO.class);
						stockProc.setLotNumber(countReport.getCountLot());
						stockProc.setSystemProcId(systemProc.getSystemProcId());
						stockProc.setEventType(EventTypeEnum.Count);
						stockProc.setBillNo(countHeader.getCountBillNo());
						// ????????????????????????
						Sku sku = SkuCache.getById(countReport.getSkuId());
						if (Func.isEmpty(sku)) {
							throw new ServiceException("???????????????(ID:" + countReport.getSkuId() + ")");
						}
						// ?????????????????????????????????
						SkuPackage skuPackage = SkuPackageCache.getById(countReport.getWspId());
						if (Func.isEmpty(skuPackage)) {
							throw new ServiceException("?????????????????????????????????(ID:" + countReport.getWspId() + ")!");
						}
						// ??????????????????????????????
						SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
							skuPackage.getWspId(), SkuLevelEnum.Base.getIndex());
						if (Func.isEmpty(skuPackageDetail)) {
							throw new ServiceException("???????????????????????????????????????(????????????:" + skuPackage.getWspName() + ")");
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
						// ?????????????????????
						for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
							if (countReport.skuLotChk(i)) {
								asnDetail.skuLotSet(i, countReport.skuLotGet(i));
							}
						}
						if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
							// ??????????????????????????????????????????????????????????????????????????????????????????
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
						// ????????????
						stockService.add(stockProc);
						lineNo++;
					}
					asnHeaderList.add(asnHeader);

				}
				// ???????????????
				if (Func.isNotEmpty(outstockReportList)) {
					LocalDateTime now = LocalDateTime.now();
					// ???????????????????????????
					SoHeaderVO soHeader = new SoHeaderVO();
					soHeader.setWhId(countHeader.getWhId());
					soHeader.setWoId(woId);
					soHeader.setWhCode(warehouse.getWhCode());
					// ??????????????????????????????
					while (true) {
						soHeader.setSoBillNo(SoCache.getSoBillNo());
						int count = soHeaderService.count(Condition.getQueryWrapper(new SoHeader()).lambda()
							.eq(SoHeader::getSoBillNo, soHeader.getSoBillNo()));
						if (count == 0) {
							break;
						}
					}

					soHeader.setBillKey(countHeader.getCountBillNo());
					soHeader.setSoBillState(SoBillStateEnum.COMPLETED.getCode());
					soHeader.setBillTypeCd(ParamCache.getValue(ParamEnum.COUNT_LOSS_TYPECD.getKey()));
					soHeader.setBillKey(soHeader.getSoBillNo());
					soHeader.setTransportCode(SoBillTransportCodeEnum.SelfTaking.getIndex());
//					soHeader.setZoneType(ZoneTypeEnum.VIRTUAL.getIndex());
					soHeader.setOutstockType(OutstockTypeEnum.Normal.getIndex());
					soHeader.setCreateType(CreateTypeEnum.INNER.getIndex());
//					soHeader.setCCode(warehouse.getWhCode());
//					soHeader.setCName(warehouse.getWhName());
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

					// ???????????????????????????
					for (CountReport countReport : outstockReportList) {
						// ??????????????????????????????
						StockSubtractDTO stockReduce = BeanUtil.copy(countReport, StockSubtractDTO.class);
						stockReduce.setLotNumber(countReport.getCountLot());
						stockReduce.setSystemProcId(systemProc.getSystemProcId());
						stockReduce.setEventType(EventTypeEnum.Count);
						stockReduce.setDataId(countReport.getWcrepId());
						stockReduce.setBillNo(countHeader.getCountBillNo());
						// ????????????????????????
						Sku sku = SkuCache.getById(countReport.getSkuId());
						if (Func.isEmpty(sku)) {
							throw new ServiceException("???????????????(ID:" + countReport.getSkuId() + ")");
						}
						// ?????????????????????????????????
						SkuPackage skuPackage = SkuPackageCache.getById(countReport.getWspId());
						if (Func.isEmpty(skuPackage)) {
							throw new ServiceException("?????????????????????????????????(ID:" + countReport.getWspId() + ")!");
						}
						// ??????????????????????????????
						SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
							skuPackage.getWspId(), SkuLevelEnum.Base.getIndex());
						if (Func.isEmpty(skuPackageDetail)) {
							throw new ServiceException("???????????????????????????????????????(????????????:" + skuPackage.getWspName() + ")");
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
						soDetail.setBillDetailState(SoDetailStateEnum.Allocated.getCode());
						// ?????????????????????
						for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
							if (countReport.skuLotChk(i)) {
								soDetail.skuLotSet(i, countReport.skuLotGet(i));
							}
						}
						if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
							// ??????????????????????????????????????????????????????????????????????????????????????????
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
						// ????????????
						stockService.subtract(stockReduce);
						lineNo++;
					}
					soHeaderList.add(soHeader);
				}
			}
		}
		// ?????????????????????
		if (Func.isNotEmpty(asnHeaderList)) {
			// ??????????????????
			List<AsnHeader> headerList = new ArrayList<>();
			asnHeaderList.forEach(headerList::add);
			asnHeaderService.saveBatch(headerList, asnHeaderList.size());

			// ??????????????????
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
		// ?????????????????????
		if (Func.isNotEmpty(soHeaderList)) {
			// ??????????????????
			List<SoHeader> headerList = new ArrayList<>();
			soHeaderList.forEach(headerList::add);
			soHeaderService.saveBatch(headerList, soHeaderList.size());

			// ??????????????????
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
		// ????????????????????????
		this.locationService.updateCountBillNo(locationIdList, null);
		// ???????????????????????????
		return this.updateBillState(countHeaderList, StockCountStateEnum.ADJUSTMENT);
	}

	/**
	 * ?????????????????????
	 *
	 * @param ids ?????????ID
	 * @return
	 */
	@Override
	public boolean differenceReport(String ids) {
		List<CountHeader> countHeaderList = super.listByIds(Func.toLongList(ids));
		for (CountHeader countHeader : countHeaderList) {
			if (!countHeader.getCountBillState().equals(StockCountStateEnum.COUNT_COMPLATED.getIndex())) {
				throw new ServiceException("?????????????????????????????????????????????");
			}
			// ????????????????????? = ??????????????????
			this.updateBillState(countHeader, StockCountStateEnum.CREATE_COUNT_DIFF);
			// ?????????????????????????????????
			List<CountDetail> countDetailList = countDetailService.list(Condition.getQueryWrapper(new CountDetail())
				.lambda().eq(CountDetail::getCountBillId, countHeader.getCountBillId()));
			if (Func.isEmpty(countDetailList)) {
				throw new ServiceException("????????????" + countHeader.getCountBillNo() + " ????????????????????????????????????");
			}

			// ????????????????????????ID??????????????????
			Map<Long, Stock> reportStockMap = new HashMap<>();
			// ?????????????????????
			List<String> serialNumberList = new ArrayList<>();

			// ????????????????????????????????????
			for (CountDetail countDetail : countDetailList) {
				// ?????????????????????????????????
				List<CountReport> reportList = new ArrayList<>();
				// ????????????????????????
				Location location = LocationCache.getById(countDetail.getLocId());
				if (Func.isEmpty(location)) {
					throw new ServiceException("???????????????????????????(??????ID???" + countDetail.getLocId() + ")???");
				}
				// ???????????????????????????????????????
				List<CountRecord> countRecordList = countRecordService.list(
					Condition.getQueryWrapper(new CountRecord())
						.lambda()
						.eq(CountRecord::getCountBillId, countHeader.getCountBillId())
						.eq(CountRecord::getLocId, countDetail.getLocId()));
				// ??????????????????????????????
				List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getWhId, countHeader.getWhId())
					.eq(Stock::getLocId, countDetail.getLocId())
					.apply("pick_qty <> stock_qty"));
				// ??????????????????????????????????????????(??????)??????(???????????????????????????????????????????????????????????????????????????)
				stockList.stream().forEach(stock -> {
					// ??????????????????
					Sku sku = SkuCache.getById(stock.getSkuId());
					if (Func.isEmpty(sku)) {
						throw new ServiceException("???????????????????????????(??????ID???" + stock.getSkuId() + ")???");
					}
					if (Func.isNotEmpty(countDetail.getSkuId()) &&
						!Func.equals(countDetail.getSkuId(), sku.getSkuId())) {
						// ?????????????????????????????????????????????????????????
						return;
					}
					// ???????????????????????????????????????
					List<StockOccupy> stockOccupyList = this.stockOccupyService.list(
						Condition.getQueryWrapper(new StockOccupy()).lambda()
							.eq(StockOccupy::getStockId, stock.getStockId()));
					if (Func.isNotEmpty(stockOccupyList)) {
						throw new ServiceException(String.format(
							"????????????%s ????????????%s ???????????????????????????????????????????????????",
							location.getLocCode(), sku.getSkuName()));
					}
					// ?????????????????? = ???????????????
					List<Serial> serialList = null;
					if (sku.getIsSn() == 1) {
						// ???????????????????????????????????????
						Serial serialQuery = new Serial();
						serialQuery.setStockId(stock.getStockId());
						serialList = serialService.list(Condition.getQueryWrapper(serialQuery));
					}
					if (Func.isNotEmpty(serialList)) {
						// ?????????????????? = ???????????????
						for (Serial serial : serialList) {
							CountReport countReport = CountReportWrapper.build().entity(
								countHeader, null, location, sku, stock, serial.getSerialNumber());
							countReport.setWmsQty(BigDecimal.ONE);
							this.countReportService.save(countReport);
							reportList.add(countReport);
							reportStockMap.put(countReport.getWcrepId(), stock);
						}
					} else {
						// ?????????????????? = ??????????????????
						CountReport countReport = CountReportWrapper.build().entity(
							countHeader, null, location, sku, stock, null);
						this.countReportService.save(countReport);
						reportList.add(countReport);
						reportStockMap.put(countReport.getWcrepId(), stock);
					}
				});

				// ??????????????????????????? ?????? ?????? ????????????
				for (CountRecord countRecord : countRecordList) {
					// ????????????????????????????????????
					if (serialNumberList.stream().filter(u -> u.equals(countRecord.getSerialNumber())).count() > 0) {
						throw new ServiceException("?????????????????????" + countRecord.getSerialNumber() + " ???????????????????????????");
					}
					// ???????????????????????????????????????????????????
					List<CountReport> reportFilter = reportList.stream().filter(report -> {
						return
							Func.isNotEmpty(report.getStockId()) &&
								report.getStockId().equals(countRecord.getStockId()) &&
								((Func.isEmpty(report.getSerialNumber()) && Func.isEmpty(countRecord.getSerialNumber())) ||
									(Func.isNotEmpty(report.getSerialNumber()) &&
										report.getSerialNumber().equals(countRecord.getSerialNumber())));
					}).collect(Collectors.toList());
					// ????????????
					if (Func.isNotEmpty(reportFilter)) {
						// ????????????????????????????????????
						reportFilter.stream().forEach(report -> {
							report.setCountQty(countRecord.getCountQty());
							report.setCountLot(countRecord.getLotNumber());
							this.countReportService.saveOrUpdate(report);
						});
					} else {
						// ????????????????????????(??????)??????
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
				// ??????????????????(???????????? ????????????=???????????? ???????????????)
				reportList.stream().forEach(report -> {
					// ????????????????????? ??????????????????
					if (report.getWmsQty().equals(report.getCountQty())) {
//						this.countReportService.removeById(report);
						return;
					}
					// ??????????????????
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
					// ??????????????????(???????????????????????????)
					if (report.getWmsQty().compareTo(report.getCountQty()) > 0) {
						StockOccupyDTO stockOccupyDTO = new StockOccupyDTO();
//						stockOccupyDTO.setTransId(report.getCountBillId());
						stockOccupyDTO.setOccupyType(StockOccupyTypeEnum.Count.getIndex());
						stockOccupyDTO.setWhId(report.getWhId());
						if (Func.isNotEmpty(stock)) {
							stockOccupyDTO.setStockId(stock.getStockId());
						}
						stockOccupyDTO.setSkuId(report.getSkuId());
						stockOccupyDTO.setSkuCode(report.getSkuCode());
						stockOccupyDTO.setSkuName(report.getSkuName());
//						stockOccupyDTO.setSystemProcId(systemProc.getSystemProcId());
//						stockOccupyDTO.setOccupyTime(LocalDateTime.now());
						Sku sku = SkuCache.getById(report.getSkuId());
						if (sku.getIsSn() == 1) {
//							stockOccupyDTO.setOccupyQty(BigDecimal.ONE);
						} else {
//							stockOccupyDTO.setOccupyQty(report.getWmsQty().subtract(report.getCountQty()));
						}
//						stockOccupyDTO.setWcrId(report.getWcrepId());
						stockOccupyDTO.setCreateUser(report.getCreateUser());
						stockOccupyDTO.setUpdateUser(report.getCreateUser());
//						stockOccupyDTO.setSoBillNo(report.getCountBillNo());
//						stockOccupyDTO.setSoBillId(report.getCountBillId());
						stockOccupyService.add(stockOccupyDTO);
					}

				});
			}
//			CallbackManager.instance.countDiff(countHeader.getCountBillNo());
		}
		return true;
	}

	@Override
	public boolean generateDifference(String ids) {
		stockCountBiz.generateDifference(ids);
		return true;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param countRecordList ????????????????????????
	 * @return
	 */
	@Override
	public boolean randomCountDifferenceReport(List<CountRecordVO> countRecordList) {
		if (Func.isEmpty(countRecordList)) {
			throw new ServiceException("????????????????????????????????????????????????");
		}
		// ???????????????????????????????????????????????????
		List<Long> wcrIdList = NodesUtil.toList(countRecordList, CountRecord::getWcrId);
		if (Func.isNotEmpty(wcrIdList)) {
			Integer complated = countRecordService.count(new QueryWrapper<CountRecord>().lambda()
				.in(CountRecord::getWcrId, wcrIdList)
				.eq(CountRecord::getRecordState, CountRecordStateEnum.Complated.getIndex()));
			if (complated > 0) {
				throw new ServiceException("???????????????????????????????????????????????????????????????????????????????????????");
			}
		}
		// ???????????????
		CountHeaderVO countHeaderVO = new CountHeaderVO();
		countHeaderVO.setCountBillNo(getCNo());
		while (super.count(Condition.getQueryWrapper(countHeaderVO)) > 0) {
			countHeaderVO.setCountBillNo(this.getCNo());
		}
		countHeaderVO.setCountBillState(StockCountStateEnum.CREATE_COUNT_DIFF.getIndex());
		countHeaderVO.setUserName(AuthUtil.getNickName());
		countHeaderVO.setCountTag(StockCountTypeEnum.RANDOM.getIndex());
		countHeaderVO.setWhId(countRecordList.get(0).getWhId());
		// ?????????????????????
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
		// ???????????????
		if (!this.add(countHeaderVO)) {
			throw new ServiceException("?????????????????????????????????");
		}
		// ??????????????????????????????ID?????????
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
		// ????????????
//		this.completeTask(countHeaderVO.getCountBillId().toString());
		// ????????????????????? = ??????????????????
//		this.updateBillState(countHeaderVO, StockCountStateEnum.CREATE_COUNT_DIFF);
		List<CountReport> reportList = new ArrayList<>();
		Map<Long, Stock> reportStockMap = new HashMap<>();
		List<Long> skuIdList = new ArrayList<>();          // ??????????????????????????????????????????????????????????????????
		List<String> serialNumberList = new ArrayList<>(); // ?????????????????????????????????

		// ???????????????????????????????????????
		for (CountRecordVO countRecord : countRecordList) {
			// ????????????????????????????????????
			if (serialNumberList.stream().filter(u -> u.equals(countRecord.getSerialNumber())).count() > 0) {
				throw new ServiceException("?????????????????????" + countRecord.getSerialNumber() + " ???????????????????????????");
			}
			// ????????????????????????
			Location location = LocationCache.getById(countRecord.getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException("???????????????????????????(??????ID???" + countRecord.getLocId() + ")???");
			}
			// ????????????(???????????????)
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
			// ??????????????????
			Sku sku = SkuCache.getById(countRecord.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException("??????????????????(??????ID???" + countRecord.getSkuId() + ")???");
			}
			// ???????????????????????????????????????
			List<Serial> serialList = null;
			if (sku.getIsSn() == 1 && Func.isNotEmpty(stock)) {
				// ???????????????????????????????????????
				Serial serialQuery = new Serial();
				serialQuery.setStockId(stock.getStockId());
				serialList = serialService.list(Condition.getQueryWrapper(serialQuery));
			}
			if (sku.getIsSn() == 1) {
				// ?????????????????? = ???????????????
				// 1. ????????????????????????????????????????????????????????????ID????????????????????????????????????????????????
				Serial existSerial = serialService.list(Condition.getQueryWrapper(new Serial())
					.lambda()
					.eq(Serial::getSerialNumber, countRecord.getSerialNumber())
				).stream().findFirst().orElse(null);

				if (Func.isNotEmpty(existSerial) && Func.isNotEmpty(stock)
					&& !existSerial.getStockId().equals(stock.getStockId())) {
					// ??????????????????
					Stock sourceStock = stockService.getById(existSerial.getStockId());
					CountReport countReport = CountReportWrapper.build().entity(
						countHeaderVO, countRecord, location, sku, sourceStock, existSerial.getSerialNumber());
					countReport.setWmsQty(BigDecimal.ONE);
					countReport.setCountQty(BigDecimal.ZERO);
					this.countReportService.save(countReport);
					reportList.add(countReport);
					reportStockMap.put(countReport.getWcrepId(), sourceStock);
				}
				// 2. ????????????????????????????????????????????????????????????
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
				// 3. ?????????????????????????????????????????????
				//    ??????????????????????????????????????????
				//    ???????????????????????????????????????
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
				// ?????????????????? = ??????????????????
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
			// ????????????????????? ??????????????????
			if (report.getWmsQty() == report.getCountQty()) {
				this.countReportService.removeById(report);
				continue;
			}
			// ??????????????????
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
			// ????????????(???????????????????????????)
			if (report.getWmsQty().compareTo(report.getCountQty()) > 0) {
				StockOccupyDTO stockOccupyDTO = new StockOccupyDTO();
//				stockOccupyDTO.setTransId(report.getCountBillId());
				stockOccupyDTO.setOccupyType(StockOccupyTypeEnum.Count.getIndex());
				stockOccupyDTO.setWhId(report.getWhId());
				if (Func.isNotEmpty(stock)) {
					stockOccupyDTO.setStockId(stock.getStockId());
				}
				stockOccupyDTO.setSkuId(report.getSkuId());
				stockOccupyDTO.setSkuCode(report.getSkuCode());
				stockOccupyDTO.setSkuName(report.getSkuName());
//				stockOccupyDTO.setSystemProcId(systemProc.getSystemProcId());
//				stockOccupyDTO.setOccupyTime(LocalDateTime.now());
				Sku sku = SkuCache.getById(report.getSkuId());
				if (sku.getIsSn() == 1) {
//					stockOccupyDTO.setOccupyQty(BigDecimal.ONE);
				} else {
//					stockOccupyDTO.setOccupyQty(report.getWmsQty().subtract(report.getCountQty()));
				}
//				stockOccupyDTO.setWcrId(report.getWcrepId());
				stockOccupyDTO.setCreateUser(report.getCreateUser());
				stockOccupyDTO.setUpdateUser(report.getCreateUser());
//				stockOccupyDTO.setSoBillNo(report.getCountBillNo());
//				stockOccupyDTO.setSoBillId(report.getCountBillId());
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
				throw new ServiceException("?????????????????????ID???" + id + "??????");
			}
			if (!countHeader.getCountBillState().equals(StockCountStateEnum.CREATE_COUNT_DIFF.getIndex())) {
				throw new ServiceException("????????????????????????????????????????????????????????????");
			}
			// ?????????????????????
			this.updateBillState(countHeader, StockCountStateEnum.INVALID);
			// ?????????????????????????????????????????????
			List<CountReport> reportList = countReportService.listByBillId(id);
			if (ObjectUtil.isEmpty(reportList)) {
				continue;
			}
			// ??????????????????
			ILocationService locationService = SpringUtil.getBean(ILocationService.class);
			List<Location> locationList = locationService.list().stream().filter(u -> {
				return countHeader.getCountBillNo().equals(u.getCountBillNo());
			}).collect(Collectors.toList());
			if (Func.isNotEmpty(locationList)) {
				locationList.stream().forEach(location -> {
					locationService.updateCountBillNo(location.getLocId(), null);
				});
			}
			// ??????????????????
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
			throw new ServiceException("?????????????????????????????????");
		}
		List<Long> idList = Func.toLongList(ids);
		List<CountHeader> countHeaderList = new ArrayList<>();
		for (Long id : idList) {
			CountHeader countHeader = super.getById(id);
			if (Func.isEmpty(countHeader)) {
				throw new ServiceException("?????????????????????ID???" + id + "??????");
			}
			if (Func.isEmpty(countHeader.getCountBillState())) {
				throw new ServiceException("????????????????????????ID???" + id + "??????");
			}
			if (StockCountStateEnum.COUNT_COMPLATED.getIndex() != countHeader.getCountBillState().intValue()) {
				throw new ServiceException(
					"??????????????????????????????????????? = " + StockCountStateEnum.COUNT_COMPLATED.getName() + " ??????????????????");
			}
			CountHeader newHeader = new CountHeader();
			newHeader.setCountBillNo(SerialNoCache.getCNo());
			// ????????????????????????????????????????????????
			boolean exist = false;
			while (super.count(Condition.getQueryWrapper(newHeader)) > 0) {
				newHeader.setCountBillNo(SerialNoCache.getCNo());
			}
			newHeader.setWhId(countHeader.getWhId());
			newHeader.setCountBillState(org.nodes.wms.dao.count.enums.StockCountStateEnum.CREATE.getCode());
			newHeader.setCountRemark(countHeader.getCountRemark());
			newHeader.setCountTag(countHeader.getCountTag());
			newHeader.setCreator(user.getNickName());
			newHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
			newHeader.setProcTime(LocalDateTime.now());
			newHeader.setCreateTime(DateUtil.now());
			super.save(newHeader);
			// ????????????????????????
			List<CountDetail> detailList = countDetailService.list(countHeader.getCountBillId());
			if (Func.isEmpty(detailList)) {
				throw new ServiceException("?????????????????????????????????????????????" + countHeader.getCountBillNo() + "??????");
			}
			// ????????????????????????????????????????????????????????????
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
	 * ?????????????????????
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
				// ??????????????????
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
					"??????[%s]?????????[%s]????????????",
					header.getCountBillNo(), StockCountStateEnum.valueOf(header.getCountBillState())));
			}
		}
		return true;
	}

	/**
	 * ??????????????????
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
	 * ?????????????????????
	 *
	 * @param countHeaderId       ???????????????ID
	 * @param stockCountStateEnum ?????????????????????
	 * @return ????????????
	 */
	private boolean updateBillState(Long countHeaderId, StockCountStateEnum stockCountStateEnum) {
		CountHeader countHeader = super.getById(countHeaderId);
		if (Func.isEmpty(countHeader)) {
			throw new ServiceException("??????????????????(ID:" + countHeaderId + ")");
		}
		return updateBillState(countHeader, stockCountStateEnum);
	}

	/**
	 * ?????????????????????
	 *
	 * @param countHeader         ???????????????
	 * @param stockCountStateEnum ?????????????????????
	 * @return ????????????
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
		countHeader.setCreator("??????");
		countHeader.setProcTime(LocalDateTime.now());
		countHeader.setCreateTime(DateUtil.now());
		countHeader.setCountRemark("????????????");
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
	 * ?????????????????????
	 *
	 * @param countHeaderList     ?????????????????????
	 * @param stockCountStateEnum ?????????????????????
	 * @return ????????????
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
				"?????????????????????????????????! ????????????:%s, ??????????????????:%s???",
				DictCache.getValue(DictCodeConstant.STOCK_COUNT_STATE, countHeader.getCountBillState()),
				stockCountStateEnum.getName()));
		}
		List<Long> countBillIdList = NodesUtil.toList(countHeaderList, CountHeader::getCountBillId);
		if (Func.isEmpty(countBillIdList)) {
			return false;
		}
		// ?????????????????????
		UpdateWrapper<CountHeader> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(CountHeader::getCountBillState, stockCountStateEnum.getIndex())
			.in(CountHeader::getCountBillId, countBillIdList);

		return super.update(updateWrapper);
	}
}
