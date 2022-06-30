package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 未收货明细分页请求dto类
 **/
@Data
public class NotReceiveDetailPageQuery implements Serializable {

	private static final long serialVersionUID = 2441383923151178232L;

	/**
	 * 收货单编码
	 */
	private String receiveNo;

	/**
	 * 单据类型
	 */
	private String billType;

	/**
	 * 上游编码
	 */
	private String externalOrderNo;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 创建时间-开始
	 */
	private Date createTimeBegin;

	/**
	 * 创建时间-结束
	 */
	private Date createTimeEnd;
}