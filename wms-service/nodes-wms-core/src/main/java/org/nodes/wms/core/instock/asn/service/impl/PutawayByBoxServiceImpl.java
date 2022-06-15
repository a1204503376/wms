package org.nodes.wms.core.instock.asn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
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
import org.nodes.wms.core.stock.core.entity.Stock;
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
 * 按箱收货实现类
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
	 * 按箱上架 获得上架信息
	 *
	 * @param putawayByBoxQueryDTO
	 * @return
	 */
	@Override
	public InstockExecuteVO queryStockForBox(PutawayByBoxQueryDTO putawayByBoxQueryDTO) {

		Warehouse warehouse = WarehouseCache.getById(putawayByBoxQueryDTO.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或者已删除");
		}
		// 如果库位是暂存区则给库位编码拼接库房编码
		if (ZoneVirtualTypeEnum.isVirtual(putawayByBoxQueryDTO.getLocCode())) {

			putawayByBoxQueryDTO.setLocCode(
				Func.firstCharToUpper(putawayByBoxQueryDTO.getLocCode().toLowerCase()) + warehouse.getWhCode());
		}
		Sku sku = SkuCache.getById(putawayByBoxQueryDTO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException(String.format("物品[%s]不存在或者已删除", putawayByBoxQueryDTO.getSkuCode()));
		}
		// 通过库位编码查询库位信息
		Location location = LocationCache.getValid(putawayByBoxQueryDTO.getLocCode());
		// 获取虚拟库位ID
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
		// 查询该物品的所有库存信息(不包含虚拟库位)
		List<Stock> stockListAll = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
			.eq(Stock::getSkuId, sku.getSkuId())
			.notIn(Stock::getZoneId, zoneIdList));

		// 查询可上架并且数量匹配的库存
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

		// 筛选匹配的库存
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
			throw new ServiceException("库存指定订单明细不存在！");
		}
		AsnHeader asnHeader = super.getById(asnDetail.getAsnBillId());
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("库存指定订单不存在！");
		}
		//返回值
		vo = instockService.execute(list, asnHeader.getBillTypeCd());
		vo.setStockLocList(StockWrapper.build().listVO(stockListAll));
		Set<Long> types = new HashSet<>();

		for (Stock item : list) {
			//种类
			types.add(item.getSkuId());
		}
		vo.setLocCode(location.getLocCode());
		//库位状态
		if (Func.isNotEmpty(location.getLocStatus())) {
			vo.setLocStatus(location.getLocStatus());
			vo.setLocStatusName(DictCache.getValue(DictConstant.LOC_STATUS, location.getLocStatus()));
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
	 * 按箱上架 提交上架信息
	 *
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	@Override
	public synchronized boolean submitPutaway(PutawayByBoxSubimitVO putawayByBoxSubimitVO) {

		if (Func.isEmpty(putawayByBoxSubimitVO.getWsuCode()) && Func.isEmpty(putawayByBoxSubimitVO.getWsuName())) {
			throw new ServiceException("参数：wsuCode, wsuName 不能同时为空！");
		}
		Warehouse warehouse = WarehouseCache.getById(putawayByBoxSubimitVO.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或者已删除");
		}
		if (ZoneVirtualTypeEnum.isVirtual(putawayByBoxSubimitVO.getLocCode())) {
			//如果库位是暂存区则给库位编码拼接库房编码
			putawayByBoxSubimitVO.setLocCode(
				Func.firstCharToUpper(putawayByBoxSubimitVO.getLocCode().toLowerCase()) + warehouse.getWhCode());
		} else {
			throw new ServiceException(String.format("物品已上架,或者库位不存在"));
		}

		//查询出来库存中在当前容器里的所有状态正常的物品
		Stock stock = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.eq(Stock::getStockId, putawayByBoxSubimitVO.getStockId()))
			.stream().findFirst().orElse(null);
		if (Func.isEmpty(stock)) {
			throw new ServiceException("该库存不存在或者已上架");
		}
		// 计算上架计量单位的转换率
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
			throw new ServiceException("物品指定的计量单位不存在！");
		}
		//原库位
		Location sourceLocation = LocationCache.getValid(putawayByBoxSubimitVO.getLocCode());
		//查询目标库位信息
		Location targetLocation = LocationCache.getValid(putawayByBoxSubimitVO.getTargetLocCode());
		if (!targetLocation.getWhId().equals(sourceLocation.getWhId())) {
			throw new ServiceException(String.format("目标库位与原库位不属于相同库房"));
		}
		//记录系统日志
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
		stockMoveDTO.setSourceLocId(sourceLocation.getLocId()); //原库位id 从数据库中查询
		stockMoveDTO.setTargetLocId(targetLocation.getLocId()); //目标库位id 通过传递的locCode查询locId
		stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
		stockMoveDTO.setEventType(EventTypeEnum.Putaway);
		stockMoveDTO.setSkuId(stock.getSkuId()); //物品id
		stockMoveDTO.setMoveQty(qty);
		stockMoveDTO.setTargetLpnCode(putawayByBoxSubimitVO.getLpnCode());
		stockService.stockMove(stockMoveDTO);
		// 保存移动记录
		TransferRecord transferRecord = new TransferRecord();
		transferRecord.setTransferType(TransferTypeEnum.PUTAWAY.getIndex());
		transferRecord.setWoId(stock.getWoId()); // 货主到
		transferRecord.setSkuId(stock.getSkuId()); //原货品
//		transferRecord.setFromLpn(stock.getLpnCode()); // 源容器
		transferRecord.setFromLocCode(putawayByBoxSubimitVO.getLocCode());//原库位编码
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone sourceZone = zoneService.getById(sourceLocation.getZoneId());
		if (Func.isNotEmpty(sourceZone)) {
			transferRecord.setFromZoneCode(sourceZone.getZoneCode());// 原库区编码
			transferRecord.setFromZoneName(sourceZone.getZoneName());// 原库区名称
		}
		transferRecord.setQty(stock.getStockQty()); // 原数量
		transferRecord.setWspId(stock.getWspId()); //原包装Id
		transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());//原层级
		transferRecord.setSkuId(stock.getSkuId());//目标货品
		transferRecord.setFromLocId(putawayByBoxSubimitVO.getSourceLocId());
		transferRecord.setToLocId(targetLocation.getLocId());
		transferRecord.setWsuCode(putawayByBoxSubimitVO.getWsuCode());
		transferRecord.setWsuName(putawayByBoxSubimitVO.getWsuName());
		transferRecord.setToLocCode(targetLocation.getLocCode());//目标库位编码
		Zone targetZone = zoneService.getById(targetLocation.getZoneId());
		if (Func.isNotEmpty(targetZone)) {
			transferRecord.setToZoneCode(targetZone.getZoneCode());//目标库区编码
			transferRecord.setToZoneName(targetZone.getZoneName());//目标库区名称
		}
		transferRecord.setQty(qty);//目标数量
		transferRecord.setWspId(stock.getWspId());//目的包装
		transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());// 目的层级
		transferRecord.setLotNumber(stock.getLotNumber());//批次号
		transferRecord.setTransferCode("");
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (stock.skuLotChk(i)) {
				transferRecord.skuLotSet(i, stock.skuLotGet(i));
			}
		}

		return transferRecordService.save(transferRecord);
	}

	/**
	 * 新按箱上架 提交上架信息
	 *
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	@Override
	public synchronized boolean submitPutawayNew(PutawayByBoxSubimitVO putawayByBoxSubimitVO) {

		Location targetLocation = LocationCache.getValid(putawayByBoxSubimitVO.getTargetLocCode());
		if (Func.isEmpty(targetLocation)) {
			throw new ServiceException("目标库位不存在！");
		}
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getBoxCode, putawayByBoxSubimitVO.getBoxCodes()));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("系统中没有查询到待上架物品！");
		}
		List<Stock> stockList = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, NodesUtil.toList(stockDetails, StockDetail::getStockId)));
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("系统中没有查询到待上架物品！");
		}
		for (StockDetail stockDetail : stockDetails) {
			Location location = LocationCache.getById(stockDetail.getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException(String.format("该箱号[%s]没有待上架的物品！", stockDetail.getBoxCode()));
			}
			boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
			if (!virtual) {
				throw new ServiceException(String.format("该箱号[%s]没有待上架的物品！", stockDetail.getBoxCode()));
			}
			if (!location.getLocCode().contains("Stage")) {
				throw new ServiceException(String.format("该箱号[%s]没有待上架的物品！", stockDetail.getBoxCode()));
			}
			Stock stock1 = stockList.stream().filter(stock -> stock.getStockId()
				.equals(stockDetail.getStockId())).findFirst().orElse(null);
			if (Func.isEmpty(stock1)) {
				throw new ServiceException("系统中没有查询到待上架物品！");
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
			transferRecord.setWoId(stock1.getWoId()); // 货主到
			transferRecord.setSkuId(stockDetail.getSkuId()); //原货品
			transferRecord.setFromLpn(stockDetail.getLpnCode()); // 源容器
			transferRecord.setFromLocCode(putawayByBoxSubimitVO.getLocCode());//原库位编码
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone sourceZone = zoneService.getById(location.getZoneId());
			if (Func.isNotEmpty(sourceZone)) {
				transferRecord.setFromZoneCode(sourceZone.getZoneCode());// 原库区编码
				transferRecord.setFromZoneName(sourceZone.getZoneName());// 原库区名称
			}
			transferRecord.setQty(stockDetail.getStockQty()); // 原数量
			transferRecord.setWspId(stockDetail.getWspId()); //原包装Id
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());//原层级
			transferRecord.setSkuId(stockDetail.getSkuId());//目标货品
			transferRecord.setFromLocId(putawayByBoxSubimitVO.getSourceLocId());
			transferRecord.setToLocId(targetLocation.getLocId());
			transferRecord.setWsuCode(putawayByBoxSubimitVO.getWsuCode());
			transferRecord.setWsuName(putawayByBoxSubimitVO.getWsuName());
			transferRecord.setToLocCode(targetLocation.getLocCode());//目标库位编码
			Zone targetZone = zoneService.getById(targetLocation.getZoneId());
			if (Func.isNotEmpty(targetZone)) {
				transferRecord.setToZoneCode(targetZone.getZoneCode());//目标库区编码
				transferRecord.setToZoneName(targetZone.getZoneName());//目标库区名称
			}
			transferRecord.setQty(stockDetail.getStockQty());//目标数量
			transferRecord.setWspId(stockDetail.getWspId());//目的包装
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());// 目的层级
			transferRecord.setLotNumber(stockDetail.getLotNumber());//批次号
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
	 * 根据箱号获取推荐信息
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
			throw new ServiceException("没有待上架的物品！");
		}
		Location location = LocationCache.getById(stockDetail.getLocId());
		if (Func.isEmpty(location)) {
			throw new ServiceException("没有待上架的物品！");
		}
		boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
		if (!virtual) {
			throw new ServiceException("没有待上架的物品！");
		}
		if (!location.getLocCode().contains("Stage")) {
			throw new ServiceException("没有待上架的物品！");
		}
		Stock stock = stockService.getById(stockDetail.getStockId());
		if (Func.isEmpty(stock)) {
			throw new ServiceException("库存不存在！");
		}
		Sku sku = SkuCache.getById(stockDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在");
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
	 * 根据箱号获取推荐信息
	 *
	 * @param boxCode
	 */
	@Override
	public BoxItemVo getInfoMoveByBoxCode(String boxCode) {

		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		StockDetail stockDetail = stockDetailService.getOne(Wrappers.lambdaQuery(StockDetail.class)
			.eq(StockDetail::getBoxCode, boxCode).apply("stock_qty - pick_qty > 0"));
		if (Func.isEmpty(stockDetail)) {
			throw new ServiceException("该箱号没有可移动的物品！");
		}
		Location location = LocationCache.getById(stockDetail.getLocId());
		if (Func.isEmpty(location)) {
			throw new ServiceException("该箱号没有可移动的物品！");
		}
		boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
		if (virtual) {
			throw new ServiceException("该箱号没有可移动的物品！");
		}
		Stock stock = stockService.getById(stockDetail.getStockId());
		if (Func.isEmpty(stock)) {
			throw new ServiceException("库存不存在！");
		}
		Sku sku = SkuCache.getById(stockDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在");
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
			throw new ServiceException("目标库位不存在！");
		}
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getBoxCode, putawayByBoxSubimitVO.getBoxCodes()));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("系统中没有查询到要移动的物品！");
		}
		List<Stock> stockList = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, NodesUtil.toList(stockDetails, StockDetail::getStockId)));
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("系统中没有查询到要移动的物品！");
		}
		for (StockDetail stockDetail : stockDetails) {
			Location location = LocationCache.getById(stockDetail.getLocId());
			if (Func.isEmpty(location)) {
				throw new ServiceException(String.format("该箱号[%s]物品在暂存区不能移动！", stockDetail.getBoxCode()));
			}
			boolean virtual = ZoneVirtualTypeEnum.isVirtual(location.getLocCode());
			if (virtual) {
				throw new ServiceException(String.format("该箱号[%s]物品在暂存区不能移动！", stockDetail.getBoxCode()));
			}

			Stock stock1 = stockList.stream().filter(stock -> stock.getStockId()
				.equals(stockDetail.getStockId())).findFirst().orElse(null);
			if (Func.isEmpty(stock1)) {
				throw new ServiceException("系统中没有查询到要移动的物品！");
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
			transferRecord.setWoId(stock1.getWoId()); // 货主到
			transferRecord.setSkuId(stockDetail.getSkuId()); //原货品
			transferRecord.setFromLpn(stockDetail.getLpnCode()); // 源容器
			transferRecord.setFromLocCode(putawayByBoxSubimitVO.getLocCode());//原库位编码
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone sourceZone = zoneService.getById(location.getZoneId());
			if (Func.isNotEmpty(sourceZone)) {
				transferRecord.setFromZoneCode(sourceZone.getZoneCode());// 原库区编码
				transferRecord.setFromZoneName(sourceZone.getZoneName());// 原库区名称
			}
			transferRecord.setQty(stockDetail.getStockQty()); // 原数量
			transferRecord.setWspId(stockDetail.getWspId()); //原包装Id
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());//原层级
			transferRecord.setSkuId(stockDetail.getSkuId());//目标货品
			transferRecord.setFromLocId(putawayByBoxSubimitVO.getSourceLocId());
			transferRecord.setToLocId(targetLocation.getLocId());
			transferRecord.setWsuCode(putawayByBoxSubimitVO.getWsuCode());
			transferRecord.setWsuName(putawayByBoxSubimitVO.getWsuName());
			transferRecord.setToLocCode(targetLocation.getLocCode());//目标库位编码
			Zone targetZone = zoneService.getById(targetLocation.getZoneId());
			if (Func.isNotEmpty(targetZone)) {
				transferRecord.setToZoneCode(targetZone.getZoneCode());//目标库区编码
				transferRecord.setToZoneName(targetZone.getZoneName());//目标库区名称
			}
			transferRecord.setQty(stockDetail.getStockQty());//目标数量
			transferRecord.setWspId(stockDetail.getWspId());//目的包装
			transferRecord.setSkuLevel(SkuLevelEnum.Base.getIndex());// 目的层级
			transferRecord.setLotNumber(stockDetail.getLotNumber());//批次号
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
