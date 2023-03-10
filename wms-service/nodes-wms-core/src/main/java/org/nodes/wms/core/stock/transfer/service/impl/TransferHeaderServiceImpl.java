package org.nodes.wms.core.stock.transfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.dto.NeedSkuDetailQueryDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.vo.NeedSkuDetailVO;
import org.nodes.wms.core.outstock.so.vo.NeedSkuVO;
import org.nodes.wms.core.outstock.so.vo.SoDetailVO;
import org.nodes.wms.core.outstock.so.wrapper.SoDetailWrapper;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.transfer.dto.ReplenishTaskDTO;
import org.nodes.wms.core.stock.transfer.entity.TransferDetail;
import org.nodes.wms.core.stock.transfer.entity.TransferDetailItem;
import org.nodes.wms.core.stock.transfer.entity.TransferHeader;
import org.nodes.wms.core.stock.transfer.enums.TransferBillStateEnum;
import org.nodes.wms.core.stock.transfer.enums.TransferBillTypeEnum;
import org.nodes.wms.core.stock.transfer.mapper.TransferHeaderMapper;
import org.nodes.wms.core.stock.transfer.service.ITransferDetailItemService;
import org.nodes.wms.core.stock.transfer.service.ITransferDetailService;
import org.nodes.wms.core.stock.transfer.service.ITransferHeaderService;
import org.nodes.wms.core.strategy.dto.OutstockExecuteDTO;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * ?????????????????? ???????????????
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TransferHeaderServiceImpl<M extends TransferHeaderMapper, T extends TransferHeader>
	extends BaseServiceImpl<TransferHeaderMapper, TransferHeader>
	implements ITransferHeaderService {

	@Autowired
	ITaskService taskService;
	@Autowired
	IStockService stockService;
	@Autowired
	IOutstockService outstockService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ITransferDetailService transferDetailService;
	@Autowired
	ITransferDetailItemService transferDetailItemService;
	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;

	@Override
	public boolean createReplenishTask(List<ReplenishTaskDTO> replenishTaskList) {

		// ????????????????????????????????????
		List<TransferHeader> transferHeaderList = super.list(Condition.getQueryWrapper(new TransferHeader())
			.lambda()
			.ne(TransferHeader::getBillState, TransferBillStateEnum.Complated.getIndex())
			.ne(TransferHeader::getBillState, TransferBillStateEnum.Cancel.getIndex()));
		List<TransferDetail> transferDetailList = null;
		if (Func.isNotEmpty(transferHeaderList)) {
			transferDetailList = transferDetailService.list(Condition.getQueryWrapper(new TransferDetail()).lambda()
				.in(TransferDetail::getTransferBillId,
					NodesUtil.toList(transferHeaderList, TransferHeader::getTransferBillId)));
		} else {
			transferDetailList = new ArrayList<>();
		}
		List<TransferDetail> finalTransferDetailList = transferDetailList;
		// ???????????? ??????ID ??????
		replenishTaskList.stream().collect(Collectors.groupingBy(ReplenishTaskDTO::getWhId))
			.forEach((whId, list) -> {
				// ????????????????????????
				Warehouse warehouse = WarehouseCache.getById(whId);
				if (Func.isEmpty(warehouse)) {
					throw new ServiceException("????????????????????????????????????[ID:" + whId + "]");
				}
				// ????????????????????????
				TransferHeader header = new TransferHeader();
				header.setWhId(warehouse.getWhId());
				header.setWhCode(warehouse.getWhCode());
				header.setWhName(warehouse.getWhName());
				header.setBillType(TransferBillTypeEnum.Replenish.getIndex());
				header.setBillState(TransferBillStateEnum.Create.getIndex());
				super.save(header);
				AtomicReference<Integer> transferDetailCount = new AtomicReference<>(0);
				// ??????????????????
				SystemProcDTO systemProcParam = new SystemProcDTO();
				systemProcParam.setProcType(SystemProcTypeEnum.TASK);
				systemProcParam.setDataType(DataTypeEnum.TransferBillId);
				systemProcParam.setAction(ActionEnum.REPLENISH_CREATE);
				systemProcParam.setBillNo(String.valueOf(header.getTransferBillId()));
				systemProcParam.setWhId(header.getWhId());
				SystemProc systemProc = systemProcService.create(systemProcParam);
				// ????????????????????????
				for (ReplenishTaskDTO replenishTask : list) {
					if (Func.isEmpty(replenishTask.getRepQty())) {
						throw new ServiceException("???????????????????????????");
					}
					if (replenishTask.getRepQty().compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}
					Sku sku = SkuCache.getById(replenishTask.getSkuId());
					if (Func.isEmpty(sku)) {
						throw new ServiceException("????????????????????????????????????[ID:" + replenishTask.getSkuId() + "]");
					}
					List<Long> soBillIdList = Func.toLongList(replenishTask.getSoBillIdsStr());
					// ?????????????????????????????????
					if (Func.isNotEmpty(finalTransferDetailList)) {
						// ???????????????????????????????????????
						List<TransferDetailItem> transferDetailItemList = transferDetailItemService.list(
							Condition.getQueryWrapper(new TransferDetailItem()).lambda()
								.in(TransferDetailItem::getTransferDetailId,
									NodesUtil.toList(finalTransferDetailList, TransferDetail::getTransferDetailId))
								.in(TransferDetailItem::getBillId, soBillIdList)
						);
						soBillIdList.removeAll(NodesUtil.toList(transferDetailItemList, TransferDetailItem::getBillId));
					}
					if (Func.isEmpty(soBillIdList)) {
						continue;
					}
					// ?????????????????????
					List<SoHeader> soHeaderList = soHeaderService.listByIds(soBillIdList);
					// ???????????????????????????
					List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
						.lambda()
						.in(SoDetail::getSoBillId, soBillIdList)
						.eq(SoDetail::getSkuId, replenishTask.getSkuId())
						.eq(SoDetail::getWspId, replenishTask.getWspId()));
					// ?????????????????????ID????????????????????????
					soDetailList.stream().collect(Collectors.groupingBy(SoDetail::getSoBillId))
						.forEach((soBillId, detailList) -> {
							SoHeader soHeader = soHeaderList.stream().filter(item -> {
								return item.getSoBillId().equals(soBillId);
							}).findFirst().orElse(null);
							if (Func.isEmpty(soHeader)) {
								throw new ServiceException("??????????????????????????????" + soBillId);
							}
							// ???????????????????????????????????????????????????????????????
							for (SoDetail soDetail : detailList) {
								if (replenishTask.getRepQty().compareTo(BigDecimal.ZERO) == 0) {
									break;
								}
								SoDetailVO soDetailVO = SoDetailWrapper.build().entityVO(soDetail);
								OutstockExecuteDTO execute = outstockService.execute(
									soHeader,
									soDetail,
									sku);
								// ???????????????????????????????????????????????????????????????
								QueryWrapper<Stock> stockQueryWrapper = Condition.getQueryWrapper(new Stock());
								stockQueryWrapper
									.func(item -> {
										for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
											if (soDetailVO.skuLotChk(i)) {
												// ???????????????????????????????????????
												item.eq("sku_lot" + i, soDetailVO.skuLotGet(i));
											}
										}
									})
									.lambda()
									.eq(Stock::getWhId, replenishTask.getWhId())
									.eq(Stock::getSkuId, replenishTask.getSkuId())
									.eq(Stock::getWspId, replenishTask.getWspId());
								stockQueryWrapper.apply(
									"stock_qty - pick_qty - occupy_qty  > 0");
								if (Func.isNotEmpty(execute.getStockList())) {
									stockQueryWrapper.lambda().notIn(
										Stock::getStockId,
										NodesUtil.toList(execute.getStockList(), Stock::getStockId));
								}
								List<Stock> stockList = stockService.list(stockQueryWrapper);
								if (Func.isEmpty(stockList)) {
									// ???????????????????????????????????????
									continue;
								}
								// ??????????????????????????????
								for (Stock stock : stockList) {
									if (replenishTask.getRepQty().compareTo(BigDecimal.ZERO) <= 0) {
										break;
									}
									// ??????????????????
									BigDecimal qty = stock.getStockQty()
										.subtract(stock.getPickQty())
										.subtract(stock.getOccupyQty());
									if (qty.compareTo(BigDecimal.ZERO) <= 0) {
										continue;
									}
									// ??????????????????????????????
									BigDecimal repQty = replenishTask.getRepQty();
									if (repQty.compareTo(soDetail.getPlanQty()) > 0) {
										if (soDetail.getPlanQty().compareTo(qty) <= 0) {
											repQty = soDetail.getPlanQty();
										} else {
											repQty = qty;
										}
									} else {
										if (repQty.compareTo(qty) > 0) {
											repQty = qty;
										}
									}
									transferDetailService.saveOrUpdate(
										header,
										stock,
										soDetail,
										repQty,
										systemProc.getSystemProcId());
									replenishTask.setRepQty(replenishTask.getRepQty().subtract(repQty));
									transferDetailCount.getAndSet(transferDetailCount.get() + 1);
								}
							}
						});
				}
				// ??????????????? ??????=0 ???????????????
				if (transferDetailCount.get() == 0) {
					super.removeById(header);
				} else {
					// ????????????
//					taskService.create(null, header.getTransferBillId(), TaskTypeEnum.Replenish);
				}
			});

		return true;
	}

	@Override
	public List<NeedSkuVO> getNeedSku() {
		List<NeedSkuVO> needSkuList = new ArrayList<>();
		// ?????????????????????????????????
		List<SoHeader> soHeaderList = soHeaderService.list(Condition.getQueryWrapper(new SoHeader()).lambda()
			.eq(SoHeader::getSoBillState, SoBillStateEnum.CREATE.getCode())
		);
		// ????????????????????????????????????????????????
		List<TransferHeader> transferHeaderList = super.list(
			Condition.getQueryWrapper(new TransferHeader()).lambda()
				.ne(TransferHeader::getBillState, TransferBillStateEnum.Complated.getIndex())
				.ne(TransferHeader::getBillState, TransferBillStateEnum.Cancel.getIndex()));
		List<TransferDetailItem> transferDetailItemList = null;
		if (Func.isNotEmpty(transferHeaderList)) {
			List<TransferDetail> transferDetailList = transferDetailService.list(
				Condition.getQueryWrapper(new TransferDetail()).lambda()
					.in(TransferDetail::getTransferBillId,
						NodesUtil.toList(transferHeaderList, TransferHeader::getTransferBillId))
			);
			if (Func.isNotEmpty(transferDetailList)) {
				transferDetailItemList = transferDetailItemService.list(
					Condition.getQueryWrapper(new TransferDetailItem()).lambda()
						.in(TransferDetailItem::getTransferDetailId,
							NodesUtil.toList(transferDetailList, TransferDetail::getTransferDetailId))
				);
			}
		} else {
			transferDetailItemList = new ArrayList<>();
		}
		// ??????????????????
		if (Func.isNotEmpty(soHeaderList)) {
			List<Long> soBillIdList = NodesUtil.toList(soHeaderList, SoHeader::getSoBillId);
			List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
				.in(SoDetail::getSoBillId, soBillIdList)
				.orderByAsc(SoDetail::getSoBillId)
			);
			// ?????????????????????????????????????????????
			for (SoDetail soDetail : soDetailList) {
				// ?????????????????????????????????????????????
				Long exist = transferDetailItemList.stream().filter(item -> {
					return item.getDetailId().equals(soDetail.getSoDetailId());
				}).count();
				if (exist > 0) {
					continue;
				}
				// ??????????????????
				SoHeader soHeader = soHeaderList.stream().filter(item -> {
					return item.getSoBillId().equals(soDetail.getSoBillId());
				}).findFirst().orElse(null);
				String soBillIdStr = soHeader.getSoBillId().toString();
				if (Func.isEmpty(soHeader)) {
					continue;
				}
				Warehouse warehouse = WarehouseCache.getById(soHeader.getWhId());
				if (Func.isEmpty(warehouse)) {
					continue;
				}
				Sku sku = SkuCache.getById(soDetail.getSkuId());
				if (Func.isEmpty(sku)) {
					continue;
				}
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(sku.getWoId());
				if (Func.isEmpty(owner)) {
					continue;
				}
				SkuPackage skuPackage = SkuPackageCache.getById(soDetail.getWspId());
				if (Func.isEmpty(skuPackage)) {
					continue;
				}
				// ??????????????????????????????
				OutstockExecuteDTO outstockExecute = this.outstockService.execute(
					soHeader,
					soDetail,
					sku);
				BigDecimal sourceStockQty = BigDecimal.ZERO;
				if (Func.isNotEmpty(outstockExecute.getStockList())) {
					// ????????????????????????
					List<Stock> sourceStockList = outstockExecute.getStockList();
					BigDecimal totalStockQty = sourceStockList.stream().map(Stock::getStockQty)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
					BigDecimal totalPickQty = sourceStockList.stream().map(Stock::getPickQty)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
					BigDecimal totalOccupyQty = sourceStockList.stream().map(Stock::getOccupyQty)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
					sourceStockQty = totalStockQty.subtract(totalPickQty)
						.subtract(totalOccupyQty);
				}
				SoDetailVO soDetailVO = SoDetailWrapper.build().entityVO(soDetail);
				// ????????????????????????????????????
				List<Stock> allStockList = stockService.list(Condition.getQueryWrapper(new Stock())
					.func(item -> {
						for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
							if (soDetailVO.skuLotChk(i)) {
								// ???????????????????????????????????????
								item.eq("sku_lot" + i, soDetailVO.skuLotGet(i));
							}
						}
					})
					.lambda()
					.eq(Stock::getSkuId, sku.getSkuId())
					.eq(Stock::getWspId, soDetail.getWspId())
					.apply("stock_qty != pick_qty"));
				BigDecimal totalStockQty = allStockList.stream().map(Stock::getStockQty)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal totalPickQty = allStockList.stream().map(Stock::getPickQty)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal totalOccupyQty = allStockList.stream().map(Stock::getOccupyQty)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal stockQty = totalStockQty.subtract(totalPickQty)
					.subtract(totalOccupyQty);
				final BigDecimal finalSourceStockQty = sourceStockQty;
				final BigDecimal finalStockQty = stockQty;
				NeedSkuVO needSku = needSkuList.stream().filter(item -> {
					return item.getWhId().equals(soHeader.getWhId()) &&
						item.getSourceZoneQty().equals(finalSourceStockQty) &&
						item.getTotalStockQty().equals(finalStockQty) &&
						item.getSkuId().equals(soDetail.getSkuId()) &&
						item.getWspId().equals(soDetail.getWspId()) &&
						item.skuLotCompare(soDetailVO);
				}).findFirst().orElse(null);
				if (Func.isEmpty(needSku)) {
					// ?????????????????????
					needSku = new NeedSkuVO();
					needSku.setWhId(warehouse.getWhId());
					needSku.setWhName(warehouse.getWhName());
					needSku.setWoId(sku.getWoId());
					needSku.setOwnerName(owner.getOwnerName());
					needSku.setSkuId(sku.getSkuId());
					needSku.setSkuCode(sku.getSkuCode());
					needSku.setSkuName(sku.getSkuName());
					needSku.setWspId(skuPackage.getWspId());
					needSku.setWspName(skuPackage.getWspName());
					needSku.setSpec(skuPackage.getSpec());
					needSku.setBillCount(1);
					needSku.setWsuName(soDetail.getBaseUmName());
					needSku.setTotalNeedQty(soDetail.getPlanQty());
					needSku.setIsFlagDesc(StringPool.CHS_NO);
					needSku.setSourceZoneQty(sourceStockQty);
					needSku.setWantage(BigDecimal.ZERO);
					needSku.setTotalStockQty(stockQty);
					if (needSku.getSoBillIdList() == null) {
						needSku.setSoBillIdList(new ArrayList<>());
					}
					needSku.getSoBillIdList().add(soBillIdStr);
					// ?????????
					for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
						needSku.skuLotSet(i, soDetailVO.skuLotGet(i));
					}
					needSkuList.add(needSku);
				} else {
					needSku.setWhName(warehouse.getWhName());
					if (!needSku.getSoBillIdList().contains(soBillIdStr)) {
						needSku.getSoBillIdList().add(soBillIdStr);
						needSku.setBillCount(needSku.getSoBillIdList().size());
					}
					// ???????????????????????????????????????
					needSku.setTotalNeedQty(needSku.getTotalNeedQty().add(soDetail.getPlanQty()));
				}
				if (needSku.getTotalNeedQty().compareTo(needSku.getSourceZoneQty()) > 0) {
					needSku.setWantage(needSku.getTotalNeedQty().subtract(needSku.getSourceZoneQty()));
					needSku.setIsFlagDesc(StringPool.CHS_YES);
				}
				needSku.setSoBillIdsStr(StringUtil.join(needSku.getSoBillIdList()));
			}
		}
		return needSkuList.stream()
			.sorted(Comparator.comparing(NeedSkuVO::getIsFlagDesc, Comparator.reverseOrder())
				.thenComparing(NeedSkuVO::getWhId)
				.thenComparing(NeedSkuVO::getWantage, Comparator.reverseOrder()))
			.collect(Collectors.toList());
	}

	@Override
	public List<NeedSkuDetailVO> getNeedSkuDetail(NeedSkuDetailQueryDTO needSkuVO) {
		List<NeedSkuDetailVO> detailList = new ArrayList<>();
		// ?????????????????????????????????
		List<SoHeader> soHeaderList = soHeaderService.list(Condition.getQueryWrapper(new SoHeader()).lambda()
			.eq(SoHeader::getSoBillState, SoBillStateEnum.CREATE.getCode())
		);
		// ??????????????????
		if (Func.isNotEmpty(soHeaderList)) {
			List<Long> soBillIdList = NodesUtil.toList(soHeaderList, SoHeader::getSoBillId);
			List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
				.func(item -> {
					for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
						if (needSkuVO.skuLotChk(i)) {
							// ???????????????????????????????????????
							item.eq("sku_lot" + i, needSkuVO.skuLotGet(i));
						}
					}
				})
				.lambda()
				.in(SoDetail::getSoBillId, soBillIdList)
				.eq(SoDetail::getSkuId, needSkuVO.getSkuId())
				.eq(SoDetail::getWspId, needSkuVO.getWspId())
				.orderByAsc(SoDetail::getSoBillId)
			);
			// ?????????????????????????????????????????????????????????????????????????????????
			for (SoDetail soDetail : soDetailList) {
				// ??????????????????
				SoHeader soHeader = soHeaderList.stream().filter(item -> {
					return item.getSoBillId().equals(soDetail.getSoBillId());
				}).findFirst().orElse(null);
				if (Func.isEmpty(soHeader)) {
					continue;
				}
				Warehouse warehouse = WarehouseCache.getById(soHeader.getWhId());
				if (Func.isEmpty(warehouse)) {
					continue;
				}
				Sku sku = SkuCache.getById(soDetail.getSkuId());
				if (Func.isEmpty(sku)) {
					continue;
				}
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(sku.getWoId());
				if (Func.isEmpty(owner)) {
					continue;
				}
				SkuPackage skuPackage = SkuPackageCache.getById(soDetail.getWspId());
				if (Func.isEmpty(skuPackage)) {
					continue;
				}
				NeedSkuDetailVO detail = detailList.stream().filter(item -> {
					return item.getSoBillId().equals(soHeader.getSoBillId()) &&
						item.getSkuId().equals(soDetail.getSkuId()) && item.getWspId().equals(soDetail.getWspId());
				}).findFirst().orElse(null);
				if (Func.isEmpty(detail)) {
					detail = new NeedSkuDetailVO();
					detail.setWhId(soHeader.getWhId());
					detail.setSoBillId(soHeader.getSoBillId());
					detail.setSoBillNo(soHeader.getSoBillNo());
					detail.setCName(soHeader.getCustomerName());
					detail.setSkuId(sku.getSkuId());
					detail.setSkuCode(sku.getSkuCode());
					detail.setSkuName(sku.getSkuName());
					detail.setWspId(skuPackage.getWspId());
					detail.setWspName(skuPackage.getWspName());
					detail.setSpec(skuPackage.getSpec());
					detail.setQty(soDetail.getPlanQty());
					detail.setWsuName(soDetail.getBaseUmName());

					detailList.add(detail);
				} else {
					detail.setQty(soDetail.getPlanQty().add(detail.getQty()));
				}
			}
		}
		return detailList;
	}
}
