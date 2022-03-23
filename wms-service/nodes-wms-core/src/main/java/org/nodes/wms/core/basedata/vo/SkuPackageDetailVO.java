
package org.nodes.wms.core.basedata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuPackageDetailVO对象", description = "SkuPackageDetailVO对象")
public class SkuPackageDetailVO extends SkuPackageDetail {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("包装ID拓展")
	private String wspIdExt;

	@ApiModelProperty("计量单位ID拓展")
	private String wsuIdExt;

	@ApiModelProperty("层级名称")
	private String skuLevelName;
}
