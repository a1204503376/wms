
package org.nodes.wms.core.instock.asn.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

/**
 * 上架策略物品明细实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@TableName("st_instock_sku_detail")
@ApiModel(value = "InstockSkuDetail对象", description = "InstockSkuDetail对象")
public class InstockSkuDetail extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * ABC分类
	 */
	@ApiModelProperty("ABC分类")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer abc;

	/**
	 * 物品备用字段(存货类型)
	 */
	@ApiModelProperty("物品备用字段(存货类型)")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer inventoryType;
	/**
	 * 上架策略物品明细ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "上架策略物品明细ID")
	@TableId(value = "strd_id", type = IdType.ASSIGN_ID)

	private Long strdId;
	/**
	 * 上架策略明细ID
	 */
	@ApiModelProperty(value = "上架策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssidId;
	/**
	 * 上架物品
	 */
	@ApiModelProperty(value = "上架物品")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long strdSkuId;
	/**
	 * 货位类型
	 */
	@ApiModelProperty(value = "货位类型")
	private int locType;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	 * 低储
	 */
	@ApiModelProperty(value = "低储")
	private Integer lowStorage;
	/**
	 * 高储
	 */
	@ApiModelProperty(value = "高储")
	private Integer highStorage;
	/**
	 * 货位个数
	 */
	@ApiModelProperty(value = "货位个数")
	private Integer locCount;

}
