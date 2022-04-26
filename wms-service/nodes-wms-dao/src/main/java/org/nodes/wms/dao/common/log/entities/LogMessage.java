package org.nodes.wms.dao.common.log.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.util.Date;

/**
 * 通知日志类
 * @author 王智勇
 */
@Data
@TableName("log_message")
public class LogMessage extends TenantEntity {

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 需要通知的用户id，为空则是通知所有人
	 */
	private Long userId;

	/**
	 * 通知内容
	 */
	private String log;

	/**
	 * 已读(0:未读,1:已读)
	 */
	private Integer readed;

	/**
	 * 失效日期，只有通知所有人时不为空
	 */
	private Date expirationDate;


}
