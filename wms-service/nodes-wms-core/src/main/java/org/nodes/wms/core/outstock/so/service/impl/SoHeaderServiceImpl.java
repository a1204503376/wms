package org.nodes.wms.core.outstock.so.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.allot.enums.AllotBillStateEnum;
import org.nodes.wms.core.allot.service.IAllotHeaderService;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuIncService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.cache.SoCache;
import org.nodes.wms.core.outstock.so.dto.SoDetailDTO;
import org.nodes.wms.core.outstock.so.dto.SoHeaderDTO;
import org.nodes.wms.core.outstock.so.dto.SoHeaderQueryDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.enums.ShipStateEnum;
import org.nodes.wms.core.outstock.so.mapper.SoHeaderMapper;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.nodes.wms.core.outstock.so.wrapper.SoDetailWrapper;
import org.nodes.wms.core.outstock.so.wrapper.SoHeaderWrapper;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.dto.StockSubtractDTO;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SyncStateEnum;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ??????????????? ???????????????
 *
 * @author zhonglianshuai
 * @since 2020-02-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SoHeaderServiceImpl<M extends SoHeaderMapper, T extends SoHeader>
	extends BaseServiceImpl<SoHeaderMapper, SoHeader>
	implements ISoHeaderService {

	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	IWellenDetailService wellenDetailService;
	@Autowired
	ITaskService taskService;
	@Autowired
	IStockService stockService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	IOutstockService outstockService;
	@Autowired
	ISkuIncService skuIncService;
	@Autowired
	ISystemProcService systemProcService;

	@Override
	public List<SoHeaderVO> selectList(SoHeaderQueryDTO soHeaderQuery) {
		List<SoHeader> list = super.list(this.getQueryWrapper(soHeaderQuery));
		return SoHeaderWrapper.build().listVO(list);
	}

	@Override
	public IPage<SoHeaderVO> selectPage(SoHeaderQueryDTO soHeaderQuery, Query query) {
		if (Func.isNull(query.getAscs())) {
			query.setDescs("createTime");
		}
		IPage<SoHeader> page = super.page(Condition.getPage(query), this.getQueryWrapper(soHeaderQuery));
		return SoHeaderWrapper.build().pageVO(page);
	}

	@Override
	public SoHeaderVO getDetail(Long soBillId) {
		// ???????????????????????????
		SoHeader soHeader = super.getById(soBillId);
		if (Func.isEmpty(soHeader)) {
			throw new ServiceException("??????[ID:" + soBillId + "]?????????! ");
		}
		// ???????????????????????????
		List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
			.lambda()
			.eq(SoDetail::getSoBillId, soBillId));
		SoHeaderVO soHeaderVO = SoHeaderWrapper.build().entityVO(soHeader);
		if (Func.isNotEmpty(soHeaderVO)) {
			soHeaderVO.setDetailList(SoDetailWrapper.build().listVO(soDetailList));
		}

		return soHeaderVO;
	}


	public boolean saveByAllot(SoHeaderDTO soHeader) {
		if (Func.isEmpty(soHeader.getSoBillNo())) {
			soHeader.setSoBillNo(SoCache.getSoBillNo());
		}
		soHeader.setSoBillState(SoBillStateEnum.CREATE.getCode());
		soHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		soHeader.setShipState(ShipStateEnum.DEFAULT.getIndex());
		this.fillSaveData(soHeader);
		boolean result = super.save(soHeader);
		//???????????????
		if (Func.isEmpty(soHeader.getBillKey())) {
			//??????????????????????????????
			soHeader.setBillKey(Func.toStr(soHeader.getSoBillId()));
			//??????????????????????????????
			soHeader.setLastUpdateDate(LocalDateTime.now());
			//??????????????????????????????
			soHeader.setPreCreateDate(LocalDateTime.now());
			//??????????????????????????????
			result = super.updateById(soHeader);
		}
		// ???????????????
		List<SoDetailDTO> details = soHeader.getDetailList();
		for (SoDetailDTO detail : details) {
			detail.setSoBillId(soHeader.getSoBillId());
			detail.setSoBillNo(soHeader.getSoBillNo());
			detail.setBillTypeCd(soHeader.getBillTypeCd());
			soDetailService.saveByAllot(detail);
		}
		return result;
	}

	@Override
	public boolean save(SoHeaderDTO soHeader) {
		if (Func.isEmpty(soHeader.getSoBillNo())) {
			soHeader.setSoBillNo(SoCache.getSoBillNo());
		}
		soHeader.setSoBillState(SoBillStateEnum.CREATE.getCode());
		soHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		soHeader.setShipState(ShipStateEnum.DEFAULT.getIndex());
		this.fillSaveData(soHeader);
		soHeader.setCreateDept(soHeader.getDeptId());
		boolean result = super.save(soHeader);
		//???????????????
		if (Func.isEmpty(soHeader.getBillKey())) {
			//??????????????????????????????
			soHeader.setBillKey(Func.toStr(soHeader.getSoBillId()));
			//??????????????????????????????
			soHeader.setLastUpdateDate(LocalDateTime.now());
			//??????????????????????????????
			soHeader.setPreCreateDate(LocalDateTime.now());
			//??????????????????????????????
			result = super.updateById(soHeader);
		}
		// ???????????????
		List<SoDetailDTO> details = soHeader.getDetailList();
		for (SoDetailDTO detail : details) {
			detail.setSoBillId(soHeader.getSoBillId());
			detail.setSoBillNo(soHeader.getSoBillNo());
			detail.setBillTypeCd(soHeader.getBillTypeCd());
			detail.setCreateDept(soHeader.getDeptId());
			soDetailService.save(detail);
		}
		return result;
	}

	@Override
	public boolean updateById(SoHeaderDTO soHeader) {
		this.fillSaveData(soHeader);
		soHeader.setCreateDept(soHeader.getDeptId());
		boolean result = super.updateById(soHeader);
		if (result) {
			// ???????????????
			List<SoDetailDTO> details = soHeader.getDetailList();
			for (SoDetailDTO detail : details) {
				detail.setSoBillId(soHeader.getSoBillId());
				detail.setSoBillNo(soHeader.getSoBillNo());
				detail.setBillTypeCd(soHeader.getBillTypeCd());
				detail.setCreateDept(soHeader.getDeptId());
				soDetailService.saveOrUpdate(detail);
			}
		}
		return result;
	}

	private void fillSaveData(SoHeaderDTO soHeader) {
		IDeptService deptService = SpringUtil.getBean(IDeptService.class);
		if (Func.isEmpty(soHeader.getDetailList())) {
			throw new ServiceException("????????????????????????");
		}
		//????????????
		Warehouse warehouse = WarehouseCache.getById(soHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("??????????????????????????????");
		}
		soHeader.setWhCode(warehouse.getWhCode());
		//????????????
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(soHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("??????????????????????????????");
		}
		soHeader.setOwnerCode(owner.getOwnerCode());
		//??????????????????????????????
		if (Func.isEmpty(soHeader.getBillKey())) {
			soHeader.setBillKey(soHeader.getSoBillNo());
		}
		//???????????????
		soHeader.setBillCreator(AuthUtil.getNickName());
		//??????
		if (Func.isNotEmpty(soHeader.getDeptId())) {
			Dept dept = deptService.getById(Long.valueOf(soHeader.getDeptId()));
			if (Func.isEmpty(dept)) {
				throw new ServiceException("??????????????????????????????");
			}
			soHeader.setDeptCode(dept.getDeptCode());//????????????
			soHeader.setDeptName(dept.getDeptName());//????????????
		}
	}

	@Override
	public boolean saveOrUpdate(SoHeaderDTO soHeader) {

		if (Func.isEmpty(soHeader.getSoBillId())) {
			return this.save(soHeader);
		} else {
			return this.updateById(soHeader);
		}
	}

	@Override
	public boolean saveOrUpdateByAllot(SoHeaderDTO soHeader) {
		if (Func.isEmpty(soHeader.getSoBillId())) {
			return this.saveByAllot(soHeader);
		} else {
			return this.updateById(soHeader);
		}
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		List<SoHeader> soHeaderList = super.listByIds(idList);
		for (SoHeader soHeader : soHeaderList) {
			if (Func.isEmpty(soHeader)) {
				throw new ServiceException("??????????????????");
			}
			if (!SoBillStateEnum.CREATE.getCode().equals(soHeader.getSoBillState())) {
				throw new ServiceException(String.format("??????[%s]???%s??????????????????",
					soHeader.getSoBillNo(), DictCache.getValue(DictCodeConstant.SO_BILL_STATE, soHeader.getSoBillState())));
			}
			if (soHeader.getBillTypeCd().equals("209")) {
				throw new ServiceException("???????????????????????????! ");
			}

		}
		boolean result = super.removeByIds(idList);
		if (result) {
			// ????????????
			result = soDetailService.remove(Condition.getQueryWrapper(new SoDetail())
				.lambda()
				.in(SoDetail::getSoBillId, idList));
		}
		return result;
	}


	@Override
	public SoHeader updateBillState(Long billId, SoBillStateEnum soBillState, boolean isCompel) {
		SoHeader soHeader = super.getById(billId);
		if (Func.isEmpty(soHeader)) {
			throw new ServiceException("??????ID???" + billId + " ????????????");
		}
		return updateBillState(soHeader, soBillState, isCompel);
	}

	@Override
	public synchronized SoHeader updateBillState(SoHeader soHeader, SoBillStateEnum soBillState, boolean isCompel) {
		if (soHeader.getSoBillState().equals(soBillState)) {
			return soHeader;
		}
		// ????????????????????? = ?????????  ???????????????????????????????????????????????????
		if (soHeader.getSoBillState().equals(SoBillStateEnum.CANCELED.getCode())) {
			throw new ServiceException("?????????" + soHeader.getSoBillNo() + " ?????????????????????????????????");
		}
		if (SoBillStateEnum.COMPLETED.getCode().equals(soBillState.getCode())) {
			if (!isCompel) {
				// ????????????????????????????????????????????????????????????
				List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
					.eq(SoDetail::getSoBillId, soHeader.getSoBillId())
					.ne(SoDetail::getSurplusQty, BigDecimal.ZERO));
				if (Func.isNotEmpty(soDetailList)) {
					//Zone pickZone = ZoneCache.getByCode(ZoneVirtualTypeEnum.Pick.toString() + soHeader.getWhCode());
					IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
					Zone pickZone = zoneService.list(Condition.getQueryWrapper(new Zone())
						.lambda()
						.eq(Zone::getZoneCode, ZoneVirtualTypeEnum.PICK.toString() + soHeader.getWhCode())
					).stream().findFirst().orElse(null);

					for (SoDetail soDetail : soDetailList) {

						int count = stockService.count(Condition.getQueryWrapper(new Stock()).lambda()
							.eq(Stock::getWhId, soHeader.getWhId())
							.eq(Stock::getSkuId, soDetail.getSkuId())
							.eq(Stock::getWspId, soDetail.getWspId())
							.eq(Stock::getStockStatus, 0)
							.func(sql -> {
								if (Func.isNotEmpty(pickZone)) {
									sql.ne(Stock::getZoneId, pickZone.getZoneId());
								}
								for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
									if (soDetail.skuLotChk(i)) {
										sql.apply("sku_lot" + i, soDetail.skuLotGet(i));
									}
								}
								sql.apply("stock_qty <> pick_qty");
							}));
						if (count > 0) {
							return soHeader;
						}
					}
				}
			}
			soHeader.setFinishDate(LocalDateTime.now());
		}
		soHeader.setSoBillState(soBillState.getCode());
		// ??????????????????????????????
		super.updateById(soHeader);
		IAllotHeaderService allotHeaderService = SpringUtil.getBean(IAllotHeaderService.class);
		if (SoBillStateEnum.COMPLETED.equals(soBillState)) {
			// ?????????????????????
			allotHeaderService.updateBillState(soHeader.getOrderNo(), AllotBillStateEnum.UN_INSTOCK);
		} else if (SoBillStateEnum.EXECUTING.equals(soBillState)) {
			allotHeaderService.updateBillState(soHeader.getOrderNo(), AllotBillStateEnum.OUTSTOCKING);
		}
		return soHeader;
	}

	@Override
	public boolean canEdit(Long soHeaderId) {
		SoHeader soHeader = super.getById(soHeaderId);
		if (Func.isEmpty(soHeader)) {
			throw new ServiceException("??????????????????????????????ID???" + soHeaderId + " ???");
		}
		// ????????????????????????
		Param param = ParamCache.getOne(ParamEnum.COUNT_LOSS_TYPECD.getKey());
		if (soHeader.getBillTypeCd().equals(param.getParamValue())) {
			throw new ServiceException("?????????????????????????????????! ");
		}
		if (soHeader.getBillTypeCd().equals("209")) {
			throw new ServiceException("??????????????????????????????! ");
		}
		return SoBillStateEnum.CREATE.getCode().equals(soHeader.getSoBillState());
//			|| SoBillStateEnum.EXECUTING.getIndex().equals(soHeader.getSoBillState())
//			|| SoBillStateEnum.PART.getIndex().equals(soHeader.getSoBillState());
	}

	@Override
	public boolean cancel(List<Long> idList) {
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		for (Long soBillId : idList) {
			SoHeader soHeader = super.getById(soBillId);
			if (Func.isEmpty(soHeader)) {
				throw new ServiceException("????????????????????????(ID: " + soBillId + ")???");
			}
			if (soHeader.getSoBillState().equals(SoBillStateEnum.CREATE.getCode())) {
				// ???????????????????????????
			} else if (soHeader.getSoBillState().equals(SoBillStateEnum.EXECUTING.getCode())) {
				// 1. ?????? ????????????
				// 1.1. ?????????????????????????????????
				Map<Long, List<WellenDetail>> wellenDetailMap = wellenDetailService.listByBillId(soBillId).stream()
					.collect(Collectors.groupingBy(WellenDetail::getSoBillId));
				if (wellenDetailMap.size() == 1) {
					// 1.1.1. ????????????????????????????????????????????????????????????????????????????????????
					List<WellenDetail> wellenDetailList = wellenDetailMap.get(soBillId);
					if (Func.isEmpty(wellenDetailList)) {
						throw new ServiceException("???????????????????????????????????????");
					}
					Long wellenId = wellenDetailList.get(0).getWellenId();
					Task task = taskService.getOne(Condition.getQueryWrapper(new Task()).lambda()
						.eq(Task::getBillId, wellenId)
						.eq(Task::getTaskTypeCd, TaskTypeEnum.Pick.getIndex()));
					if (Func.isNotEmpty(task)) {
						taskService.close(task.getTaskId().toString());
					}
					// 2. ??????(???????????????)????????????
					StockOccupySubtractDTO stockOccupyReduceDTO = new StockOccupySubtractDTO();
					stockOccupyReduceDTO.setTransId(wellenId);
					stockOccupyReduceDTO.setOccupyType(StockOccupyTypeEnum.PickPlan.getIndex());
					stockOccupyReduceDTO.setSoBillId(soBillId);
					stockOccupyService.subtract(stockOccupyReduceDTO);
				}
				// 1.1.2. ?????????????????????????????????????????????(??????????????????????????????????????????)
			} else if (soHeader.getSoBillState().equals(SoBillStateEnum.COMPLETED.getCode())) {
				// ??????????????????????????????????????????
				List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
					.lambda()
					.eq(StockDetail::getSoBillId, soBillId));
				if (Func.isEmpty(stockDetailList)) {
					throw new ServiceException("?????????" + soHeader.getSoBillNo() + " ??????????????????????????????????????????");
				}
				BigDecimal stock_qty = stockDetailList.stream().map(StockDetail::getStockQty)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal pick_qty = stockDetailList.stream().map(StockDetail::getPickQty)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
					.eq(SoDetail::getSoBillId, soBillId));
				BigDecimal scan_qty = soDetailList.stream().map(SoDetail::getScanQty)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				if (BigDecimalUtil.ne(stock_qty.subtract(pick_qty), scan_qty)) {
					throw new ServiceException("?????????" + soHeader.getSoBillNo() + " ????????????????????????????????????????????????");
				}
			} else {
				// ?????????????????????????????????????????????
				throw new ServiceException(
					"??????????????????(" + soHeader.getSoBillNo() + ") ??????(" +
						DictCache.getValue(DictCodeConstant.SO_BILL_STATE, soHeader.getSoBillState()) + ") ??????????????????");
			}
			// ???????????????????????????
			taskService.closeTask(soBillId, TaskTypeEnum.ALL);
			this.updateBillState(soBillId, SoBillStateEnum.CANCELED, true);
		}

		return true;
	}

	private QueryWrapper<SoHeader> getQueryWrapper(SoHeaderQueryDTO soHeaderQuery) {
		QueryWrapper<SoHeader> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
			.func(sql -> {
				if (Func.isNotEmpty(soHeaderQuery.getSoBillNo())) {
					sql.like(SoHeader::getSoBillNo, soHeaderQuery.getSoBillNo());
				}
				if (Func.isNotEmpty(soHeaderQuery.getOrderNo())) {
					sql.like(SoHeader::getOrderNo, soHeaderQuery.getOrderNo());
				}
				if (Func.isNotEmpty(soHeaderQuery.getCName())) {
					sql.and(c_keyword -> {
						c_keyword
							.or().like(SoHeader::getCustomerCode, soHeaderQuery.getCName())
							.or().like(SoHeader::getCustomerName, soHeaderQuery.getCName());
					});
				}
				if (Func.isNotEmpty(soHeaderQuery.getBillTypeCd())) {
					sql.eq(SoHeader::getBillTypeCd, soHeaderQuery.getBillTypeCd());
				}
				if (Func.isNotEmpty(soHeaderQuery.getSoBillState())) {
					sql.eq(SoHeader::getSoBillState, soHeaderQuery.getSoBillState());
				}
				if (Func.isNotEmpty(soHeaderQuery.getSyncState())) {
					sql.eq(SoHeader::getSyncState, soHeaderQuery.getSyncState());
				}
				if (Func.isNotEmpty(soHeaderQuery.getPostState())) {
					sql.eq(SoHeader::getPostState, soHeaderQuery.getPostState());
				}
				if (Func.isNotEmpty(soHeaderQuery.getPostTime())) {
					sql.apply("DATE_FORMAT(post_time, '%Y-%m-%d') = DATE_FORMAT({0}, '%Y-%m-%d')",
						soHeaderQuery.getPostTime());
				}
				if (Func.isNotEmpty(soHeaderQuery.getPostUser())) {
					sql.eq(SoHeader::getPostUser, soHeaderQuery.getPostUser());
				}
				if (Func.isNotEmpty(soHeaderQuery.getWoId())) {
					sql.eq(SoHeader::getWoId, soHeaderQuery.getWoId());
				}
				if (Func.isNotEmpty(soHeaderQuery.getWhId())) {
					sql.eq(SoHeader::getWhId, soHeaderQuery.getWhId());
				}
				if (Func.isNotEmpty(soHeaderQuery.getDeptId())) {
					sql.eq(SoHeader::getDeptId, soHeaderQuery.getDeptId());
				}
				if (Func.isNotEmpty(soHeaderQuery.getExpressName())) {
					sql.like(SoHeader::getExpressName, soHeaderQuery.getExpressName());
				}
			});
		return queryWrapper;
	}

	@Override
	public boolean completed(List<Long> idList) {
		// ???????????????
		List<SoHeader> soHeaderList = super.listByIds(idList);
		// ???????????????????????????????????????????????????
		IStockOccupyService stockOccupyService = SpringUtil.getBean(IStockOccupyService.class);
		List<StockOccupy> stockOccupyList = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
			.lambda());
//			.in(StockOccupy::getSoBillId, idList));
		if (Func.isNotEmpty(soHeaderList)) {
			for (SoHeader soHeader : soHeaderList) {
				// ????????????????????????
				if (SoBillStateEnum.COMPLETED.getCode().equals(soHeader.getSoBillState())) {
					continue;
				}
				// ????????????????????????????????????????????????
				if (SoBillStateEnum.CANCELED.getCode().equals(soHeader.getSoBillState())) {
					throw new ServiceException(String.format("?????????[%s] %s??????????????????????????????",
						soHeader.getSoBillNo(), SoBillStateEnum.valueOf(soHeader.getSoBillState())));
				}
				List<StockOccupy> occupyList = stockOccupyList.stream().filter(u -> {
//					return soHeader.getSoBillId().equals(u.getSoBillId());
					return true;
				}).collect(Collectors.toList());
				if (Func.isNotEmpty(occupyList)) {
					throw new ServiceException(String.format("?????????[%s] ??????????????????????????????????????????????????????????????????! ",
						soHeader.getSoBillNo()));
				}
				this.updateBillState(soHeader, SoBillStateEnum.COMPLETED, true);
			}
		}
		return true;
	}

	@Override
	public boolean completedOutstock(List<Long> idList) {
		// ???????????????
		List<SoHeader> soHeaderList = super.listByIds(idList);
		// ???????????????????????????????????????????????????
		IStockOccupyService stockOccupyService = SpringUtil.getBean(IStockOccupyService.class);
		List<StockOccupy> stockOccupyList = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
				.lambda()
//			.in(StockOccupy::getSoBillId, idList)
		);
		// ???????????????????????????
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.in(StockDetail::getSoBillId, idList));
		// ????????????????????????
		List<SoDetail> soDetailListAll = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
			.in(SoDetail::getSoBillId, idList));
		for (SoHeader soHeader : soHeaderList) {
			// ?????????????????????????????????????????????
//			if (!SoBillStateEnum.COMPLETED.getIndex().equals(soHeader.getSoBillState())) {
//				throw new ServiceException(
//					String.format("?????????[%s] ?????????????????????????????????????????????????????????", soHeader.getSoBillNo()));
//			}
			// ????????????????????????????????????????????????
			if (SoBillStateEnum.CANCELED.getCode().equals(soHeader.getSoBillState())) {
				throw new ServiceException(String.format("?????????[%s] %s?????????????????????????????????",
					soHeader.getSoBillNo(), SoBillStateEnum.valueOf(soHeader.getSoBillState())));
			}
			if (ShipStateEnum.SENDED.getIndex().equals(soHeader.getShipState())) {
				throw new ServiceException(String.format("?????????[%s] ?????????????????????????????????????????????", soHeader.getSoBillNo()));
			}
			List<StockOccupy> occupyList = stockOccupyList.stream().filter(u -> {
//				return soHeader.getSoBillId().equals(u.getSoBillId());
				return true;
			}).collect(Collectors.toList());
			if (Func.isNotEmpty(occupyList)) {
				throw new ServiceException(String.format("?????????[%s] ??????????????????????????????????????????????????????????????????! ",
					soHeader.getSoBillNo()));
			}
			this.updateBillState(soHeader, SoBillStateEnum.COMPLETED, true);
			// ??????????????????
			taskService.closeTask(soHeader.getSoBillId(), TaskTypeEnum.FillLpn);
			// ??????????????????
			UpdateWrapper<SoHeader> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				.set(SoHeader::getShipState, ShipStateEnum.SENDED.getIndex())
				.eq(SoHeader::getSoBillId, soHeader.getSoBillId());
			super.update(updateWrapper);
			// ??????????????????
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.COMPLATED_OUTSTOCK);
			systemProcParam.setDataType(DataTypeEnum.SO_BILL_NO);
			systemProcParam.setAction(ActionEnum.COMPLATED_OUTSTOCK);
			systemProcParam.setBillNo(soHeader.getSoBillNo());
			Long systemProcId = systemProcService.create(systemProcParam).getSystemProcId();
			// ????????????

			List<SoDetail> soDetailList = soDetailListAll.stream().filter(soDetail -> {
				return soDetail.getSoBillId().equals(soHeader.getSoBillId());
			}).collect(Collectors.toList());
			if (Func.isEmpty(soDetailList)) {
				continue;
			}
			for (SoDetail soDetail : soDetailList) {

				stockDetailList.stream().filter(u -> {
					return soDetail.getSoDetailId().equals(u.getBillDetailId());
				}).forEach(stockDetail -> {
					if (Func.isEmpty(stockDetail)) {
						return;
					}
					if (BigDecimalUtil.ne(stockDetail.getPickQty(), BigDecimal.ZERO)) {
						// ?????????????????????????????????
						UpdateWrapper<SoDetail> soDetailUpdateWrapper = new UpdateWrapper<>();
						soDetailUpdateWrapper.lambda()
							.set(SoDetail::getScanQty, soDetail.getScanQty().subtract(stockDetail.getPickQty()))
							.set(SoDetail::getSurplusQty, soDetail.getSurplusQty().add(stockDetail.getPickQty()))
							.eq(SoDetail::getSoDetailId, soDetail.getSoDetailId());
						soDetailService.update(soDetailUpdateWrapper);
					}
					StockSubtractDTO stockReduceDTO = new StockSubtractDTO();
					stockReduceDTO.setStockId(stockDetail.getStockId());
					stockReduceDTO.setSkuId(soDetail.getSkuId());
					stockReduceDTO.setPickQty(stockDetail.getStockQty().subtract(stockDetail.getPickQty()));
					stockReduceDTO.setEventType(EventTypeEnum.LoadingStock);
					stockReduceDTO.setSystemProcId(systemProcId);
					stockService.subtract(stockReduceDTO);
				});
			}
		}
		return true;
	}
}
