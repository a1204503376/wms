package org.nodes.wms.dao.instock.receiveLog.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 收货清点记录请求dto类
 **/
@Data
public class ReceiveLogPageRequest implements Serializable {

	private static final long serialVersionUID = -3141574572154946992L;

	/**
	 * 收货单编码
	 */
	private String receiveNo;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * LPN
	 */
	private String lpnCode;

	/**
	 * 序列号
	 */
	private String snCode;

	/**
	 * 收货人
	 */
	private Long createUser;

	/**
	 * 库位id
	 */
	private Long locId;

	/**
	 * 库房id
	 */
	private List<Long> whIdList;

	/**
	 * 货主id
	 */
	private Long ownerId;

	/**
	 * 收货时间-开始
	 */
	private Date createTimeDateBegin;

	/**
	 * 收货时间-结束
	 */
	private Date createTimeDateEnd;
}
