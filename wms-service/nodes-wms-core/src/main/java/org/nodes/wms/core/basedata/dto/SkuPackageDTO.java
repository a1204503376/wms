
package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;

import java.util.List;

/**
 * 包装ID数据传输对象实体类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuPackageDTO extends SkuPackage {
	private static final long serialVersionUID = 1L;

	/**
	 * 包装明细集合
	 */
	@ApiModelProperty("包装明细集合")
	private List<SkuPackageDetailDTO> skuPackageDetailDTOList;

	@ApiModelProperty("删除集合")
	private List<SkuPackageDetailDTO> pageDeletedList;

	/**
	 * 包装ID列表
	 */
	@ApiModelProperty("包装ID列表")
	private String wspIds;
}
