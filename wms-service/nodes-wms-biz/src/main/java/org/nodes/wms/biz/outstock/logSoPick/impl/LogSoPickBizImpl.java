package org.nodes.wms.biz.outstock.logSoPick.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPicExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickPageResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拣货记录日志接口实现类
 *
 * @author nodesc
 **/
@Service
@RequiredArgsConstructor
public class LogSoPickBizImpl implements LogSoPickBiz {

	private final LogSoPickDao logSoPickDao;

	@Override
	public List<LogSoPickIndexResponse> findPickSkuQtyTop10() {
		return logSoPickDao.getPickSkuQtyTop10();
	}

	@Override
	public Page<LogSoPickForSoDetailResponse> pageLogSoPickForSoDetailBySoBillId(Query query,
																				 SoBillIdRequest soBillIdRequest) {
		return logSoPickDao.pageForSoDetailBySoBillId(Condition.getPage(query), soBillIdRequest.getSoBillId());
	}

	@Override
	public Page<LogSoPickPageResponse> page(Query query, LogSoPickPageQuery logSoPickPageQuery) {
		return logSoPickDao.page(Condition.getPage(query), logSoPickPageQuery);
	}

	@Override
	public void export(LogSoPickPageQuery logSoPickPageQuery, HttpServletResponse response) {
		List<LogSoPicExcelResponse> logSoPicExcelList = logSoPickDao.listByQuery(logSoPickPageQuery);
		ExcelUtil.export(response, "", "", logSoPicExcelList, LogSoPicExcelResponse.class);
	}

	@Override
	public List<LogSoPick> findByIds(List<Long> lsopIdList) {
		return logSoPickDao.getByIds(lsopIdList);
	}

	@Override
	public List<LogSoPick> findEnableBySoHeaderId(Long soBillId) {
		List<LogSoPick> allPickLog = logSoPickDao.findBySoHeaderId(soBillId);
		if (Func.isEmpty(allPickLog)) {
			return null;
		}

		// 排除撤销的记录
		List<LogSoPick> cancelPickLogs = allPickLog.stream()
			.filter(item -> BigDecimalUtil.le(item.getPickRealQty(), BigDecimal.ZERO))
			.collect(Collectors.toList());
		if (Func.isEmpty(cancelPickLogs)) {
			return allPickLog;
		}

		// 正向的拣货记录
		List<LogSoPick> result = allPickLog.stream()
			.filter(item -> BigDecimalUtil.gt(item.getPickRealQty(), BigDecimal.ZERO))
			.collect(Collectors.toList());
		for (LogSoPick logSoPickOfCancel : cancelPickLogs) {
			// 从正向拣货记录中排除撤销的记录
			for (LogSoPick item : result) {
				if (SkuLotUtil.compareAllSkuLot(item, logSoPickOfCancel)
					&& BigDecimalUtil
					.eq(item.getPickRealQty().add(logSoPickOfCancel.getPickRealQty()), BigDecimal.ZERO)
					&& item.getSoDetailId().equals(logSoPickOfCancel.getSoDetailId())) {
					result.remove(item);
					break;
				}
			}
		}
		return result;
	}
}
