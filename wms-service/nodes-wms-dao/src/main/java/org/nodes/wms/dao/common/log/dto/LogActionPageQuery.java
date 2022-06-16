package org.nodes.wms.dao.common.log.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 业务日志查询条件
 */
@Data
public class LogActionPageQuery implements Serializable {
	private static final long serialVersionUID = -3026767384177930684L;
	/**
	 * 操作人员账号
	 */
	private String userAccount;
	/**
	 * 操作人员真实名称
	 */
	private String userRealName;
	/**
	 * 操作类型
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private List<Integer> type;
	/**
	 * 目标单据id,可能为空
	 */
	private Long billId;
	/**
	 * 目标单据编码,可能为空
	 */
	private String billNo;
	/**
	 * 操作内容
	 */
	private String log;
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
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 更新时间开始
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date updateTimeBegin;
	/**
	 * 更新时间结束
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date updateTimeEnd;
	/**
	 * 更新人
	 */
	private String updateUser;
}
