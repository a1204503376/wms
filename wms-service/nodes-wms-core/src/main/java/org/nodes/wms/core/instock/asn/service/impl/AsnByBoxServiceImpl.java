package org.nodes.wms.core.instock.asn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.constant.CommonConstant;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.allot.service.IAllotHeaderService;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuLotVal;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.ISkuLogService;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuLotValService;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.core.instock.asn.dto.AsnByBoxSubmitDTO;
import org.nodes.wms.core.instock.asn.dto.AsnDetailSkuLotDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderBoxDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderBoxQueryDTO;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.entity.ContainerLog;
import org.nodes.wms.core.instock.asn.enums.AsnDetailStatusEnum;
import org.nodes.wms.core.instock.asn.mapper.AsnHeaderMapper;
import org.nodes.wms.core.instock.asn.mapper.AsnLpnDetailMapper;
import org.nodes.wms.core.instock.asn.service.IAsnByBoxService;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.IContainerLogService;
import org.nodes.wms.core.instock.asn.service.ISnService;
import org.nodes.wms.core.instock.asn.vo.*;
import org.nodes.wms.core.instock.asn.wrapper.AsnByBoxWrapper;
import org.nodes.wms.core.instock.asn.wrapper.AsnHeaderWrapper;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.service.IAsnInventoryService;
import org.nodes.wms.core.instock.purchase.service.IPoDetailService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.dto.StockAddDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.system.dto.TaskDTO;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 按箱收货实现类
 *
 * @Author zx
 * @Date 2020/6/19
 **/
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AsnByBoxServiceImpl extends AbsBaseAsnHeaderService<AsnHeaderMapper, AsnHeader> implements IAsnByBoxService {

	@Autowired
	private IAsnDetailService asnDetailService;
	@Autowired
	IPoDetailService poDetailService;
	@Autowired
	private ISkuLotService skuLotService;
	@Autowired
	private IStockService stockService;
	@Autowired
	private ISystemProcService systemProcService;
	@Autowired
	private IContainerLogService containerLogService;
	@Autowired
	private ISkuUmService skuUmService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	ISkuLogService skuLogService;
	@Autowired
	AsnLpnDetailMapper asnLpnDetailMapper;
	@Autowired
	IAsnInventoryService asnInventoryService;
	@Autowired
	ISnService snService;

	/**
	 * 按箱收货 通过单据编号查询未完成的单据信息
	 *
	 * @param asnHeader
	 * @return
	 */
	@Override
	public AsnHeaderVO getAsnHeader(AsnHeader asnHeader) {
		if (Func.isEmpty(asnHeader.getAsnBillNo())) throw new ServiceException("单据编号不能为空");
		QueryWrapper<AsnHeader> aqw = new QueryWrapper<>();
		aqw.lambda().eq(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo())
			.ne(AsnHeader::getAsnBillState, AsnBillStateEnum.COMPLETED.getCode());
		AsnHeader header = super.getOne(aqw);
		if (Func.isEmpty(header)) throw new ServiceException("单据已完成或者不存在");
		AsnDetailMinVO asnDetailMinVO = getFinishAsnDetail(header.getAsnBillId());

		AsnHeaderVO result = AsnHeaderWrapper.build().entityVO(header);
		result.setFinish(asnDetailMinVO.getFinish());
		result.setCount(asnDetailMinVO.getCount());

		String boxRule = ParamCache.getValue(ParamEnum.LPN_BOX_RULE.getKey());
		if (Func.isNotEmpty(boxRule)) {
			try {
				result.setRuleCode(Integer.parseInt(boxRule));
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException("箱号规则格式错误");
			}
			if (StringPool.ZERO.equals(boxRule) || StringPool.ONE.equals(boxRule)) {
				result.setBoxCode(SerialNoCache.createBoxCode());
			}
		}

		return result;
	}

	/**
	 * 生成箱号
	 *
	 * @return
	 */
	@Override
	public String createBoxCode() {
		Param param = ParamCache.getOne(ParamEnum.LPN_BOX_RULE.getKey());
		if (StringPool.ZERO.equals(param.getParamValue()) ||
			StringPool.ONE.equals(param.getParamValue())) {
			return SerialNoCache.createBoxCode();
		} else {
			return "";
		}
	}


	/**
	 * 按箱收货 通过单据编号和sku编号查询该单据的物品的明细整合信息
	 *
	 * @param asnHeaderBoxQueryDTO
	 * @return
	 */
	@Override
	public AsnHeaderBoxQueryVO getAsnHeaderDetail(AsnHeaderBoxQueryDTO asnHeaderBoxQueryDTO) {
		// 查询当前订单的当前物品有多少个明细
		List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail())
			.lambda()
			.eq(AsnDetail::getSkuCode, asnHeaderBoxQueryDTO.getSkuCode())
			.eq(AsnDetail::getAsnBillNo, asnHeaderBoxQueryDTO.getAsnBillNo())
			.orderByAsc(AsnDetail::getAsnLineNo));
		AsnDetailSkuLotDTO skuLots = asnHeaderBoxQueryDTO.getSkuLots();
		List<AsnDetail> collect = asnDetailList.stream().filter(asnDetail1 -> {
			if (Func.isNotEmpty(skuLots)) {
				List<Boolean> flags = new ArrayList<>();
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (asnDetail1.skuLotGet(i).equals(skuLots.skuLotGet(i))) {
						flags.add(true);
					}
				}
				return flags.size() == ParamCache.LOT_COUNT;
			}
			return false;
		}).collect(Collectors.toList());
		AsnDetail asnDetail = asnDetailList.get(0);
		if (Func.isNotEmpty(skuLots) && Func.isEmpty(collect)) {
			throw new ServiceException("没有查询到该批次物品明细！");
		}
		if (Func.isNotEmpty(skuLots) && Func.isNotEmpty(collect)) {
			asnDetail = collect.get(0);
		}
		//验证物品明细包装是否相同
		Long wspId = CommonConstant.TOP_PARENT_ID;
		BigDecimal sum = BigDecimal.ZERO;
		for (AsnDetail asnDetail1 : asnDetailList) {
			if (wspId == CommonConstant.TOP_PARENT_ID) {
				wspId = asnDetail1.getWspId();
			} else if (!wspId.equals(asnDetail1.getWspId())) {
				throw new ServiceException(String.format(
					"单据[%s]中的物品[%s]包装必须相同",
					asnHeaderBoxQueryDTO.getAsnBillNo(), asnHeaderBoxQueryDTO.getSkuCode()));
			}
			sum = sum.add(asnDetail1.getPlanQty());
		}
		BigDecimal scanQtys = asnDetailList.stream().map(AsnDetail::getScanQty).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (Func.isEmpty(asnDetailList)) {
			throw new ServiceException(String.format("单据[%s]没有物品[%s],或者物品已收满",
				asnHeaderBoxQueryDTO.getAsnBillNo(), asnHeaderBoxQueryDTO.getSkuCode()));
		}

		// 比较收货明细的批属性，确定明细行
//		if (Func.isNotEmpty(asnHeaderBoxQueryDTO.getSkuLots())) {
//			for (AsnDetail item : asnDetailList) {
//				boolean isSkuLot = true;
//				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
//					if (!item.skuLotGet(i).equals(asnHeaderBoxQueryDTO.getSkuLots().skuLotGet(i))) {
//						isSkuLot = false;
//						break;
//					}
//				}
//				if (isSkuLot && BigDecimalUtil.ne(item.getSurplusQty(), BigDecimal.ZERO)) {
//					asnDetail = item;
//					break;
//				}
//			}
//			if(Func.isEmpty(asnDetail)){
//				asnDetail = asnDetailList.get(0);
//			}
//		} else {
//			asnDetail = asnDetailList.get(0);
//		}
		if (Func.isEmpty(asnDetail)) {
			throw new ServiceException(String.format("单据[%s]没有单位[%s]的物品[%s],或者物品已收满",
				asnHeaderBoxQueryDTO.getAsnBillNo(), asnHeaderBoxQueryDTO.getWsuName(),
				asnHeaderBoxQueryDTO.getSkuCode()));
		}
		//获得物品
		Sku sku = SkuCache.getById(asnDetail.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或已经删除");
		}
		//计算明细表实际数量与计划数量
		AsnDetailMinVO minVO = this.getMinVoByBillId(asnDetailList);
		//包装头表
		SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
		if (Func.isEmpty(skuPackage)) {
			throw new ServiceException("包装不存在或已经删除");
		}
		//包装明细列表
		List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
		if (Func.isEmpty(skuPackageDetails)) {
			throw new ServiceException(String.format("包装[%s]没有包装明细", skuPackage.getWspId()));
		}
		//批属性规则
		List<SkuLotConfigVO> skuConfigs = new ArrayList<>();
		skuConfigs = skuLotService.getConfig(asnDetail.getSkuId());
		if (Func.isEmpty(skuConfigs)) {
			throw new ServiceException(String.format("物品[%s]没有批属性规则", asnDetail.getSkuCode()));
		}
		List<AsnDetail> list = new ArrayList<>();
		list.add(asnDetail);

		AsnHeaderBoxQueryVO vo = new AsnHeaderBoxQueryVO();
		vo.setSku(sku);
		vo.setAsnDetails(list);
		vo.setSkuPackage(skuPackage);
		vo.setSkuPackageDetails(skuPackageDetails);
		vo.setAsnDetail(minVO);
		vo.setSkuConfig(skuConfigs);
		vo.setTotalPlanQty(sum);
		vo.setTotalScanQty(scanQtys);
		return vo;
	}

	/**
	 * 按箱收货 提交入库
	 *
	 * @param boxSubmitVO
	 * @return
	 */
	@Override
	public synchronized Map<String, Object> submitAsnHeader(AsnHeaderBoxSubmitVO boxSubmitVO) {
		//结果集
		Map<String, Object> result = new HashMap<>();
		//参数为空校验
		AsnByBoxWrapper.build().verifyInstock(boxSubmitVO);
		//VO转DTO
		AsnHeaderBoxDTO asnHeaderBoxDTO = AsnByBoxWrapper.build().submitVOToDto(boxSubmitVO);
		//保存入库信息
		this.saveInstock(asnHeaderBoxDTO);
		//是否完成收货后生成上架任务,如果不生成上架任务则完成收货后跳转到上架页面
		boolean isTurnPutaway = this.updateAsnHeaderState(asnHeaderBoxDTO.getAsnBillId());
		//计算入库单据进度
		AsnDetailMinVO minVO = this.getAsnHeaderProgress(asnHeaderBoxDTO.getAsnBillId());
		result.put("asnDetail", minVO);
		result.put("isTurnPutaway", isTurnPutaway);
		return result;
	}

	/**
	 * 按箱收货 修改单据状态
	 *
	 * @param asnBillId 入库单主键ID
	 * @return
	 */
	private synchronized boolean updateAsnHeaderState(Long asnBillId) {
		AsnHeader asnHeader = super.getById(asnBillId);
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("入库单不存在(ID:" + asnBillId + ")！");
		}
		List<AsnDetail> details = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail()).lambda()
			.eq(AsnDetail::getAsnBillId, asnBillId));
		boolean ifFinish = true;
		int billState = 0;
		for (AsnDetail detail : details) {
			if (0 != detail.getSurplusQty().compareTo(BigDecimal.ZERO)) {
				ifFinish = false;
			}
		}
		billState = ifFinish ? AsnBillStateEnum.COMPLETED.getCode() : AsnBillStateEnum.PART.getCode();
		//修改值
		AsnHeader update = new AsnHeader();
		update.setAsnBillState(billState);
		//是否自动生成上架任务
		boolean isAutoPutaway = false;
		if (ifFinish) {
			update.setFinishDate(LocalDateTime.now());
			taskService.closeTask(asnBillId, TaskTypeEnum.Check);

			isAutoPutaway = StringPool.ONE.equals(ParamCache.getValue(ParamEnum.TASK_PUTAWAY_MODE.getKey()));
			if (isAutoPutaway) {
				TaskDTO task = new TaskDTO();
				task.setTaskType(TaskTypeEnum.Putaway.getIndex());
				task.setBillId(asnBillId);
				taskService.create(task);
			}
		}
		AsnHeader asnBill = super.getOne(Condition.getQueryWrapper(new AsnHeader()).lambda()
			.eq(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo()));
		//实际到货时间
		if (ObjectUtil.isNotEmpty(asnBill) && Func.isEmpty(asnBill.getActualArrivalDate()))
			update.setActualArrivalDate(LocalDateTime.now());
		//修改时的查询条件,按单查询
		UpdateWrapper<AsnHeader> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda().eq(AsnHeader::getAsnBillNo, asnHeader.getAsnBillNo());
		super.update(update, updateWrapper);

		return isAutoPutaway;
	}

	/**
	 * 按箱收货 查询收货单进度
	 *
	 * @param asnBillId
	 * @return
	 */
	protected AsnDetailMinVO getAsnHeaderProgress(Long asnBillId) {
		//计算明细表实际数量与计划数量
		AsnDetailMinVO minVO = new AsnDetailMinVO();
		minVO.setFinish(getFinishAsnDetail(asnBillId).getFinish());
		minVO.setCount(getFinishAsnDetail(asnBillId).getCount());
		return minVO;
	}

	/**
	 * 按箱收货 通过单据ID查询单据内明细物品收货数量,规格,明细状态
	 *
	 * @param list
	 * @return
	 */
	protected AsnDetailMinVO getMinVoByBillId(List<AsnDetail> list) {
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
				throw new ServiceException(String.format("物品[%s]不存在或已经删除", asnDetail.getSkuId()));
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
	 * 按箱收货 查询单据内明细完成进度
	 *
	 * @param asnBillId
	 * @return
	 */
	public AsnDetailMinVO getFinishAsnDetail(Long asnBillId) {
		AsnDetail param = new AsnDetail();
		param.setAsnBillId(asnBillId);
		List<AsnDetail> list = asnDetailService.list(Condition.getQueryWrapper(param));
		int finish = 0;
		int count = list.size();
		for (AsnDetail asnDetail : list) {
			if (0 == asnDetail.getSurplusQty().compareTo(BigDecimal.ZERO)) { // 没有完成的;
				finish++;
			}
		}
		AsnDetailMinVO vo = new AsnDetailMinVO();
		vo.setFinish(finish);
		vo.setCount(count);
		return vo;
	}

	/**
	 * 按箱收货 保存入库的单据信息
	 *
	 * @param asnHeaderBoxDTO
	 * @return
	 */
	private boolean saveInstock(AsnHeaderBoxDTO asnHeaderBoxDTO) {
		//通过单据编号查询单据信息
		QueryWrapper<AsnHeader> ahqw = new QueryWrapper<>();
		ahqw.lambda().eq(AsnHeader::getAsnBillNo, asnHeaderBoxDTO.getAsnBillNo());
		AsnHeader asnHeader = super.getOne(ahqw);
		if (asnHeader.getAsnBillState() == AsnBillStateEnum.COMPLETED.getCode()) {
			throw new ServiceException("该单据已完成或者已关闭");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("订单：" + asnHeader.getAsnBillNo() + " 已取消，请停止收货！");
		}
		//供应商 用于清点记录
//		asnHeaderBoxDTO.setSName(asnHeader.getSName());
		AsnInventory asnInventory = null;
		Task task = taskService.getById(asnHeaderBoxDTO.getTaskId());
		if (Func.isNotEmpty(task)) {
			asnInventory = asnInventoryService.getOne(Condition.getQueryWrapper(new AsnInventory()).lambda()
				.eq(AsnInventory::getAsnBillNo, task.getTaskRemark()));
		}
		//判断库位是否存在
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("单据所选库房不存在或已经删除");
		}
		//查询库位对象,对库位是否可用进行验证
		Location loc = LocationCache.getValid(
			Func.firstCharToUpper(asnHeaderBoxDTO.getLocCode().toLowerCase()) + warehouse.getWhCode());
		//库位ID
		Long locId = loc.getLocId();
		//收货后更新任务开始时间
		taskService.updateBeginTime(asnHeaderBoxDTO.getAsnBillNo(), TaskTypeEnum.Check);
		//查询代收单据的物品明细列表
		QueryWrapper<AsnDetail> asnDetailQueryWrapper = Condition.getQueryWrapper(new AsnDetail());

		if (Func.isNotEmpty(asnHeaderBoxDTO.getSkuLots())) {
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (Func.isEmpty(asnHeaderBoxDTO.getSkuLots().skuLotGet(i))) {
					continue;
				}
				asnDetailQueryWrapper.eq("sku_lot" + i, asnHeaderBoxDTO.getSkuLots().skuLotGet(i));
			}
		}
		asnDetailQueryWrapper.lambda()
			.eq(AsnDetail::getAsnBillNo, asnHeaderBoxDTO.getAsnBillNo())
			.eq(AsnDetail::getSkuCode, asnHeaderBoxDTO.getSkuCode());
		List<AsnDetail> asnDetails = asnDetailService.list(asnDetailQueryWrapper);
		if (asnDetails.size() <= 0) {
			throw new ServiceException(String.format("单据内没有物品[%s]", asnHeaderBoxDTO.getSkuCode()));
		}
		//验证物品明细包装是否相同
		Long wspId = 0L;
		for (AsnDetail asnDetail : asnDetails) {
			if (wspId == 0L) {
				wspId = asnDetail.getWspId();
			} else {
				if (wspId.equals(asnDetail.getWspId())) {
					continue;
				} else {
					throw new ServiceException(String.format("单据[%s]中的物品[%s]包装必须相同"
						, asnHeaderBoxDTO.getAsnBillNo(), asnHeaderBoxDTO.getSkuCode()));
				}
			}
		}
		//DTO内参数封装
		//库位ID
		asnHeaderBoxDTO.setLocId(locId);
		//包装ID
		asnHeaderBoxDTO.setWspId(asnDetails.get(0).getWspId());
		//库房ID
		asnHeaderBoxDTO.setWhId(asnHeader.getWhId());
		//单据ID
		asnHeaderBoxDTO.setAsnBillId(asnHeader.getAsnBillId());
		//物品ID
		asnHeaderBoxDTO.setSkuId(asnDetails.get(0).getSkuId());

		Sku sku = SkuCache.getById(asnHeaderBoxDTO.getSkuId());
		if (Func.isEmpty(sku)) {
			throw new ServiceException("物品不存在或已经删除");
		}
		SkuPackage skuPackage = SkuPackageCache.getById(asnHeaderBoxDTO.getWspId());
		if (Func.isEmpty(skuPackage)) {
			throw new ServiceException("包装不存在(包装ID：" + asnHeaderBoxDTO.getWspId() + ")");
		}
		//收货实际数量物品数量
		BigDecimal realQty = asnHeaderBoxDTO.getScanQty();
		//物品批属性规则
		ISkuLotValService skuLotValService = SpringUtil.getBean(ISkuLotValService.class);
		SkuLotVal skuLotVal = skuLotValService.getById(sku.getWslvId());
		if (Func.isEmpty(skuLotVal)) throw new ServiceException(String.format("物品[%s]的批属性验证规则不完善"));
		// 当前批属性下的单据明细物品总数
		BigDecimal surplusQty = BigDecimal.ZERO;
		for (AsnDetail asnDetail : asnDetails) {
			surplusQty = surplusQty.add(asnDetail.getSurplusQty());
		}
		//首先判断是否允许超收 0:不允许,1:允许
		if (0 == ParamCache.IS_OVER_VALUE) {
			// 不允许超收的情况下 若超收则提示不可超收
			if (realQty.compareTo(surplusQty) > 0) {
				throw new ServiceException("接收数量不能超过计划数量");
			}
		}
		//系统日志记录
		Long systemProcId = saveSystemProcLog(asnHeaderBoxDTO, realQty);
		// 计划接收数量
		BigDecimal planQty;
		//开始给每行明细表单更新状态
		for (int i = 0; i < asnDetails.size(); i++) {
			//获得对象
			AsnDetail asnDetail = asnDetails.get(i);
			//若本次存入数量>0 说明还有未存入的数量
			if (realQty.compareTo(BigDecimal.ZERO) > 0) {
				//是否是序列号管理 0:不是,1:是
				planQty = asnDetail.getPlanQty();
				//规格 清点记录用
				asnHeaderBoxDTO.setSkuSpec(asnDetail.getSkuSpec());
				//计划数量 清点记录用
				asnHeaderBoxDTO.setPlanQty(planQty);
				//存储参数
				AsnDetail asnDetail1 = asnDetailService.getById(asnDetail.getAsnDetailId());
				//已接收状态
				asnDetail1.setDetailStatus(20);
				//判断是否是最后一行 等于则是最后一行
				if (i == asnDetails.size() - 1) {
					//存入数量迭代
					asnDetail1.setScanQty(asnDetail.getScanQty().add(realQty));
					asnDetail1.setSurplusQty(planQty.subtract(asnDetail.getScanQty().add(realQty)).max(BigDecimal.ZERO));
					//保存增加的库存信息
					this.saveAddStockInfo(asnHeaderBoxDTO, asnInventory, asnDetail, realQty, systemProcId);
					asnDetailService.updateById(asnDetail1);
					// 更新PO明细
					poDetailService.updateQty(asnDetail1);
					break;
				} else {
					//不是最后一行
					// 得到剩余量
					BigDecimal surplusCount;
					if (planQty.compareTo(asnDetail.getScanQty()) <= 0) continue;
					surplusCount = planQty.subtract(asnDetail.getScanQty());
					//本行实际存入的物品数量
					BigDecimal scanCount = surplusCount.min(realQty);
					//迭代结束的实际数量
					BigDecimal lastCount = scanCount.add(asnDetail.getScanQty());

					//保存增加的库存信息
					this.saveAddStockInfo(asnHeaderBoxDTO, asnInventory, asnDetail, scanCount, systemProcId);
					realQty = realQty.subtract(surplusCount);
					asnDetail1.setScanQty(lastCount);
					asnDetail1.setSurplusQty(planQty.subtract(lastCount).max(BigDecimal.ZERO));
					asnDetailService.updateById(asnDetail1);
					// 更新PO明细
					poDetailService.updateQty(asnDetail1);
				}
			}
		}
		return true;
	}


	/**
	 * 按箱收货 增加的库存信息
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @param systemProcId
	 * @return
	 */
	private Stock saveAddStockInfo(AsnHeaderBoxDTO dto, AsnInventory asnInventory, AsnDetail asnDetail, BigDecimal count, Long systemProcId) {
		BladeUser user = AuthUtil.getUser();
		StockAddDTO stockProc = Objects.requireNonNull(BeanUtil.copy(dto, StockAddDTO.class));
		stockProc.setSystemProcId(systemProcId);//日志id
		stockProc.setLocId(dto.getLocId());//库位id
		stockProc.setSkuId(dto.getSkuId());//物品id
		stockProc.setWspId(dto.getWspId());//包装id
		stockProc.setStockQty(count);//数量
		stockProc.setEventType(EventTypeEnum.Check);//事务类型
		stockProc.setBoxCode(dto.getBoxCode());//箱号
		stockProc.setBillId(asnDetail.getAsnBillId());//收货单id
		stockProc.setBillDetailId(asnDetail.getAsnDetailId());//收货单明细id
		stockProc.setBillNo(asnDetail.getAsnBillNo());//收货单编码

		//批属性
		Map<String, Object> skuLotMap = new HashMap<>();
		try {
			skuLotMap = SkuLotWrapper.skuLotToMap(dto.getSkuLots());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String key : skuLotMap.keySet()) {
			if (Func.isNotEmpty(key)) {
				stockProc.skuLotSet(Integer.parseInt(key.substring(6)), skuLotMap.get(key).toString());
			}
		}

		Stock stock = stockService.add(stockProc);
		//清点记录
		this.saveContainerLog(dto, asnInventory, asnDetail, count, stock);
		return stock;
	}

	/**
	 * 按箱收货 保存清点记录
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @return
	 */
	private boolean saveContainerLog(AsnHeaderBoxDTO dto, AsnInventory asnInventory, AsnDetail asnDetail, BigDecimal count, Stock stock) {
		//插入清点记录
		ContainerLog containerLog = new ContainerLog();
		containerLog.setAsnBillId(asnDetail.getAsnBillId()); //订单id
		containerLog.setAsnBillNo(dto.getAsnBillNo()); //单据编码
		if (Func.isNotEmpty(asnInventory)) {
			containerLog.setInventoryId(asnInventory.getId());
		}
		containerLog.setAsnDetailId(asnDetail.getAsnDetailId()); //订单明细id
		containerLog.setBoxCode(dto.getBoxCode());//箱号
		containerLog.setLocId(dto.getLocId());//库位id
		containerLog.setLocCode(dto.getLocCode());//库位编码
		containerLog.setAclQty(count); //数量
		containerLog.setSkuId(asnDetail.getSkuId());//物品id
		containerLog.setSkuName(asnDetail.getSkuName()); //物品名称
		containerLog.setWspId(asnDetail.getWspId());
		containerLog.setSkuLevel(asnDetail.getSkuLevel()); //包装层级
		containerLog.setAclStatus(1); //清点状态
		containerLog.setLotNumber(stock.getLotNumber()); //批次号
		containerLog.setSkuSpec(dto.getSkuSpec()); //规格
		containerLog.setPlanQty(dto.getPlanQty()); //计划数量
		containerLogService.save(containerLog);
		return true;
	}

	/**
	 * 按箱收货 保存系统日志
	 *
	 * @param dto
	 * @param count
	 */
	private Long saveSystemProcLog(AsnHeaderBoxDTO dto, BigDecimal count) {
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.ASN);
		systemProcParam.setDataType(DataTypeEnum.AsnBillNo);
		systemProcParam.setAction(ActionEnum.RECEIVE_BOX);
		systemProcParam.setBillNo(dto.getAsnBillNo());
		systemProcParam.setOperationQty(count);
		systemProcParam.setOperationUnit(dto.getWsuName());
		systemProcParam.setWhId(dto.getWhId());
		return systemProcService.create(systemProcParam).getSystemProcId();
	}

	@Override
	public synchronized AsnByBoxSubmitVO submit(AsnByBoxSubmitDTO asnByBoxSubmitDTO) {

		// 获取入库单
		AsnHeader asnHeader = super.getOne(Wrappers.lambdaQuery(AsnHeader.class)
			.eq(AsnHeader::getAsnBillNo, asnByBoxSubmitDTO.getAsnBillNo()));
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("订单不存在(订单ID:" + asnByBoxSubmitDTO.getAsnBillId() + ")");
		}
		if (asnHeader.getAsnBillState() == AsnBillStateEnum.COMPLETED.getCode()) {
			throw new ServiceException("该单据已完成或者已关闭");
		} else if (asnHeader.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("订单：" + asnHeader.getAsnBillNo() + " 已取消，请停止收货！");
		}
		Location location = checkLoc(asnHeader.getWhId(), asnByBoxSubmitDTO.getLocCode());
		// 更新任务开始时间
		taskService.updateBeginTime(asnByBoxSubmitDTO.getAsnBillNo(), TaskTypeEnum.Check);
		// 获取当前收货的所有明细
		List<AsnDetail> asnDetails = asnDetailService.list(Wrappers.lambdaQuery(AsnDetail.class)
			.eq(AsnDetail::getAsnBillId, asnHeader.getAsnBillId()));
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getBoxCode, asnByBoxSubmitDTO.getBoxCodes()));
		List<String> boxCodesTemp = NodesUtil.toList(stockDetails, StockDetail::getBoxCode);
		for (String boxcode : asnByBoxSubmitDTO.getBoxCodes()) {
			if (!boxCodesTemp.contains(boxcode)) {
				throw new ServiceException(String.format("没有查询到[%s]该箱号物品信息！", boxcode));
			}
			StockDetail stockDetail1 = stockDetails.stream()
				.filter(stockDetail -> stockDetail.getBoxCode().equals(boxcode)).findFirst().orElse(null);
//			Stock stock = stockService.list(Wrappers.lambdaQuery(Stock.class)
//				.eq(Stock::getSkuId, stockDetail1.getSkuId())
//				.eq(Stock::getLocId, location.getLocId())).stream().findFirst().orElse(null);

			SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(stockDetail1.getWspId(), SkuLevelEnum.Box.getIndex());
			if (Func.isEmpty(packageDetail)) {
				throw new ServiceException("包装明细不存在或者已删除");
			}

			StockAddDTO stockProc = new StockAddDTO();
			stockProc.setLocId(location.getLocId());
			stockProc.setSkuId(stockDetail1.getSkuId());
			stockProc.setWspId(stockDetail1.getWspId());
			stockProc.setBoxCode(boxcode);
			stockProc.setLotNumber(stockDetail1.getLotNumber());
			stockProc.setBillDetailId(stockDetail1.getBillDetailId());
			stockProc.setBillId(stockDetail1.getSoBillId());
			stockProc.setStockQty(stockDetail1.getUnreceivedQty());
			stockProc.setLpnCode("");
			stockProc.setWellenId(null);
			stockProc.setSdId(stockDetail1.getId());
			stockProc.setSerialList(null);
			AsnHeaderBoxDTO asnHeaderBoxDTO = new AsnHeaderBoxDTO();
			asnHeaderBoxDTO.setBoxCode(boxcode);
			asnHeaderBoxDTO.setWspId(stockDetail1.getWspId());
			Long systemProcId = saveSystemProcLog(asnHeaderBoxDTO, stockDetail1.getUnreceivedQty());
			stockProc.setSystemProcId(systemProcId);
			stockService.add(stockProc);

			List<AsnDetail> asnDetailList = asnDetails.stream().filter(asnDetail -> asnDetail.getSkuId().equals(stockDetail1.getSkuId())).collect(Collectors.toList());
			List<AsnDetail> unUpdateAsnDetails = new ArrayList<>();
			for (AsnDetail asnDetail : asnDetailList) {
				if (BigDecimalUtil.eq(asnDetail.getSurplusQty(), stockDetail1.getUnreceivedQty())) {
					asnDetail.setSurplusQty(BigDecimal.ZERO);
					asnDetail.setScanQty(asnDetail.getScanQty().add(stockDetail1.getUnreceivedQty()));
					asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
					unUpdateAsnDetails.add(asnDetail);
					break;
				}
				if (BigDecimalUtil.lt(asnDetail.getSurplusQty(), stockDetail1.getUnreceivedQty())) {
					asnDetail.setSurplusQty(BigDecimal.ZERO);
					asnDetail.setScanQty(asnDetail.getScanQty().add(asnDetail.getSurplusQty()));
					asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
					unUpdateAsnDetails.add(asnDetail);
					continue;
				}
				if (BigDecimalUtil.gt(asnDetail.getSurplusQty(), stockDetail1.getUnreceivedQty())) {
					asnDetail.setSurplusQty(asnDetail.getSurplusQty().subtract(stockDetail1.getUnreceivedQty()));
					asnDetail.setScanQty(asnDetail.getScanQty().add(stockDetail1.getUnreceivedQty()));
					asnDetail.setDetailStatus(AsnDetailStatusEnum.RECEIVED.getIndex());
					unUpdateAsnDetails.add(asnDetail);
					continue;
				}
			}
			asnDetailService.updateBatchById(unUpdateAsnDetails);
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
		return null;
	}

	/**
	 * 修改入库单状态
	 *
	 * @param asnBillId
	 * @param asnBillState
	 * @return
	 */
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
		if (AsnBillStateEnum.COMPLETED.equals(asnBillState)) {
			// 更新调拨单状态
//			allotHeaderService.updateBillState(asnHeader.getOrderNo(), AllotBillStateEnum.COMPLETED);
		} else if (AsnBillStateEnum.PART.equals(asnBillState)) {
//			allotHeaderService.updateBillState(asnHeader.getOrderNo(), AllotBillStateEnum.INSTOCKING);
		}
		return asnHeader;
	}
//	@Override
//	public StockVO getLabelInfoWithStock(AsnHeaderBoxQueryDTO asnHeaderBoxQueryDTO) {
//		if(Func.isNotEmpty(asnHeaderBoxQueryDTO.getSkuCode())){
//			QueryWrapper<Stock> stockLambdaQueryWrapper = Condition.getQueryWrapper(new Stock());
//			List<Sku> skuList = SkuCache.listByCode(asnHeaderBoxQueryDTO.getSkuCode());
//			stockLambdaQueryWrapper.lambda().in(Stock::getSkuId, NodesUtil.toList(skuList, "skuId", Long.class));
//			AsnDetailSkuLotDTO skuLots = asnHeaderBoxQueryDTO.getSkuLots();
//			if(Func.isNotEmpty(skuLots)){
//				Field[] declaredFields = AsnDetailSkuLotDTO.class.getDeclaredFields();
//				int index = 1;
//				for(Field field:declaredFields){
//					field.setAccessible(true);
//					if(index<SystemSetting.LOT_COUNT_VALUE){
//						try {
//							Object o = field.get(skuLots);
//							String columnName = StringUtil.humpToUnderline(field.getName());
//							stockLambdaQueryWrapper.eq(columnName, o);
//						} catch (IllegalAccessException e) {
//							e.printStackTrace();
//						}
//					}
//					index++;
//				}
//			}
//		}
//
//		return null;
//	}
}
