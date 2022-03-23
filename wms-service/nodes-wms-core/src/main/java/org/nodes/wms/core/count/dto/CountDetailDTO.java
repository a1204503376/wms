
package org.nodes.wms.core.count.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.count.entity.CountDetail;

/**
 * 盘点单明细表数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CountDetailDTO extends CountDetail {
	private static final long serialVersionUID = 1L;

}
