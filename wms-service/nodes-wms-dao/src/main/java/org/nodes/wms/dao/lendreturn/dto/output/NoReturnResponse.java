package org.nodes.wms.dao.lendreturn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.dto.SkuLotDto;

import java.math.BigDecimal;

/**
 *未归还
 */
@Data
public class NoReturnResponse extends SkuLotDto {

	/**
	 * 借出归还人姓名
	 */
	private String lendReturnName;

	/**
	 * 物品id
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
	 * 借出数量
	 */
	private BigDecimal lendQty;

	/**
	 * 归还数量
	 */
	private BigDecimal returnQty;

	/**
	 * 未归还的数量
	 */
	@JsonSerialize
	public BigDecimal getNoReturnQty() {
		return lendQty.subtract(returnQty);
	}

	/**
	 * 计量单位编码
	 */
	private String wsuCode;

	/**
	 * 计量单位名称
	 */
	private String wsuName;

	/**
	 * 序列号
	 */
	private String snCode;

	/**
	 * 单据编码
	 */
	private String billNo;
}
