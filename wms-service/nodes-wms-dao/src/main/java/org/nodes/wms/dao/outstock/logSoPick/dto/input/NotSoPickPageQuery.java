package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 未发货分页请求dto类
 **/
@Data
public class NotSoPickPageQuery implements Serializable {

	private static final long serialVersionUID = -9216042978283508798L;

	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 单据类型
	 */
	private List<String> billTypeCdList;

	/**
	 * 上游编码
	 */
	private String orderNo;

	/**
	 * 物品编码
	 */
	private List<Long> skuIdList;

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
