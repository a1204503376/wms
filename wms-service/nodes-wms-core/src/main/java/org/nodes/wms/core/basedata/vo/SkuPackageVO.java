
package org.nodes.wms.core.basedata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;

import java.util.List;

/**
 * 包装ID视图实体类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuPackageVO对象", description = "包装ID")
public class SkuPackageVO extends SkuPackage {
	private static final long serialVersionUID = 1L;

	/**
	 * 包装明细集合
	 */
	@ApiModelProperty("包装明细集合")
	private List<SkuPackageDetailVO> skuPackageDetailVOList;
}
