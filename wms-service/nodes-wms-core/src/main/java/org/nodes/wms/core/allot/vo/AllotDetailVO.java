
package org.nodes.wms.core.allot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.allot.entity.AllotDetail;

/**
 * 调拨明细表视图实体类
 *
 * @author Wangjw
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AllotDetailVO对象", description = "调拨明细表")
public class AllotDetailVO extends AllotDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 层级名称
	 */
	private String skuLevelName;

	/**
	 * 计划数量
	 */
	@ApiModelProperty("计划调拨数量（带单位）")
	private String allotPlanQtyName;

}
