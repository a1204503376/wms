package org.nodes.wms.core.count.service.impl;

import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.enums.TransferTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.enums.ChangeTypeEnum;
import org.nodes.wms.core.count.enums.CountByEnum;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
public class ChangeCountTypeQuery extends CountTypeQuery {

	private List<Location> myLocation = new ArrayList<>();
	private List<Long> skuIds = new ArrayList<>();

	@Override
	public List<CountHeaderVO> queryByCountType() {
		super.queryByCountType();
		if (countByEnum == CountByEnum.LOCATION) {
			countHeaderVOList = countHeaderVOList.stream().sorted(
				Comparator.comparing(CountHeaderVO::getLocCode))
				.collect(Collectors.toList());
		}
		return countHeaderVOList;
	}

	@Override
	protected void queryByLocation() {
		common();
		if (Func.isNotEmpty(countHeader.getChecked()) && countHeader.getChecked()) {
			if (myLocation.size() > 0) {
				Iterator<Location> iterator = myLocation.iterator();
				while (iterator.hasNext()) {
					Location location = iterator.next();
					if (location.getLocStatus() != 1) {
						iterator.remove();
					}
				}
			}
		}
		if (Func.isNotEmpty(locationList)) {
			List<Long> locIds = NodesUtil.toList(locationList, Location::getLocId);
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailByLocCodes(locIds);
			myLocation.forEach(location -> {
				CountHeaderVO countHeaderVO = new CountHeaderVO();
				countHeaderVO.setWhName(warehouse.getWhName());
				countHeaderVO.setLocCode(location.getLocCode());
				countHeaderVO.setLocId(location.getLocId());
				countHeaderVO.setZoneId(countHeader.getZoneId());
				if (Func.isNotEmpty(countDetails)) {
					List<CountDetail> collect = countDetails.stream().filter(countDetail -> countDetail.getLocId().equals(location.getLocId()))
						.collect(Collectors.toList());
					countHeaderVO.setCountBillNo(NodesUtil.join(collect, "countBillNo"));
				}
				countHeaderVO.setLocState(location.getLocStatus());
				countHeaderVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, location.getLocStatus()));
				countHeaderVO.setLastLocCountDate(location.getLastLocCountDate());
				countHeaderVO.setAbc(location.getAbc());
				countHeaderVO.setAbcDesc(DictCache.getValue(DictCodeConstant.LOC_ABC, location.getAbc()));
				countHeaderVOList.add(countHeaderVO);
			});
		}
	}

	@Override
	protected void queryBySku() {
		common();
		List<Stock> stockList = stockService.selectStockByLoc(
			NodesUtil.toList(myLocation, Location::getLocId),
			null, skuIds);
		if (Func.isNotEmpty(stockList)) {
			List<Long> skuIds = NodesUtil.toList(stockList, Stock::getSkuId);
			List<CountDetail> countDetails = countDetailService.selectOccupyCountDetailBySkuCodes(skuIds);
			for (Stock stock : stockList) {
				CountHeaderVO findObj = countHeaderVOList.stream().filter(u -> {
					return u.getSkuId().equals(stock.getSkuId()) && u.getLocId().equals(stock.getLocId());
				}).findFirst().orElse(null);
				if (Func.isNotEmpty(findObj)) {
					continue;
				}
				CountHeaderVO countHeaderVO = new CountHeaderVO();
				countHeaderVO.setWhName(warehouse.getWhName());
				countHeaderVO.setLocId(stock.getLocId());
				countHeaderVO.setZoneId(countHeader.getZoneId());
				countHeaderVO.setSkuId(stock.getSkuId());
				Location location = locationList.stream().filter(u -> {
					return u.getLocId().equals(stock.getLocId());
				}).findFirst().orElse(null);
				if (Func.isNotEmpty(location)) {
					countHeaderVO.setLocCode(location.getLocCode());
					if (Func.isNotEmpty(countDetails)) {
						List<CountDetail> collect = countDetails.stream().filter(countDetail ->
							countDetail.getSkuId().equals(stock.getSkuId())
								&& countDetail.getLocId().equals(location.getLocId()))
							.collect(Collectors.toList());
						countHeaderVO.setCountBillNo(NodesUtil.join(collect, CountDetail::getCountBillNo));
					}
					countHeaderVO.setLocState(location.getLocStatus());
					countHeaderVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, location.getLocStatus()));
					countHeaderVO.setLastLocCountDate(location.getLastLocCountDate());
					Sku sku = SkuCache.getById(stock.getSkuId());
					if (Func.isNotEmpty(sku)) {
						countHeaderVO.setSkuCode(sku.getSkuCode());
						countHeaderVO.setSkuName(sku.getSkuName());
						countHeaderVO.setAbc(sku.getAbc());
						countHeaderVO.setAbcDesc(DictCache.getValue(DictCodeConstant.LOC_ABC, sku.getAbc()));
						countHeaderVOList.add(countHeaderVO);
					}
				}
			}
		}
	}


	private void common() {
		if (Func.isEmpty(countHeader.getChangeType())) {
			throw new ServiceException("请选择类型");
		}
		if (Func.isEmpty(countHeader.getStartTime())) {
			throw new ServiceException("开始时间不能为空");
		}
		myLocation.clear();
		skuIds.clear();
		if (Func.isNotEmpty(countHeader.getChangeType())) {
			countHeader.getChangeType().forEach(changType -> {
				if (ChangeTypeEnum.OUTSTOCK.getIndex().equals(changType)) {
					List<SoPick> soPickList = soPickService.list(Condition.getQueryWrapper(new SoPick()).lambda()
						.ge(SoPick::getProcTime, countHeader.getStartTime())
						.func(sql -> {
							if (countByEnum == CountByEnum.LOCATION) {
								sql.groupBy(SoPick::getLocId);
							} else {
								sql.groupBy(SoPick::getSkuId);
							}
						})
					);
					List<Location> collect = locationList.stream().filter(location -> {
						for (SoPick soPick : soPickList) {
							if (location.getLocId().equals(soPick.getLocId())) {
								return true;
							}
						}
						return false;
					}).collect(Collectors.toList());
					skuIds.addAll(NodesUtil.toList(soPickList, SoPick::getSkuId));
					myLocation.addAll(collect);
				} else {
					List<TransferRecord> transferRecordList = transferRecordService.list(
						Condition.getQueryWrapper(new TransferRecord()).lambda()
							.ge(TransferRecord::getUpdateTime, countHeader.getStartTime())
							.func(sql -> {
								if (ChangeTypeEnum.INSTOCK.getIndex().equals(changType)) {
									sql.eq(TransferRecord::getTransferType, TransferTypeEnum.PUTAWAY.getIndex());
								} else {
									sql.ne(TransferRecord::getTransferType, TransferTypeEnum.PUTAWAY.getIndex());
								}
								if (countByEnum == CountByEnum.LOCATION) {
									sql.groupBy(TransferRecord::getToLocCode);
								} else {
									sql.groupBy(TransferRecord::getSkuId);
								}
							}));
					List<Location> collect = locationList.stream().filter(location -> {
						for (TransferRecord transferRecord : transferRecordList) {
							if (location.getLocCode().equals(transferRecord.getFromLocCode())
								|| location.getLocCode().equals(transferRecord.getToLocCode())) {
								return true;
							}
						}
						return false;
					}).collect(Collectors.toList());
					skuIds.addAll(NodesUtil.toList(transferRecordList, TransferRecord::getSkuId));
					myLocation.addAll(collect);
				}
			});
		}
	}
}
