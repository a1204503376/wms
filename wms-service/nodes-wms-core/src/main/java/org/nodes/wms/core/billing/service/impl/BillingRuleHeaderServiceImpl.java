package org.nodes.wms.core.billing.service.impl;

import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.billing.cache.BillingRuleHeaderCache;
import org.nodes.wms.core.billing.dto.BillingRuleHeaderDTO;
import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import org.nodes.wms.core.billing.service.IBillingRuleDetailService;
import org.nodes.wms.core.billing.vo.BillingRuleHeaderVO;
import org.nodes.wms.core.billing.mapper.BillingRuleHeaderMapper;
import org.nodes.wms.core.billing.service.IBillingRuleHeaderService;
import org.nodes.wms.core.billing.wrapper.BillingRuleDetailWrapper;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.billing.wrapper.BillingRuleHeaderWrapper;

import javax.swing.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 计费规则主表 服务实现类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class BillingRuleHeaderServiceImpl<M extends BillingRuleHeaderMapper, T extends BillingRuleHeader>
	extends BaseServiceImpl<BillingRuleHeaderMapper, BillingRuleHeader>
	implements IBillingRuleHeaderService {

	@Autowired
	IBillingRuleDetailService billingRuleDetailService;

	@Override
	public boolean save(BillingRuleHeaderDTO billingRuleHeaderDTO) {
		if (Func.isEmpty(billingRuleHeaderDTO.getDetailList())) {
			throw new ServiceException("明细不能为空! ");
		}
		Warehouse warehouse = WarehouseCache.getById(billingRuleHeaderDTO.getWhId());
		if (Func.isNotEmpty(warehouse)) {
			billingRuleHeaderDTO.setWhCode(warehouse.getWhCode());
			billingRuleHeaderDTO.setWhName(warehouse.getWhName());
		}
		boolean result = super.save(billingRuleHeaderDTO);
		if (result) {
			BillingRuleHeaderCache.saveOrUpdate(billingRuleHeaderDTO);
			billingRuleHeaderDTO.getDetailList().forEach(detail -> {
				detail.setHeaderId(billingRuleHeaderDTO.getId());
			});
			billingRuleDetailService.saveOrUpdateBatch(billingRuleHeaderDTO.getDetailList());
		}
		return result;
	}

	@Override
	public boolean updateById(BillingRuleHeaderDTO billingRuleHeaderDTO) {
		if (Func.isEmpty(billingRuleHeaderDTO.getDetailList())) {
			throw new ServiceException("明细不能为空! ");
		}
		boolean result = super.updateById(billingRuleHeaderDTO);
		if (result) {
			BillingRuleHeaderCache.saveOrUpdate(billingRuleHeaderDTO);
			List<Long> detailIdList = NodesUtil.toList(billingRuleHeaderDTO.getDetailList(), BillingRuleHeader::getId);
			if (Func.isNotEmpty(detailIdList)) {
				billingRuleDetailService.remove(
					Condition.getQueryWrapper(new BillingRuleDetail()).lambda()
						.notIn(BillingRuleDetail::getId, detailIdList)
						.eq(BillingRuleDetail::getHeaderId, billingRuleHeaderDTO.getId()));
			}
			billingRuleDetailService.saveOrUpdateBatch(billingRuleHeaderDTO.getDetailList());
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(BillingRuleHeaderDTO entity) {
		return Func.isEmpty(super.getIdVal(entity)) ? this.save(entity) : this.updateById(entity);
	}

	@Override
	public BillingRuleHeaderVO getDetail(Serializable id) {
		BillingRuleHeaderVO billingRuleHeader = BillingRuleHeaderWrapper.build().entityVO(super.getById(id));
		if (Func.isNotEmpty(billingRuleHeader)) {
			List<BillingRuleDetail> detailList = billingRuleDetailService.list(
				Condition.getQueryWrapper(new BillingRuleDetail()).lambda()
					.eq(BillingRuleDetail::getHeaderId, id));
			billingRuleHeader.setDetailList(BillingRuleDetailWrapper.build().listVO(detailList));
		}
		return billingRuleHeader;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			BillingRuleHeaderCache.removeById(id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			BillingRuleHeaderCache.removeByIds(idList);
		}
		return result;
	}
}
