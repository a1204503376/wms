
package org.nodes.wms.core.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.wms.core.system.entity.BarcodeRule;
import org.nodes.wms.core.system.mapper.BarcodeRuleMapper;
import org.nodes.wms.core.system.service.IBarcodeRuleService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 条码规则服务实现类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class BarcodeRuleServiceImpl<M extends BarcodeRuleMapper, T extends BarcodeRule>
	extends BaseServiceImpl<BarcodeRuleMapper, BarcodeRule>
	implements IBarcodeRuleService {

	@Override
	public boolean save(BarcodeRule entity) {
		List<BarcodeRule> barcodeRules = super.list(Wrappers.lambdaQuery(BarcodeRule.class)
			.eq(BarcodeRule::getWhId, entity.getWhId())
			.eq(BarcodeRule::getBarcodeType, entity.getBarcodeType()));
		if(Func.isNotEmpty(barcodeRules)){
			throw new ServiceException("该库房已经存在该条码类型规则！");
		}
		return super.save(entity);
	}

	@Override
	public boolean updateById(BarcodeRule entity) {
		List<BarcodeRule> barcodeRules = super.list(Wrappers.lambdaQuery(BarcodeRule.class)
			.eq(BarcodeRule::getWhId, entity.getWhId())
			.ne(BarcodeRule::getSbrId, entity.getSbrId())
			.eq(BarcodeRule::getBarcodeType, entity.getBarcodeType()));
		if(Func.isNotEmpty(barcodeRules)) {
			throw new ServiceException("该库房已经存在该条码类型规则！");
		}
		return super.updateById(entity);
	}
}
