
package org.nodes.wms.core.basedata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.basedata.entity.SkuInc;

/**
 * 物品供应商关联视图实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuIncVO对象", description = "物品供应商关联")
public class SkuIncVO extends SkuInc {
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商名称
	 */
	@ApiModelProperty("供应商名称")
	private String enterpriseName;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
}
