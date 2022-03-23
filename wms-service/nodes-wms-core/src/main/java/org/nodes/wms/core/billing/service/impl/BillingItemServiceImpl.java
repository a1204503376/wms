package org.nodes.wms.core.billing.service.impl;

import org.nodes.wms.core.billing.cache.BillingItemCache;
import org.nodes.wms.core.billing.entity.BillingItem;
import org.nodes.wms.core.billing.vo.BillingItemVO;
import org.nodes.wms.core.billing.mapper.BillingItemMapper;
import org.nodes.wms.core.billing.service.IBillingItemService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.billing.wrapper.BillingItemWrapper;

import java.io.Serializable;
import java.util.Collection;

/**
 * 计费项目 服务实现类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class BillingItemServiceImpl<M extends BillingItemMapper, T extends BillingItem>
	extends BaseServiceImpl<BillingItemMapper, BillingItem>
	implements IBillingItemService {

	@Override
	public boolean save(BillingItem entity) {
		boolean result = super.save(entity);
		if (result) {
			//BillingItemCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(BillingItem entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//BillingItemCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//BillingItemCache.removeById(id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//BillingItemCache.removeByIds(idList);
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(BillingItem billingItem){
		long cnt = super.count(Condition.getQueryWrapper(new BillingItem())
		.lambda()
		.eq(BillingItem::getCode,billingItem.getCode()));
		if (cnt > 0L){
			throw new ServiceException("该编码已存在，请修改编码");
		}
		super.saveOrUpdate(billingItem);
	return true;
	}
}
