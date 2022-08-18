package org.nodes.wms.dao.count.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * PDA显示箱号和箱子内物品总数量的返回对象
 */
@Data
public class PdaBoxQtyResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = 2040077459648458152L;
	/**
	 * 库存ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 库存量
	 */
	private BigDecimal stockQty;

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 计量单位编码
	 */
	private String wsuCode;

	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 包装ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 箱内物品总数量
	 */
	private BigDecimal totalQty;

	/**
	 * 传输给前端做是否无误的判断
	 */
	private Boolean isValid = false;

	/**
	 * 传输给前端按钮默认显示
	 */
	private Boolean isButton = true;
}
