package org.nodes.wms.biz.instock.receiveLog.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.dao.instock.receiveLog.ReceiveLogDao;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.springframework.stereotype.Service;

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
}
