package org.nodes.wms.dao.lendreturn.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.lendreturn.LogNoReturnDao;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.input.LogLendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;
import org.nodes.wms.dao.lendreturn.mapper.LogNoReturnMapper;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 未归还记录 Dao接口 实现类
 */
@Repository
public class LogNoReturnDaoImpl
	extends BaseServiceImpl<LogNoReturnMapper, LogNoReturn>
	implements LogNoReturnDao {

	/**
	 * 判断物品是否存在借出记录
	 *
	 * @return true:存在借出记录，false：没有记录
	 */
	@Override
	public LogNoReturn sameSku(LogLendReturnRequest logLendReturnRequest) {

		LambdaQueryWrapper<LogNoReturn> queryWrapper = getLambdaQuery()
			.eq(LogNoReturn::getSkuId, logLendReturnRequest.getSkuId())
			.eq(LogNoReturn::getLendReturnName,logLendReturnRequest.getLendReturnName());

		SkuLotUtil.applySql(queryWrapper,logLendReturnRequest);

		return super.getOne(queryWrapper);
	}

	private static LambdaQueryWrapper<LogNoReturn> getLambdaQuery() {
		return Wrappers.lambdaQuery(LogNoReturn.class);
	}

	@Override
    public Page<NoReturnResponse> selectPage(Page<LogNoReturn> page, LendReturnQuery lendReturnQuery) {
		LambdaQueryWrapper<LogNoReturn> queryWrapper = getLambdaQuery()
			.in(Func.isNotEmpty(lendReturnQuery.getSkuIdList()),LogNoReturn::getSkuId,lendReturnQuery.getSkuIdList())
			.like(Func.isNotBlank(lendReturnQuery.getLendReturnName()),LogNoReturn::getLendReturnName,lendReturnQuery.getLendReturnName())
			.ge(Func.notNull(lendReturnQuery.getCreateTimeBegin()), BaseEntity::getCreateTime,lendReturnQuery.getCreateTimeBegin())
			.le(Func.notNull(lendReturnQuery.getCreateTimeEnd()),BaseEntity::getCreateTime,lendReturnQuery.getCreateTimeEnd());

		SkuLotUtil.applySql(queryWrapper,lendReturnQuery);

		IPage<LogNoReturn> logNoReturnPage = page(page, queryWrapper);

		return new Page<NoReturnResponse>(logNoReturnPage.getCurrent(), logNoReturnPage.getSize(), logNoReturnPage.getTotal())
			.setRecords(Func.copy(logNoReturnPage.getRecords(), NoReturnResponse.class));
    }

	@Override
	public List<LogNoReturn> listByQuery(LendReturnQuery lendReturnQuery) {
		LambdaQueryWrapper<LogNoReturn> queryWrapper = getLambdaQuery()
			.in(Func.isNotEmpty(lendReturnQuery.getSkuIdList()),LogNoReturn::getSkuId,lendReturnQuery.getSkuIdList())
			.like(Func.isNotBlank(lendReturnQuery.getLendReturnName()),LogNoReturn::getLendReturnName,lendReturnQuery.getLendReturnName())
			.ge(Func.notNull(lendReturnQuery.getCreateTimeBegin()), BaseEntity::getCreateTime,lendReturnQuery.getCreateTimeBegin())
			.le(Func.notNull(lendReturnQuery.getCreateTimeEnd()),BaseEntity::getCreateTime,lendReturnQuery.getCreateTimeEnd());

		SkuLotUtil.applySql(queryWrapper,lendReturnQuery);
		return super.list(queryWrapper);
	}
}
