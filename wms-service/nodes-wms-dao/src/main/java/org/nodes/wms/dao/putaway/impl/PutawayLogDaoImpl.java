package org.nodes.wms.dao.putaway.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.wms.dao.putaway.PutawayLogDao;
import org.nodes.wms.dao.putaway.dto.input.PutawayPageQuery;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.nodes.wms.dao.putaway.mapper.PutawayLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;


@Repository
public class PutawayLogDaoImpl
	extends BaseServiceImpl<PutawayLogMapper, PutawayLog> implements PutawayLogDao {

	@Override
	public IPage<PutawayLog> getPage(IPage<PutawayLog> page, PutawayPageQuery putawayPageQuery) {
		LambdaQueryWrapper<PutawayLog> queryWrapper = Wrappers.lambdaQuery();

			queryWrapper.like(PutawayLog::getBoxCode, putawayPageQuery.getBoxCode())
				.like(PutawayLog::getLpnCode, putawayPageQuery.getLpnCode())
				.like(PutawayLog::getSkuCode, putawayPageQuery.getSkuCode())
				.like(PutawayLog::getTargetLocCode, putawayPageQuery.getTargetLocCode())
				.like(PutawayLog::getSkuLot1, putawayPageQuery.getSkuLot1())
				.like(PutawayLog::getSkuLot2, putawayPageQuery.getSkuLot2());
		return super.baseMapper.selectPage(page, queryWrapper);
	}
}
