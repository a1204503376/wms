package org.nodes.wms.dao.common.log.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.common.log.LogMessageDao;
import org.nodes.wms.dao.common.log.dto.LogMessageResponse;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogMessage;
import org.nodes.wms.dao.common.log.mapper.LogActionMapper;
import org.nodes.wms.dao.common.log.mapper.LogMessageMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 日志Service
 * @author 王智勇
 */
@Repository
@Service
@RequiredArgsConstructor
public class LogMessageDaoImpl extends BaseServiceImpl<LogMessageMapper, LogMessage> implements LogMessageDao {
	/**
	 * 添加LogMessage日志
	 * @param messageCollection LogMessage
	 * @return 是否成功
	 */
	@Override
	public Boolean insertLogMessage(Collection<LogMessage> messageCollection) {
		return super.saveBatch(messageCollection);
	}

    @Override
    public Integer getLogMsgCount() {
		return super.count(new LambdaQueryWrapper<LogMessage>().eq(LogMessage::getReaded,0)
			.ge(LogMessage::getExpirationDate,DateUtil.formatDate(new Date()))
		);
    }

	@Override
	public List<LogMessageResponse> getLogMsgList(Long num, String date) {
		return super.baseMapper.getLogMsgList(num,date);
	}


	@Override
	public void updateLogMsgReaded(Long num, Long id) {
		num = num >0? 0L : 1L;
		super.update(new LambdaUpdateWrapper<LogMessage>()
			.set(LogMessage::getReaded,num)
			.eq(LogMessage::getId,id));
	}

}
