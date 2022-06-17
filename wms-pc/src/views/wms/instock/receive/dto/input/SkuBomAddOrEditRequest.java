package org.nodes.wms.dao.basics.bom.dto.input;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新增或修改物品清单
 **/
@Data
public class SkuBomAddOrEditRequest implements Serializable {

	private static final long serialVersionUID = -6259087345235468355L;

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 物品id
	 */
	private Long skuId;

	/**
	 * 主单位编码
	 */
	private String wsuCode;

	/**
	 * 组合物品id
	 */
	private Long joinSkuId;

	/**
	 * 组合数量
	 */
	private BigDecimal qty;

	/**
	 * 组合单位编码
	 */
	private String joinWsuCode;

	/**
	 * 备注
	 */
	private String remark;
}
