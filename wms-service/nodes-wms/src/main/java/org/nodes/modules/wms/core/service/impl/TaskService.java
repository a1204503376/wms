package org.nodes.modules.wms.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.enums.RelStateEnum;
import org.nodes.wms.core.relenishment.service.IRelDetailService;
import org.nodes.wms.core.relenishment.wrapper.RelDetailWrapper;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.mapper.TaskMapper;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.system.service.impl.TaskServiceImpl;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.system.wrapper.TaskWrapper;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.enums.CountByEnum;
import org.nodes.wms.core.count.enums.LocStatusEnum;
import org.nodes.wms.core.count.service.ICountDetailService;
import org.nodes.wms.core.count.service.ICountHeaderService;
import org.nodes.wms.core.count.wrapper.CountDetailWrapper;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.wrapper.AsnDetailWrapper;
import org.nodes.wms.core.instock.inventory.service.IAsnInventoryService;
import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.service.IPickPlanService;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.vo.WellenSoHeaderVo;
import org.nodes.wms.core.outstock.so.wrapper.PickPlanWrapper;
import org.nodes.wms.core.stock.transfer.service.ITransferDetailService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: pengwei
 * date: 2021/6/10 10:57
 * description: TaskService
 */
@Service

@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TaskService extends TaskServiceImpl<TaskMapper, Task> implements ITaskService {

	@Autowired
	IAsnInventoryService asnInventoryService;
	@Autowired
	IAsnDetailService asnDetailService;
	@Autowired
	IWellenDetailService wellenDetailService;
	@Autowired
	IPickPlanService pickPlanService;
	@Autowired
	ICountDetailService countDetailService;
	@Autowired
	ITransferDetailService transferDetailService;
	@Autowired
	private IRelDetailService relDetailService;

	@Override
	public List<TaskVO> list(QueryWrapper<Task> queryWrapper) {
		List<TaskVO> taskList = TaskWrapper.build().listVO(
			super.list(queryWrapper.lambda().eq(Task::getUserId, AuthUtil.getUserId())));
		if (Func.isNotEmpty(taskList)) {
			// 根据任务类型分组
			taskList.stream().collect(Collectors.groupingBy(TaskVO::getTaskTypeCd))
				.forEach((taskTypeCd, list) -> {
					// 给默认值
					list.forEach(taskVO -> {
						taskVO.setPlanQty(0);
						taskVO.setRealQty(0);
						taskVO.setTaskRemark(taskVO.getBillNo());
					});
					if (taskTypeCd.equals(TaskTypeEnum.Check.getIndex())) {
						// 获取清点任务信息
						List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
							.lambda()
							.in(AsnDetail::getAsnBillId, NodesUtil.toList(list, TaskVO::getBillId)));
						if (Func.isNotEmpty(asnDetailList)) {
							list.forEach(taskVO -> {
								// 计划量
								List<AsnDetail> detailList = asnDetailList.stream().filter(detail -> {
									return detail.getAsnBillId().equals(taskVO.getBillId());
								}).collect(Collectors.toList());
								taskVO.setPlanQty(detailList.size());
								// 已完成量
								detailList = detailList.stream().filter(detail -> {
									return BigDecimalUtil.eq(detail.getSurplusQty(), BigDecimal.ZERO);
								}).collect(Collectors.toList());
								taskVO.setRealQty(detailList.size());
							});
						}
					} else if (taskTypeCd.equals(TaskTypeEnum.Pick.getIndex())) {
						List<Long> soBillIdList = NodesUtil.toList(list, TaskVO::getBillId);
						List<WellenSoHeaderVo> wellenSoHeaderVos = wellenDetailService.getSoHeaderByWellenId(soBillIdList);

						// 获取出库任务信息
						List<PickPlan> pickPlanListAll = pickPlanService.list(
							Condition.getQueryWrapper(new PickPlan())
								.lambda()
								.in(PickPlan::getTaskId, NodesUtil.toList(list, TaskVO::getTaskId)));
						// 获取波次明细信息
						List<WellenDetail> wellenDetailList = wellenDetailService.list(
							Condition.getQueryWrapper(new WellenDetail())
								.lambda()
								.in(WellenDetail::getWellenId, NodesUtil.toList(list, TaskVO::getBillId)));
						if (ObjectUtil.isNotEmpty(pickPlanListAll)) {
							list.forEach(taskVO -> {
								List<PickPlan> pickPlanList = pickPlanListAll.stream().filter(u -> {
									return u.getTaskId().equals(taskVO.getTaskId());
								}).collect(Collectors.toList());
								if(Func.isNotEmpty(pickPlanList)){
									taskVO.setLocName(pickPlanList.get(0).getLocCode());
									taskVO.setPlanQty(pickPlanList.size());
									pickPlanList = pickPlanList.stream().filter(pickPlan -> {
										return BigDecimalUtil.eq(pickPlan.getPickPlanQty(), pickPlan.getPickRealQty());
									}).collect(Collectors.toList());
									taskVO.setRealQty(pickPlanList.size());
								}
								List<WellenDetail> detailList = wellenDetailList.stream().filter(u -> {
									return u.getWellenId().equals(taskVO.getBillId());
								}).collect(Collectors.toList());
								taskVO.setTaskRemark(NodesUtil.join(detailList, "soBillNo"));
								WellenSoHeaderVo soHeader = wellenSoHeaderVos.stream().filter(u -> {
									return u.getWellenId().equals(taskVO.getBillId());
								}).findFirst().orElse(null);
								if (Func.isNotEmpty(soHeader)) {
									taskVO.setCName(soHeader.getSoHeader().getCName());
									taskVO.setOrderNo(soHeader.getSoHeader().getOrderNo());
									taskVO.setSobillNo(soHeader.getSoHeader().getSoBillNo());
									taskVO.setTransportDate(soHeader.getSoHeader().getTransportDate());
								}
							});
						}
					} else if (taskTypeCd.equals(TaskTypeEnum.Count.getIndex())) {
						List<CountHeader> countHeaders = SpringUtil.getBean(ICountHeaderService.class)
							.list(Wrappers.lambdaQuery(CountHeader.class)
								.in(CountHeader::getCountBillId, NodesUtil.toList(list, TaskVO::getBillId)));
						// 获取盘点任务信息
						List<CountDetail> countDetailListAll = countDetailService.list(
							Condition.getQueryWrapper(new CountDetail())
								.lambda()
								.in(CountDetail::getCountBillId, NodesUtil.toList(list, TaskVO::getBillId))
						);
						if (Func.isNotEmpty(countDetailListAll)) {
							list.forEach(taskVO -> {
								CountHeader countHeader = countHeaders.stream().filter(u -> {
									return u.getCountBillId().equals(taskVO.getBillId());
								}).findFirst().orElse(null);
								if (Func.isNotEmpty(countHeader)) {
									taskVO.setCountByDesc(CountByEnum.getCountByEnumByIndex(countHeader.getCountBy()).getName());
								}
								List<CountDetail> countDetailList = countDetailListAll.stream().filter(u -> {
									return u.getCountBillId().equals(taskVO.getBillId());
								}).collect(Collectors.toList());
								taskVO.setPlanQty(countDetailList.size());
								countDetailList = countDetailList.stream().filter(countDetail -> {
									return countDetail.getLocState().equals(LocStatusEnum.Complated.getIndex());
								}).collect(Collectors.toList());
								taskVO.setRealQty(countDetailList.size());
							});
						}
					} else if (taskTypeCd.equals(TaskTypeEnum.Replenish.getIndex())) {
						// 获取补货任务信息
						QueryWrapper<RelDetail> tdqw = new QueryWrapper<>();
						tdqw.lambda().in(RelDetail::getRelBillId, NodesUtil.toList(list, TaskVO::getBillId));
						List<RelDetail> relDetails = relDetailService.list(tdqw);
						if (Func.isNotEmpty(relDetails)) {
							list.forEach(taskVO -> {
								List<RelDetail> transferDetailList = relDetails.stream().filter(u -> {
									return u.getRelBillId().equals(taskVO.getBillId());
								}).collect(Collectors.toList());
								taskVO.setPlanQty(transferDetailList.size());
								transferDetailList = transferDetailList.stream().filter(relDetail -> !relDetail.getRelStatus().equals(RelStateEnum.EXECUTED.getIndex())).collect(Collectors.toList());
								taskVO.setRealQty(transferDetailList.size());
							});

						}
					}
				});
		}
		return taskList;
	}

	@Override
	public TaskVO getDetail(Long taskId) {
		TaskVO task = TaskWrapper.build().entityVO(super.getById(taskId));
		if (ObjectUtil.isNotEmpty(task)) {
			if (task.getTaskTypeCd().equals(TaskTypeEnum.Check.getIndex())) {
				// 获取清点任务信息
				List<AsnDetail> asnDetailList = asnDetailService.list(task.getBillId());
				task.setDetail(AsnDetailWrapper.build().listVO(asnDetailList));
			} else if (task.getTaskTypeCd().equals(TaskTypeEnum.Pick.getIndex())) {
				// 获取出库任务信息
				PickPlan pickPlanQuery = new PickPlan();
				pickPlanQuery.setTaskId(task.getTaskId());
				List<PickPlan> pickPlanList = pickPlanService.list(Condition.getQueryWrapper(pickPlanQuery));
				task.setDetail(PickPlanWrapper.build().listVO(pickPlanList));
			} else if (task.getTaskTypeCd().equals(TaskTypeEnum.Count.getIndex())) {
				// 获取盘点任务信息
				List<CountDetail> countDetailList = countDetailService.list(
					new QueryWrapper<CountDetail>().lambda().eq(CountDetail::getTaskGroup, task.getTaskGroup()));
				task.setDetail(CountDetailWrapper.build().listVO(countDetailList));
			} else if (task.getTaskTypeCd().equals(TaskTypeEnum.Replenish.getIndex())) {
				// 补货任务详细信息
				List<RelDetail> relDetails = relDetailService.list(Wrappers.lambdaQuery(RelDetail.class)
					.eq(RelDetail::getRelBillId, task.getBillId())
				.eq(RelDetail::getAttribute1,"1"));
				task.setDetail(RelDetailWrapper.build().listVO(relDetails));
			}
		}
		return task;
	}
}
