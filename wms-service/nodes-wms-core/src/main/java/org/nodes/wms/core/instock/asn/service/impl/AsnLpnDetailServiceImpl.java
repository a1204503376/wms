package org.nodes.wms.core.instock.asn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.dto.SkuLogDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuLogService;
import org.nodes.wms.core.instock.asn.dto.AsnDTO;
import org.nodes.wms.core.instock.asn.entity.*;
import org.nodes.wms.core.instock.asn.enums.AsnDetailStatusEnum;
import org.nodes.wms.core.instock.asn.mapper.AsnHeaderMapper;
import org.nodes.wms.core.instock.asn.mapper.AsnLpnDetailMapper;
import org.nodes.wms.core.instock.asn.service.*;
import org.nodes.wms.core.instock.asn.vo.AsnDetailMinVO;
import org.nodes.wms.core.instock.asn.vo.AsnHeaderVO;
import org.nodes.wms.core.instock.asn.vo.AsnLpnDetailVO;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.service.IAsnInventoryService;
import org.nodes.wms.core.instock.purchase.service.IPoDetailService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.stock.core.dto.StockAddDTO;
import org.nodes.wms.core.stock.core.dto.StockMoveDTO;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.utils.SkuLotUtil;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.sku.enums.SkuLogTypeEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * ?????????????????? ???????????????
 *
 * @author zhonglianshuai
 * @since 2020-02-14
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AsnLpnDetailServiceImpl<M extends AsnLpnDetailMapper, T extends AsnLpnDetail>
	extends BaseServiceImpl<AsnLpnDetailMapper, AsnLpnDetail>
	implements IAsnLpnDetailService {

	@Autowired
	IAsnHeaderService asnHeaderService;
	@Autowired
	IPoDetailService poDetailService;
	@Autowired
	IStockService stockService;
	@Autowired
	IAsnDetailService asnDetailService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	IContainerLogService containerLogService;
	@Autowired
	AsnHeaderMapper asnHeaderMapper1;
	@Autowired
	ISnService snService;
	@Autowired
	ISkuLogService skuLogService;
	@Autowired
	ITaskService taskService;
	@Autowired
	IAsnInventoryService asnInventoryService;

	@Override
	public IPage<AsnLpnDetailVO> selectLpnDetailPage(IPage<AsnLpnDetailVO> page, AsnLpnDetailVO lpnDetail) {
		return page.setRecords(baseMapper.selectLpnDetailPage(page, lpnDetail));
	}

	@Override
	public Map<String, Object> getAsnLpnDetail(AsnLpnDetail lpnDetail) {
		Map<String, Object> result = new HashMap<>();
		//????????????????????????
		AsnLpnDetail asnLpnDetail = super.getOne(Condition.getQueryWrapper(lpnDetail));
		if (ObjectUtil.isEmpty(asnLpnDetail))
			throw new RuntimeException("???????????????????????????");
		/*else if (Func.isNotEmpty(asnLpnDetail.getLpnStatus()) && asnLpnDetail.getLpnStatus().equals(1))
			throw new RuntimeException("?????????????????????");*/

		//???????????????????????????
		AsnHeader asnHeader = asnHeaderService.getById(asnLpnDetail.getAsnBillId());
		AsnHeaderVO asnHeaderVO = new AsnHeaderVO();
		if (ObjectUtil.isNotEmpty(asnHeader)) {
			asnHeaderVO = BeanUtil.copy(asnHeader, AsnHeaderVO.class);
			if (Func.isNotEmpty(asnHeaderVO.getWhId())) {
				Warehouse warehouse = WarehouseCache.getById(asnHeaderVO.getWhId());
				if (warehouse != null && Func.isNotEmpty(warehouse.getWhName()))
					asnHeaderVO.setWhName(warehouse.getWhName());
			}
			if (Func.isNotEmpty(asnHeaderVO.getWoId())) {
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(asnHeaderVO.getWoId());
				if (owner != null && Func.isNotEmpty(owner.getOwnerName())) {
					asnHeaderVO.setOwnerName(owner.getOwnerName());
				}
			}
		}
		List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(new AsnDetail()).lambda().eq(AsnDetail::getAsnDetailId, asnLpnDetail.getAsnDetailId()));
		//??????????????????????????????????????????
		AsnDetailMinVO minVO = this.getMinVoByBillId(asnDetailList);
		//????????????
		SkuPackage skuPackage = SkuPackageCache.getById(asnDetailList.get(0).getWspId());
		//??????????????????
		List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(skuPackage.getWspId());
		if (skuPackageDetails.size() <= 0) {
			result.put("msg", String.format("??????[%s]??????????????????", skuPackage.getWspId()));
			result.put("success", false);
			return result;
		}
		//??????????????????
		Sku sku = SkuCache.getById(asnLpnDetail.getSkuId());

		result.put("sku", sku);
		result.put("asnHeader", asnHeaderVO);
		result.put("asnDetails", asnDetailList);
		result.put("skuPackage", skuPackage);
		result.put("skuPackageDetails", skuPackageDetails);
		result.put("asnDetail", minVO);
		result.put("asnLpnDetail", asnLpnDetail);

		return result;
	}

	@Override
	public Boolean creatLpn(String asnBillIds) {
		List<Long> longListIds = Func.toLongList(asnBillIds);
		for (Long ids : longListIds) {
			AsnHeader asnHeader = asnHeaderService.getById(ids);
			if (!asnHeader.getAsnBillState().equals(AsnBillStateEnum.NOT_RECEIPT.getCode())) {
				throw new ServiceException(String.format(
					"?????????????????????[%s] ???????????????????????????????????????????????????[%s]! ",
					AsnBillStateEnum.NOT_RECEIPT.getDesc(), AsnBillStateEnum.valueOf(AsnBillStateEnum.class, asnHeader.getAsnBillState().toString())));
			}
			AsnDetail paramDetail = new AsnDetail();
			paramDetail.setAsnBillId(asnHeader.getAsnBillId());
			boolean minLevel = false;
			boolean maxLevel = false;
			List<AsnDetail> asnDetailList = asnDetailService.list(Condition.getQueryWrapper(paramDetail)
				.lambda().orderByAsc(AsnDetail::getAsnLineNo));
			if (Func.isEmpty(asnDetailList)) {
				throw new ServiceException("????????????????????????" + asnHeader.getAsnBillNo());
			}
			Long receiveCount = asnDetailList.stream().filter(item -> {
				return item.getDetailStatus() == AsnDetailStatusEnum.RECEIVED.getIndex();
			}).count();
			if (receiveCount > 0) {
				throw new ServiceException("??????????????????????????????????????????????????????LPN--" + asnHeader.getAsnBillNo());
			}
			//?????????????????????????????????
			this.remove(new QueryWrapper<AsnLpnDetail>().lambda()
				.eq(AsnLpnDetail::getAsnBillId, ids));
			for (AsnDetail asnDetail : asnDetailList) {
				//????????????
				Sku sku = SkuCache.getById(asnDetail.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("??????????????????????????????--" + sku.getSkuName());
				}
				//???????????????????????????
				if (asnDetail.getConvertQty().equals(0))//??????????????????0????????????1?????????????????????
				{
					asnDetail.setConvertQty(1);
				}
				BigDecimal allQty = asnDetail.getPlanQty();
				if (Func.isEmpty(allQty)) {
					throw new ServiceException("??????????????????????????? " + asnDetail.getSkuName() + " ??????????????????");
				}
				int countLpn = 0;//??????lpn??????
				int lpnQty = 0;
				//?????????????????????????????????
				SkuPackage skuPackage = SkuPackageCache.getById(asnDetail.getWspId());
				//??????????????????
				List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(asnDetail.getWspId());
				if (skuPackageDetailList.size() <= 0)
					throw new ServiceException("???????????????????????????--" + skuPackage.getWspName());
				SkuPackageDetail skuPackageDetailMin = new SkuPackageDetail();

				for (SkuPackageDetail skuPackageDetail : skuPackageDetailList) {
					if (Func.toInt(skuPackageDetail.getSkuLevel()) == SkuLevelEnum.Base.getIndex()) {
						minLevel = true;
						skuPackageDetailMin = skuPackageDetail;
					}
					if (Func.toInt(skuPackageDetail.getSkuLevel()) == SkuLevelEnum.Plate.getIndex()) {
						maxLevel = true;
						if (allQty.intValue() % Func.toInt(skuPackageDetail.getConvertQty()) > 0) {

							throw new ServiceException("??????????????????????????????--" + asnHeader.getAsnBillNo());
						}
						countLpn = allQty.intValue() / Func.toInt(skuPackageDetail.getConvertQty());
						lpnQty = Func.toInt(skuPackageDetail.getConvertQty());
					}

				}
				if (!minLevel) {
					throw new ServiceException(String.format(
						"?????????[%s]??????????????????????????????! " + skuPackage.getWspName()));
				}
				if (!maxLevel) {
					throw new ServiceException(String.format(
						"?????????[%s]?????????????????????????????????! ", skuPackage.getWspName(), SkuLevelEnum.Base.getName()));
				}
				if (countLpn <= 0) {
					throw new ServiceException(String.format(
						"?????????[%s]??????????????????[%s]??????????????????", skuPackage.getWspName(), SkuLevelEnum.Plate.getName()));
				}
				//?????????????????????????????????????????????????????????????????????
				List<String> detailCode = new ArrayList<>();
				if (sku.getIsSn().equals(1)) {
					QueryWrapper<Sn> queryWrapper = new QueryWrapper<>();
					queryWrapper.lambda()
						.eq(Sn::getAsnBillId, asnDetail.getAsnBillId())
						.eq(Sn::getAsnDetailId, asnDetail.getAsnDetailId())
						.eq(Sn::getSnStatus, 1);
					List<Sn> sn = snService.list(queryWrapper);
					if (sn.size() <= 0)
						throw new ServiceException("???????????????????????????");
					else if (sn.size() < allQty.intValue())
						throw new ServiceException("??????????????????????????????");
					else {
						String code = "";
						for (int i = 1; i <= sn.size(); i++) {
							if (i % lpnQty == 0) {
								detailCode.add(code);
								code = "";
							} else {
								if (!Func.isNotEmpty(code))
									code += ",";
								code += sn.get(i - 1).getSnDetailCode();
							}
						}
					}
				}
				for (int i = 0; i < countLpn; i++) {
					AsnLpnDetail asnLpnDetail = new AsnLpnDetail();
					asnLpnDetail.setAsnBillId(asnHeader.getAsnBillId());
					asnLpnDetail.setAsnBillNo(asnHeader.getAsnBillNo());
					asnLpnDetail.setAsnDetailId(asnDetail.getAsnDetailId());
					asnLpnDetail.setLpnCode(SerialNoCache.getLpnCode());
					asnLpnDetail.setLpnQty(Func.toLong(lpnQty));
					asnLpnDetail.setSkuId(asnDetail.getSkuId());
					asnLpnDetail.setSkuName(asnDetail.getSkuName());
					asnLpnDetail.setWspId(skuPackage.getWspId());
					asnLpnDetail.setSkuLevel(Func.toInt(skuPackageDetailMin.getSkuLevel()));
					asnLpnDetail.setUmCode(skuPackageDetailMin.getWsuCode());
					asnLpnDetail.setUmName(skuPackageDetailMin.getWsuName());
					asnLpnDetail.setSkuSpec(skuPackageDetailMin.getSkuSpec());
					asnLpnDetail.setLpnStatus(StringPool.ONE);//?????????
					if (sku.getIsSn().equals(1) && detailCode.size() > 0)
						asnLpnDetail.setSnDetailCode(detailCode.get(i));
					if (!this.save(asnLpnDetail)) throw new ServiceException("??????LPN????????????--" + asnHeader.getAsnBillNo());
				}
			}
		}
		return true;
	}

	@Override
	public synchronized Map<String, Object> submitAsnLpn(AsnDTO asnDTO) {
		//????????????????????????????????????
		QueryWrapper<AsnHeader> ahqw = new QueryWrapper<>();
		ahqw.lambda().eq(AsnHeader::getAsnBillNo, asnDTO.getAsnBillNo());
		AsnHeader asnHeader1 = asnHeaderMapper1.selectOne(ahqw);
		if (Func.isEmpty(asnHeader1)) {
			throw new ServiceException("??????????????????(??????:" + asnDTO.getAsnBillNo() + ")???");
		}
		if (asnHeader1.getAsnBillState().equals(AsnBillStateEnum.CANCEL.getCode())) {
			throw new ServiceException("?????????" + asnHeader1.getAsnBillNo() + " ??????????????????????????????");
		}
		AsnInventory asnInventory = null;
		Task task = taskService.getById(asnDTO.getTaskId());
		if (Func.isNotEmpty(task)) {
			asnInventory = asnInventoryService.getOne(Condition.getQueryWrapper(new AsnInventory()).lambda()
				.eq(AsnInventory::getAsnBillNo, task.getTaskRemark()));
		}
		//????????? ??????????????????
//		asnDTO.setSName(asnHeader1.getSName());
		asnDTO.setSName(asnHeader1.getSupplierName());

		if (Func.isNotEmpty(asnDTO.getLocCode())) {
			//????????????????????????
			AsnLpnDetail asnLpnDetail = super.getById(asnDTO.getAsnLpnId());
			if (ObjectUtil.isNotEmpty(asnLpnDetail) && asnLpnDetail.getLpnStatus().equals("2"))
				throw new ServiceException(String.format("??????[%s]??????????????????", asnDTO.getLpnCode()));
			asnLpnDetail.setLpnStatus("2");
			super.updateById(asnLpnDetail);
			//?????????????????????ID
			String[] locCodes = {"stage", "pick", "move", "pack"};
			Set<String> set = new HashSet<>(Arrays.asList(locCodes));
			Warehouse warehouse = WarehouseCache.getById(asnDTO.getWhId());
			Location location = new Location();
			location.setWhId(asnDTO.getWhId());
			location.setLocCode(asnDTO.getLocCode());
			if (set.contains(asnDTO.getLocCode().toLowerCase()))
				location.setLocCode(Func.firstCharToUpper(asnDTO.getLocCode().toLowerCase()) + warehouse.getWhCode());
			location = LocationCache.getValid(location.getLocCode());
			asnDTO.setLocId(location.getLocId());
			//??????????????????
			Long systemProcId = saveSystemProcLog(asnDTO, asnDTO.getScanQty());
			//???????????????????????????
			AsnDetail asnDetail = asnDetailService.getById(asnDTO.getAsnDetailId());
			if (ObjectUtil.isNotEmpty(asnDetail) && asnDetail.getSurplusQty().compareTo(BigDecimal.ZERO) <= 0)
				throw new ServiceException("??????????????????????????????????????????????????????");
			BigDecimal scanQuy = asnDetail.getScanQty().add(asnDTO.getScanQty());
			asnDetail.setScanQty(scanQuy);
			asnDetail.setSurplusQty(asnDetail.getPlanQty().subtract(asnDetail.getScanQty()));
			asnDetail.setDetailStatus(20);
			//?????? ???????????????
			asnDTO.setSkuSpec(asnDetail.getSkuSpec());
			//???????????? ???????????????
			asnDTO.setPlanQty(asnDetail.getPlanQty());
			asnDetailService.updateById(asnDetail);
			// ??????PO??????
			poDetailService.updateQty(asnDetail);
			//???????????????????????????
			if (asnDetail.getSurplusQty().compareTo(BigDecimal.ZERO) > 0) {
				asnHeaderService.updateAsnBillState(asnDTO.getAsnBillId(), AsnBillStateEnum.PART);
			} else {
				asnHeaderService.updateAsnBillState(asnDTO.getAsnBillId(), AsnBillStateEnum.COMPLETED);
			}
			//?????????????????????????????????
			this.saveContainerLog(asnDTO, asnInventory, asnDetail, asnDTO.getScanQty(), systemProcId);

			// ????????????????????????
			SkuLogDTO skuLogDTO = new SkuLogDTO();
			skuLogDTO.setSkuId(asnDetail.getSkuId());
			skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.INSTOCK);
			this.skuLogService.saveOrUpdate(skuLogDTO);
		} else {
			throw new ServiceException("?????????????????????");
		}
		AsnLpnDetail asnLpnDetail = new AsnLpnDetail();
		asnLpnDetail.setLpnCode(asnDTO.getLpnCode());
		return getAsnLpnDetail(asnLpnDetail);
	}

	/**
	 * ??????AsnBillId??????AsnDetailMinVO
	 *
	 * @param list
	 * @return
	 */
	public AsnDetailMinVO getMinVoByBillId(List<AsnDetail> list) {
		//?????????????????????????????????
		BigDecimal planCount = BigDecimal.ZERO;
		BigDecimal scanCount = BigDecimal.ZERO;
		AsnDetailMinVO minVO = new AsnDetailMinVO();
		for (AsnDetail asnDetail : list) {
			Sku sku = SkuCache.getById(asnDetail.getSkuId());
			if (Func.isEmpty(sku)) {
				throw new ServiceException(String.format("??????[%s]???????????????", asnDetail.getSkuId()));
			}
			planCount = planCount.add(asnDetail.getPlanQty());
			scanCount = scanCount.add(asnDetail.getScanQty());
		}
		minVO.setPlanCountQty(planCount);
		minVO.setScanCountQty(scanCount);

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
		minVO.setSkuSpec(spec.substring(0, spec.length() - 1));

		//????????????
		minVO.setDetailStatusName(DictCache.getValue("detail_status", list.get(list.size() - 1).getDetailStatus()));
		//????????????
		minVO.setSkuName(list.get(list.size() - 1).getSkuName());

		return minVO;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param dto
	 * @param count
	 */
	public Long saveSystemProcLog(AsnDTO dto, BigDecimal count) {
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.ASN);
		systemProcParam.setDataType(DataTypeEnum.AsnBillNo);
		systemProcParam.setAction(ActionEnum.RECEIVE_LPN);
		systemProcParam.setBillNo(dto.getAsnBillNo());
		systemProcParam.setLpnCode(dto.getLpnCode());
		systemProcParam.setOperationQty(count);
		systemProcParam.setOperationUnit(SkuPackageDetailCache.getById(dto.getWspdId()).getWsuName());
		systemProcParam.setWhId(dto.getWhId());
		return systemProcService.create(systemProcParam).getSystemProcId();
	}

	/**
	 * ??????????????????
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @return
	 */
	public boolean saveContainerLog(AsnDTO dto, AsnInventory asnInventory, AsnDetail asnDetail, BigDecimal count, Long systemProcId) {

		BladeUser user = AuthUtil.getUser();
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
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
		//????????????
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

		//????????????
		Stock stock = this.saveStock(dto, asnDetail, count, user, systemProcId);
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
		containerLog.setSkuName(asnDetail.getSkuName()); //????????????
		containerLog.setWspId(asnDetail.getWspId());
		containerLog.setSkuLevel(asnDetail.getSkuLevel()); //????????????
		containerLog.setAclStatus(1); //????????????
		containerLog.setLotNumber(stock.getLotNumber()); //???????????????

		containerLog.setSkuSpec(dto.getSkuSpec()); //??????
		containerLog.setPlanQty(dto.getPlanQty()); //????????????
		if (1 == dto.getIsSn()) {
			String codes = dto.getSnDetailCode();
			if (Func.isNotEmpty(codes)) {
				for (String code : codes.split(",")) {
					containerLog.setSnDetailCode(code); // ?????????
					containerLogService.save(containerLog);
					return true;
				}
			} else {
				throw new ServiceException("?????????????????????");
			}
		} else {
			containerLogService.save(containerLog);
		}

		return true;
	}

	/**
	 * ???????????? ????????????????????? ?????????????????????
	 *
	 * @param dto
	 * @param asnDetail
	 * @param count
	 * @param user
	 * @return
	 */
	private Stock saveStock(AsnDTO dto, AsnDetail asnDetail, BigDecimal count, BladeUser user, Long systemProcId) {
		StockAddDTO stockProc = Objects.requireNonNull(BeanUtil.copy(dto, StockAddDTO.class));
		stockProc.setSystemProcId(systemProcId);//??????id
		stockProc.setLocId(dto.getLocId());//??????id
		stockProc.setSkuId(dto.getSkuId());//??????id
		stockProc.setWspId(dto.getWspId());//??????id
		stockProc.setStockQty(count);//??????
		stockProc.setEventType(EventTypeEnum.Check);//????????????
		stockProc.setLpnCode(dto.getLpnCode());//????????????
		stockProc.setBillId(asnDetail.getAsnBillId());//?????????id
		stockProc.setBillDetailId(asnDetail.getAsnDetailId());//???????????????id
		stockProc.setBillNo(asnDetail.getAsnBillNo());//???????????????

		//?????????
//		Map<String, Object> skuLotMap = new HashMap<>();
//		try {
//			skuLotMap = SkuLotWrapper.skuLotToMap(dto.getAsnDetailSkuLotParam());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		for (String key : skuLotMap.keySet()) {
//			if (Func.isNotEmpty(key)) {
//				stockProc.skuLotSet(Integer.parseInt(key.substring(6)), skuLotMap.get(key).toString());
//			}
//		}
		Sku sku = SkuCache.getById(dto.getSkuId());
		if (Func.isNotEmpty(sku)) {
			SkuLotUtil.fill(sku, stockProc, asnDetail, dto);
		}
		return stockService.add(stockProc);
	}
}
