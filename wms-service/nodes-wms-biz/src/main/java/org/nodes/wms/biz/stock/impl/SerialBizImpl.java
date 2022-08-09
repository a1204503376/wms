package org.nodes.wms.biz.stock.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.dao.stock.SerialDao;
import org.nodes.wms.dao.stock.SerialLogDao;
import org.nodes.wms.dao.stock.dto.input.SerialLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.SerialLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.SerialLogPageResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SerialBizImpl implements SerialBiz {
	private final SerialDao serialDao;

	private final SerialLogDao serialLogDao;

	@Override
	public List<String> findSerialNoByStockId(Long stockId) {
		return serialDao.getSerialNoByStockId(stockId);
	}

	@Override
	public Page<SerialLogPageResponse> pageLog(Query query, SerialLogPageQuery serialLogPageQuery) {
		return serialLogDao.page(Condition.getPage(query), serialLogPageQuery);
	}

	@Override
	public void exportLog(SerialLogPageQuery serialLogPageQuery, HttpServletResponse response) {
		List<SerialLogExcelResponse> serialLogList = serialLogDao.listByQuery(serialLogPageQuery);
		ExcelUtil.export(response, "", "", serialLogList, SerialLogExcelResponse.class);
	}
}