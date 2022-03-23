
package org.nodes.wms.core.allot.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.allot.entity.AllotDetail;

/**
 * 调拨明细表数据传输对象实体类
 *
 * @author Wangjw
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AllotDetailDTO extends AllotDetail {
	private static final long serialVersionUID = 1L;

}
