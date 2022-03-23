package org.nodes.wms.core.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 功能计数	实体类
 *
 * @author NodeX
 * @since 2021-07-23
 */
@Data
@TableName("pub_function_count")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FunctionCount对象", description = "功能计数	")
public class FunctionCount extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 功能方法
	 */
	@ApiModelProperty(value = "功能方法")
	private String method;
	/**
	 * 功能名字
	 */
	@ApiModelProperty(value = "功能名字")
	private String name;
	/**
	 * 执行次数
	 */
	@ApiModelProperty(value = "执行次数")
	private Integer count;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
}
