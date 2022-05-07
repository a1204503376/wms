package org.nodes.wms.dao.instock.receive.log.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 清点记录 返回前端视图类
 **/
@Data
public class ReceiveLogResponse implements Serializable {
	private static final long serialVersionUID = 5496764886677759763L;
	/**
	 *  清点表 ID
	 */
	private Long id;
	/**
	 * 订单行号
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
	 * 数量
	 */
	private BigDecimal qty;
	/**
	 * 计量单位名称
	 */
	private String um_name;
}
