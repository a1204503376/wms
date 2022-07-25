package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分配页面-拣货计划信息响应对象
 **/
@Data
public class SoPickPlanForDistResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -845376913346559673L;

	/**
	 * 拣货计划id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickPlanId;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 分配量
	 */
	private BigDecimal pickPlanQty;

	/**
	 * 库区id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;

	/**
	 * 库区名称
	 */
	private String zoneName;

	/**
	 * 库位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位名称
	 */
	private String locName;

	/**
	 * lpn
	 */
	private String lpnCode;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 批次
	 */
	private String lotNumber;

	/**
	 * 可用量
	 */
	private BigDecimal enableQty;

	/**
	 * 余额
	 */
	private BigDecimal surplusQty;

	/**
	 * 状态
	 */
	private SoBillStateEnum state;
}
