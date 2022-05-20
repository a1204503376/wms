package org.nodes.wms.core.basedata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.nodes.core.tool.validation.Excel;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author wanglei
 * @program 物品分类实体
 * @description 物品分类实体
 * @create 20191128
 */
@Data
@TableName("wms_sku_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "物品分类对象", description = "物品分类对象")
public class SkuType extends TenantEntity {

	/**
	 * 物品分类ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品分类Id")
	@TableId(value = "sku_type_id", type = IdType.ASSIGN_ID)
	private Long skuTypeId;

	/**
	 * 上位物品分类ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "上位物品分类ID")
	private Long typePreId;
	/**
	 * 上位物品分类名称
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "上位物品分类名称")
	private String parentName;
	/**
	 * 物品分类名称
	 */
	@ApiModelProperty(value = "物品分类名称")
	@Length(max = 50, message = "物品分类名称最大长度为50位")
	@NotBlank(message = "物品分类名称不能为空！",groups = {Excel.class})
	private String typeName;
	/**
	 * 物品分类编码
	 */
	@ApiModelProperty(value = "物品分类编码")
	@Length(max = 50, message = "物品分类编码最大长度为50位")
	@NotBlank(message = "物品分类编码不能为空！",groups = {Excel.class})
	private String typeCode;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	private Long woId;
	/**
	 * 绩效系数
	 */
	@ApiModelProperty(value = "绩效系数")
	private BigDecimal gradeNum;
	/**
	 * 物品分类路径
	 */
	@ApiModelProperty(value = "物品分类路径")
	private String typePath;
	/**
	 * 物品分类备注
	 */
	@ApiModelProperty(value = "物品分类备注")
	@Length(max = 300, message = "备注最大长度为300位")
	private String typeRemark;
}

