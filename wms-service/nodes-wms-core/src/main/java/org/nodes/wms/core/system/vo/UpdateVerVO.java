
package org.nodes.wms.core.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.UpdateVer;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2020-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UpdateVerVO对象", description = "UpdateVerVO对象")
public class UpdateVerVO extends UpdateVer {
	private static final long serialVersionUID = 1L;

}
