
package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.InstockSkuDetail;

/**
 * 上架策略物品明细 视图实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InstockSkuDetailVO对象", description = "InstockSkuDetailVO对象")
public class InstockSkuDetailVO extends InstockSkuDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 物品ABC分类描述
	 */
	@ApiModelProperty("物品ABC分类描述")
	private String abcDesc;
	/**
	 * 物品存放类型
	 */
	@ApiModelProperty("物品存放类型")
	private String inventoryTypeDesc;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;
	/**
	 * 库位类型描述
	 */
	@ApiModelProperty("库位类型描述")
	private String locTypeDesc;
	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 层级
	 */
	@ApiModelProperty("层级")
	private String skuLevelDesc;
}
