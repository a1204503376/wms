package org.nodes.wms.dao.basics.skuType.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新增或修改dto
 **/
@Data
public class SkuTypeAddOrEditRequest implements Serializable {

	private static final long serialVersionUID = -4934346443921178945L;

	/**
	 * 物品分类id
	 */
	private Long skuTypeId;

	/**
	 * 分类编码
	 */
	@NotNull(message = "分类编码不能为空")
	private String typeCode;

	/**
	 * 分类名称
	 */
	@NotNull(message = "分类名称不能为空")
	private String typeName;

	/**
	 * 货主
	 */
	@NotNull(message = "货主不能为空")
	private Long woId;

	/**
	 * 上位物品分类id
	 */
	private Long typePreId;

	/**
	 * 上位物品分类编码
	 */
	private String typePreCode;

	/**
	 * 绩效系数
	 */
	private BigDecimal gradeNum;

	/**
	 * 物品分类路径
	 */
	private String typePath;

	/**
	 * 物品分类备注
	 */
	private String typeRemark;
}
