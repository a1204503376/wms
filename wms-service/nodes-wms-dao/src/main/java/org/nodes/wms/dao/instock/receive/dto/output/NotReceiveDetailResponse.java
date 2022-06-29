package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 未收货明细响应类
 **/
@Data
public class NotReceiveDetailResponse implements Serializable {

	private static final long serialVersionUID = 6752313650369510531L;

	/**
	 * 收货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveId;

	/**
	 * 收货单编码
	 */
	private String receiveNo;

	/**
	 * 单据类型
	 */
	private String billTypeName;

	/**
	 * 上游编码
	 */
	private String externalOrderNo;

	/**
	 * 行号
	 */
	private String lineNo;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 实收数量
	 */
	private BigDecimal scanQty;

	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;

	/**
	 * 计量单位
	 */
	private String umName;

	/**
	 * 生产批次
	 */
	private String skuLot1;

	/**
	 * 规格型号
	 */
	private String skuLot2;

	/**
	 * 收货日期
	 */
	private String skuLot3;

	/**
	 * 专用客户
	 */
	private String skuLot4;

	/**
	 * 钢背批次
	 */
	private String skuLot5;

	/**
	 * 摩擦块批次
	 */
	private String skuLot6;

	/**
	 * 产品标识代码
	 */
	private String skuLot7;

	/**
	 * 是否CRCC验证
	 */
	private String skuLot8;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
