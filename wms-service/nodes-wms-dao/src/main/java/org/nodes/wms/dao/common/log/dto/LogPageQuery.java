package org.nodes.wms.dao.common.log.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LogPageQuery {
	/**
	 * ID
	 */
	private Long billId;

	/**
	 * 操作人员账号
	 */
	private String userAccount;
	/**
	 * 任务名称
	 */
	private String crontabTaskName;
	/**
	 * 创建时间开始
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date createTimeBegin;
	/**
	 * 创建时间结束
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date createTimeEnd;
}
