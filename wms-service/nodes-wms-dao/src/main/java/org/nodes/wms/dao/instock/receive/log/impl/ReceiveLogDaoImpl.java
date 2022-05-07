package org.nodes.wms.dao.instock.receive.log.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.receive.log.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receive.log.entities.ReceiveLog;
import org.nodes.wms.dao.instock.receive.log.mapper.ReceiveLogMapper;
import org.nodes.wms.dao.instock.receive.log.ReceiveLogDao;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 清点记录 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class ReceiveLogDaoImpl extends BaseServiceImpl<ReceiveLogMapper, ReceiveLog> implements ReceiveLogDao {
	private final ReceiveLogMapper receiveLogMapper;
	@Override
	public List<ReceiveLogResponse> selectByDetailIds(List<Long> detailIds) {
		return receiveLogMapper.selectByDetailIds(detailIds);
	}
}
