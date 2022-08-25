package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 库存日志分页请求dto类
 **/
@Data
public class StockLogPageQuery implements Serializable {

	private static final long serialVersionUID = -4357221036269401231L;

	/**
	 * 物品id
	 */
	private List<Long> skuIdList;

	/**
	 * 来源的单据编码
	 */
	private String sourceBillNo;

	/**
	 * 库存日志类型
	 */
	private List<String> logTypeList;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	private Date createTimeBegin;

	/**
	 * 创建时间
	 */
	private Date createTimeEnd;

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * 库位id集合
	 */
	private List<Long> locIdList;

	/**
	 * 库区id集合
	 */
	private List<Long> zoneIdList;

	/**
	 * 生产批次
	 */
	private String skuLot1;

	/**
	 * 库存id，可以为空
	 */
	private Long stockId;
}
