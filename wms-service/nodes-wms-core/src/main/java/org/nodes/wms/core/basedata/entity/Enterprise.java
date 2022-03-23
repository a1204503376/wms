
package org.nodes.wms.core.basedata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.nodes.core.tool.validation.*;
import org.nodes.wms.core.common.entity.AttributeBase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 来往企业实体类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Data
@TableName("pub_enterprise")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Enterprise对象", description = "来往企业")
public class Enterprise extends AttributeBase {

	private static final long serialVersionUID = 1L;
	public static final int DATA_TYPE = 1;
	public static final String TYPE_KEY = "enterprise_type";

	/**
	 * 企业ID
	 */
	@TableId(value = "pe_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "企业ID")
	private Long peId;
	/**
	 * 企业编码
	 */
	@ApiModelProperty(value = "企业编码", required = true)
	@NotBlank(message = "企业编码不能为空!",groups = {Excel.class, Add.class, Update.class})
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	private String enterpriseCode;
	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称", required = true)
	@NotNull(message = "企业名称不能为空")
	@Length(min = 1, max = 300, message = "最小长度为1位，最大长度为300位")
	private String enterpriseName;
	/**
	 * 企业简称
	 */
	@ApiModelProperty(value = "企业简称")
	@Length(max = 100, message = "最大长度为100位")
	private String enterpriseNameS;
	/**
	 * 企业类型
	 */
	@ApiModelProperty(value = "企业类型")
	@NotBlank(message = "企业类型不能为空!",groups = {GroupA.class,Excel.class,Add.class,Update.class})
	private String enterpriseType;
	/**
	 * 城市
	 */
	@ApiModelProperty(value = "城市")
	@Length(max = 200, message = "字符长度不能超过200")
	private String city;
	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	@Length(max = 200, message = "字符长度不能超过200")
	private String province;
	/**
	 * 邮政编码
	 */
	@ApiModelProperty(value = "邮政编码")
	@Length(max = 50, message = "字符长度不能超过50")
	private String zipCode;
	/**
	 * 国家
	 */
	@ApiModelProperty(value = "国家")
	@Length(max = 200, message = "字符长度不能超过200")
	private String country;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@Length(max = 1000, message = "字符长度不能超过1000")
	private String remark;
	/**
	 * 货主ID
	 */
	@NotNull(message = "货主不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID", required = true)
	private Long woId;
}
