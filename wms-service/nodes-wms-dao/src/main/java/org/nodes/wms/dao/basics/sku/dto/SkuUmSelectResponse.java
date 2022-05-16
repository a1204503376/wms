package org.nodes.wms.dao.basics.sku.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 计量单位下拉选择返回对象
 **/
@Data
public class SkuUmSelectResponse implements Serializable {

	private static final long serialVersionUID = 8270657834460095504L;

	/**
	 * 计量单位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsuId;


	/**
	 * 计量单位编码
	 */
	private String wsuCode;

	/**
	 * 计量单位名称
	 */
	private String wsuName;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 物品分类id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuTypeId;
}
