
package org.nodes.wms.core.stock.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.Lot;

/**
 * 批次号数据传输对象实体类
 *
 * @author pengwei
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LotDTO extends Lot {
	private static final long serialVersionUID = 1L;

}
