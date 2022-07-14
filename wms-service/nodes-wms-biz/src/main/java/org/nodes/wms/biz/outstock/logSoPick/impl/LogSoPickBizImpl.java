package org.nodes.wms.biz.outstock.logSoPick.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 拣货记录日志接口实现类
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
	public Page<LogSoPickForSoDetailResponse> pageLogSoPickForSoDetailBySoBillId(Query query, SoBillIdRequest soBillIdRequest) {
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
}
