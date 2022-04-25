package org.nodes.wms.dao.common.log.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.common.log.LogDao;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogMessage;
import org.nodes.wms.dao.common.log.mapper.LogActionMapper;
import org.nodes.wms.dao.common.log.mapper.LogMessageMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 日志Service
 * @author 王智勇
 */
@Repository
@Service
@RequiredArgsConstructor
public class LogDaoImpl extends BaseServiceImpl<LogMessageMapper, LogMessage> implements LogDao {

	private final LogActionMapper logActionMapper;

	/**
	 * 添加LogMessage日志
	 * @param messageCollection LogMessage
	 * @return 是否成功
	 */
	@Override
	public Boolean addLogMessage(Collection<LogMessage> messageCollection) {
		return super.saveBatch(messageCollection);
	}

	/**
	 * 添加审计日志添加对象
	 * @param logAction 日志审批对象
	 * @return 是否成功
	 */
	@Override
	public Boolean addLogAction(LogAction logAction) {
		return logActionMapper.insert(logAction)>0;
	}
}
