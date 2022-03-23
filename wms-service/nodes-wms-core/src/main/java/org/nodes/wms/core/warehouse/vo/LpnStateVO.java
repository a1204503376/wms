package org.nodes.wms.core.warehouse.vo;

import org.nodes.wms.core.warehouse.entity.LpnState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 容器状态表视图实体类
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LpnStateVO对象", description = "容器状态表")
public class LpnStateVO extends LpnState {
	private static final long serialVersionUID = 1L;

}
