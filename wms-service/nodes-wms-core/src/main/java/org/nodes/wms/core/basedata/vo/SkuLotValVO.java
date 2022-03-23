
package org.nodes.wms.core.basedata.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.basedata.entity.SkuLotVal;

/**
 * 批属性验证视图实体类
 *
 * @author chenhz
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuLotValVO对象", description = "批属性验证")
public class SkuLotValVO extends SkuLotVal {
	private static final long serialVersionUID = 1L;

	/**
	 * 货主名称
	 */
	private String ownerName;

	/**
	 * 库房名称
	 */
	private String whName;
}
