
package org.nodes.wms.core.basedata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.basedata.entity.SkuReplace;

/**
 * 物品替代视图实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuReplaceVO对象", description = "物品替代")
public class SkuReplaceVO extends SkuReplace {
	private static final long serialVersionUID = 1L;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;

	/**
	 * 包装单位名称
	 */
	@ApiModelProperty("包装单位名称")
	private String wsuName;

	/**
	 * 替代物品名称
	 */
	@ApiModelProperty("替代物品名称")
	private String wsrepSkuName;

	/**
	 * 替代包装名称
	 */
	@ApiModelProperty("替代包装名称")
	private String wsrepWspName;

	/**
	 * 替代包装单位名称
	 */
	@ApiModelProperty("替代包装单位名称")
	private String wsrepWsuName;
}
