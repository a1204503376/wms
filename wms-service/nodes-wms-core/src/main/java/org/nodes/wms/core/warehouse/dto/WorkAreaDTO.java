
package org.nodes.wms.core.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.WorkArea;

/**
 * 工作区数据传输对象实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkAreaDTO extends WorkArea {
	private static final long serialVersionUID = 1L;

}
