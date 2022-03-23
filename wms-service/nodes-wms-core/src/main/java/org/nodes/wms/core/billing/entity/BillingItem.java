package org.nodes.wms.core.billing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 计费项目实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingItem对象", description = "计费项目")
public class BillingItem extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 编码
	 */
	@ApiModelProperty(value = "编码")
	private String code;
	/**
	 * 项目
	 */
	@ApiModelProperty(value = "项目")
	private String name;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
