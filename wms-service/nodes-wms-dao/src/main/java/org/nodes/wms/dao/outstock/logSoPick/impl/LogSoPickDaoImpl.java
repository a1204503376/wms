package org.nodes.wms.dao.outstock.logSoPick.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPicExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickPageResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.logSoPick.mapper.LogSoPickMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 拣货记录日志Dao接口实现类
 *
 * @author nodesc
 **/
@Repository
public class LogSoPickDaoImpl extends BaseServiceImpl<LogSoPickMapper, LogSoPick> implements LogSoPickDao {

	@Override
	public List<LogSoPickIndexResponse> getPickSkuQtyTop10() {
		return super.baseMapper.selectPickSkuQtyTop10();
	}

	@Override
	public Page<LogSoPickForSoDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, Long soBillId) {
		return super.baseMapper.pageForSoDetailBySoBillId(page, soBillId);
	}

	@Override
	public Page<LogSoPickPageResponse> page(IPage<?> page, LogSoPickPageQuery logSoPickPageQuery) {
		return super.baseMapper.page(page, logSoPickPageQuery);
	}

	@Override
	public List<LogSoPicExcelResponse> listByQuery(LogSoPickPageQuery logSoPickPageQuery) {
		return super.baseMapper.listByQuery(logSoPickPageQuery);
	}

	@Override
	public List<LogSoPick> getByIds(List<Long> lsopIdList) {
		return super.listByIds(lsopIdList);
	}

	@Override
	public void saveLogSoPick(LogSoPick logSoPick) {
		if (!super.save(logSoPick)) {
			throw new ServiceException("保存拣货记录失败");
		}
	}

	@Override
	public List<LogSoPick> findBySoHeaderId(Long soBillId) {
		LambdaQueryWrapper<LogSoPick> queryWrapper = Wrappers.lambdaQuery(LogSoPick.class);
		queryWrapper.eq(LogSoPick::getSoBillId, soBillId);
		return super.list(queryWrapper);
	}

	@Override
	public void setCancelPick(Long lsopId) {
		LambdaUpdateWrapper<LogSoPick> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper
			.eq(LogSoPick::getLsopId, lsopId)
			.set(LogSoPick::getCancelLogId, lsopId);
		super.update(updateWrapper);
	}

	@Override
	public Integer getBoxCountBySoHeaderId(Long soBillId) {
		AssertUtil.notNull(soBillId, "发货单ID不能为空");
		LambdaQueryWrapper<LogSoPick> queryWrapper = Wrappers.lambdaQuery(LogSoPick.class);
		queryWrapper
			.apply(String.format("so_bill_id =%s and cancel_log_id is null or so_bill_id =%s and  cancel_log_id = ''", soBillId, soBillId));
		List<LogSoPick> soPickList = super.list(queryWrapper);
		if (soPickList.size() == 0) {
			return 0;
		}
		List<String> boxCodeList = soPickList.stream()
			.map(LogSoPick::getBoxCode)
			.distinct()
			.collect(Collectors.toList());
		return boxCodeList.size();
	}
}
