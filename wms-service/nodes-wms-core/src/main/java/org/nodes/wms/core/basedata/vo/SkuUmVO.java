
package org.nodes.wms.core.basedata.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.basedata.entity.SkuUm;

/**
 * 计量单位 视图实体类
 *
 * @author zhongls
 * @since 2019-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuUmVO对象", description = "SkuUmVO对象")
public class SkuUmVO extends SkuUm {
	private static final long serialVersionUID = 1L;

}
