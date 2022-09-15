package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出库单编辑明细响应类
 **/
@Data
public class SoDetailEditResponse implements Serializable {

	private static final long serialVersionUID = -2262857697950582572L;

	/**
	 * 发货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;

	/**
	 * 行号
	 */
	private String soLineNo;

	/**
	 * 物品id
	 */
	@NotNull(message = "物品不能为空")
	private SkuSelectResponse sku;

	/**
	 * 计量单位编码
	 */
	private String umCode;

	/**
	 * 物品规格
	 */
	private String skuSpec;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 生产批次
	 */
	private String skuLot1;

	/**
	 * 专用客户
	 */
	private String skuLot4;

	/**
	 * 备注
	 */
	private String remark;
}
