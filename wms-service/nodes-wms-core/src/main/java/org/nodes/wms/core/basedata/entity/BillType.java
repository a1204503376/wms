package org.nodes.wms.core.basedata.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;

/**
 * 单据类型实体类
 *
 * @author NodeX
 * @since 2019-12-24
 */
@Data
@TableName("wms_bill_type")
@ApiModel(value = "BillType对象", description = "单据类型")
public class BillType extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 单据类型ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "单据类型ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billTypeId;
	/**
	 * 单据类型编号
	 */
	@ApiModelProperty(value = "单据类型编号")
	private String billTypeCd;
	/**
	 * 单据类型名称
	 */
	@ApiModelProperty(value = "单据类型名称")
	private String billTypeName;
	/**
	 * 类型模式
	 */
	@ApiModelProperty(value = "类型模式")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String ioType;

	/**
	 * 租户ID
	 */
	@ApiModelProperty("租户ID")
	private String tenantId;
}
