package org.nodes.wms.dao.receive.log.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.receive.header.mapper.HeaderMapper;
import org.nodes.wms.dao.receive.log.LogDao;
import org.nodes.wms.dao.receive.log.dto.output.LogResponse;
import org.nodes.wms.dao.receive.log.entities.ReceiveLog;
import org.nodes.wms.dao.receive.log.mapper.LogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 清点记录 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class LogDaoImpl extends BaseServiceImpl<LogMapper, ReceiveLog> implements LogDao {
	private final LogMapper logMapper;
	@Override
	public List<LogResponse> selectByDetailIds(List<Long> detailIds) {
		return logMapper.selectByDetailIds(detailIds);
	}
}
