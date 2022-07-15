package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 发货单查看明细头表信息响应类
 **/
@Data
public class LogForSoDetailResponse implements Serializable {

	private static final long serialVersionUID = -5860395304662466540L;

	/**
	 * 操作人员账号
	 */
	private String userAccount;

	/**
	 * 操作人员真实姓名
	 */
	private String userRealName;

	/**
	 * 操作类型
	 */
	private Integer type;

	/**
	 * 操作内容
	 */
	private String log;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
