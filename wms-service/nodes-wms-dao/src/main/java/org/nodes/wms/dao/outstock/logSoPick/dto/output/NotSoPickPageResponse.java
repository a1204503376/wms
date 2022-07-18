package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 未发货记录响应类
 **/
@Data
public class NotSoPickPageResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -2918535262772599847L;

	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 单据类型
	 */
	private String billTypeName;

	/**
	 * 上游编码
	 */
	private String orderNo;

	/**
	 * 行号
	 */
	private String soLineNo;

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
	 * 创建人
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
