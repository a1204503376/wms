package org.nodes.wms.core.system.entity;

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
 * 任务角色关联表实体类
 *
 * @author NodeX
 * @since 2020-06-08
 */
@Data
@TableName("tsk_role_task")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RoleTask对象", description = "任务角色关联表")
public class RoleTask extends BaseEntity implements Serializable  {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private Long trtId;
	/**
	 * 角色id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "角色id")
	private Long roleId;
	/**
	 * 任务类型编码
	 */
	@ApiModelProperty(value = "任务类型编码")
	private Integer taskTypeCd;


}
