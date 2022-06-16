
package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.dao.basics.sku.entities.SkuReplace;

import java.util.List;

/**
 * 物品数据传输对象实体类
 *
 * @author pengwei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuDTO extends Sku {
	private static final long serialVersionUID = 1L;

	/**
	 * 物品替代集合
	 */
	@ApiModelProperty("物品替代集合")
	private List<SkuReplace> skuReplaceList;

	/**
	 * 删除的物品替代集合
	 */
	@ApiModelProperty("删除的物品替代集合")
	private List<SkuReplace> skuReplaceDeletedList;

	/**
	 * 物品供应商关联集合
	 */
	@ApiModelProperty("物品供应商关联集合")
	private List<SkuInc> skuIncList;

	/**
	 * 删除的物品供应商关联集合
	 */
	@ApiModelProperty("删除的物品供应商关联集合")
	private List<SkuInc> skuIncDeletedList;
	/**
	 *
	 */
	@ApiModelProperty(value = "货主编码")
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;
	/**
	 * 物品分类编码
	 */
	@ApiModelProperty(value = "物品分类编码")
	private String typeCode;
	/**
	 * 物品分类名称
	 */
	@ApiModelProperty(value = "物品分类名称")
	private String typeName;
	/**
	 * 包装名称
	 */
	@ApiModelProperty(value = "包装名称")
	private String wspName;
	/**
	 * 批属性编码
	 */
	@ApiModelProperty(value = "批属性编码")
	private String skuLotCode;
	/**
	 * 批属性名称
	 */
	@ApiModelProperty(value = "批属性名称")
	private String skuLotName;
	/**
	 * 批属性验证名称
	 */
	@ApiModelProperty(value = "批属性验证名称")
	private String skuLotValName;
	/**
	 * 库存ids
	 */
	@ApiModelProperty(value = "库存ids")
	private String stockIds;
}
