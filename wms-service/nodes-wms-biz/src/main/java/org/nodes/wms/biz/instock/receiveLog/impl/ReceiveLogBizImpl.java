package org.nodes.wms.biz.instock.receiveLog.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.dao.instock.receiveLog.ReceiveLogDao;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogExcelResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 清点记录业务层实现类
 */
@Service
@RequiredArgsConstructor
public class ReceiveLogBizImpl implements ReceiveLogBiz {
	private final ReceiveLogDao receiveLogDao;

	@Override
	public List<ReceiveLogResponse> getReceiveLogList(Long receiveId) {
		return receiveLogDao.getReceiveLogList(receiveId);
	}

	@Override
	public List<ReceiveLogIndexResponse> findReceiveSkuQtyTop10() {
		return receiveLogDao.getReceiveSkuQtyTop10();
	}

	@Override
	public ReceiveLog newReceiveLog(ReceiveLog receiveLog) {
		return null;
	}

	@Override
	public Page<ReceiveLogPageResponse> page(Query query, ReceiveLogPageQuery receiveLogPageQuery) {
		return receiveLogDao.page(Condition.getPage(query), receiveLogPageQuery);
	}

	@Override
	public void exportExcel(ReceiveLogPageQuery receiveLogPageQuery, HttpServletResponse response) {
		List<ReceiveLogExcelResponse> receiveLogList
			= receiveLogDao.getReceiveLogListByQuery(receiveLogPageQuery);
		ExcelUtil.export(response, "收货记录", "收货记录", receiveLogList, ReceiveLogExcelResponse.class);
	}
}
