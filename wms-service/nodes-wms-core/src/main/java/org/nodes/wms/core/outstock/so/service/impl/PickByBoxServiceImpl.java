package org.nodes.wms.core.outstock.so.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuLot;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.*;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.dto.SkuLogDTO;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.enums.SkuLogTypeEnum;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.dto.PickPlanDTO;
import org.nodes.wms.core.outstock.so.entity.*;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.enums.WellenStateEnum;
import org.nodes.wms.core.outstock.so.mapper.PickPlanMapper;
import org.nodes.wms.core.outstock.so.mapper.SoDetailMapper;
import org.nodes.wms.core.outstock.so.mapper.WellenMapper;
import org.nodes.wms.core.outstock.so.service.*;
import org.nodes.wms.core.outstock.so.vo.*;
import org.nodes.wms.core.outstock.so.wrapper.PickPlanWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 按箱拣货 服务实现类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class PickByBoxServiceImpl<M extends PickPlanMapper, T extends PickPlan>
	extends BaseServiceImpl<PickPlanMapper, PickPlan>
	implements IPickByBoxService {

	@Autowired
	IWellenService wellenService;
	@Autowired
	IWellenDetailService wellenDetailService;
	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	IStockService stockService;
	@Autowired
	ITaskService taskService;
	@Autowired
	ISoPickService soPickService;
	@Autowired
	ISkuUmService skuUmService;
	@Autowired
	SoDetailMapper soDetailMapper;
	@Autowired
	IPickPlanService pickPlanService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	WellenMapper wellenMapper;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	ISkuLogService skuLogService;
	@Autowired
	ISkuService skuService;
	/**
	 * 系统日志服务
	 */
	@Autowired
	private ISystemProcService systemProcService;


	public List<PickPlan> getPickInfoByOrder(Map<String, String> param){
		String wellenNo = param.get("wellenNo");
		String taskId = param.get("taskId");
		String soBillNo = param.get("soBillNo");
		PickPlanMapper planMapper = SpringUtil.getBean(PickPlanMapper.class);
		Long wellenId = null;
		if(Func.isNotEmpty(wellenNo)){
			Task task = taskService.getOne(Wrappers.lambdaQuery(Task.class).eq(Task::getBillNo, wellenNo));
			if(Func.isNotEmpty(task)){
				wellenId = task.getBillId();
			}
		}
		List<PickPlan> pickPlans = planMapper.selectPickPlanListForPickByWellenId(wellenId, Func.isNotEmpty(taskId)?new Long(taskId):null,soBillNo);
		return pickPlans;
	}



	/**
	 * 以任务ID为条件,并通过库位编码排序查询拣货单据列表
	 *
	 * @param
	 * @return
	 */
	@Override
	public PickTaskVO getByTaskId(Map<String, String> param) {
		PickTaskVO pickTaskVO = new PickTaskVO();
		String wellenNo = param.get("wellenNo");
		String taskId = param.get("taskId");
		Long wellenId = null;
		if (Func.isEmpty(wellenNo) && Func.isEmpty(taskId)) {
			throw new ServiceException("波次编码与任务ID至少有一个不为空:请前端调试");
		}
		Task detail = taskService.getOne(Wrappers.lambdaQuery(Task.class).eq(Task::getBillNo, wellenNo));
		if (Func.isNotEmpty(detail)) {
			List<Long> ids = new ArrayList<>();
			ids.add(detail.getBillId());
			List<WellenSoHeaderVo> wellenSoHeaderVos = wellenDetailService.getSoHeaderByWellenId(ids);
			if (Func.isNotEmpty(wellenSoHeaderVos)) {
				pickTaskVO.setOrderNo(wellenSoHeaderVos.get(0).getSoHeader().getOrderNo());
				pickTaskVO.setSobillNo(wellenSoHeaderVos.get(0).getSoHeader().getSoBillNo());
				pickTaskVO.setCName(wellenSoHeaderVos.get(0).getSoHeader().getCName());
				pickTaskVO.setTransportDate(wellenSoHeaderVos.get(0).getSoHeader().getTransportDate());
			}
		}

		//登录用户信息
		BladeUser user = AuthUtil.getUser();
		//总数量
		PickPlan pickPlanParam = new PickPlan();
		if (Func.isNotEmpty(wellenNo)) { // 波次编码不为空
			if (Func.isEmpty(wellenMapper.selectWellenListByNo(wellenNo))) {
				return pickTaskVO;
			}
			wellenId = wellenMapper.selectWellenListByNo(wellenNo).getWellenId();
			pickPlanParam.setWellenId(wellenId);
		}
		if (Func.isNotEmpty(taskId)) { // 任务id不为空
			pickPlanParam.setTaskId(Long.parseLong(taskId));
		}
		// 待办拣货计划
		Long finalWellenId = wellenId;
		List<PickPlan> list = pickPlanService.list(Condition.getQueryWrapper(new PickPlan())
			.lambda()
			.func(sql -> {
				if (Func.isNotEmpty(finalWellenId)) {
					sql.eq(PickPlan::getWellenId, finalWellenId);
				} else {
					sql.eq(PickPlan::getTaskId, pickPlanParam.getTaskId());
				}
			}).apply("pick_plan_qty <> pick_real_qty")
			.orderByAsc(PickPlan::getLocCode));
		List<PickPlan> list1 = pickPlanService.list(Condition.getQueryWrapper(new PickPlan())
			.lambda()
			.func(sql -> {
				if (Func.isNotEmpty(finalWellenId)) {
					sql.eq(PickPlan::getWellenId, finalWellenId);
				} else {
					sql.eq(PickPlan::getTaskId, pickPlanParam.getTaskId());
				}
			}));
		int planCount = super.count(Condition.getQueryWrapper(pickPlanParam).lambda()
			.orderByDesc(PickPlan::getLocCode));
		//没有待办拣货计划
		if (list.size() <= 0) {
			return pickTaskVO;
		}
		Map<Long, BigDecimal> collect = list1.stream().collect(Collectors.groupingBy(PickPlan::getSkuId))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickPlanQty()))
				.map(PickPlan::getPickPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
		Map<Long, BigDecimal> collect1 = list1.stream().collect(Collectors.groupingBy(PickPlan::getSkuId))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickRealQty()))
				.map(PickPlan::getPickRealQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
		pickTaskVO.setCount(planCount); //总进度
		pickTaskVO.setFinish(planCount - list.size()); //完成进度

		if (Func.isEmpty(wellenNo)) { // 波次编码为空 则显示任务id
			pickTaskVO.setTitleCode(taskId); //任务id
		}
		if (Func.isEmpty(taskId)) { // 任务id为空 则显示波次编码
			pickTaskVO.setTitleCode(wellenNo); //波次编码
		}

		List<PickSkuVO> pickSkuVOs = new ArrayList<>();
		for (PickPlan pickPlan : list) {
			PickSkuVO pickSkuVO = new PickSkuVO();
			PickPlanWrapper.build().pickPlan2PickSkuVO(pickPlan, pickSkuVO);
			pickSkuVO.setTotalPlanQty(collect.get(pickPlan.getSkuId()));
			pickSkuVO.setTotalScanQty(collect1.get(pickPlan.getSkuId()));
			pickSkuVO.setIsSn(SkuCache.getById(pickPlan.getSkuId()).getIsSn()); //是否序列号
			pickSkuVO.setUserName(user.getNickName());// 拣货人
			pickSkuVO.setSkuId(pickPlan.getSkuId()); //物品ID
			pickSkuVO.setLotNumber(pickPlan.getLotNumber());
			//通过包装id查询包装明细列表
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(pickPlan.getWspId())
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

				if (skuPackageDetail.getSkuLevel() == pickPlan.getSkuLevel()) {
					pickSkuVO.setDefaultPackageDetail(packageViewVO);//set默认包装明细
					pickSkuVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //set规格

					pickSkuVO.setPlanQtyName(skuUmService.convert(pickPlan.getWspId(),
						pickPlan.getSkuLevel(), pickPlan.getPickPlanQty()));
					pickSkuVO.setRealQtyName(skuUmService.convert(pickPlan.getWspId(),
						pickPlan.getSkuLevel(), pickPlan.getPickRealQty()));

				}
				//添加包装列表信息
				packageDetailResultList.add(packageViewVO);
			}
			//set包装明细列表
			pickSkuVO.setPackageDetails(packageDetailResultList);

			try {
				List<Stock> stocks = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getLotNumber, pickPlan.getLotNumber())
					.eq(Stock::getSkuId, pickPlan.getSkuId()));
				ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
				Sku sku = SkuCache.getById(pickPlan.getSkuId());
				if (Func.isNotEmpty(sku)){
					SkuLot skuLot = skuLotService.getById(sku.getWslId());
					if(Func.isNotEmpty(skuLot)){
						pickSkuVO.setSkuLotValue(skuLot);
					}
				}
				if (stocks.size() > 0) {
					Map<String, Object> skuLotMap = SkuLotWrapper.skuLotToMapWithStock(stocks.get(0));
					SkuLotBaseEntity skuLotDTO = new SkuLotBaseEntity();
					if (Func.isNotEmpty(skuLotMap)) {
						for (String s : skuLotMap.keySet()) {
							String t = s.substring(s.lastIndexOf("t") + 1, s.length());
							skuLotDTO.skuLotSet(Integer.parseInt(t), skuLotMap.get(s).toString());
						}
					}
					pickSkuVO.setSkuLot(skuLotDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//通过拣货计划id查询拣货日志获得序列号
			QueryWrapper<SoPick> soPickQueryWrapper = new QueryWrapper<>();
			soPickQueryWrapper.lambda().eq(SoPick::getPickPlanId, pickPlan.getPickPlanId());
			List<String> serialList = new ArrayList<>();
			List<SoPick> soPickList = soPickService.list(soPickQueryWrapper);
			if (Func.isNotEmpty(soPickList)) {
				if (soPickList.size() > 0) {
					for (SoPick soPick : soPickList) {
						if (Func.isNotEmpty(soPick.getSoDetailCode())) {
							String[] soDetailCodes = soPick.getSoDetailCode().split(",");
							for (int i = 0; i < soDetailCodes.length; i++) {
								String soDetailCode = soDetailCodes[i];
								serialList.add(soDetailCode);
							}
						}
					}
				}
			}
			pickSkuVO.setSerialList(serialList);
			pickSkuVOs.add(pickSkuVO);
		}
		pickSkuVOs.sort(new Comparator<PickSkuVO>() {
			@Override
			public int compare(PickSkuVO o1, PickSkuVO o2) {
				Location location1 = LocationCache.getByCode(o1.getSourceLocCode());
				Location location2 = LocationCache.getByCode(o2.getSourceLocCode());
				if (Func.isEmpty(location1) || Func.isEmpty(location2)) {
					return 0;
				}
				if (Func.isEmpty(location1.getLogicAllocation()) || Func.isEmpty(location2.getLogicAllocation())) {
					return 0;
				}
				return location1.getLogicAllocation().compareTo(location2.getLogicAllocation());
			}
		});
		pickTaskVO.setPickPlans(pickSkuVOs);

		return pickTaskVO;
	}

	/**
	 * 按箱拣货提交
	 *
	 * @param pickTaskSubmitVO
	 * @return
	 */
	@Override
	public synchronized PickTaskVO submitPickInfo(PickTaskSubmitVO pickTaskSubmitVO) {

		if (Func.isEmpty(pickTaskSubmitVO.getWellenNo()) && Func.isEmpty(pickTaskSubmitVO.getTaskId())) {
			throw new ServiceException("波次编码与任务ID至少有一个不为空:请前端调试");
		}
		// 获取任务
		QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(pickTaskSubmitVO.getTaskId())) {
			taskQueryWrapper.lambda().eq(Task::getTaskId, pickTaskSubmitVO.getTaskId());
		} else {
			taskQueryWrapper.lambda()
				.eq(Task::getBillNo, pickTaskSubmitVO.getWellenNo())
				.eq(Task::getTaskTypeCd, TaskTypeEnum.Pick.getIndex());
		}
		Task task = taskService.getOne(taskQueryWrapper);
		if (Func.isEmpty(task)) {
			throw new ServiceException("任务不存在！");
		}
		PickTaskVO pickTaskVO = new PickTaskVO();
		List<Long> ids = new ArrayList<>();
		ids.add(task.getBillId());
		List<WellenSoHeaderVo> wellenSoHeaderVos = wellenDetailService.getSoHeaderByWellenId(ids);
		if (Func.isNotEmpty(wellenSoHeaderVos)) {
			pickTaskVO.setOrderNo(wellenSoHeaderVos.get(0).getSoHeader().getOrderNo());
			pickTaskVO.setSobillNo(wellenSoHeaderVos.get(0).getSoHeader().getSoBillNo());
			pickTaskVO.setCName(wellenSoHeaderVos.get(0).getSoHeader().getCName());
			pickTaskVO.setTransportDate(wellenSoHeaderVos.get(0).getSoHeader().getTransportDate());
		}
		// 获取当前波次下所有拣货计划
		List<PickPlan> pickPlanList = pickPlanService.list(Condition.getQueryWrapper(new PickPlan())
			.lambda()
			.func(sql -> {
					if (Func.isNotEmpty(pickTaskSubmitVO.getTaskId())) {
						sql.eq(PickPlan::getTaskId, pickTaskSubmitVO.getTaskId());
					} else
						sql.eq(PickPlan::getWellenId, task.getBillId());
				}
			).orderByAsc(PickPlan::getLocCode)
		);
		Map<Long, BigDecimal> collect = pickPlanList.stream().collect(Collectors.groupingBy(PickPlan::getSkuId))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickPlanQty()))
				.map(PickPlan::getPickPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add)));

		//获得拣货计划
		PickPlan pickPlan = pickPlanList.stream().filter(u -> {
			return u.getPickPlanId().equals(pickTaskSubmitVO.getPickPlanId());
		}).findFirst().orElse(null);
		if (Func.isEmpty(pickPlan)) {
			throw new ServiceException(String.format("物品[%s]不在拣货计划内", pickTaskSubmitVO.getSkuCode()));
		}
		// 验证拣货数量不能超过计划量
		BigDecimal bigDecimal = pickTaskSubmitVO.getPickQty().add(pickPlan.getPickRealQty());
		if (BigDecimalUtil.gt(bigDecimal,  pickPlan.getPickPlanQty())) {
			throw new ServiceException(String.format("拣货数量超过计划数量"));
		}
		// 验证批次号是否一致
		if (!pickTaskSubmitVO.getLotNumber().equals(pickPlan.getLotNumber())) {
			throw new ServiceException(String.format(
				"物品[%s]的批次[%s]不匹配", pickTaskSubmitVO.getSkuCode(), pickTaskSubmitVO.getLotNumber()));
		}
		// 获取源库位
		Location sourceLoc = LocationCache.getValid(pickTaskSubmitVO.getSourceLocCode());
		//赋值
		PickPlanDTO pickPlanDTO = PickPlanWrapper.pickSubmitVO2PickPlanDTO(pickPlan, pickTaskSubmitVO);
		//保存系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.PICK);
		if (Func.isNotEmpty(pickPlanDTO.getWellenNo())) {
			// 波次编码不为空
			systemProcParam.setDataType(DataTypeEnum.WellenNo);
			systemProcParam.setBillNo(pickPlanDTO.getWellenNo());
		} else if (Func.isNotEmpty(pickPlanDTO.getTaskId())) {
			// 任务id不为空
			systemProcParam.setDataType(DataTypeEnum.TaskNo);
			systemProcParam.setBillNo(pickPlanDTO.getTaskId().toString());
		}
		systemProcParam.setAction(ActionEnum.PICK_BOX);
		systemProcParam.setOperationQty(pickPlanDTO.getPickQty());
		systemProcParam.setOperationUnit(SkuPackageDetailCache.getById(pickPlanDTO.getWspdId()).getWsuName());
		systemProcParam.setWhId(pickPlanDTO.getWhId());
		SystemProc systemProc = systemProcService.create(systemProcParam);
		pickPlanDTO.setSystemProcId(systemProc.getSystemProcId());

		// 查找库存占用信息
		List<StockOccupy> stockOccupyList = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
			.lambda()
			.eq(StockOccupy::getOccupyType, StockOccupyTypeEnum.PickPlan.getIndex())
			.eq(StockOccupy::getPickPlanId, pickPlan.getPickPlanId()));
		if (Func.isEmpty(stockOccupyList)) {
			throw new ServiceException(String.format("物品[%s]不在任务内，或已拣货完成！", pickTaskSubmitVO.getSkuCode()));
		}
		// 获取关联的所有表头
		List<SoHeader> soHeaderList = soHeaderService.listByIds(
			NodesUtil.toList(stockOccupyList, StockOccupy::getSoBillId)
		);
		// 获取关联的所有明细
		List<SoDetail> soDetailList = soDetailMapper.selectList(Condition.getQueryWrapper(new SoDetail())
			.lambda()
			.in(SoDetail::getSoDetailId, NodesUtil.toList(stockOccupyList, StockOccupy::getSoDetailId)));
		// 获取所有有关联的库存信息
		List<Stock> stockList = stockService.listByIds(
			NodesUtil.toList(stockOccupyList, StockOccupy::getStockId)
		);
		for (StockOccupy stockOccupy : stockOccupyList) {
			pickPlanDTO.setSoDetailId(stockOccupy.getSoDetailId());
			SoHeader soHeader = soHeaderList.stream().filter(u -> {
				return u.getSoBillId().equals(stockOccupy.getSoBillId());
			}).findFirst().orElse(null);
			if (Func.isNotEmpty(soHeader)) {
				pickPlanDTO.setSoBillId(soHeader.getSoBillId());
				pickPlanDTO.setSoBillNo(soHeader.getSoBillNo());
			}
			// 获取明细
			SoDetail soDetail = soDetailList.stream().filter(u -> {
				return u.getSoDetailId().equals(stockOccupy.getSoDetailId());
			}).findFirst().orElse(null);
			if (Func.isEmpty(soDetail)) {
				throw new ServiceException("订单明细不存在(ID:" + stockOccupy.getSoDetailId() + ")");
			}
			if (!sourceLoc.getLocId().equals(pickPlan.getLocId())) {
				throw new ServiceException(String.format("请到库位[%s] 拣货！", pickPlan.getLocCode()));
			}
			// 获取库存
			Stock stock = stockList.stream().filter(u -> {
				return u.getStockId().equals(stockOccupy.getStockId());
			}).findFirst().orElse(null);
			if (Func.isEmpty(stock)) {
				throw new ServiceException("库存信息不存在(ID:" + stockOccupy.getStockId() + ")");
			}
			// 验证手持提交的批属性信息
			if (Func.isEmpty(pickPlanDTO.getSkuLot())) {
				pickPlanDTO.setSkuLot(new SkuLotBaseEntity());
			}
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				String value = stock.skuLotGet(i);
				pickPlanDTO.getSkuLot().skuLotSet(i, value);
				if (pickTaskSubmitVO.getLots().skuLotChk(i)) {
					if (!pickTaskSubmitVO.getLots().skuLotGet(i).equals(stock.skuLotGet(i))) {
						throw new ServiceException("批属性" + i + " 不匹配! ");
					}
				}
			}
			// 更新波次
			wellenService.updateState(pickPlanDTO.getWellenId(), WellenStateEnum.Begin);
			// 库存移动
			pickPlanService.moveStockSku(pickPlanDTO);

			// 更新拣货计划数量
			pickPlan.setPickRealQty(pickPlan.getPickRealQty().add(pickPlanDTO.getPickQty()));
			UpdateWrapper<PickPlan> pickPlanUpdateWrapper = new UpdateWrapper<>();
			pickPlanUpdateWrapper.lambda()
				.set(PickPlan::getPickRealQty, pickPlan.getPickRealQty())
				.eq(PickPlan::getPickPlanId, pickPlan.getPickPlanId());
			pickPlanService.update(pickPlanUpdateWrapper);
			// 保存拣货日志
			pickPlanService.saveLogSoPick(pickPlanDTO);
			/**
			 * description	取消订单验证
			 * author		pengwei
			 * date			2020-07-02
			 */
			if (soHeader.getSoBillState().equals(SoBillStateEnum.CANCEL.getIndex())) {
				throw new ServiceException("取消订单不允许再执行拣货操作；请将已拣物品放到暂存区后返回任务列表刷新任务！");
			}
			//更新任务开始时间
			taskService.updateBeginTime(pickPlanDTO.getWellenNo(), TaskTypeEnum.Pick);
			// 保存物品操作记录
			SkuLogDTO skuLogDTO = new SkuLogDTO();
			skuLogDTO.setSkuId(soDetail.getSkuId());
			skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.OUTSTOCK);
			this.skuLogService.saveOrUpdate(skuLogDTO);
			// 更新出库单明细数量
			soDetailService.updateSoDetailQty(soDetail, pickTaskSubmitVO.getPickQty());
			//计划列表
			long count = pickPlanList.stream().filter(u -> {
				return BigDecimalUtil.ne(u.getPickRealQty(), u.getPickPlanQty());
			}).count();
			// 拣货完成
			if (count == 0L) {
				pickPlanService.closeTack(pickPlanDTO);
			}
			if (BigDecimalUtil.eq(pickTaskSubmitVO.getPickQty(), BigDecimal.ZERO)) {
				break;
			}
		}
		List<PickPlan> unPickList = pickPlanList.stream().filter(u -> {
			return BigDecimalUtil.ne(u.getPickPlanQty(), u.getPickRealQty());
		}).collect(Collectors.toList());


		//总进度
		pickTaskVO.setCount(pickPlanList.size());
		//完成进度
		pickTaskVO.setFinish(pickPlanList.size() - unPickList.size());
		if (Func.isNotEmpty(pickTaskSubmitVO.getTaskId())) {
			pickTaskVO.setTitleCode(pickTaskSubmitVO.getTaskId());
		} else {
			pickTaskVO.setTitleCode(pickTaskSubmitVO.getWellenNo());
		}
		List<PickPlan> pickPlanList1 = pickPlanService.list(Condition.getQueryWrapper(new PickPlan())
			.lambda()
			.func(sql -> {
					if (Func.isNotEmpty(pickTaskSubmitVO.getTaskId())) {
						sql.eq(PickPlan::getTaskId, pickTaskSubmitVO.getTaskId());
					} else
						sql.eq(PickPlan::getWellenId, task.getBillId());
				}
			).orderByAsc(PickPlan::getLocCode)
		);
		Map<Long, BigDecimal> collect1 = pickPlanList.stream().collect(Collectors.groupingBy(PickPlan::getSkuId))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickRealQty()))
				.map(PickPlan::getPickRealQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
		// 处理未拣货完成的任务明细
		if (Func.isNotEmpty(unPickList)) {
			// 查询所有关联的库存
			List<Stock> unPickStockList = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.in(Stock::getLotNumber, NodesUtil.toList(unPickList, PickPlan::getLotNumber)));
			// 查询已拣货的序列号信息
			List<SoPick> soPickListAll = soPickService.list(Condition.getQueryWrapper(new SoPick())
				.lambda()
				.in(SoPick::getPickPlanId, NodesUtil.toList(unPickList, PickPlan::getPickPlanId)));
			pickTaskVO.setPickPlans(new ArrayList<>());
			for (PickPlan item : unPickList) {
				PickSkuVO pickSkuVO = new PickSkuVO();
				PickPlanWrapper.build().pickPlan2PickSkuVO(item, pickSkuVO);
				pickSkuVO.setTotalPlanQty(collect.get(item.getSkuId()));
				pickSkuVO.setTotalScanQty(collect1.get(item.getSkuId()));
				pickSkuVO.setIsSn(SkuCache.getById(item.getSkuId()).getIsSn()); //是否序列号
				pickSkuVO.setUserName(AuthUtil.getNickName());// 拣货人
				pickSkuVO.setSkuId(item.getSkuId()); //物品ID
				pickSkuVO.setLotNumber(item.getLotNumber());
				//通过包装id查询包装明细列表
				List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(item.getWspId())
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

					if (skuPackageDetail.getSkuLevel() == item.getSkuLevel()) {
						pickSkuVO.setDefaultPackageDetail(packageViewVO);//set默认包装明细
						pickSkuVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //set规格

						pickSkuVO.setPlanQtyName(skuUmService.convert(item.getWspId(),
							item.getSkuLevel(), item.getPickPlanQty()));
						pickSkuVO.setRealQtyName(skuUmService.convert(item.getWspId(),
							item.getSkuLevel(), item.getPickRealQty()));

					}
					//添加包装列表信息
					packageDetailResultList.add(packageViewVO);
				}
				//set包装明细列表
				pickSkuVO.setPackageDetails(packageDetailResultList);

				try {
					List<Stock> stocks = unPickStockList.stream().filter(u -> {
						return u.getLotNumber().equals(item.getLotNumber())
							&& u.getSkuId().equals(item.getSkuId());
					}).collect(Collectors.toList());
					ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
					pickSkuVO.setSkuLotValue(skuLotService.getById(SkuCache.getById(item.getSkuId()).getWslId()));
					if (stocks.size() > 0) {
						Map<String, Object> skuLotMap = SkuLotWrapper.skuLotToMapWithStock(stocks.get(0));
						SkuLotBaseEntity skuLotDTO = new SkuLotBaseEntity();
						if (Func.isNotEmpty(skuLotMap)) {
							for (String s : skuLotMap.keySet()) {
								String t = s.substring(s.lastIndexOf("t") + 1, s.length());
								skuLotDTO.skuLotSet(Integer.parseInt(t), skuLotMap.get(s).toString());
							}
						}
						pickSkuVO.setSkuLot(skuLotDTO);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//通过拣货计划id查询拣货日志获得序列号
				List<String> serialList = new ArrayList<>();
				List<SoPick> soPickList = soPickListAll.stream().filter(u -> {
					return u.getPickPlanId().equals(item.getPickPlanId());
				}).collect(Collectors.toList());
				if (Func.isNotEmpty(soPickList)) {
					for (SoPick soPick : soPickList) {
						if (Func.isNotEmpty(soPick.getSoDetailCode())) {
							String[] soDetailCodes = soPick.getSoDetailCode().split(",");
							for (int i = 0; i < soDetailCodes.length; i++) {
								String soDetailCode = soDetailCodes[i];
								serialList.add(soDetailCode);
							}
						}
					}
				}
				pickSkuVO.setSerialList(serialList);
				pickTaskVO.getPickPlans().add(pickSkuVO);
			}
		}
		if (Func.isNotEmpty(pickTaskVO.getPickPlans())) {
			pickTaskVO.getPickPlans().sort(new Comparator<PickSkuVO>() {
				@Override
				public int compare(PickSkuVO o1, PickSkuVO o2) {
					Location location1 = LocationCache.getByCode(o1.getSourceLocCode());
					Location location2 = LocationCache.getByCode(o2.getSourceLocCode());
					if (Func.isEmpty(location1) || Func.isEmpty(location2)) {
						return 0;
					}
					if (Func.isEmpty(location1.getLogicAllocation()) || Func.isEmpty(location2.getLogicAllocation())) {
						return 0;
					}
					return location1.getLogicAllocation().compareTo(location2.getLogicAllocation());
				}
			});
		}
		return pickTaskVO;
	}

	@Override
	public PickSkuVO getTotalScanQtyBySku(String wellenNo, String skuCode) {
		PickSkuVO pickSkuVO = new PickSkuVO();
		Wellen wellen = wellenService.getOne(Wrappers.lambdaQuery(Wellen.class).eq(Wellen::getWellenNo, wellenNo));
		if (Func.isEmpty(wellen)) return pickSkuVO;
		List<PickPlan> list = pickPlanService.list(Wrappers.lambdaQuery(PickPlan.class)
			.eq(PickPlan::getWellenId, wellen.getWellenId())
			.eq(PickPlan::getSkuCode, skuCode));
		Map<String, BigDecimal> collect = list.stream().collect(Collectors.groupingBy(PickPlan::getSkuCode))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickRealQty()))
				.map(PickPlan::getPickRealQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
		Map<String, BigDecimal> collect1 = list.stream().collect(Collectors.groupingBy(PickPlan::getSkuCode))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickPlanQty()))
				.map(PickPlan::getPickPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
		pickSkuVO.setTotalScanQty(collect.get(skuCode));
		pickSkuVO.setTotalPlanQty(collect1.get(skuCode));
		SkuDTO skuDTO = SkuCache.get(skuCode);
		Sku sku = skuService.getOne(Wrappers.lambdaQuery(Sku.class).eq(Sku::getSkuCode, skuCode));
		if(Func.isNotEmpty(sku)){
			SkuPackageDetail one = SkuPackageDetailCache.getOne(sku.getWspId(), SkuLevelEnum.Base.getIndex());
			if(Func.isNotEmpty(one)){
				pickSkuVO.setBaseUm(one.getWsuName());
			}
		}
		return pickSkuVO;
	}
}
