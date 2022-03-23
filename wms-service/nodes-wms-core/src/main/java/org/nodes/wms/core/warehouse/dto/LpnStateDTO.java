package org.nodes.wms.core.warehouse.dto;

import org.nodes.wms.core.warehouse.entity.LpnState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 容器状态表数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LpnStateDTO extends LpnState {
	private static final long serialVersionUID = 1L;

}
