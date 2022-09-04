
package org.nodes.wms.core.strategy.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.putaway.entities.StInstockConfig;

/**
 * 上架策略执行条件数据传输对象实体类
 *
 * @author chz
 * @since 2020-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InstockConfigDTO extends StInstockConfig {
	private static final long serialVersionUID = 1L;

}
