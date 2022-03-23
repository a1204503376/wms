package org.nodes.wms.core.common.vo;

import org.nodes.wms.core.common.entity.FunctionCount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 功能计数	视图实体类
 *
 * @author NodeX
 * @since 2021-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FunctionCountVO对象", description = "功能计数	")
public class FunctionCountVO extends FunctionCount {
	private static final long serialVersionUID = 1L;

}
