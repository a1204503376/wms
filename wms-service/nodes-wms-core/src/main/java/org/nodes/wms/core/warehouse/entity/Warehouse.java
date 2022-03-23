package org.nodes.wms.core.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.nodes.wms.core.common.entity.AttributeBase;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 仓库实体类
 *
 * @author Wangjw
 * @since 2019-12-06
 */
@Data
@TableName("wms_warehouse")
@ApiModel(value = "Warehouse对象", description = "仓库")
public class Warehouse extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int DATA_TYPE = 2;

	/**
	 * 仓库ID
	 */
	@TableId(value = "wh_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "仓库ID")
	private Long whId;
	/**
	 * 仓库编码
	 */
	@NotNull(message = "仓库编码不能为空")
	@Length(min = 1, max = 30, message = "最小长度为1位，最大长度为30位")
	@ApiModelProperty(value = "仓库编码")
	private String whCode;
	/**
	 * 仓库名称
	 */
	@NotNull(message = "仓库名称不能为空")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	@ApiModelProperty(value = "仓库名称")
	private String whName;
	/**
	 * 城市
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	@ApiModelProperty(value = "城市")
	private String city;
	/**
	 * 省
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	@ApiModelProperty(value = "省")
	private String province;
	/**
	 * 邮政编码
	 */
	@Length(max = 50, message = "字符长度不能超过50")
	@ApiModelProperty(value = "邮政编码")
	private String zipCode;
	/**
	 * 国家
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	@ApiModelProperty(value = "国家")
	private String country;
	/**
	 * 备注
	 */
	@Length(max = 1000, message = "字符长度不能超过1000")
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 入库暂存区
	 */
	@ApiModelProperty(value = "入库暂存区")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stage;
	/**
	 * 出库暂存区
	 */
	@ApiModelProperty(value = "出库暂存区")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pick;
	/**
	 * 包装暂存区
	 */
	@ApiModelProperty(value = "包装暂存区")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pack;
	/**
	 * 移动暂存区
	 */
	@ApiModelProperty(value = "移动暂存区")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long move;

	/**
	 * 所属机构
	 */
	@ApiModelProperty("所属机构")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long deptId;
}
