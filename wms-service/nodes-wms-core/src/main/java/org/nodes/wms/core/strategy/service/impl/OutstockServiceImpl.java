
package org.nodes.wms.core.strategy.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.core.basedata.service.ISkuOutstockService;
import org.nodes.wms.core.outstock.so.dto.CreatePickPlanByWellenDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;
import org.nodes.wms.core.strategy.dto.OutstockDTO;
import org.nodes.wms.core.strategy.dto.OutstockDetailDTO;
import org.nodes.wms.core.strategy.dto.OutstockExecuteDTO;
import org.nodes.wms.core.strategy.entity.*;
import org.nodes.wms.core.strategy.mapper.OutstockMapper;
import org.nodes.wms.core.strategy.service.*;
import org.nodes.wms.core.strategy.vo.OutstockDetailVO;
import org.nodes.wms.core.strategy.vo.OutstockStockVO;
import org.nodes.wms.core.strategy.vo.OutstockVO;
import org.nodes.wms.core.strategy.vo.OutstockZoneDetailVO;
import org.nodes.wms.core.strategy.wrapper.*;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.enums.ZoneTypeEnum;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ???????????? ???????????????
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Slf4j
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OutstockServiceImpl<M extends OutstockMapper, T extends Outstock>
	extends BaseServiceImpl<OutstockMapper, Outstock>
	implements IOutstockService {

	@Autowired
	IOutstockDetailService outstockDetailService;
	@Autowired
	IOutstockConfigLotService outstockConfigLotService;
	@Autowired
	IOutstockConfigUdfService outstockConfigUdfService;
	@Autowired
	IOutstockZoneDetailService outstockZoneDetailService;
	@Autowired
	IOutstockConfigService outstockConfigService;
	@Autowired
	IStockService stockService;
	@Autowired
	IZoneService zoneService;

	@Override
	public boolean save(OutstockDTO outstockDTO) {
		IOutstockDetailService outstockDetailService = SpringUtil.getBean(IOutstockDetailService.class);
		boolean insert = false;
		this.validData(outstockDTO);
		//???????????????????????????
		if (Func.isNotEmpty(outstockDTO.getSsoId())) {
			updateById(outstockDTO);
			return true;
		}
		Outstock param = new Outstock();
		param.setSsoCode(outstockDTO.getSsoCode());
		//??????????????????????????????getOne
		if (super.count(Condition.getQueryWrapper(param).lambda()
			.eq(Outstock::getSsoCode, outstockDTO.getSsoCode())) > 0) {
			throw new ServiceException("??????????????????");
		}
		//??????????????????
		insert = super.save(outstockDTO);
		//????????????
		//OutstockCache.saveOrUpdate(outstockDTO);
		Long ssoId = outstockDTO.getSsoId();//????????????ID
		String dataType = Outstock.DATA_TYPE;//????????????
		Long whId = outstockDTO.getWhId();//??????ID
		List<OutstockDetailDTO> outstockDetailDTOList = outstockDTO.getOutstockDetailDTO();
		if (ObjectUtil.isNotEmpty(outstockDetailDTOList)) {
			outstockDetailDTOList.stream().forEach(outstockDetailDTO -> {
				OutstockDetail outstockDetail = new OutstockDetail();
				outstockDetail.setOutstockFunction(outstockDetailDTO.getOutstockFunction());
				outstockDetail.setSsoId(ssoId);
				long cnt = outstockDetailService.count(Condition.getQueryWrapper(new OutstockDetail())
					.lambda()
					.eq(OutstockDetail::getSsoId, ssoId)
					.eq(OutstockDetail::getOutstockFunction, outstockDetailDTO.getOutstockFunction())
				);
				if (cnt > 0L)
					throw new ServiceException("????????????????????????");
				//??????????????????????????????
				outstockDetailDTO.setSsoId(ssoId);
				outstockDetailDTO.setDataType(dataType);
				if (outstockDetailService.save(outstockDetailDTO)) {
					saveOrUpdate(outstockDetailDTO, whId);
				}
			});
		}
		return insert;
	}


	@Override
	public boolean updateById(OutstockDTO outstockDTO) {
		IOutstockDetailService outstockDetailService = SpringUtil.getBean(IOutstockDetailService.class);
		boolean update = false;
		this.validData(outstockDTO);
		update = super.updateById(outstockDTO);
		Long ssoId = outstockDTO.getSsoId();//????????????ID
		String dataType = Outstock.DATA_TYPE;//????????????
		Long whId = outstockDTO.getWhId();//??????ID
		//???????????????????????????????????????????????????
		List<OutstockDetailDTO> outstockDetailDTOList = outstockDTO.getOutstockDetailDTO();
		List<OutstockDetailDTO> outstockDetailDeletedList = outstockDTO.getOutstockDetailDeletedList();
		if (ObjectUtil.isNotEmpty(outstockDetailDTOList)) {
			outstockDetailDTOList.stream().forEach(outstockDetailDTO -> {
				//?????????????????????????????????????????????
				OutstockDetail outstockDetail = outstockDetailDTO;
				long cnt = outstockDetailService.count(Condition.getQueryWrapper(new OutstockDetail())
					.lambda()
					.eq(OutstockDetail::getSsoId, outstockDetail.getSsoId())
				);
				if (cnt <= 0L) {
					//??????????????????????????????
					outstockDetail.setDataType(dataType);
					outstockDetail.setSsoId(ssoId);
					if (outstockDetailService.save(outstockDetail)) {
						saveOrUpdate(outstockDetailDTO, whId);
					}
				} else {
					//?????????????????????????????????????????????????????????????????????
					outstockDetail.setIsDeleted(0);
					if (outstockDetailService.updateById(outstockDetail)) {
						saveOrUpdate(outstockDetailDTO, whId);
					}
				}
			});
		}
		if (ObjectUtil.isNotEmpty(outstockDetailDeletedList)) {
			outstockDetailDeletedList.stream().forEach(detail -> {
				Long ssodId = detail.getSsodId();
				//?????????????????????
				removeUdfBySsodId(ssodId);
				//???????????????
				removeLotBySsodId(ssodId);
				//??????????????????
				removeZoneBySsodId(ssodId);
				//??????????????????
				removeConfigBySsodId(ssodId);
				//??????????????????
				outstockDetailService.removeById(ssodId);
			});
		}
		return update;
	}

	@Override
	public boolean saveOrUpdate(OutstockDTO outstockDTO) {
		if (Func.isEmpty(outstockDTO.getOutstockDetailDTO())) {
			throw new ServiceException("??????????????????,???????????????");
		}
		if (Func.isEmpty(outstockDTO.getSsoId())) {
			return this.save(outstockDTO);
		} else {
			return this.updateById(outstockDTO);
		}
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		IOutstockService outstockService = SpringUtil.getBean(IOutstockService.class);
		idList.forEach(id -> {
			ISkuOutstockService skuOutstockService = SpringUtil.getBean(ISkuOutstockService.class);
			long cnt = skuOutstockService.count(Condition.getQueryWrapper(new SkuOutstock())
				.lambda()
				.eq(SkuOutstock::getSsoId, id));
			if (cnt > 0L) {
				Outstock outstock = outstockService.getById(id);
				throw new ServiceException(String.format("[%s] ????????????????????????????????????", outstock.getSsoName()));
			}
			if (this.removeById(id)) {
				//OutstockCache.removeById((Long) id);
				this.removeDetailById((Long) id);
			}
		});
		return true;
	}

	//??????????????????
	private void removeDetailById(Long ssoId) {
		IOutstockDetailService outstockDetailService = SpringUtil.getBean(IOutstockDetailService.class);
		//List<OutstockDetail> outstockDetailList = OutstockDetailCache.list(ssoId);
		List<OutstockDetail> outstockDetailList = outstockDetailService.list(Condition.getQueryWrapper(new OutstockDetail())
			.lambda()
			.eq(OutstockDetail::getSsoId, ssoId)
		);
		if (Func.isNotEmpty(outstockDetailList)) {
			for (OutstockDetail detail : outstockDetailList) {
				//?????????????????????
				removeUdfBySsodId(detail.getSsodId());
				//???????????????
				removeLotBySsodId(detail.getSsodId());
				//??????????????????
				removeZoneBySsodId(detail.getSsodId());
				//??????????????????
				removeConfigBySsodId(detail.getSsodId());
			}
		}
		//??????????????????
		OutstockDetail outstockDetail = new OutstockDetail();
		outstockDetail.setSsoId(ssoId);
		if (outstockDetailService.remove(Condition.getQueryWrapper(outstockDetail))) {
			//OutstockDetailCache.removeBySsoId(ssoId);
			outstockDetailService.remove(Condition.getQueryWrapper(new OutstockDetail())
				.lambda()
				.eq(OutstockDetail::getSsoId, ssoId)
			);
		}
	}

	//?????????????????????
	private void removeUdfBySsodId(Long ssodId) {
		OutstockConfigUdf outstockConfigUdf = new OutstockConfigUdf();
		IOutstockConfigUdfService outstockConfigUdfService = SpringUtil.getBean(IOutstockConfigUdfService.class);
		outstockConfigUdf.setSsodId(ssodId);
		if (outstockConfigUdfService.remove(Condition.getQueryWrapper(outstockConfigUdf))) {
			//OutstockConfigUdfCache.removeBySsodId(ssodId);
			outstockConfigUdfService.remove(Condition.getQueryWrapper(new OutstockConfigUdf())
				.lambda()
				.eq(OutstockConfigUdf::getSsodId, ssodId)
			);
		}
	}

	//???????????????
	private void removeLotBySsodId(Long ssodId) {
		IOutstockConfigLotService outstockConfigLotService = SpringUtil.getBean(IOutstockConfigLotService.class);
		OutstockConfigLot outstockConfigLot = new OutstockConfigLot();
		outstockConfigLot.setSsodId(ssodId);
		if (outstockConfigLotService.remove(Condition.getQueryWrapper(outstockConfigLot))) {
			//OutstockConfigLotCache.removeBySsodId(ssodId);
			outstockConfigLotService.remove(Condition.getQueryWrapper(new OutstockConfigLot())
				.lambda()
				.eq(OutstockConfigLot::getSsodId, ssodId)
			);
		}
	}

	//??????????????????
	private void removeZoneBySsodId(Long ssodId) {
		OutstockZoneDetail outstockZoneDetail = new OutstockZoneDetail();
		IOutstockZoneDetailService outstockZoneDetailService = SpringUtil.getBean(IOutstockZoneDetailService.class);
		outstockZoneDetail.setSsodId(ssodId);
		if (outstockZoneDetailService.remove(Condition.getQueryWrapper(outstockZoneDetail))) {
			//OutstockZoneDetailCache.removeBySsodId(ssodId);
			outstockZoneDetailService.remove(Condition.getQueryWrapper(new OutstockZoneDetail())
				.lambda()
				.eq(OutstockZoneDetail::getSsodId, ssodId)
			);
		}
	}

	//??????????????????
	private void removeConfigBySsodId(Long ssodId) {
		IOutstockConfigService outstockConfigService = SpringUtil.getBean(IOutstockConfigService.class);
		OutstockConfig outstockConfig = new OutstockConfig();
		outstockConfig.setSsodId(ssodId);
		if (outstockConfigService.remove(Condition.getQueryWrapper(outstockConfig))) {
			//OutstockConfigCache.removeBySsodId(ssodId);
			outstockConfigService.remove(Condition.getQueryWrapper(new OutstockConfig())
				.lambda()
				.eq(OutstockConfig::getSsodId, ssodId));
		}
	}

	private void saveOrUpdate(OutstockDetailDTO outstockDetailDTO, Long whId) {
		List<OutstockConfigLot> outstockConfigLotList = outstockDetailDTO.getDetailConfigLot();
		List<OutstockConfigUdf> outstockConfigUdfList = outstockDetailDTO.getDetailConfigUdf();
		List<OutstockZoneDetail> outstockZoneDetailList = outstockDetailDTO.getOutstockZoneDetail();
		List<OutstockConfig> outstockConfigList = outstockDetailDTO.getOutstockConfigList();
		List<OutstockConfigLot> outstockConfigLotDeletedList = outstockDetailDTO.getDetailConfigLotDeletedList();
		List<OutstockConfigUdf> outstockConfigUdfDeletedList = outstockDetailDTO.getDetailConfigUdfDeletedList();
		List<OutstockZoneDetail> outstockZoneDetailDeleteList = outstockDetailDTO.getOutstockZoneDetailDeletedList();
		List<OutstockConfig> outstockConfigDeleteList = outstockDetailDTO.getOutstockConfigDeletedList();
		//????????????????????????ID
		Long ssodId = outstockDetailDTO.getSsodId();

		if (ObjectUtil.isNotEmpty(outstockConfigLotList)) {
			outstockConfigLotList.stream().forEach(outstockConfigLot -> {
				//???????????????????????????????????????
				outstockConfigLot.setSsodId(ssodId);
				outstockConfigLotService.saveOrUpdate(outstockConfigLot);
			});
		}
		if (ObjectUtil.isNotEmpty(outstockConfigUdfList)) {
			outstockConfigUdfList.stream().forEach(outstockConfigUdf -> {
				//?????????????????????????????????????????????
				outstockConfigUdf.setSsodId(ssodId);
				outstockConfigUdfService.saveOrUpdate(outstockConfigUdf);
			});
		}
		if (ObjectUtil.isNotEmpty(outstockZoneDetailList)) {
			outstockZoneDetailList.stream().forEach(outstockZoneDetail -> {
				outstockZoneDetail.setSsodId(ssodId);
				outstockZoneDetail.setWhId(whId);
				outstockZoneDetailService.saveOrUpdate(outstockZoneDetail);
			});
		}
		if (ObjectUtil.isNotEmpty(outstockConfigList)) {
			outstockConfigList.stream().forEach(outstockConfig -> {
				outstockConfig.setSsodId(ssodId);
				outstockConfigService.saveOrUpdate(outstockConfig);
			});
		}
		//???????????????
		if (ObjectUtil.isNotEmpty(outstockConfigLotDeletedList)) {
			List<Long> idList = NodesUtil.toList(outstockConfigLotDeletedList, OutstockConfigLot::getSsoclId);
			outstockConfigLotService.removeByIds(idList);
		}
		//?????????????????????
		if (ObjectUtil.isNotEmpty(outstockConfigUdfDeletedList)) {
			List<Long> idList = NodesUtil.toList(outstockConfigUdfDeletedList, OutstockConfigUdf::getSsocuId);
			if (Func.isNotEmpty(idList)) {
				outstockConfigUdfService.removeByIds(idList);
			}
		}
		//??????????????????
		if (ObjectUtil.isNotEmpty(outstockZoneDetailDeleteList)) {
			List<Long> idList = NodesUtil.toList(outstockZoneDetailDeleteList, OutstockZoneDetail::getStozdId);
			outstockZoneDetailService.removeByIds(idList);
		}
		//??????????????????
		if (ObjectUtil.isNotEmpty(outstockConfigDeleteList)) {
			List<Long> configIdList = NodesUtil.toList(outstockConfigDeleteList, OutstockConfig::getSsocId);
			if (configIdList.size() > 0) {
				outstockConfigService.removeByIds(configIdList);
			}
		}
	}

	@Override
	public OutstockVO getOne(Long ssoId) {
		IOutstockConfigLotService outstockConfigLotService = SpringUtil.getBean(IOutstockConfigLotService.class);
		IOutstockConfigUdfService outstockConfigUdfService = SpringUtil.getBean(IOutstockConfigUdfService.class);
		IOutstockDetailService outstockDetailService = SpringUtil.getBean(IOutstockDetailService.class);
		IOutstockZoneDetailService outstockZoneDetailService = SpringUtil.getBean(IOutstockZoneDetailService.class);
		OutstockVO outstockVo = new OutstockVO();
		Outstock outstock = getById(ssoId);
		if (outstock != null) {
			outstockVo = OutstockWrapper.build().entityVO(outstock);
			OutstockDetail outstockDetail = new OutstockDetail();
			outstockDetail.setSsoId(outstockVo.getSsoId());
			//List<OutstockDetail> outstockDetailList = OutstockDetailCache.list(outstockVo.getSsoId());
			List<OutstockDetail> outstockDetailList = outstockDetailService.list(Condition.getQueryWrapper(new OutstockDetail())
				.lambda()
				.eq(OutstockDetail::getSsoId, outstockDetail.getSsoId())
			);

			if (outstockDetailList != null && outstockDetailList.size() > 0) {
				List<OutstockDetailVO> outstockDetailvoList = OutstockDetailWrapper.build().listVO(outstockDetailList);
				outstockDetailvoList.stream().forEach(item -> {
					//????????????????????????
					if (Func.isNotEmpty(item.getOutstockFunction())) {
						List<String> descList = new ArrayList<>();
						List<Integer> valueList = Func.toIntList(item.getOutstockFunction());
						valueList.forEach(value -> {
							descList.add(DictCache.getValue(DictCodeConstant.OUTSTOCK_FUNCTION, value));
						});

						item.setOutstockFunctionDesc(Func.join(descList));
					}
					//?????????????????????????????????
					/*List<OutstockConfigUdf> outstockConfigUdfList = OutstockConfigUdfCache.listBySsodId(
						item.getSsodId());*/
					List<OutstockConfigUdf> outstockConfigUdfList = outstockConfigUdfService.list(Condition.getQueryWrapper(new OutstockConfigUdf())
						.lambda()
						.eq(OutstockConfigUdf::getSsodId, item.getSsodId())
					);
					if (Func.isNotEmpty(outstockConfigUdfList)) {
						item.setDetailConfigUdf(OutstockConfigUdfWrapper.build().listVO(outstockConfigUdfList));
					}
					//???????????????????????????
					/*List<OutstockConfigLot> outstockConfigLotList = OutstockConfigLotCache.listBySsodId(
						item.getSsodId());*/
					List<OutstockConfigLot> outstockConfigLotList = outstockConfigLotService.list(Condition.getQueryWrapper(new OutstockConfigLot())
						.lambda()
						.eq(OutstockConfigLot::getSsodId, item.getSsodId())
					);

					if (Func.isNotEmpty(outstockConfigLotList)) {
						item.setDetailConfigLot(OutstockConfigLotWrapper.build().listVO(outstockConfigLotList));
					}
					//??????????????????????????????
					/*List<OutstockZoneDetail> outstockZoneDetailList = OutstockZoneDetailCache.listBySsodId(
						item.getSsodId());*/
					List<OutstockZoneDetail> outstockZoneDetailList = outstockZoneDetailService.list(Condition.getQueryWrapper(new OutstockZoneDetail())
						.lambda()
						.eq(OutstockZoneDetail::getSsodId, item.getSsodId())
					);
					if (Func.isNotEmpty(outstockZoneDetailList)) {
						item.setOutstockZoneDetail(OutstockZoneDetailWrapper.build().listVO(outstockZoneDetailList));
					}
					//??????????????????????????????
					IOutstockConfigService outstockConfigService = SpringUtil.getBean(IOutstockConfigService.class);
					//List<OutstockConfig> outstockConfigList = OutstockConfigCache.listBySsodId(item.getSsodId());
					List<OutstockConfig> outstockConfigList
						= outstockConfigService.list(Condition.getQueryWrapper(new OutstockConfig())
						.lambda()
						.eq(OutstockConfig::getSsodId, item.getSsodId())
					);
					if (Func.isNotEmpty(outstockConfigList)) {
						item.setOutstockConfigList(OutstockConfigWrapper.build().listVO(outstockConfigList));
					}
				});
				//??????????????????????????????
				outstockVo.setOutstockDetailVO(outstockDetailvoList);
			}
		}
		return outstockVo;
	}

	@Override
	public OutstockExecuteDTO execute(SoHeader soHeader, SoDetail soDetail, Sku sku) {
		IOutstockService outstockService = SpringUtil.getBean(IOutstockService.class);
		IOutstockConfigService outstockConfigService = SpringUtil.getBean(IOutstockConfigService.class);
		IOutstockZoneDetailService outstockZoneDetailService = SpringUtil.getBean(IOutstockZoneDetailService.class);
		// ????????????
		OutstockExecuteDTO outstockExecute = new OutstockExecuteDTO();
		outstockExecute.setUserCode(AuthUtil.getUserAccount());
		outstockExecute.setUserName(AuthUtil.getNickName());
		outstockExecute.setWellenNo(null);
		outstockExecute.setSoBillNo(soHeader.getSoBillNo());
		outstockExecute.setWhId(soHeader.getWhId());
		outstockExecute.setExecuteTime(LocalDateTime.now());
		outstockExecute.setSuccess(false);
		outstockExecute.setStockQty(BigDecimal.ZERO);
		try {
			outstockExecute.setSkuCode(soDetail.getSkuCode());
			outstockExecute.setSkuName(soDetail.getSkuName());
			outstockExecute.setDetailLotNumber(soDetail.getLotNumber());
			// ??????????????????????????????
			//SkuOutstock skuOutstock = SkuOutstockCache.getOne(soDetail.getSkuId(), soHeader.getWhId());
			ISkuOutstockService skuOutstockService = SpringUtil.getBean(ISkuOutstockService.class);
			SkuOutstock skuOutstock = skuOutstockService.list(Condition.getQueryWrapper(new SkuOutstock())
				.lambda()
				.eq(SkuOutstock::getSkuId, soDetail.getSkuId())
				.eq(SkuOutstock::getWhId, soHeader.getWhId())
			).stream().findFirst().orElse(null);
			if (ObjectUtils.isEmpty(skuOutstock)) {
				throw new ServiceException("?????????" + soDetail.getSkuName() + " ????????????????????????");
			}
			outstockExecute.setSsoId(skuOutstock.getSsoId());
			// ??????????????????????????????
			Outstock outstock = outstockService.getById(skuOutstock.getSsoId());
			if (ObjectUtil.isEmpty(outstock)) {
				throw new ServiceException("?????????" + soDetail.getSkuName() + " ?????????????????????????????????");
			}
			outstockExecute.setSsoCode(outstock.getSsoCode());
			outstockExecute.setSsoName(outstock.getSsoName());
			// ?????????????????????????????????
			OutstockDetail outstockDetail = outstockDetailService.find(outstock, soHeader, soHeader.getBillTypeCd(), sku);
			if (ObjectUtil.isEmpty(outstockDetail)) {
				throw new ServiceException("?????????" + sku.getSkuName() + " ????????????????????????");
			}
			outstockExecute.setSsodProcOrder(outstockDetail.getSsodProcOrder());
			outstockExecute.setOutstockFunction(outstockDetail.getOutstockFunction());
			if (Func.isNotEmpty(outstockDetail.getOutstockFunction())) {
				List<String> descList = new ArrayList<>();
				List<Integer> valueList = Func.toIntList(outstockDetail.getOutstockFunction());
				valueList.forEach(value -> {
					descList.add(DictCache.getValue(DictCodeConstant.OUTSTOCK_FUNCTION, value));
				});

				outstockExecute.setOutstockFunctionDesc(Func.join(descList));
			}
			// ??????????????????
			/*List<OutstockZoneDetail> outstockZoneDetails = OutstockZoneDetailCache.listBySsodId(
				outstockDetail.getSsodId());*/

			List<OutstockZoneDetail> outstockZoneDetails = outstockZoneDetailService.list(Condition.getQueryWrapper(new OutstockZoneDetail())
				.lambda()
				.eq(OutstockZoneDetail::getSsodId, outstockDetail.getSsodId())
			);

			if (ObjectUtil.isEmpty(outstockZoneDetails)) {
				throw new ServiceException("?????????" + sku.getSkuName() + " ????????????????????????");
			}
			// ???????????????????????????????????????????????????
			List<OutstockZoneDetailVO> outstockZoneDetailVOList = OutstockZoneDetailWrapper.build().listVO(
				outstockZoneDetails);/*.stream().filter(detail -> {
				return detail.getZoneType() == soHeader.getZoneType();
			}).collect(Collectors.toList());*/
			if (ObjectUtil.isEmpty(outstockZoneDetailVOList)) {
				throw new ServiceException("?????????????????????????????????????????????????????????");
			}
			outstockExecute.setSourceZoneName(NodesUtil.join(outstockZoneDetailVOList, "zoneName"));
			// ???????????????????????????????????????
			List<Long> zoneIdList = NodesUtil.toList(outstockZoneDetailVOList, OutstockZoneDetailVO::getZoneId);
			/*List<Location> locationList = LocationCache.list().stream().filter(u -> {
				return zoneIdList.contains(u.getZoneId()) && Func.isEmpty(u.getCountBillNo());
			}).collect(Collectors.toList());*/
			ILocationService locationService = SpringUtil.getBean(ILocationService.class);
			List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.in(Location::getZoneId, zoneIdList)
				.isNull(Location::getCountBillNo));
			outstockExecute.setLocationList(locationList);
			// ?????????????????????
			/*List<OutstockConfigLot> outstockConfigLots = OutstockConfigLotCache.listBySsodId(
				outstockDetail.getSsodId());*/
			List<OutstockConfigLot> outstockConfigLots = outstockConfigLotService.list(Condition.getQueryWrapper(new OutstockConfigLot())
				.lambda()
				.eq(OutstockConfigLot::getSsodId, outstockDetail.getSsodId())
			);
			// ??????????????????
			OutstockStockVO stockOutstockRlt = this.getStockInfo(
				sku.getSkuId(), soDetail.getWspId(), soDetail, outstockZoneDetailVOList, outstockConfigLots);
			if (ObjectUtil.isNotEmpty(stockOutstockRlt)) {
				// ???????????????????????????????????????????????????
				if (Func.isNotEmpty(stockOutstockRlt.getStockList())) {
//					List<Stock> stockList = FunctionCodeFactory.create(skuOutstock, outstockDetail)
//						.execute(stockOutstockRlt.getStockList(), BigDecimal.ZERO);
					List<Stock> stockList = outstockDetailService.execute(
						outstockDetail, skuOutstock, stockOutstockRlt.getStockList(), BigDecimal.ZERO);
					outstockExecute.setStockList(stockList);
					// ??????????????????????????????????????????????????????????????????
					outstockExecute.setStockQty(stockList.stream().map(stock -> {
						return stock.getStockQty().subtract(stock.getPickQty());
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
					outstockExecute.setOccupyQty(stockList.stream().map(stock -> {
						return stock.getOccupyQty();
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
				}
				// ????????????????????????????????????
				outstockExecute.setDetailSkuLot(StringUtil.join(stockOutstockRlt.getSoDetailSkuLotStrList()));
				outstockExecute.setSsoSkuLot(StringUtil.join(stockOutstockRlt.getSsoSkuLotStrList()));
				outstockExecute.setSqlCmd(stockOutstockRlt.getSqlCmd());
			}
			outstockExecute.setSuccess(true);
		} catch (Exception ex) {
			outstockExecute.setSuccess(false);
			outstockExecute.setException(ex.getMessage());
		}
		return outstockExecute;
	}

	@Override
	public OutstockExecuteDTO manualExecute(SoHeader soHeader, SoDetail soDetail,
											Sku sku, boolean isWellen, CreatePickPlanByWellenDTO dto) {
		// ????????????
		OutstockExecuteDTO outstockExecute = new OutstockExecuteDTO();
		outstockExecute.setUserCode(AuthUtil.getUserAccount());
		outstockExecute.setUserName(AuthUtil.getNickName());
		outstockExecute.setWellenNo(null);
		outstockExecute.setSoBillNo(soHeader.getSoBillNo());
		outstockExecute.setWhId(soHeader.getWhId());
		outstockExecute.setExecuteTime(LocalDateTime.now());
		outstockExecute.setSuccess(false);
		outstockExecute.setStockQty(BigDecimal.ZERO);
		try {
			outstockExecute.setSkuCode(soDetail.getSkuCode());
			outstockExecute.setSkuName(soDetail.getSkuName());
			outstockExecute.setDetailLotNumber(soDetail.getLotNumber());
			OutstockStockVO stockOutstockRlt = this.getStockInfo(
				sku.getSkuId(), soDetail.getWspId(), soDetail, null, null);
			if (ObjectUtil.isNotEmpty(stockOutstockRlt)) {
				// ???????????????????????????????????????????????????
				if (Func.isNotEmpty(stockOutstockRlt.getStockList())) {
					ManualAllocRequestDTO manualAllocRequestDTO = new ManualAllocRequestDTO();
					manualAllocRequestDTO.setDto(dto);
					manualAllocRequestDTO.setStocks(stockOutstockRlt.getStockList());
					List<Stock> stockList = ManualAllocStrategyUtil.executeStrategy(manualAllocRequestDTO);
					outstockExecute.setStockList(stockList);
					// ??????????????????????????????????????????????????????????????????
					outstockExecute.setStockQty(stockList.stream().map(stock -> {
						return stock.getStockQty().subtract(stock.getPickQty());
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
					outstockExecute.setOccupyQty(stockList.stream().map(stock -> {
						return stock.getOccupyQty();
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
				}
				// ????????????????????????????????????
				outstockExecute.setSqlCmd(stockOutstockRlt.getSqlCmd());
			}
			outstockExecute.setSuccess(true);
		} catch (Exception ex) {
			outstockExecute.setSuccess(false);
			outstockExecute.setException(ex.getMessage());
		}
		return outstockExecute;
	}

	protected OutstockStockVO getStockInfo(Long skuId, Long wspId, SkuLotBaseEntity soDetail,
										   List<OutstockZoneDetailVO> zoneList, List<OutstockConfigLot> configLotList) {
		OutstockStockVO stockOutstockRltDTO = new OutstockStockVO();

		QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
		stockQueryWrapper.lambda()
			.eq(Stock::getStockStatus, StockStatusEnum.NORMAL.getIndex())
			.eq(Stock::getSkuId, skuId)
			.eq(Stock::getWspId, wspId)
			.func(sql -> {
				if (Func.isNotEmpty(zoneList)) {
					sql.and(obj -> {
						for (OutstockZoneDetail outstockZoneDetail : zoneList) {
							obj.or().eq(Stock::getZoneId, outstockZoneDetail.getZoneId());
						}
					});
				} else {
					// ?????????????????????
					List<Zone> outstockZoneList = zoneService.list(Condition.getQueryWrapper(new Zone()).lambda()
						.eq(Zone::getZoneType, ZoneTypeEnum.SHIPPING_OUTSTOCK.getIndex()));
					if (Func.isNotEmpty(outstockZoneList)) {
						sql.notIn(Stock::getZoneId, NodesUtil.toList(outstockZoneList, Zone::getZoneId));
					}
					List<Zone> instockZoneList = zoneService.list(Condition.getQueryWrapper(new Zone()).lambda()
						.eq(Zone::getZoneType, ZoneTypeEnum.SHIPPING_INSTOCK.getIndex()));
					if (Func.isNotEmpty(instockZoneList)) {
						sql.notIn(Stock::getZoneId, NodesUtil.toList(instockZoneList, Zone::getZoneId));
					}
				}
			});

		// ?????????????????????
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (soDetail.skuLotChk(i)) {
				// ???????????????????????????????????????
				stockQueryWrapper.eq("sku_lot" + i, soDetail.skuLotGet(i));
				stockOutstockRltDTO.getSoDetailSkuLotStrList().add("sku_lot" + i + " = " + soDetail.skuLotGet(i));
			} else {
				// ??????????????????????????????
				if (Func.isNotEmpty(configLotList)) {
					int finalI = i;
					List<OutstockConfigLot> configLots = configLotList.stream().filter(lot -> {
						return lot.getSkuLotNumber().equals(finalI);
					}).collect(Collectors.toList());
					if (ObjectUtil.isEmpty(configLots)) {
						continue;
					}
					// ???????????????????????????
					for (OutstockConfigLot configLot : configLots) {
						stockQueryWrapper.and(obj -> {
							obj.or();
							List<String> skuLotValList = new ArrayList<>();

							if (StringUtil.isNotBlank(configLot.getSkuLotVal1())) {
								skuLotValList.add(configLot.getSkuLotVal1());
							}
							if (StringUtil.isNotBlank(configLot.getSkuLotVal2())) {
								skuLotValList.add(configLot.getSkuLotVal2());
							}
							if (StringUtil.isNotBlank(configLot.getSkuLotVal3())) {
								skuLotValList.add(configLot.getSkuLotVal3());
							}
							for (String skuLotVal : skuLotValList) {
								String skuLotValStr = this.wrapperMatch(obj, configLot.getSkuLotOperation(),
									"sku_lot" + finalI, skuLotVal);
								stockOutstockRltDTO.getSsoSkuLotStrList().add(skuLotValStr);
							}
						});
					}
				}
			}
		}
		// ?????? ??????/ ???????????? ?????????
		/*List<Location> locList = LocationCache.list().stream().filter(u -> {
			return Func.isNotEmpty(u.getCountBillNo());
		}).collect(Collectors.toList());*/
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		List<Location> locList = locationService.list(Condition.getQueryWrapper(new Location())
			.lambda()
			.and(wrapp->wrapp.isNotNull(Location::getLockFlag).ne(Location::getLockFlag,""))
			);
//			.isNotNull(Location::getCountBillNo)
//			.ne(Location::getCountBillNo, StringPool.EMPTY));
		List<Long> locIdList = NodesUtil.toList(locList, Location::getLocId);
		if (Func.isNotEmpty(locIdList)) {
			stockQueryWrapper.lambda().notIn(Stock::getLocId, locIdList);
		}
		// ?????? ?????????, ???????????? ??????
		stockQueryWrapper.lambda()
			.orderByAsc(Stock::getLotNumber)
			.orderByAsc(Stock::getLastInTime);

		// ???????????????SQL??????
		StringBuffer sqlBuf = new StringBuffer("select * from wms_stock ");
		String whereStr = stockQueryWrapper.getCustomSqlSegment();
		Map<String, Object> params = stockQueryWrapper.getParamNameValuePairs();
		if (ObjectUtil.isNotEmpty(params)) {
			for (String paramKey : params.keySet()) {
				String key = "#{ew.paramNameValuePairs." + paramKey + "}";
				whereStr = whereStr.replace(key, String.valueOf(params.get(paramKey)));
			}
		}
		sqlBuf.append(whereStr);

		List<Stock> stockList = stockService.list(stockQueryWrapper);
		stockOutstockRltDTO.setStockList(stockList);
		stockOutstockRltDTO.setSqlCmd(sqlBuf.toString());

		return stockOutstockRltDTO;
	}

	/**
	 * ??????????????????
	 *
	 * @param queryWrapper ???????????????
	 * @param operator     ???????????????
	 * @param column       ??????
	 * @param value        ???
	 */
	protected String wrapperMatch(QueryWrapper<Stock> queryWrapper, int operator, String column, String value) {
		switch (operator) {
			case 0:
				queryWrapper.eq(column, value);
				return column + " = " + value + ", ";
			case 1:
				queryWrapper.gt(column, value);
				return column + " > " + value + ", ";
			case 2:
				queryWrapper.lt(column, value);
				return column + " < " + value + ", ";
			case 3:
				queryWrapper.gt(column, value).or().eq(column, value);
				return column + " >= " + value + ", ";
			case 4:
				queryWrapper.lt(column, value).or().eq(column, value);
				return column + " <= " + value + ", ";
			case 5:
				queryWrapper.like(column, value);
				return column + " like " + value + ", ";
			case 6:
				queryWrapper.isNotNull(column);
				return column + " != null" + ", ";
			default:
				return StringPool.EMPTY;
		}
	}

	protected void validData(OutstockDTO outstockDTO) {
		// ??????????????????????????????
		Outstock outstockQuery = new Outstock();
		outstockQuery.setSsoCode(outstockDTO.getSsoCode());
		Outstock findObj = super.getOne(Condition.getQueryWrapper(outstockQuery));
		if (ObjectUtil.isNotEmpty(findObj) && findObj.getSsoCode().equals(outstockDTO.getSsoCode()) &&
			!findObj.getSsoId().equals(outstockDTO.getSsoId())) {
			throw new ServiceException("???????????????" + findObj.getSsoCode() + " ????????????");
		}
	}


}
