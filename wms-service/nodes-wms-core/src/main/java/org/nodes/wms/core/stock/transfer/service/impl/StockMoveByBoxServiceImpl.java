package org.nodes.wms.core.stock.transfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.stock.core.dto.StockMoveByBoxQueryDTO;
import org.nodes.wms.core.stock.core.dto.StockMoveByBoxSubmitDTO;
import org.nodes.wms.core.stock.core.dto.StockMoveDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.StockAndPackageVO;
import org.nodes.wms.core.stock.core.vo.StockMoveByBoxVO;
import org.nodes.wms.core.stock.core.vo.StockMoveByBoxVerifyVO;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.enums.TransferTypeEnum;
import org.nodes.wms.core.stock.transfer.service.IStockMoveByBoxService;
import org.nodes.wms.core.stock.transfer.service.ITransferRecordService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 按箱移动服务接口实现类
 *
 * @Author zx
 * @Date 2020/7/24
 **/
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockMoveByBoxServiceImpl implements IStockMoveByBoxService {

	@Autowired
	IStockService stockService;
	@Autowired
	ISkuLotService skuLotService;
	@Autowired
	ISkuUmService skuUmService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ITransferRecordService transferRecordService;

	/**
	 * 按箱移动 查询待移动库存
	 *
	 * @return
	 */
	@Override
	public StockMoveByBoxVO getMoveStock(StockMoveByBoxQueryDTO stockMoveByBoxQueryDTO) {
		if (Func.isEmpty(stockMoveByBoxQueryDTO.getSourceLocCode()) || Func.isEmpty(stockMoveByBoxQueryDTO.getSkuId())) {
			return null;
		}
		StockMoveByBoxVO stockMoveByBoxVO;
		if (Func.isEmpty(stockMoveByBoxQueryDTO.getSourceLocCode()))
			throw new ServiceException("原库位不能为空:sourceLocCode");
		if (Func.isEmpty(stockMoveByBoxQueryDTO.getSkuId()))
			throw new ServiceException("物品ID不能为空:skuId");
		if (Func.isEmpty(stockMoveByBoxQueryDTO.getWsuName()))
			throw new ServiceException("单位不能为空:wsuName");
		if (Func.isEmpty(stockMoveByBoxQueryDTO.getSkuCode()))
			throw new ServiceException("物品编码不能为空:skuCode");
		if (Func.isEmpty(stockMoveByBoxQueryDTO.getSkuName()))
			throw new ServiceException("物品名称不能为空:skuName");
		if (Func.isEmpty(stockMoveByBoxQueryDTO.getMoveQty())) {
			throw new ServiceException("移动数量不能为空:moveQty");
		}
		//库位
		Location location = LocationCache.getByCode(stockMoveByBoxQueryDTO.getSourceLocCode());

		LocationCache.validData(location);
		//单位
		ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
		//List<SkuUm> skuUmList = SkuUmCache.listByName(stockMoveByBoxQueryDTO.getWsuName());
		List<SkuUm> skuUmList = skuUmService.list(Condition.getQueryWrapper(new SkuUm())
			.lambda()
			.eq(SkuUm::getWsuName, stockMoveByBoxQueryDTO.getWsuName())
		).stream().collect(Collectors.toList());
		if (Func.isEmpty(skuUmList)) {
			throw new ServiceException(String.format("物料计量单位不存在", stockMoveByBoxQueryDTO.getWsuName()));
		}
		//包装明细
		List<String> wsuNameList = NodesUtil.toList(skuUmList, SkuUm::getWsuName);
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		List<SkuPackageDetail> skuPackageDetailList = new ArrayList<>();
		if (Func.isNotEmpty(wsuNameList)) {
			skuPackageDetailList = skuPackageDetailService.list(Condition.getQueryWrapper(new SkuPackageDetail())
				.lambda()
				.in(SkuPackageDetail::getWsuName, wsuNameList));
		}
		if (Func.isEmpty(skuPackageDetailList)) {
			throw new ServiceException(String.format("基础计量单位为%s的包装不存在", stockMoveByBoxQueryDTO.getWsuName()));
		}
		//库存
		QueryWrapper<Stock> sqw = Condition.getQueryWrapper(new Stock());
		sqw.lambda()
			.eq(Stock::getSkuId, stockMoveByBoxQueryDTO.getSkuId())
			.eq(Stock::getLocId, location.getLocId())
			.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
			.in(Stock::getWspId, NodesUtil.toList(skuPackageDetailList, SkuPackageDetail::getWspId))
			.apply("stock_qty - pick_qty - occupy_qty  >= " + stockMoveByBoxQueryDTO.getMoveQty());

		if (Func.isNotEmpty(stockMoveByBoxQueryDTO.getSkuLots())) {
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (stockMoveByBoxQueryDTO.getSkuLots().skuLotChk(i)) {
					sqw.eq("sku_lot" + i, stockMoveByBoxQueryDTO.getSkuLots().skuLotGet(i));
				}
			}
		}
		List<Stock> stockList = stockService.list(sqw);

		if (Func.isEmpty(stockList)) {
			return null;
		}
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.in(StockDetail::getStockId, NodesUtil.toList(stockList, Stock::getStockId)));
		List<Long> soBillIdList = NodesUtil.toList(stockDetailList, StockDetail::getSoBillId);
		final List<SoHeader> soHeaderList = new ArrayList<>();
		if (Func.isNotEmpty(soBillIdList)) {
			ISoHeaderService soHeaderService = SpringUtil.getBean(ISoHeaderService.class);
			soHeaderList.addAll(soHeaderService.listByIds(soBillIdList));
		}
		stockMoveByBoxVO = BeanUtil.copy(stockMoveByBoxQueryDTO, StockMoveByBoxVO.class);

		//转换类型
		List<StockAndPackageVO> stockAndPackageList = new ArrayList<>();
		StockAndPackageVO stockAndPackageVO1 = stockList.stream()
			.map(stock -> {
				StockAndPackageVO stockAndPackageVO = BeanUtil.copy(stock, StockAndPackageVO.class);

				if (Func.isNotEmpty(stockAndPackageVO)) {
					SkuPackage skuPackage = SkuPackageCache.getById(stock.getWspId());
					if (Func.isNotEmpty(skuPackage)) {
						stockAndPackageVO.setSkuSpec(skuPackage.getSpec());
					}
					SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(
						stock.getWspId(), SkuLevelEnum.Base.getIndex());
					if (Func.isNotEmpty(stockAndPackageVO) && Func.isNotEmpty(skuPackageDetail)) {
						stockAndPackageVO.setWsuName(skuPackageDetail.getWsuName());
						stockMoveByBoxVO.setWsuName(skuPackageDetail.getWsuName());
					}
					StockDetail stockDetail = stockDetailList.stream().filter(u -> {
						return Func.equals(u.getStockId(), stock.getStockId());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(stockDetail)) {
						SoHeader soHeader = soHeaderList.stream().filter(u -> {
							return u.getSoBillId().equals(stockDetail.getSoBillId());
						}).findFirst().orElse(null);
						if (Func.isNotEmpty(soHeader)) {
							stockAndPackageVO.setSoBillNo(soHeader.getSoBillNo());
							stockAndPackageVO.setEnterpriseName(soHeader.getCName());
							stockAndPackageVO.setOrderNo(soHeader.getOrderNo());
						}
					}
				}

				return stockAndPackageVO;
			}).findFirst().orElse(null);
		stockAndPackageList.add(stockAndPackageVO1);
		stockMoveByBoxVO.setStockList(stockAndPackageList);
		List<SkuLotConfigVO> skuLotLabelList = skuLotService.getConfig(stockMoveByBoxQueryDTO.getSkuId());
		if (Func.isNotEmpty(skuLotLabelList)) {
			skuLotLabelList.stream()
				.filter(skuLotLabelVO -> Func.isNotEmpty(stockMoveByBoxQueryDTO.getSkuLots()))
				.forEach(skuLotLabelVO -> {
					String skuLotValue =
						stockMoveByBoxQueryDTO.getSkuLots().skuLotGet(skuLotLabelVO.getSkuLotIndex());
					if (Func.isNotEmpty(skuLotValue.trim())) skuLotLabelVO.setSkuLotValue(skuLotValue);
				});
		}
		stockMoveByBoxVO.setSkuLotValList(skuLotLabelList);
		stockMoveByBoxVO.setSourceLocId(location.getLocId());
		return stockMoveByBoxVO;
	}

	/**
	 * 去重用于判断是否能存入set
	 *
	 * @param keyExtractor
	 * @param <T>
	 * @return
	 */
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	/**
	 * 按箱移动 物品移动验证
	 *
	 * @param stockMoveByBoxSubmitDTOList
	 * @return
	 */
	@Override
	public List<StockMoveByBoxVerifyVO> verifyStockForMoveByBox(List<StockMoveByBoxSubmitDTO> stockMoveByBoxSubmitDTOList) {
		//首先验证数据,
		this.verifyForMoveByBox(stockMoveByBoxSubmitDTOList, null);
		//查询库存列表
		QueryWrapper<Stock> sqw = new QueryWrapper<>();
		sqw.lambda().in(Stock::getStockId, NodesUtil.toList(stockMoveByBoxSubmitDTOList, StockMoveByBoxSubmitDTO::getStockId));
		List<Stock> stockList = stockService.list(sqw);
		//库位验证
		if (Func.isNotEmpty(stockList)) {
			stockList.stream().map(stock -> LocationCache.getById(stock.getLocId())).forEach(LocationCache::validData);
		} else {
			throw new ServiceException("库存信息已经不存在");
		}

		List<StockMoveByBoxVerifyVO> resultList = new ArrayList<>();

		for (StockMoveByBoxSubmitDTO dto : stockMoveByBoxSubmitDTOList) {
			// 找到对应的库存信息
			Stock stock = stockList.stream().filter(item -> {
				return item.getStockId().equals(dto.getStockId());
			}).findFirst().orElse(null);
			if (Func.isEmpty(stock)) {
				resultList.add(new StockMoveByBoxVerifyVO(dto.getStockId()));
				continue;
			}
			// 计算该库存可用数量
			BigDecimal qty = stock.getStockQty()
				.subtract(stock.getPickQty())
				.subtract(stock.getOccupyQty());
			if (qty.compareTo(dto.getMoveQty()) < 0) {
				resultList.add(new StockMoveByBoxVerifyVO(dto.getStockId()));
				continue;
			}
			stock.setOccupyQty(stock.getOccupyQty().add(dto.getMoveQty()));
		}
		return resultList;
	}

	/**
	 * 按箱移动 提交库存移动信息
	 *
	 * @param stockMoveByBoxSubmitDTOList
	 * @return
	 */
	@Override
	public boolean submitStockForMoveByBox(List<StockMoveByBoxSubmitDTO> stockMoveByBoxSubmitDTOList) {
		//首先验证数据
		this.verifyForMoveByBox(stockMoveByBoxSubmitDTOList, StringPool.ONE);
		BladeUser user = AuthUtil.getUser();

		//查询库存列表
		QueryWrapper<Stock> sqw = new QueryWrapper<>();
		sqw.lambda().in(
			Stock::getStockId, NodesUtil.toList(stockMoveByBoxSubmitDTOList, StockMoveByBoxSubmitDTO::getStockId));
		List<Stock> stockList = stockService.list(sqw);

		//系统日志用标识
		String billNo = IdWorker.getId() + "";
		for (StockMoveByBoxSubmitDTO stockMoveByBoxSubmitDTO : stockMoveByBoxSubmitDTOList) {
			//目标库位
			Location targetLocation = LocationCache.getByCode(stockMoveByBoxSubmitDTO.getTargetLocCode());
			LocationCache.validData(targetLocation);
			///原库位
			Location sourceLocation = LocationCache.getById(stockMoveByBoxSubmitDTO.getSourceLocId());
			LocationCache.validData(sourceLocation);
			if (!sourceLocation.getWhId().equals(targetLocation.getWhId())) {
				throw new ServiceException("原库位与目的库位不属于相同库房");
			}
			StockMoveDTO stockMoveDTO = new StockMoveDTO();
			stockMoveDTO.setSourceLocId(stockMoveByBoxSubmitDTO.getSourceLocId()); //原库位
			stockMoveDTO.setSourceLpnCode(stockMoveByBoxSubmitDTO.getSourceLpnCode());

			stockMoveDTO.setTargetLocId(targetLocation.getLocId()); //目标库位
			stockMoveDTO.setTargetLpnCode(stockMoveByBoxSubmitDTO.getTargetLpnCode());
			stockMoveDTO.setSkuId(stockMoveByBoxSubmitDTO.getSkuId()); //物品id
			stockMoveDTO.setSourceStockId(stockMoveByBoxSubmitDTO.getStockId()); //原库存
			stockMoveDTO.setMoveQty(stockMoveByBoxSubmitDTO.getMoveQty()); //数量

			stockMoveDTO.setEventType(EventTypeEnum.Move);
			stockMoveByBoxSubmitDTO.setBillNo(billNo);
			stockMoveDTO.setSystemProcId(this.saveMoveSystemLog(stockMoveByBoxSubmitDTO)); //系统日志
			this.saveTransferRecord(stockMoveByBoxSubmitDTO, stockList);//移动日志
			stockService.stockMove(stockMoveDTO);
		}
		return true;
	}

	/**
	 * 按箱移动 数据验证
	 *
	 * @param stockMoveByBoxSubmitDTOList
	 */
	private void verifyForMoveByBox(List<StockMoveByBoxSubmitDTO> stockMoveByBoxSubmitDTOList, String type) {
		if (Func.isEmpty(stockMoveByBoxSubmitDTOList))
			throw new ServiceException("请先扫描箱码");

		stockMoveByBoxSubmitDTOList.forEach(stockMoveByBoxSubmitDTO -> {
			if (Func.isEmpty(stockMoveByBoxSubmitDTO.getStockId()))
				throw new ServiceException("库存ID不能为空:stockId");
			if (Func.isEmpty(stockMoveByBoxSubmitDTO.getSourceLocId()))
				throw new ServiceException("原库位IF不能为空:sourceLocId");
			if (Func.isEmpty(stockMoveByBoxSubmitDTO.getSkuCode()))
				throw new ServiceException("物品编码不能为空:skuCode");
			if (Func.isEmpty(stockMoveByBoxSubmitDTO.getSkuName()))
				throw new ServiceException("物品名称不能为空:skuName");
			if (Func.isEmpty(stockMoveByBoxSubmitDTO.getMoveQty()))
				throw new ServiceException("移动数量不能为空:moveQty");
			if (StringPool.ONE.equals(type)) {
				if (Func.isEmpty(stockMoveByBoxSubmitDTO.getWsuName()))
					throw new ServiceException("单位名称不能为空:wsuName");
				if (Func.isEmpty(stockMoveByBoxSubmitDTO.getTargetLocCode()))
					throw new ServiceException("目标库位编码不能为空:targetLocCode");
				if (Func.isEmpty(stockMoveByBoxSubmitDTO.getSkuId()))
					throw new ServiceException("物品ID不能为空:skuId");
			}
		});
	}


	/**
	 * 保存移动的系统日志
	 *
	 * @param stockMoveByBoxSubmitDTO
	 * @return
	 */
	private Long saveMoveSystemLog(StockMoveByBoxSubmitDTO stockMoveByBoxSubmitDTO) {
		// 创建系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.MOVE);
		systemProcParam.setAction(ActionEnum.MOVE_BOX);
		systemProcParam.setBillNo(stockMoveByBoxSubmitDTO.getBillNo());
		Location location = LocationCache.getById(stockMoveByBoxSubmitDTO.getSourceLocId());
		LocationCache.validData(location);
		systemProcParam.setWhId(location.getWhId()); //库房
		systemProcParam.setOperationQty(stockMoveByBoxSubmitDTO.getMoveQty()); //数量
		systemProcParam.setOperationUnit(stockMoveByBoxSubmitDTO.getWsuName()); //单位
		SystemProc systemProc = systemProcService.create(systemProcParam);
		return systemProc.getSystemProcId();
	}

	/**
	 * 保存移动记录
	 *
	 * @return
	 */
	private boolean saveTransferRecord(StockMoveByBoxSubmitDTO stockMoveByBoxSubmitDTO, List<Stock> stockList) {
		List<Stock> stocks = stockList.stream()
			.filter(stock -> stock.getStockId().equals(stockMoveByBoxSubmitDTO.getStockId()))
			.collect(Collectors.toList());
		if (Func.isEmpty(stocks)) {
			return false;
		}
		TransferRecord transferRecord = new TransferRecord();
		transferRecord.setTransferType(TransferTypeEnum.INNER_WAREHOUSE.getIndex());
		transferRecord.setWoId(stocks.get(0).getWoId());// 货主从
//		transferRecord.setFromLpn(stocks.get(0).getLpnCode()); // 源容器

		Location sourceLoc = LocationCache.getValid(stockMoveByBoxSubmitDTO.getSourceLocId().toString());
		transferRecord.setFromLocCode(sourceLoc.getLocCode());//原库位编码
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone sourceZone = zoneService.getById(sourceLoc.getZoneId());
		if (Func.isNotEmpty(sourceZone)) {
			transferRecord.setFromZoneCode(sourceZone.getZoneCode());// 原库区编码
			transferRecord.setFromZoneName(sourceZone.getZoneName());// 原库区名称
		}
		transferRecord.setQty(stockMoveByBoxSubmitDTO.getMoveQty()); // 原数量
		transferRecord.setWspId(stocks.get(0).getWspId()); //原包装Id
		List<SkuPackageDetail> packageDetails = SkuPackageDetailCache.listByWspId(stocks.get(0).getWspId())
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
		transferRecord.setSkuId(stocks.get(0).getSkuId());//目标货品
//		transferRecord.setToLpn(stocks.get(0).getLpnCode()); // 目标容器编码
		Location targetLoc = LocationCache.getValid(stockMoveByBoxSubmitDTO.getTargetLocCode());
		transferRecord.setToLocCode(targetLoc.getLocCode());//目标库位编码
		Zone targetZone = zoneService.getById(targetLoc.getZoneId());
		if (Func.isNotEmpty(targetZone)) {
			transferRecord.setToZoneCode(targetZone.getZoneCode());//目标库区编码
			transferRecord.setToZoneName(targetZone.getZoneName());//目标库区名称
		}
		transferRecord.setQty(stockMoveByBoxSubmitDTO.getMoveQty());//目标数量
		transferRecord.setLotNumber(stocks.get(0).getLotNumber());//批次号

		Stock stock = stocks.get(0);
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (stock.skuLotChk(i)) {
				transferRecord.skuLotSet(i, stock.skuLotGet(i));
			}
		}
		return transferRecordService.save(transferRecord);
	}
}
