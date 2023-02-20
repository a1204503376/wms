package org.nodes.wms.dao.instock.receiveLog.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReceiveLogResponse implements Serializable {
	private static final long serialVersionUID = -608565512818187913L;
	/**
	 * 订单行号
	 */
	private String lineNo;
	/**
	 * 数量
	 */
	private BigDecimal qty;
	/**
	 * lpn编码
	 */
	private String lpnCode;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 序列号
	 */
	private String snCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 物品名称
	 */
	private String skuName;
	/**
	 * 包装层级
	 */
	private Integer skuLevel;
	/**
	 * 规格
	 */
	private String skuSpec;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 型号
	 */
	private String skuLot2;
	/**
	 * 货主编码
	 */
	private String ownerCode;
	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
