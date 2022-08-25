package org.nodes.wms.dao.outstock.so.dto.input;

import lombok.Data;
import org.nodes.core.tool.validation.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 编辑-发货单明细新增或编辑请求类
 **/
@Data
public class SoDetailAddOrEditRequest implements Serializable {

	private static final long serialVersionUID = -7991304045150802122L;

	/**
	 * 发货单明细id
	 */
	@NotNull(message = "发货单明细id不能为空", groups = Update.class)
	private Long soDetailId;

	/**
	 * 行号
	 */
	private String soLineNo;

	/**
	 * 物品id
	 */
	@NotNull(message = "物品不能为空")
	private Long skuId;

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
