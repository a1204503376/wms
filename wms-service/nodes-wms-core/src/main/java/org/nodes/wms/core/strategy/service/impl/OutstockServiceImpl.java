
package org.nodes.wms.core.strategy.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.core.basedata.service.ISkuOutstockService;
import org.nodes.wms.core.outstock.so.dto.CreatePickPlanByWellenDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.stock.core.entity.Stock;
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
 * 分配策略 服务实现类
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
		//如果主键存在则更新
		if (Func.isNotEmpty(outstockDTO.getSsoId())) {
			updateById(outstockDTO);
			return true;
		}
		Outstock param = new Outstock();
		param.setSsoCode(outstockDTO.getSsoCode());
		//查询货主编码是否重复getOne
		if (super.count(Condition.getQueryWrapper(param).lambda()
			.eq(Outstock::getSsoCode, outstockDTO.getSsoCode())) > 0) {
			throw new ServiceException("策略编码重复");
		}
		//添加分配策略
		insert = super.save(outstockDTO);
		//添加缓存
		//OutstockCache.saveOrUpdate(outstockDTO);
		Long ssoId = outstockDTO.getSsoId();//分配策略ID
		String dataType = Outstock.DATA_TYPE;//数据类型
		Long whId = outstockDTO.getWhId();//库房ID
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
					throw new ServiceException("分配计算代码重复");
				//添加分配策略明细信息
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
		Long ssoId = outstockDTO.getSsoId();//分配策略ID
		String dataType = Outstock.DATA_TYPE;//数据类型
		Long whId = outstockDTO.getWhId();//库房ID
		//将所有关联策略明细更新为待删除状态
		List<OutstockDetailDTO> outstockDetailDTOList = outstockDTO.getOutstockDetailDTO();
		List<OutstockDetailDTO> outstockDetailDeletedList = outstockDTO.getOutstockDetailDeletedList();
		if (ObjectUtil.isNotEmpty(outstockDetailDTOList)) {
			outstockDetailDTOList.stream().forEach(outstockDetailDTO -> {
				//判断是否存在该分配策略明细信息
				OutstockDetail outstockDetail = outstockDetailDTO;
				long cnt = outstockDetailService.count(Condition.getQueryWrapper(new OutstockDetail())
					.lambda()
					.eq(OutstockDetail::getSsoId, outstockDetail.getSsoId())
				);
				if (cnt <= 0L) {
					//添加分配策略明细信息
					outstockDetail.setDataType(dataType);
					outstockDetail.setSsoId(ssoId);
					if (outstockDetailService.save(outstockDetail)) {
						saveOrUpdate(outstockDetailDTO, whId);
					}
				} else {
					//修改分配策略明细信息并将待删除状态改为正常状态
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
				//删除自定义属性
				removeUdfBySsodId(ssodId);
				//删除批属性
				removeLotBySsodId(ssodId);
				//删除货源库区
				removeZoneBySsodId(ssodId);
				//删除执行条件
				removeConfigBySsodId(ssodId);
				//删除策略明细
				outstockDetailService.removeById(ssodId);
			});
		}
		return update;
	}

	@Override
	public boolean saveOrUpdate(OutstockDTO outstockDTO) {
		if (Func.isEmpty(outstockDTO.getOutstockDetailDTO())) {
			throw new ServiceException("明细不能为空,请添加明细");
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
				throw new ServiceException(String.format("[%s] 策略已被使用不允许删除！", outstock.getSsoName()));
			}
			if (this.removeById(id)) {
				//OutstockCache.removeById((Long) id);
				this.removeDetailById((Long) id);
			}
		});
		return true;
	}

	//删除策略明细
	private void removeDetailById(Long ssoId) {
		IOutstockDetailService outstockDetailService = SpringUtil.getBean(IOutstockDetailService.class);
		//List<OutstockDetail> outstockDetailList = OutstockDetailCache.list(ssoId);
		List<OutstockDetail> outstockDetailList = outstockDetailService.list(Condition.getQueryWrapper(new OutstockDetail())
			.lambda()
			.eq(OutstockDetail::getSsoId, ssoId)
		);
		if (Func.isNotEmpty(outstockDetailList)) {
			for (OutstockDetail detail : outstockDetailList) {
				//删除自定义属性
				removeUdfBySsodId(detail.getSsodId());
				//删除批属性
				removeLotBySsodId(detail.getSsodId());
				//删除货源库区
				removeZoneBySsodId(detail.getSsodId());
				//删除执行条件
				removeConfigBySsodId(detail.getSsodId());
			}
		}
		//删除策略明细
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

	//删除自定义属性
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

	//删除批属性
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

	//删除货源库区
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

	//删除执行条件
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
		//获取分配策略明细ID
		Long ssodId = outstockDetailDTO.getSsodId();

		if (ObjectUtil.isNotEmpty(outstockConfigLotList)) {
			outstockConfigLotList.stream().forEach(outstockConfigLot -> {
				//判断是否存在批属性设置信息
				outstockConfigLot.setSsodId(ssodId);
				outstockConfigLotService.saveOrUpdate(outstockConfigLot);
			});
		}
		if (ObjectUtil.isNotEmpty(outstockConfigUdfList)) {
			outstockConfigUdfList.stream().forEach(outstockConfigUdf -> {
				//判断是否存在自定义属性设置信息
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
		//删除批属性
		if (ObjectUtil.isNotEmpty(outstockConfigLotDeletedList)) {
			List<Long> idList = NodesUtil.toList(outstockConfigLotDeletedList, OutstockConfigLot::getSsoclId);
			outstockConfigLotService.removeByIds(idList);
		}
		//删除自定义属性
		if (ObjectUtil.isNotEmpty(outstockConfigUdfDeletedList)) {
			List<Long> idList = NodesUtil.toList(outstockConfigUdfDeletedList, OutstockConfigUdf::getSsocuId);
			if (Func.isNotEmpty(idList)) {
				outstockConfigUdfService.removeByIds(idList);
			}
		}
		//删除货源库区
		if (ObjectUtil.isNotEmpty(outstockZoneDetailDeleteList)) {
			List<Long> idList = NodesUtil.toList(outstockZoneDetailDeleteList, OutstockZoneDetail::getStozdId);
			outstockZoneDetailService.removeByIds(idList);
		}
		//删除执行条件
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
					//分配计算代码描述
					if (Func.isNotEmpty(item.getOutstockFunction())) {
						List<String> descList = new ArrayList<>();
						List<Integer> valueList = Func.toIntList(item.getOutstockFunction());
						valueList.forEach(value -> {
							descList.add(DictCache.getValue(DictConstant.OUTSTOCK_FUNCTION, value));
						});

						item.setOutstockFunctionDesc(Func.join(descList));
					}
					//获取并给自定义属性赋值
					/*List<OutstockConfigUdf> outstockConfigUdfList = OutstockConfigUdfCache.listBySsodId(
						item.getSsodId());*/
					List<OutstockConfigUdf> outstockConfigUdfList = outstockConfigUdfService.list(Condition.getQueryWrapper(new OutstockConfigUdf())
						.lambda()
						.eq(OutstockConfigUdf::getSsodId, item.getSsodId())
					);
					if (Func.isNotEmpty(outstockConfigUdfList)) {
						item.setDetailConfigUdf(OutstockConfigUdfWrapper.build().listVO(outstockConfigUdfList));
					}
					//获取并给批属性赋值
					/*List<OutstockConfigLot> outstockConfigLotList = OutstockConfigLotCache.listBySsodId(
						item.getSsodId());*/
					List<OutstockConfigLot> outstockConfigLotList = outstockConfigLotService.list(Condition.getQueryWrapper(new OutstockConfigLot())
						.lambda()
						.eq(OutstockConfigLot::getSsodId, item.getSsodId())
					);

					if (Func.isNotEmpty(outstockConfigLotList)) {
						item.setDetailConfigLot(OutstockConfigLotWrapper.build().listVO(outstockConfigLotList));
					}
					//获取并给货源库区赋值
					/*List<OutstockZoneDetail> outstockZoneDetailList = OutstockZoneDetailCache.listBySsodId(
						item.getSsodId());*/
					List<OutstockZoneDetail> outstockZoneDetailList = outstockZoneDetailService.list(Condition.getQueryWrapper(new OutstockZoneDetail())
						.lambda()
						.eq(OutstockZoneDetail::getSsodId, item.getSsodId())
					);
					if (Func.isNotEmpty(outstockZoneDetailList)) {
						item.setOutstockZoneDetail(OutstockZoneDetailWrapper.build().listVO(outstockZoneDetailList));
					}
					//获取并给执行条件赋值
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
				//获取并给策略明细赋值
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
		// 分配结果
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
			// 获取该物品的出库设置
			//SkuOutstock skuOutstock = SkuOutstockCache.getOne(soDetail.getSkuId(), soHeader.getWhId());
			ISkuOutstockService skuOutstockService = SpringUtil.getBean(ISkuOutstockService.class);
			SkuOutstock skuOutstock = skuOutstockService.list(Condition.getQueryWrapper(new SkuOutstock())
				.lambda()
				.eq(SkuOutstock::getSkuId, soDetail.getSkuId())
				.eq(SkuOutstock::getWhId, soHeader.getWhId())
			).stream().findFirst().orElse(null);
			if (ObjectUtils.isEmpty(skuOutstock)) {
				throw new ServiceException("物品：" + soDetail.getSkuName() + " 未设置分配策略！");
			}
			outstockExecute.setSsoId(skuOutstock.getSsoId());
			// 获取该物品的分配策略
			Outstock outstock = outstockService.getById(skuOutstock.getSsoId());
			if (ObjectUtil.isEmpty(outstock)) {
				throw new ServiceException("物品：" + soDetail.getSkuName() + " 指定的分配策略不存在！");
			}
			outstockExecute.setSsoCode(outstock.getSsoCode());
			outstockExecute.setSsoName(outstock.getSsoName());
			// 找到符合条件的分配明细
			OutstockDetail outstockDetail = outstockDetailService.find(outstock, soHeader, soHeader.getBillTypeCd(), sku);
			if (ObjectUtil.isEmpty(outstockDetail)) {
				throw new ServiceException("物品：" + sku.getSkuName() + " 没有可执行策略！");
			}
			outstockExecute.setSsodProcOrder(outstockDetail.getSsodProcOrder());
			outstockExecute.setOutstockFunction(outstockDetail.getOutstockFunction());
			if (Func.isNotEmpty(outstockDetail.getOutstockFunction())) {
				List<String> descList = new ArrayList<>();
				List<Integer> valueList = Func.toIntList(outstockDetail.getOutstockFunction());
				valueList.forEach(value -> {
					descList.add(DictCache.getValue(DictConstant.OUTSTOCK_FUNCTION, value));
				});

				outstockExecute.setOutstockFunctionDesc(Func.join(descList));
			}
			// 获取货源库区
			/*List<OutstockZoneDetail> outstockZoneDetails = OutstockZoneDetailCache.listBySsodId(
				outstockDetail.getSsodId());*/

			List<OutstockZoneDetail> outstockZoneDetails = outstockZoneDetailService.list(Condition.getQueryWrapper(new OutstockZoneDetail())
				.lambda()
				.eq(OutstockZoneDetail::getSsodId, outstockDetail.getSsodId())
			);

			if (ObjectUtil.isEmpty(outstockZoneDetails)) {
				throw new ServiceException("物品：" + sku.getSkuName() + " 未设置货源库区！");
			}
			// 通过订单表头指定的库区类型进行筛选
			List<OutstockZoneDetailVO> outstockZoneDetailVOList = OutstockZoneDetailWrapper.build().listVO(
				outstockZoneDetails);/*.stream().filter(detail -> {
				return detail.getZoneType() == soHeader.getZoneType();
			}).collect(Collectors.toList());*/
			if (ObjectUtil.isEmpty(outstockZoneDetailVOList)) {
				throw new ServiceException("分配策略货源库区与订单拣货库区不匹配！");
			}
			outstockExecute.setSourceZoneName(NodesUtil.join(outstockZoneDetailVOList, "zoneName"));
			// 获取货源库区下所有库位信息
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
			// 获取批属性设定
			/*List<OutstockConfigLot> outstockConfigLots = OutstockConfigLotCache.listBySsodId(
				outstockDetail.getSsodId());*/
			List<OutstockConfigLot> outstockConfigLots = outstockConfigLotService.list(Condition.getQueryWrapper(new OutstockConfigLot())
				.lambda()
				.eq(OutstockConfigLot::getSsodId, outstockDetail.getSsodId())
			);
			// 获取库存信息
			OutstockStockVO stockOutstockRlt = this.getStockInfo(
				sku.getSkuId(), soDetail.getWspId(), soDetail, outstockZoneDetailVOList, outstockConfigLots);
			if (ObjectUtil.isNotEmpty(stockOutstockRlt)) {
				// 根据物品出库设置，处理下库存的顺序
				if (Func.isNotEmpty(stockOutstockRlt.getStockList())) {
//					List<Stock> stockList = FunctionCodeFactory.create(skuOutstock, outstockDetail)
//						.execute(stockOutstockRlt.getStockList(), BigDecimal.ZERO);
					List<Stock> stockList = outstockDetailService.execute(
						outstockDetail, skuOutstock, stockOutstockRlt.getStockList(), BigDecimal.ZERO);
					outstockExecute.setStockList(stockList);
					// 统计下：库存总数、分配占用总数、盘点占用总数
					outstockExecute.setStockQty(stockList.stream().map(stock -> {
						return stock.getStockQty().subtract(stock.getPickQty());
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
					outstockExecute.setOccupyQty(stockList.stream().map(stock -> {
						return stock.getOccupyQty();
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
				}
				// 处理下前端需要显示的数据
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
		// 分配结果
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
				// 根据物品出库设置，处理下库存的顺序
				if (Func.isNotEmpty(stockOutstockRlt.getStockList())) {
					ManualAllocRequestDTO manualAllocRequestDTO = new ManualAllocRequestDTO();
					manualAllocRequestDTO.setDto(dto);
					manualAllocRequestDTO.setStocks(stockOutstockRlt.getStockList());
					List<Stock> stockList = ManualAllocStrategyUtil.executeStrategy(manualAllocRequestDTO);
					outstockExecute.setStockList(stockList);
					// 统计下：库存总数、分配占用总数、盘点占用总数
					outstockExecute.setStockQty(stockList.stream().map(stock -> {
						return stock.getStockQty().subtract(stock.getPickQty());
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
					outstockExecute.setOccupyQty(stockList.stream().map(stock -> {
						return stock.getOccupyQty();
					}).reduce(BigDecimal.ZERO, BigDecimal::add));
				}
				// 处理下前端需要显示的数据
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
					// 屏蔽出库暂存区
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

		// 增加批属性条件
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			if (soDetail.skuLotChk(i)) {
				// 增加订单明细里批属性的条件
				stockQueryWrapper.eq("sku_lot" + i, soDetail.skuLotGet(i));
				stockOutstockRltDTO.getSoDetailSkuLotStrList().add("sku_lot" + i + " = " + soDetail.skuLotGet(i));
			} else {
				// 查找是否设定了批属性
				if (Func.isNotEmpty(configLotList)) {
					int finalI = i;
					List<OutstockConfigLot> configLots = configLotList.stream().filter(lot -> {
						return lot.getSkuLotNumber().equals(finalI);
					}).collect(Collectors.toList());
					if (ObjectUtil.isEmpty(configLots)) {
						continue;
					}
					// 增加批属性设定条件
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
		// 排除 禁用/ 盘点占用 的库位
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
		// 根据 批次号, 入库时间 排序
		stockQueryWrapper.lambda()
			.orderByAsc(Stock::getLotNumber)
			.orderByAsc(Stock::getLastInTime);

		// 拼接执行的SQL语句
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
	 * 封装条件规则
	 *
	 * @param queryWrapper 查询封装器
	 * @param operator     运算算索引
	 * @param column       列名
	 * @param value        值
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
		// 验证策略编码是否存在
		Outstock outstockQuery = new Outstock();
		outstockQuery.setSsoCode(outstockDTO.getSsoCode());
		Outstock findObj = super.getOne(Condition.getQueryWrapper(outstockQuery));
		if (ObjectUtil.isNotEmpty(findObj) && findObj.getSsoCode().equals(outstockDTO.getSsoCode()) &&
			!findObj.getSsoId().equals(outstockDTO.getSsoId())) {
			throw new ServiceException("策略编码：" + findObj.getSsoCode() + " 已存在！");
		}
	}


}
