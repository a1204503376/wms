package org.nodes.wms.dao.basics.sku.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 物品包装明细返回对象
 **/
@Data
public class SkuPackageDetailResponse implements Serializable {


	private static final long serialVersionUID = -5902133314113415500L;

	/**
	 * 包装明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	/**
	 * 包装id
	 */
	private Long wspId;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 计量单位id
	 */
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
	 * 换算倍数
	 */
	private Integer convertQty;

}
