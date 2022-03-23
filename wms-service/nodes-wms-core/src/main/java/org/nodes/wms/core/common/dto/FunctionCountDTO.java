package org.nodes.wms.core.common.dto;

import org.nodes.wms.core.common.entity.FunctionCount;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能计数	数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionCountDTO extends FunctionCount {
	private static final long serialVersionUID = 1L;

}
