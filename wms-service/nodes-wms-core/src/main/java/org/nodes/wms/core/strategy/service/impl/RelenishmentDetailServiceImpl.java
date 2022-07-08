
package org.nodes.wms.core.strategy.service.impl;


import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.core.strategy.dto.RelenishmentDetailDTO;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.nodes.wms.core.strategy.mapper.RelenishmentDetailMapper;
import org.nodes.wms.core.strategy.service.IRelenishmentDetailService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
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
 * 补货策略明细服务实现类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RelenishmentDetailServiceImpl<M extends RelenishmentDetailMapper, T extends RelenishmentDetail>
	extends BaseServiceImpl<RelenishmentDetailMapper, RelenishmentDetail>
	implements IRelenishmentDetailService {


	@Override
	public boolean save(RelenishmentDetail entity) {
		entity.setSsidProcOrder(-1);
		entity.setSkuLevel(-1);
		boolean result = super.save(entity);
		if (result) {
			//RelenishmentDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(RelenishmentDetail entity) {
		entity.setSsidProcOrder(-1);
		entity.setSkuLevel(-1);
		boolean result = super.updateById(entity);
		if (result) {
			//RelenishmentDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//RelenishmentDetailCache.remove((Long) id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//RelenishmentDetailCache.remove(idList);
		}
		return result;
	}

	@Override
	public boolean save(RelenishmentDetailDTO relenishmentDetailDTO) {
		relenishmentDetailDTO.setSsidProcOrder(-1);
		relenishmentDetailDTO.setSkuLevel(-1);
		boolean result = super.save(relenishmentDetailDTO);

		if (result) {
			//RelenishmentDetailCache.saveOrUpdate(relenishmentDetailDTO);
		}
		return result;
	}


	@Override
	public boolean updateById(RelenishmentDetailDTO instockDetailDTO) {
		instockDetailDTO.setSsidProcOrder(-1);
		instockDetailDTO.setSkuLevel(-1);
		boolean result = super.updateById(instockDetailDTO);
		if (result) {
			//RelenishmentDetailCache.saveOrUpdate(instockDetailDTO);
		}
		return result;
	}

	@Override
	public RelenishmentDetail find(@NotNull Relenishment instock, String billTypeCd, Long skuTypeId,
							  SkuLotBaseEntity skuLotEntity) {
		RelenishmentDetail instockDetail = null;
		IRelenishmentDetailService relenishmentDetailService = SpringUtil.getBean(IRelenishmentDetailService.class);
		//List<RelenishmentDetail> instockDetailList = RelenishmentDetailCache.list(instock.getSsiId());
		List<RelenishmentDetail> instockDetailList = relenishmentDetailService.list(Condition.getQueryWrapper(new RelenishmentDetail())
		.lambda()
		.eq(RelenishmentDetail::getSsiId,instock.getSsiId())
		);
		if (Func.isEmpty(instockDetailList)) {
			throw new ServiceException("补货策略：" + instock.getSsiName() + " 明细为空！");
		}
		// 按执行顺序排序
		instockDetailList.sort(Comparator.comparing(RelenishmentDetail::getSsidProcOrder));
		return instockDetail;
	}
}
