
package org.nodes.wms.core.basedata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 计量单位 实体类
 *
 * @author zhongls
 * @since 2019-12-05
 */
@Data
@TableName("wms_sku_um")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuUm对象", description = "SkuUm对象")
public class SkuUm extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 计量单位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "计量单位id")
	@TableId(value = "wsu_id", type = IdType.ASSIGN_ID)
	private Long wsuId;
	/**
	 * 计量单位编码
	 */
	@ApiModelProperty(value = "计量单位编码")
	@Length(min = 1, max = 20, message = "最小长度为1位，最大长度为20位")
	private String wsuCode;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	private String wsuName;

}
