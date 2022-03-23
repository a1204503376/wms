package org.nodes.wms.core.instock.purchase.dto;

import org.nodes.wms.core.instock.purchase.entity.PoDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货单明细表数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PoDetailDTO extends PoDetail {
	private static final long serialVersionUID = 1L;


}
