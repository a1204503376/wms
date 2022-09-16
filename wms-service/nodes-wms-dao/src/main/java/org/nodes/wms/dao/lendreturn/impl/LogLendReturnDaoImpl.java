package org.nodes.wms.dao.lendreturn.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.lendreturn.LogLendReturnDao;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.output.LendReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.nodes.wms.dao.lendreturn.mapper.LogLendReturnMapper;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

/**
 * 借出归还记录 Dao接口 实现类
 */
@Repository
public class LogLendReturnDaoImpl
	extends BaseServiceImpl<LogLendReturnMapper, LogLendReturn>
	implements LogLendReturnDao {

    @Override
    public Page<LendReturnResponse> selectPage(IPage<LogLendReturn> page, LendReturnQuery lendReturnQuery) {
		LambdaQueryWrapper<LogLendReturn> queryWrapper = Wrappers.lambdaQuery(LogLendReturn.class)
			.in(Func.isNotEmpty(lendReturnQuery.getTypeList()),LogLendReturn::getType, lendReturnQuery.getTypeList())
			.in(Func.isNotEmpty(lendReturnQuery.getSkuIdList()),LogLendReturn::getSkuId,lendReturnQuery.getSkuIdList())
			.eq(Func.isNotBlank(lendReturnQuery.getBillNo()),LogLendReturn::getBillNo,lendReturnQuery.getBillNo())
			.like(Func.isNotBlank(lendReturnQuery.getLendReturnName()),LogLendReturn::getLendReturnName,lendReturnQuery.getLendReturnName())
			.ge(Func.notNull(lendReturnQuery.getCreateTimeBegin()), BaseEntity::getCreateTime,lendReturnQuery.getCreateTimeBegin())
			.le(Func.notNull(lendReturnQuery.getCreateTimeEnd()),BaseEntity::getCreateTime,lendReturnQuery.getCreateTimeEnd());

		SkuLotUtil.applySql(queryWrapper,lendReturnQuery);

		IPage<LogLendReturn> logLendReturnPage = page(page, queryWrapper);

		return new Page<LendReturnResponse>(logLendReturnPage.getCurrent(), logLendReturnPage.getSize(), logLendReturnPage.getTotal())
			.setRecords(Func.copy(logLendReturnPage.getRecords(), LendReturnResponse.class));
    }

    @Override
    public boolean deleteBySoDetailId(Long soDetailId) {
        return super.baseMapper.deleteBySoDetailId(soDetailId);
    }
}
