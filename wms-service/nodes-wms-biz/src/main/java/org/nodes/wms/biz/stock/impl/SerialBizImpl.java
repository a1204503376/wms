package org.nodes.wms.biz.stock.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.dao.stock.SerialDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SerialBizImpl implements SerialBiz {
	private final SerialDao serialDao;
	@Override
	public List<String> findSerialNoByStockId(Long stockId) {
		return serialDao.getSerialNoByStockId(stockId);
	}
}
