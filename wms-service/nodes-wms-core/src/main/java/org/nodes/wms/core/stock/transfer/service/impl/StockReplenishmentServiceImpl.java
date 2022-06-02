package org.nodes.wms.core.stock.transfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
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
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.ZoneCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.enums.TransferBillStateEnum;
import org.nodes.wms.core.stock.transfer.enums.TransferTypeEnum;
import org.nodes.wms.core.stock.transfer.service.*;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
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
 * 补货服务实现类
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
	 * 补货-获得补货明细信息
	 *
	 * @param taskId
	 * @return
	 */
	@Override
	public StockReplenishmentVO getReplenishmenInfo(String taskId) {
		//获得任务信息
		Task task = taskService.getById(taskId);
		if (Func.isEmpty(task)) return null;
		//补货明细列表
		QueryWrapper<RelDetail> tdqw = new QueryWrapper<>();
		tdqw.lambda().eq(RelDetail::getRelBillId, task.getBillId());
		List<RelDetail> relDetails = relDetailService.list(tdqw);
		if (Func.isEmpty(relDetails)) throw new ServiceException("没有指定补货任务明细");

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
		//返回的补货任务信息
		StockReplenishmentVO stockReplenishmentVO = new StockReplenishmentVO();
		//补货明细
		stockReplenishmentVO.setDetailList(relDetailVos);
		//完成任务数
		stockReplenishmentVO.setFinish(relDetails.size() - collect.size());
		//总数
		stockReplenishmentVO.setTotal(relDetails.size());

//		//库存列表
//		QueryWrapper<Stock> sqw = new QueryWrapper<>();
//		sqw.lambda().in(Stock::getStockId, NodesUtil.toList(transferDetailList, "stockId", Long.class));
//		List<Stock> stockList = stockService.list(sqw);
//
//		List<StockReplenishmentDetailVO> detailList = new ArrayList<>();
//		for (TransferDetail transferDetail : transferDetailList) {
//			//返回的明细列表中的对象
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
//			//库位
//			Location location = LocationCache.getById(transferDetail.getSourceLocId());
//			LocationCache.validData(location);
//			stockReplenishmentDetailVO.setSourceLocCode(location.getLocCode());
//			//规格
//			SkuPackage skuPackage = SkuPackageCache.getById(transferDetail.getWspId());
//			if (Func.isNotEmpty(skuPackage))
//				stockReplenishmentDetailVO.setSkuSpec(skuPackage.getSpec());
//			//批属性
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
//			//返回的补货明细列表 只有未完成的才返回
//			if (stockReplenishmentDetailVO.getPlanQty().compareTo(stockReplenishmentDetailVO.getTransQty()) > 0)
//				detailList.add(stockReplenishmentDetailVO);
//		}


		return stockReplenishmentVO;
	}

	/**
	 * 补货-获得物品列表
	 *
	 * @param skuCode
	 * @return
	 */
	@Override
	public List<RelDetailVo> getSkuListByCode(String skuCode, String taskId) {
		List<RelDetailVo> relDetailVos = new ArrayList<>();
		//获得任务信息
		Task task = taskService.getById(taskId);
		if (Func.isEmpty(task)) throw new ServiceException("没有指定任务");
		//补货明细列表
		QueryWrapper<RelDetail> tdqw = new QueryWrapper<>();
		tdqw.lambda().eq(RelDetail::getSkuCode, skuCode)
			.eq(RelDetail::getRelBillId, task.getBillId());
		List<RelDetail> detailList = relDetailService.list(tdqw);
		if (Func.isEmpty(detailList)) throw new ServiceException("没有指定补货任务明细");
		//通过物品id,包装id,货主id去重
		List<RelDetail> relDetails = detailList.stream()
			.collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
				new ConcurrentSkipListSet<>(Comparator.comparing(RelDetail::getWspId)
					.thenComparing(RelDetail::getSkuId).thenComparing(RelDetail::getWoId))), ArrayList::new));
		if (Func.isNotEmpty(relDetails)) {
			for (RelDetail relDetail : relDetails) {
				RelDetailVo relDetailVo =
					BeanUtil.copy(relDetail, RelDetailVo.class);
				//货主名称
				if (Func.isNotEmpty(relDetailVo.getWoId())) {
					IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
					Owner owner = ownerService.getById(relDetailVo.getWoId());
					if (Func.isNotEmpty(owner)) {
						relDetailVo.setOwnerName(owner.getOwnerName());
					}
				}
				//规格
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
	 * 补货 提交补货信息
	 *
	 * @param stockReplenishmentSubmitDTO
	 * @return
	 */
	@Override
	public synchronized boolean submitReplenishmen(StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO) {
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getRelDetailId()))
			throw new ServiceException("补货任务明细ID不能为空!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getSkuId()))
			throw new ServiceException("物品ID不能为空!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getWspId()))
			throw new ServiceException("包装ID不能为空!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getTargetLocCode()))
			throw new ServiceException("目标库位编码不能为空!");
		if (Func.isEmpty(stockReplenishmentSubmitDTO.getRealQty()))
			throw new ServiceException("补货数量不能为空!");

		RelDetail relDetail = relDetailService.getOne(Wrappers.lambdaQuery(RelDetail.class)
			.eq(RelDetail::getRelDetailId, stockReplenishmentSubmitDTO.getRelDetailId()));
		if (Func.isEmpty(relDetail))
			throw new ServiceException("补货任务不存在或者已完成");
		Location targetLocation = LocationCache.getByCode(stockReplenishmentSubmitDTO.getTargetLocCode());
		LocationCache.validData(targetLocation);
		Location sourceLocation = LocationCache.getById(relDetail.getFromLocId());
		LocationCache.validData(sourceLocation);
		if (!targetLocation.getWhId().equals(sourceLocation.getWhId())) {
			throw new ServiceException("目标库位与原库位不属于同一个库房");
		}
		if (!stockReplenishmentSubmitDTO.getTargetZoneId().equals(relDetail.getToZoneId())) {
			throw new ServiceException("目标库区和计划目标库区不匹配！");
		}
		if (!targetLocation.getLocId().equals(relDetail.getToLocId())) {
			throw new ServiceException("目标库位和计划目标库位不匹配！");
		}
		BigDecimal add = stockReplenishmentSubmitDTO.getRealQty().add(relDetail.getRealQty());
		if (BigDecimalUtil.gt(add,relDetail.getRelQty())) {
			throw new ServiceException("补货数量超出计划补货数量！");
		}
		relDetail.setRealQty(add);
		if(BigDecimalUtil.eq(add,relDetail.getRelQty())){
			relDetail.setRelStatus(RelStateEnum.EXECUTED.getIndex());
		}else{
			relDetail.setRelStatus(RelStateEnum.EXECUTING.getIndex());
		}
		relDetailService.updateById(relDetail);
		//更改任务开始时间
		taskService.updateBeginTime(relDetail.getRelBillNo(), TaskTypeEnum.Replenish);
		//更改头表状态 并关闭任务
		this.updateRelHeaderState(relDetail);
		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setSourceLocId(relDetail.getFromLocId()); //原库位
		stockMoveDTO.setTargetLocId(relDetail.getToLocId()); //目标库位
		stockMoveDTO.setSkuId(relDetail.getSkuId()); //物品id
		stockMoveDTO.setMoveQty(stockReplenishmentSubmitDTO.getRealQty().multiply(new BigDecimal(relDetail.getConvertQty()))); //数量
		stockMoveDTO.setEventType(EventTypeEnum.Move);
		stockMoveDTO.setSystemProcId(this.saveMoveSystemLog(stockReplenishmentSubmitDTO, relDetail)); //系统日志
		this.saveTransferRecord(stockReplenishmentSubmitDTO, relDetail);//移动日志
		stockService.stockMove(stockMoveDTO);
		StockOccupySubtractDTO stockOccupyReduceDTO = new StockOccupySubtractDTO();
		stockOccupyReduceDTO.setTransId(relDetail.getRelBillId());
		stockOccupyReduceDTO.setOccupyType(StockOccupyTypeEnum.Replenish.getIndex());
		stockOccupyReduceDTO.setQty(stockReplenishmentSubmitDTO.getRealQty().multiply(new BigDecimal(relDetail.getConvertQty())));
		stockOccupyService.subtract(stockOccupyReduceDTO);
		return true;
	}

	/**
	 * 更改头表状态
	 *
	 * @param relDetail
	 * @return
	 */
	private boolean updateRelHeaderState(RelDetail relDetail) {
		if (Func.isEmpty(relHeaderService.getById(relDetail.getRelBillId())))
			throw new ServiceException("补货任务不存在或者已完成");
		String sql = "rel_qty > real_qty";
		//查询头表完成进度
		QueryWrapper<RelDetail> tdqw = new QueryWrapper<>();
		tdqw.lambda().eq(RelDetail::getRelBillId, relDetail.getRelBillId())
			.apply(sql);
		List<RelDetail> relDetailList = relDetailService.list(tdqw);
		//不存在则完成 存在则进行中
		RelHeader relHeader = new RelHeader();
		relHeader.setRelBillId(relDetail.getRelBillId());
		if (Func.isEmpty(relDetailList)) {
			relHeader.setRelBillState(RelStateEnum.EXECUTED.getIndex());
			//完成则关闭任务
			taskService.closeTask(relDetail.getRelBillId(), TaskTypeEnum.Replenish);
		} else {
			relHeader.setRelBillState(TransferBillStateEnum.Executing.getIndex());
		}
		relHeaderService.updateById(relHeader);
		return true;
	}

	/**
	 * 保存补货的系统日志
	 *
	 * @param stockReplenishmentSubmitDTO
	 * @return
	 */
	private Long saveMoveSystemLog(StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO,
								   RelDetail relDetail) {
		// 创建系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.TASK);
		systemProcParam.setAction(ActionEnum.REPLENISH_SUBMIT);
		systemProcParam.setBillNo(relDetail.getRelBillNo());
		systemProcParam.setWhId(relDetail.getFromWhId()); //库房
		systemProcParam.setOperationQty(stockReplenishmentSubmitDTO.getRealQty()); //数量
		systemProcParam.setOperationUnit(relDetail.getUmName()); //单位
		SystemProc systemProc = systemProcService.create(systemProcParam);
		return systemProc.getSystemProcId();
	}

	/**
	 * 保存移动记录
	 *
	 * @return
	 */
	private boolean saveTransferRecord(StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO, RelDetail relDetail) {

		TransferRecord transferRecord = new TransferRecord();
		transferRecord.setTransferDetailId(relDetail.getRelDetailId());
		transferRecord.setTransferCode(relDetail.getRelBillNo());
		transferRecord.setTransferType(TransferTypeEnum.REPLENISH.getIndex());
		transferRecord.setWoId(relDetail.getWoId());// 货主从
		transferRecord.setFromLpn(relDetail.getFromLpnCode()); // 源容器

		Location sLocation = LocationCache.getValid(relDetail.getFromLocId());
		transferRecord.setFromLocCode(sLocation.getLocCode());//原库位编码
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone sourceZone = zoneService.getById(sLocation.getZoneId());
		if (Func.isNotEmpty(sourceZone)) {
			transferRecord.setFromZoneCode(sourceZone.getZoneCode());// 原库区编码
			transferRecord.setFromZoneName(sourceZone.getZoneName());// 原库区名称
		}
		transferRecord.setFromLocId(relDetail.getFromLocId());
		transferRecord.setToLocId(relDetail.getToLocId());
		transferRecord.setWsuCode(relDetail.getUmCode());
		transferRecord.setWsuName(relDetail.getUmName());//原计量单位
		transferRecord.setSkuLevel(relDetail.getSkuLevel());//原层级
		transferRecord.setSkuId(relDetail.getSkuId());//目标货品
		transferRecord.setToLpn(relDetail.getToLpnCode()); // 目标容器编码
		Location tLocation = LocationCache.getValid(stockReplenishmentSubmitDTO.getTargetLocCode());
		transferRecord.setToLocCode(tLocation.getLocCode());//目标库位编码
		Zone targetZone = zoneService.getById(tLocation.getZoneId());
		if (Func.isNotEmpty(targetZone)) {
			transferRecord.setToZoneCode(targetZone.getZoneCode());//目标库区编码
			transferRecord.setToZoneName(targetZone.getZoneName());//目标库区名称
		}
		transferRecord.setQty(stockReplenishmentSubmitDTO.getRealQty());//目标数量
		transferRecord.setWspId(relDetail.getWspId());//目的包装

		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (!relDetail.skuLotChk(i)) {
				continue;
			}
			transferRecord.skuLotSet(i, relDetail.skuLotGet(i));
		}
		return transferRecordService.save(transferRecord);
	}


	/**
	 * 补货-查询补货记录
	 * @param taskId
	 * @return
	 */
	@Override
	public StockReplenishmentVO getRelRecords(String taskId){
		StockReplenishmentVO stockReplenishmentVO=new StockReplenishmentVO();
		List<RelDetailVo> detailList = new ArrayList<>();
		//获得任务信息
		Task task = taskService.getById(taskId);
		if (Func.isEmpty(task)) throw new ServiceException("没有指定任务");
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
