package org.nodes.wms.dao.basics.sku.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 计量单位下拉选择请求参数对象
 **/
@Data
public class SkuUmSelectQuery implements Serializable {

	private static final long serialVersionUID = -4634805672625900444L;

	/**
	 * 物品id
	 */
	@NotNull(message = "物品id不能为空")
	private Long skuId;
}
