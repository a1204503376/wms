
package org.nodes.wms.core.strategy.service.impl;


import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.strategy.dto.InstockDetailDTO;
import org.nodes.wms.dao.putaway.mapper.StInstockDetailMapper;
import org.nodes.wms.core.strategy.service.IInstockConfigLotService;
import org.nodes.wms.core.strategy.service.IInstockConfigService;
import org.nodes.wms.core.strategy.service.IInstockDetailService;
import org.nodes.wms.dao.putaway.entities.StInstock;
import org.nodes.wms.dao.putaway.entities.StInstockConfig;
import org.nodes.wms.dao.putaway.entities.StInstockConfigLot;
import org.nodes.wms.dao.putaway.entities.StInstockDetail;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 上架策略明细服务实现类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class InstockDetailServiceImpl<M extends StInstockDetailMapper, T extends StInstockDetail>
	extends BaseServiceImpl<StInstockDetailMapper, StInstockDetail>
	implements IInstockDetailService {

	@Autowired
	IInstockConfigLotService instockConfigLotService;
	@Autowired
	IInstockConfigService instockConfigService;

	@Override
	public boolean save(StInstockDetail entity) {
		boolean result = super.save(entity);
		if (result) {
			//InstockDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(StInstockDetail entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//InstockDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//InstockDetailCache.remove((Long) id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//InstockDetailCache.remove(idList);
		}
		return result;
	}

	@Override
	public boolean save(InstockDetailDTO instockDetailDTO) {
		boolean result = super.save(instockDetailDTO);

		if (result) {
			//InstockDetailCache.saveOrUpdate(instockDetailDTO);

			if (ObjectUtil.isNotEmpty(instockDetailDTO.getInstockConfigLotList())) {
				instockDetailDTO.getInstockConfigLotList().forEach(u -> u.setSsidId(instockDetailDTO.getSsidId()));
				instockConfigLotService.saveBatch(instockDetailDTO.getInstockConfigLotList(),
					instockDetailDTO.getInstockConfigLotList().size());
			}
			if (ObjectUtil.isNotEmpty(instockDetailDTO.getInstockConfigList())) {
				instockDetailDTO.getInstockConfigList().forEach(u -> u.setSsidId(instockDetailDTO.getSsidId()));
				instockConfigService.saveBatch(instockDetailDTO.getInstockConfigList(),
					instockDetailDTO.getInstockConfigList().size());
			}
		}
		return result;
	}


	@Override
	public boolean updateById(InstockDetailDTO instockDetailDTO) {
		boolean result = super.updateById(instockDetailDTO);
		if (result) {
			//InstockDetailCache.saveOrUpdate(instockDetailDTO);
		}
		return result;
	}

	@Override
	public StInstockDetail find(@NotNull StInstock instock, String billTypeCd, Long skuTypeId,
								SkuLotBaseEntity skuLotEntity) {
		StInstockDetail instockDetail = null;
		IInstockConfigLotService instockConfigLotService = SpringUtil.getBean(IInstockConfigLotService.class);
		IInstockDetailService instockDetailService = SpringUtil.getBean(IInstockDetailService.class);
		//List<InstockDetail> instockDetailList = InstockDetailCache.list(instock.getSsiId());
		List<StInstockDetail> instockDetailList = instockDetailService.list(Condition.getQueryWrapper(new StInstockDetail())
		.lambda()
		.eq(StInstockDetail::getSsiId,instock.getSsiId())
		);
		if (Func.isEmpty(instockDetailList)) {
			throw new ServiceException("上架策略：" + instock.getSsiName() + " 明细为空！");
		}
		IInstockConfigService instockConfigService = SpringUtil.getBean(IInstockConfigService.class);
		// 按执行顺序排序
		instockDetailList.sort(Comparator.comparing(StInstockDetail::getSsidProcOrder));
		// 遍历明细，找到满足条件的策略明细
		for (StInstockDetail item : instockDetailList) {
			// 获取执行条件
			//List<InstockConfig> instockConfigList = InstockConfigCache.listByssidId(item.getSsidId());
			List<StInstockConfig> instockConfigList = instockConfigService.list(Condition.getQueryWrapper(new StInstockConfig())
			.lambda()
			.eq(StInstockConfig::getSsidId,item.getSsidId())
			);
			// 执行条件为空的情况下，表示忽略（也就是满足）
			if (Func.isEmpty(instockConfigList)) {
				instockDetail = item;
				break;
			}
			// 获取满足执行条件的数量
			Long count = instockConfigList.stream().filter(u -> {
				Boolean billTypeCdResult = true;
				Boolean skuTypeIdResult = true;
				if (Func.isNotEmpty(u.getBillTypeCd())) {
					billTypeCdResult = u.getBillTypeCd().equals(billTypeCd);
				}
				if (Func.isNotEmpty(u.getSkuTypeId())) {
					ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
					SkuType skuType = skuTypeService.getById(skuTypeId);
					skuTypeIdResult = Func.isNotEmpty(skuType) && Func.isNotEmpty(skuType.getTypePath())
						&& skuType.getTypePath().contains(String.valueOf(u.getSkuTypeId()));
				}
				return billTypeCdResult && skuTypeIdResult;
			}).count();
			if (count == 0) {
				continue;
			}
			// 获取批属性设定
			//List<InstockConfigLot> instockConfigLotList = InstockConfigLotCache.listBySsidId(item.getSsidId());
			List<StInstockConfigLot> instockConfigLotList = instockConfigLotService.list(Condition.getQueryWrapper(new StInstockConfigLot())
			.lambda()
			.eq(StInstockConfigLot::getSsidId,item.getSsidId())
			);
 			if (!instockConfigLotService.match(instockConfigLotList, skuLotEntity)) {
				continue;
			}
			instockDetail = item;
			break;
		}
		return instockDetail;
	}
}
