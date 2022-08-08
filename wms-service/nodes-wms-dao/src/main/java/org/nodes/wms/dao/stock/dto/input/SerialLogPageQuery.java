package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 序列号分页查询请求类
 **/
@Data
public class SerialLogPageQuery implements Serializable {

	private static final long serialVersionUID = 1168855968280657456L;

	/**
	 * 序列号-开始
	 */
	private String serialNumberBegin;

	/**
	 * 序列号-结束
	 */
	private String serialNumberEnd;

	/**
	 * 批次
	 */
	private String lotNumber;

	/**
	 * 入库日期-开始
	 */
	private Date createTimeBegin;

	/**
	 * 入库日期-开始
	 */
	private Date createTimeEnd;

	/**
	 * 序列号状态
	 */
	private List<Integer> serialStateList;
}
