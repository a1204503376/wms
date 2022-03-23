package org.nodes.wms.core.stock.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.ErpStock;
import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description erp库存
 * @create 20200331
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ErpStockVO对象", description = "ERP库存")
public class ErpStockVO extends ErpStock {

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;
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
	 * 包装包称
	 */
	@ApiModelProperty("包装包称")
	private String wspName;
	/**
	 * 包装层级描述
	 */
	@ApiModelProperty("包装层级描述")
	private String skuLevelDesc;
	/**
	 * wms库存数量
	 */
	@ApiModelProperty("wms库存数量")
	private BigDecimal wmsStockQty = BigDecimal.ZERO;
	/**
	 * 差异数量
	 */
	@ApiModelProperty("差异数量")
	private BigDecimal diffQty;
}
