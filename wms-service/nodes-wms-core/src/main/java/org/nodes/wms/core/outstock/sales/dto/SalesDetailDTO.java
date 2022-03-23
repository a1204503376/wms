package org.nodes.wms.core.outstock.sales.dto;

import org.nodes.wms.core.outstock.sales.entity.SalesDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发货单明细
数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesDetailDTO extends SalesDetail {
	private static final long serialVersionUID = 1L;

}
