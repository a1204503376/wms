
package org.nodes.wms.core.relenishment.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.relenishment.entity.RelDetail;

/**
 * 补货表头数据传输对象实体类
 *
 * @author whj
 * @since 2019-12-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RelDetailDTO extends RelDetail {
	private static final long serialVersionUID = 1L;


}
