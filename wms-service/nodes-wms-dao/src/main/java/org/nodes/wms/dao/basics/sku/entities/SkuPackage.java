
package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;

/**
 * 包装ID实体类
 *
 * @author NodeX
 * @since 2019-12-17
 */
@Data
@TableName("wms_sku_package")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuPackage对象", description = "包装ID")
public class SkuPackage extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 包装ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wsp_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "包装ID")
	private Long wspId;
	/**
	 * 包装名称
	 */
	@ApiModelProperty(value = "包装名称")
	private String wspName;
	/**
	 * 上线包装名称
	 */
	@ApiModelProperty(value = "上线包装名称")
	private String onlinePackage;
	/**
	 * 上线包装类型
	 */
	@ApiModelProperty(value = "上线包装类型")
	private String packageType;
	/**
	 * 托盘每层箱数
	 */
	@ApiModelProperty(value = "托盘每层箱数")
	private BigDecimal palletBoxLevel;
	/**
	 * 每托盘层数
	 */
	@ApiModelProperty(value = "每托盘层数")
	private BigDecimal palletLevel;
	/**
	 * 每托盘重量
	 */
	@ApiModelProperty(value = "每托盘重量")
	private BigDecimal lpnWeight;
	/**
	 * 每托盘高度
	 */
	@ApiModelProperty(value = "每托盘高度")
	private BigDecimal lpnHeight;
	/**
	 * 每托盘长度
	 */
	@ApiModelProperty(value = "每托盘长度")
	private BigDecimal lpnLength;
	/**
	 * 每托盘宽度
	 */
	@ApiModelProperty(value = "每托盘宽度")
	private BigDecimal lpnWidth;
	/**
	 * 包装规格
	 */
	@ApiModelProperty("包装规格")
	private String spec;
}
