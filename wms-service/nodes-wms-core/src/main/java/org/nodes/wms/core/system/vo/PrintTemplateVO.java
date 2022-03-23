package org.nodes.wms.core.system.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.PrintTemplate;

/**
 * 打印模板视图实体类
 *
 * @author NodeX
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PrintTemplateVO对象", description = "打印模板")
public class PrintTemplateVO extends PrintTemplate {
	private static final long serialVersionUID = 1L;

	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 模板类别描述
	 */
	@ApiModelProperty("模板类别描述")
	private String sptTypeDesc;

	/**
	 * 客户ID
	 */
	@ApiModelProperty("客户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long peId;

	/**
	 * 客户编码
	 */
	@ApiModelProperty("客户编码")
	private String enterpriseCode;

	/**
	 * 客户名称
	 */
	@ApiModelProperty("客户名称")
	private String enterpriseName;
}
