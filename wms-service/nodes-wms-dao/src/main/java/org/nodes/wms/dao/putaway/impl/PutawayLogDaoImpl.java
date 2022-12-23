package org.nodes.wms.dao.putaway.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.wms.dao.putaway.PutawayLogDao;
import org.nodes.wms.dao.putaway.dto.input.PutawayPageQuery;
import org.nodes.wms.dao.putaway.dto.output.PutawayLogExcelResponse;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.nodes.wms.dao.putaway.mapper.PutawayLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class PutawayLogDaoImpl
	extends BaseServiceImpl<PutawayLogMapper, PutawayLog> implements PutawayLogDao {

	@Override
	public IPage<PutawayLog> getPage(IPage<PutawayLog> page, PutawayPageQuery putawayPageQuery) {
		LambdaQueryWrapper<PutawayLog> queryWrapper = Wrappers.lambdaQuery();
			queryWrapper
				.like(Func.isNotBlank(putawayPageQuery.getBoxCode()), PutawayLog::getBoxCode, putawayPageQuery.getBoxCode())
				.like(Func.isNotBlank(putawayPageQuery.getLpnCode()), PutawayLog::getLpnCode, putawayPageQuery.getLpnCode())
				.in(Func.isNotEmpty(putawayPageQuery.getSkuIdList()), PutawayLog::getSkuId, putawayPageQuery.getSkuIdList())
				.like(Func.isNotBlank(putawayPageQuery.getTargetLocCode()), PutawayLog::getTargetLocCode, putawayPageQuery.getTargetLocCode())
				.like(Func.isNotBlank(putawayPageQuery.getSkuLot1()), PutawayLog::getSkuLot1, putawayPageQuery.getSkuLot1())
				.like(Func.isNotBlank(putawayPageQuery.getSkuLot2()), PutawayLog::getSkuLot2, putawayPageQuery.getSkuLot2());
		return super.baseMapper.selectPage(page, queryWrapper);
	}

	public List<PutawayLogExcelResponse> getListForExport(PutawayPageQuery queryParam) {
		LambdaQueryWrapper<PutawayLog> queryWrapper = Wrappers.lambdaQuery(PutawayLog.class);
		queryWrapper
			.like(Func.isNotBlank(queryParam.getBoxCode()), PutawayLog::getBoxCode, queryParam.getBoxCode())
			.like(Func.isNotBlank(queryParam.getLpnCode()), PutawayLog::getLpnCode, queryParam.getLpnCode())
			.in(Func.isNotEmpty(queryParam.getSkuIdList()), PutawayLog::getSkuId, queryParam.getSkuIdList())
			.like(Func.isNotBlank(queryParam.getTargetLocCode()),PutawayLog::getTargetLocCode, queryParam.getTargetLocCode())
			.like(Func.isNotBlank(queryParam.getSkuLot1()), PutawayLog::getSkuLot1, queryParam.getSkuLot1())
			.like(Func.isNotBlank(queryParam.getSkuLot2()), PutawayLog::getSkuLot2, queryParam.getSkuLot2());
		return super.baseMapper.listByWrapper(queryWrapper);
	}
}
