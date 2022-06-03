package org.nodes.wms.core.relenishment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.SysCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.relenishment.dto.RelHeaderDTO;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.entity.RelHeader;
import org.nodes.wms.core.relenishment.enums.RelStateEnum;
import org.nodes.wms.core.relenishment.mapper.RelHeaderMapper;
import org.nodes.wms.core.relenishment.service.IRelDetailService;
import org.nodes.wms.core.relenishment.service.IRelHeaderService;
import org.nodes.wms.core.relenishment.vo.RelDetailVo;
import org.nodes.wms.core.relenishment.vo.RelHeaderVo;
import org.nodes.wms.core.relenishment.wrapper.RelDetailWrapper;
import org.nodes.wms.core.relenishment.wrapper.RelHeaderWrapper;
import org.nodes.wms.core.stock.core.dto.StockOccupyDTO;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.system.dto.TaskDTO;
import org.nodes.wms.core.system.entity.TaskPackage;
import org.nodes.wms.core.system.enums.TaskProcTypeEnum;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskPackageService;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Primary
@Service
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RelHeaderServiceImpl<M extends RelHeaderMapper, T extends RelHeader>
	extends BaseServiceImpl<RelHeaderMapper, RelHeader>
	implements IRelHeaderService {
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	private IRelDetailService relDetailService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private ITaskPackageService packageService;

	@Override
	public IPage<RelHeaderVo> selectPage(RelHeaderDTO relHeaderDTO, Query query) {
		IPage<RelHeader> page = super.page(Condition.getPage(query), Condition.getQueryWrapper(relHeaderDTO));
		IPage<RelHeaderVo> data = RelHeaderWrapper.build().pageVO(page);
		return data;
	}

	@Override
	@Transactional
	public void createRelenishOrder(RelHeaderDTO relHeaderDTO) {
		RelHeader relHeader = new RelHeader();
		relHeader.setRelBillNo("REL" + SerialNoCache.getSerialDateNo("REL", 8));
		relHeader.setTenantId(AuthUtil.getTenantId());
		relHeader.setRelBillState(RelStateEnum.UNEXECUTED.getIndex());
		BladeUser user = AuthUtil.getUser();
		if (!"developer".equals(user.getAccount()) && !"admin".equals(user.getAccount())) {
			Dept dept = SysCache.getDept(Func.toLong(AuthUtil.getDeptId()));
			if (Func.isNotEmpty(dept)) {
				relHeader.setDeptCode(dept.getDeptCode());
				relHeader.setDeptId(dept.getId());
				relHeader.setDeptName(dept.getDeptName());
			}
		}
		relHeader.setCreateUser(AuthUtil.getUserId());
		relHeader.setWhId(relHeaderDTO.getWhId());
		relHeader.setWoId(relHeaderDTO.getWoId());
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(relHeaderDTO.getWoId());
		if (Func.isNotEmpty(owner)) {
			relHeader.setWoCode(owner.getOwnerCode());
			relHeader.setWoName(owner.getOwnerName());
		}
		relHeader.setWhCode(relHeaderDTO.getWoCode());
		relHeader.setCreateTime(DateUtil.now());
		if (super.save(relHeader)) {
			;
			relDetailService.createRelenishDetail1(relHeaderDTO.getIds(), relHeader);
		}
	}

	@Override
	public RelHeaderVo getDetail(Long relBillId) {
		RelHeader relHeader = super.getById(relBillId);
		RelHeaderVo relHeaderVo = RelHeaderWrapper.build().entityVO(relHeader);
		List<RelDetail> list = relDetailService.list(Wrappers.lambdaQuery(RelDetail.class).eq(RelDetail::getRelBillId, relBillId));
		List<RelDetailVo> relDetailVos = RelDetailWrapper.build().listVO(list);
		relHeaderVo.setRelDetailVos(relDetailVos);
		return relHeaderVo;
	}

	@Override
	public boolean saveOrUpdate(RelHeaderVo relHeaderVo) {
		super.saveOrUpdate(relHeaderVo);
		if (Func.isNotEmpty(relHeaderVo.getRelDetailVos())) {
			List<RelDetailVo> detailList = relHeaderVo.getRelDetailVos();
			for (RelDetailVo detail : detailList) {
				if (Func.isNotEmpty(detail.getFromWhId()) && detail.getFromWhId() != -1
					&& Func.isNotEmpty(detail.getFromZoneId()) && detail.getFromZoneId() != -1
					&& Func.isNotEmpty(detail.getFromLocId()) && detail.getFromLocId() != -1
					&& BigDecimalUtil.gt(detail.getRelQty(), BigDecimal.ZERO)) {
					detail.setAttribute1("1");
				}
				relDetailService.saveOrUpdate(detail);
			}
		}
		return true;
	}

	private QueryWrapper<RelHeader> getQueryWrapper(RelHeaderDTO relHeaderDTO) {

		return null;
	}

	@Override
	public boolean canEdit(Long relBillId) {
		RelHeader relHeader = super.getById(relBillId);
		if (Func.isEmpty(relHeader)) {
			throw new ServiceException("补货单不存在！（订单ID：" + relBillId + " ）");
		}
		boolean result = relHeader.getRelBillState().equals(RelStateEnum.UNEXECUTED.getIndex());
		return result;
	}

	@Override
	public boolean relTask(String ids) {
		List<Long> idList = Func.toLongList(ids);
		List<RelHeader> relHeaders = super.listByIds(idList);
		for (RelHeader relHeader : relHeaders) {
			if (!relHeader.getRelBillState().equals(RelStateEnum.UNEXECUTED.getIndex())) {
				throw new ServiceException(String.format("单据[%s]不是未执行状态不能下发任务！", relHeader.getRelBillNo()));
			}
			List<RelDetail> list = relDetailService.list(Wrappers.lambdaQuery(RelDetail.class)
				.eq(RelDetail::getRelBillId, relHeader.getRelBillId()).eq(RelDetail::getAttribute1, "1"));
			if (Func.isEmpty(list)) {
				throw new ServiceException(String.format("单据[%s]至少要有一条有效的补货明细！", relHeader.getRelBillNo()));
			}
			TaskDTO taskDTO = new TaskDTO();
			taskDTO.setBillNo(relHeader.getRelBillNo());
			taskDTO.setBillId(relHeader.getRelBillId());
			taskDTO.setTaskType(TaskTypeEnum.Replenish.getIndex());
			taskDTO.setTaskQty(relHeaders.size());
			taskDTO.setBillTypeCd("909");
			taskDTO.setWhId(relHeader.getWhId());
			taskDTO.setAllotTime(LocalDateTime.now());
			TaskPackage taskPackage = new TaskPackage();
			taskPackage.setWhId(relHeader.getWhId());
			taskPackage.setTaskTypeCd(TaskTypeEnum.Replenish.getIndex());
			taskPackage.setTaskProcType(TaskProcTypeEnum.Artificial.getIndex());
			taskPackage.setTaskDataId(relHeader.getRelBillId());
			taskPackage.setTtpCreateTime(LocalDateTime.now());
			if (!packageService.save(taskPackage)) {
				throw new ServiceException("保存任务头表失败");
			}
			taskDTO.setTaskPackage(taskPackage);
			relHeader.setRelBillState(RelStateEnum.EXECUTING.getIndex());
			taskService.create(taskDTO);
			//生成补货占用
			list.forEach(relDetail -> {
				SystemProcDTO systemProcParam = new SystemProcDTO();
				systemProcParam.setProcType(SystemProcTypeEnum.REL);
				systemProcParam.setDataType(DataTypeEnum.RelBillNo);
				systemProcParam.setAction(ActionEnum.REPLENISH_CREATE);
				systemProcParam.setWhId(relHeader.getWhId());
				systemProcParam.setBillNo(relHeader.getRelBillNo());
				systemProcParam.setOperationQty(relDetail.getRelQty());
				systemProcParam.setOperationUnit(relDetail.getUmName());
				systemProcParam.setLpnCode(relDetail.getToLpnCode());
				SystemProc systemProc = systemProcService.create(systemProcParam);
				// 创建库存占用
				StockOccupyDTO stockOccupyDTO = new StockOccupyDTO();
				stockOccupyDTO.setTransId(relHeader.getRelBillId());
				stockOccupyDTO.setOccupyType(StockOccupyTypeEnum.Replenish.getIndex());
				stockOccupyDTO.setWhId(relHeader.getWhId());
				stockOccupyDTO.setStockId(relDetail.getStockId());
				stockOccupyDTO.setSkuId(relDetail.getSkuId());
				stockOccupyDTO.setSkuCode(relDetail.getSkuCode());
				stockOccupyDTO.setSkuName(relDetail.getSkuName());
				stockOccupyDTO.setSystemProcId(systemProc.getSystemProcId());
				stockOccupyDTO.setOccupyTime(LocalDateTime.now());
				stockOccupyDTO.setOccupyQty(relDetail.getRelQty().multiply(new BigDecimal(relDetail.getConvertQty())));
				stockOccupyDTO.setSoBillNo(relDetail.getRelBillNo());
				stockOccupyService.add(stockOccupyDTO);
			});

		}
		return super.updateBatchById(relHeaders);
	}

	@Override
	public boolean removeDataByIds(String ids) {
		List<Long> idList = Func.toLongList(ids);
		List<RelHeader> relHeaders = super.listByIds(idList);
		for (RelHeader relHeader : relHeaders) {
			if (Func.isEmpty(relHeader)) {
				throw new ServiceException("补货单据不存在！");
			}
			if (!relHeader.getRelBillState().equals(RelStateEnum.UNEXECUTED.getIndex())) {
				throw new ServiceException(String.format("单据[%s]不是未执行状态不可删除"
					, relHeader.getRelBillNo()));
			}
		}
		super.removeByIds(idList);
		relDetailService.remove(Wrappers.lambdaQuery(RelDetail.class).in(RelDetail::getRelBillId, idList));
		return true;
	}

	@Override
	public boolean checkTask(String ids) {
		List<Long> idList = Func.toLongList(ids);
		List<RelDetail> list = relDetailService.list(Wrappers.lambdaQuery(RelDetail.class)
			.in(RelDetail::getRelBillId, idList)
			.isNull(RelDetail::getAttribute1));
		if (Func.isNotEmpty(list)) {
			return false;
		}
		return true;
	}

}
