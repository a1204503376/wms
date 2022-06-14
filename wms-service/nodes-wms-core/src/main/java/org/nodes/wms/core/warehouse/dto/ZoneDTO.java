
package org.nodes.wms.core.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.zone.entities.Zone;

/**
 * 库区管理 数据传输对象实体类
 *
 * @author zhongls
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ZoneDTO extends Zone {
	private static final long serialVersionUID = 1L;

}
