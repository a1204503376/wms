package org.nodes.wms.biz.outstock.logSoPick.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.dot.output.LogSoPickIndexResponse;
import org.springframework.stereotype.Service;

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
}
