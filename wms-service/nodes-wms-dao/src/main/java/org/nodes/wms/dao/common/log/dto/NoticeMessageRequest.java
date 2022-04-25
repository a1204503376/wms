package org.nodes.wms.dao.common.log.dto;

import lombok.Data;
import org.springblade.core.secure.BladeUser;

import java.util.Date;
import java.util.List;

/**
 * 发送通知类型的通知
 */
@Data
public class NoticeMessageRequest {
	/**
	 * 通知内容
	 */
	private String log;

	/**
	 * 通知失效时间
	 */
	private Date expirationDate;

	/**
	 * 需要通知的用户列表
	 */
	private List<BladeUser> noticeUserList;
}
