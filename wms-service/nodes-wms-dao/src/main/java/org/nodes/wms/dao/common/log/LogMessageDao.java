package org.nodes.wms.dao.common.log;

import org.nodes.wms.dao.common.log.dto.output.LogMessageResponse;
import org.nodes.wms.dao.common.log.entities.LogMessage;

import java.util.Collection;
import java.util.List;

/**
 * 通知日志dao层
 * @author 王智勇
 */
public interface LogMessageDao {


	/**
	 * 添加LogMessage日志
	 * @param messageCollection LogMessage
	 * @return 是否成功
	 */
    Boolean insertLogMessage(Collection<LogMessage> messageCollection);

	/**
	 * 获取消息总条数
	 * @return
	 */
	Integer getLogMsgCount();

	/**
	 * 根据消息已读状态获取消息集合
	 * @param num 消息已读状态  0 未读   1 已读
	 */
	List<LogMessageResponse> getLogMsgList(Long num, String date);

	/**
	 * 根据已读状态和id修改已读状态
	 * @param num 已读状态
	 * @param id   消息id
	 */
	void updateLogMsgReaded(Long num,Long id);


}
