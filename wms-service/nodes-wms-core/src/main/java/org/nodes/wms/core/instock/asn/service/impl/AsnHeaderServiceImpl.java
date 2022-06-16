package org.nodes.wms.core.instock.asn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.commons.nullanalysis.NotNull;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.allot.service.IAllotHeaderService;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.dto.SkuLogDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.*;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.instock.asn.cache.AsnCache;
import org.nodes.wms.core.instock.asn.dto.AsnDTO;
import org.nodes.wms.core.instock.asn.dto.AsnDetailDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderOrderDto;
import org.nodes.wms.core.instock.asn.entity.*;
import org.nodes.wms.core.instock.asn.enums.AsnDetailStatusEnum;
import org.nodes.wms.core.instock.asn.excel.AsnHeaderExcel;
import org.nodes.wms.core.instock.asn.excel.SnExcel;
import org.nodes.wms.core.instock.asn.mapper.AsnDetailMapper;
import org.nodes.wms.core.instock.asn.mapper.AsnHeaderMapper;
import org.nodes.wms.core.instock.asn.mapper.AsnLpnDetailMapper;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.instock.asn.service.IContainerLogService;
import org.nodes.wms.core.instock.asn.service.ISnService;
import org.nodes.wms.core.instock.asn.vo.*;
import org.nodes.wms.core.instock.asn.wrapper.AsnDetailWrapper;
import org.nodes.wms.core.instock.asn.wrapper.AsnHeaderWrapper;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.service.IAsnInventoryService;
import org.nodes.wms.core.instock.purchase.service.IPoDetailService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.dto.*;
import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.service.ILotService;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockMoveService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.enums.TransferTypeEnum;
import org.nodes.wms.core.stock.transfer.service.ITransferRecordService;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.utils.SkuLotUtil;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.enums.ZoneTypeEnum;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.ILpnService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.sku.enums.SkuLogTypeEnum;
import org.nodes.wms.dao.basics.sku.enums.SnEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收货单头表 服务实现类
 *
 * @author zx
 * @since 2019-12-12
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AsnHeaderServiceImpl<M extends AsnHeaderMapper, T extends AsnHeader> extends AbsBaseAsnHeaderService<AsnHeaderMapper, AsnHeader> implements IAsnHeaderService {
	@Autowired
	IAsnDetailService asnDetailService;
	@Autowired
	IPoDetailService poDetailService;
	@Autowired
	IContainerLogService containerLogService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	IStockService stockService;
	@Autowired
	IStockDetailService stockDetailService;
	@Autowired
	ITaskService taskService;
	@Autowired
	ISkuLogService skuLogService;
	@Autowired
	ITransferRecordService transferRecordService;
	@Autowired
	AsnDetailMapper asnDetailMapper;
	@Autowired
	ISnService snService;
	@Autowired
	ISkuUmService skuUmService;
	@Autowired
	ISkuLotService skuLotService;
	@Autowired
	AsnLpnDetailMapper asnLpnDetailMapper;
	@Autowired
	IInstockService instockService;
	@Autowired
	IAsnInventoryService asnInventoryService;
	@Autowired
	@Qualifier("lpnPutWayMoveService")
	IStockMoveService stockMoveService;

	@Autowired
	IDeptService deptService;
	@Autowired
	ISkuPackageService skuPackageService;

	/**
	 * 入库单管理 关闭收货单
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public boolean finishAsnBill(String ids) {
		if (Func.isNotEmpty(ids)) {
			List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList());
			List<String> billStateList = new ArrayList<>();
			billStateList.add(AsnBillStateEnum.NOT_RECEIPT.getCode() + "");
			billStateList.add(AsnBillStateEnum.PART.getCode() + "");

			if (Func.isNotEmpty(idList)) {
				for (Long id : idList) {
					List<AsnHeader> asnHeaderList = super.list(Condition.getQueryWrapper(new AsnHeader()).lambda()
						.eq(AsnHeader::getAsnBillId, id).in(AsnHeader::getAsnBillState, billStateList));
					if (Func.isNotEmpty(asnHeaderList)) {
						//修改值
						AsnHeader asnHeader = new AsnHeader();
						asnHeader.setAsnBillState(40);
						asnHeader.setFinishDate(LocalDateTime.now());
						//修改条件
						UpdateWrapper<AsnHeader> updateWrapper = new UpdateWrapper<>();
						updateWrapper.lambda().eq(AsnHeader::getAsnBillId, id);
						super.update(asnHeader, updateWrapper);
					} else {
						throw new ServiceException("单据为不可关闭状态");
					}
					taskService.closeTask(id, TaskTypeEnum.Check);
				}
			}
		}
		return true;
	}

	@Override
	public AsnHeaderVO getDetail(Long asnBillId) {
		//查询收货单详情
		AsnHeader asnHeader = super.getById(asnBillId);
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("订单已删除或不存在");
		}
		AsnHeaderVO asnHeaderVO = AsnHeaderWrapper.build().entityVO(asnHeader);
		//查询收货单明细分页列表
		List<AsnDetail> asnDetailList = asnDetailService.list(
			Condition.getQueryWrapper(new AsnDetail()).lambda()
				.eq(AsnDetail::getAsnBillId, asnHeader.getAsnBillId())
				.orderByAsc(AsnDetail::getAsnLineNo));
		asnHeaderVO.setAsnDetailList(AsnDetailWrapper.build().listVO(asnDetailList));
		return asnHeaderVO;
	}

	@Override
	public List<AsnHeaderVO> selectList(AsnHeaderDTO asnHeader) {
		List<AsnHeader> list = super.list(this.getQueryWrapper(asnHeader));
		return AsnHeaderWrapper.build().listVO(list);
	}

	/**
	 * 整托上架 获得上架信息
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	@Override
	public InstockExecuteVO queryStockByLpnCode(InStockSubmitVO inStockSubmitVO) {
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		List<Zone> stageLocations = zoneService.list(Wrappers.lambdaQuery(Zone.class).eq(Zone::getZoneType, 73));
		List<StockDetail> stockDetailList = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getZoneId, NodesUtil.toList(stageLocations, Zone::getZoneId))
			.eq(StockDetail::getLpnCode, inStockSubmitVO.getLpnCode())
		);
		if (Func.isEmpty(stockDetailList)) {
			return InstockExecuteVO.EMPTY;
		}
		List<StockDetail> stockDetails1 = stockDetailList.stream().filter(stockDetail -> Func.isEmpty(stockDetail.getLocId())
			&& BigDecimalUtil.gt(stockDetail.getUnreceivedQty(), BigDecimal.ZERO)).collect(Collectors.toList());
		AsnHeader asnHeader = null;
		IAsnHeaderService asnHeaderService = SpringUtil.getBean(IAsnHeaderService.class);
		if (Func.isNotEmpty(stockDetails1)) {
			asnHeader = asnHeaderService.getById(stockDetails1.get(0).getSoBillId());
			if (Func.isNotEmpty(asnHeader)) {
				AsnDTO asnDTO = new AsnDTO();
				asnDTO.setAsnBillNo(asnHeader.getAsnBillNo());
				asnDTO.setWhId(asnHeader.getWhId());
				asnDTO.setLocCode("STAGE");
				asnDTO.setLpnCodes(new ArrayList<String>() {{
					add(inStockSubmitVO.getLpnCode());
				}});
				submitAsnTray(asnDTO);
			}
		} else {
			asnHeader = asnHeaderService.getById(stockDetailList.get(0).getSoBillId());
		}
		stockDetailList = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getZoneId, NodesUtil.toList(stageLocations, Zone::getZoneId))
			.eq(StockDetail::getLpnCode, inStockSubmitVO.getLpnCode())
		);
		if (Func.isEmpty(stockDetailList)) {
			return InstockExecuteVO.EMPTY;
		}

		Warehouse warehouse = WarehouseCache.getById(stockDetailList.get(0).getWhId());
//
//		Location location1 = locationService.getOne(Wrappers.lambdaQuery(Location.class)
//			.eq(Location::getLocCode, "Stage" + warehouse.getWhCode()));
//		List<StockDetail> stockDetails = stockDetailList.stream().filter(stockDetail -> stockDetail.getLocId().equals(location1.getLocId()))
//			.collect(Collectors.toList());
//		if (Func.isEmpty(stockDetails)) {
//			return InstockExecuteVO.EMPTY;
//		}
		List<Stock> list = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
			.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
			.in(Stock::getStockId, NodesUtil.toList(stockDetailList, StockDetail::getStockId)));

		if (Func.isEmpty(list)) {
			return InstockExecuteVO.EMPTY;
		}
//		AsnDetail asnDetail = asnDetailService.getById(stockDetailList.get(0).getBillDetailId());
//		if (Func.isEmpty(asnDetail)) {
//			throw new ServiceException("库存指定订单明细不存在！");
//		}
//		AsnHeader asnHeader = super.getById(asnDetail.getAsnBillId());
//		if (Func.isEmpty(asnHeader)) {
//			throw new ServiceException("库存指定订单不存在！");
//		}
		//返回值
		InstockExecuteVO vo = instockService.execute(
			list, Func.isNotEmpty(asnHeader) ? asnHeader.getBillTypeCd() : null);
//		Set<Long> types = new HashSet<>();
//		for (Stock item : list) {
//			//种类
//			types.add(item.getSkuId());
//		}
		BigDecimal qty = stockDetailList.stream().map(StockDetail::getStockQty).reduce(BigDecimal::add).get();
		vo.setTypeCount(qty.intValue());
		//获得当前库位

		Location location = LocationCache.getById(list.get(0).getLocId());
		if (Func.isNotEmpty(location)) {
			vo.setLocCode(location.getLocCode());
			//库位状态
			if (Func.isNotEmpty(location.getLocStatus())) {
				vo.setLocStatus(location.getLocStatus());
				vo.setLocStatusName(DictCache.getValue(DictConstant.LOC_STATUS, location.getLocStatus()));
			}
		}

		if (Func.isNotEmpty(warehouse)) {
			vo.setWhId(warehouse.getWhId());
			vo.setWhName(warehouse.getWhName());
		}

		return vo;
	}

	@Override
	public IPage<AsnHeaderVO> selectPage(AsnHeaderDTO asnHeader, Query query) {
		IPage<AsnHeader> page = super.page(Condition.getPage(query), this.getQueryWrapper(asnHeader));
		return AsnHeaderWrapper.build().pageVO(page);
	}

	@Override
	public boolean saveOrUpdate(AsnHeaderDTO asnHeader) {

		//机构对象
//		Dept dept = SysCache.getDept(asnHeader.getDeptId());
//		if (Func.isNotEmpty(dept)) {
//			asnHeader.setDeptCode(dept.getDeptCode());//机构编码
//			asnHeader.setDeptName(dept.getDeptName());//机构名称
//		}
		if (Func.isEmpty(asnHeader.getAsnDetailList())) {
			throw new ServiceException(String.format("至少有一条收货明细"));
		}
		//库房
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或者已删除");
		}
		asnHeader.setWhCode(warehouse.getWhCode());//库房编码
		//货主编码
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(asnHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("货主不存在或者已删除");
		}
		asnHeader.setOwnerCode(owner.getOwnerCode());
		asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());//单据状态

		//上位系统单据唯一标识
//		if (Func.isEmpty(asnHeader.getBillKey())) {
//			asnHeader.setBillKey(asnHeader.getAsnBillNo());
//		}
//		//上位系统最后更新时间
//		if (Func.isEmpty(asnHeader.getLastUpdateDate())) {
//			asnHeader.setLastUpdateDate(LocalDateTime.now());
//		}
//		//上位系统订单创建时间
//		if (Func.isEmpty(asnHeader.getPreCreateDate())) {
//			asnHeader.setPreCreateDate(LocalDateTime.now());
//		}
		//创建类型
		if (Func.isEmpty(asnHeader.getCreateType())) {
			asnHeader.setCreateType(10);
		}

		if (Func.isEmpty(asnHeader.getAsnBillId())) {
			while (super.count(Condition.getQueryWrapper(new AsnHeader()).lambda()
				.eq(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo())) > 0) {
				asnHeader.setAsnBillNo(AsnCache.getAsnBillNo());
			}
			asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());
//			asnHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		}
		if (Func.isEmpty(asnHeader.getAsnBillNo())) {
			asnHeader.setAsnBillNo(AsnCache.getAsnBillNo());
		}
		if (super.saveOrUpdate(asnHeader)) {
			stockDetailService.remove(Condition.getQueryWrapper(new StockDetail()).lambda()
				.eq(StockDetail::getSoBillId, asnHeader.getAsnBillId())
				.gt(StockDetail::getUnreceivedQty, BigDecimal.ZERO));
			//保存明细表
			if (AsnBillStateEnum.NOT_RECEIPT.getCode().equals(asnHeader.getAsnBillState())) {
				List<AsnDetailDTO> detailList = asnHeader.getAsnDetailList();
				for (AsnDetailDTO detail : detailList) {
					detail.setAsnBillId(asnHeader.getAsnBillId());
					detail.setAsnBillNo(asnHeader.getAsnBillNo());
					asnDetailService.saveOrUpdate(detail);

					this.addStockDetail(asnHeader, detail);
				}
			}
		}
		return true;
	}

	@Override
	public boolean saveOrUpdateByAllot(AsnHeaderDTO asnHeader) {

		//机构对象
//		Dept dept = SysCache.getDept(asnHeader.getDeptId());
//		if (Func.isNotEmpty(dept)) {
//			asnHeader.setDeptCode(dept.getDeptCode());//机构编码
//			asnHeader.setDeptName(dept.getDeptName());//机构名称
//		}
		if (Func.isEmpty(asnHeader.getAsnDetailList())) {
			throw new ServiceException(String.format("至少有一条收货明细"));
		}
		//库房
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或者已删除");
		}
		asnHeader.setWhCode(warehouse.getWhCode());//库房编码
		//货主编码
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(asnHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("货主不存在或者已删除");
		}
		asnHeader.setOwnerCode(owner.getOwnerCode());
		asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());//单据状态

		//上位系统单据唯一标识
//		if (Func.isEmpty(asnHeader.getBillKey())) {
//			asnHeader.setBillKey(asnHeader.getAsnBillNo());
//		}
//		//上位系统最后更新时间
//		if (Func.isEmpty(asnHeader.getLastUpdateDate())) {
//			asnHeader.setLastUpdateDate(LocalDateTime.now());
//		}
//		//上位系统订单创建时间
//		if (Func.isEmpty(asnHeader.getPreCreateDate())) {
//			asnHeader.setPreCreateDate(LocalDateTime.now());
//		}
		//创建类型
		if (Func.isEmpty(asnHeader.getCreateType())) {
			asnHeader.setCreateType(10);
		}

		if (Func.isEmpty(asnHeader.getAsnBillId())) {
			while (super.count(Condition.getQueryWrapper(new AsnHeader()).lambda()
				.eq(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo())) > 0) {
				asnHeader.setAsnBillNo(AsnCache.getAsnBillNo());
			}
			asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());
//			asnHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		}
		if (Func.isEmpty(asnHeader.getAsnBillNo())) {
			asnHeader.setAsnBillNo(AsnCache.getAsnBillNo());
		}
		if (super.saveOrUpdate(asnHeader)) {
			//保存明细表
			if (AsnBillStateEnum.NOT_RECEIPT.getCode().equals(asnHeader.getAsnBillState())) {
				List<AsnDetailDTO> detailList = asnHeader.getAsnDetailList();
				for (AsnDetailDTO detail : detailList) {
					detail.setAsnBillId(asnHeader.getAsnBillId());
					detail.setAsnBillNo(asnHeader.getAsnBillNo());
					asnDetailService.saveOrUpdateByAllot(detail);
				}
			}

		}
		return true;
	}


	@Override
	public boolean removeById(Serializable id) {
		return this.removeByIds(Func.toLongList(Func.toStr(id)));
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = false;
		for (Serializable id : idList) {
			//状态不是单据创建,不可删除
			AsnHeader asnHeader = super.getById(id);
			if (Func.isEmpty(asnHeader)) {
				throw new ServiceException("单据不存在！");
			}
			if (!AsnBillStateEnum.NOT_RECEIPT.getCode().equals(asnHeader.getAsnBillState())) {
				throw new ServiceException(
					String.format("单据[%s]为%s状态不可删除",
						asnHeader.getAsnBillNo(),
						DictCache.getValue(DictConstant.ASN_BILL_STATE, asnHeader.getAsnBillState())));
			}
			if (asnHeader.getBillTypeCd().equals("109")) {
				throw new ServiceException("调拨入货单不可删除! ");
			}
		}
		if (Func.isNotEmpty(idList) && super.removeByIds(idList)) {
			result = asnDetailService.remove(Condition.getQueryWrapper(new AsnDetail())
				.lambda()
				.in(AsnDetail::getAsnBillId, idList));
			// 同时清空库存明细
			stockDetailService.remove(Condition.getQueryWrapper(new StockDetail()).lambda()
				.in(StockDetail::getSoBillId, idList));
		}
		return result;
	}

	@Override
	public boolean canEdit(Long asnHeaderId) {
		AsnHeader asnHeader = super.getById(asnHeaderId);
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("入库单不存在！（订单ID：" + asnHeaderId + " ）");
		}
		// 盘盈单不允许编辑
		Param param = ParamCache.getOne(ParamEnum.COUNT_PROFIT_TYPECD.getKey());
		if (asnHeader.getBillTypeCd().equals(param.getParamValue())) {
			throw new ServiceException("当前单据类型不允许编辑! ");
		}
		if (asnHeader.getBillTypeCd().equals("109")) {
			throw new ServiceException("调拨入货单不允许编辑! ");
		}
		// 验证入库单状态
		boolean result = AsnBillStateEnum.NOT_RECEIPT.getCode().equals(asnHeader.getAsnBillState());
//			|| AsnBillStateEnum.PART.getIndex().equals(asnHeader.getAsnBillState())
//			|| AsnBillStateEnum.PART.getIndex().equals(asnHeader.getAsnBillState());

//		if (result) {
//			// 验证是否已经生成了LPN
//			result = asnLpnDetailMapper.selectCount(Condition.getQueryWrapper(new AsnLpnDetail()).lambda()
//				.eq(AsnLpnDetail::getAsnBillId, asnHeaderId)) == 0;
//		}
		return result;
	}

	/**
	 * 按件收货 查询收货单头表列表
	 *
	 * @param asnHeader
	 * @return
	 */
	@Override
	public List<AsnHeader> listForPDA(AsnHeader asnHeader) {
		List<AsnHeader> list = baseMapper.selectList(Condition.getQueryWrapper(asnHeader).lambda()
			.ne(AsnHeader::getAsnBillState, AsnBillStateEnum.COMPLETED.getCode()));
		return list;
	}

	/**
	 * 托盘转移
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	@Override
	public boolean submitMove(InStockSubmitVO inStockSubmitVO) {
		if (Func.isEmpty(inStockSubmitVO.getLpnCode())) {
			throw new ServiceException("请先扫描容器");
		}

		if (Func.isEmpty(inStockSubmitVO.getLocCode())) {
			throw new ServiceException("目标库位不能为空");
		}

		if (Func.isEmpty(inStockSubmitVO.getWhId())) {
			throw new ServiceException("库房ID不能为空");
		}

		//查询出来库存中在当前容器里的所有状态正常的物品
		List<Stock> list = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
//			.eq(Stock::getLpnCode, inStockSubmitVO.getLpnCode())
			.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
			.apply("stock_qty - pick_qty > 0"));

		if (Func.isEmpty(list)) throw new ServiceException(String.format("容器[%s]上没有可移动的物品"
			, inStockSubmitVO.getLpnCode()));
		//原库位id
		Long oldLocId = list.get(0).getLocId();

		//查询目标库位信息
		Location location = LocationCache.getValid(inStockSubmitVO.getLocCode());
		if (Func.isEmpty(location)) {
			throw new ServiceException(String.format("目标库位不属于当前库房"));
		}

		//记录系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.MOVE);
		systemProcParam.setAction(ActionEnum.MOVE_LPN);
		systemProcParam.setLpnCode(inStockSubmitVO.getLpnCode());
		systemProcParam.setWhId(inStockSubmitVO.getWhId());
		SystemProc systemProc = systemProcService.create(systemProcParam);

		BladeUser user = AuthUtil.getUser();
		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setSourceLpnCode(inStockSubmitVO.getLpnCode());
		stockMoveDTO.setSourceLocId(oldLocId); //原库位id 从数据库中查询
		stockMoveDTO.setTargetLocId(location.getLocId()); //目标库位id 通过传递的locCode查询locId
		stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
		stockMoveDTO.setEventType(EventTypeEnum.Move);
		stockService.stockMoveByLpn(stockMoveDTO);

		return true;
	}

	@Override
	public AsnHeaderVO getAsnHeaderAndDetails(String asnBillNo) {
		AsnHeader asnHeader = baseMapper.selectOne(Wrappers.lambdaQuery(AsnHeader.class).eq(AsnHeader::getAsnBillNo, asnBillNo));
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("订单不存在！");
		}
		List<AsnDetail> asnDetails;
		if (ParamCache.IS_OVER_VALUE == 0) {
			asnDetails = asnDetailService.list(Wrappers.lambdaQuery(AsnDetail.class)
				.eq(AsnDetail::getAsnBillNo, asnBillNo)
				.gt(AsnDetail::getSurplusQty, BigDecimal.ZERO)
			);
			if (Func.isNotEmpty(asnDetails)) {
				asnDetails.forEach(asnDetail -> asnDetail.setPlanQty(asnDetail.getSurplusQty()));
			}
		} else {
			asnDetails = asnDetailService.list(Wrappers.lambdaQuery(AsnDetail.class)
				.eq(AsnDetail::getAsnBillNo, asnBillNo)
			);
		}
		AsnHeaderVO asnHeaderVO = AsnHeaderWrapper.build().entityVO(asnHeader);
		List<AsnDetailVO> asnDetailVOS = AsnDetailWrapper.build().listVO(asnDetails);
		asnDetailVOS.forEach(asnDetailVO -> {
			SkuPackage skuPackage = SkuPackageCache.getById(asnDetailVO.getWspId());
			if (Func.isEmpty(skuPackage)) {
				throw new ServiceException("包装不存在或者已删除");
			}
			//包装明细列表
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
			if (skuPackageDetails.size() <= 0) {
				throw new ServiceException(String.format("包装[%s]没有包装明细", skuPackage.getWspId()));
			}
			asnDetailVO.setSkuPackageDetails(skuPackageDetails);
		});
		asnHeaderVO.setAsnDetailList(asnDetailVOS);
		return asnHeaderVO;
	}

	@Override
	public boolean submitAsnHeaderWithOrder(AsnHeaderOrderDto asnHeaderOrderDto) {
		// 获取入库单
		AsnHeader asnHeader = super.getOne(Wrappers.lambdaQuery(AsnHeader.class).eq(AsnHeader::getAsnBillNo, asnHeaderOrderDto.getAsnBillNo()));
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("订单不存在(订单编号:" + asnHeaderOrderDto.getAsnBillNo() + ")");
		}
		if (asnHeader.getAsnBillState() == AsnBillStateEnum.COMPLETED.getCode()) {
			throw new ServiceException("该单据已完成或者已关闭");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("订单：" + asnHeader.getAsnBillNo() + " 已取消，请停止收货！");
		}
		//判断库位是否存在
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("入库单所选库房不存在或已经删除！");
		}
		checkLoc(asnHeader.getWhId(), asnHeaderOrderDto.getLocCode());
		// 更新任务开始时间
		taskService.updateBeginTime(asnHeaderOrderDto.getAsnBillNo(), TaskTypeEnum.Check);
		// 获取当前收货的所有明细
		List<AsnDetail> asnDetailList = asnDetailService.list(Wrappers.lambdaQuery(AsnDetail.class)
			.eq(AsnDetail::getAsnBillNo, asnHeaderOrderDto.getAsnBillNo()));
		if (Func.isEmpty(asnDetailList)) {
			throw new ServiceException("订单明细不存在！");
		}
		//更新容器明细信息
		UpdateWrapper<AsnLpnDetail> asnLpnDetailUpdateWrapper = new UpdateWrapper<>();
		asnLpnDetailUpdateWrapper.lambda().set(AsnLpnDetail::getLpnStatus, "2")
			.eq(AsnLpnDetail::getAsnBillNo, asnHeaderOrderDto.getAsnBillNo())
			.eq(AsnLpnDetail::getLpnStatus, StringPool.ONE);
		asnLpnDetailMapper.update(null, asnLpnDetailUpdateWrapper);

		asnDetailList.forEach(asnDetail -> {
			AsnDetailVO asnDetailVO = asnHeaderOrderDto.getAsnDetailMap().stream()
				.filter(asnDetailVO1 -> asnDetailVO1.getAsnDetailId().equals(asnDetail.getAsnDetailId()))
				.findFirst().orElse(null);
			if (Func.isNotEmpty(asnDetailVO)) {
				//查询库位对象,对库位是否可用进行验证
				Location location = null;
				if (ZoneVirtualTypeEnum.isVirtual(asnHeaderOrderDto.getLocCode())) {
					location = LocationCache.getValid(
						Func.firstCharToUpper(asnHeaderOrderDto.getLocCode().toLowerCase()) + warehouse.getWhCode());
				} else {
					location = LocationCache.getValid(asnHeaderOrderDto.getLocCode());
				}
				if (Func.isEmpty(location)) {
					throw new ServiceException("入库单所选库房不存在入库暂存区！");
				}
				// 获取物品详细信息
				Sku sku = SkuCache.getById(asnDetail.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("订单明细指定物品(物品ID:" + asnDetail.getSkuId() + ")不存在！");
				}
				// 获取明细对应的包装信息
				SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
				if (Func.isEmpty(skuPackage)) {
					throw new ServiceException("订单明细对应包装(包装ID:" + asnDetail.getWspId() + ")不存在！");
				}
//				SkuPackageDetail one = SkuPackageDetailCache.getOne(asnDetail.getWspId(), asnDetailVO.getSkuLevel());
//				BigDecimal scanQty = asnDetailVO.getPlanQty().multiply(BigDecimal.valueOf(one.getConvertQty()));
				BigDecimal add = asnDetailVO.getPlanQty().add(asnDetail.getScanQty());
				if (ParamCache.IS_OVER_VALUE == 0) {
					if (BigDecimalUtil.gt(add, asnDetail.getPlanQty())) {
						throw new ServiceException("接收数量不能超过计划数量!");
					}
				}
				asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
				asnDetail.setScanQty(add);
				if (BigDecimalUtil.gt(asnDetail.getPlanQty(), asnDetail.getScanQty())) {
					asnDetail.setSurplusQty(asnDetail.getPlanQty().subtract(asnDetail.getScanQty()));
				} else {
					asnDetail.setSurplusQty(BigDecimal.ZERO);
				}
				//系统日志记录
				SystemProcDTO systemProcParam = new SystemProcDTO();
				systemProcParam.setProcType(SystemProcTypeEnum.ASN);
				systemProcParam.setDataType(DataTypeEnum.AsnBillNo);
				systemProcParam.setAction(ActionEnum.RECEIVE_ORDER);
				systemProcParam.setBillNo(asnHeaderOrderDto.getAsnBillNo());
				systemProcParam.setOperationQty(asnDetailVO.getScanQty());
				systemProcParam.setOperationUnit(asnDetailVO.getUmName());
				systemProcParam.setWhId(asnHeaderOrderDto.getWhId());
				Long systemProcId = systemProcService.create(systemProcParam).getSystemProcId();

				StockAddDTO stockProc = new StockAddDTO();
				stockProc.setSystemProcId(systemProcId);//日志id
				stockProc.setLocId(location.getLocId());//库位id
				stockProc.setSkuId(asnDetail.getSkuId());//物品id
				stockProc.setWspId(asnDetail.getWspId());//包装id
				stockProc.setStockQty(asnDetailVO.getPlanQty());//数量
				stockProc.setEventType(EventTypeEnum.Check);//事务类型
				stockProc.setBillId(asnDetail.getAsnBillId());//收货单id
				stockProc.setBillDetailId(asnDetail.getAsnDetailId());//收货单明细id
				stockProc.setBillNo(asnDetail.getAsnBillNo());//收货单编码
				stockProc.setLpnCode(asnHeaderOrderDto.getLpnCode());
				SkuLotUtil.fill(sku, stockProc, asnDetail, asnDetail);
				if (Func.isNotEmpty(stockProc.getSkuLot1()) && Func.isNotEmpty(sku.getQualityHours())) {
					Date skulot1 = DateUtil.parse(stockProc.getSkuLot1(), "yyyyMMdd");
					Integer qualityHours = sku.getQualityHours();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(skulot1);
					calendar.add(Calendar.DATE, qualityHours);
					Date time = calendar.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String format = sdf.format(time);
					stockProc.setValidTime(format);
				}
				if (StringPool.Y.toUpperCase().equals(asnDetail.getImportSn())) {
					List<Sn> list = snService.list(Wrappers.lambdaQuery(Sn.class).eq(Sn::getAsnDetailId, asnDetail.getAsnDetailId()));
					if (Func.isNotEmpty(list)) {
						stockProc.setSerialList(NodesUtil.toList(list, Sn::getSnDetailCode));
					}
				}
				Stock stock = stockService.add(stockProc);
				// 插入清点记录
				ContainerLog containerLog = new ContainerLog();
				containerLog.setAsnBillId(asnDetail.getAsnBillId()); //订单id
				containerLog.setAsnBillNo(asnHeaderOrderDto.getAsnBillNo()); //单据编码
				containerLog.setAsnDetailId(asnDetail.getAsnDetailId()); //订单明细id
				containerLog.setLocId(location.getLocId());//库位id
				containerLog.setLocCode(asnHeaderOrderDto.getLocCode());//库位编码
				containerLog.setAclQty(asnDetail.getScanQty()); //数量
				containerLog.setSkuId(asnDetail.getSkuId());//物品id
				containerLog.setSkuName(asnDetail.getSkuName()); //物品名称
				containerLog.setWspId(asnDetail.getWspId());
				containerLog.setSkuLevel(asnDetail.getSkuLevel()); //包装层级
				containerLog.setAclStatus(1); //清点状态
				containerLog.setLotNumber(stock.getLotNumber()); //批次号
				containerLog.setSkuSpec(asnDetail.getSkuSpec()); //规格
				containerLog.setPlanQty(asnDetail.getPlanQty()); //计划数量
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (!asnDetail.skuLotChk(i)) {
						continue;
					}
					containerLog.skuLotSet(i, asnDetail.skuLotGet(i));
				}
				containerLogService.save(containerLog);
				// 保存物品操作记录
				SkuLogDTO skuLogDTO = new SkuLogDTO();
				skuLogDTO.setSkuId(asnDetail.getSkuId());
				skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.INSTOCK);
				this.skuLogService.saveOrUpdate(skuLogDTO);
			}
		});

		asnDetailService.updateBatchById(asnDetailList);
		// 获取未收完的明细数量
		Integer unReceiveCount = (int) asnDetailList.stream().filter(u -> {
			return BigDecimalUtil.gt(u.getSurplusQty(), BigDecimal.ZERO);
		}).count();

		if (unReceiveCount == 0 && ParamCache.IS_OVER_VALUE == 0) {
			this.updateAsnBillState(asnHeader.getAsnBillId(), AsnBillStateEnum.COMPLETED);
			taskService.closeTask(asnHeader.getAsnBillId(), TaskTypeEnum.Check);
		} else {
			this.updateAsnBillState(asnHeader.getAsnBillId(), AsnBillStateEnum.PART);
		}
		return true;
	}

	@Override
	public void submitAsnTray(AsnDTO dto) {
		AsnHeader asnHeader = super.getOne(Condition.getQueryWrapper(new AsnHeader())
			.lambda()
			.eq(AsnHeader::getAsnBillNo, dto.getAsnBillNo()));
		if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.COMPLETED.getCode())) {
			throw new ServiceException("该单据已完成或者已关闭");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("订单：" + asnHeader.getAsnBillNo() + " 已取消，请停止收货！");
		}
//		dto.setSName(asnHeader.getSName());
		dto.setSName(asnHeader.getSupplierName());
		Location location = checkLoc(dto.getWhId(), dto.getLocCode());
		dto.setLocId(location.getLocId());
		List<String> lpnCodes = dto.getLpnCodes();
		if (Func.isEmpty(lpnCodes)) {
			throw new ServiceException("扫描托盘不能为空！");
		}
		List<AsnDetail> asnDetails = asnDetailService.list(asnHeader.getAsnBillId());
		if (Func.isEmpty(asnDetails)) {
			throw new ServiceException("单据明细不存在！");
		}

		//过滤出托级明细
//		List<AsnDetail> lpnDetails = asnDetails.stream().
//			filter(asnDetail -> asnDetail.getSkuLevel().equals(SkuLevelEnum.Plate.getIndex()))
//			.collect(Collectors.toList());

		//根据托级明细ID查询库存明细表
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getSkuId, NodesUtil.toList(asnDetails, AsnDetail::getSkuId))
			.in(StockDetail::getLpnCode, dto.getLpnCodes()).eq(StockDetail::getWhId, dto.getWhId())
			.gt(StockDetail::getUnreceivedQty, BigDecimal.ZERO));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("没有查询到该单据相关托盘物品信息！");
		}

		List<String> lpnCodesTemp = NodesUtil.toList(stockDetails, StockDetail::getLpnCode);
		for (String lpncode : lpnCodes) {
			if (!lpnCodesTemp.contains(lpncode)) {
				throw new ServiceException(String.format("没有查询到[%s]该托盘物品信息！", lpncode));
			}
			List<StockDetail> stockDetailList = stockDetails.stream()
				.filter(stockDetail -> stockDetail.getLpnCode().equals(lpncode)).collect(Collectors.toList());
			for (StockDetail stockDetail : stockDetailList) {
				SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(stockDetail.getWspId(), SkuLevelEnum.Plate.getIndex());
				if (Func.isEmpty(packageDetail)) {
					throw new ServiceException("包装明细不存在或者已删除");
				}
				StockAddDTO stockProc = new StockAddDTO();
				stockProc.setLocId(location.getLocId());
				stockProc.setSkuId(stockDetail.getSkuId());
				stockProc.setWspId(stockDetail.getWspId());
				stockProc.setBoxCode("");
				stockProc.setLotNumber(stockDetail.getLotNumber());
				stockProc.setBillDetailId(stockDetail.getBillDetailId());
				stockProc.setBillId(stockDetail.getSoBillId());
				stockProc.setStockQty(stockDetail.getUnreceivedQty());
				stockProc.setLpnCode(lpncode);
				stockProc.setWellenId(null);
				stockProc.setSdId(stockDetail.getId());
				stockProc.setSerialList(null);
				dto.setLpnCode(lpncode);
				dto.setWspId(stockDetail.getWspId());
				dto.setWspdId(packageDetail.getWspdId());
				Long systemProcId = saveSystemProcLog(dto, stockDetail.getUnreceivedQty());
				stockProc.setSystemProcId(systemProcId);
				Stock stock = stockService.add(stockProc);
				// 插入清点记录
				ContainerLog containerLog = new ContainerLog();
				containerLog.setAsnBillId(asnHeader.getAsnBillId()); //订单id
				containerLog.setAsnBillNo(asnHeader.getAsnBillNo()); //单据编码
				containerLog.setAsnDetailId(stockDetail.getBillDetailId()); //订单明细id
				containerLog.setLocId(location.getLocId());//库位id
				containerLog.setLocCode(location.getLocCode());//库位编码
				containerLog.setLpnCode(stockDetail.getLpnCode());
				containerLog.setAclQty(stockDetail.getUnreceivedQty()); //数量
				containerLog.setSkuId(stockDetail.getSkuId());//物品id
				containerLog.setSkuName(stock.getSkuName()); //物品名称
				containerLog.setWspId(stock.getWspId());
				containerLog.setSkuLevel(stock.getSkuLevel()); //包装层级
				containerLog.setAclStatus(1); //清点状态
				containerLog.setLotNumber(stockDetail.getLotNumber()); //批次号
				containerLog.setPlanQty(stockDetail.getUnreceivedQty()); //计划数量
				containerLogService.save(containerLog);

				List<AsnDetail> asnDetailList = asnDetails.stream().filter(asnDetail -> asnDetail.getSkuId().equals(stockDetail.getSkuId())).collect(Collectors.toList());
				List<AsnDetail> unUpdateAsnDetails = new ArrayList<>();
				for (AsnDetail asnDetail : asnDetailList) {
					if (BigDecimalUtil.eq(asnDetail.getSurplusQty(), stockDetail.getUnreceivedQty())) {
						asnDetail.setSurplusQty(BigDecimal.ZERO);
						asnDetail.setScanQty(asnDetail.getScanQty().add(stockDetail.getUnreceivedQty()));
						asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
						unUpdateAsnDetails.add(asnDetail);
						break;
					}
					if (BigDecimalUtil.lt(asnDetail.getSurplusQty(), stockDetail.getUnreceivedQty())) {
						asnDetail.setSurplusQty(BigDecimal.ZERO);
						asnDetail.setScanQty(asnDetail.getScanQty().add(asnDetail.getSurplusQty()));
						asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
						unUpdateAsnDetails.add(asnDetail);
						continue;
					}
					if (BigDecimalUtil.gt(asnDetail.getSurplusQty(), stockDetail.getUnreceivedQty())) {
						asnDetail.setSurplusQty(asnDetail.getSurplusQty().subtract(stockDetail.getUnreceivedQty()));
						asnDetail.setScanQty(asnDetail.getScanQty().add(stockDetail.getUnreceivedQty()));
						asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
						unUpdateAsnDetails.add(asnDetail);
						continue;
					}


				}
				asnDetailService.updateBatchById(unUpdateAsnDetails);
			}
		}
		// 获取未收完的明细数量
		List<AsnDetail> asnDetailList = asnDetailService.list(asnHeader.getAsnBillId());
		Integer unReceiveCount = (int) asnDetailList.stream().filter(u -> {
			return BigDecimalUtil.gt(u.getSurplusQty(), BigDecimal.ZERO);
		}).count();

		if (unReceiveCount != 0) {
			this.updateAsnBillState(asnHeader.getAsnBillId(), AsnBillStateEnum.PART);
		} else {
			this.updateAsnBillState(asnHeader.getAsnBillId(), AsnBillStateEnum.COMPLETED);
		}
	}

	@Override
	public boolean submitPutawayNew(PutawayByTranSubmitVO putawayByTranSubmitVO) {
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		if (ZoneVirtualTypeEnum.isVirtual(putawayByTranSubmitVO.getTargetLocCode())) {
			throw new ServiceException("目标库位不能是暂存库位！");
		}
		List<Zone> zones = zoneService.list(Wrappers.lambdaQuery(Zone.class).eq(Zone::getZoneType, 73));
		//查询出来库存中在当前容器里的所有状态正常的物品
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.in(StockDetail::getZoneId, NodesUtil.toList(zones, Zone::getZoneId))
			.in(StockDetail::getLpnCode, NodesUtil.toList(putawayByTranSubmitVO.getLpnItems(), PutawayLpnItemVo::getLpnCode))
			.apply("stock_qty > pick_qty"));

		for (PutawayLpnItemVo putawayLpnItemVo : putawayByTranSubmitVO.getLpnItems()) {
			Location location = LocationCache.getValid(putawayByTranSubmitVO.getTargetLocCode());
			if (!location.getWhId().equals(putawayByTranSubmitVO.getWhId())) {
				throw new ServiceException(String.format(
					"扫描库位[%s] 不属于容器[%s]所在库房！", putawayByTranSubmitVO.getTargetLocCode(), putawayLpnItemVo.getLpnCode()));
			}

			if (!StringPool.UNKNOWN.equals(putawayLpnItemVo.getZoneCode())) {//库区不等于unknown 需要校验库位是否属于该库区

				Zone zone = zoneService.getById(location.getZoneId());
				if (Func.isEmpty(zone)) {
					throw new ServiceException(String.format("库区[%s]不存在或者已删除", location.getZoneId()));
				}
				if (!putawayLpnItemVo.getZoneCode().contains(zone.getZoneCode())) {
					throw new ServiceException(String.format("目标库位[%s]不属于[%s]库区", putawayByTranSubmitVO.getTargetLocCode(), zoneService.getById(location.getZoneId()).getZoneName()));
				}
			}

			List<StockDetail> stockDetails = stockDetailList.stream().filter(stockDetail -> stockDetail.getLpnCode()
				.equals(putawayLpnItemVo.getLpnCode())).collect(Collectors.toList());

			if (Func.isEmpty(stockDetails)) {
				throw new ServiceException(String.format("容器[%s]上没有可上架物品", putawayLpnItemVo.getLpnCode()));
			}

			for (StockDetail stockDetail : stockDetails) {
				//记录系统日志
				SystemProcDTO systemProcParam = new SystemProcDTO();
				systemProcParam.setProcType(SystemProcTypeEnum.ASN);
				systemProcParam.setAction(ActionEnum.PUTAWAY_LPN);
				systemProcParam.setLpnCode(stockDetail.getLpnCode());
				systemProcParam.setWhId(putawayByTranSubmitVO.getWhId());
				SystemProc systemProc = systemProcService.create(systemProcParam);

				StockMoveDTO stockMoveDTO = new StockMoveDTO();
				stockMoveDTO.setSourceLpnCode(stockDetail.getLpnCode());
				stockMoveDTO.setSourceLocId(stockDetail.getLocId());
				stockMoveDTO.setSdId(stockDetail.getId());
				stockMoveDTO.setTargetLocId(location.getLocId());
				stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
				stockMoveDTO.setEventType(EventTypeEnum.Putaway);
				stockService.stockMoveByLpn(stockMoveDTO);
				InStockSubmitVO inStockSubmitVO = new InStockSubmitVO();
				inStockSubmitVO.setTargetLocId(location.getLocId());
				inStockSubmitVO.setSourceLocId(stockDetail.getLocId());
				this.saveTransferRecord(inStockSubmitVO, stockDetail);
			}
		}
		return true;
	}

	/**
	 * 按件收货 提交入库
	 *
	 * @param dto
	 * @return
	 */
	@Override
	public synchronized SubmitAsnHeaderVO submitAsnHeader(AsnDTO dto) {
		boolean isAutoPutaway = false;

		//查询单据信息 用于清点记录中的供应商名;
		AsnHeader asnHeader = super.getOne(Condition.getQueryWrapper(new AsnHeader())
			.lambda()
			.eq(AsnHeader::getAsnBillNo, dto.getAsnBillNo()));
		if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.COMPLETED.getCode())) {
			throw new ServiceException("该单据已完成或者已关闭");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("订单：" + asnHeader.getAsnBillNo() + " 已取消，请停止收货！");
		}
		AsnInventory asnInventory = null;
		Task task = taskService.getById(dto.getTaskId());
		if (Func.isNotEmpty(task)) {
			asnInventory = asnInventoryService.getOne(Condition.getQueryWrapper(new AsnInventory()).lambda()
				.eq(AsnInventory::getAsnBillNo, task.getTaskRemark()));
		}
		//供应商名 用于清点记录
//		dto.setSName(asnHeader.getSName());
		dto.setSName(asnHeader.getSupplierName());
		//校验库
//		Warehouse warehouse = WarehouseCache.getById(dto.getWhId());
//		if (Func.isEmpty(warehouse)) {
//			throw new ServiceException("库房不存在或者已删除");
//		}
//		String locCode = null;
//		if (ZoneVirtualTypeEnum.isVirtual(dto.getLocCode())) {
//			locCode = Func.firstCharToUpper(dto.getLocCode().toLowerCase()) + warehouse.getWhCode();
//		} else {
//			locCode = dto.getLocCode();
//		}
//		Location location = LocationCache.getValid(locCode);
		Location location = checkLoc(dto.getWhId(), dto.getLocCode());
		dto.setLocId(location.getLocId());

		//本次实际接收的物品数量
		BigDecimal realQty;

		Sku sku = SkuCache.getById(dto.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或者已删除");
		}
		// 判断是否是序列号管理 0:不是,1:是
		if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
			// 是序列号管理,不需要乘以换算倍率
			realQty = dto.getScanQty();
		} else {
			// 本次接收的物品数量(基础单位)=本次接收的物品数量(非基础单位)*包装明细中的换算倍率
			SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(dto.getWspdId());
			if (Func.isEmpty(skuPackageDetail)) {
				throw new ServiceException("包装明细不存在或者已删除");
			}
			realQty = dto.getScanQty().multiply(new BigDecimal(skuPackageDetail.getConvertQty()));
		}
		// 更新任务开始时间
		taskService.updateBeginTime(dto.getAsnBillNo(), TaskTypeEnum.Check);
		// 获取入库单下所有明细
		List<AsnDetail> asnDetailListAll = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
			.lambda()
			.eq(AsnDetail::getAsnBillId, asnHeader.getAsnBillId()));
		// 筛选出当前收货的物品
		List<AsnDetail> asnDetailList = asnDetailListAll.stream().filter(u -> {
			boolean result = false;
			if (u.getSkuId().equals(dto.getSkuId())) {
				result = true;
				// 比较批属性
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					String value = dto.skuLotGet(i);
					if (!u.skuLotChk(i)) {
						continue;
					}
					if (!u.skuLotGet(i).equals(value)) {
						result = false;
						break;
					}
				}
			}
			return result;
		}).collect(Collectors.toList());
		if (Func.isEmpty(asnDetailList)) {
			throw new ServiceException(
				String.format("入库单[%s] 中不存在物品[%s]! ", asnHeader.getAsnBillNo(), sku.getSkuName()));
		}

		// 当前批属性下的单据明细物品总数
		BigDecimal skuLotQty = BigDecimal.ZERO;
		for (AsnDetail asnDetail : asnDetailList) { // 这个是对比实际收货数量与明细总数量
			skuLotQty = skuLotQty.add(asnDetail.getSurplusQty());
		}
		//首先判断是否允许超收 0:不允许,1:允许
		if (0 == ParamCache.IS_OVER_VALUE) {
			// 不允许超收的情况下 若超收则提示不可超收
			if (realQty.compareTo(skuLotQty) > 0) {
				throw new ServiceException("接收数量不能超过计划数量");
			}
		}

		//系统日志记录
		Long systemProcId = saveSystemProcLog(dto, realQty);
		//更新容器明细信息
		UpdateWrapper<AsnLpnDetail> asnLpnDetailUpdateWrapper = new UpdateWrapper<>();
		asnLpnDetailUpdateWrapper.lambda().set(AsnLpnDetail::getLpnStatus, "2")
			.eq(AsnLpnDetail::getAsnBillId, asnHeader.getAsnBillId())
			.in(AsnLpnDetail::getAsnDetailId, NodesUtil.toList(asnDetailList, AsnDetail::getAsnDetailId))
			.eq(AsnLpnDetail::getLpnStatus, StringPool.ONE);
		asnLpnDetailMapper.update(null, asnLpnDetailUpdateWrapper);

		// 计划接收数量
		BigDecimal planQty;

		//开始给每行明细表单更新状态
		for (AsnDetail asnDetail : asnDetailList) {
			if (BigDecimalUtil.le(realQty, BigDecimal.ZERO)) {
				continue;
			}
			//是否是序列号管理 0:不是,1:是
			planQty = asnDetail.getPlanQty();
			if (BigDecimalUtil.lt(planQty, realQty)) {
				continue;
			}
			//如果时序列号管理则更新序列号状态
			if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
				this.updateSn(dto);
			}
			//规格 清点记录用
			dto.setSkuSpec(asnDetail.getSkuSpec());
			//计划数量 清点记录用
			if (BigDecimalUtil.lt(planQty, realQty)) {
				asnDetail.setSurplusQty(BigDecimal.ZERO);
			} else {
				asnDetail.setSurplusQty(planQty.subtract(realQty).subtract(asnDetail.getScanQty()));
			}
			//保存增加的库存信息
			this.saveAddStockInfo(dto, asnInventory, asnDetail, sku, realQty, systemProcId);
			// 更新明细数量、状态、自动生成的批属性
			UpdateWrapper<AsnDetail> updateWrapper = new UpdateWrapper<>();
			BigDecimal totalScanQty = asnDetail.getScanQty().add(realQty);
			updateWrapper.lambda()
				.eq(AsnDetail::getAsnDetailId, asnDetail.getAsnDetailId())
				.set(AsnDetail::getDetailStatus, AsnDetailStatusEnum.RECEIVED.getIndex())
				.set(AsnDetail::getScanQty, totalScanQty)
				.set(AsnDetail::getSurplusQty, planQty.subtract(totalScanQty));
			asnDetailService.update(null, updateWrapper);
			// 更新PO明细
			asnDetail.setScanQty(totalScanQty);
			asnDetail.setSurplusQty(planQty.subtract(totalScanQty));
			poDetailService.updateQty(asnDetail);
		}
		// 获取未收完的明细数量
		Integer unReceiveCount = (int) asnDetailListAll.stream().filter(u -> {
			return BigDecimalUtil.gt(u.getSurplusQty(), BigDecimal.ZERO);
		}).count();

		if (unReceiveCount != 0) {
			this.updateAsnBillState(dto.getAsnBillId(), AsnBillStateEnum.PART);
		} else {
			this.updateAsnBillState(dto.getAsnBillId(), AsnBillStateEnum.COMPLETED);
//			taskService.closeTask(dto.getAsnBillId(), TaskTypeEnum.Check);
//			if (!isAutoPutaway) {
//				TaskDTO taskDTO = new TaskDTO();
//				taskDTO.setBillId(asnHeader.getAsnBillId());
//				taskDTO.setBillTypeCd(asnHeader.getBillTypeCd());
//				taskDTO.setWhId(dto.getWhId());
//				taskDTO.setTaskType(TaskTypeEnum.Putaway.getIndex());
//				taskDTO.setTaskPackage(null);
//				taskService.create(taskDTO);
//			}
		}
		AsnDetailMinVO minVO = new AsnDetailMinVO();
		minVO.setCount(asnDetailListAll.size());
		minVO.setFinish(asnDetailListAll.size() - unReceiveCount);

		SubmitAsnHeaderVO submitAsnHeaderVO = new SubmitAsnHeaderVO();
		submitAsnHeaderVO.setAsnDetail(minVO);
		submitAsnHeaderVO.setIsAutoPutaway(!StringPool.ONE.equals(ParamCache.getValue(ParamEnum.TASK_PUTAWAY_MODE.getKey())));
		return submitAsnHeaderVO;
	}

	/**
	 * 按件收货 增加的库存信息
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @param systemProcId
	 * @return
	 */
	private Stock saveAddStockInfo(AsnDTO dto, AsnInventory asnInventory, AsnDetail asnDetail, Sku sku, BigDecimal count, Long systemProcId) {
		//1.查询当前容器上的课移动物品
//		List<Stock> stocks = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
////			.eq(Stock::getLpnCode, dto.getLpnCode())
//			.apply("stock_qty - pick_qty > 0"));
		List<StockDetail> stockDetailList = null;
		if (Func.isEmpty(dto.getLpnCode()) && Func.isEmpty(dto.getBoxCode())) {
		} else {
			stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
				.lambda()
				.func(sql -> {
					if (Func.isNotEmpty(dto.getLpnCode()) && Func.isNotEmpty(dto.getBoxCode())) {
						sql.eq(StockDetail::getLpnCode, dto.getLpnCode()).
							eq(StockDetail::getBoxCode, dto.getBoxCode());
					} else if (Func.isNotEmpty(dto.getLpnCode())) {
						sql.eq(StockDetail::getLpnCode, dto.getLpnCode());
					} else if (Func.isNotEmpty(dto.getBoxCode())) {
						sql.eq(StockDetail::getBoxCode, dto.getBoxCode());
					}
				})
				.apply("stock_qty - pick_qty > 0"));
		}

		if (Func.isNotEmpty(stockDetailList)) {
			if (!stockDetailList.get(0).getLocId().equals(dto.getLocId())) {
				//按托移动后再入库
				StockMoveDTO stockMoveDTO = new StockMoveDTO();

				stockMoveDTO.setSourceLpnCode(dto.getLpnCode());
				stockMoveDTO.setSourceLocId(stockDetailList.get(0).getLocId()); //原库位id 从数据库中查询
				stockMoveDTO.setTargetLocId(dto.getLocId()); //目标库位id 通过传递的locCode查询locId
				stockMoveDTO.setSystemProcId(systemProcId);
				stockMoveDTO.setEventType(EventTypeEnum.Move);
				stockService.stockMoveByLpn(stockMoveDTO);
			}
		}

		StockAddDTO stockProc = BeanUtil.copy(dto, StockAddDTO.class);
		stockProc.setSystemProcId(systemProcId);//日志id
		stockProc.setLocId(dto.getLocId());//库位id
		stockProc.setSkuId(dto.getSkuId());//物品id
		stockProc.setWspId(dto.getWspId());//包装id
		stockProc.setStockQty(count);//数量
		stockProc.setEventType(EventTypeEnum.Check);//事务类型
		stockProc.setLpnCode(dto.getLpnCode());//容器编码j
		stockProc.setBillId(asnDetail.getAsnBillId());//收货单id
		stockProc.setBillDetailId(asnDetail.getAsnDetailId());//收货单明细id
		stockProc.setBillNo(asnDetail.getAsnBillNo());//收货单编码

		String[] snCode = dto.getSnDetailCode().split(",");
		List<String> serilList = new ArrayList<>();
		for (String s : snCode) {
			if (Func.isNotEmpty(s)) {
				serilList.add(s);
			}
		}
		stockProc.setSerialList(serilList);
		SkuLotUtil.fill(sku, stockProc, asnDetail, dto);

		Stock stock = stockService.add(stockProc);
		// 保存物品操作记录
		SkuLogDTO skuLogDTO = new SkuLogDTO();
		skuLogDTO.setSkuId(dto.getSkuId());
		skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.INSTOCK);
		this.skuLogService.saveOrUpdate(skuLogDTO);
		//清点记录
		this.saveContainerLog(dto, asnInventory, asnDetail, count, stock);

		return stock;
	}

	/**
	 * 按件收货 保存清点记录
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @return
	 */
	private boolean saveContainerLog(AsnDTO dto, AsnInventory asnInventory, AsnDetail asnDetail, BigDecimal count, Stock stock) {
		//插入清点记录
		ContainerLog containerLog = new ContainerLog();
		containerLog.setAsnBillId(asnDetail.getAsnBillId()); //订单id
		containerLog.setAsnBillNo(dto.getAsnBillNo()); //单据编码
		if (Func.isNotEmpty(asnInventory)) {
			containerLog.setInventoryId(asnInventory.getId());
		}
		containerLog.setAsnDetailId(asnDetail.getAsnDetailId()); //订单明细id
		containerLog.setLpnCode(dto.getLpnCode()); //容器编码
		containerLog.setLocId(dto.getLocId());//库位id
		containerLog.setLocCode(dto.getLocCode());//库位编码
		containerLog.setAclQty(count); //数量
		containerLog.setSkuId(asnDetail.getSkuId());//物品id
		containerLog.setSkuCode(asnDetail.getSkuCode());
		containerLog.setSkuName(asnDetail.getSkuName()); //物品名称
		containerLog.setWspId(asnDetail.getWspId());
		containerLog.setSkuLevel(asnDetail.getSkuLevel()); //包装层级
		containerLog.setAclStatus(1); //清点状态
		containerLog.setSkuSpec(dto.getSkuSpec()); //规格
		containerLog.setPlanQty(asnDetail.getPlanQty()); //计划数量
		containerLog.setLotNumber(stock.getLotNumber()); //批次号
		containerLog.setSnDetailCode(dto.getSnDetailCode());
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (!dto.skuLotChk(i)) {
				continue;
			}
			containerLog.skuLotSet(i, dto.skuLotGet(i));
		}
		containerLogService.save(containerLog);
		return true;
	}

	/**
	 * 按件收货 保存系统日志
	 *
	 * @param dto
	 * @param count
	 */
	private Long saveSystemProcLog(AsnDTO dto, BigDecimal count) {
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.ASN);
		systemProcParam.setDataType(DataTypeEnum.AsnBillNo);
		systemProcParam.setAction(ActionEnum.RECEIVE_BASE);
		systemProcParam.setBillNo(dto.getAsnBillNo());
		systemProcParam.setLpnCode(dto.getLpnCode());
		systemProcParam.setOperationQty(count);
		systemProcParam.setOperationUnit(SkuPackageDetailCache.getById(dto.getWspdId()).getWsuName());
		systemProcParam.setWhId(dto.getWhId());

		return systemProcService.create(systemProcParam).getSystemProcId();
	}

	/**
	 * 按件收货 序列号物品收货候更新序列号
	 *
	 * @param dto
	 * @return
	 */
	private boolean updateSn(AsnDTO dto) {
		String[] snCodes = dto.getSnDetailCode().split(",");
		for (String code : snCodes) {
			Sn param = new Sn();
			param.setSnDetailCode(code);
			param.setAsnBillId(dto.getAsnBillId());
			Sn sn = snService.getOne(Condition.getQueryWrapper(param));

			AsnDetail asnDetail = asnDetailService.getOne(Condition.getQueryWrapper(new AsnDetail())
				.lambda().eq(AsnDetail::getAsnDetailId, dto.getAsnDetailId()));

			if (Func.isNotEmpty(sn)) {
				if ("Y".equals(asnDetail.getImportSn())) {
					if (sn.getAsnBillId().equals(asnDetail.getAsnBillId())) {
						if ("2".equals(sn.getSnStatus())) {
							throw new ServiceException(String.format("序列号[%s]已清点", code));
						}
						//更新序列号
						sn.setSnStatus("2");
						snService.updateById(sn);
					} else {
						throw new ServiceException(String.format("序列号[%s]不属于物品[%s]", code, asnDetail.getSkuName()));
					}
				} else {
					throw new ServiceException(String.format("序列号[%s]已导入其他单据", code, asnDetail.getAsnBillNo()));
				}
			} else {
				if ("Y".equals(asnDetail.getImportSn())) {
					throw new ServiceException(String.format("序列号[%s]不属于物品[%s]", code, asnDetail.getSkuName()));
				} else {
					//新增序列号
					Sn snParam = new Sn();
					snParam.setAsnDetailId(dto.getAsnDetailId());
					snParam.setAsnBillId(dto.getAsnBillId());
					snParam.setSnDetailCode(code);
					snParam.setSnStatus("2");
					snService.save(snParam);
				}
			}
		}
		return true;
	}

	/**
	 * 整托上架 提交上架信息
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	@Override
	public synchronized boolean submitPutaway(InStockSubmitVO inStockSubmitVO) {
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		if (Func.isEmpty(inStockSubmitVO.getWhId())) {
			throw new ServiceException("库房ID不能为空");
		}
		if (Func.isEmpty(inStockSubmitVO.getLpnCode())) {
			throw new ServiceException("请先扫描容器");
		}
		if (Func.isEmpty(inStockSubmitVO.getLocCode())) {
			throw new ServiceException("目标库位不能为空");
		}
		if (ZoneVirtualTypeEnum.isVirtual(inStockSubmitVO.getLocCode())) {
			throw new ServiceException("目标库位不能是暂存库位！");
		}
		Location location = LocationCache.getValid(inStockSubmitVO.getLocCode());
		if (!location.getWhId().equals(inStockSubmitVO.getWhId())) {
			throw new ServiceException(String.format(
				"扫描库位[%s] 不属于容器[%s]所在库房！", inStockSubmitVO.getLocCode(), inStockSubmitVO.getLpnCode()));
		}
		inStockSubmitVO.setTargetLocId(location.getLocId());
		if (!StringPool.UNKNOWN.equals(inStockSubmitVO.getZoneCode())) {//库区不等于unknown 需要校验库位是否属于该库区

			Zone zone = zoneService.getById(location.getZoneId());
			if (Func.isEmpty(zone)) {
				throw new ServiceException(String.format("库区[%s]不存在或者已删除", location.getZoneId()));
			}
			if (!inStockSubmitVO.getZoneCode().contains(zone.getZoneCode())) {
				throw new ServiceException(String.format("目标库位[%s]不属于[%s]库区", inStockSubmitVO.getLocCode(), zoneService.getById(location.getZoneId()).getZoneName()));
			}
		}
		List<Zone> zones = zoneService.list(Wrappers.lambdaQuery(Zone.class).eq(Zone::getZoneType, 73));
		//查询出来库存中在当前容器里的所有状态正常的物品
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.in(StockDetail::getZoneId, NodesUtil.toList(zones, Zone::getZoneId))
			.eq(StockDetail::getLpnCode, inStockSubmitVO.getLpnCode())
			.apply("stock_qty > pick_qty"));
		if (Func.isEmpty(stockDetailList)) {
			throw new ServiceException(String.format("容器[%s]上没有可上架物品", inStockSubmitVO.getLpnCode()));
		}
//		List<Stock> list = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
//			.in(Stock::getStockId, NodesUtil.toList(stockDetailList, StockDetail::getStockId)));
//
//		if (Func.isEmpty(list)) {
//			throw new ServiceException(String.format("容器[%s]上没有可上架物品", inStockSubmitVO.getLpnCode()));
//		}

		for (StockDetail stockDetail : stockDetailList) {
			//记录系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.ASN);
			systemProcParam.setAction(ActionEnum.PUTAWAY_LPN);
			systemProcParam.setLpnCode(stockDetail.getLpnCode());
			systemProcParam.setWhId(inStockSubmitVO.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);

			StockMoveDTO stockMoveDTO = new StockMoveDTO();
			stockMoveDTO.setSourceLpnCode(stockDetail.getLpnCode());
			stockMoveDTO.setSourceLocId(stockDetail.getLocId());
			stockMoveDTO.setSdId(stockDetail.getId());
			stockMoveDTO.setTargetLocId(location.getLocId());
			stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
			stockMoveDTO.setEventType(EventTypeEnum.Putaway);
			stockService.stockMoveByLpn(stockMoveDTO);


			this.saveTransferRecord(inStockSubmitVO, stockDetail);
		}

		/*//原库位id
		Long oldLocId = list.get(0).getLocId();
		inStockSubmitVO.setSourceLocId(oldLocId);
		Location oldLocation = LocationCache.getValid(list.get(0).getLocCode());
		String oldLocCode = oldLocation.getLocCode();

		if (!ZoneVirtualTypeEnum.isVirtual(oldLocCode)) {
			throw new ServiceException(String.format("容器[%s]已上架", inStockSubmitVO.getLpnCode()));
		}

		//查询目标库位信息
		Location location = LocationCache.getValid(inStockSubmitVO.getLocCode());
		if (!location.getWhId().equals(inStockSubmitVO.getWhId())) {
			throw new ServiceException(String.format(
				"扫描库位[%s] 不属于容器[%s]所在库房！", inStockSubmitVO.getLocCode(), inStockSubmitVO.getLpnCode()));
		}
		inStockSubmitVO.setTargetLocId(location.getLocId());
		if (!StringPool.UNKNOWN.equals(inStockSubmitVO.getZoneCode())) {//库区不等于unknown 需要校验库位是否属于该库区

			Zone zone = zoneService.getById(location.getZoneId());
			if (Func.isEmpty(zone)) {
				throw new ServiceException(String.format("库区[%s]不存在或者已删除", location.getZoneId()));
			}
			if (!inStockSubmitVO.getZoneCode().contains(zone.getZoneCode())) {
				throw new ServiceException(String.format("目标库位[%s]不属于[%s]库区", inStockSubmitVO.getLocCode(), zoneService.getById(location.getZoneId()).getZoneName()));
			}
		}


		//记录系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.ASN);
		systemProcParam.setAction(ActionEnum.PUTAWAY_LPN);
		systemProcParam.setLpnCode(inStockSubmitVO.getLpnCode());
		systemProcParam.setWhId(inStockSubmitVO.getWhId());
		SystemProc systemProc = systemProcService.create(systemProcParam);

//		LpnPutWayMoveServiceImpl.LpnPutWayMoveParam param = new LpnPutWayMoveServiceImpl.LpnPutWayMoveParam();
//		param.setSourceLpnCode(inStockSubmitVO.getLpnCode());
//		param.setSourceLocId(oldLocId); //原库位id 从数据库中查询
//		param.setTargetLocId(location.getLocId()); //目标库位id 通过传递的locCode查询locId
//		param.setSystemProcId(systemProc.getSystemProcId());
//		param.setEventType(EventTypeEnum.Putaway);
//		stockMoveService.move(param);

		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setSourceLpnCode(inStockSubmitVO.getLpnCode());
		stockMoveDTO.setSourceLocId(oldLocId);
		stockMoveDTO.setTargetLocId(location.getLocId());
		stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
		stockMoveDTO.setEventType(EventTypeEnum.Putaway);
		stockService.stockMoveByLpn(stockMoveDTO);

		this.saveTransferRecord(inStockSubmitVO, list);*/

		return true;
	}

	/**
	 * 整托上架 保存移动记录
	 *
	 * @return
	 */
	private boolean saveTransferRecord(InStockSubmitVO inStockSubmitVO, StockDetail stockDetail) {
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
//		List<StockDetail> stockDetailList = new ArrayList<>();
//		if (Func.isNotEmpty(stocks)) {
//			stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail()).lambda()
//				.in(StockDetail::getStockId, NodesUtil.toList(stocks, Stock::getStockId)));
//		}
//		Stock stock = stockService.getById(stockDetail.getStockId());
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
//		for (Stock stock : stocks) {
		TransferRecord transferRecord = new TransferRecord();
		transferRecord.setTransferType(TransferTypeEnum.PUTAWAY.getIndex());
//			transferRecord.setWoId(stock.getWoId());
		transferRecord.setSkuId(stockDetail.getSkuId()); //原货品
//			transferRecord.setFromLpn(stock.getLpnCode()); // 源容器
		Location sourceLocation = LocationCache.getById(stockDetail.getLocId());
		transferRecord.setFromLocCode(sourceLocation.getLocCode());//原库位编码
		Zone sourceZone = zoneService.getById(sourceLocation.getZoneId());
		transferRecord.setFromZoneCode(sourceZone.getZoneCode());// 原库区编码
		transferRecord.setFromZoneName(sourceZone.getZoneName());// 原库区名称
		transferRecord.setQty(stockDetail.getStockQty()); // 原数量
		transferRecord.setWspId(stockDetail.getWspId()); //原包装Id
		transferRecord.setSkuLevel(1);//原层级
		Location targetLocation = LocationCache.getById(inStockSubmitVO.getTargetLocId());
		transferRecord.setFromLocId(inStockSubmitVO.getSourceLocId());
		transferRecord.setToLocId(targetLocation.getLocId());
		//包装信息
//			if (Func.isNotEmpty(stockDetail.getWspId())) {
//				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(stockDetail.getWspId(), stock.getSkuLevel());
//				if (Func.isNotEmpty(skuPackageDetail)) {
//					transferRecord.setWsuCode(skuPackageDetail.getWsuCode());
//					transferRecord.setWsuName(skuPackageDetail.getWsuName());
//				}
//			}
		transferRecord.setToLocCode(targetLocation.getLocCode());//目标库位编码
		Zone targetZone = zoneService.getById(sourceLocation.getZoneId());
		transferRecord.setToZoneCode(targetZone.getZoneCode());//目标库区编码
		transferRecord.setToZoneName(targetZone.getZoneName());//目标库区名称
		transferRecord.setLotNumber(stockDetail.getLotNumber());//批次号

//			StockDetail stockDetail = stockDetailList.stream().filter(u -> {
//				return Func.equals(u.getStockId(), stock.getStockId());
//			}).findFirst().orElse(null);
//			if (Func.isNotEmpty(stockDetail)) {
//				AsnDetail asnDetail = asnDetailMapper.selectById(stockDetail.getBillDetailId());
//				if (Func.isNotEmpty(asnDetail)) {
//					transferRecord.setTransferCode(stock.get());
//				}
//			}

//			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
//				if (stock.skuLotChk(i)) {
//					transferRecord.skuLotSet(i, stock.skuLotGet(i));
//				}
//			}
		transferRecordService.save(transferRecord);
//		}
		return true;
	}

	/**
	 * 按件收货 序列号是否属于收货单
	 *
	 * @param dto
	 * @return
	 */
	@Override
	public boolean instockHasSerial(AsnDTO dto) {
		if (Func.isEmpty(dto.getAsnDetailId())) {
			throw new ServiceException("单据明细ID不能为空");
		}
		if (Func.isEmpty(dto.getSnDetailCode())) {
			throw new ServiceException("序列号不能为空");
		}

		//查询单据明细
		AsnDetail asnDetail = asnDetailService.getOne(Condition.getQueryWrapper(new AsnDetail())
			.lambda().eq(AsnDetail::getAsnDetailId, dto.getAsnDetailId()));
		if (Func.isEmpty(asnDetail)) {
			throw new ServiceException("收货单不存在或者已删除");
		}

		Sn param = new Sn();
		param.setSnDetailCode(dto.getSnDetailCode());
		param.setAsnBillId(asnDetail.getAsnBillId());
		List<Sn> sns = new ArrayList<>();

		if ("Y".equals(asnDetail.getImportSn())) {//已导入则验证序列号是否该订单的
			param.setSnStatus(StringPool.ONE);
			sns = snService.list(Condition.getQueryWrapper(param));
			if (sns.size() == 0) {
				throw new ServiceException("序列号未导入或已收");
			}
		} else {//未导入则验证序列号是否其他订单的
			sns = snService.list(Condition.getQueryWrapper(param));
			if (sns.size() > 0) {
				throw new ServiceException("序列号已导入其他明细或已收");
			}
		}
		return true;
	}

	/**
	 * 收货-获得物品列表
	 *
	 * @param skuCode
	 * @return
	 */
	@Override
	public List<AsnSkuVO> getSkuListForInstock(String asnBillNo, String skuCode) {
		List<AsnSkuVO> skuList = new ArrayList<>();
		List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail()).lambda()
			.eq(AsnDetail::getAsnBillNo, asnBillNo)
			.eq(AsnDetail::getSkuCode, skuCode).groupBy(AsnDetail::getWspId));
		if (Func.isNotEmpty(asnDetailList)) {
			for (AsnDetail asnDetail : asnDetailList) {
				AsnSkuVO sku = new AsnSkuVO();
				sku.setSkuId(asnDetail.getSkuId());
				sku.setSkuName(asnDetail.getSkuName());
				sku.setSkuCode(asnDetail.getSkuCode());
				sku.setWspId(asnDetail.getWspId());
				SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
				if (Func.isNotEmpty(skuPackage)) {
					sku.setSkuSpec(skuPackage.getSpec());
					sku.setWspName(skuPackage.getWspName());
				} else {
					throw new ServiceException("入库单中的包装不存在或者已删除");
				}
				skuList.add(sku);
			}
		} else {
			throw new ServiceException("入库单没有该物品");
		}
		return skuList;
	}

	/**
	 * 按件收货 获得详情
	 *
	 * @param asnBillNo
	 * @param skuCode
	 * @return
	 */
	@Override
	public Map<String, Object> getAsnHeaderDetail(String asnBillNo, String skuCode/*, String wspId*/) {
		Map<String, Object> result = new HashMap<>();

		//查询单据明细列表
		AsnDetail paramDetail = new AsnDetail();
		paramDetail.setSkuCode(UrlUtil.decode(skuCode, StringPool.UTF_8));

		paramDetail.setAsnBillNo(asnBillNo);
		List<AsnDetail> asnDetails = asnDetailService.list(Condition.getQueryWrapper(paramDetail)
			.lambda().ne(AsnDetail::getSurplusQty, 0).orderByAsc(AsnDetail::getAsnLineNo));

		//用来计算总数的
		List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(paramDetail)
			.lambda().orderByAsc(AsnDetail::getAsnLineNo));

		//判断有无明细
		if (0 >= asnDetails.size()) {
			result.put("msg", String.format("当前收货单没有物品[%s],或者物品已收满", skuCode));
			result.put("success", false);
			return result;
		}

		//获得物品
		Sku sku = SkuCache.getById(asnDetails.get(0).getSkuId());
		if (Func.isEmpty(sku)) {
			result.put("msg", String.format("数据库没有物品[%s]的记录", asnDetails.get(0).getSkuId()));
			result.put("success", false);
			return result;
		}
//			throw new ServiceException(String.format("数据库没有物品[%s]的记录",asnDetails.get(0).getSkuId()));

		//计算明细表实际数量与计划数量
		AsnDetailMinVO minVO = this.getMinVoByBillId(asnDetailList);

		//包装头表
		SkuPackage skuPackage = SkuPackageCache.getById(asnDetails.get(0).getWspId());
		if (Func.isEmpty(skuPackage)) {
			throw new ServiceException("包装不存在或者已删除");
		}
		//包装明细列表
		List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
		if (skuPackageDetails.size() <= 0) {
			result.put("msg", String.format("包装[%s]没有包装明细", skuPackage.getWspId()));
			result.put("success", false);
			return result;
		}
//			throw new ServiceException(String.format("包装[%s]没有包装明细", skuPackage.getWspId()));


		//批属性规则
		List<SkuLotConfigVO> skuConfigs = new ArrayList<>();
		skuConfigs = skuLotService.getConfig(asnDetails.get(0).getSkuId());
		if (skuConfigs.size() <= 0) {
			result.put("msg", String.format("物品[%s]没有批属性规则", asnDetails.get(0).getSkuId()));
			result.put("success", false);
			return result;
		}
//			throw new ServiceException(String.format("物品[%s]没有批属性规则", asnDetails.get(0).getSkuId()));


		result.put("success", true);
		result.put("sku", sku);
		result.put("asnDetails", asnDetails);
		result.put("skuPackage", skuPackage);
		result.put("skuPackageDetails", skuPackageDetails);
		result.put("asnDetail", minVO);
		result.put("skuConfig", skuConfigs);

		return result;
	}

	/**
	 * 按件收货 通过AsnBillId获得AsnDetailMinVO
	 *
	 * @param list
	 * @return
	 */
	AsnDetailMinVO getMinVoByBillId(List<AsnDetail> list) {
		//计算计划数量与实际数量
		BigDecimal planCount = BigDecimal.ZERO;
		BigDecimal scanCount = BigDecimal.ZERO;
		int skuLevel = 0;
		AsnDetailMinVO minVO = new AsnDetailMinVO();
		int skuIsSn = 0;
		Long wspId = 0L;
		for (AsnDetail asnDetail : list) {
			Sku sku = SkuCache.getById(asnDetail.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException(String.format("物品[%s]不存在或者已删除", asnDetail.getSkuId()));
			}
			skuIsSn = sku.getIsSn();
			wspId = asnDetail.getWspId();
			planCount = planCount.add(asnDetail.getPlanQty());
			scanCount = scanCount.add(asnDetail.getScanQty());

			if (1 == skuIsSn) {
				skuLevel = 1;
			} else {
				if (asnDetail.getSkuLevel() > skuLevel) {
					//最大层级
					skuLevel = asnDetail.getSkuLevel();
				}
			}
		}
		minVO.setPlanCountQty(planCount);
		minVO.setScanCountQty(scanCount);

		if (1 == skuIsSn) {
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(wspId);

		}

		minVO.setPlanQtyName(skuUmService.convert(list.get(0).getWspId(), skuLevel, planCount));
		minVO.setScanQtyName(skuUmService.convert(list.get(0).getWspId(), skuLevel, scanCount));

		//规格
		Collections.sort(list, Comparator.comparing(AsnDetail::getSkuLevel));

		List<Integer> temp = new ArrayList<>();
		List<AsnDetail> noList = list.stream().filter(// 过滤去重
			v -> {
				boolean flag = !temp.contains(v.getSkuLevel());
				temp.add(v.getSkuLevel());
				return flag;
			}
		).collect(Collectors.toList());

		String spec = "1-";
		for (AsnDetail asnDetail : noList) {
			if (1 == asnDetail.getSkuLevel() && list.size() == 1) {
				spec += "1-";
				break;
			}
			if (1 != asnDetail.getSkuLevel())
				spec += asnDetail.getConvertQty() + "-";

		}
		if (1 == skuIsSn) {
			minVO.setSkuSpec("1-1");
		} else {
			minVO.setSkuSpec(spec.substring(0, spec.length() - 1));
		}

		//接收状态
		minVO.setDetailStatusName(DictCache.getValue("detail_status", list.get(list.size() - 1).getDetailStatus()));
		//物品名称
		minVO.setSkuName(list.get(list.size() - 1).getSkuName());

		return minVO;
	}

	/**
	 * 按件收货 收货单列表展示 完成的明细表
	 *
	 * @param asnBillId
	 * @return
	 */
	@Override
	public AsnDetailMinVO getFinishAsnDetail(Long asnBillId) {
		// 获取任务明细
		List<AsnDetail> list = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
			.lambda()
			.eq(AsnDetail::getAsnBillId, asnBillId));
		int count = list.size();
		int finish = (int) list.stream().filter(u -> {
			return BigDecimalUtil.eq(u.getSurplusQty(), BigDecimal.ZERO);
		}).count();

		AsnDetailMinVO vo = new AsnDetailMinVO();
		vo.setFinish(finish);
		vo.setCount(count);

		return vo;
	}

	@Override
	public boolean cancel(List<Long> asnBillIdList) {
		for (Long asnBillId : asnBillIdList) {
			AsnHeader asnHeader = super.getById(asnBillId);
			if (Func.isEmpty(asnHeader)) {
				throw new ServiceException("指定入库单不存在(ID:" + asnBillId + ")");
			}
			// 记录系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.ASN);
			systemProcParam.setDataType(DataTypeEnum.AsnBillNo);
			systemProcParam.setAction(ActionEnum.CANCEL);
			systemProcParam.setBillNo(asnHeader.getAsnBillNo());
			systemProcParam.setWhId(asnHeader.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);
			if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.NOT_RECEIPT.getCode())) {

			} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.PART.getCode())) {
				// 清空该订单已收货的库存
				List<ContainerLog> containerLogList = containerLogService.list(
					Condition.getQueryWrapper(new ContainerLog())
						.lambda()
						.eq(ContainerLog::getAsnBillId, asnBillId));
				// 循环收货记录， 处理库存
				for (ContainerLog containerLog : containerLogList) {
					StockSubtractDTO stockReduce = new StockSubtractDTO();
					stockReduce.setLocId(containerLog.getLocId());
					stockReduce.setLocCode(containerLog.getLocCode());
					stockReduce.setLpnCode(containerLog.getLpnCode());
					stockReduce.setBoxCode(containerLog.getBoxCode());
					stockReduce.setSkuId(containerLog.getSkuId());
					stockReduce.setSystemProcId(systemProc.getSystemProcId());
					stockReduce.setLotNumber(containerLog.getLotNumber());
					stockReduce.setBillDetailId(containerLog.getAsnDetailId());
					stockReduce.setPickQty(containerLog.getAclQty());
					if (Func.isNotEmpty(containerLog.getSnDetailCode())) {
						stockReduce.setSerialList(Func.toStrList(containerLog.getSnDetailCode()));
					}
					stockReduce.setEventType(EventTypeEnum.InstockCancel);
					stockService.subtract(stockReduce);
				}
			} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.COMPLETED.getCode())) {
				throw new ServiceException(String.format("订单：%s 已收货完成，拒绝执行取消操作！", asnHeader.getAsnBillNo()));
			} else {
				String asnBillState = DictCache.getValue(DictConstant.ASN_BILL_STATE, asnHeader.getAsnBillState());
				throw new ServiceException(String.format(
					"订单：%s 当前状态[%s] 不允许执行取消操作！", asnHeader.getAsnBillNo(), asnBillState));
			}
			// 关闭入库单的任务
			taskService.closeTask(asnBillId, TaskTypeEnum.ALL);
			// 更改订单状态
			this.updateAsnBillState(asnBillId, AsnBillStateEnum.CANCEL);
		}
		return true;
	}

	/**
	 * 修改入库单状态
	 *
	 * @param asnBillId
	 * @param asnBillState
	 * @return
	 */
	@Override
	public synchronized AsnHeader updateAsnBillState(Long asnBillId, AsnBillStateEnum asnBillState) {
		AsnHeader asnHeader = super.getById(asnBillId);
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("订单ID：" + asnBillId + " 不存在！");
		}
		// 当订单状态已经 = 已取消  则不允许再进行任何修改订单状态操作
		if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("订单：" + asnHeader.getAsnBillNo() + " 已取消，拒绝当前操作！");
		}
		if (asnBillState.equals(AsnBillStateEnum.PART)) {
			// 修改实际到货时间
			if (Func.isEmpty(asnHeader.getActualArrivalDate())) {
				asnHeader.setActualArrivalDate(LocalDateTime.now());
			}
		} else if (asnBillState.equals(AsnBillStateEnum.COMPLETED)) {
			// 修改单据完成时间
			asnHeader.setFinishDate(LocalDateTime.now());
		}
		asnHeader.setAsnBillState(asnBillState.getCode());
		// 修改订单主表订单状态
		super.updateById(asnHeader);
		IAllotHeaderService allotHeaderService = SpringUtil.getBean(IAllotHeaderService.class);
//		if (AsnBillStateEnum.COMPLETED.equals(asnBillState)) {
//			// 更新调拨单状态
//			allotHeaderService.updateBillState(asnHeader.getOrderNo(), AllotBillStateEnum.COMPLETED);
//		} else if (AsnBillStateEnum.PART.equals(asnBillState)) {
//			allotHeaderService.updateBillState(asnHeader.getOrderNo(), AllotBillStateEnum.INSTOCKING);
//		}
		return asnHeader;
	}

	@Override
	public List<DataVerify> validSnExcel(List<SnExcel> dataList) {

		List<DataVerify> dataVerifyList = new ArrayList<>();

		if (Func.isNotEmpty(dataList)) {
			Map<String, String> map = getBoxCodesByGroupCode(dataList);
			int index = 1;
			for (SnExcel snExcel : dataList) {
				DataVerify dataVerify = new DataVerify();
				dataVerify.setIndex(index);
				index++;

				SnVO snVO = new SnVO();
				ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(snVO, Excel.class);

				AsnHeader asnHeader = super.getOne(Condition.getQueryWrapper(new AsnHeader()).lambda()
					.eq(AsnHeader::getAsnBillNo, snExcel.getAsnBillNo()));
				// 判断入库单是否存在
				if (Func.isNotEmpty(asnHeader)) {
					snVO.setAsnBillNo(snExcel.getAsnBillNo());
				} else {
					validResult.addError("asnBillNo", "入库单[" + snExcel.getAsnBillNo() + "]不存在");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				}
				// 判断入库单是否已经进行了
				if (!asnHeader.getAsnBillState().equals(AsnBillStateEnum.NOT_RECEIPT.getCode())) {
					validResult.addError("asnBillNo", "当前入库单[" + snExcel.getAsnBillNo() + "] 已经进行或完成，不允许导入序列号");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				}

				List<Sn> snList = snService.list(Condition.getQueryWrapper(new Sn()).lambda()
					.eq(Sn::getSnDetailCode, snExcel.getSnDetailCode())
					.eq(Sn::getAsnBillId, asnHeader.getAsnBillId()));
				// 判断序列号是否存在
				if (Func.isNotEmpty(snList) && snList.size() > 0) {
					validResult.addError("snDetailCode", "序列号[" + snExcel.getSnDetailCode() + "]已存在");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				} else {
					snVO.setSnDetailCode(snExcel.getSnDetailCode());
					dataVerify.setCacheKey(snExcel.getSnDetailCode());
				}

				Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, String> next = iterator.next();
					if (Func.isNotEmpty(next.getKey()) && snExcel.getGroupNum().equals(next.getKey())) {
						snVO.setBoxCode(next.getValue());
					}
				}

				AsnDetail asnDetail = asnDetailService.getOne(Condition.getQueryWrapper(new AsnDetail()).lambda()
					.eq(AsnDetail::getAsnBillNo, snExcel.getAsnBillNo())
					.eq(AsnDetail::getAsnLineNo, snExcel.getAsnLineNo()));

				if (Func.isEmpty(asnDetail)) {
					validResult.addError("asnBillNo", "入库单[" + snExcel.getAsnBillNo() + "]缺少行号[" + snExcel.getAsnLineNo() + "]");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				}

				// 验证序列号是否存在
				int existCount = snService.count(Condition.getQueryWrapper(new Sn())
					.lambda().eq(Sn::getSnDetailCode, snVO.getSnDetailCode()));
				if (existCount > 0) {
					throw new ServiceException("序列号:" + snVO.getSnDetailCode() + " 已存在！");
				}
				// 验证序列号数量是否与明细计划收货数量一致
				Long snCount = dataList.stream().filter(u -> {
					return asnDetail.getAsnLineNo().equals(u.getAsnLineNo());
				}).count();
				// 加上该明细已导入的序列号总数
				snCount += snService.count(Condition.getQueryWrapper(new Sn()).lambda()
					.eq(Sn::getAsnDetailId, asnDetail.getAsnDetailId()));
				if (snCount != asnDetail.getPlanQty().longValue()) {
					throw new ServiceException("序列号数量必须与明细[行号:" + asnDetail.getAsnLineNo() + "]计划收货数量保持一致！");
				}
				snVO.setAsnLineNo(snExcel.getAsnLineNo());

				Sku sku = SkuCache.getById(asnDetail.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("物品不存在或者已删除");
				}
				if (1 != sku.getIsSn()) {
					validResult.addError("skuName", "物品[" + sku.getSkuName() + "]是非序列号管理");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				}
				dataVerify.setObj(snVO);
				dataVerifyList.add(dataVerify);

			}
		}

		return dataVerifyList;
	}

	@Override
	public boolean importSnData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerifyDTO : dataVerifyList) {
			ObjectMapper objectMapper = new ObjectMapper();
			SnVO snVO = objectMapper.convertValue(dataVerifyDTO.getObj(), SnVO.class);
			AsnDetail param = new AsnDetail();
			param.setAsnBillNo(snVO.getAsnBillNo());
			param.setAsnLineNo(snVO.getAsnLineNo());
			if (Func.isNotEmpty(snVO.getAsnBillNo()) && Func.isNotEmpty(snVO.getAsnLineNo())) {
				AsnDetail asnDetail = asnDetailService.getOne(Condition.getQueryWrapper(param));
				Sn sn = new Sn();
				sn.setAsnBillId(asnDetail.getAsnBillId());
				sn.setAsnDetailId(asnDetail.getAsnDetailId());
				sn.setSnDetailCode(snVO.getSnDetailCode());
				sn.setBoxCode(snVO.getBoxCode());
				sn.setSnStatus(StringPool.ONE);
				asnDetail.setImportSn(StringPool.Y.toUpperCase());

				AsnDetail updateAsnDetail = new AsnDetail();
				updateAsnDetail.setAsnDetailId(asnDetail.getAsnDetailId());
				updateAsnDetail.setImportSn(StringPool.Y.toUpperCase());
				asnDetailService.updateById(updateAsnDetail);
				snService.save(sn);
			}
		}
		return true;
	}

	@Override
	public void exportAsnExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<AsnHeader> asnHeaderList = super.list(Condition.getQueryWrapper(params, AsnHeader.class));
		if (Func.isNotEmpty(asnHeaderList)) {
			List<AsnHeaderExcel> asnHeaderExcels = this.getAsnExportDTOList(asnHeaderList);
			ExcelUtil.export(response, "入库单", "入库单数据表", asnHeaderExcels, AsnHeaderExcel.class);
		}
	}

	private List<AsnHeaderExcel> getAsnExportDTOList(List<AsnHeader> asnHeaderList) {
		List<AsnHeaderExcel> asnExportList = new ArrayList<>();
		List<Long> asnBillIdList = NodesUtil.toList(asnHeaderList, AsnHeader::getAsnBillId);
		// 查询所有收货单明细
		List<AsnDetailVO> asnDetailAll = AsnDetailWrapper.build().listVO(
			asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
				.lambda().in(AsnDetail::getAsnBillId, asnBillIdList)));

		//查询所有货主列表
		List<Long> woIdList = NodesUtil.toList(asnHeaderList, AsnHeader::getWoId);
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		List<Owner> ownerAll = ownerService.list().stream().filter(owner -> {
			return woIdList.contains(owner.getWoId());
		}).collect(Collectors.toList());

		//查询入库单所属库房
		List<Long> whIdList = NodesUtil.toList(asnHeaderList, AsnHeader::getWhId);
		/*List<Warehouse> warehouseAll = WarehouseCache.list().stream().filter(u -> {
			return whIdList.contains(u.getWhId());
		}).collect(Collectors.toList());*/
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<Warehouse> warehouseAll = warehouseService.listByIds(whIdList);

		for (AsnHeader asnHeader : asnHeaderList) {
			List<AsnDetailVO> asnDetailList = asnDetailAll.stream().filter(asnDetail -> {
				return asnDetail.getAsnBillId().equals(asnHeader.getAsnBillId());
			}).collect(Collectors.toList());

			// 查询当前入库单的货主
			Owner owner = ownerAll.stream().filter(item -> {
				return item.getWoId().equals(asnHeader.getWoId());
			}).findFirst().orElse(null);

			//查询当前入库单所属库房
			Warehouse warehouse = warehouseAll.stream().filter(item -> {
				return item.getWhId().equals(asnHeader.getWhId());
			}).findFirst().orElse(null);
			AsnHeaderExcel asnExportDTO = new AsnHeaderExcel();
			asnExportDTO.setAsnBillNo(asnHeader.getAsnBillNo());
//			asnExportDTO.setContact(asnHeader.getContact());
//			asnExportDTO.setPhone(asnHeader.getPhone());
//			asnExportDTO.setSuAddress(asnHeader.getSAddress());
//			asnExportDTO.setDeptCode(asnHeader.getDeptCode());
//			asnExportDTO.setDeptName(asnHeader.getDeptName());
//			asnExportDTO.setAsnRemark(asnHeader.getAsnBillRemark());
//			asnExportDTO.setOrderNo(asnHeader.getOrderNo());
//			asnExportDTO.setSuCode(asnHeader.getSCode());
//			asnExportDTO.setSuName(asnHeader.getSName());
			asnExportDTO.setScheduledArrivalDate(Date.from(asnHeader.getScheduledArrivalDate().atZone(ZoneId.systemDefault()).toInstant()));
			//货主
			if (Func.isNotEmpty(owner)) {
				asnExportDTO.setWoCode(owner.getOwnerCode());
				asnExportDTO.setWoName(owner.getOwnerName());
			}

			//所属库房
			if (Func.isNotEmpty(warehouse)) {
				asnExportDTO.setWhCode(warehouse.getWhCode());
				asnExportDTO.setWhName(warehouse.getWhName());
			}

			//单据类型
			BillType billType = BillTypeCache.getByCode(asnHeader.getBillTypeCd());
			if (Func.isNotEmpty(billType)) {
				asnExportDTO.setBillTypeCd(billType.getBillTypeName());
			}
			//入库方式
			asnExportDTO.setInstoreType(DictCache.getValue(DictConstant.INSTORE_TYPE, asnHeader.getInstoreType()));
			int maxLength = 1;
			if (Func.isNotEmpty(asnDetailList)) {
				maxLength = asnDetailList.size();
			}
			for (int i = 0; i < maxLength; i++) {
				AsnDetail asnDetail = i < asnDetailList.size() ? asnDetailList.get(i) : null;
				if (Func.isNotEmpty(asnDetail)) {
					// 封装入库单明细数据
					AsnHeaderExcel copy = BeanUtil.copy(asnExportDTO, AsnHeaderExcel.class);
					copy.setSkuCode(asnDetail.getSkuCode());
					copy.setSkuName(asnDetail.getSkuName());
					SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
					if (Func.isNotEmpty(skuPackage)) {
						copy.setWspName(skuPackage.getWspName());
					}
					copy.setAsnLineNo(asnDetail.getAsnLineNo());
					copy.setUmCode(asnDetail.getUmCode());
					copy.setUmName(asnDetail.getUmName());
					copy.setPlanQty(asnDetail.getPlanQty());
//					asnExportDTO.setDetailPrice(asnDetail.getDetailPrice());
					copy.setScanQty(asnDetail.getScanQty());
					copy.setSurplusQty(asnDetail.getSurplusQty());
//					asnExportDTO.setDetailAmount(asnDetail.getDetailAmount());
//					copy.setAsnBillDetailKey(asnDetail.getAsnBillDetailKey());
//					copy.setAsnDetailRemark(asnDetail.getAsnDetailRemark());
					// 处理批属性
					for (int j = 1; j <= ParamCache.LOT_COUNT; j++) {
						copy.skuLotSet(j, asnDetail.skuLotGet(j));
					}
					asnExportList.add(copy);
				}
			}
		}
		return asnExportList;
	}

	@Override
	public List<DataVerify> validAsnExcel(List<AsnHeaderExcel> dataList) {
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		List<DataVerify> dataVerifyList = new ArrayList<>();
		if (Func.isNotEmpty(dataList)) {
			List<String> asnBillTypeCodeList = NodesUtil.toList(dataList, AsnHeaderExcel::getBillTypeCd);
			List<String> instoreTypeCodeList = NodesUtil.toList(dataList, AsnHeaderExcel::getInstoreType);
			List<String> ownerCodeList = NodesUtil.toList(dataList, AsnHeaderExcel::getWoCode);
			List<String> whCodeList = NodesUtil.toList(dataList, AsnHeaderExcel::getWhCode);
			List<String> deptCodeList = NodesUtil.toList(dataList, AsnHeaderExcel::getDeptCode);
			List<String> skuCodeList = NodesUtil.toList(dataList, AsnHeaderExcel::getSkuCode);
			List<String> wspNameList = NodesUtil.toList(dataList, AsnHeaderExcel::getWspName);
			List<String> umCodeList = NodesUtil.toList(dataList, AsnHeaderExcel::getUmCode);

			// 单据类型
			IBillTypeService billTypeService = SpringUtil.getBean(IBillTypeService.class);
			List<BillType> billTypeAllList = billTypeService.list(Condition.getQueryWrapper(new BillType()).lambda()
				.eq(BillType::getIoType, "I")
				.in(BillType::getBillTypeName, asnBillTypeCodeList));
			// 入库方式
			List<Dict> instoreTypeAllList = DictCache.list(DictConstant.INSTORE_TYPE).stream().filter(u -> {
				return instoreTypeCodeList.contains(String.valueOf(u.getDictValue()));
			}).collect(Collectors.toList());
			// 货主
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			/*List<Owner> ownerAllList = ownerService.list().stream().filter(owner -> {
				return ownerCodeList.contains(owner.getOwnerCode());
			}).collect(Collectors.toList());*/
			List<Owner> ownerAllList = ownerService.list(Condition.getQueryWrapper(new Owner())
				.lambda()
				.in(Owner::getOwnerCode, ownerCodeList));
			// 仓库
			/*List<Warehouse> warehouseAllList = WarehouseCache.list().stream().filter(u -> {
				return whCodeList.contains(u.getWhCode());
			}).collect(Collectors.toList());*/
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			List<Warehouse> warehouseAllList = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
				.lambda()
				.in(Warehouse::getWhCode, whCodeList));
			// 机构
			List<Dept> deptAllList = new ArrayList<>();
			if (Func.isNotEmpty(deptCodeList)) {
				deptAllList = deptService.list(Condition.getQueryWrapper(new Dept()).lambda()
					.in(Dept::getDeptCode, deptCodeList));
			}
			// 明细 - 物品信息
			//List<Sku> skuAllList = SkuCache.listByCode(skuCodeList);
			ISkuService skuService = SpringUtil.getBean(ISkuService.class);
			List<Sku> skuAllList = skuService.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.in(Sku::getSkuCode, skuCodeList)
			).stream().collect(Collectors.toList());
			// 明细 - 包装信息
			List<SkuPackage> skuPackageAllList = new ArrayList<>();
			// 明细 - 包装明细信息
			List<SkuPackageDetail> skuPackageDetailAllList = new ArrayList<>();
			if (Func.isNotEmpty(wspNameList)) {
				skuPackageAllList = skuPackageService.list(Condition.getQueryWrapper(new SkuPackage())
					.lambda()
					.in(SkuPackage::getWspName, wspNameList));
				if (Func.isNotEmpty(skuPackageAllList)) {
					skuPackageDetailAllList = skuPackageDetailService.list(Condition.getQueryWrapper(
							new SkuPackageDetail())
						.lambda()
						.in(SkuPackageDetail::getWspId, NodesUtil.toList(skuPackageAllList, SkuPackage::getWspId))
						.func(sql -> {
							if (Func.isNotEmpty(umCodeList)) {
								sql.in(SkuPackageDetail::getWsuCode, umCodeList);
							}
						})
					);
				}
			}
			List<String> billNoList = NodesUtil.toList(dataList, AsnHeaderExcel::getAsnBillNo);
			List<AsnHeader> asnHeaders = super.list(Condition.getQueryWrapper(new AsnHeader()).lambda()
				.in(AsnHeader::getAsnBillNo, billNoList));
			List<AsnHeaderDTO> asnHeaderList = new ArrayList<>();
			Map<String, AsnHeaderDTO> cache = new HashMap<>();
			int index = 1;
			List<Integer> snLists = new ArrayList<>();
			for (AsnHeaderExcel asnHeaderExcel : dataList) {
				DataVerify dataVerify = new DataVerify();
				dataVerify.setIndex(index);
				AsnHeaderDTO asnHeaderDTO = new AsnHeaderDTO();
				ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(asnHeaderExcel);
				List<String> errorList = new ArrayList<>();
				String cacheKey = asnHeaderExcel.getAsnBillNo();
				AsnHeader asnHeader = asnHeaders.stream().filter(u -> {
					return u.getAsnBillNo().equals(asnHeaderExcel.getAsnBillNo());
				}).findFirst().orElse(null);

				if (Func.isNotEmpty(asnHeader)) {
					asnHeaderDTO.setAsnBillNo(asnHeaderExcel.getAsnBillNo());

					Warehouse warehouse = warehouseAllList.stream().filter(u -> {
						return u.getWhCode().equals(asnHeaderExcel.getWhCode());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(warehouse)) {
						asnHeaderDTO.setWhId(warehouse.getWhId());
						asnHeaderDTO.setWhCode(warehouse.getWhCode());
					} else {
						errorList.add("指定库房编码不存在");
					}
					// 货主
					Owner owner = ownerAllList.stream().filter(u -> {
						return u.getOwnerCode().equals(asnHeaderExcel.getWoCode());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(owner)) {
						asnHeaderDTO.setWoId(owner.getWoId());
						asnHeaderDTO.setOwnerCode(owner.getOwnerCode());
					} else {
						errorList.add("指定货主编码不存在");
					}
					// 单据类型
					BillType billType = billTypeAllList.stream().filter(u -> {
						return u.getBillTypeName().equals(asnHeaderExcel.getBillTypeCd());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(billType)) {
						asnHeaderDTO.setBillTypeCd(billType.getBillTypeCd());
					} else {
						errorList.add("指定单据类型不存在");
					}
					// 入库方式
					Dict instoreType = instoreTypeAllList.stream().filter(u -> {
						return u.getDictValue().equals(asnHeaderExcel.getInstoreType());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(instoreType)) {
						asnHeaderDTO.setInstoreType(instoreType.getDictKey());
					} else {
						errorList.add("指定入库方式不存在");
					}
					// 机构
					Dept dept = deptAllList.stream().filter(u -> {
						return u.getDeptCode().equals(asnHeaderExcel.getDeptCode());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(dept)) {
//						asnHeaderDTO.setDeptId(dept.getId());
//						asnHeaderDTO.setDeptCode(dept.getDeptCode());
//						asnHeaderDTO.setDeptName(dept.getDeptName());
					}
					asnHeaderDTO.setCreateUser(AuthUtil.getUserId());
//					asnHeaderDTO.setContact(asnHeaderExcel.getContact());
//					asnHeaderDTO.setPhone(asnHeaderExcel.getPhone());
//					asnHeaderDTO.setSAddress(asnHeaderExcel.getSuAddress());
					asnHeaderDTO.setAsnBillRemark(asnHeaderExcel.getAsnBillRemark());
//					asnHeaderDTO.setOrderNo(asnHeaderExcel.getOrderNo());
//					asnHeaderDTO.setSCode(asnHeaderExcel.getSuCode());
					asnHeaderDTO.setSName(asnHeaderExcel.getSuName());
					asnHeaderDTO.setScheduledArrivalDateCd(asnHeaderExcel.getScheduledArrivalDate());

					// 处理明细
					AsnDetailDTO asnDetail = BeanUtil.copy(asnHeaderExcel, AsnDetailDTO.class);
					if (Func.isEmpty(asnDetail)) {
						continue;
					}
					// 物品
					Sku sku = skuAllList.stream().filter(u -> {
						return u.getSkuCode().equals(asnHeaderExcel.getSkuCode());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(sku)) {
						asnDetail.setSkuId(sku.getSkuId());
						asnDetail.setSkuCode(sku.getSkuCode());
						asnDetail.setSkuName(sku.getSkuName());
					} else {
						errorList.add("指定物品编码不存在");
					}
					// 包装
					SkuPackage skuPackage = skuPackageAllList.stream().filter(u -> {
						return u.getWspName().equals(asnHeaderExcel.getWspName());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(skuPackage)) {
						asnDetail.setWspId(skuPackage.getWspId());
						// 包装明细
						List<SkuPackageDetail> skuPackageDetailList = skuPackageDetailAllList.stream().filter(u -> {
							return u.getWspId().equals(skuPackage.getWspId());
						}).collect(Collectors.toList());
						// 业务单位
						SkuPackageDetail packageDetail = skuPackageDetailList.stream().filter(u -> {
							return u.getWsuCode().equals(asnHeaderExcel.getUmCode());
						}).findFirst().orElse(null);
						if (Func.isNotEmpty(packageDetail)) {
							asnDetail.setSkuLevel(packageDetail.getSkuLevel());
							asnDetail.setSkuSpec(packageDetail.getSkuSpec());
							asnDetail.setConvertQty(packageDetail.getConvertQty());
							asnDetail.setUmCode(packageDetail.getWsuCode());
							asnDetail.setUmName(packageDetail.getWsuName());
						} else {
							errorList.add("指定包装明细不存在");
						}
						// 基础计量单位
						SkuPackageDetail basePackageDetail = skuPackageDetailList.stream().filter(u -> {
							return u.getSkuLevel().equals(SkuLevelEnum.Base.getIndex());
						}).findFirst().orElse(null);
						if (Func.isNotEmpty(basePackageDetail)) {
							asnDetail.setBaseUmCode(basePackageDetail.getWsuCode());
							asnDetail.setBaseUmName(basePackageDetail.getWsuName());
						} else {
							errorList.add("指定包装未设置基础计量单位");
						}
					} else {
						errorList.add("指定包装名称不存在");
					}
					// 数量
					asnDetail.setScanQty(BigDecimal.ZERO);
					if (Func.isNotEmpty(asnDetail.getPlanQty())) {
						asnDetail.setSurplusQty(asnDetail.getPlanQty().subtract(asnDetail.getScanQty()));
					}
					if (Func.isEmpty(asnHeaderDTO.getAsnDetailList())) {
						asnHeaderDTO.setAsnDetailList(new ArrayList<>());
					}
					asnHeaderDTO.getAsnDetailList().add(asnDetail);
				} else {
					errorList.add(String.format("入库单[%s]不存在", asnHeaderExcel.getAsnBillNo()));
				}
				if (validResult.hasErrors()) {
					errorList.add(StringUtil.join(validResult.getAllErrors()));
				}
				if (Func.isNotEmpty(errorList)) {
					dataVerify.setMessage(StringUtil.join(errorList));
				} else {
					if (cache.containsKey(cacheKey)) {
						cache.get(cacheKey).getAsnDetailList().addAll(asnHeaderDTO.getAsnDetailList());
					} else {
						// 记录到缓存map中
						cache.put(cacheKey, asnHeaderDTO);
					}
					dataVerify.setCacheKey(cacheKey);
				}
				dataVerifyList.add(dataVerify);
				index++;
			}
			if (Func.isNotEmpty(cache)) {
				// 存储数据到redis缓存中
				for (String code : cache.keySet()) {
					AsnCache.put(code, cache.get(code));
				}
			}
		}
		return dataVerifyList;
	}

	@Override
	public boolean importAsnData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			AsnHeaderDTO asnHeaderDTO = AsnCache.getDTOByBillNo(dataVerify.getCacheKey());
			if (Func.isEmpty(asnHeaderDTO)) {
				continue;
			}
			asnHeaderDTO.setScheduledArrivalDate(asnHeaderDTO.getScheduledArrivalDateCd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			asnHeaderDTO.setUpdateUser(AuthUtil.getUserId());
			asnHeaderDTO.setUpdateTime(DateUtil.now());
			IBillTypeService billTypeService = SpringUtil.getBean(IBillTypeService.class);
			BillType billType = billTypeService.list(Condition.getQueryWrapper(new BillType()).lambda()
					.eq(BillType::getBillTypeName, asnHeaderDTO.getBillTypeCd()))
				.stream().findFirst().orElse(null);
			if (Func.isNotEmpty(billType)) {
				asnHeaderDTO.setBillTypeCd(billType.getBillTypeCd());
			}
//			if (Func.isNotEmpty(asnHeaderDTO.getSCode())) {
//				//供应商
//				IEnterpriseService enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
//				//Enterprise enterprise = enterpriseService.getByCode(asnHeaderDTO.getSCode());
//				Enterprise enterprise = enterpriseService.list(Condition.getQueryWrapper(new Enterprise())
//						.lambda()
//						.eq(Enterprise::getEnterpriseCode, asnHeaderDTO.getSCode()))
//					.stream().findFirst().orElse(null);
//
//				if (enterprise != null) {
//					asnHeaderDTO.setSName(enterprise.getEnterpriseName());
//				}
//			}
			Query tmpQuery = new Query();
			tmpQuery.setCurrent(1);
			tmpQuery.setSize(1);
			List<AsnDetailDTO> asnDetailList = asnHeaderDTO.getAsnDetailList();
			if (asnDetailList != null && asnDetailList.size() > 0) {
				for (int i = 0; i < asnDetailList.size(); i++) {
					AsnDetailDTO asnDetail = asnDetailList.get(i);
					//通过包装id获得包装明细列表(升序排序)
					List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(asnDetail.getWspId())
						.stream()
						.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
						.collect(Collectors.toList());

					for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
						if (skuPackageDetail.getWsuCode().equals(asnDetail.getUmCode())) {
							//包装层级
							asnDetail.setSkuLevel(Integer.parseInt(skuPackageDetail.getSkuLevel().toString()));//包装层级
							//换算倍数
							asnDetail.setConvertQty(Integer.parseInt(skuPackageDetail.getConvertQty().toString()));
						}
						if (1 == Integer.parseInt(skuPackageDetail.getSkuLevel().toString())) {
							//基础计量单位编码
							asnDetail.setBaseUmCode(skuPackageDetail.getWsuCode());
							//基础计量单位名称
							asnDetail.setBaseUmName(skuPackageDetail.getWsuName());
						}
					}
					//换算倍率
					if (Func.isNotEmpty(asnDetail.getWspId())) {
						SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(asnDetail.getWspId());
						asnDetail.setConvertQty(Func.isNotEmpty(skuPackageDetail) ?
							Integer.parseInt(skuPackageDetail.getConvertQty().toString()) : asnDetail.getConvertQty());
					}
				}
			}
			this.saveOrUpdate(asnHeaderDTO);
			AsnCache.remove(asnHeaderDTO.getAsnBillNo());
		}
		return true;
	}

	/**
	 * 返回错误信息
	 *
	 * @param result
	 * @param dataVerifyDTO
	 * @param validResult
	 */
	private void returnErrors(List<DataVerify> result, DataVerify dataVerifyDTO, ValidationUtil.ValidResult validResult) {
		List<String> errorList = new ArrayList<>();
		if (Func.isNotEmpty(validResult.getAllErrors())) {
			errorList.add(StringUtil.join(validResult.getAllErrors()));
		}
		dataVerifyDTO.setMessage(StringUtil.join(validResult.getAllErrors()));
		result.add(dataVerifyDTO);
	}

	/**
	 * 根据分组编号生成箱号
	 *
	 * @param dataList
	 * @return
	 */
	@NotNull
	private Map<String, String> getBoxCodesByGroupCode(List<SnExcel> dataList) {

		HashMap<String, List<SnExcel>> map = new HashMap<String, List<SnExcel>>();
		for (SnExcel obj : dataList) {
			if (map.containsKey(obj.getGroupNum())) {
				List<SnExcel> oldSL = new ArrayList<>();
				oldSL.add(obj);
				map.get(obj.getGroupNum()).addAll(oldSL);
			} else {
				List<SnExcel> newSL = new ArrayList<>();
				newSL.add(obj);
				map.put(obj.getGroupNum(), newSL);
			}
		}

		Map<String, String> groupBoxCodeMap = new HashMap<>();
		if (Func.isNotEmpty(map)) {
			Iterator<Map.Entry<String, List<SnExcel>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, List<SnExcel>> next = iterator.next();
				groupBoxCodeMap.put(next.getKey(), SerialNoCache.createBoxCode());
			}
		}
		return groupBoxCodeMap;
	}


	private LambdaQueryWrapper<AsnHeader> getQueryWrapper(AsnHeaderDTO asnHeader) {
		LambdaQueryWrapper<AsnHeader> queryWrapper = Condition.getQueryWrapper(new AsnHeader()).lambda();

		if (Func.isNotEmpty(asnHeader.getAsnBillNo())) {
			queryWrapper.like(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo());
		}
//		if (Func.isNotEmpty(asnHeader.getOrderNo())) {
//			queryWrapper.like(AsnHeader::getOrderNo, asnHeader.getOrderNo());
//		}
		if (Func.isNotEmpty(asnHeader.getBillTypeCd())) {
			queryWrapper.eq(AsnHeader::getBillTypeCd, asnHeader.getBillTypeCd());
		}
		if (Func.isNotEmpty(asnHeader.getAsnBillState())) {
			queryWrapper.eq(AsnHeader::getAsnBillState, asnHeader.getAsnBillState());
		}
		// 处理 预计到货时间 范围查询
		if (Func.isNotEmpty(asnHeader.getScheduledArrivalRange())) {
			String[] timeList = JsonUtil.parse(asnHeader.getScheduledArrivalRange(), String[].class);
			if (Func.isNotEmpty(timeList) && timeList.length > 0) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				queryWrapper.ge(AsnHeader::getScheduledArrivalDate, LocalDateTime.parse(timeList[0], dateTimeFormatter));
				if (timeList.length > 1) {
					queryWrapper.le(AsnHeader::getScheduledArrivalDate, LocalDateTime.parse(timeList[1], dateTimeFormatter));
				}
			}
		}
		// 处理 实际到货时间 范围查询
		if (Func.isNotEmpty(asnHeader.getActualArrivalRange())) {
			String[] timeList = JsonUtil.parse(asnHeader.getActualArrivalRange(), String[].class);
			if (Func.isNotEmpty(timeList) && timeList.length > 0) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				queryWrapper.ge(AsnHeader::getActualArrivalDate, LocalDateTime.parse(timeList[0], dateTimeFormatter));
				if (timeList.length > 1) {
					queryWrapper.le(AsnHeader::getActualArrivalDate, LocalDateTime.parse(timeList[1], dateTimeFormatter));
				}
			}
		}
		// 处理 单据完成时间 范围查询
		if (Func.isNotEmpty(asnHeader.getFinishRange())) {
			String[] timeList = JsonUtil.parse(asnHeader.getFinishRange(), String[].class);
			if (Func.isNotEmpty(timeList) && timeList.length > 0) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				queryWrapper.ge(AsnHeader::getFinishDate, LocalDateTime.parse(timeList[0], dateTimeFormatter));
				if (timeList.length > 1) {
					queryWrapper.le(AsnHeader::getFinishDate, LocalDateTime.parse(timeList[1], dateTimeFormatter));
				}
			}
		}
		queryWrapper.orderByDesc(AsnHeader::getAsnBillNo);
		return queryWrapper;
	}

	public void addStockDetail(AsnHeader asnHeader, AsnDetail asnDetail) {
		ILotService lotService = SpringUtil.getBean(ILotService.class);
		SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(asnDetail.getWspId(), SkuLevelEnum.Plate.getIndex());

		Sku sku = SkuCache.getById(asnDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException(String.format("物品[%s] 不存在或已删除！", asnDetail.getSkuId()));
		}
		BigDecimal surplus_qty = asnDetail.getPlanQty();
		BigDecimal convert_qty = BigDecimal.ZERO;
		if (Func.isEmpty(skuPackageDetail)) {
			convert_qty = asnDetail.getPlanQty();
		} else {
			convert_qty = new BigDecimal(skuPackageDetail.getConvertQty());
		}
		// 查询当前库房的入库暂存区
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		Zone zone = zoneService.list(Condition.getQueryWrapper(new Zone()).lambda()
				.eq(Zone::getWhId, asnHeader.getWhId())
				.eq(Zone::getZoneType, ZoneTypeEnum.SHIPPING_INSTOCK.getIndex()))
			.stream().findFirst().orElse(null);
		List<StockDetail> stockDetailList = new ArrayList<>();

		ILpnService lpnService = SpringUtil.getBean(ILpnService.class);
		while (BigDecimalUtil.gt(surplus_qty, BigDecimal.ZERO)) {
			StockDetail stockDetail = new StockDetail();
			stockDetail.setWhId(asnHeader.getWhId());
			if (Func.isNotEmpty(zone)) {
				stockDetail.setZoneId(zone.getZoneId());
			}
			stockDetail.setSkuId(asnDetail.getSkuId());
			stockDetail.setWspId(asnDetail.getWspId());
			stockDetail.setLpnCode(lpnService.create());
			//stockDetail.setBoxCode(SerialNoCache.createBoxCode());
			StockProcDTO stockProc = new StockProcDTO();
//			SkuLotUtil.fill(sku, stockProc, asnDetail, null);
			LotDTO lotDTO = BeanUtil.copy(stockProc, LotDTO.class);
			lotDTO.setWhId(asnHeader.getWhId());
			lotDTO.setSkuId(sku.getSkuId());
			lotDTO.setSkuCode(sku.getSkuCode());
			lotDTO.setSkuName(sku.getSkuName());
			lotDTO.setSystemProcId(-1L);
			Lot lot = lotService.chkLotNumber(lotDTO);
			if (Func.isNotEmpty(lot)) {
				stockDetail.setLotNumber(lot.getLotNumber());
			}
			if (BigDecimalUtil.ge(surplus_qty, convert_qty)) {
				stockDetail.setUnreceivedQty(convert_qty);
				surplus_qty = surplus_qty.subtract(convert_qty);
			} else {
				stockDetail.setUnreceivedQty(surplus_qty);
				surplus_qty = BigDecimal.ZERO;
			}
			stockDetail.setBillDetailId(asnDetail.getAsnDetailId());
			stockDetail.setSoBillId(asnHeader.getAsnBillId());
			stockDetailList.add(stockDetail);
		}
		if (Func.isNotEmpty(stockDetailList)) {
			stockDetailService.saveBatch(stockDetailList, stockDetailList.size());
		}
	}
}
