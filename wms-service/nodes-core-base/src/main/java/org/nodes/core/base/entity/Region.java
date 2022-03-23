package org.nodes.core.base.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;

/**
 * 区域表实体类
 *
 * @author NodeX
 * @since 2022-01-21
 */
@Data
@TableName("blade_region")
@ApiModel(value = "行政区域对象", description = "行政区域对象")
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "区域编号")
	@TableId(value = "code", type = IdType.ASSIGN_ID)
	private String code;

	/**
	 * 父区域编号
	 */
	@ApiModelProperty(value = "父区域编号")
	private String parentCode;

	/**
	 *祖区域编号
	 */
	@ApiModelProperty(value = "祖区域编号")
	private String ancestors;

	/**
	 * 区划名称
	 */
	@ApiModelProperty(value = "区划名称")
	private String name;

	/**
	 * 省级区划编号
	 */
	@ApiModelProperty(value = "省级区划编号")
	private String provinceCode;

	/**
	 * 省级名称
	 */
	@ApiModelProperty(value = "省级名称")
	private String provinceName;

	/**
	 * 市级区划编号
	 */
	@ApiModelProperty(value = "市级区划编号")
	private String cityCode;

	/**
	 * 市级名称
	 */
	@ApiModelProperty(value = "市级名称")
	private String cityName;

	/**
	 * 区级区划编号
	 */
	@ApiModelProperty(value = "区级区划编号")
	private String districtCode;

	/**
	 * 区级名称
	 */
	@ApiModelProperty(value = "区级名称")
	private String districtName;

	/**
	 * 镇级区划编号
	 */
	@ApiModelProperty(value = "镇级区划编号")
	private String townCode;

	/**
	 * 镇级名称
	 */
	@ApiModelProperty(value = "镇级名称")
	private String townName;

	/**
	 * 村级区划编号
	 */
	@ApiModelProperty(value = "村级区划编号")
	private String villageCode;

	/**
	 * 村级名称
	 */
	@ApiModelProperty(value = "村级名称")
	private String villageName;

	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer regionLevel;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
