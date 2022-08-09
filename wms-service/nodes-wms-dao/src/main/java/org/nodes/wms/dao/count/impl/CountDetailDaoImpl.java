package org.nodes.wms.dao.count.impl;

import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.count.CountDetailDao;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.mapper.CountDetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 盘点单明细
 */
@Repository
public class CountDetailDaoImpl
	extends BaseServiceImpl<CountDetailMapper, CountDetail>
	implements CountDetailDao {

    @Override
    public boolean deleteByIds(List<Long> ids) {
		AssertUtil.notEmpty(ids,"参数为空");
		return baseMapper.deleteByCountDetailId(ids)>0;
    }

	@Override
	public List<CountDetail> selectByCountBillId(Long countBillId) {
		AssertUtil.notNull(countBillId,"盘点单ID为空");
		return super.lambdaQuery()
			.eq(CountDetail::getCountBillId,countBillId)
			.select(
				CountDetail::getCountDetailId,
				CountDetail::getCountDetailState,
				CountDetail::getLocId,
				CountDetail::getLocCode,
				CountDetail::getBoxCode,
				CountDetail::getUserName)
			.groupBy(CountDetail::getLocCode)
			.list();
	}
}
