package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.Sn;

/**
 * 入库货品序列号视图实体类
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SnVO对象", description = "入库货品序列号")
public class SnVO extends Sn {
	private static final long serialVersionUID = 1L;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 行号
	 */
	private String asnLineNo;

	/**
	 * 入库单编号
	 */
	private String asnBillNo;

}
