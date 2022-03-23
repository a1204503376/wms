
package org.nodes.wms.core.strategy.service.impl;

import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.common.entity.AttributeBase;
import org.nodes.wms.core.strategy.cache.OutstockConfigUdfCache;
import org.nodes.wms.core.strategy.entity.OutstockConfigUdf;
import org.nodes.wms.core.strategy.mapper.OutstockConfigUdfMapper;
import org.nodes.wms.core.strategy.service.IOutstockConfigUdfService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 分配策略自定义属性设定 服务实现类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OutstockConfigUdfServiceImpl<M extends OutstockConfigUdfMapper, T extends OutstockConfigUdf>
	extends BaseServiceImpl<OutstockConfigUdfMapper, OutstockConfigUdf>
	implements IOutstockConfigUdfService {

	@Override
	public boolean save(OutstockConfigUdf entity) {
		boolean result = super.save(entity);
		if (result) {
			//OutstockConfigUdfCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(OutstockConfigUdf entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//OutstockConfigUdfCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//OutstockConfigUdfCache.remove(id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//OutstockConfigUdfCache.remove(idList);
		}
		return result;
	}

	/**
	 * 效验单据自定义属性
	 *
	 * @param outstockConfigUdfs 单据自定义属性集合
	 * @param soHeader           验证单据主表
	 * @return 自定义属性成立结果集合
	 */
	@Override
	public List<Boolean> matchBillUdf(List<OutstockConfigUdf> outstockConfigUdfs, AttributeBase soHeader) {
		List<Boolean> result = new ArrayList<>();
		for (OutstockConfigUdf configUdf : outstockConfigUdfs) {
			// 获取订单自定义属性
			String value = soHeader.getValue(configUdf.getBillUdfNumber());
			if (StringUtil.isEmpty(value)) {
				continue;
			}
			int operator = configUdf.getBillUdfOperation();
			// 验证结果
			Boolean matchResult = NodesUtil.match(value, configUdf.getBillUdfVal1(), operator) ||
					NodesUtil.match(value, configUdf.getBillUdfVal2(), operator) ||
					NodesUtil.match(value, configUdf.getBillUdfVal3(), operator);

			result.add(matchResult);
		}

		return result;
	}
}
