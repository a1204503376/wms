package org.nodes.wms.core.stock.transfer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.transfer.entity.TransferDetail;

/**
 * 库内移动明细视图实体类
 *
 * @author pengwei
 * @since 2020-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TransferDetailVO对象", description = "库内移动明细")
public class TransferDetailVO extends TransferDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;

	/**
	 * 原库位编码
	 */
	@ApiModelProperty("原库位编码")
	private String sourceLocCode;

	/**
	 * 目标库位编码
	 */
	@ApiModelProperty("目标库位编码")
	private String targetLocCode;
}
