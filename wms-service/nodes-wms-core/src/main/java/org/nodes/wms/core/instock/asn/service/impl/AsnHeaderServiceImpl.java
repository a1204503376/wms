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
import org.nodes.core.constant.DictCodeConstant;
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
import org.nodes.wms.dao.stock.entities.Stock;
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
 * ??????????????? ???????????????
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
	 * ??????????????? ???????????????
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
						//?????????
						AsnHeader asnHeader = new AsnHeader();
						asnHeader.setAsnBillState(40);
						asnHeader.setFinishDate(LocalDateTime.now());
						//????????????
						UpdateWrapper<AsnHeader> updateWrapper = new UpdateWrapper<>();
						updateWrapper.lambda().eq(AsnHeader::getAsnBillId, id);
						super.update(asnHeader, updateWrapper);
					} else {
						throw new ServiceException("???????????????????????????");
					}
					taskService.closeTask(id, TaskTypeEnum.Check);
				}
			}
		}
		return true;
	}

	@Override
	public AsnHeaderVO getDetail(Long asnBillId) {
		//?????????????????????
		AsnHeader asnHeader = super.getById(asnBillId);
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("???????????????????????????");
		}
		AsnHeaderVO asnHeaderVO = AsnHeaderWrapper.build().entityVO(asnHeader);
		//?????????????????????????????????
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
	 * ???????????? ??????????????????
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
//			throw new ServiceException("????????????????????????????????????");
//		}
//		AsnHeader asnHeader = super.getById(asnDetail.getAsnBillId());
//		if (Func.isEmpty(asnHeader)) {
//			throw new ServiceException("??????????????????????????????");
//		}
		//?????????
		InstockExecuteVO vo = instockService.execute(
			list, Func.isNotEmpty(asnHeader) ? asnHeader.getBillTypeCd() : null);
//		Set<Long> types = new HashSet<>();
//		for (Stock item : list) {
//			//??????
//			types.add(item.getSkuId());
//		}
		BigDecimal qty = stockDetailList.stream().map(StockDetail::getStockQty).reduce(BigDecimal::add).get();
		vo.setTypeCount(qty.intValue());
		//??????????????????

		Location location = LocationCache.getById(list.get(0).getLocId());
		if (Func.isNotEmpty(location)) {
			vo.setLocCode(location.getLocCode());
			//????????????
			if (Func.isNotEmpty(location.getLocStatus())) {
				vo.setLocStatus(location.getLocStatus());
				vo.setLocStatusName(DictCache.getValue(DictCodeConstant.LOC_STATUS, location.getLocStatus()));
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

		//????????????
//		Dept dept = SysCache.getDept(asnHeader.getDeptId());
//		if (Func.isNotEmpty(dept)) {
//			asnHeader.setDeptCode(dept.getDeptCode());//????????????
//			asnHeader.setDeptName(dept.getDeptName());//????????????
//		}
		if (Func.isEmpty(asnHeader.getAsnDetailList())) {
			throw new ServiceException(String.format("???????????????????????????"));
		}
		//??????
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}
		asnHeader.setWhCode(warehouse.getWhCode());//????????????
		//????????????
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(asnHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("??????????????????????????????");
		}
		asnHeader.setOwnerCode(owner.getOwnerCode());
		asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());//????????????

		//??????????????????????????????
//		if (Func.isEmpty(asnHeader.getBillKey())) {
//			asnHeader.setBillKey(asnHeader.getAsnBillNo());
//		}
//		//??????????????????????????????
//		if (Func.isEmpty(asnHeader.getLastUpdateDate())) {
//			asnHeader.setLastUpdateDate(LocalDateTime.now());
//		}
//		//??????????????????????????????
//		if (Func.isEmpty(asnHeader.getPreCreateDate())) {
//			asnHeader.setPreCreateDate(LocalDateTime.now());
//		}
		//????????????
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
			//???????????????
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

		//????????????
//		Dept dept = SysCache.getDept(asnHeader.getDeptId());
//		if (Func.isNotEmpty(dept)) {
//			asnHeader.setDeptCode(dept.getDeptCode());//????????????
//			asnHeader.setDeptName(dept.getDeptName());//????????????
//		}
		if (Func.isEmpty(asnHeader.getAsnDetailList())) {
			throw new ServiceException(String.format("???????????????????????????"));
		}
		//??????
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}
		asnHeader.setWhCode(warehouse.getWhCode());//????????????
		//????????????
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(asnHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("??????????????????????????????");
		}
		asnHeader.setOwnerCode(owner.getOwnerCode());
		asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT.getCode());//????????????

		//??????????????????????????????
//		if (Func.isEmpty(asnHeader.getBillKey())) {
//			asnHeader.setBillKey(asnHeader.getAsnBillNo());
//		}
//		//??????????????????????????????
//		if (Func.isEmpty(asnHeader.getLastUpdateDate())) {
//			asnHeader.setLastUpdateDate(LocalDateTime.now());
//		}
//		//??????????????????????????????
//		if (Func.isEmpty(asnHeader.getPreCreateDate())) {
//			asnHeader.setPreCreateDate(LocalDateTime.now());
//		}
		//????????????
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
			//???????????????
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
			//????????????????????????,????????????
			AsnHeader asnHeader = super.getById(id);
			if (Func.isEmpty(asnHeader)) {
				throw new ServiceException("??????????????????");
			}
			if (!AsnBillStateEnum.NOT_RECEIPT.getCode().equals(asnHeader.getAsnBillState())) {
				throw new ServiceException(
					String.format("??????[%s]???%s??????????????????",
						asnHeader.getAsnBillNo(),
						DictCache.getValue(DictCodeConstant.ASN_BILL_STATE, asnHeader.getAsnBillState())));
			}
			if (asnHeader.getBillTypeCd().equals("109")) {
				throw new ServiceException("???????????????????????????! ");
			}
		}
		if (Func.isNotEmpty(idList) && super.removeByIds(idList)) {
			result = asnDetailService.remove(Condition.getQueryWrapper(new AsnDetail())
				.lambda()
				.in(AsnDetail::getAsnBillId, idList));
			// ????????????????????????
			stockDetailService.remove(Condition.getQueryWrapper(new StockDetail()).lambda()
				.in(StockDetail::getSoBillId, idList));
		}
		return result;
	}

	@Override
	public boolean canEdit(Long asnHeaderId) {
		AsnHeader asnHeader = super.getById(asnHeaderId);
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("??????????????????????????????ID???" + asnHeaderId + " ???");
		}
		// ????????????????????????
		Param param = ParamCache.getOne(ParamEnum.COUNT_PROFIT_TYPECD.getKey());
		if (asnHeader.getBillTypeCd().equals(param.getParamValue())) {
			throw new ServiceException("?????????????????????????????????! ");
		}
		if (asnHeader.getBillTypeCd().equals("109")) {
			throw new ServiceException("??????????????????????????????! ");
		}
		// ?????????????????????
		boolean result = AsnBillStateEnum.NOT_RECEIPT.getCode().equals(asnHeader.getAsnBillState());
//			|| AsnBillStateEnum.PART.getIndex().equals(asnHeader.getAsnBillState())
//			|| AsnBillStateEnum.PART.getIndex().equals(asnHeader.getAsnBillState());

//		if (result) {
//			// ???????????????????????????LPN
//			result = asnLpnDetailMapper.selectCount(Condition.getQueryWrapper(new AsnLpnDetail()).lambda()
//				.eq(AsnLpnDetail::getAsnBillId, asnHeaderId)) == 0;
//		}
		return result;
	}

	/**
	 * ???????????? ???????????????????????????
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
	 * ????????????
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	@Override
	public boolean submitMove(InStockSubmitVO inStockSubmitVO) {
		if (Func.isEmpty(inStockSubmitVO.getLpnCode())) {
			throw new ServiceException("??????????????????");
		}

		if (Func.isEmpty(inStockSubmitVO.getLocCode())) {
			throw new ServiceException("????????????????????????");
		}

		if (Func.isEmpty(inStockSubmitVO.getWhId())) {
			throw new ServiceException("??????ID????????????");
		}

		//?????????????????????????????????????????????????????????????????????
		List<Stock> list = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
//			.eq(Stock::getLpnCode, inStockSubmitVO.getLpnCode())
			.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
			.apply("stock_qty - pick_qty > 0"));

		if (Func.isEmpty(list)) throw new ServiceException(String.format("??????[%s]???????????????????????????"
			, inStockSubmitVO.getLpnCode()));
		//?????????id
		Long oldLocId = list.get(0).getLocId();

		//????????????????????????
		Location location = LocationCache.getValid(inStockSubmitVO.getLocCode());
		if (Func.isEmpty(location)) {
			throw new ServiceException(String.format("?????????????????????????????????"));
		}

		//??????????????????
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.MOVE);
		systemProcParam.setAction(ActionEnum.MOVE_LPN);
		systemProcParam.setLpnCode(inStockSubmitVO.getLpnCode());
		systemProcParam.setWhId(inStockSubmitVO.getWhId());
		SystemProc systemProc = systemProcService.create(systemProcParam);

		BladeUser user = AuthUtil.getUser();
		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setSourceLpnCode(inStockSubmitVO.getLpnCode());
		stockMoveDTO.setSourceLocId(oldLocId); //?????????id ?????????????????????
		stockMoveDTO.setTargetLocId(location.getLocId()); //????????????id ???????????????locCode??????locId
		stockMoveDTO.setSystemProcId(systemProc.getSystemProcId());
		stockMoveDTO.setEventType(EventTypeEnum.Move);
		stockService.stockMoveByLpn(stockMoveDTO);

		return true;
	}

	@Override
	public AsnHeaderVO getAsnHeaderAndDetails(String asnBillNo) {
		AsnHeader asnHeader = baseMapper.selectOne(Wrappers.lambdaQuery(AsnHeader.class).eq(AsnHeader::getAsnBillNo, asnBillNo));
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("??????????????????");
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
				throw new ServiceException("??????????????????????????????");
			}
			//??????????????????
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
			if (skuPackageDetails.size() <= 0) {
				throw new ServiceException(String.format("??????[%s]??????????????????", skuPackage.getWspId()));
			}
			asnDetailVO.setSkuPackageDetails(skuPackageDetails);
		});
		asnHeaderVO.setAsnDetailList(asnDetailVOS);
		return asnHeaderVO;
	}

	@Override
	public boolean submitAsnHeaderWithOrder(AsnHeaderOrderDto asnHeaderOrderDto) {
		// ???????????????
		AsnHeader asnHeader = super.getOne(Wrappers.lambdaQuery(AsnHeader.class).eq(AsnHeader::getAsnBillNo, asnHeaderOrderDto.getAsnBillNo()));
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("???????????????(????????????:" + asnHeaderOrderDto.getAsnBillNo() + ")");
		}
		if (asnHeader.getAsnBillState() == AsnBillStateEnum.COMPLETED.getCode()) {
			throw new ServiceException("?????????????????????????????????");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("?????????" + asnHeader.getAsnBillNo() + " ??????????????????????????????");
		}
		//????????????????????????
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("????????????????????????????????????????????????");
		}
		checkLoc(asnHeader.getWhId(), asnHeaderOrderDto.getLocCode());
		// ????????????????????????
		taskService.updateBeginTime(asnHeaderOrderDto.getAsnBillNo(), TaskTypeEnum.Check);
		// ?????????????????????????????????
		List<AsnDetail> asnDetailList = asnDetailService.list(Wrappers.lambdaQuery(AsnDetail.class)
			.eq(AsnDetail::getAsnBillNo, asnHeaderOrderDto.getAsnBillNo()));
		if (Func.isEmpty(asnDetailList)) {
			throw new ServiceException("????????????????????????");
		}
		//????????????????????????
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
				//??????????????????,?????????????????????????????????
				Location location = null;
				if (ZoneVirtualTypeEnum.isVirtual(asnHeaderOrderDto.getLocCode())) {
					location = LocationCache.getValid(
						Func.firstCharToUpper(asnHeaderOrderDto.getLocCode().toLowerCase()) + warehouse.getWhCode());
				} else {
					location = LocationCache.getValid(asnHeaderOrderDto.getLocCode());
				}
				if (Func.isEmpty(location)) {
					throw new ServiceException("????????????????????????????????????????????????");
				}
				// ????????????????????????
				Sku sku = SkuCache.getById(asnDetail.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("????????????????????????(??????ID:" + asnDetail.getSkuId() + ")????????????");
				}
				// ?????????????????????????????????
				SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
				if (Func.isEmpty(skuPackage)) {
					throw new ServiceException("????????????????????????(??????ID:" + asnDetail.getWspId() + ")????????????");
				}
//				SkuPackageDetail one = SkuPackageDetailCache.getOne(asnDetail.getWspId(), asnDetailVO.getSkuLevel());
//				BigDecimal scanQty = asnDetailVO.getPlanQty().multiply(BigDecimal.valueOf(one.getConvertQty()));
				BigDecimal add = asnDetailVO.getPlanQty().add(asnDetail.getScanQty());
				if (ParamCache.IS_OVER_VALUE == 0) {
					if (BigDecimalUtil.gt(add, asnDetail.getPlanQty())) {
						throw new ServiceException("????????????????????????????????????!");
					}
				}
				asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
				asnDetail.setScanQty(add);
				if (BigDecimalUtil.gt(asnDetail.getPlanQty(), asnDetail.getScanQty())) {
					asnDetail.setSurplusQty(asnDetail.getPlanQty().subtract(asnDetail.getScanQty()));
				} else {
					asnDetail.setSurplusQty(BigDecimal.ZERO);
				}
				//??????????????????
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
				stockProc.setSystemProcId(systemProcId);//??????id
				stockProc.setLocId(location.getLocId());//??????id
				stockProc.setSkuId(asnDetail.getSkuId());//??????id
				stockProc.setWspId(asnDetail.getWspId());//??????id
				stockProc.setStockQty(asnDetailVO.getPlanQty());//??????
				stockProc.setEventType(EventTypeEnum.Check);//????????????
				stockProc.setBillId(asnDetail.getAsnBillId());//?????????id
				stockProc.setBillDetailId(asnDetail.getAsnDetailId());//???????????????id
				stockProc.setBillNo(asnDetail.getAsnBillNo());//???????????????
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
				// ??????????????????
				ContainerLog containerLog = new ContainerLog();
				containerLog.setAsnBillId(asnDetail.getAsnBillId()); //??????id
				containerLog.setAsnBillNo(asnHeaderOrderDto.getAsnBillNo()); //????????????
				containerLog.setAsnDetailId(asnDetail.getAsnDetailId()); //????????????id
				containerLog.setLocId(location.getLocId());//??????id
				containerLog.setLocCode(asnHeaderOrderDto.getLocCode());//????????????
				containerLog.setAclQty(asnDetail.getScanQty()); //??????
				containerLog.setSkuId(asnDetail.getSkuId());//??????id
				containerLog.setSkuName(asnDetail.getSkuName()); //????????????
				containerLog.setWspId(asnDetail.getWspId());
				containerLog.setSkuLevel(asnDetail.getSkuLevel()); //????????????
				containerLog.setAclStatus(1); //????????????
				containerLog.setLotNumber(stock.getLotNumber()); //?????????
				containerLog.setSkuSpec(asnDetail.getSkuSpec()); //??????
				containerLog.setPlanQty(asnDetail.getPlanQty()); //????????????
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (!asnDetail.skuLotChk(i)) {
						continue;
					}
					containerLog.skuLotSet(i, asnDetail.skuLotGet(i));
				}
				containerLogService.save(containerLog);
				// ????????????????????????
				SkuLogDTO skuLogDTO = new SkuLogDTO();
				skuLogDTO.setSkuId(asnDetail.getSkuId());
				skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.INSTOCK);
				this.skuLogService.saveOrUpdate(skuLogDTO);
			}
		});

		asnDetailService.updateBatchById(asnDetailList);
		// ??????????????????????????????
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
			throw new ServiceException("?????????????????????????????????");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("?????????" + asnHeader.getAsnBillNo() + " ??????????????????????????????");
		}
//		dto.setSName(asnHeader.getSName());
		dto.setSName(asnHeader.getSupplierName());
		Location location = checkLoc(dto.getWhId(), dto.getLocCode());
		dto.setLocId(location.getLocId());
		List<String> lpnCodes = dto.getLpnCodes();
		if (Func.isEmpty(lpnCodes)) {
			throw new ServiceException("???????????????????????????");
		}
		List<AsnDetail> asnDetails = asnDetailService.list(asnHeader.getAsnBillId());
		if (Func.isEmpty(asnDetails)) {
			throw new ServiceException("????????????????????????");
		}

		//?????????????????????
//		List<AsnDetail> lpnDetails = asnDetails.stream().
//			filter(asnDetail -> asnDetail.getSkuLevel().equals(SkuLevelEnum.Plate.getIndex()))
//			.collect(Collectors.toList());

		//??????????????????ID?????????????????????
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getSkuId, NodesUtil.toList(asnDetails, AsnDetail::getSkuId))
			.in(StockDetail::getLpnCode, dto.getLpnCodes()).eq(StockDetail::getWhId, dto.getWhId())
			.gt(StockDetail::getUnreceivedQty, BigDecimal.ZERO));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("???????????????????????????????????????????????????");
		}

		List<String> lpnCodesTemp = NodesUtil.toList(stockDetails, StockDetail::getLpnCode);
		for (String lpncode : lpnCodes) {
			if (!lpnCodesTemp.contains(lpncode)) {
				throw new ServiceException(String.format("???????????????[%s]????????????????????????", lpncode));
			}
			List<StockDetail> stockDetailList = stockDetails.stream()
				.filter(stockDetail -> stockDetail.getLpnCode().equals(lpncode)).collect(Collectors.toList());
			for (StockDetail stockDetail : stockDetailList) {
				SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(stockDetail.getWspId(), SkuLevelEnum.Plate.getIndex());
				if (Func.isEmpty(packageDetail)) {
					throw new ServiceException("????????????????????????????????????");
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
				// ??????????????????
				ContainerLog containerLog = new ContainerLog();
				containerLog.setAsnBillId(asnHeader.getAsnBillId()); //??????id
				containerLog.setAsnBillNo(asnHeader.getAsnBillNo()); //????????????
				containerLog.setAsnDetailId(stockDetail.getBillDetailId()); //????????????id
				containerLog.setLocId(location.getLocId());//??????id
				containerLog.setLocCode(location.getLocCode());//????????????
				containerLog.setLpnCode(stockDetail.getLpnCode());
				containerLog.setAclQty(stockDetail.getUnreceivedQty()); //??????
				containerLog.setSkuId(stockDetail.getSkuId());//??????id
				containerLog.setSkuName(stock.getSkuName()); //????????????
				containerLog.setWspId(stock.getWspId());
				containerLog.setSkuLevel(stock.getSkuLevel()); //????????????
				containerLog.setAclStatus(1); //????????????
				containerLog.setLotNumber(stockDetail.getLotNumber()); //?????????
				containerLog.setPlanQty(stockDetail.getUnreceivedQty()); //????????????
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
		// ??????????????????????????????
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
			throw new ServiceException("????????????????????????????????????");
		}
		List<Zone> zones = zoneService.list(Wrappers.lambdaQuery(Zone.class).eq(Zone::getZoneType, 73));
		//?????????????????????????????????????????????????????????????????????
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.in(StockDetail::getZoneId, NodesUtil.toList(zones, Zone::getZoneId))
			.in(StockDetail::getLpnCode, NodesUtil.toList(putawayByTranSubmitVO.getLpnItems(), PutawayLpnItemVo::getLpnCode))
			.apply("stock_qty > pick_qty"));

		for (PutawayLpnItemVo putawayLpnItemVo : putawayByTranSubmitVO.getLpnItems()) {
			Location location = LocationCache.getValid(putawayByTranSubmitVO.getTargetLocCode());
			if (!location.getWhId().equals(putawayByTranSubmitVO.getWhId())) {
				throw new ServiceException(String.format(
					"????????????[%s] ???????????????[%s]???????????????", putawayByTranSubmitVO.getTargetLocCode(), putawayLpnItemVo.getLpnCode()));
			}

			if (!StringPool.UNKNOWN.equals(putawayLpnItemVo.getZoneCode())) {//???????????????unknown ???????????????????????????????????????

				Zone zone = zoneService.getById(location.getZoneId());
				if (Func.isEmpty(zone)) {
					throw new ServiceException(String.format("??????[%s]????????????????????????", location.getZoneId()));
				}
				if (!putawayLpnItemVo.getZoneCode().contains(zone.getZoneCode())) {
					throw new ServiceException(String.format("????????????[%s]?????????[%s]??????", putawayByTranSubmitVO.getTargetLocCode(), zoneService.getById(location.getZoneId()).getZoneName()));
				}
			}

			List<StockDetail> stockDetails = stockDetailList.stream().filter(stockDetail -> stockDetail.getLpnCode()
				.equals(putawayLpnItemVo.getLpnCode())).collect(Collectors.toList());

			if (Func.isEmpty(stockDetails)) {
				throw new ServiceException(String.format("??????[%s]????????????????????????", putawayLpnItemVo.getLpnCode()));
			}

			for (StockDetail stockDetail : stockDetails) {
				//??????????????????
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
	 * ???????????? ????????????
	 *
	 * @param dto
	 * @return
	 */
	@Override
	public synchronized SubmitAsnHeaderVO submitAsnHeader(AsnDTO dto) {
		boolean isAutoPutaway = false;

		//?????????????????? ????????????????????????????????????;
		AsnHeader asnHeader = super.getOne(Condition.getQueryWrapper(new AsnHeader())
			.lambda()
			.eq(AsnHeader::getAsnBillNo, dto.getAsnBillNo()));
		if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.COMPLETED.getCode())) {
			throw new ServiceException("?????????????????????????????????");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("?????????" + asnHeader.getAsnBillNo() + " ??????????????????????????????");
		}
		AsnInventory asnInventory = null;
		Task task = taskService.getById(dto.getTaskId());
		if (Func.isNotEmpty(task)) {
			asnInventory = asnInventoryService.getOne(Condition.getQueryWrapper(new AsnInventory()).lambda()
				.eq(AsnInventory::getAsnBillNo, task.getTaskRemark()));
		}
		//???????????? ??????????????????
//		dto.setSName(asnHeader.getSName());
		dto.setSName(asnHeader.getSupplierName());
		//?????????
//		Warehouse warehouse = WarehouseCache.getById(dto.getWhId());
//		if (Func.isEmpty(warehouse)) {
//			throw new ServiceException("??????????????????????????????");
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

		//?????????????????????????????????
		BigDecimal realQty;

		Sku sku = SkuCache.getById(dto.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("??????????????????????????????");
		}
		// ?????????????????????????????? 0:??????,1:???
		if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
			// ??????????????????,???????????????????????????
			realQty = dto.getScanQty();
		} else {
			// ???????????????????????????(????????????)=???????????????????????????(???????????????)*??????????????????????????????
			SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getById(dto.getWspdId());
			if (Func.isEmpty(skuPackageDetail)) {
				throw new ServiceException("????????????????????????????????????");
			}
			realQty = dto.getScanQty().multiply(new BigDecimal(skuPackageDetail.getConvertQty()));
		}
		// ????????????????????????
		taskService.updateBeginTime(dto.getAsnBillNo(), TaskTypeEnum.Check);
		// ??????????????????????????????
		List<AsnDetail> asnDetailListAll = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
			.lambda()
			.eq(AsnDetail::getAsnBillId, asnHeader.getAsnBillId()));
		// ??????????????????????????????
		List<AsnDetail> asnDetailList = asnDetailListAll.stream().filter(u -> {
			boolean result = false;
			if (u.getSkuId().equals(dto.getSkuId())) {
				result = true;
				// ???????????????
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
				String.format("?????????[%s] ??????????????????[%s]! ", asnHeader.getAsnBillNo(), sku.getSkuName()));
		}

		// ?????????????????????????????????????????????
		BigDecimal skuLotQty = BigDecimal.ZERO;
		for (AsnDetail asnDetail : asnDetailList) { // ???????????????????????????????????????????????????
			skuLotQty = skuLotQty.add(asnDetail.getSurplusQty());
		}
		//?????????????????????????????? 0:?????????,1:??????
		if (0 == ParamCache.IS_OVER_VALUE) {
			// ??????????????????????????? ??????????????????????????????
			if (realQty.compareTo(skuLotQty) > 0) {
				throw new ServiceException("????????????????????????????????????");
			}
		}

		//??????????????????
		Long systemProcId = saveSystemProcLog(dto, realQty);
		//????????????????????????
		UpdateWrapper<AsnLpnDetail> asnLpnDetailUpdateWrapper = new UpdateWrapper<>();
		asnLpnDetailUpdateWrapper.lambda().set(AsnLpnDetail::getLpnStatus, "2")
			.eq(AsnLpnDetail::getAsnBillId, asnHeader.getAsnBillId())
			.in(AsnLpnDetail::getAsnDetailId, NodesUtil.toList(asnDetailList, AsnDetail::getAsnDetailId))
			.eq(AsnLpnDetail::getLpnStatus, StringPool.ONE);
		asnLpnDetailMapper.update(null, asnLpnDetailUpdateWrapper);

		// ??????????????????
		BigDecimal planQty;

		//???????????????????????????????????????
		for (AsnDetail asnDetail : asnDetailList) {
			if (BigDecimalUtil.le(realQty, BigDecimal.ZERO)) {
				continue;
			}
			//???????????????????????? 0:??????,1:???
			planQty = asnDetail.getPlanQty();
			if (BigDecimalUtil.lt(planQty, realQty)) {
				continue;
			}
			//????????????????????????????????????????????????
			if (SnEnum.YES.getIndex().equals(sku.getIsSn())) {
				this.updateSn(dto);
			}
			//?????? ???????????????
			dto.setSkuSpec(asnDetail.getSkuSpec());
			//???????????? ???????????????
			if (BigDecimalUtil.lt(planQty, realQty)) {
				asnDetail.setSurplusQty(BigDecimal.ZERO);
			} else {
				asnDetail.setSurplusQty(planQty.subtract(realQty).subtract(asnDetail.getScanQty()));
			}
			//???????????????????????????
			this.saveAddStockInfo(dto, asnInventory, asnDetail, sku, realQty, systemProcId);
			// ??????????????????????????????????????????????????????
			UpdateWrapper<AsnDetail> updateWrapper = new UpdateWrapper<>();
			BigDecimal totalScanQty = asnDetail.getScanQty().add(realQty);
			updateWrapper.lambda()
				.eq(AsnDetail::getAsnDetailId, asnDetail.getAsnDetailId())
				.set(AsnDetail::getDetailStatus, AsnDetailStatusEnum.RECEIVED.getIndex())
				.set(AsnDetail::getScanQty, totalScanQty)
				.set(AsnDetail::getSurplusQty, planQty.subtract(totalScanQty));
			asnDetailService.update(null, updateWrapper);
			// ??????PO??????
			asnDetail.setScanQty(totalScanQty);
			asnDetail.setSurplusQty(planQty.subtract(totalScanQty));
			poDetailService.updateQty(asnDetail);
		}
		// ??????????????????????????????
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
	 * ???????????? ?????????????????????
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @param systemProcId
	 * @return
	 */
	private Stock saveAddStockInfo(AsnDTO dto, AsnInventory asnInventory, AsnDetail asnDetail, Sku sku, BigDecimal count, Long systemProcId) {
		//1.???????????????????????????????????????
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
				//????????????????????????
				StockMoveDTO stockMoveDTO = new StockMoveDTO();

				stockMoveDTO.setSourceLpnCode(dto.getLpnCode());
				stockMoveDTO.setSourceLocId(stockDetailList.get(0).getLocId()); //?????????id ?????????????????????
				stockMoveDTO.setTargetLocId(dto.getLocId()); //????????????id ???????????????locCode??????locId
				stockMoveDTO.setSystemProcId(systemProcId);
				stockMoveDTO.setEventType(EventTypeEnum.Move);
				stockService.stockMoveByLpn(stockMoveDTO);
			}
		}

		StockAddDTO stockProc = BeanUtil.copy(dto, StockAddDTO.class);
		stockProc.setSystemProcId(systemProcId);//??????id
		stockProc.setLocId(dto.getLocId());//??????id
		stockProc.setSkuId(dto.getSkuId());//??????id
		stockProc.setWspId(dto.getWspId());//??????id
		stockProc.setStockQty(count);//??????
		stockProc.setEventType(EventTypeEnum.Check);//????????????
		stockProc.setLpnCode(dto.getLpnCode());//????????????j
		stockProc.setBillId(asnDetail.getAsnBillId());//?????????id
		stockProc.setBillDetailId(asnDetail.getAsnDetailId());//???????????????id
		stockProc.setBillNo(asnDetail.getAsnBillNo());//???????????????

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
		// ????????????????????????
		SkuLogDTO skuLogDTO = new SkuLogDTO();
		skuLogDTO.setSkuId(dto.getSkuId());
		skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.INSTOCK);
		this.skuLogService.saveOrUpdate(skuLogDTO);
		//????????????
		this.saveContainerLog(dto, asnInventory, asnDetail, count, stock);

		return stock;
	}

	/**
	 * ???????????? ??????????????????
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @return
	 */
	private boolean saveContainerLog(AsnDTO dto, AsnInventory asnInventory, AsnDetail asnDetail, BigDecimal count, Stock stock) {
		//??????????????????
		ContainerLog containerLog = new ContainerLog();
		containerLog.setAsnBillId(asnDetail.getAsnBillId()); //??????id
		containerLog.setAsnBillNo(dto.getAsnBillNo()); //????????????
		if (Func.isNotEmpty(asnInventory)) {
			containerLog.setInventoryId(asnInventory.getId());
		}
		containerLog.setAsnDetailId(asnDetail.getAsnDetailId()); //????????????id
		containerLog.setLpnCode(dto.getLpnCode()); //????????????
		containerLog.setLocId(dto.getLocId());//??????id
		containerLog.setLocCode(dto.getLocCode());//????????????
		containerLog.setAclQty(count); //??????
		containerLog.setSkuId(asnDetail.getSkuId());//??????id
		containerLog.setSkuCode(asnDetail.getSkuCode());
		containerLog.setSkuName(asnDetail.getSkuName()); //????????????
		containerLog.setWspId(asnDetail.getWspId());
		containerLog.setSkuLevel(asnDetail.getSkuLevel()); //????????????
		containerLog.setAclStatus(1); //????????????
		containerLog.setSkuSpec(dto.getSkuSpec()); //??????
		containerLog.setPlanQty(asnDetail.getPlanQty()); //????????????
		containerLog.setLotNumber(stock.getLotNumber()); //?????????
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
	 * ???????????? ??????????????????
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
	 * ???????????? ???????????????????????????????????????
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
							throw new ServiceException(String.format("?????????[%s]?????????", code));
						}
						//???????????????
						sn.setSnStatus("2");
						snService.updateById(sn);
					} else {
						throw new ServiceException(String.format("?????????[%s]???????????????[%s]", code, asnDetail.getSkuName()));
					}
				} else {
					throw new ServiceException(String.format("?????????[%s]?????????????????????", code, asnDetail.getAsnBillNo()));
				}
			} else {
				if ("Y".equals(asnDetail.getImportSn())) {
					throw new ServiceException(String.format("?????????[%s]???????????????[%s]", code, asnDetail.getSkuName()));
				} else {
					//???????????????
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
	 * ???????????? ??????????????????
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	@Override
	public synchronized boolean submitPutaway(InStockSubmitVO inStockSubmitVO) {
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		if (Func.isEmpty(inStockSubmitVO.getWhId())) {
			throw new ServiceException("??????ID????????????");
		}
		if (Func.isEmpty(inStockSubmitVO.getLpnCode())) {
			throw new ServiceException("??????????????????");
		}
		if (Func.isEmpty(inStockSubmitVO.getLocCode())) {
			throw new ServiceException("????????????????????????");
		}
		if (ZoneVirtualTypeEnum.isVirtual(inStockSubmitVO.getLocCode())) {
			throw new ServiceException("????????????????????????????????????");
		}
		Location location = LocationCache.getValid(inStockSubmitVO.getLocCode());
		if (!location.getWhId().equals(inStockSubmitVO.getWhId())) {
			throw new ServiceException(String.format(
				"????????????[%s] ???????????????[%s]???????????????", inStockSubmitVO.getLocCode(), inStockSubmitVO.getLpnCode()));
		}
		inStockSubmitVO.setTargetLocId(location.getLocId());
		if (!StringPool.UNKNOWN.equals(inStockSubmitVO.getZoneCode())) {//???????????????unknown ???????????????????????????????????????

			Zone zone = zoneService.getById(location.getZoneId());
			if (Func.isEmpty(zone)) {
				throw new ServiceException(String.format("??????[%s]????????????????????????", location.getZoneId()));
			}
			if (!inStockSubmitVO.getZoneCode().contains(zone.getZoneCode())) {
				throw new ServiceException(String.format("????????????[%s]?????????[%s]??????", inStockSubmitVO.getLocCode(), zoneService.getById(location.getZoneId()).getZoneName()));
			}
		}
		List<Zone> zones = zoneService.list(Wrappers.lambdaQuery(Zone.class).eq(Zone::getZoneType, 73));
		//?????????????????????????????????????????????????????????????????????
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.in(StockDetail::getZoneId, NodesUtil.toList(zones, Zone::getZoneId))
			.eq(StockDetail::getLpnCode, inStockSubmitVO.getLpnCode())
			.apply("stock_qty > pick_qty"));
		if (Func.isEmpty(stockDetailList)) {
			throw new ServiceException(String.format("??????[%s]????????????????????????", inStockSubmitVO.getLpnCode()));
		}
//		List<Stock> list = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
//			.in(Stock::getStockId, NodesUtil.toList(stockDetailList, StockDetail::getStockId)));
//
//		if (Func.isEmpty(list)) {
//			throw new ServiceException(String.format("??????[%s]????????????????????????", inStockSubmitVO.getLpnCode()));
//		}

		for (StockDetail stockDetail : stockDetailList) {
			//??????????????????
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

		/*//?????????id
		Long oldLocId = list.get(0).getLocId();
		inStockSubmitVO.setSourceLocId(oldLocId);
		Location oldLocation = LocationCache.getValid(list.get(0).getLocCode());
		String oldLocCode = oldLocation.getLocCode();

		if (!ZoneVirtualTypeEnum.isVirtual(oldLocCode)) {
			throw new ServiceException(String.format("??????[%s]?????????", inStockSubmitVO.getLpnCode()));
		}

		//????????????????????????
		Location location = LocationCache.getValid(inStockSubmitVO.getLocCode());
		if (!location.getWhId().equals(inStockSubmitVO.getWhId())) {
			throw new ServiceException(String.format(
				"????????????[%s] ???????????????[%s]???????????????", inStockSubmitVO.getLocCode(), inStockSubmitVO.getLpnCode()));
		}
		inStockSubmitVO.setTargetLocId(location.getLocId());
		if (!StringPool.UNKNOWN.equals(inStockSubmitVO.getZoneCode())) {//???????????????unknown ???????????????????????????????????????

			Zone zone = zoneService.getById(location.getZoneId());
			if (Func.isEmpty(zone)) {
				throw new ServiceException(String.format("??????[%s]????????????????????????", location.getZoneId()));
			}
			if (!inStockSubmitVO.getZoneCode().contains(zone.getZoneCode())) {
				throw new ServiceException(String.format("????????????[%s]?????????[%s]??????", inStockSubmitVO.getLocCode(), zoneService.getById(location.getZoneId()).getZoneName()));
			}
		}


		//??????????????????
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.ASN);
		systemProcParam.setAction(ActionEnum.PUTAWAY_LPN);
		systemProcParam.setLpnCode(inStockSubmitVO.getLpnCode());
		systemProcParam.setWhId(inStockSubmitVO.getWhId());
		SystemProc systemProc = systemProcService.create(systemProcParam);

//		LpnPutWayMoveServiceImpl.LpnPutWayMoveParam param = new LpnPutWayMoveServiceImpl.LpnPutWayMoveParam();
//		param.setSourceLpnCode(inStockSubmitVO.getLpnCode());
//		param.setSourceLocId(oldLocId); //?????????id ?????????????????????
//		param.setTargetLocId(location.getLocId()); //????????????id ???????????????locCode??????locId
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
	 * ???????????? ??????????????????
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
		transferRecord.setSkuId(stockDetail.getSkuId()); //?????????
//			transferRecord.setFromLpn(stock.getLpnCode()); // ?????????
		Location sourceLocation = LocationCache.getById(stockDetail.getLocId());
		transferRecord.setFromLocCode(sourceLocation.getLocCode());//???????????????
		Zone sourceZone = zoneService.getById(sourceLocation.getZoneId());
		transferRecord.setFromZoneCode(sourceZone.getZoneCode());// ???????????????
		transferRecord.setFromZoneName(sourceZone.getZoneName());// ???????????????
		transferRecord.setQty(stockDetail.getStockQty()); // ?????????
		transferRecord.setWspId(stockDetail.getWspId()); //?????????Id
		transferRecord.setSkuLevel(1);//?????????
		Location targetLocation = LocationCache.getById(inStockSubmitVO.getTargetLocId());
		transferRecord.setFromLocId(inStockSubmitVO.getSourceLocId());
		transferRecord.setToLocId(targetLocation.getLocId());
		//????????????
//			if (Func.isNotEmpty(stockDetail.getWspId())) {
//				SkuPackageDetail skuPackageDetail = SkuPackageDetailCache.getOne(stockDetail.getWspId(), stock.getSkuLevel());
//				if (Func.isNotEmpty(skuPackageDetail)) {
//					transferRecord.setWsuCode(skuPackageDetail.getWsuCode());
//					transferRecord.setWsuName(skuPackageDetail.getWsuName());
//				}
//			}
		transferRecord.setToLocCode(targetLocation.getLocCode());//??????????????????
		Zone targetZone = zoneService.getById(sourceLocation.getZoneId());
		transferRecord.setToZoneCode(targetZone.getZoneCode());//??????????????????
		transferRecord.setToZoneName(targetZone.getZoneName());//??????????????????
		transferRecord.setLotNumber(stockDetail.getLotNumber());//?????????

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
	 * ???????????? ??????????????????????????????
	 *
	 * @param dto
	 * @return
	 */
	@Override
	public boolean instockHasSerial(AsnDTO dto) {
		if (Func.isEmpty(dto.getAsnDetailId())) {
			throw new ServiceException("????????????ID????????????");
		}
		if (Func.isEmpty(dto.getSnDetailCode())) {
			throw new ServiceException("?????????????????????");
		}

		//??????????????????
		AsnDetail asnDetail = asnDetailService.getOne(Condition.getQueryWrapper(new AsnDetail())
			.lambda().eq(AsnDetail::getAsnDetailId, dto.getAsnDetailId()));
		if (Func.isEmpty(asnDetail)) {
			throw new ServiceException("?????????????????????????????????");
		}

		Sn param = new Sn();
		param.setSnDetailCode(dto.getSnDetailCode());
		param.setAsnBillId(asnDetail.getAsnBillId());
		List<Sn> sns = new ArrayList<>();

		if ("Y".equals(asnDetail.getImportSn())) {//?????????????????????????????????????????????
			param.setSnStatus(StringPool.ONE);
			sns = snService.list(Condition.getQueryWrapper(param));
			if (sns.size() == 0) {
				throw new ServiceException("???????????????????????????");
			}
		} else {//????????????????????????????????????????????????
			sns = snService.list(Condition.getQueryWrapper(param));
			if (sns.size() > 0) {
				throw new ServiceException("???????????????????????????????????????");
			}
		}
		return true;
	}

	/**
	 * ??????-??????????????????
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
					throw new ServiceException("?????????????????????????????????????????????");
				}
				skuList.add(sku);
			}
		} else {
			throw new ServiceException("????????????????????????");
		}
		return skuList;
	}

	/**
	 * ???????????? ????????????
	 *
	 * @param asnBillNo
	 * @param skuCode
	 * @return
	 */
	@Override
	public Map<String, Object> getAsnHeaderDetail(String asnBillNo, String skuCode/*, String wspId*/) {
		Map<String, Object> result = new HashMap<>();

		//????????????????????????
		AsnDetail paramDetail = new AsnDetail();
		paramDetail.setSkuCode(UrlUtil.decode(skuCode, StringPool.UTF_8));

		paramDetail.setAsnBillNo(asnBillNo);
		List<AsnDetail> asnDetails = asnDetailService.list(Condition.getQueryWrapper(paramDetail)
			.lambda().ne(AsnDetail::getSurplusQty, 0).orderByAsc(AsnDetail::getAsnLineNo));

		//?????????????????????
		List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(paramDetail)
			.lambda().orderByAsc(AsnDetail::getAsnLineNo));

		//??????????????????
		if (0 >= asnDetails.size()) {
			result.put("msg", String.format("???????????????????????????[%s],?????????????????????", skuCode));
			result.put("success", false);
			return result;
		}

		//????????????
		Sku sku = SkuCache.getById(asnDetails.get(0).getSkuId());
		if (Func.isEmpty(sku)) {
			result.put("msg", String.format("?????????????????????[%s]?????????", asnDetails.get(0).getSkuId()));
			result.put("success", false);
			return result;
		}
//			throw new ServiceException(String.format("?????????????????????[%s]?????????",asnDetails.get(0).getSkuId()));

		//??????????????????????????????????????????
		AsnDetailMinVO minVO = this.getMinVoByBillId(asnDetailList);

		//????????????
		SkuPackage skuPackage = SkuPackageCache.getById(asnDetails.get(0).getWspId());
		if (Func.isEmpty(skuPackage)) {
			throw new ServiceException("??????????????????????????????");
		}
		//??????????????????
		List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
		if (skuPackageDetails.size() <= 0) {
			result.put("msg", String.format("??????[%s]??????????????????", skuPackage.getWspId()));
			result.put("success", false);
			return result;
		}
//			throw new ServiceException(String.format("??????[%s]??????????????????", skuPackage.getWspId()));


		//???????????????
		List<SkuLotConfigVO> skuConfigs = new ArrayList<>();
		skuConfigs = skuLotService.getConfig(asnDetails.get(0).getSkuId());
		if (skuConfigs.size() <= 0) {
			result.put("msg", String.format("??????[%s]?????????????????????", asnDetails.get(0).getSkuId()));
			result.put("success", false);
			return result;
		}
//			throw new ServiceException(String.format("??????[%s]?????????????????????", asnDetails.get(0).getSkuId()));


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
	 * ???????????? ??????AsnBillId??????AsnDetailMinVO
	 *
	 * @param list
	 * @return
	 */
	AsnDetailMinVO getMinVoByBillId(List<AsnDetail> list) {
		//?????????????????????????????????
		BigDecimal planCount = BigDecimal.ZERO;
		BigDecimal scanCount = BigDecimal.ZERO;
		int skuLevel = 0;
		AsnDetailMinVO minVO = new AsnDetailMinVO();
		int skuIsSn = 0;
		Long wspId = 0L;
		for (AsnDetail asnDetail : list) {
			Sku sku = SkuCache.getById(asnDetail.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException(String.format("??????[%s]????????????????????????", asnDetail.getSkuId()));
			}
			skuIsSn = sku.getIsSn();
			wspId = asnDetail.getWspId();
			planCount = planCount.add(asnDetail.getPlanQty());
			scanCount = scanCount.add(asnDetail.getScanQty());

			if (1 == skuIsSn) {
				skuLevel = 1;
			} else {
				if (asnDetail.getSkuLevel() > skuLevel) {
					//????????????
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

		//??????
		Collections.sort(list, Comparator.comparing(AsnDetail::getSkuLevel));

		List<Integer> temp = new ArrayList<>();
		List<AsnDetail> noList = list.stream().filter(// ????????????
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

		//????????????
		minVO.setDetailStatusName(DictCache.getValue("detail_status", list.get(list.size() - 1).getDetailStatus()));
		//????????????
		minVO.setSkuName(list.get(list.size() - 1).getSkuName());

		return minVO;
	}

	/**
	 * ???????????? ????????????????????? ??????????????????
	 *
	 * @param asnBillId
	 * @return
	 */
	@Override
	public AsnDetailMinVO getFinishAsnDetail(Long asnBillId) {
		// ??????????????????
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
				throw new ServiceException("????????????????????????(ID:" + asnBillId + ")");
			}
			// ??????????????????
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.ASN);
			systemProcParam.setDataType(DataTypeEnum.AsnBillNo);
			systemProcParam.setAction(ActionEnum.CANCEL);
			systemProcParam.setBillNo(asnHeader.getAsnBillNo());
			systemProcParam.setWhId(asnHeader.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);
			if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.NOT_RECEIPT.getCode())) {

			} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.PART.getCode())) {
				// ?????????????????????????????????
				List<ContainerLog> containerLogList = containerLogService.list(
					Condition.getQueryWrapper(new ContainerLog())
						.lambda()
						.eq(ContainerLog::getAsnBillId, asnBillId));
				// ????????????????????? ????????????
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
				throw new ServiceException(String.format("?????????%s ?????????????????????????????????????????????", asnHeader.getAsnBillNo()));
			} else {
				String asnBillState = DictCache.getValue(DictCodeConstant.ASN_BILL_STATE, asnHeader.getAsnBillState());
				throw new ServiceException(String.format(
					"?????????%s ????????????[%s] ??????????????????????????????", asnHeader.getAsnBillNo(), asnBillState));
			}
			// ????????????????????????
			taskService.closeTask(asnBillId, TaskTypeEnum.ALL);
			// ??????????????????
			this.updateAsnBillState(asnBillId, AsnBillStateEnum.CANCEL);
		}
		return true;
	}

	/**
	 * ?????????????????????
	 *
	 * @param asnBillId
	 * @param asnBillState
	 * @return
	 */
	@Override
	public synchronized AsnHeader updateAsnBillState(Long asnBillId, AsnBillStateEnum asnBillState) {
		AsnHeader asnHeader = super.getById(asnBillId);
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("??????ID???" + asnBillId + " ????????????");
		}
		// ????????????????????? = ?????????  ???????????????????????????????????????????????????
		if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("?????????" + asnHeader.getAsnBillNo() + " ?????????????????????????????????");
		}
		if (asnBillState.equals(AsnBillStateEnum.PART)) {
			// ????????????????????????
			if (Func.isEmpty(asnHeader.getActualArrivalDate())) {
				asnHeader.setActualArrivalDate(LocalDateTime.now());
			}
		} else if (asnBillState.equals(AsnBillStateEnum.COMPLETED)) {
			// ????????????????????????
			asnHeader.setFinishDate(LocalDateTime.now());
		}
		asnHeader.setAsnBillState(asnBillState.getCode());
		// ??????????????????????????????
		super.updateById(asnHeader);
		IAllotHeaderService allotHeaderService = SpringUtil.getBean(IAllotHeaderService.class);
//		if (AsnBillStateEnum.COMPLETED.equals(asnBillState)) {
//			// ?????????????????????
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
				// ???????????????????????????
				if (Func.isNotEmpty(asnHeader)) {
					snVO.setAsnBillNo(snExcel.getAsnBillNo());
				} else {
					validResult.addError("asnBillNo", "?????????[" + snExcel.getAsnBillNo() + "]?????????");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				}
				// ????????????????????????????????????
				if (!asnHeader.getAsnBillState().equals(AsnBillStateEnum.NOT_RECEIPT.getCode())) {
					validResult.addError("asnBillNo", "???????????????[" + snExcel.getAsnBillNo() + "] ????????????????????????????????????????????????");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				}

				List<Sn> snList = snService.list(Condition.getQueryWrapper(new Sn()).lambda()
					.eq(Sn::getSnDetailCode, snExcel.getSnDetailCode())
					.eq(Sn::getAsnBillId, asnHeader.getAsnBillId()));
				// ???????????????????????????
				if (Func.isNotEmpty(snList) && snList.size() > 0) {
					validResult.addError("snDetailCode", "?????????[" + snExcel.getSnDetailCode() + "]?????????");
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
					validResult.addError("asnBillNo", "?????????[" + snExcel.getAsnBillNo() + "]????????????[" + snExcel.getAsnLineNo() + "]");
					returnErrors(dataVerifyList, dataVerify, validResult);
					continue;
				}

				// ???????????????????????????
				int existCount = snService.count(Condition.getQueryWrapper(new Sn())
					.lambda().eq(Sn::getSnDetailCode, snVO.getSnDetailCode()));
				if (existCount > 0) {
					throw new ServiceException("?????????:" + snVO.getSnDetailCode() + " ????????????");
				}
				// ????????????????????????????????????????????????????????????
				Long snCount = dataList.stream().filter(u -> {
					return asnDetail.getAsnLineNo().equals(u.getAsnLineNo());
				}).count();
				// ??????????????????????????????????????????
				snCount += snService.count(Condition.getQueryWrapper(new Sn()).lambda()
					.eq(Sn::getAsnDetailId, asnDetail.getAsnDetailId()));
				if (snCount != asnDetail.getPlanQty().longValue()) {
					throw new ServiceException("??????????????????????????????[??????:" + asnDetail.getAsnLineNo() + "]?????????????????????????????????");
				}
				snVO.setAsnLineNo(snExcel.getAsnLineNo());

				Sku sku = SkuCache.getById(asnDetail.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("??????????????????????????????");
				}
				if (1 != sku.getIsSn()) {
					validResult.addError("skuName", "??????[" + sku.getSkuName() + "]?????????????????????");
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
			throw new ServiceException("???????????????????????????");
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
			ExcelUtil.export(response, "?????????", "??????????????????", asnHeaderExcels, AsnHeaderExcel.class);
		}
	}

	private List<AsnHeaderExcel> getAsnExportDTOList(List<AsnHeader> asnHeaderList) {
		List<AsnHeaderExcel> asnExportList = new ArrayList<>();
		List<Long> asnBillIdList = NodesUtil.toList(asnHeaderList, AsnHeader::getAsnBillId);
		// ???????????????????????????
		List<AsnDetailVO> asnDetailAll = AsnDetailWrapper.build().listVO(
			asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
				.lambda().in(AsnDetail::getAsnBillId, asnBillIdList)));

		//????????????????????????
		List<Long> woIdList = NodesUtil.toList(asnHeaderList, AsnHeader::getWoId);
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		List<Owner> ownerAll = ownerService.list().stream().filter(owner -> {
			return woIdList.contains(owner.getWoId());
		}).collect(Collectors.toList());

		//???????????????????????????
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

			// ??????????????????????????????
			Owner owner = ownerAll.stream().filter(item -> {
				return item.getWoId().equals(asnHeader.getWoId());
			}).findFirst().orElse(null);

			//?????????????????????????????????
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
			//??????
			if (Func.isNotEmpty(owner)) {
				asnExportDTO.setWoCode(owner.getOwnerCode());
				asnExportDTO.setWoName(owner.getOwnerName());
			}

			//????????????
			if (Func.isNotEmpty(warehouse)) {
				asnExportDTO.setWhCode(warehouse.getWhCode());
				asnExportDTO.setWhName(warehouse.getWhName());
			}

			//????????????
			BillType billType = BillTypeCache.getByCode(asnHeader.getBillTypeCd());
			if (Func.isNotEmpty(billType)) {
				asnExportDTO.setBillTypeCd(billType.getBillTypeName());
			}
			//????????????
			asnExportDTO.setInstoreType(DictCache.getValue(DictCodeConstant.INSTORE_TYPE, asnHeader.getInstoreType()));
			int maxLength = 1;
			if (Func.isNotEmpty(asnDetailList)) {
				maxLength = asnDetailList.size();
			}
			for (int i = 0; i < maxLength; i++) {
				AsnDetail asnDetail = i < asnDetailList.size() ? asnDetailList.get(i) : null;
				if (Func.isNotEmpty(asnDetail)) {
					// ???????????????????????????
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
					// ???????????????
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

			// ????????????
			IBillTypeService billTypeService = SpringUtil.getBean(IBillTypeService.class);
			List<BillType> billTypeAllList = billTypeService.list(Condition.getQueryWrapper(new BillType()).lambda()
				.eq(BillType::getIoType, "I")
				.in(BillType::getBillTypeName, asnBillTypeCodeList));
			// ????????????
			List<Dict> instoreTypeAllList = DictCache.list(DictCodeConstant.INSTORE_TYPE).stream().filter(u -> {
				return instoreTypeCodeList.contains(String.valueOf(u.getDictValue()));
			}).collect(Collectors.toList());
			// ??????
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			/*List<Owner> ownerAllList = ownerService.list().stream().filter(owner -> {
				return ownerCodeList.contains(owner.getOwnerCode());
			}).collect(Collectors.toList());*/
			List<Owner> ownerAllList = ownerService.list(Condition.getQueryWrapper(new Owner())
				.lambda()
				.in(Owner::getOwnerCode, ownerCodeList));
			// ??????
			/*List<Warehouse> warehouseAllList = WarehouseCache.list().stream().filter(u -> {
				return whCodeList.contains(u.getWhCode());
			}).collect(Collectors.toList());*/
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			List<Warehouse> warehouseAllList = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
				.lambda()
				.in(Warehouse::getWhCode, whCodeList));
			// ??????
			List<Dept> deptAllList = new ArrayList<>();
			if (Func.isNotEmpty(deptCodeList)) {
				deptAllList = deptService.list(Condition.getQueryWrapper(new Dept()).lambda()
					.in(Dept::getDeptCode, deptCodeList));
			}
			// ?????? - ????????????
			//List<Sku> skuAllList = SkuCache.listByCode(skuCodeList);
			ISkuService skuService = SpringUtil.getBean(ISkuService.class);
			List<Sku> skuAllList = skuService.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.in(Sku::getSkuCode, skuCodeList)
			).stream().collect(Collectors.toList());
			// ?????? - ????????????
			List<SkuPackage> skuPackageAllList = new ArrayList<>();
			// ?????? - ??????????????????
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
						errorList.add("???????????????????????????");
					}
					// ??????
					Owner owner = ownerAllList.stream().filter(u -> {
						return u.getOwnerCode().equals(asnHeaderExcel.getWoCode());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(owner)) {
						asnHeaderDTO.setWoId(owner.getWoId());
						asnHeaderDTO.setOwnerCode(owner.getOwnerCode());
					} else {
						errorList.add("???????????????????????????");
					}
					// ????????????
					BillType billType = billTypeAllList.stream().filter(u -> {
						return u.getBillTypeName().equals(asnHeaderExcel.getBillTypeCd());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(billType)) {
						asnHeaderDTO.setBillTypeCd(billType.getBillTypeCd());
					} else {
						errorList.add("???????????????????????????");
					}
					// ????????????
					Dict instoreType = instoreTypeAllList.stream().filter(u -> {
						return u.getDictValue().equals(asnHeaderExcel.getInstoreType());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(instoreType)) {
						asnHeaderDTO.setInstoreType(instoreType.getDictKey());
					} else {
						errorList.add("???????????????????????????");
					}
					// ??????
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

					// ????????????
					AsnDetailDTO asnDetail = BeanUtil.copy(asnHeaderExcel, AsnDetailDTO.class);
					if (Func.isEmpty(asnDetail)) {
						continue;
					}
					// ??????
					Sku sku = skuAllList.stream().filter(u -> {
						return u.getSkuCode().equals(asnHeaderExcel.getSkuCode());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(sku)) {
						asnDetail.setSkuId(sku.getSkuId());
						asnDetail.setSkuCode(sku.getSkuCode());
						asnDetail.setSkuName(sku.getSkuName());
					} else {
						errorList.add("???????????????????????????");
					}
					// ??????
					SkuPackage skuPackage = skuPackageAllList.stream().filter(u -> {
						return u.getWspName().equals(asnHeaderExcel.getWspName());
					}).findFirst().orElse(null);
					if (Func.isNotEmpty(skuPackage)) {
						asnDetail.setWspId(skuPackage.getWspId());
						// ????????????
						List<SkuPackageDetail> skuPackageDetailList = skuPackageDetailAllList.stream().filter(u -> {
							return u.getWspId().equals(skuPackage.getWspId());
						}).collect(Collectors.toList());
						// ????????????
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
							errorList.add("???????????????????????????");
						}
						// ??????????????????
						SkuPackageDetail basePackageDetail = skuPackageDetailList.stream().filter(u -> {
							return u.getSkuLevel().equals(SkuLevelEnum.Base.getIndex());
						}).findFirst().orElse(null);
						if (Func.isNotEmpty(basePackageDetail)) {
							asnDetail.setBaseUmCode(basePackageDetail.getWsuCode());
							asnDetail.setBaseUmName(basePackageDetail.getWsuName());
						} else {
							errorList.add("???????????????????????????????????????");
						}
					} else {
						errorList.add("???????????????????????????");
					}
					// ??????
					asnDetail.setScanQty(BigDecimal.ZERO);
					if (Func.isNotEmpty(asnDetail.getPlanQty())) {
						asnDetail.setSurplusQty(asnDetail.getPlanQty().subtract(asnDetail.getScanQty()));
					}
					if (Func.isEmpty(asnHeaderDTO.getAsnDetailList())) {
						asnHeaderDTO.setAsnDetailList(new ArrayList<>());
					}
					asnHeaderDTO.getAsnDetailList().add(asnDetail);
				} else {
					errorList.add(String.format("?????????[%s]?????????", asnHeaderExcel.getAsnBillNo()));
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
						// ???????????????map???
						cache.put(cacheKey, asnHeaderDTO);
					}
					dataVerify.setCacheKey(cacheKey);
				}
				dataVerifyList.add(dataVerify);
				index++;
			}
			if (Func.isNotEmpty(cache)) {
				// ???????????????redis?????????
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
			throw new ServiceException("???????????????????????????");
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
//				//?????????
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
					//????????????id????????????????????????(????????????)
					List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(asnDetail.getWspId())
						.stream()
						.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
						.collect(Collectors.toList());

					for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
						if (skuPackageDetail.getWsuCode().equals(asnDetail.getUmCode())) {
							//????????????
							asnDetail.setSkuLevel(Integer.parseInt(skuPackageDetail.getSkuLevel().toString()));//????????????
							//????????????
							asnDetail.setConvertQty(Integer.parseInt(skuPackageDetail.getConvertQty().toString()));
						}
						if (1 == Integer.parseInt(skuPackageDetail.getSkuLevel().toString())) {
							//????????????????????????
							asnDetail.setBaseUmCode(skuPackageDetail.getWsuCode());
							//????????????????????????
							asnDetail.setBaseUmName(skuPackageDetail.getWsuName());
						}
					}
					//????????????
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
	 * ??????????????????
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
	 * ??????????????????????????????
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
		// ?????? ?????????????????? ????????????
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
		// ?????? ?????????????????? ????????????
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
		// ?????? ?????????????????? ????????????
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
			throw new ServiceException(String.format("??????[%s] ????????????????????????", asnDetail.getSkuId()));
		}
		BigDecimal surplus_qty = asnDetail.getPlanQty();
		BigDecimal convert_qty = BigDecimal.ZERO;
		if (Func.isEmpty(skuPackageDetail)) {
			convert_qty = asnDetail.getPlanQty();
		} else {
			convert_qty = new BigDecimal(skuPackageDetail.getConvertQty());
		}
		// ????????????????????????????????????
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
