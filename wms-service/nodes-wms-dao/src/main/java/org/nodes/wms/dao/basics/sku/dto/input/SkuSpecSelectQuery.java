package org.nodes.wms.dao.basics.sku.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 物品规格下拉组件数据请求类
 **/
@Data
public class SkuSpecSelectQuery implements Serializable {

	private static final long serialVersionUID = -4634805672625900444L;

	/**
	 * 物品id
	 */
	@NotNull(message = "物品id不能为空")
	private Long skuId;
}
