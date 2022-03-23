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
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.common.entity.AttributeBase;

import javax.validation.constraints.NotBlank;

/**
 * 货主管理 实体类
 *
 * @author zhongls
 * @since 2019-12-05
 */
@Data
@TableName("wms_owner")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Owner对象", description = "Owner对象")
public class Owner extends AttributeBase {

	private static final long serialVersionUID = 1L;
	public static final int DATA_TYPE = 3;

	/**
	 * 货主ID
	 */
	@ApiModelProperty(value = "货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wo_id", type = IdType.ASSIGN_ID)
	private Long woId;
	/**
	 * 货主编码
	 */
	@ApiModelProperty(value = "货主编码")
	@NotBlank(message = "货主编码不能为空！",groups = {Excel.class})
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;
	/**
	 * 货主简称
	 */
	@ApiModelProperty(value = "货主简称")
	private String ownerNameS;
	/**
	 * 城市
	 */
	@ApiModelProperty(value = "城市")
	private String ownerCity;
	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	private String ownerProvince;
	/**
	 * 邮政编码
	 */
	@ApiModelProperty(value = "邮政编码")
	private String ownerZipCode;
	/**
	 * 国家
	 */
	@ApiModelProperty(value = "国家")
	private String ownerCountry;
}
