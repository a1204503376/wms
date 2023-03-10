package org.nodes.wms.core.stock.transfer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.vo.SkuPackageDetailViewVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.dto.StockMoveByBoxQueryDTO;
import org.nodes.wms.core.stock.core.dto.StockMoveDTO;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.mapper.StockMapper;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockMoveService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.SkuStockLotMoveVO;
import org.nodes.wms.core.stock.core.vo.StockSkuMoveSubmitVO;
import org.nodes.wms.core.stock.core.vo.StockSkuMoveVO;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.enums.TransferTypeEnum;
import org.nodes.wms.core.stock.transfer.service.IStockInnerService;
import org.nodes.wms.core.stock.transfer.service.ITransferRecordService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.warehouse.wrapper.WarehouseWrapper;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ???????????????????????????
 *
 * @Author zx
 * @Date 2020/3/18
 **/
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockInnerServiceImpl<M extends StockMapper, T extends Stock>
	extends BaseServiceImpl<StockMapper, Stock>
	implements IStockInnerService {

	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ISkuLotService skuLotService;
	@Autowired
	ISkuUmService skuUmService;
	@Autowired
	ITransferRecordService transferRecordService;
	@Autowired
	ISerialService serialService;
	@Autowired
	IStockService stockService;
	@Autowired
	IStockDetailService stockDetailService;
	@Autowired
	@Qualifier("skuStockMoveService")
	IStockMoveService stockMoveService;

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@Override
	public List<StockSkuMoveVO> listWarehouse(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<Warehouse> warehouses = warehouseService.list();
		List<StockSkuMoveVO> result = new ArrayList<>();
		for (Warehouse warehouse : warehouses) {
			result.add(WarehouseWrapper.warehouse2StockSkuMoveVO(warehouse));
		}
		return result;

	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public StockSkuMoveVO getLocCodeByLpnCode(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		if (Func.isEmpty(stockSkuMoveSubmitVO.getSourceLpnCode())) {
			throw new ServiceException("????????????????????????");
		}
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.eq(StockDetail::getLpnCode, stockSkuMoveSubmitVO.getSourceLpnCode()));

		if (Func.isNotEmpty(stockDetailList)) {
			Location location = LocationCache.getById(stockDetailList.get(0).getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException("??????????????????????????????");
			}
			stockSkuMoveVO.setSourceLocCode(location.getLocCode());
		}
		return stockSkuMoveVO;
	}

	/**
	 * ????????????id???????????????????????????????????????
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public StockSkuMoveVO getSkuInfoBySkuId(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		//??????id,????????????,??????id,???????????????????????????
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		if (Func.isEmpty(stockSkuMoveSubmitVO.getSourceBoxCode())
			&& Func.isEmpty(stockSkuMoveSubmitVO.getSourceLpnCode())) {
			List<Stock> stocks = stockService.getStockListBySkuAndLoc(stockSkuMoveSubmitVO.getSkuId(),
				stockSkuMoveSubmitVO.getSourceLocCode()
			);
			if (Func.isEmpty(stocks)) {
				throw new ServiceException("??????????????????????????????");
			}
			BigDecimal moveQty = BigDecimal.ZERO;
			for (Stock stock : stocks) {
				moveQty = moveQty.add(stock.getStockQty()
					.subtract(stock.getOccupyQty())
					.subtract(stock.getPickQty()));
			}
			int skuLevel = 0;
			long wspId = 0L;
			for (Stock stock : stocks) {
				if (stock.getSkuLevel() > skuLevel) {
					skuLevel = stock.getSkuLevel();
					wspId = stock.getWspId();
				}
			}
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(stocks.get(0).getWspId())
				.stream()
				.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
				.collect(Collectors.toList());
			//??????????????????
			List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
			for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
				SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //??????????????????
				packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // ????????????id
				packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //??????????????????
				packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //??????????????????
				packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // ????????????
				packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //??????

				if (skuPackageDetail.getSkuLevel() == skuLevel) { //????????????????????????
					stockSkuMoveVO.setDefaultPackageDetail(packageViewVO);
					stockSkuMoveVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //????????????
				}
				packageDetailResultList.add(packageViewVO);
			}
			stockSkuMoveVO.setPackageDetails(packageDetailResultList);
			stockSkuMoveVO.setSkuName(stocks.get(0).getSkuName());
			stockSkuMoveVO.setWhId(stocks.get(0).getWhId());
			stockSkuMoveVO.setStockQty(moveQty);
			stockSkuMoveVO.setStockQtyName(SkuPackageDetailCache.convert1(wspId, skuLevel, moveQty,true));
			return stockSkuMoveVO;
		}
		List<StockDetail> stockDetails = stockService.getStockListBylpnAndbox(stockSkuMoveSubmitVO.getSkuId(),
			stockSkuMoveSubmitVO.getSourceLocCode(),
			stockSkuMoveSubmitVO.getSourceLpnCode(),
			stockSkuMoveSubmitVO.getSourceBoxCode()
		);
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("??????????????????????????????????????????");
		}
		List<Stock> stocks = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, NodesUtil.toList(stockDetails, StockDetail::getStockId)));
		BigDecimal moveQty = BigDecimal.ZERO;
		for (StockDetail stock : stockDetails) {
			moveQty = moveQty.add(stock.getStockQty()
				.subtract(stock.getPickQty()));
		}
		int skuLevel = 0;
		long wspId = 0L;
		for (Stock stock : stocks) {
			if (stock.getSkuLevel() > skuLevel) {
				skuLevel = stock.getSkuLevel();
				wspId = stock.getWspId();
			}
		}
		List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(stocks.get(0).getWspId())
			.stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
			.collect(Collectors.toList());
		//??????????????????
		List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
		for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
			SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //??????????????????
			packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // ????????????id
			packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //??????????????????
			packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //??????????????????
			packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // ????????????
			packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //??????

			if (skuPackageDetail.getSkuLevel() == skuLevel) { //????????????????????????
				stockSkuMoveVO.setDefaultPackageDetail(packageViewVO);
				stockSkuMoveVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //????????????
			}
			packageDetailResultList.add(packageViewVO);
		}
		stockSkuMoveVO.setPackageDetails(packageDetailResultList);
		stockSkuMoveVO.setSkuName(stocks.get(0).getSkuName());
		stockSkuMoveVO.setWhId(stocks.get(0).getWhId());
		stockSkuMoveVO.setStockQty(moveQty);
		stockSkuMoveVO.setStockQtyName(SkuPackageDetailCache.convert1(wspId, skuLevel, moveQty,true));
		return stockSkuMoveVO;
	}


		/*List<Stock> stocks = new ArrayList<>();


		Stock param = new Stock();
		param.setSkuId(stockSkuMoveSubmitVO.getSkuId());
		//???????????????????????????
		if (Func.isNotEmpty(stockSkuMoveSubmitVO.getSourceLocCode())) {// ??????
			//Location location = LocationCache.getByCode(stockSkuMoveSubmitVO.getSourceLocCode());
			ILocationService locationService = SpringUtil.getBean(ILocationService.class);
			Location location = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.eq(Location::getLocCode, stockSkuMoveSubmitVO.getSourceLocCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(location)) {
				stockSkuMoveSubmitVO.setLocId(-1L);
			} else {
				stockSkuMoveSubmitVO.setLocId(location.getLocId());
			}
			param.setLocId(stockSkuMoveSubmitVO.getLocId());
		}
		stocks = super.list(Condition.getQueryWrapper(param));
		int skuLevel = 0;
		long wspId = 0L;
		if (stocks.size() > 0) {
			//????????????
			Sku sku = SkuCache.getById(stocks.get(0).getSkuId());
			if (Func.isNotEmpty(sku)) {
				stockSkuMoveVO.setSkuName(sku.getSkuName());
			}

			//????????????????????????????????????
			for (Stock stock : stocks) {
				if (stock.getSkuLevel() > skuLevel) {
					skuLevel = stock.getSkuLevel();
					wspId = stock.getWspId();
				}
			}
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(stocks.get(0).getWspId())
				.stream()
				.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
				.collect(Collectors.toList());
			//??????????????????
			List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
			for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
				SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //??????????????????
				packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // ????????????id
				packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //??????????????????
				packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //??????????????????
				packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // ????????????
				packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //??????

				if (skuPackageDetail.getSkuLevel() == skuLevel) { //????????????????????????
					stockSkuMoveVO.setDefaultPackageDetail(packageViewVO);
					stockSkuMoveVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //????????????
				}
				packageDetailResultList.add(packageViewVO);
			}
			stockSkuMoveVO.setPackageDetails(packageDetailResultList);

			//?????????????????????
			BigDecimal moveQty = BigDecimal.ZERO;
			for (Stock stock : stocks) {
				moveQty = moveQty.add(stock.getStockQty()
					.subtract(stock.getOccupyQty())
					.subtract(stock.getPickQty()));
			}
			stockSkuMoveVO.setWhId(stocks.get(0).getWhId());
			stockSkuMoveVO.setStockQty(moveQty);
			stockSkuMoveVO.setStockQtyName(skuUmService.convert(wspId, skuLevel, moveQty));
		} else {
			throw new ServiceException("?????????????????????????????????");
		}*/

//		return stockSkuMoveVO;
//}

	/**
	 * ??????????????????
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public StockSkuMoveVO verifySkuQty(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		//??????id,????????????,??????id,???????????????????????????
		this.verifyStockSkuMoveSubmit(stockSkuMoveSubmitVO);
		Sku sku = SkuCache.getById(stockSkuMoveSubmitVO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("??????????????????????????????");
		}

		boolean isSn = 1 == sku.getIsSn();
//		if (!isSn) {
//			if (Func.isEmpty(stockSkuMoveSubmitVO.getWspdId())) {
//				throw new ServiceException("????????????ID????????????:???????????????");
//			}
//		}

		// ??????????????????????????????
		Location location = LocationCache.getValid(stockSkuMoveSubmitVO.getSourceLocCode());
		Warehouse warehouse = WarehouseCache.getById(location.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}

		if (Func.isEmpty(location)) {
			throw new ServiceException(String.format("??????[%s]???????????????[%s]",
				warehouse.getWhName(),
				stockSkuMoveSubmitVO.getSourceLocCode()));
		}

		//??????????????????????????????
//		Stock param = new Stock();
//		param.setSkuId(stockSkuMoveSubmitVO.getSkuId());
//		param.setLpnCode(stockSkuMoveSubmitVO.getSourceLpnCode());
//		param.setWhId(location.getWhId());
//		param.setLocId(stockSkuMoveSubmitVO.getLocId());
//		List<Stock> stocks = super.list(Condition.getQueryWrapper(param)
//			.apply("stock_qty - occupy_qty - pick_qty > 0"));

		List<StockDetail> stocks = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.eq(StockDetail::getWhId, location.getWhId())
			.eq(StockDetail::getSkuId, stockSkuMoveSubmitVO.getSkuId())
			.eq(StockDetail::getLocId, location.getLocId())
			.func(sql -> {
				if (Func.isNotEmpty(stockSkuMoveSubmitVO.getSourceLpnCode()) && Func.isNotEmpty(stockSkuMoveSubmitVO.getSourceBoxCode())) {
					sql.eq(StockDetail::getLpnCode, stockSkuMoveSubmitVO.getSourceLpnCode()).
						eq(StockDetail::getBoxCode, stockSkuMoveSubmitVO.getSourceBoxCode());
				} else if (Func.isNotEmpty(stockSkuMoveSubmitVO.getSourceLpnCode())) {
					sql.eq(StockDetail::getLpnCode, stockSkuMoveSubmitVO.getSourceLpnCode());
				} else if (Func.isNotEmpty(stockSkuMoveSubmitVO.getSourceBoxCode())) {
					sql.eq(StockDetail::getBoxCode, stockSkuMoveSubmitVO.getSourceBoxCode());
				}
			}));
		List<Stock> stockList = new ArrayList<>();
		if (Func.isNotEmpty(stocks)) {
			stockList = super.list(Wrappers.lambdaQuery(Stock.class).in(Stock::getStockId, NodesUtil.toList(stocks, StockDetail::getStockId)));
		}
		if (isSn) {
			if (Func.isEmpty(stockSkuMoveSubmitVO.getSerialList()) || stockSkuMoveSubmitVO.getSerialList().size() == 0) {
				throw new ServiceException("????????????????????????");
			}
			if (stocks.size() > 0) {
				List<Serial> serialList = serialService.list(Condition.getQueryWrapper(new Serial())
					.lambda()
					.in(Serial::getStockId, NodesUtil.toList(stocks, Stock::getStockId)));

				if (Func.isEmpty(serialList)) {
					throw new ServiceException(String.format(
						"??????[%s]??????????????????[%s]! ",
						stockSkuMoveSubmitVO.getSourceLpnCode(),
						StringUtil.join(stockSkuMoveSubmitVO.getSerialList())));
				}
				for (String serialNumber : stockSkuMoveSubmitVO.getSerialList()) {
					Serial serial = serialList.stream().filter(u -> {
						return u.getSerialNumber().equals(serialNumber);
					}).findFirst().orElse(null);
					if (Func.isEmpty(serial)) {
						throw new ServiceException(String.format(
							"??????[%s]??????????????????[%s]", stockSkuMoveSubmitVO.getSourceLpnCode(), serialNumber));
					}
				}
			}
		}

		if (Func.isEmpty(stocks.size())) {
			throw new ServiceException(String.format("??????[%s]??????[%s]???????????????[%s]???????????????",
				stockSkuMoveSubmitVO.getSourceLocCode(),
				stockSkuMoveSubmitVO.getSourceLpnCode(),
				sku.getSkuName()));
		}
		// ?????????????????????????????????
		BigDecimal moveQty = BigDecimal.ZERO;
		for (StockDetail stock : stocks) {
			moveQty = moveQty.add(stock.getStockQty()
				.subtract(stock.getPickQty()));
		}
		if (stockSkuMoveSubmitVO.getMoveQty().compareTo(moveQty) > 0) {
			throw new ServiceException("???????????????????????????");
		}
		// ??????????????????????????????
		List<String> LotNumber = new ArrayList<>();//?????????????????????
		stockList = stockList.stream().filter(
			v -> {
				boolean flag = !LotNumber.contains(v.getLotNumber());
				LotNumber.add(v.getLotNumber());
				return flag;
			}
		).collect(Collectors.toList());

		// ???????????????????????????
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		//????????????????????????
		List<SkuStockLotMoveVO> list = new ArrayList<>();
		for (Stock stock : stockList) {
			SkuStockLotMoveVO skuStockLotMoveVO = new SkuStockLotMoveVO();
			skuStockLotMoveVO.setLotNumber(stock.getLotNumber()); //?????????
			// ????????????
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(SkuCache.getById(stock.getSkuId()).getWoId());
			skuStockLotMoveVO.setOwnerName(Func.isEmpty(owner)
				? "" : owner.getOwnerName());//??????
			skuStockLotMoveVO.setStockId(stock.getStockId());//??????id
			Sku skuCache = SkuCache.getById(stock.getSkuId());
			if (Func.isEmpty(skuCache)) {
				throw new ServiceException("??????????????????????????????");
			}
			skuStockLotMoveVO.setSkuName(skuCache.getSkuName()); //????????????

			Stock stockParam = new Stock();
			stockParam.setSkuId(stock.getSkuId());
//			stockParam.setLpnCode(stock.getLpnCode());
			stockParam.setWhId(stock.getWhId());
			stockParam.setLocId(stock.getLocId());
			stockParam.setLotNumber(stock.getLotNumber());
			List<Stock> stockLotParams = super.list(Condition.getQueryWrapper(stockParam));
			//???????????????????????????
			BigDecimal moveLotQty = BigDecimal.ZERO;
			for (Stock stockLotParam : stockLotParams) {
				moveLotQty = moveLotQty.add(stockLotParam.getStockQty()
					.subtract(stockLotParam.getOccupyQty())
					.subtract(stockLotParam.getPickQty()));
			}
			//set ????????????
			skuStockLotMoveVO.setStockQty(moveLotQty);
			Location sourceLoc = LocationCache.getById(stock.getLocId());
			if (Func.isEmpty(sourceLoc)) {
				throw new ServiceException("??????????????????????????????");
			}
			skuStockLotMoveVO.setSourceLocCode(sourceLoc.getLocCode()); //?????????
//			skuStockLotMoveVO.setSourceLpnCode(stock.getLpnCode()); //?????????
			skuStockLotMoveVO.setSkuId(stock.getSkuId()); //??????id
			skuStockLotMoveVO.setWhId(stock.getWhId()); //??????Id
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(stocks.get(0).getWspId())
				.stream()
				.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
				.collect(Collectors.toList());
			//??????????????????
			List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
			for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
				SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //??????????????????
				packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // ????????????id
				packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //??????????????????
				packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //??????????????????
				packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // ????????????
				packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //??????

				if (skuPackageDetail.getSkuLevel() == 1) { //???????????????????????????
					skuStockLotMoveVO.setDefaultPackageDetail(packageViewVO);//??????????????????
					skuStockLotMoveVO.setSkuSpec("1-1"); //????????????
				}
				packageDetailResultList.add(packageViewVO);
			}
			skuStockLotMoveVO.setPackageDetails(packageDetailResultList);

			try {
				//??????????????????map
				Map<String, Object> skuLotMap = SkuLotWrapper.skuLotToMap(stock);
				//???????????????????????????
				List<SkuLotConfigVO> skuLotlist = skuLotService.getConfig(stock.getSkuId());
				for (SkuLotConfigVO skuLotAndLotValVO : skuLotlist) {
					//?????????????????????????????????????????????????????????????????????
					if (Func.isNotEmpty(skuLotMap.get("skuLot" + skuLotAndLotValVO.getSkuLotIndex()))) {
						skuLotAndLotValVO.setSkuLotValue(skuLotMap.get("skuLot" + skuLotAndLotValVO.getSkuLotIndex()).toString());
					}
				}
				skuStockLotMoveVO.setSkuLotList(skuLotlist);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(skuStockLotMoveVO);
		}
		stockSkuMoveVO.setSkuStockLotMoveVOs(list);
		return stockSkuMoveVO;
	}

	/**
	 * ??????????????????
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public StockSkuMoveVO verifyLotQty(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		this.verifyStockSkuMoveSubmit(stockSkuMoveSubmitVO);
		Sku sku = SkuCache.getById(stockSkuMoveSubmitVO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("??????????????????????????????");
		}
		boolean isSn = 1 == sku.getIsSn();
		if (!isSn) {
//			if (Func.isEmpty(stockSkuMoveSubmitVO.getWspdId())) {
//				throw new ServiceException("????????????ID????????????:???????????????");
//			}
			if (Func.isEmpty(stockSkuMoveSubmitVO.getLotNumber())) {
				throw new ServiceException("?????????????????????:???????????????");
			}
		}
		Location location = LocationCache.getValid(stockSkuMoveSubmitVO.getSourceLocCode());
		Warehouse warehouse = WarehouseCache.getById(location.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}

		if (Func.isEmpty(location)) {
			throw new ServiceException(String.format("??????[%s]???????????????[%s]",
				warehouse.getWhName(),
				stockSkuMoveSubmitVO.getSourceLocCode()));
		}

		//??????????????????
		List<Stock> stocks = super.list(Condition.getQueryWrapper(new Stock()).lambda()
			.eq(Stock::getSkuId, stockSkuMoveSubmitVO.getSkuId())
			.eq(Stock::getLocId, location.getLocId())
			.eq(Stock::getLotNumber, stockSkuMoveSubmitVO.getLotNumber()));

		//?????????????????????
		BigDecimal moveQty = BigDecimal.ZERO;
		for (Stock stock : stocks) {
			moveQty = moveQty.add(stock.getStockQty()
				.subtract(stock.getOccupyQty())
				.subtract(stock.getPickQty()));
		}
//		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(stockSkuMoveSubmitVO.getWspdId());
//		if (Func.isEmpty(skuPackageDetail)) {
//			throw new ServiceException("????????????????????????????????????");
//		}

		if (isSn) {
			if (stockSkuMoveSubmitVO.getMoveQty().compareTo(moveQty) > 0) {
				throw new ServiceException("???????????????????????????");
			}
			if (BigDecimalUtil.gt(stockSkuMoveSubmitVO.getMoveQty(), moveQty)) {
				throw new ServiceException("???????????????????????????");
			} else if (BigDecimalUtil.eq(stockSkuMoveSubmitVO.getMoveQty(), moveQty)) {
				stockSkuMoveVO.setStockQty(moveQty);
			} else {
				stockSkuMoveVO.setStockQty(moveQty
					.subtract(stockSkuMoveSubmitVO.getMoveQty()));
			}
		} else {
//			BigDecimal multiply = stockSkuMoveSubmitVO.getMoveQty()
//				.multiply(new BigDecimal(skuPackageDetail.getConvertQty()));
			if (BigDecimalUtil.gt(stockSkuMoveSubmitVO.getMoveQty(), moveQty)) {
				throw new ServiceException("???????????????????????????");
			} else if (BigDecimalUtil.eq(stockSkuMoveSubmitVO.getMoveQty(), moveQty)) {
				stockSkuMoveVO.setStockQty(moveQty);
			} else {
				stockSkuMoveVO.setStockQty(moveQty
					.subtract(stockSkuMoveSubmitVO.getMoveQty()));
			}
		}
		return stockSkuMoveVO;
	}

	/**
	 * ????????????
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public synchronized boolean moveStockSku(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {

		Sku sku = SkuCache.getById(stockSkuMoveSubmitVO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("??????????????????????????????");
		}

		boolean isSn = 1 == sku.getIsSn();
		if (Func.isEmpty(stockSkuMoveSubmitVO.getSerialList()) && isSn) {
			throw new ServiceException("???????????????");
		}
//		if (Func.isEmpty(stockSkuMoveSubmitVO.getWspdId())) {
//			throw new ServiceException("????????????ID????????????:???????????????");
//		}

		//??????????????????
		Location sourceLocation = LocationCache.getValid(stockSkuMoveSubmitVO.getSourceLocCode());

		//??????????????????
		Location targetLocation = LocationCache.getValid(stockSkuMoveSubmitVO.getTargetLocCode());
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone zone = zoneService.getById(targetLocation.getZoneId());
		if(Func.isNotEmpty(zone)&&zone.getZoneType()==74){
			throw new ServiceException("?????????????????????????????????");
		}
		if (!sourceLocation.getWhId().equals(targetLocation.getWhId())) {
			throw new ServiceException(String.format(
				"?????????[%s]???????????????[%s]??????????????????????????????????????????????????????",
				sourceLocation.getLocCode(), targetLocation.getLocCode()));
		}
//		if (stockSkuMoveSubmitVO.getSourceLpnCode().equals(stockSkuMoveSubmitVO.getTargetLpnCode())) {
//			throw new ServiceException("????????????????????????????????????");
//		}

		Long systemProcId = this.saveMoveSystemLog(stockSkuMoveSubmitVO);

		//????????????
		BigDecimal moveQty;
//		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(stockSkuMoveSubmitVO.getWspdId());
//		if (Func.isEmpty(skuPackageDetail)) {
//			throw new ServiceException("????????????????????????????????????");
//		}

//		if (isSn) {
//			moveQty = stockSkuMoveSubmitVO.getMoveQty();
//		} else {
//			moveQty = stockSkuMoveSubmitVO.getMoveQty().multiply(new BigDecimal(skuPackageDetail.getConvertQty()));
//		}
		stockSkuMoveSubmitVO.setMoveQty(stockSkuMoveSubmitVO.getMoveQty());
		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setLotNumber(stockSkuMoveSubmitVO.getLotNumber());
		stockMoveDTO.setTargetLpnCode(stockSkuMoveSubmitVO.getTargetLpnCode());
		stockMoveDTO.setTargetBoxCode(stockSkuMoveSubmitVO.getTargetBoxCode());
		stockMoveDTO.setSourceLpnCode(stockSkuMoveSubmitVO.getSourceLpnCode());
		stockMoveDTO.setSourceBoxCode(stockSkuMoveSubmitVO.getSourceBoxCode());
		stockMoveDTO.setSourceLocId(sourceLocation.getLocId());
		stockMoveDTO.setTargetLocId(targetLocation.getLocId());
		stockMoveDTO.setSkuId(stockSkuMoveSubmitVO.getSkuId());
		stockMoveDTO.setSerialList(stockSkuMoveSubmitVO.getSerialList());
		stockMoveDTO.setMoveQty(stockSkuMoveSubmitVO.getMoveQty());
		stockMoveDTO.setEventType(EventTypeEnum.Move);
		stockMoveDTO.setSystemProcId(systemProcId);
		List<Stock> stockList = stockService.stockMove(stockMoveDTO);
		this.saveTransferRecord(stockSkuMoveSubmitVO, stockList);
		return true;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	private Long saveMoveSystemLog(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		// ??????????????????
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.MOVE);
		systemProcParam.setAction(ActionEnum.MOVE_SKU);

		Location location = LocationCache.getByCode(stockSkuMoveSubmitVO.getSourceLocCode());
		systemProcParam.setWhId(location.getWhId());
		systemProcParam.setOperationQty(stockSkuMoveSubmitVO.getMoveQty());
		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(stockSkuMoveSubmitVO.getWspdId());
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException("????????????????????????????????????");
		}
		systemProcParam.setOperationUnit(skuPackageDetail.getWsuName());
		SystemProc systemProc = systemProcService.create(systemProcParam);
		return systemProc.getSystemProcId();
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	boolean saveTransferRecord(StockSkuMoveSubmitVO stockSkuMoveSubmitVO, List<Stock> stockList) {
		List<TransferRecord> recordList = new ArrayList<>();
		stockList.forEach(stock -> {
			TransferRecord transferRecord = new TransferRecord();
			transferRecord.setTransferCode("");
			transferRecord.setTransferType(TransferTypeEnum.INNER_WAREHOUSE.getIndex());
			transferRecord.setWoId(stockSkuMoveSubmitVO.getWoId());// ?????????
			transferRecord.setSkuId(stockSkuMoveSubmitVO.getSkuId()); //?????????
			transferRecord.setFromLpn(stockSkuMoveSubmitVO.getSourceLpnCode()); // ?????????
			Location sourceLoc = LocationCache.getByCode(stockSkuMoveSubmitVO.getSourceLocCode());
			if (Func.isEmpty(sourceLoc)) {
				throw new ServiceException("??????????????????????????????");
			}
			transferRecord.setFromLocId(sourceLoc.getLocId());
			transferRecord.setFromLocCode(stockSkuMoveSubmitVO.getSourceLocCode());//???????????????
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone sourceZone = zoneService.getById(sourceLoc.getZoneId());
			if (Func.isNotEmpty(sourceZone)) {
				transferRecord.setFromZoneCode(sourceZone.getZoneCode());// ???????????????
				transferRecord.setFromZoneName(sourceZone.getZoneName());// ???????????????
			}
			transferRecord.setQty(stockSkuMoveSubmitVO.getMoveQty()); // ?????????
			transferRecord.setWspId(stock.getWspId()); //?????????Id
			List<SkuPackageDetail> packageDetails = SkuPackageDetailCache.listByWspId(stock.getWspId())
				.stream()
				.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
				.collect(Collectors.toList());
			for (SkuPackageDetail packageDetail : packageDetails) {
				if (packageDetail.getSkuLevel() == 1) {
					transferRecord.setWsuCode(packageDetail.getWsuCode());
					transferRecord.setWsuName(packageDetail.getWsuName());//???????????????
				}
			}
			transferRecord.setSkuLevel(1);//?????????
			transferRecord.setToLpn(stockSkuMoveSubmitVO.getTargetLpnCode()); // ??????????????????
			transferRecord.setToLocCode(stockSkuMoveSubmitVO.getTargetLocCode());//??????????????????
			Location targetLoc = LocationCache.getByCode(stockSkuMoveSubmitVO.getSourceLocCode());
			if (Func.isEmpty(targetLoc)) {
				throw new ServiceException("??????????????????????????????");
			}
			transferRecord.setToLocId(targetLoc.getLocId());
			Zone targetZone = zoneService.getById(targetLoc.getZoneId());
			if (Func.isNotEmpty(targetZone)) {
				transferRecord.setToZoneCode(targetZone.getZoneCode());
				transferRecord.setToZoneName(targetZone.getZoneName());
			}

			transferRecord.setLotNumber(stockSkuMoveSubmitVO.getLotNumber());//?????????

			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (stock.skuLotChk(i)) {
					transferRecord.skuLotSet(i, stock.skuLotGet(i));
				}
			}
			recordList.add(transferRecord);
		});

		return transferRecordService.saveBatch(recordList, recordList.size());
	}

	/**
	 * ????????????
	 *
	 * @param stockSkuMove
	 */
	private void verifyStockSkuMoveSubmit(StockSkuMoveSubmitVO stockSkuMove) {
		if (stockSkuMove.getMoveQty().compareTo(BigDecimal.ONE) < 0) {
			throw new ServiceException("?????????????????????");
		}
		//??????id,????????????,??????id,???????????????????????????
		if (Func.isEmpty(stockSkuMove.getSkuId())) {
			throw new ServiceException("??????ID????????????");
		}
		if (Func.isEmpty(stockSkuMove.getSourceLocCode())) {
			throw new ServiceException("????????????????????????");
		}
		if (Func.isEmpty(SkuCache.getById(stockSkuMove.getSkuId()))) {
			throw new ServiceException("?????????????????????");
		}
	}


	@Override
	public boolean submitSkuInfoForBox(StockMoveByBoxQueryDTO stockMoveByBoxQueryDTO) {
		return false;
	}

}
