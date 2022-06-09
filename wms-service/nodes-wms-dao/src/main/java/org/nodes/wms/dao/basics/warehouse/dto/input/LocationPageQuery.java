package org.nodes.wms.dao.basics.warehouse.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 库区查询条件dto
 **/
@Data
public class LocationPageQuery implements Serializable {

	private static final long serialVersionUID = -6819803594398735910L;
	// TODO 库位查找功能 暂时不清楚有哪些查询条件

	/**
	 * 库位编码
	 */
	private String lcoCode;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 库区id
	 */
	private Long zoneId;

	/**
	 * 库位类型
	 */
	private Integer locType;

	/**
	 * 库位种类
	 */
	private Integer locCategory;

	/**
	 * 库位处理
	 */
	private String checkDigit;

	/**
	 * 线路顺序
	 */
	private Integer logicAllocation;

	/**
	 * 使用状态
	 */
	private Integer locStatus;

	/**
	 * abc分类
	 */
	private Integer abc;

	/**
	 * 货架列
	 */
	private String locColumn;

	/**
	 * 货架排
	 */
	private String locBank;

	/**
	 * 上架顺序
	 */
	private Integer putOrder;

	/**
	 * 容器类型id
	 */
	private Long lpnTypeId;
}
