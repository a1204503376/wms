package org.nodes.wms.dao.basics.skuType.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;

/**
 * 物品分类实体类
 **/

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("wms_sku_type")
public class SkuType extends TenantEntity {

	private static final long serialVersionUID = -3599575852441635890L;
	/**
	 * 物品分类id
	 */
	@TableId(value = "sku_type_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuTypeId;

	/**
	 * 上位物品分类id
	 */
	private Long typePreId;

	/**
	 * 物品分类名称
	 */
	private String typeName;

	/**
	 * 物品分类编码
	 */
	private String typeCode;

	/**
	 * 货主id
	 */
	private Long woId;

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

	/**
	 * 数据类型
	 */
	private Integer dataType;

	/**
	 * 排序
	 */
	private Integer sort;
}
