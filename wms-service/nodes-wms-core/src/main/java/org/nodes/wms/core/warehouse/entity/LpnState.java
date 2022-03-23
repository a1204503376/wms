package org.nodes.wms.core.warehouse.entity;

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
 * 容器状态表实体类
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Data
@TableName("wms_lpn_state")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LpnState对象", description = "容器状态表")
public class LpnState extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键id")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 容器状态
	 */
	@ApiModelProperty(value = "容器状态")
	private Integer lpnState;
	/**
	 * 上级id
	 */
	@ApiModelProperty(value = "上级id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;
	/**
	 * 上级编码
	 */
	@ApiModelProperty(value = "上级编码")
	private String parentCode;


}
