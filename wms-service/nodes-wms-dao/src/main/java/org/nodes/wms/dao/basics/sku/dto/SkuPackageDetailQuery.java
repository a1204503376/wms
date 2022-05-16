package org.nodes.wms.dao.basics.sku.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 物品包装明细插叙参数对象
 **/
@Data
public class SkuPackageDetailQuery implements Serializable {

	private static final long serialVersionUID = -7647369972981498603L;

	/**
	 * 物品id
	 */
	@NotNull(message = "物品id不能为空")
	private Long skuId;
}
