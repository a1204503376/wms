
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
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.enums.SyncStateEnum;
import org.nodes.wms.core.outstock.so.mapper.SoHeaderMapper;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.nodes.wms.core.outstock.so.wrapper.SoDetailWrapper;
import org.nodes.wms.core.outstock.so.wrapper.SoHeaderWrapper;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.dto.StockSubtractDTO;
import org.nodes.wms.dao.stock.entities.Stock;
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
 * 出库单头表 服务实现类
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
		// 获取出库单表头信息
		SoHeader soHeader = super.getById(soBillId);
		if (Func.isEmpty(soHeader)) {
			throw new ServiceException("订单[ID:" + soBillId + "]不存在! ");
		}
		// 获取出库单明细信息
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
		soHeader.setSoBillState(SoBillStateEnum.CREATE.getIndex());
		soHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		soHeader.setShipState(ShipStateEnum.DEFAULT.getIndex());
		this.fillSaveData(soHeader);
		boolean result = super.save(soHeader);
		//无单据出库
		if (Func.isEmpty(soHeader.getBillKey())) {
			//上位系统单据唯一标识
			soHeader.setBillKey(Func.toStr(soHeader.getSoBillId()));
			//上位系统最后更新时间
			soHeader.setLastUpdateDate(LocalDateTime.now());
			//上位系统订单创建时间
			soHeader.setPreCreateDate(LocalDateTime.now());
			//更新上位系统默认信息
			result = super.updateById(soHeader);
		}
		// 保存明细表
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
		soHeader.setSoBillState(SoBillStateEnum.CREATE.getIndex());
		soHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		soHeader.setShipState(ShipStateEnum.DEFAULT.getIndex());
		this.fillSaveData(soHeader);
		soHeader.setCreateDept(soHeader.getDeptId());
		boolean result = super.save(soHeader);
		//无单据出库
		if (Func.isEmpty(soHeader.getBillKey())) {
			//上位系统单据唯一标识
			soHeader.setBillKey(Func.toStr(soHeader.getSoBillId()));
			//上位系统最后更新时间
			soHeader.setLastUpdateDate(LocalDateTime.now());
			//上位系统订单创建时间
			soHeader.setPreCreateDate(LocalDateTime.now());
			//更新上位系统默认信息
			result = super.updateById(soHeader);
		}
		// 保存明细表
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
			// 保存明细行
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
			throw new ServiceException("请创建出库单明细");
		}
		//库房编码
		Warehouse warehouse = WarehouseCache.getById(soHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或已经删除");
		}
		soHeader.setWhCode(warehouse.getWhCode());
		//货主编码
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(soHeader.getWoId());
		if (Func.isEmpty(owner)) {
			throw new ServiceException("货主不存在或已经删除");
		}
		soHeader.setOwnerCode(owner.getOwnerCode());
		//上位系统单据唯一标识
		if (Func.isEmpty(soHeader.getBillKey())) {
			soHeader.setBillKey(soHeader.getSoBillNo());
		}
		//创建人姓名
		soHeader.setBillCreator(AuthUtil.getNickName());
		//部门
		if (Func.isNotEmpty(soHeader.getDeptId())) {
			Dept dept = deptService.getById(Long.valueOf(soHeader.getDeptId()));
			if (Func.isEmpty(dept)) {
				throw new ServiceException("机构不存在或已经删除");
			}
			soHeader.setDeptCode(dept.getDeptCode());//机构编码
			soHeader.setDeptName(dept.getDeptName());//机构名称
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
				throw new ServiceException("单据不存在！");
			}
			if (!SoBillStateEnum.CREATE.getIndex().equals(soHeader.getSoBillState())) {
				throw new ServiceException(String.format("单据[%s]为%s状态不可删除",
					soHeader.getSoBillNo(), DictCache.getValue(DictCodeConstant.SO_BILL_STATE,soHeader.getSoBillState())));
			}
			if (soHeader.getBillTypeCd().equals("209")) {
				throw new ServiceException("调拨发货单不可删除! ");
			}

		}
		boolean result = super.removeByIds(idList);
		if (result) {
			// 删除明细
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
			throw new ServiceException("订单ID：" + billId + " 不存在！");
		}
		return updateBillState(soHeader, soBillState, isCompel);
	}

	@Override
	public synchronized SoHeader updateBillState(SoHeader soHeader, SoBillStateEnum soBillState, boolean isCompel) {
		if (soHeader.getSoBillState().equals(soBillState)) {
			return soHeader;
		}
		// 当订单状态已经 = 已取消  则不允许再进行任何修改订单状态操作
		if (soHeader.getSoBillState().equals(SoBillStateEnum.CANCEL.getIndex())) {
			throw new ServiceException("订单：" + soHeader.getSoBillNo() + " 已取消，拒绝当前操作！");
		}
		if (SoBillStateEnum.COMPLETED.getIndex().equals(soBillState.getIndex())) {
			if (!isCompel) {
				// 如果还有未分配的库存信息，不更新订单状态
				List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
					.eq(SoDetail::getSoBillId, soHeader.getSoBillId())
					.ne(SoDetail::getSurplusQty, BigDecimal.ZERO));
				if (Func.isNotEmpty(soDetailList)) {
					//Zone pickZone = ZoneCache.getByCode(ZoneVirtualTypeEnum.Pick.toString() + soHeader.getWhCode());
					IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
					Zone pickZone = zoneService.list(Condition.getQueryWrapper(new Zone())
					.lambda()
					.eq(Zone::getZoneCode,ZoneVirtualTypeEnum.Pick.toString() + soHeader.getWhCode())
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
		soHeader.setSoBillState(soBillState.getIndex());
		// 修改订单主表订单状态
		super.updateById(soHeader);
		IAllotHeaderService allotHeaderService = SpringUtil.getBean(IAllotHeaderService.class);
		if (SoBillStateEnum.COMPLETED.equals(soBillState)) {
			// 更新调拨单状态
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
			throw new ServiceException("出库单不存在！（订单ID：" + soHeaderId + " ）");
		}
		// 盘盈单不允许编辑
		Param param = ParamCache.getOne(ParamEnum.COUNT_LOSS_TYPECD.getKey());
		if (soHeader.getBillTypeCd().equals(param.getParamValue())) {
			throw new ServiceException("当前单据类型不允许编辑! ");
		}
		if (soHeader.getBillTypeCd().equals("209")) {
			throw new ServiceException("调拨发货单不允许编辑! ");
		}
		return SoBillStateEnum.CREATE.getIndex().equals(soHeader.getSoBillState());
//			|| SoBillStateEnum.EXECUTING.getIndex().equals(soHeader.getSoBillState())
//			|| SoBillStateEnum.PART.getIndex().equals(soHeader.getSoBillState());
	}

	@Override
	public boolean cancel(List<Long> idList) {
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		for (Long soBillId : idList) {
			SoHeader soHeader = super.getById(soBillId);
			if (Func.isEmpty(soHeader)) {
				throw new ServiceException("指定出库单不存在(ID: " + soBillId + ")！");
			}
			if (soHeader.getSoBillState().equals(SoBillStateEnum.CREATE.getIndex())) {
				// 暂时无其他业务处理
			} else if (soHeader.getSoBillState().equals(SoBillStateEnum.EXECUTING.getIndex())) {
				// 1. 关闭 拣货任务
				// 1.1. 先获取该订单的波次信息
				Map<Long, List<WellenDetail>> wellenDetailMap = wellenDetailService.listByBillId(soBillId).stream()
					.collect(Collectors.groupingBy(WellenDetail::getSoBillId));
				if (wellenDetailMap.size() == 1) {
					// 1.1.1. 如果为按单分配的任务，则通过波次编码找到任务，并关闭任务
					List<WellenDetail> wellenDetailList = wellenDetailMap.get(soBillId);
					if (Func.isEmpty(wellenDetailList)) {
						throw new ServiceException("数据异常：未找到波次信息！");
					}
					Long wellenId = wellenDetailList.get(0).getWellenId();
					Task task = taskService.getOne(Condition.getQueryWrapper(new Task()).lambda()
						.eq(Task::getBillId, wellenId)
						.eq(Task::getTaskTypeCd, TaskTypeEnum.Pick.getIndex()));
					if (Func.isNotEmpty(task)) {
						taskService.close(task.getTaskId().toString());
					}
					// 2. 清除(未拣货物品)库存占用
					StockOccupySubtractDTO stockOccupyReduceDTO = new StockOccupySubtractDTO();
					stockOccupyReduceDTO.setTransId(wellenId);
					stockOccupyReduceDTO.setOccupyType(StockOccupyTypeEnum.PickPlan.getIndex());
					stockOccupyReduceDTO.setSoBillId(soBillId);
					stockOccupyService.subtract(stockOccupyReduceDTO);
				}
				// 1.1.2. 如果为多单分配的任务：不做处理(手持端在二次分拣的时候再处理)
			} else if (soHeader.getSoBillState().equals(SoBillStateEnum.COMPLETED.getIndex())) {
				// 查询该订单的库存是否还在库内
				List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
					.lambda()
					.eq(StockDetail::getSoBillId, soBillId));
				if (Func.isEmpty(stockDetailList)) {
					throw new ServiceException("订单：" + soHeader.getSoBillNo() + " 已出库，不允许执行取消操作！");
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
					throw new ServiceException("订单：" + soHeader.getSoBillNo() + " 已部分出库，不允许执行取消操作！");
				}
			} else {
				// 其它状态的订单暂不支持取消操作
				throw new ServiceException(
					"暂不支持订单(" + soHeader.getSoBillNo() + ") 状态(" +
						DictCache.getValue(DictCodeConstant.SO_BILL_STATE, soHeader.getSoBillState()) + ") 做取消操作！");
			}
			// 关闭其他关联的任务
			taskService.closeTask(soBillId, TaskTypeEnum.ALL);
			this.updateBillState(soBillId, SoBillStateEnum.CANCEL, true);
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
							.or().like(SoHeader::getCCode, soHeaderQuery.getCName())
							.or().like(SoHeader::getCName, soHeaderQuery.getCName());
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
		// 查询出库单
		List<SoHeader> soHeaderList = super.listByIds(idList);
		// 获取与当前订单有关联的库存占用信息
		IStockOccupyService stockOccupyService = SpringUtil.getBean(IStockOccupyService.class);
		List<StockOccupy> stockOccupyList = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
			.lambda()
			.in(StockOccupy::getSoBillId, idList));
		if (Func.isNotEmpty(soHeaderList)) {
			for (SoHeader soHeader : soHeaderList) {
				// 忽略已完成的订单
				if (SoBillStateEnum.COMPLETED.getIndex().equals(soHeader.getSoBillState())) {
					continue;
				}
				// 对已取消、已撤销的订单，异常提示
				if (SoBillStateEnum.CANCEL.getIndex().equals(soHeader.getSoBillState()) ||
					SoBillStateEnum.REPEAL.getIndex().equals(soHeader.getSoBillState())) {
					throw new ServiceException(String.format("出库单[%s] %s，不允许再执行此操作",
						soHeader.getSoBillNo(), SoBillStateEnum.valueOf(soHeader.getSoBillState())));
				}
				List<StockOccupy> occupyList = stockOccupyList.stream().filter(u -> {
					return soHeader.getSoBillId().equals(u.getSoBillId());
				}).collect(Collectors.toList());
				if (Func.isNotEmpty(occupyList)) {
					throw new ServiceException(String.format("出库单[%s] 还存在未拣货的物品，请完成拣货后再执行此操作! ",
						soHeader.getSoBillNo()));
				}
				this.updateBillState(soHeader, SoBillStateEnum.COMPLETED, true);
			}
		}
		return true;
	}

	@Override
	public boolean completedOutstock(List<Long> idList) {
		// 查询出库单
		List<SoHeader> soHeaderList = super.listByIds(idList);
		// 获取与当前订单有关联的库存占用信息
		IStockOccupyService stockOccupyService = SpringUtil.getBean(IStockOccupyService.class);
		List<StockOccupy> stockOccupyList = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
			.lambda()
			.in(StockOccupy::getSoBillId, idList));
		// 获取订单对应的库存
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.in(StockDetail::getSoBillId, idList));
		// 获取订单所有明细
		List<SoDetail> soDetailListAll = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
			.in(SoDetail::getSoBillId, idList));
		for (SoHeader soHeader : soHeaderList) {
			// 只允许拣货完成的订单执行此操作
//			if (!SoBillStateEnum.COMPLETED.getIndex().equals(soHeader.getSoBillState())) {
//				throw new ServiceException(
//					String.format("出库单[%s] 还未拣货完成，请拣完货后再执行此操作！", soHeader.getSoBillNo()));
//			}
			// 对已取消、已撤销的订单，异常提示
			if (SoBillStateEnum.CANCEL.getIndex().equals(soHeader.getSoBillState()) ||
				SoBillStateEnum.REPEAL.getIndex().equals(soHeader.getSoBillState())) {
				throw new ServiceException(String.format("出库单[%s] %s，不允许再执行此操作！",
					soHeader.getSoBillNo(), SoBillStateEnum.valueOf(soHeader.getSoBillState())));
			}
			if (ShipStateEnum.SENDED.getIndex().equals(soHeader.getShipState())) {
				throw new ServiceException(String.format("出库单[%s] 已经出库，不允许再执行此操作！", soHeader.getSoBillNo()));
			}
			List<StockOccupy> occupyList = stockOccupyList.stream().filter(u -> {
				return soHeader.getSoBillId().equals(u.getSoBillId());
			}).collect(Collectors.toList());
			if (Func.isNotEmpty(occupyList)) {
				throw new ServiceException(String.format("出库单[%s] 还存在未拣货的物品，请完成拣货后再执行此操作! ",
					soHeader.getSoBillNo()));
			}
			this.updateBillState(soHeader, SoBillStateEnum.COMPLETED, true);
			// 关闭拼托任务
			taskService.closeTask(soHeader.getSoBillId(), TaskTypeEnum.FillLpn);
			// 更改订单状态
			UpdateWrapper<SoHeader> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				.set(SoHeader::getShipState, ShipStateEnum.SENDED.getIndex())
				.eq(SoHeader::getSoBillId, soHeader.getSoBillId());
			super.update(updateWrapper);
			// 记录系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.COMPLATED_OUTSTOCK);
			systemProcParam.setDataType(DataTypeEnum.SO_BILL_NO);
			systemProcParam.setAction(ActionEnum.COMPLATED_OUTSTOCK);
			systemProcParam.setBillNo(soHeader.getSoBillNo());
			Long systemProcId = systemProcService.create(systemProcParam).getSystemProcId();
			// 扣除库存

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
						// 需要修改明细的实出数量
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
