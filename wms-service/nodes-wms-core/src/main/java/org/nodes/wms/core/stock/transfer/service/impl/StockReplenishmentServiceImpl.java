package org.nodes.wms.core.stock.transfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.entity.RelHeader;
import org.nodes.wms.core.relenishment.enums.RelStateEnum;
import org.nodes.wms.core.relenishment.service.IRelDetailService;
import org.nodes.wms.core.relenishment.service.IRelHeaderService;
import org.nodes.wms.core.relenishment.vo.RelDetailVo;
import org.nodes.wms.core.relenishment.wrapper.RelDetailWrapper;
import org.nodes.wms.core.stock.core.dto.StockMoveDTO;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.dto.StockReplenishmentSubmitDTO;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.StockReplenishmentVO;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.enums.TransferBillStateEnum;
import org.nodes.wms.core.stock.transfer.enums.TransferTypeEnum;
import org.nodes.wms.core.stock.transfer.service.*;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * ?????????????????????
 *
 * @Author zx
 * @Date 2020/8/4
 **/
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockReplenishmentServiceImpl implements IStockReplenishmentService {

	@Autowired
	ITaskService taskService;
	@Autowired
	ITransferHeaderService transferHeaderService;
	@Autowired
	ITransferDetailService transferDetailService;
	@Autowired
	IStockService stockService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	ISkuLotService skuLotService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ITransferRecordService transferRecordService;
	@Autowired
	ITransferDetailItemService transferDetailItemService;
	@Autowired
	IOutstockService outstockService;
	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	private IRelDetailService relDetailService;
	@Autowired
	private IZoneService zoneService;
	@Autowired
	private IRelHeaderService relHeaderService;
	/**
	 * ??????-????????????????????????
	 *
	 * @param taskId
	 * @return
	 */
	@Override
	public StockReplenishmentVO getReplenishmenInfo(String taskId) {
		//??????????????????
		Task task = taskService.getById(taskId);
		if (Func.isEmpty(task)) return null;
		//??????????????????
		QueryWrapper<RelDetail> tdqw = new QueryWrapper<>();
		tdqw.lambda().eq(RelDetail::getRelBillId, task.getBillId());
		List<RelDetail> relDetails = relDetailService.list(tdqw);
		if (Func.isEmpty(relDetails)) throw new ServiceException("??????????????????????????????");

		List<RelDetail> collect = relDetails.stream()
			.filter(relDetail -> !relDetail.getRelStatus().equals(RelStateEnum.EXECUTED.getIndex()))
			.collect(Collectors.toList());
		List<RelDetailVo> relDetailVos = RelDetailWrapper.build().listVO(collect);
		for (RelDetailVo relDetailVo : relDetailVos) {
			List<Zone> zoneList = zoneService.list(Wrappers.lambdaQuery(Zone.class)
				.eq(Zone::getWhId, relDetailVo.getFromWhId()));
			relDetailVo.setZoneList(zoneList);
			List<SkuLotConfigVO> skuLotLabelList = skuLotService.getConfig(relDetailVo.getSkuId());
			if (Func.isNotEmpty(skuLotLabelList)) {
				for (SkuLotConfigVO skuLotLabelVO : skuLotLabelList) {
					Map<String, Object> skuLotMap = new HashMap<>();
					try {
						skuLotMap = SkuLotWrapper.skuLotToMap1(relDetailVo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Object skuLot = skuLotMap.get("skuLot" + skuLotLabelVO.getSkuLotIndex());
					if (Func.isNotEmpty(skuLot)){
						skuLotLabelVO.setSkuLotValue(skuLot.toString());
					}
				}
			}
			relDetailVo.setSkuLotValList(skuLotLabelList);
		}
		//???????????????????????????
		StockReplenishmentVO stockReplenishmentVO = new StockReplenishmentVO();
		//????????????
		stockReplenishmentVO.setDetailList(relDetailVos);
		//???????????????
		stockReplenishmentVO.setFinish(relDetails.size() - collect.size());
		//??????
		stockReplenishmentVO.setTotal(relDetails.size());

//		//????????????
//		QueryWrapper<Stock> sqw = new QueryWrapper<>();
//		sqw.lambda().in(Stock::getStockId, NodesUtil.toList(transferDetailList, "stockId", Long.class));
//		List<Stock> stockList = stockService.list(sqw);
//
//		List<StockReplenishmentDetailVO> detailList = new ArrayList<>();
//		for (TransferDetail transferDetail : transferDetailList) {
//			//?????????????????????????????????
//			StockReplenishmentDetailVO stockReplenishmentDetailVO =
//				BeanUtil.copy(transferDetail, StockReplenishmentDetailVO.class);
//
//			List<TransferDetailItem> transferDetailItemList = transferDetailItemService
//				.list(Condition.getQueryWrapper(new TransferDetailItem()).lambda()
//					.eq(TransferDetailItem::getTransferDetailId, transferDetail.getTransferDetailId()));
//			if (Func.isNotEmpty(transferDetailItemList)) {
//				SoHeader soHeader = BeanUtil.copy(soHeaderService.getById(transferDetailItemList.get(0).getBillId()),SoHeader.class);
//				SoDetail soDetail = BeanUtil.copy(soDetailService.getById(transferDetailItemList.get(0).getDetailId()),SoDetail.class);
//				Sku sku = SkuCache.getById(transferDetail.getSkuId());
//				OutstockExecuteDTO execute = outstockService.execute(soHeader, soDetail, sku);
//				if (Func.isNotEmpty(execute.getLocationList()))
//					stockReplenishmentDetailVO
//						.setLocationList(NodesUtil.toList(execute.getLocationList(), "locCode", String.class));
//			}
//
//			//??????
//			Location location = LocationCache.getById(transferDetail.getSourceLocId());
//			LocationCache.validData(location);
//			stockReplenishmentDetailVO.setSourceLocCode(location.getLocCode());
//			//??????
//			SkuPackage skuPackage = SkuPackageCache.getById(transferDetail.getWspId());
//			if (Func.isNotEmpty(skuPackage))
//				stockReplenishmentDetailVO.setSkuSpec(skuPackage.getSpec());
//			//?????????
//			List<SkuLotConfigVO> skuLotLabelList;
//				skuLotLabelList = skuLotService.getConfig(transferDetail.getSkuId());
//				if (Func.isNotEmpty(skuLotLabelList)) {
//					for (SkuLotConfigVO skuLotLabelVO : skuLotLabelList) {
//						List<Stock> stocks = stockList.stream()
//							.filter(stock -> stock.getStockId().equals(transferDetail.getStockId()))
//							.collect(Collectors.toList());
//						if (Func.isNotEmpty(stocks)) {
//							Stock stock = stocks.get(0);
//							Map<String, Object> skuLotMap = null;
//							try {
//								skuLotMap = SkuLotWrapper.skuLotToMap(stock);
//							} catch (IllegalAccessException e) {
//								e.printStackTrace();
//							}
//							Object skuLot = skuLotMap.get("skuLot" + skuLotLabelVO.getSkuLotIndex());
//							if (Func.isNotEmpty(skuLot))
//								skuLotLabelVO.setSkuLotValue(skuLot.toString());
//						}
//					}
//				}
//				stockReplenishmentDetailVO.setSkuLotValList(skuLotLabelList);
//			//??????????????????????????? ???????????????????????????
//			if (stockReplenishmentDetailVO.getPlanQty().compareTo(stockReplenishmentDetailVO.getTransQty()) > 0)
//				detailList.add(stockReplenishmentDetailVO);
//		}


		return stockReplenishmentVO;
	}

	/**
	 * ??????-??????????????????
	 *
	 * @param skuCode
	 * @return
	 */
	@Override
	public List<RelDetailVo> getSkuListByCode(String skuCode, String taskId) {
		List<RelDetailVo> relDetailVos = new ArrayList<>();
		//??????????????????
		Task task = taskService.getById(taskId);
		if (Func.isEmpty(task)) throw new ServiceException("??????????????????");
		//??????????????????
		QueryWrapper<RelDetail> tdqw = new QueryWrapper<>();
		tdqw.lambda().eq(RelDetail::getSkuCode, skuCode)
			.eq(RelDetail::getRelBillId, task.getBillId());
		List<RelDetail> detailList = relDetailService.list(tdqw);
		if (Func.isEmpty(detailList)) throw new ServiceException("??????????????????????????????");
		//????????????id,??????id,??????id??????
		List<RelDetail> relDetails = detailList.stream()
			.collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
				new ConcurrentSkipListSet<>(Comparator.comparing(RelDetail::getWspId)
					.thenComparing(RelDetail::getSkuId).thenComparing(RelDetail::getWoId))), ArrayList::new));
		if (Func.isNotEmpty(relDetails)) {
			for (RelDetail relDetail : relDetails) {
				RelDetailVo relDetailVo =
					BeanUtil.copy(relDetail, RelDetailVo.class);
				//????????????
				if (Func.isNotEmpty(relDetailVo.getWoId())) {
					IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
					Owner owner = ownerService.getById(relDetailVo.getWoId());
					if (Func.isNotEmpty(owner)) {
						relDetailVo.setOwnerName(owner.getOwnerName());
					}
				}
				//??????
				SkuPackage skuPackage = SkuPackageCache.getById(relDetailVo.getWspId());
				if (Func.isNotEmpty(skuPackage)) {
					relDetailVo.setSkuSpec(skuPackage.getSpec());
					relDetailVo.setWspName(skuPackage.getWspName());
				}
				relDetailVos.add(relDetailVo);
			}
		}
		return relDetailVos;
	}

	/**
	 * ?????? ??????????????????
	 *
	 * @param stockReplenishmentSubmitDTO
	 * @return
	 */
	@Override
	public synchronized boolean submitReplenishmen(StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO) {
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getRelDetailId()))
			throw new ServiceException("??????????????????ID????????????!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getSkuId()))
			throw new ServiceException("??????ID????????????!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getWspId()))
			throw new ServiceException("??????ID????????????!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getTargetLocCode()))
			throw new ServiceException("??????????????????????????????!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getRealQty()))
			throw new ServiceException("????????????????????????!");

		RelDetail relDetail = relDetailService.getOne(Wrappers.lambdaQuery(RelDetail.class)
			.eq(RelDetail::getRelDetailId, stockReplenishmentSubmitDTO.getRelDetailId()));
		if (Func.isEmpty(relDetail))
			throw new ServiceException("????????????????????????????????????");
		Location targetLocation = LocationCache.getByCode(stockReplenishmentSubmitDTO.getTargetLocCode());
		LocationCache.validData(targetLocation);
		Location sourceLocation = LocationCache.getById(relDetail.getFromLocId());
		LocationCache.validData(sourceLocation);
		if (!targetLocation.getWhId().equals(sourceLocation.getWhId())) {
			throw new ServiceException("????????????????????????????????????????????????");
		}
		if (!stockReplenishmentSubmitDTO.getTargetZoneId().equals(relDetail.getToZoneId())) {
			throw new ServiceException("?????????????????????????????????????????????");
		}
		if (!targetLocation.getLocId().equals(relDetail.getToLocId())) {
			throw new ServiceException("?????????????????????????????????????????????");
		}
		BigDecimal add = stockReplenishmentSubmitDTO.getRealQty().add(relDetail.getRealQty());
		if (BigDecimalUtil.gt(add,relDetail.getRelQty())) {
			throw new ServiceException("???????????????????????????????????????");
		}
		relDetail.setRealQty(add);
		if(BigDecimalUtil.eq(add,relDetail.getRelQty())){
			relDetail.setRelStatus(RelStateEnum.EXECUTED.getIndex());
		}else{
			relDetail.setRelStatus(RelStateEnum.EXECUTING.getIndex());
		}
		relDetailService.updateById(relDetail);
		//????????????????????????
		taskService.updateBeginTime(relDetail.getRelBillNo(), TaskTypeEnum.Replenish);
		//?????????????????? ???????????????
		this.updateRelHeaderState(relDetail);
		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setSourceLocId(relDetail.getFromLocId()); //?????????
		stockMoveDTO.setTargetLocId(relDetail.getToLocId()); //????????????
		stockMoveDTO.setSkuId(relDetail.getSkuId()); //??????id
		stockMoveDTO.setMoveQty(stockReplenishmentSubmitDTO.getRealQty().multiply(new BigDecimal(relDetail.getConvertQty()))); //??????
		stockMoveDTO.setEventType(EventTypeEnum.Move);
		stockMoveDTO.setSystemProcId(this.saveMoveSystemLog(stockReplenishmentSubmitDTO, relDetail)); //????????????
		this.saveTransferRecord(stockReplenishmentSubmitDTO, relDetail);//????????????
		stockService.stockMove(stockMoveDTO);
		StockOccupySubtractDTO stockOccupyReduceDTO = new StockOccupySubtractDTO();
		stockOccupyReduceDTO.setTransId(relDetail.getRelBillId());
		stockOccupyReduceDTO.setOccupyType(StockOccupyTypeEnum.Replenish.getIndex());
		stockOccupyReduceDTO.setQty(stockReplenishmentSubmitDTO.getRealQty().multiply(new BigDecimal(relDetail.getConvertQty())));
		stockOccupyService.subtract(stockOccupyReduceDTO);
		return true;
	}

	/**
	 * ??????????????????
	 *
	 * @param relDetail
	 * @return
	 */
	private boolean updateRelHeaderState(RelDetail relDetail) {
		if (Func.isEmpty(relHeaderService.getById(relDetail.getRelBillId())))
			throw new ServiceException("????????????????????????????????????");
		String sql = "rel_qty > real_qty";
		//????????????????????????
		QueryWrapper<RelDetail> tdqw = new QueryWrapper<>();
		tdqw.lambda().eq(RelDetail::getRelBillId, relDetail.getRelBillId())
			.apply(sql);
		List<RelDetail> relDetailList = relDetailService.list(tdqw);
		//?????????????????? ??????????????????
		RelHeader relHeader = new RelHeader();
		relHeader.setRelBillId(relDetail.getRelBillId());
		if (Func.isEmpty(relDetailList)) {
			relHeader.setRelBillState(RelStateEnum.EXECUTED.getIndex());
			//?????????????????????
			taskService.closeTask(relDetail.getRelBillId(), TaskTypeEnum.Replenish);
		} else {
			relHeader.setRelBillState(TransferBillStateEnum.Executing.getIndex());
		}
		relHeaderService.updateById(relHeader);
		return true;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param stockReplenishmentSubmitDTO
	 * @return
	 */
	private Long saveMoveSystemLog(StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO,
								   RelDetail relDetail) {
		// ??????????????????
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.TASK);
		systemProcParam.setAction(ActionEnum.REPLENISH_SUBMIT);
		systemProcParam.setBillNo(relDetail.getRelBillNo());
		systemProcParam.setWhId(relDetail.getFromWhId()); //??????
		systemProcParam.setOperationQty(stockReplenishmentSubmitDTO.getRealQty()); //??????
		systemProcParam.setOperationUnit(relDetail.getUmName()); //??????
		SystemProc systemProc = systemProcService.create(systemProcParam);
		return systemProc.getSystemProcId();
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	private boolean saveTransferRecord(StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO, RelDetail relDetail) {

		TransferRecord transferRecord = new TransferRecord();
		transferRecord.setTransferDetailId(relDetail.getRelDetailId());
		transferRecord.setTransferCode(relDetail.getRelBillNo());
		transferRecord.setTransferType(TransferTypeEnum.REPLENISH.getIndex());
		transferRecord.setWoId(relDetail.getWoId());// ?????????
		transferRecord.setFromLpn(relDetail.getFromLpnCode()); // ?????????

		Location sLocation = LocationCache.getValid(relDetail.getFromLocId());
		transferRecord.setFromLocCode(sLocation.getLocCode());//???????????????
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone sourceZone = zoneService.getById(sLocation.getZoneId());
		if (Func.isNotEmpty(sourceZone)) {
			transferRecord.setFromZoneCode(sourceZone.getZoneCode());// ???????????????
			transferRecord.setFromZoneName(sourceZone.getZoneName());// ???????????????
		}
		transferRecord.setFromLocId(relDetail.getFromLocId());
		transferRecord.setToLocId(relDetail.getToLocId());
		transferRecord.setWsuCode(relDetail.getUmCode());
		transferRecord.setWsuName(relDetail.getUmName());//???????????????
		transferRecord.setSkuLevel(relDetail.getSkuLevel());//?????????
		transferRecord.setSkuId(relDetail.getSkuId());//????????????
		transferRecord.setToLpn(relDetail.getToLpnCode()); // ??????????????????
		Location tLocation = LocationCache.getValid(stockReplenishmentSubmitDTO.getTargetLocCode());
		transferRecord.setToLocCode(tLocation.getLocCode());//??????????????????
		Zone targetZone = zoneService.getById(tLocation.getZoneId());
		if (Func.isNotEmpty(targetZone)) {
			transferRecord.setToZoneCode(targetZone.getZoneCode());//??????????????????
			transferRecord.setToZoneName(targetZone.getZoneName());//??????????????????
		}
		transferRecord.setQty(stockReplenishmentSubmitDTO.getRealQty());//????????????
		transferRecord.setWspId(relDetail.getWspId());//????????????

		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (!relDetail.skuLotChk(i)) {
				continue;
			}
			transferRecord.skuLotSet(i, relDetail.skuLotGet(i));
		}
		return transferRecordService.save(transferRecord);
	}


	/**
	 * ??????-??????????????????
	 * @param taskId
	 * @return
	 */
	@Override
	public StockReplenishmentVO getRelRecords(String taskId){
		StockReplenishmentVO stockReplenishmentVO=new StockReplenishmentVO();
		List<RelDetailVo> detailList = new ArrayList<>();
		//??????????????????
		Task task = taskService.getById(taskId);
		if (Func.isEmpty(task)) throw new ServiceException("??????????????????");
		List<TransferRecord> list = transferRecordService.list(Wrappers.lambdaQuery(TransferRecord.class)
			.eq(TransferRecord::getTransferCode, task.getBillNo()));
		list.forEach(transferRecord->{
			RelDetail relDetail = relDetailService.getById(transferRecord.getTransferDetailId());
			RelDetailVo detailVo = BeanUtil.copy(relDetail, RelDetailVo.class);
			detailVo.setFromLocName(transferRecord.getFromLocCode());
			SkuPackage skuPackage = SkuPackageCache.getById(transferRecord.getWspId());
			detailVo.setSkuSpec(skuPackage.getSpec());
			detailVo.setRealQty(transferRecord.getQty());
			detailList.add(detailVo);
		});
		stockReplenishmentVO.setDetailList(detailList);
		return stockReplenishmentVO;
	}

}
