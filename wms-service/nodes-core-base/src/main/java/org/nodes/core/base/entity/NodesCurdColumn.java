package org.nodes.core.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 列显隐表实体类
 *
 * @author wangYunan
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "NodesCurdColumn对象", description = "列显隐表")
public class NodesCurdColumn extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 菜单id
	 */
	@ApiModelProperty(value = "菜单id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long menuId;
	/**
	 * 父级id
	 */
	@ApiModelProperty(value = "父级id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;
	/**
	 * 字段名
	 */
	@ApiModelProperty(value = "字段名")
	private String prop;
	/**
	 * 列名
	 */
	@ApiModelProperty(value = "列名")
	private String label;
	/**
	 * 别名
	 */
	@ApiModelProperty(value = "别名")
	private String aliasName;
	/**
	 * 是否隐藏0:是1:否
	 */
	@ApiModelProperty(value = "是否隐藏0:是1:否")
	private String hide;
	/**
	 * 是否冻结0:是1:否
	 */
	@ApiModelProperty(value = "是否冻结0:是1:否")
	private String fixed;
	/**
	 * 宽度
	 */
	@ApiModelProperty(value = "宽度")
	private Integer width;
	/**
	 * 备用字段1
	 */
	@ApiModelProperty(value = "备用字段1")
	private String attribute1;
	/**
	 * 备用字段2
	 */
	@ApiModelProperty(value = "备用字段2")
	private String attribute2;


}
