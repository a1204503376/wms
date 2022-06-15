
package org.nodes.wms.core.basedata.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.sku.entities.Sku;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物品视图实体类
 *
 * @author pengwei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuVO对象", description = "物品")
public class SkuVO extends Sku {
	private static final long serialVersionUID = 1L;

	/**
	 * 包装信息
	 */
	@ApiModelProperty("包装信息")
	private SkuPackageVO skuPackage;
	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String spec;

	/**
	 * 物品替代集合
	 */
	@ApiModelProperty("物品替代集合")
	private List<SkuReplaceVO> skuReplaceList;

	/**
	 * 物品供应商关联集合
	 */
	@ApiModelProperty("物品供应商关联集合")
	private List<SkuIncVO> skuIncList;

	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 物品分类编码
	 */
	@ApiModelProperty("物品分类编码")
	private String typeCode;

	/**
	 * 物品分类名称
	 */
	@ApiModelProperty("物品分类名称")
	private String typeName;

	/**
	 * 上位物品分类ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	public Long parentId;

	/**
	 * 上级物品分类名称
	 */
	private String parentName;

	/**
	 * 绩效系数
	 */
	@ApiModelProperty(value = "绩效系数")
	private BigDecimal gradeNum;

	/**
	 * 物品分类ID
	 */
	@ApiModelProperty("物品分类ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long typePreId;

	/**
	 * 是否批次化管理（字符串表示形式）
	 */
	@ApiModelProperty("是否批次化管理（字符串表示形式）")
	private String isSnDesc;

	/**
	 * 是否批次化管理（Boolean表示形式）
	 */
	@ApiModelProperty("是否批次化管理（Boolean表示形式）")
	private Boolean sn;

	/**
	 * 物品批属性
	 */
	@ApiModelProperty("物品批属性")
	private SkuLotVO skuLot;

	/**
	 * 物品批属性验证
	 */
	@ApiModelProperty("物品批属性验证")
	private SkuLotValVO skuLotVal;

	/**
	 * 存货类型描述
	 */
	@ApiModelProperty("存货类型描述")
	private String inventoryTypeDesc;

	/**
	 * 保质期日期类型
	 */
	@ApiModelProperty(value = "保质期日期类型")
	private String qualityDateTypeCd;

	/**
	 * 批属性名称
	 */
	@ApiModelProperty(value = "批属性名称")
	private String skuLotName;

	/**
	 * 批属性验证名称
	 */
	@ApiModelProperty(value = "批属性验证名称")
	private String skuLotValName;

	/**
	 * ABC字典值
	 */
	@ApiModelProperty(value = "ABC分类字典值")
	private String abcName;

	/**
	 * 存货类型
	 */
	@ApiModelProperty(value = "存货类型")
	private String skuStorageTypeName;
}
