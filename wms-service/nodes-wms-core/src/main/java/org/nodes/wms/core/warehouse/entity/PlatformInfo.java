
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
import org.nodes.core.tool.validation.Add;
import org.nodes.core.tool.validation.Excel;
import org.nodes.core.tool.validation.Update;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotBlank;

/**
 * 月台实体类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Data
@TableName("wms_platform_info")
@ApiModel(value = "PlatformInfo对象", description = "PlatformInfo对象")
public class PlatformInfo extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 月台ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "月台ID")
	@TableId(value = "spi_id", type = IdType.ASSIGN_ID)
	private Long spiId;
	/**
	 * 月台分类
	 */
	@ApiModelProperty(value = "月台分类")
	private Integer platformType;
	/**
	 * 月台编号
	 */
	@ApiModelProperty(value = "月台编号")
	@Length(min = 1, max = 30, message = "最小长度为1位，最大长度为30位")
	@NotBlank(message = "月台编号不能为空！",groups = {Excel.class, Add.class, Update.class})
	private String platformCode;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 月台名称
	 */
	@ApiModelProperty(value = "月台名称")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	@NotBlank(message = "月台名称不能为空！",groups = {Excel.class, Add.class, Update.class})
	private String platformName;
	/**
	 * 月台容量
	 */
	@ApiModelProperty(value = "月台容量")
	private Integer platformCapacity;

}
