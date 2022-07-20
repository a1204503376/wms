package org.nodes.wms.dao.common.log.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.common.log.LogActionDao;
import org.nodes.wms.dao.common.log.dto.input.LogActionPageQuery;
import org.nodes.wms.dao.common.log.dto.input.LogPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogActionExcelResponse;
import org.nodes.wms.dao.common.log.dto.output.LogActionPageResponse;
import org.nodes.wms.dao.common.log.dto.output.LogReceiveResponse;
import org.nodes.wms.dao.common.log.dto.output.LogTaskResponse;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.mapper.LogActionMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 审计日志Service
 *
 * @author 王智勇
 */
@Repository
@Service
@RequiredArgsConstructor
public class LogActionDaoImpl extends BaseServiceImpl<LogActionMapper, LogAction> implements LogActionDao {
	/**
	 * 添加审计日志添加对象
	 *
	 * @param logAction 日志审批对象
	 * @return 是否成功
	 */
	@Override
	public Boolean insertLogAction(LogAction logAction) {
		return super.save(logAction);
	}

	@Override
	public List<LogAction> findLogByBillId(Long billId) {
		return super.list(new LambdaQueryWrapper<LogAction>()
			.eq(LogAction::getBillId, billId).eq(LogAction::getType, billId));
	}

	@Override
	public Page<LogTaskResponse> getPage(LogPageQuery logPageQuery, IPage<LogAction> page) {
		return super.baseMapper.getPage(logPageQuery, page);
	}

	/**
	 * @param logActionPageQuery 业务日志查询条件
	 * @param page               分页参数
	 * @return 业务日志响应对象
	 */
	@Override
	public Page<LogActionPageResponse> getLists(LogActionPageQuery logActionPageQuery, IPage<LogAction> page) {
		return super.baseMapper.getLists(logActionPageQuery, page);
	}

	@Override
	public List<LogActionExcelResponse> getActionLists(LogActionPageQuery logActionPageQuery) {
		return super.baseMapper.getActionLists(logActionPageQuery);
	}

	@Override
	public List<LogReceiveResponse> findLogByReceiveId(Long receiveId) {
		return super.baseMapper.findLogByReceiveId(receiveId);
	}

	@Override
	public IPage<LogAction> pageLogByBillId(IPage<LogAction> page, Long billId) {
		QueryWrapper<LogAction> lambdaQueryWrapper = Wrappers.query();
		lambdaQueryWrapper
			.eq("bill_id", billId)
			.orderByDesc(Func.isEmpty(page.orders()), "create_time");
		return super.baseMapper.selectPage(page, lambdaQueryWrapper);
	}
}
