package org.nodes.modules.wms.core.service.impl;

import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.modules.wms.core.service.StockService;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.common.enums.CreateTypeEnum;
import org.nodes.wms.core.instock.asn.cache.AsnCache;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.enums.AsnDetailStatusEnum;
import org.nodes.wms.core.instock.asn.enums.SyncStateEnum;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.instock.asn.vo.AsnDetailVO;
import org.nodes.wms.core.instock.asn.vo.AsnHeaderVO;
import org.nodes.wms.core.outstock.so.cache.SoCache;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.enums.OutstockTypeEnum;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.enums.SoBillTransportCodeEnum;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.vo.SoDetailVO;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.nodes.wms.dao.instock.asn.enums.InStorageTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service("mStockService")
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockServiceImpl implements StockService {

	@Autowired
	private IStockService stockService;
	@Autowired
	private IAsnHeaderService asnHeaderService;
	@Autowired
	private IAsnDetailService asnDetailService;
	@Autowired
	private ISoHeaderService soHeaderService;
	@Autowired
	private ISoDetailService soDetailService;

	@Override
	public boolean updateBatch(List<Stock> stockList) {
		//????????????????????????
		List<Stock> originalStockList = stockService.list();
		//???????????????????????????????????????
		List<Stock> changeStockList = new ArrayList<>();
		// ?????????????????????????????????
		List<AsnHeaderVO> asnHeaderList = new ArrayList<>();
		// ?????????????????????????????????
		List<SoHeaderVO> soHeaderList = new ArrayList<>();

		for (Stock originalStock : originalStockList) {
			originalStock.setStockQty(BigDecimalUtil.globeScale(originalStock.getStockQty()));
			//????????????????????????????????????
			Stock stock = stockList.stream()
				.filter(u -> u.getStockId().equals(originalStock.getStockId()) && !u.equals(originalStock))
				.findFirst().orElse(null);

			//???????????????????????????????????????????????????
			if (Func.isNotEmpty(stock)) {
				//????????????????????????
				changeStockList.add(stock);

				Warehouse warehouse = WarehouseCache.getById(stock.getWhId());
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(stock.getWoId());
				LocalDateTime now = LocalDateTime.now();
				// ???????????????????????????
				AsnHeaderVO asnHeader = new AsnHeaderVO();
				asnHeader.setWhId(stock.getWhId());//??????id
				asnHeader.setWhCode(warehouse.getWhCode());//????????????
				asnHeader.setWoId(stock.getWoId());//??????id
				asnHeader.setOwnerCode(owner.getOwnerCode());//????????????
				// ??????????????????????????????
				while (true) {
					String asnBillNo = AsnCache.getAsnBillNo();
					asnHeader.setAsnBillNo(asnBillNo);//???????????????????????????????????????????????????
					int count = asnHeaderService.count(Condition.getQueryWrapper(new AsnHeader()).lambda()
						.eq(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo()));
					if (count == 0) {
						break;
					}
				}
				asnHeader.setAsnBillState(AsnBillStateEnum.COMPLETED.getCode());//????????????
				asnHeader.setBillTypeCd(ParamCache.getValue(ParamEnum.COUNT_PROFIT_TYPECD.getKey()));//??????????????????
//				asnHeader.setBillKey(asnHeader.getAsnBillNo());//??????????????????????????????
//				asnHeader.setLastUpdateDate(now);
//				asnHeader.setPreCreateDate(now);
				asnHeader.setScheduledArrivalDate(now);
				asnHeader.setActualArrivalDate(now);
				asnHeader.setFinishDate(now);
				asnHeader.setInstoreType(InStorageTypeEnum.Normal.getCode());//????????????
//				asnHeader.setSCode(warehouse.getWhCode());
//				asnHeader.setSName(warehouse.getWhName());
//				asnHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());//????????????
				asnHeader.setCreateType(CreateTypeEnum.INNER.getIndex());//????????????
				asnHeader.setAsnDetailList(new ArrayList<>());

				// ???????????????????????????
				// ????????????????????????
				Sku sku = SkuCache.getById(stock.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("???????????????(ID:" + stock.getSkuId() + ")");
				}
				// ?????????????????????????????????
				SkuPackage skuPackage = SkuPackageCache.getById(stock.getWspId());
				if (Func.isEmpty(skuPackage)) {
					throw new ServiceException("?????????????????????????????????(ID:" + stock.getWspId() + ")!");
				}
				// ??????????????????????????????
				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
					skuPackage.getWspId(), SkuLevelEnum.Base.getIndex());
				if (Func.isEmpty(skuPackageDetail)) {
					throw new ServiceException("???????????????????????????????????????(????????????:" + skuPackage.getWspName() + ")");
				}
				NumberFormat numberFormat = NumberFormat.getNumberInstance();
				numberFormat.setMinimumIntegerDigits(8);
				numberFormat.setGroupingUsed(false);
				Integer lineNo = 1;

				AsnDetailVO asnDetail = new AsnDetailVO();
				asnDetail.setAsnLineNo(numberFormat.format(lineNo));//????????????
//				asnDetail.setAsnBillDetailKey(asnDetail.getAsnLineNo());//????????????????????????????????????
				asnDetail.setSkuId(sku.getSkuId());//??????id
				asnDetail.setWspId(skuPackage.getWspId());//??????id
				asnDetail.setSkuLevel(SkuLevelEnum.Base.getIndex());//??????
				asnDetail.setSkuCode(sku.getSkuCode());
				asnDetail.setSkuName(sku.getSkuName());
				asnDetail.setSkuSpec(skuPackage.getSpec());//??????
				asnDetail.setConvertQty(skuPackageDetail.getConvertQty());//????????????
				asnDetail.setUmCode(skuPackageDetail.getWsuCode());
				asnDetail.setUmName(skuPackageDetail.getWsuName());
				asnDetail.setBaseUmCode(skuPackageDetail.getWsuCode());
				asnDetail.setBaseUmName(skuPackageDetail.getWsuName());
				asnDetail.setPlanQty(stock.getStockQty());//????????????
				asnDetail.setScanQty(asnDetail.getPlanQty());//????????????
				asnDetail.setSurplusQty(asnDetail.getPlanQty().subtract(asnDetail.getScanQty()));//????????????
				asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());//????????????
				asnDetail.setImportSn(StringPool.N.toUpperCase());//????????????
				// ?????????????????????
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (stock.skuLotChk(i)) {
						asnDetail.skuLotSet(i, stock.skuLotGet(i));
					}
				}
				asnHeader.getAsnDetailList().add(asnDetail);
				asnHeaderList.add(asnHeader);


				// ???????????????????????????
				Warehouse originalWarehouse = WarehouseCache.getById(originalStock.getWhId());
				Owner originalOwner = ownerService.getById(originalStock.getWoId());
				SoHeaderVO soHeader = new SoHeaderVO();
				soHeader.setWhId(originalStock.getWhId());
				soHeader.setWhCode(originalWarehouse.getWhCode());
				soHeader.setWoId(originalStock.getWoId());
				soHeader.setOwnerCode(originalOwner.getOwnerCode());//????????????
				// ??????????????????????????????
				while (true) {
					soHeader.setSoBillNo(SoCache.getSoBillNo());
					int count = soHeaderService.count(Condition.getQueryWrapper(new SoHeader()).lambda()
						.eq(SoHeader::getSoBillNo, soHeader.getSoBillNo()));
					if (count == 0) {
						break;
					}
				}
				soHeader.setSoBillState(SoBillStateEnum.COMPLETED.getCode());
				soHeader.setBillTypeCd(ParamCache.getValue(ParamEnum.COUNT_LOSS_TYPECD.getKey()));//??????????????????
				soHeader.setBillKey(soHeader.getSoBillNo());
				soHeader.setLastUpdateDate(now);
				soHeader.setPreCreateDate(now);
				soHeader.setFinishDate(now);
				soHeader.setTransportCode(SoBillTransportCodeEnum.SelfTaking.getIndex());//????????????
				soHeader.setOutstockType(OutstockTypeEnum.Normal.getIndex());
//				soHeader.setCCode(warehouse.getWhCode());
//				soHeader.setCName(warehouse.getWhName());
				soHeader.setTransportDate(now);
				soHeader.setCreateType(CreateTypeEnum.INNER.getIndex());
				soHeader.setBillCreator(AuthUtil.getNickName());
				soHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
				soHeader.setDetailList(new ArrayList<>());


				// ???????????????????????????
				// ????????????????????????
				Sku originalSku = SkuCache.getById(originalStock.getSkuId());
				if (Func.isEmpty(originalSku)) {
					throw new ServiceException("???????????????(ID:" + originalStock.getSkuId() + ")");
				}
				// ?????????????????????????????????
				SkuPackage originalSkuPackage = SkuPackageCache.getById(originalStock.getWspId());
				if (Func.isEmpty(skuPackage)) {
					throw new ServiceException("?????????????????????????????????(ID:" + originalStock.getWspId() + ")!");
				}
				// ??????????????????????????????
				SkuPackageDetail originalSkuPackageDetail = SkuPackageDetailCache.getOne(
					originalSkuPackage.getWspId(), SkuLevelEnum.Base.getIndex());
				if (Func.isEmpty(skuPackageDetail)) {
					throw new ServiceException("???????????????????????????????????????(????????????:" + originalSkuPackage.getWspName() + ")");
				}
				SoDetailVO soDetail = new SoDetailVO();
				soDetail.setSoLineNo(numberFormat.format(lineNo));//????????????
				soDetail.setBillTypeCd(soHeader.getBillTypeCd());
				soDetail.setBillDetailKey(soDetail.getSoLineNo());
				soDetail.setSkuId(originalStock.getSkuId());
				soDetail.setSkuCode(originalSku.getSkuCode());
				soDetail.setSkuName(originalSku.getSkuName());
				soDetail.setWspId(originalStock.getWspId());
				soDetail.setSkuLevel(SkuLevelEnum.Base.getIndex());//??????
				soDetail.setSkuSpec(originalSkuPackage.getSpec());//??????
				soDetail.setConvertQty(originalSkuPackageDetail.getConvertQty());
				soDetail.setUmCode(originalSkuPackageDetail.getWsuCode());
				soDetail.setUmName(originalSkuPackageDetail.getWsuName());
				soDetail.setBaseUmCode(originalSkuPackageDetail.getWsuCode());
				soDetail.setBaseUmName(originalSkuPackageDetail.getWsuName());
				soDetail.setPlanQty(originalStock.getStockQty());
				soDetail.setScanQty(soDetail.getPlanQty());
				soDetail.setSurplusQty(soDetail.getPlanQty().subtract(soDetail.getScanQty()));
				soDetail.setBillDetailState(SoDetailStateEnum.Allocated.getCode());//????????????
				// ?????????????????????
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (originalStock.skuLotChk(i)) {
						soDetail.skuLotSet(i, originalStock.skuLotGet(i));
					}
				}
				soHeader.getDetailList().add(soDetail);
				soHeaderList.add(soHeader);
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

		return stockService.updateBatchById(changeStockList);
	}
}
