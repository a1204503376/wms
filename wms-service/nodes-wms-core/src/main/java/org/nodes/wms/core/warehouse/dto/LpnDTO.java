
package org.nodes.wms.core.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.Lpn;

/**
 * 容器数据传输对象实体类
 *
 * @author liangmei
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LpnDTO extends Lpn {
	private static final long serialVersionUID = 1L;

}
