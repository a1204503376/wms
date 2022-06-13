
package org.nodes.wms.core.outstock.loading.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.entity.TruckSerial;
import org.nodes.wms.core.outstock.loading.entity.TruckStock;
import org.nodes.wms.core.outstock.loading.mapper.SoTruckHeaderMapper;
import org.nodes.wms.core.outstock.loading.service.ISoTruckDetailService;
import org.nodes.wms.core.outstock.loading.service.ISoTruckHeaderService;
import org.nodes.wms.core.outstock.loading.service.ITruckSerialService;
import org.nodes.wms.core.outstock.loading.service.ITruckStockService;
import org.nodes.wms.core.outstock.loading.vo.*;
import org.nodes.wms.core.outstock.loading.wrapper.SoTruckHeaderWrapper;
import org.nodes.wms.core.outstock.loading.wrapper.TruckStockWrapper;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.enums.ShipStateEnum;
import org.nodes.wms.core.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.service.IWellenService;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.dto.StockSubtractDTO;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.enums.ZoneTypeEnum;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.jackson.JsonUtil;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 车次头表 服务实现类
 *
 * @author chz
 * @since 2020-03-03
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SoTruckHeaderServiceImpl<M extends SoTruckHeaderMapper, T extends SoTruckHeader>
	extends BaseServiceImpl<SoTruckHeaderMapper, SoTruckHeader>
	implements ISoTruckHeaderService {

	@Autowired
	ISoTruckDetailService soTruckDetailService;
	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	IWellenService wellenService;
	@Autowired
	IWellenDetailService wellenDetailService;
	@Autowired
	IStockService stockService;
	@Autowired
	IStockDetailService stockDetailService;
	@Autowired
	ISerialService serialService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ITruckStockService truckStockService;
	@Autowired
	ITruckSerialService truckSerialService;

	@Override
	public Boolean confirmSo(String truckId) {
		List<Long> longList = Func.toLongList(truckId);
		for (Long truckIdOne : longList) {
			List<SoTruckDetail> soTruckDetailList = soTruckDetailService.list(new QueryWrapper<SoTruckDetail>().lambda()
				.eq(SoTruckDetail::getTruckId, truckIdOne));
			List<SoTruckDetail> soTruckDetailListDistinct = soTruckDetailList.stream().collect(
				Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getSoBillId()))),
					ArrayList::new));
			if (soTruckDetailListDistinct.size() > 0) {
				for (SoTruckDetail soTruckDetail : soTruckDetailListDistinct) {
					// 更新出库单发货时间
					SoHeader soHeader = new SoHeader();
					soHeader.setSoBillId(soTruckDetail.getSoBillId());
					soHeader.setShipState(ShipStateEnum.SENDED.getIndex());
					soHeaderService.updateById(soHeader);
					soHeaderService.updateBillState(soTruckDetail.getSoBillId(), SoBillStateEnum.COMPLETED, true);
				}
			}
			SoTruckHeader soTruckHeader = new SoTruckHeader();
			soTruckHeader.setTruckId(truckIdOne);
			soTruckHeader.setTruckState(20);
			this.updateById(soTruckHeader);

		}
		return true;
	}

	@Override
	public Boolean SaveconfirmSo(String truckId) {
		SoTruckHeader soTruckHeader = super.getById(truckId);
		if (20 == soTruckHeader.getTruckState()) {
			throw new ServiceException("该车次已发货");
		}
		this.confirmSo(truckId);
		//根据车次ID查询车次明细
		List<SoTruckDetail> SoTruckList = soTruckDetailService.list(new QueryWrapper<SoTruckDetail>().lambda()
			.eq(SoTruckDetail::getTruckId, truckId));
		if (Func.isEmpty(SoTruckList)) {
			throw new ServiceException("还没有装车！");
		}
		// 查询库存占用
		List<StockOccupy> stockOccupyList = new ArrayList<>();
		List<Long> soBillIdList = NodesUtil.toList(SoTruckList, SoTruckDetail::getSoBillId);
		if (Func.isNotEmpty(soBillIdList)) {
			IStockOccupyService stockOccupyService = SpringUtil.getBean(IStockOccupyService.class);
			stockOccupyList = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
				.lambda()
				.in(StockOccupy::getSoBillId, soBillIdList));
			for (Long soBillId : soBillIdList) {
				StockOccupySubtractDTO stockOccupyReduceDTO = new StockOccupySubtractDTO();
				stockOccupyReduceDTO.setSoBillId(soBillId);
				stockOccupyReduceDTO.setOccupyType(StockOccupyTypeEnum.PickPlan.getIndex());
				stockOccupyService.subtract(stockOccupyReduceDTO);
			}
		}
		List<String> lpnCodeList = NodesUtil.toList(SoTruckList, SoTruckDetail::getLpnCode);
		if (Func.isEmpty(lpnCodeList)) {
			throw new ServiceException("没有扫描容器");
		}
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		List<Zone> outstockZoneList = zoneService.list(Condition.getQueryWrapper(new Zone()).lambda()
			.eq(Zone::getZoneType, ZoneTypeEnum.SHIPPING_OUTSTOCK.getIndex()));
		for (SoTruckDetail soTruckDetail : SoTruckList) {
			//根据容器查询库存
			List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
				.lambda()
				.in(StockDetail::getLpnCode, lpnCodeList)
				.func(sql -> {
					if (Func.isNotEmpty(outstockZoneList)) {
						sql.in(StockDetail::getZoneId, NodesUtil.toList(outstockZoneList, Zone::getZoneId));
					}
				}));
			//根据容器查询库存
			stockDetailList.stream().collect(Collectors.groupingBy(StockDetail::getStockId))
				.forEach((stockId, detailList) -> {
					Stock stock = stockService.getById(stockId);
					if (Func.isEmpty(stock)) {
						return;
					}
					List<String> serialList = new ArrayList<>();
					//插入系统日志
					SystemProcDTO systemProcParam = new SystemProcDTO();
					systemProcParam.setProcType(SystemProcTypeEnum.LOADING);
					systemProcParam.setDataType(DataTypeEnum.LoddingBillNo);
					systemProcParam.setAction(ActionEnum.LOADING_EXE);
					systemProcParam.setBillNo(soTruckDetail.getTruckCode());
					Location location = LocationCache.getById(stock.getLocId());
					if (Func.isNotEmpty(location)) {
						systemProcParam.setLocCode(location.getLocCode());
					}
					systemProcParam.setLpnCode(NodesUtil.join(detailList, StockDetail::getLpnCode));
					Sku sku = SkuCache.getById(stock.getSkuId());
					Integer isSn = 0;
					if (Func.isNotEmpty(sku)) {
						isSn = sku.getIsSn();
						systemProcParam.setSkuCode(sku.getSkuCode());
						systemProcParam.setSkuName(sku.getSkuName());
					}
					systemProcParam.setOperationQty(stock.getStockQty());
					systemProcParam.setWhId(stock.getWhId());
					systemProcParam.setRemark(JsonUtil.toJson(stock));
					Long systemProcId = systemProcService.create(systemProcParam).getSystemProcId();

					if (isSn == 1) {
						//如果是序列号查询序列号明细
						List<Serial> serialListModel = serialService.list(new QueryWrapper<Serial>().lambda()
							.eq(Serial::getStockId, stock.getStockId()));
						for (Serial serial : serialListModel) {
							serialList.add(serial.getSerialNumber());
							//插入序列号日志
							TruckSerial truckSerial = new TruckSerial();
							truckSerial.setTruckId(Long.parseLong(truckId));
							truckSerial.setSerialId(serial.getSerialId());
							truckSerial.setStockId(serial.getStockId());
							truckSerial.setWhId(serial.getWhId());
							truckSerial.setSerialNumber(serial.getSerialNumber());
							truckSerial.setSerialState(serial.getSerialState());
							truckSerial.setSkuId(serial.getSkuId());
							truckSerial.setSkuCode(serial.getSkuCode());
							truckSerial.setSkuName(serial.getSkuName());
							truckSerial.setLotNumber(serial.getLotNumber());
							truckSerial.setSystemProcId(systemProcId);
							truckSerialService.save(truckSerial);
						}
					}

					//插入装车日志
					detailList.forEach(detail -> {
						TruckStock truckStock = new TruckStock();
						truckStock.setTruckId(Long.parseLong(truckId));
						BeanUtil.copy(stock, truckStock);
						truckStock.setLpnCode(detail.getLpnCode());
						truckStockService.save(truckStock);
					});

					//执行库存扣减
					StockSubtractDTO stockReduceDTO = new StockSubtractDTO();
					stockReduceDTO.setStockId(stock.getStockId());
					stockReduceDTO.setSkuId(stock.getSkuId());
					stockReduceDTO.setPickQty(stock.getStockQty().subtract(stock.getPickQty()));
					stockReduceDTO.setSystemProcId(systemProcId);
					stockReduceDTO.setSerialList(serialList);
					stockReduceDTO.setLotNumber(stock.getLotNumber());
					stockReduceDTO.setEventType(EventTypeEnum.LoadingStock);
					stockService.subtract(stockReduceDTO);
				});
		}
		return true;
	}

	@Override
	public SoTruckHeaderPdaVO getTruckHeader(String truckCode, String driverName, String driverPhone, String plateNumber, String truckHeaderWaybill) {
		//查询正在装车的车次信息
		SoTruckHeader soTruckHeader = new SoTruckHeader();
		soTruckHeader.setTruckCode(truckCode);
		soTruckHeader.setDriverName(driverName);
		soTruckHeader.setDriverPhone(driverPhone);
		soTruckHeader.setPlateNumber(plateNumber);
		soTruckHeader.setTruckHeaderWaybill(truckHeaderWaybill);

		List<SoTruckHeader> soTruckHeaderList = this.list(Condition.getQueryWrapper(soTruckHeader).lambda()
			.eq(SoTruckHeader::getTruckState, 10));
		if (soTruckHeaderList.size() == 0) {
			throw new ServiceException("未查询到车次信息");
		} else if (soTruckHeaderList.size() > 1) {
			throw new ServiceException("查询多条车次信息，数据异常");
		}
		return BeanUtil.copy(soTruckHeaderList.get(0), SoTruckHeaderPdaVO.class);
	}

	@Override
	public SoTruckDetailPdaVO getSkuStock(String LpnCode) {
		SoTruckDetailPdaVO soTruckDetailPdaVO = new SoTruckDetailPdaVO();
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		List<Zone> zoneList = zoneService.list(Condition.getQueryWrapper(new Zone()).lambda()
			.eq(Zone::getZoneType, ZoneTypeEnum.SHIPPING_OUTSTOCK.getIndex()));
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.eq(StockDetail::getLpnCode, LpnCode)
			.func(sql -> {
				if (Func.isNotEmpty(zoneList)) {
					sql.in(StockDetail::getZoneId, NodesUtil.toList(zoneList, Zone::getZoneId));
				}
			})
			.apply("stock_qty > pick_qty"));

		if (Func.isEmpty(stockDetailList)) {
			throw new ServiceException("未查询到LPN编码对应的信息：" + LpnCode);
		}
		if (stockDetailList.get(0).getBillDetailId() == null) {
			throw new ServiceException("LPN编码对应的出库单信息为空：" + LpnCode);
		}
		BigDecimal reduce = stockDetailList.stream().map(StockDetail::getStockQty).reduce(BigDecimal.ZERO, BigDecimal::add);
		StockPdaVO stockPdaVO = getStockVo(stockDetailList.get(0), 1);
		stockPdaVO.setSkuCount(stockDetailList.size());
		SoDetail soDetail = soDetailService.getById(stockDetailList.get(0).getBillDetailId());
		if (soDetail == null) {
			throw new ServiceException("LPN编码对应的出库单明细信息为空：" + LpnCode);
		}
		stockPdaVO.setScanQty(reduce);
		soTruckDetailPdaVO.setStockPdaVO(stockPdaVO);
		soTruckDetailPdaVO.setSoBillId(soDetail.getSoBillId());
		return soTruckDetailPdaVO;
	}

	@Override
	public SoTruckHeaderPdaVO getSkuStockDetailList(String LpnCode) {
		SoTruckHeaderPdaVO soTruckHeaderPdaVO = new SoTruckHeaderPdaVO();
//		List<StockPdaVO> stockPdaVOList = new ArrayList<>();
//		List<Stock> stockList = stockService.list(new QueryWrapper<Stock>().lambda()
//			.eq(Stock::getLpnCode, LpnCode).apply("stock_qty-occupy_qty-pick_qty-count_occupy_qty >0"));
//		List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
//			.eq(Stock::getLpnCode, LpnCode));
//		if (stockList.size() == 0) {
//			throw new ServiceException("未查询到LPN编码对应的信息：" + LpnCode);
//		}
//		BigDecimal reduce = stockList.stream().map(Stock::getStockQty).reduce(BigDecimal.ZERO, BigDecimal::add);
//		for (Stock stock : stockList) {
//			StockPdaVO stockVo = getStockVo(stock, 2);
//			stockVo.setScanQty(reduce);
//			stockVo.setSkuCount(stockList.size());
//			stockPdaVOList.add(stockVo);
//		}
//		//封装订单信息，因为尽量减少前台返回数据，所以单独赋值使用字段
//		SoDetail soDetail = soDetailService.getById(stockList.get(0).getBillDetailId());
//		if (soDetail == null) throw new ServiceException("LPN编码对应的出库单明细信息为空：" + LpnCode);
//		SoHeader soHeader = soHeaderService.getById(soDetail.getSoBillId());
//		soTruckHeaderPdaVO.setSoBillId(soHeader.getSoBillId());
////		stockVo.setScanQty(reduce);
////		stockVo.setSkuCount(stockList.size());
////		soTruckHeaderPdaVO.set
//		soTruckHeaderPdaVO.setCName(soHeader.getCName());
//		soTruckHeaderPdaVO.setAddress(soHeader.getAddress());
//		soTruckHeaderPdaVO.setContact(soHeader.getContact());
//		soTruckHeaderPdaVO.setPhone(soHeader.getPhone());
//		//封装所有信息
//		soTruckHeaderPdaVO.setStockPdaVOList(stockPdaVOList);
		return soTruckHeaderPdaVO;
	}

	@Override
	public Boolean saveSoTruckDetail(List<SoTruckDetail> soTruckDetailList) {
		if (soTruckDetailList.size() == 0) {
			throw new ServiceException("不能传入空的LPN装车列表");
		}
		for (SoTruckDetail soTruckDetail : soTruckDetailList) {
			if (Func.isEmpty(soTruckDetail.getTruckId())) {
				throw new ServiceException("TruckId为空");
			}
			if (Func.isEmpty(soTruckDetail.getSoBillId())) {
				throw new ServiceException("出库单Id为空");
			}
			if (Func.isEmpty(soTruckDetail.getTruckCode())) {
				throw new ServiceException("车辆编号为空");
			}
			if (Func.isEmpty(soTruckDetail.getLpnCode())) {
				throw new ServiceException("LPN编码为空");
			}
			// 订单取消验证
			SoHeader soHeader = soHeaderService.getById(soTruckDetail.getSoBillId());
			if (Func.isEmpty(soHeader)) {
				throw new ServiceException("指定出库单不存在！");
			}
			// 查询该订单是否存在已装车的容器
			Integer truckDetailSize = soTruckDetailService.list(Condition.getQueryWrapper(new SoTruckDetail()).lambda()
				.eq(SoTruckDetail::getSoBillId, soHeader.getSoBillId())).size();
			if (soHeader.getSoBillState().equals(SoBillStateEnum.CANCEL) && truckDetailSize.equals(0)) {
				throw new ServiceException(
					"当前容器：" + soTruckDetail.getLpnCode() + " 关联的出库单(" + soHeader.getSoBillNo() + ")已取消！");
			}
			//校验重复添加，如果未完成的装车明细中有当前容器编码则为重复添加
			//查询所有的正在装车订单
			List<SoTruckHeader> soTruckHeaderList = this.list(new QueryWrapper<SoTruckHeader>().lambda()
				.eq(SoTruckHeader::getTruckState, 10));
			List<Long> longList = new ArrayList<>();
			for (SoTruckHeader soTruckHeader : soTruckHeaderList) {
				longList.add(soTruckHeader.getTruckId());
			}
			//查询所有正在装车的明细
			List<SoTruckDetail> soTruckDetailListQuery = soTruckDetailService.list(new QueryWrapper<SoTruckDetail>().lambda()
				.in(SoTruckDetail::getTruckId, longList));

			if (soTruckDetailListQuery != null) {
				for (SoTruckDetail soTruckDetailCp : soTruckDetailListQuery) {
					if (soTruckDetailCp.getLpnCode().equals(soTruckDetail.getLpnCode())) {
						throw new ServiceException("LPN编码已存在:" + soTruckDetail.getLpnCode());
					}
				}
			}
			// 根据订单id获取波次号
			List<WellenDetail> wellenDetailList = wellenDetailService.listByBillId(soTruckDetail.getSoBillId());
			if (Func.isNotEmpty(wellenDetailList)) {
				soTruckDetail.setWellenId(wellenDetailList.get(0).getWellenId());
			}
			soTruckDetailService.save(soTruckDetail);
		}
		return true;
	}

	@Override
	public SoTruckHeaderVO getDetail(SoTruckHeader soTruckHeader) {
		List<SoTruckHeader> soTruckHeaderList = super.list(Condition.getQueryWrapper(soTruckHeader));
		if (Func.isEmpty(soTruckHeaderList)) {
			return null;
		}
		SoTruckHeaderVO soTruckHeaderVO = SoTruckHeaderWrapper.build().entityVO(soTruckHeaderList.get(0));
		if (Func.isEmpty(soTruckHeaderVO)) {
			return null;
		}
		// 获取所有明细
		List<SoTruckDetail> truckDetailList = soTruckDetailService.list(
			Condition.getQueryWrapper(
				new SoTruckDetail()).lambda().eq(SoTruckDetail::getTruckId, soTruckHeaderVO.getTruckId())
		);
		if (Func.isNotEmpty(truckDetailList)) {
			List<SoTruckDetailVO> detailVOList = new ArrayList<>();
			// 查询该装车单下发货库存日志
			List<TruckStockVO> truckStockList = TruckStockWrapper.build().listVO(
				truckStockService.list(
					Condition.getQueryWrapper(new TruckStock())
						.lambda()
						.eq(TruckStock::getTruckId, soTruckHeaderVO.getTruckId())));
			truckDetailList.forEach(detail -> {
				// 封装明细
				SoTruckDetailVO truckDetail = BeanUtil.copy(detail, SoTruckDetailVO.class);
				// 设置出库单编码
				SoHeader soHeader = soHeaderService.getById(truckDetail.getSoBillId());
				if (Func.isNotEmpty(soHeader)) {
					truckDetail.setSoBillNo(soHeader.getSoBillNo());
				}
				// 设置波次编码
				Wellen wellen = wellenService.getById(truckDetail.getWellenId());
				if (Func.isNotEmpty(wellen)) {
					truckDetail.setWellenNo(String.valueOf(wellen.getWellenNo()));
				}
				if (Func.isNotEmpty(truckDetail)) {
					// 根据明细的容器筛选日志
					List<TruckStockVO> filterList = truckStockList.stream().filter(truckStock -> {
						return truckStock.getTruckId().equals(detail.getTruckId()) &&
							truckStock.getLpnCode().equals(detail.getLpnCode());
					}).collect(Collectors.toList());
					// 循环日志
					if (Func.isNotEmpty(filterList)) {
						filterList.forEach(filter -> {
							SoTruckDetailVO temp = new SoTruckDetailVO();
							BeanUtil.copyProperties(truckDetail, temp);
							BeanUtil.copyProperties(filter, temp);
							detailVOList.add(temp);
						});
					} else {
						detailVOList.add(truckDetail);
					}
				}
			});

			soTruckHeaderVO.setTruckDetailList(detailVOList);
		}
		return soTruckHeaderVO;
	}

	private StockPdaVO getStockVo(StockDetail stock, int type) {
		StockPdaVO stockPdaVO = new StockPdaVO();
		Warehouse warehouse = WarehouseCache.getById(stock.getWhId());
		if (Func.isNotEmpty(warehouse)) {
			stockPdaVO.setWhName(warehouse.getWhName());
		}
		Location location = LocationCache.getById(stock.getLocId());
		if (Func.isNotEmpty(location)) {
			stockPdaVO.setLocCode(location.getLocCode());
		}
		stockPdaVO.setLpnCode(stock.getLpnCode());
		stockPdaVO.setStockQty(stock.getStockQty());
		if (type == 2) {
			Sku sku = SkuCache.getById(stock.getSkuId());
			if (Func.isNotEmpty(sku)) {
				stockPdaVO.setSkuName(sku.getSkuName());
				stockPdaVO.setSkuCode(sku.getSkuCode());
			}
			stockPdaVO.setStockId(stock.getStockId());
			stockPdaVO.setBillDetailId(stock.getBillDetailId());

		}
		return stockPdaVO;
	}
}
