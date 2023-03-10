package org.nodes.wms.core.instock.asn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.instock.asn.dto.PutawayByBoxQueryDTO;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.mapper.AsnHeaderMapper;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.IPutawayByBoxService;
import org.nodes.wms.core.instock.asn.vo.BoxItemVo;
import org.nodes.wms.core.instock.asn.vo.PutawayByBoxSubimitVO;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.dto.StockMoveDTO;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.enums.TransferTypeEnum;
import org.nodes.wms.core.stock.transfer.service.ITransferRecordService;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ?????????????????????
 *
 * @Author zx
 * @Date 2020/6/29
 **/
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class PutawayByBoxServiceImpl<E extends AsnHeaderMapper, T extends AsnHeader>
	extends BaseServiceImpl<AsnHeaderMapper, AsnHeader>
	implements IPutawayByBoxService {

	@Autowired
	IStockService stockService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ITransferRecordService transferRecordService;
	@Autowired
	IInstockService instockService;
	@Autowired
	IAsnDetailService asnDetailService;

	/**
	 * ???????????? ??????????????????
	 *
	 * @param putawayByBoxQueryDTO
	 * @return
	 */
	@Override
	public InstockExecuteVO queryStockForBox(PutawayByBoxQueryDTO putawayByBoxQueryDTO) {

		Warehouse warehouse = WarehouseCache.getById(putawayByBoxQueryDTO.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}
		// ????????????????????????????????????????????????????????????
		if (ZoneVirtualTypeEnum.isVirtual(putawayByBoxQueryDTO.getLocCode())) {

			putawayByBoxQueryDTO.setLocCode(
				Func.firstCharToUpper(putawayByBoxQueryDTO.getLocCode().toLowerCase()) + warehouse.getWhCode());
		}
		Sku sku = SkuCache.getById(putawayByBoxQueryDTO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException(String.format("??????[%s]????????????????????????", putawayByBoxQueryDTO.getSkuCode()));
		}
		// ????????????????????????????????????
		Location location = LocationCache.getValid(putawayByBoxQueryDTO.getLocCode());
		// ??????????????????ID
		List<Integer> virtualZoneList = ZoneVirtualTypeEnum.list();
/*		List<Zone> zoneList = ZoneCache.list().stream().filter(u -> {
			return virtualZoneList.contains(u.getZoneType());
		}).collect(Collectors.toList());*/
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		List<Zone> zoneList = zoneService.list(Condition.getQueryWrapper(new Zone())
			.lambda()
			.in(Zone::getZoneType, virtualZoneList)
		);
		List<Long> zoneIdList = NodesUtil.toList(zoneList, Zone::getZoneId);
		// ????????????????????????????????????(?????????????????????)
		List<Stock> stockListAll = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
			.eq(Stock::getSkuId, sku.getSkuId())
			.notIn(Stock::getZoneId, zoneIdList));

		// ??????????????????????????????????????????
		QueryWrapper<Stock> stockQueryWrapper = Condition.getQueryWrapper(new Stock());
		stockQueryWrapper.lambda()
			.eq(Stock::getSkuId, sku.getSkuId())
			.eq(Stock::getLocId, location.getLocId())
			.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
			.apply("stock_qty <> pick_qty");
		if (Func.isNotEmpty(putawayByBoxQueryDTO.getSkuLots())) {
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (Func.isEmpty(putawayByBoxQueryDTO.getSkuLots().skuLotGet(i))) {
					continue;
				}
				stockQueryWrapper.eq("sku_lot" + i, putawayByBoxQueryDTO.getSkuLots().skuLotGet(i));
			}
		}

		// ?????????????????????
		List<Stock> list = stockService.list(stockQueryWrapper);

		InstockExecuteVO vo = new InstockExecuteVO();

		if (Func.isEmpty(list)) {
			return InstockExecuteVO.EMPTY;
		}
		Stock stock = list.get(0);
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		StockDetail stockDetail = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
				.lambda()
				.in(StockDetail::getStockId, NodesUtil.toList(list, Stock::getStockId)))
			.stream().findFirst().orElse(null);
		if (Func.isEmpty(stockDetail)) {
			return InstockExecuteVO.EMPTY;
		}
		AsnDetail asnDetail = asnDetailService.getById(stockDetail.getBillDetailId());
		if (Func.isEmpty(asnDetail)) {
			throw new ServiceException("????????????????????????????????????");
		}
		AsnHeader asnHeader = super.getById(asnDetail.getAsnBillId());
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("??????????????????????????????");
		}
		//?????????
		vo = instockService.execute(list, asnHeader.getBillTypeCd());
		vo.setStockLocList(StockWrapper.build().listVO(stockListAll));
		Set<Long> types = new HashSet<>();

		for (Stock item : list) {
			//??????
			types.add(item.getSkuId());
		}
		vo.setLocCode(location.getLocCode());
		//????????????
		if (Func.isNotEmpty(location.getLocStatus())) {
			vo.setLocStatus(location.getLocStatus());
			vo.setLocStatusName(DictCache.getValue(DictCodeConstant.LOC_STATUS, location.getLocStatus()));
		}

		if (Func.isNotEmpty(warehouse)) {
			vo.setWhId(warehouse.getWhId());
			vo.setWhName(warehouse.getWhName());
		}
		if (Func.isNotEmpty(list)) {
			BigDecimal reduce1 = list.stream().map(Stock::getStockQty)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal reduce2 = list.stream().map(Stock::getPickQty)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			vo.setPlanCount(reduce1.subtract(reduce2));
			SkuPackageDetail one = SkuPackageDetailCache.getOne(stock.getWspId(), SkuLevelEnum.Base.getIndex());
			vo.setUmName(one.getWsuName());
		}
		vo.setTypeCount(types.size());
		vo.setStockId(stock.getStockId());
		return vo;
	}

	/**
	 * ???????????? ??????????????????
	 *
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	@Override
	public synchronized boolean submitPutaway(PutawayByBoxSubimitVO putawayByBoxSubimitVO) {

		if (Func.isEmpty(putawayByBoxSubimitVO.getWsuCode()) && Func.isEmpty(putawayByBoxSubimitVO.getWsuName())) {
			throw new ServiceException("?????????wsuCode, wsuName ?????????????????????");
		}
		Warehouse warehouse = WarehouseCache.getById(putawayByBoxSubimitVO.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}
		if (ZoneVirtualTypeEnum.isVirtual(putawayByBoxSubimitVO.getLocCode())) {
			//????????????????????????????????????????????????????????????
			putawayByBoxSubimitVO.setLocCode(
				Func.firstCharToUpper(putawayByBoxSubimitVO.getLocCode().toLowerCase()) + warehouse.getWhCode());
		} else {
			throw new ServiceException(String.format("???????????????,?????????????????????"));
		}

		//?????????????????????????????????????????????????????????????????????
		Stock stock = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.eq(Stock::getStockId, putawayByBoxSubimitVO.getStockId()))
			.stream().findFirst().orElse(null);
		if (Func.isEmpty(stock)) {
			throw new ServiceException("?????????????????????????????????");
		}
		// ????????????????????????????????????
		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.listByWspId(stock.getWspId())
			.stream()
			.filter(item -> {
				if (Func.isNotEmpty(putawayByBoxSubimitVO.getWsuCode())) {
					return putawayByBoxSubimitVO.getWsuCode().equals(item.getWsuCode());
				} else if (Func.isNotEmpty(putawayByBoxSubimitVO.getWsuName())) {
					return putawayByBoxSubimitVO.getWsuName().equals(item.getWsuName());
				} else {
					return false;
				}
			}).findFirst().orElse(null);
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException("???????????????????????????????????????");
		}
		//?????????
		Location sourceLocation = LocationCache.getValid(putawayByBoxSubimitVO.getLocCode());
		//????????????????????????
		Location targetLocation = LocationCache.getValid(putawayByBoxSubimitVO.getTargetLocCode());
		if (!targetLocation.getWhId().equals(sourceLocation.getWhId())) {
			throw new ServiceException(String.format("?????????????????????????????????????????????"));
		}
		//??????????????????
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.ASN);
		systemProcParam.setAction(ActionEnum.PUTAWAY_BOX);
//		systemProcParam.setLpnCode(stock.getLpnCode());
		systemProcParam.setWhId(stock.getWhId());
		SystemProc systemProc = systemProcService.create(systemProcParam);

		BigDecimal qty = putawayByBoxSubimitVO.getQty();

		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setSourceStockId(stock.getStockId());
//		stockMoveDTO.setSourceLpnCode(stock.getLpnCode());
		stockMoveDTO.setSourceLocId(sourceLocation.getLocId()); //?????????id ?????????????????????
		stockMoveDTO.setTargetLocId(targetLocation.getLocId()); //????????????id ???????????????locCode??????locId
		stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
		stockMoveDTO.setEventType(EventTypeEnum.Putaway);
		stockMoveDTO.setSkuId(stock.getSkuId()); //??????id
		stockMoveDTO.setMoveQty(qty);
		stockMoveDTO.setTargetLpnCode(putawayByBoxSubimitVO.getLpnCode());
		stockService.stockMove(stockMoveDTO);
		// ??????????????????
		TransferRecord transferRecord = new TransferRecord();
		transferRecord.setTransferType(TransferTypeEnum.PUTAWAY.getIndex());
		transferRecord.setWoId(stock.getWoId()); // ?????????
		transferRecord.setSkuId(stock.getSkuId()); //?????????
//		transferRecord.setFromLpn(stock.getLpnCode()); // ?????????
		transferRecord.setFromLocCode(putawayByBoxSubimitVO.getLocCode());//???????????????
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone sourceZone = zoneService.getById(sourceLocation.getZoneId());
		if (Func.isNotEmpty(sourceZone)) {
			transferRecord.setFromZoneCode(sourceZone.getZoneCode());// ???????????????
			transferRecord.setFromZoneName(sourceZone.getZoneName());// ???????????????
		}
		transferRecord.setQty(stock.getStockQty()); // ?????????
		transferRecord.setWspId(stock.getWspId()); //?????????Id
		transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());//?????????
		transferRecord.setSkuId(stock.getSkuId());//????????????
		transferRecord.setFromLocId(putawayByBoxSubimitVO.getSourceLocId());
		transferRecord.setToLocId(targetLocation.getLocId());
		transferRecord.setWsuCode(putawayByBoxSubimitVO.getWsuCode());
		transferRecord.setWsuName(putawayByBoxSubimitVO.getWsuName());
		transferRecord.setToLocCode(targetLocation.getLocCode());//??????????????????
		Zone targetZone = zoneService.getById(targetLocation.getZoneId());
		if (Func.isNotEmpty(targetZone)) {
			transferRecord.setToZoneCode(targetZone.getZoneCode());//??????????????????
			transferRecord.setToZoneName(targetZone.getZoneName());//??????????????????
		}
		transferRecord.setQty(qty);//????????????
		transferRecord.setWspId(stock.getWspId());//????????????
		transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());// ????????????
		transferRecord.setLotNumber(stock.getLotNumber());//?????????
		transferRecord.setTransferCode("");
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (stock.skuLotChk(i)) {
				transferRecord.skuLotSet(i, stock.skuLotGet(i));
			}
		}

		return transferRecordService.save(transferRecord);
	}

	/**
	 * ??????????????? ??????????????????
	 *
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	@Override
	public synchronized boolean submitPutawayNew(PutawayByBoxSubimitVO putawayByBoxSubimitVO) {

		Location targetLocation = LocationCache.getValid(putawayByBoxSubimitVO.getTargetLocCode());
		if (Func.isEmpty(targetLocation)) {
			throw new ServiceException("????????????????????????");
		}
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getBoxCode, putawayByBoxSubimitVO.getBoxCodes()));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("??????????????????????????????????????????");
		}
		List<Stock> stockList = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, NodesUtil.toList(stockDetails, StockDetail::getStockId)));
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("??????????????????????????????????????????");
		}
		for (StockDetail stockDetail : stockDetails) {
			Location location = LocationCache.getById(stockDetail.getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException(String.format("?????????[%s]???????????????????????????", stockDetail.getBoxCode()));
			}
			boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
			if (!virtual) {
				throw new ServiceException(String.format("?????????[%s]???????????????????????????", stockDetail.getBoxCode()));
			}
			if (!location.getLocCode().contains("Stage")) {
				throw new ServiceException(String.format("?????????[%s]???????????????????????????", stockDetail.getBoxCode()));
			}
			Stock stock1 = stockList.stream().filter(stock -> stock.getStockId()
				.equals(stockDetail.getStockId())).findFirst().orElse(null);
			if (Func.isEmpty(stock1)) {
				throw new ServiceException("??????????????????????????????????????????");
			}
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.ASN);
			systemProcParam.setAction(ActionEnum.PUTAWAY_BOX);
			systemProcParam.setWhId(targetLocation.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);

			StockMoveDTO stockMoveDTO = new StockMoveDTO();
			stockMoveDTO.setSourceStockId(stock1.getStockId());
			stockMoveDTO.setSourceLocId(stockDetail.getLocId());
			stockMoveDTO.setTargetLocId(targetLocation.getLocId());
			stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
			stockMoveDTO.setEventType(EventTypeEnum.Putaway);
			stockMoveDTO.setSkuId(stockDetail.getSkuId());
			stockMoveDTO.setSourceBoxCode(stockDetail.getBoxCode());
			stockMoveDTO.setTargetBoxCode(stockDetail.getBoxCode());
			stockMoveDTO.setMoveQty(stockDetail.getStockQty());
			stockMoveDTO.setTargetLpnCode(stockDetail.getLpnCode());
			stockService.stockMove(stockMoveDTO);
			TransferRecord transferRecord = new TransferRecord();
			transferRecord.setTransferType(TransferTypeEnum.PUTAWAY.getIndex());
			transferRecord.setWoId(stock1.getWoId()); // ?????????
			transferRecord.setSkuId(stockDetail.getSkuId()); //?????????
			transferRecord.setFromLpn(stockDetail.getLpnCode()); // ?????????
			transferRecord.setFromLocCode(putawayByBoxSubimitVO.getLocCode());//???????????????
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone sourceZone = zoneService.getById(location.getZoneId());
			if (Func.isNotEmpty(sourceZone)) {
				transferRecord.setFromZoneCode(sourceZone.getZoneCode());// ???????????????
				transferRecord.setFromZoneName(sourceZone.getZoneName());// ???????????????
			}
			transferRecord.setQty(stockDetail.getStockQty()); // ?????????
			transferRecord.setWspId(stockDetail.getWspId()); //?????????Id
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());//?????????
			transferRecord.setSkuId(stockDetail.getSkuId());//????????????
			transferRecord.setFromLocId(putawayByBoxSubimitVO.getSourceLocId());
			transferRecord.setToLocId(targetLocation.getLocId());
			transferRecord.setWsuCode(putawayByBoxSubimitVO.getWsuCode());
			transferRecord.setWsuName(putawayByBoxSubimitVO.getWsuName());
			transferRecord.setToLocCode(targetLocation.getLocCode());//??????????????????
			Zone targetZone = zoneService.getById(targetLocation.getZoneId());
			if (Func.isNotEmpty(targetZone)) {
				transferRecord.setToZoneCode(targetZone.getZoneCode());//??????????????????
				transferRecord.setToZoneName(targetZone.getZoneName());//??????????????????
			}
			transferRecord.setQty(stockDetail.getStockQty());//????????????
			transferRecord.setWspId(stockDetail.getWspId());//????????????
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());// ????????????
			transferRecord.setLotNumber(stockDetail.getLotNumber());//?????????
			transferRecord.setTransferCode("");
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (stock1.skuLotChk(i)) {
					transferRecord.skuLotSet(i, stock1.skuLotGet(i));
				}
			}
			transferRecordService.save(transferRecord);
		}
		return true;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param boxCode
	 */
	@Override
	public BoxItemVo getRecommendInfoByBoxCode(String boxCode) {

		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		StockDetail stockDetail = stockDetailService.getOne(Wrappers.lambdaQuery(StockDetail.class)
			.eq(StockDetail::getBoxCode, boxCode).apply("stock_qty - pick_qty > 0"));
		if (Func.isEmpty(stockDetail)) {
			throw new ServiceException("???????????????????????????");
		}
		Location location = LocationCache.getById(stockDetail.getLocId());
		if (Func.isEmpty(location)) {
			throw new ServiceException("???????????????????????????");
		}
		boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
		if (!virtual) {
			throw new ServiceException("???????????????????????????");
		}
		if (!location.getLocCode().contains("Stage")) {
			throw new ServiceException("???????????????????????????");
		}
		Stock stock = stockService.getById(stockDetail.getStockId());
		if (Func.isEmpty(stock)) {
			throw new ServiceException("??????????????????");
		}
		Sku sku = SkuCache.getById(stockDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("???????????????");
		}
		List<Stock> stockList = new ArrayList<Stock>() {{
			add(stock);
		}};
		InstockExecuteVO executeVO = instockService.execute(stockList, null);
		BoxItemVo boxItemVo = new BoxItemVo();
		boxItemVo.setBoxCode(boxCode);
		boxItemVo.setReLocCode(executeVO.getLocCode());
		boxItemVo.setReZone(executeVO.getZoneCode());
		boxItemVo.setSkuCode(sku.getSkuCode());
		boxItemVo.setSkuName(sku.getSkuName());
		return boxItemVo;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param boxCode
	 */
	@Override
	public BoxItemVo getInfoMoveByBoxCode(String boxCode) {

		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		StockDetail stockDetail = stockDetailService.getOne(Wrappers.lambdaQuery(StockDetail.class)
			.eq(StockDetail::getBoxCode, boxCode).apply("stock_qty - pick_qty > 0"));
		if (Func.isEmpty(stockDetail)) {
			throw new ServiceException("????????????????????????????????????");
		}
		Location location = LocationCache.getById(stockDetail.getLocId());
		if (Func.isEmpty(location)) {
			throw new ServiceException("????????????????????????????????????");
		}
		boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
		if (virtual) {
			throw new ServiceException("????????????????????????????????????");
		}
		Stock stock = stockService.getById(stockDetail.getStockId());
		if (Func.isEmpty(stock)) {
			throw new ServiceException("??????????????????");
		}
		Sku sku = SkuCache.getById(stockDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("???????????????");
		}
		BoxItemVo boxItemVo = new BoxItemVo();
		boxItemVo.setBoxCode(boxCode);
		boxItemVo.setSkuCode(sku.getSkuCode());
		boxItemVo.setSkuName(sku.getSkuName());
		boxItemVo.setSourceLocCode(location.getLocCode());
		return boxItemVo;
	}

	@Override
	public synchronized boolean submitMoveForBoxNew(PutawayByBoxSubimitVO putawayByBoxSubimitVO) {

		Location targetLocation = LocationCache.getValid(putawayByBoxSubimitVO.getTargetLocCode());
		if (Func.isEmpty(targetLocation)) {
			throw new ServiceException("????????????????????????");
		}
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getBoxCode, putawayByBoxSubimitVO.getBoxCodes()));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("?????????????????????????????????????????????");
		}
		List<Stock> stockList = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, NodesUtil.toList(stockDetails, StockDetail::getStockId)));
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("?????????????????????????????????????????????");
		}
		for (StockDetail stockDetail : stockDetails) {
			Location location = LocationCache.getById(stockDetail.getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException(String.format("?????????[%s]?????????????????????????????????", stockDetail.getBoxCode()));
			}
			boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
			if (virtual) {
				throw new ServiceException(String.format("?????????[%s]?????????????????????????????????", stockDetail.getBoxCode()));
			}

			Stock stock1 = stockList.stream().filter(stock -> stock.getStockId()
				.equals(stockDetail.getStockId())).findFirst().orElse(null);
			if (Func.isEmpty(stock1)) {
				throw new ServiceException("?????????????????????????????????????????????");
			}
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.ASN);
			systemProcParam.setAction(ActionEnum.PUTAWAY_BOX);
			systemProcParam.setWhId(targetLocation.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);

			StockMoveDTO stockMoveDTO = new StockMoveDTO();
			stockMoveDTO.setSourceStockId(stock1.getStockId());
			stockMoveDTO.setSourceLocId(stockDetail.getLocId());
			stockMoveDTO.setTargetLocId(targetLocation.getLocId());
			stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
			stockMoveDTO.setEventType(EventTypeEnum.Move);
			stockMoveDTO.setSkuId(stockDetail.getSkuId());
			stockMoveDTO.setSourceBoxCode(stockDetail.getBoxCode());
			stockMoveDTO.setTargetBoxCode(stockDetail.getBoxCode());
			stockMoveDTO.setMoveQty(stockDetail.getStockQty());
			stockMoveDTO.setTargetLpnCode(stockDetail.getLpnCode());
			stockService.stockMove(stockMoveDTO);
			TransferRecord transferRecord = new TransferRecord();
			transferRecord.setTransferType(TransferTypeEnum.PUTAWAY.getIndex());
			transferRecord.setWoId(stock1.getWoId()); // ?????????
			transferRecord.setSkuId(stockDetail.getSkuId()); //?????????
			transferRecord.setFromLpn(stockDetail.getLpnCode()); // ?????????
			transferRecord.setFromLocCode(putawayByBoxSubimitVO.getLocCode());//???????????????
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone sourceZone = zoneService.getById(location.getZoneId());
			if (Func.isNotEmpty(sourceZone)) {
				transferRecord.setFromZoneCode(sourceZone.getZoneCode());// ???????????????
				transferRecord.setFromZoneName(sourceZone.getZoneName());// ???????????????
			}
			transferRecord.setQty(stockDetail.getStockQty()); // ?????????
			transferRecord.setWspId(stockDetail.getWspId()); //?????????Id
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());//?????????
			transferRecord.setSkuId(stockDetail.getSkuId());//????????????
			transferRecord.setFromLocId(putawayByBoxSubimitVO.getSourceLocId());
			transferRecord.setToLocId(targetLocation.getLocId());
			transferRecord.setWsuCode(putawayByBoxSubimitVO.getWsuCode());
			transferRecord.setWsuName(putawayByBoxSubimitVO.getWsuName());
			transferRecord.setToLocCode(targetLocation.getLocCode());//??????????????????
			Zone targetZone = zoneService.getById(targetLocation.getZoneId());
			if (Func.isNotEmpty(targetZone)) {
				transferRecord.setToZoneCode(targetZone.getZoneCode());//??????????????????
				transferRecord.setToZoneName(targetZone.getZoneName());//??????????????????
			}
			transferRecord.setQty(stockDetail.getStockQty());//????????????
			transferRecord.setWspId(stockDetail.getWspId());//????????????
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());// ????????????
			transferRecord.setLotNumber(stockDetail.getLotNumber());//?????????
			transferRecord.setTransferCode("");
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (stock1.skuLotChk(i)) {
					transferRecord.skuLotSet(i, stock1.skuLotGet(i));
				}
			}
			transferRecordService.save(transferRecord);
		}
		return true;
	}
}
