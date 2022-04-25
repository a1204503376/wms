package org.nodes.wms.dao.basics.carriers.dto.input;

import lombok.Data;

import java.util.Date;

/**
 * 承运商表 分页参数
 **/
@Data
public class CarriersPageQuery {
	/**
	 * 承运商编码
	 */
	private String code;
	/**
	 * 承运商名称
	 */
	private String name;
	/**
	 * 承运商简称
	 */
	private String  simpleName;
	/**
	 * 创建时间开始
	 */
	private Date createTimeBegin;
	/**
	 * 创建时间结束
	 */
	private Date createTimeEnd;
	/**
	 * 更新时间开始
	 */
	private Date  updateTimeBegin;
	/**
	 * 更新时间结束
	 */
	private Date  updateTimeEnd;
}
