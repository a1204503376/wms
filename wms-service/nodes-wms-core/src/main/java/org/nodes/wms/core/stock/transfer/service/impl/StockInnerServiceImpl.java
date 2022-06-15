package org.nodes.wms.core.stock.transfer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
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
import org.nodes.wms.core.stock.core.entity.Stock;
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
 * 库存内部接口实现类
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
	 * 查询库房列表
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
	 * 通过容器编码查询库位编号
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public StockSkuMoveVO getLocCodeByLpnCode(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		if (Func.isEmpty(stockSkuMoveSubmitVO.getSourceLpnCode())) {
			throw new ServiceException("容器编码不能为空");
		}
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.eq(StockDetail::getLpnCode, stockSkuMoveSubmitVO.getSourceLpnCode()));

		if (Func.isNotEmpty(stockDetailList)) {
			Location location = LocationCache.getById(stockDetailList.get(0).getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException("库位不存在或已经删除");
			}
			stockSkuMoveVO.setSourceLocCode(location.getLocCode());
		}
		return stockSkuMoveVO;
	}

	/**
	 * 通过物品id与容器编码查询库存物品信息
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public StockSkuMoveVO getSkuInfoBySkuId(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		//物品id,库位编码,库房id,与容器编码不可为空
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		if (Func.isEmpty(stockSkuMoveSubmitVO.getSourceBoxCode())
			&& Func.isEmpty(stockSkuMoveSubmitVO.getSourceLpnCode())) {
			List<Stock> stocks = stockService.getStockListBySkuAndLoc(stockSkuMoveSubmitVO.getSkuId(),
				stockSkuMoveSubmitVO.getSourceLocCode()
			);
			if (Func.isEmpty(stocks)) {
				throw new ServiceException("该库位暂无该物品库存");
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
			//包装列表返回
			List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
			for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
				SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //默认包装明细
				packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // 包装明细id
				packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //计量单位编码
				packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //计量单位名称
				packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // 换算倍率
				packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //层级

				if (skuPackageDetail.getSkuLevel() == skuLevel) { //显示最大包装层级
					stockSkuMoveVO.setDefaultPackageDetail(packageViewVO);
					stockSkuMoveVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //默认规格
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
			throw new ServiceException("系统没有查询到可移动的库存！");
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
		//包装列表返回
		List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
		for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
			SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //默认包装明细
			packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // 包装明细id
			packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //计量单位编码
			packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //计量单位名称
			packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // 换算倍率
			packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //层级

			if (skuPackageDetail.getSkuLevel() == skuLevel) { //显示最大包装层级
				stockSkuMoveVO.setDefaultPackageDetail(packageViewVO);
				stockSkuMoveVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //默认规格
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
		//判断源库位是否存在
		if (Func.isNotEmpty(stockSkuMoveSubmitVO.getSourceLocCode())) {// 存在
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
			//物品名称
			Sku sku = SkuCache.getById(stocks.get(0).getSkuId());
			if (Func.isNotEmpty(sku)) {
				stockSkuMoveVO.setSkuName(sku.getSkuName());
			}

			//获取库存中最大的包装层级
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
			//包装列表返回
			List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
			for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
				SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //默认包装明细
				packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // 包装明细id
				packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //计量单位编码
				packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //计量单位名称
				packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // 换算倍率
				packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //层级

				if (skuPackageDetail.getSkuLevel() == skuLevel) { //显示最大包装层级
					stockSkuMoveVO.setDefaultPackageDetail(packageViewVO);
					stockSkuMoveVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //默认规格
				}
				packageDetailResultList.add(packageViewVO);
			}
			stockSkuMoveVO.setPackageDetails(packageDetailResultList);

			//库存可移动数量
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
			throw new ServiceException("该库位暂无该物品库存！");
		}*/

//		return stockSkuMoveVO;
//}

	/**
	 * 校验物品数量
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public StockSkuMoveVO verifySkuQty(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		//物品id,库位编码,库房id,与容器编码不可为空
		this.verifyStockSkuMoveSubmit(stockSkuMoveSubmitVO);
		Sku sku = SkuCache.getById(stockSkuMoveSubmitVO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或已经删除");
		}

		boolean isSn = 1 == sku.getIsSn();
//		if (!isSn) {
//			if (Func.isEmpty(stockSkuMoveSubmitVO.getWspdId())) {
//				throw new ServiceException("包装明细ID不能为空:请前端校验");
//			}
//		}

		// 验证库位是否该库房内
		Location location = LocationCache.getValid(stockSkuMoveSubmitVO.getSourceLocCode());
		Warehouse warehouse = WarehouseCache.getById(location.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或已经删除");
		}

		if (Func.isEmpty(location)) {
			throw new ServiceException(String.format("库房[%s]内没有库位[%s]",
				warehouse.getWhName(),
				stockSkuMoveSubmitVO.getSourceLocCode()));
		}

		//查询库存列表批次列表
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
				throw new ServiceException("请扫描物品序列号");
			}
			if (stocks.size() > 0) {
				List<Serial> serialList = serialService.list(Condition.getQueryWrapper(new Serial())
					.lambda()
					.in(Serial::getStockId, NodesUtil.toList(stocks, Stock::getStockId)));

				if (Func.isEmpty(serialList)) {
					throw new ServiceException(String.format(
						"容器[%s]中没有序列号[%s]! ",
						stockSkuMoveSubmitVO.getSourceLpnCode(),
						StringUtil.join(stockSkuMoveSubmitVO.getSerialList())));
				}
				for (String serialNumber : stockSkuMoveSubmitVO.getSerialList()) {
					Serial serial = serialList.stream().filter(u -> {
						return u.getSerialNumber().equals(serialNumber);
					}).findFirst().orElse(null);
					if (Func.isEmpty(serial)) {
						throw new ServiceException(String.format(
							"容器[%s]中没有序列号[%s]", stockSkuMoveSubmitVO.getSourceLpnCode(), serialNumber));
					}
				}
			}
		}

		if (Func.isEmpty(stocks.size())) {
			throw new ServiceException(String.format("库位[%s]容器[%s]中没有物品[%s]的库存信息",
				stockSkuMoveSubmitVO.getSourceLocCode(),
				stockSkuMoveSubmitVO.getSourceLpnCode(),
				sku.getSkuName()));
		}
		// 校验数量是否超过总数量
		BigDecimal moveQty = BigDecimal.ZERO;
		for (StockDetail stock : stocks) {
			moveQty = moveQty.add(stock.getStockQty()
				.subtract(stock.getPickQty()));
		}
		if (stockSkuMoveSubmitVO.getMoveQty().compareTo(moveQty) > 0) {
			throw new ServiceException("超过可移动库存数量");
		}
		// 库存中批次号列表去重
		List<String> LotNumber = new ArrayList<>();//临时存放批次号
		stockList = stockList.stream().filter(
			v -> {
				boolean flag = !LotNumber.contains(v.getLotNumber());
				LotNumber.add(v.getLotNumber());
				return flag;
			}
		).collect(Collectors.toList());

		// 查询物品名称等信息
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		//存放物品批次列表
		List<SkuStockLotMoveVO> list = new ArrayList<>();
		for (Stock stock : stockList) {
			SkuStockLotMoveVO skuStockLotMoveVO = new SkuStockLotMoveVO();
			skuStockLotMoveVO.setLotNumber(stock.getLotNumber()); //批次号
			// 获得货主
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(SkuCache.getById(stock.getSkuId()).getWoId());
			skuStockLotMoveVO.setOwnerName(Func.isEmpty(owner)
				? "" : owner.getOwnerName());//货主
			skuStockLotMoveVO.setStockId(stock.getStockId());//库存id
			Sku skuCache = SkuCache.getById(stock.getSkuId());
			if (Func.isEmpty(skuCache)) {
				throw new ServiceException("物品不存在或已经删除");
			}
			skuStockLotMoveVO.setSkuName(skuCache.getSkuName()); //物品名称

			Stock stockParam = new Stock();
			stockParam.setSkuId(stock.getSkuId());
//			stockParam.setLpnCode(stock.getLpnCode());
			stockParam.setWhId(stock.getWhId());
			stockParam.setLocId(stock.getLocId());
			stockParam.setLotNumber(stock.getLotNumber());
			List<Stock> stockLotParams = super.list(Condition.getQueryWrapper(stockParam));
			//库存可移动数量计算
			BigDecimal moveLotQty = BigDecimal.ZERO;
			for (Stock stockLotParam : stockLotParams) {
				moveLotQty = moveLotQty.add(stockLotParam.getStockQty()
					.subtract(stockLotParam.getOccupyQty())
					.subtract(stockLotParam.getPickQty()));
			}
			//set 库存数量
			skuStockLotMoveVO.setStockQty(moveLotQty);
			Location sourceLoc = LocationCache.getById(stock.getLocId());
			if (Func.isEmpty(sourceLoc)) {
				throw new ServiceException("库位不存在或已经删除");
			}
			skuStockLotMoveVO.setSourceLocCode(sourceLoc.getLocCode()); //源库位
//			skuStockLotMoveVO.setSourceLpnCode(stock.getLpnCode()); //源容器
			skuStockLotMoveVO.setSkuId(stock.getSkuId()); //物品id
			skuStockLotMoveVO.setWhId(stock.getWhId()); //库房Id
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(stocks.get(0).getWspId())
				.stream()
				.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
				.collect(Collectors.toList());
			//包装列表返回
			List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
			for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
				SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //默认包装明细
				packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // 包装明细id
				packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //计量单位编码
				packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //计量单位名称
				packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // 换算倍率
				packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //层级

				if (skuPackageDetail.getSkuLevel() == 1) { //只显示最低层级包装
					skuStockLotMoveVO.setDefaultPackageDetail(packageViewVO);//最低包装明细
					skuStockLotMoveVO.setSkuSpec("1-1"); //默认规格
				}
				packageDetailResultList.add(packageViewVO);
			}
			skuStockLotMoveVO.setPackageDetails(packageDetailResultList);

			try {
				//批属性内容转map
				Map<String, Object> skuLotMap = SkuLotWrapper.skuLotToMap(stock);
				//获得批属性验证规则
				List<SkuLotConfigVO> skuLotlist = skuLotService.getConfig(stock.getSkuId());
				for (SkuLotConfigVO skuLotAndLotValVO : skuLotlist) {
					//通过索引获得批属性的值并存入批属性验证规则列表
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
	 * 校验批次数量
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
			throw new ServiceException("物品不存在或已经删除");
		}
		boolean isSn = 1 == sku.getIsSn();
		if (!isSn) {
//			if (Func.isEmpty(stockSkuMoveSubmitVO.getWspdId())) {
//				throw new ServiceException("包装明细ID不能为空:请前端校验");
//			}
			if (Func.isEmpty(stockSkuMoveSubmitVO.getLotNumber())) {
				throw new ServiceException("批次号不能为空:请前端校验");
			}
		}
		Location location = LocationCache.getValid(stockSkuMoveSubmitVO.getSourceLocCode());
		Warehouse warehouse = WarehouseCache.getById(location.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或已经删除");
		}

		if (Func.isEmpty(location)) {
			throw new ServiceException(String.format("库房[%s]内没有库位[%s]",
				warehouse.getWhName(),
				stockSkuMoveSubmitVO.getSourceLocCode()));
		}

		//查询库存列表
		List<Stock> stocks = super.list(Condition.getQueryWrapper(new Stock()).lambda()
			.eq(Stock::getSkuId, stockSkuMoveSubmitVO.getSkuId())
			.eq(Stock::getLocId, location.getLocId())
			.eq(Stock::getLotNumber, stockSkuMoveSubmitVO.getLotNumber()));

		//库存可移动数量
		BigDecimal moveQty = BigDecimal.ZERO;
		for (Stock stock : stocks) {
			moveQty = moveQty.add(stock.getStockQty()
				.subtract(stock.getOccupyQty())
				.subtract(stock.getPickQty()));
		}
//		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(stockSkuMoveSubmitVO.getWspdId());
//		if (Func.isEmpty(skuPackageDetail)) {
//			throw new ServiceException("包装明细不存在或已经删除");
//		}

		if (isSn) {
			if (stockSkuMoveSubmitVO.getMoveQty().compareTo(moveQty) > 0) {
				throw new ServiceException("超过可移动库存数量");
			}
			if (BigDecimalUtil.gt(stockSkuMoveSubmitVO.getMoveQty(), moveQty)) {
				throw new ServiceException("超过可移动库存数量");
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
				throw new ServiceException("超过可移动库存数量");
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
	 * 移动库存
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	@Override
	public synchronized boolean moveStockSku(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {

		Sku sku = SkuCache.getById(stockSkuMoveSubmitVO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或已经删除");
		}

		boolean isSn = 1 == sku.getIsSn();
		if (Func.isEmpty(stockSkuMoveSubmitVO.getSerialList()) && isSn) {
			throw new ServiceException("序列号为空");
		}
//		if (Func.isEmpty(stockSkuMoveSubmitVO.getWspdId())) {
//			throw new ServiceException("包装明细ID不能为空:请前端校验");
//		}

		//验证目标库位
		Location sourceLocation = LocationCache.getValid(stockSkuMoveSubmitVO.getSourceLocCode());

		//验证目标库位
		Location targetLocation = LocationCache.getValid(stockSkuMoveSubmitVO.getTargetLocCode());
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone zone = zoneService.getById(targetLocation.getZoneId());
		if(Func.isNotEmpty(zone)&&zone.getZoneType()==74){
			throw new ServiceException("不能移动到出库暂存区！");
		}
		if (!sourceLocation.getWhId().equals(targetLocation.getWhId())) {
			throw new ServiceException(String.format(
				"源库位[%s]与目标库位[%s]不属于同一个库房，不允许执行此操作！",
				sourceLocation.getLocCode(), targetLocation.getLocCode()));
		}
//		if (stockSkuMoveSubmitVO.getSourceLpnCode().equals(stockSkuMoveSubmitVO.getTargetLpnCode())) {
//			throw new ServiceException("目标容器与原容器不能相同");
//		}

		Long systemProcId = this.saveMoveSystemLog(stockSkuMoveSubmitVO);

		//移动数量
		BigDecimal moveQty;
//		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(stockSkuMoveSubmitVO.getWspdId());
//		if (Func.isEmpty(skuPackageDetail)) {
//			throw new ServiceException("包装明细不存在或已经删除");
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
	 * 保存移动的系统日志
	 *
	 * @param stockSkuMoveSubmitVO
	 * @return
	 */
	private Long saveMoveSystemLog(StockSkuMoveSubmitVO stockSkuMoveSubmitVO) {
		// 创建系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.MOVE);
		systemProcParam.setAction(ActionEnum.MOVE_SKU);

		Location location = LocationCache.getByCode(stockSkuMoveSubmitVO.getSourceLocCode());
		systemProcParam.setWhId(location.getWhId());
		systemProcParam.setOperationQty(stockSkuMoveSubmitVO.getMoveQty());
		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(stockSkuMoveSubmitVO.getWspdId());
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException("包装明细不存在或已经删除");
		}
		systemProcParam.setOperationUnit(skuPackageDetail.getWsuName());
		SystemProc systemProc = systemProcService.create(systemProcParam);
		return systemProc.getSystemProcId();
	}

	/**
	 * 保存移动记录
	 *
	 * @return
	 */
	boolean saveTransferRecord(StockSkuMoveSubmitVO stockSkuMoveSubmitVO, List<Stock> stockList) {
		List<TransferRecord> recordList = new ArrayList<>();
		stockList.forEach(stock -> {
			TransferRecord transferRecord = new TransferRecord();
			transferRecord.setTransferCode("");
			transferRecord.setTransferType(TransferTypeEnum.INNER_WAREHOUSE.getIndex());
			transferRecord.setWoId(stockSkuMoveSubmitVO.getWoId());// 货主从
			transferRecord.setSkuId(stockSkuMoveSubmitVO.getSkuId()); //原货品
			transferRecord.setFromLpn(stockSkuMoveSubmitVO.getSourceLpnCode()); // 源容器
			Location sourceLoc = LocationCache.getByCode(stockSkuMoveSubmitVO.getSourceLocCode());
			if (Func.isEmpty(sourceLoc)) {
				throw new ServiceException("库位不存在或已经删除");
			}
			transferRecord.setFromLocId(sourceLoc.getLocId());
			transferRecord.setFromLocCode(stockSkuMoveSubmitVO.getSourceLocCode());//原库位编码
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone sourceZone = zoneService.getById(sourceLoc.getZoneId());
			if (Func.isNotEmpty(sourceZone)) {
				transferRecord.setFromZoneCode(sourceZone.getZoneCode());// 原库区编码
				transferRecord.setFromZoneName(sourceZone.getZoneName());// 原库区名称
			}
			transferRecord.setQty(stockSkuMoveSubmitVO.getMoveQty()); // 原数量
			transferRecord.setWspId(stock.getWspId()); //原包装Id
			List<SkuPackageDetail> packageDetails = SkuPackageDetailCache.listByWspId(stock.getWspId())
				.stream()
				.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
				.collect(Collectors.toList());
			for (SkuPackageDetail packageDetail : packageDetails) {
				if (packageDetail.getSkuLevel() == 1) {
					transferRecord.setWsuCode(packageDetail.getWsuCode());
					transferRecord.setWsuName(packageDetail.getWsuName());//原计量单位
				}
			}
			transferRecord.setSkuLevel(1);//原层级
			transferRecord.setToLpn(stockSkuMoveSubmitVO.getTargetLpnCode()); // 目标容器编码
			transferRecord.setToLocCode(stockSkuMoveSubmitVO.getTargetLocCode());//目标库位编码
			Location targetLoc = LocationCache.getByCode(stockSkuMoveSubmitVO.getSourceLocCode());
			if (Func.isEmpty(targetLoc)) {
				throw new ServiceException("库位不存在或已经删除");
			}
			transferRecord.setToLocId(targetLoc.getLocId());
			Zone targetZone = zoneService.getById(targetLoc.getZoneId());
			if (Func.isNotEmpty(targetZone)) {
				transferRecord.setToZoneCode(targetZone.getZoneCode());
				transferRecord.setToZoneName(targetZone.getZoneName());
			}

			transferRecord.setLotNumber(stockSkuMoveSubmitVO.getLotNumber());//批次号

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
	 * 数据校验
	 *
	 * @param stockSkuMove
	 */
	private void verifyStockSkuMoveSubmit(StockSkuMoveSubmitVO stockSkuMove) {
		if (stockSkuMove.getMoveQty().compareTo(BigDecimal.ONE) < 0) {
			throw new ServiceException("请输入有效数量");
		}
		//物品id,库位编码,库房id,与容器编码不可为空
		if (Func.isEmpty(stockSkuMove.getSkuId())) {
			throw new ServiceException("物品ID不能为空");
		}
		if (Func.isEmpty(stockSkuMove.getSourceLocCode())) {
			throw new ServiceException("库位编码不能为空");
		}
		if (Func.isEmpty(SkuCache.getById(stockSkuMove.getSkuId()))) {
			throw new ServiceException("物品信息不存在");
		}
	}


	@Override
	public boolean submitSkuInfoForBox(StockMoveByBoxQueryDTO stockMoveByBoxQueryDTO) {
		return false;
	}

}
