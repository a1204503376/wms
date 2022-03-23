
package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 分配策略自定义属性设定 实体类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Data
@TableName("st_outstock_config_udf")
@ApiModel(value = "OutstockConfigUdf对象", description = "OutstockConfigUdf对象")
public class OutstockConfigUdf extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 分配策略自定义属性设定ID
	 */
	@ApiModelProperty(value = "分配策略自定义属性设定ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "ssocu_id", type = IdType.ASSIGN_ID)
	private Long ssocuId;
	/**
	 * 分配策略明细ID
	 */
	@ApiModelProperty(value = "分配策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssodId;
	/**
	 * 自定义属性索引
	 */
	@ApiModelProperty(value = "自定义属性索引")
	private Integer billUdfNumber;
	/**
	 * 自定义属性操作符
	 */
	@ApiModelProperty(value = "自定义属性操作符")
	private Integer billUdfOperation;
	/**
	 * 自定义属性设定值2
	 */
	@ApiModelProperty(value = "自定义属性设定值2")
	private String billUdfVal1;
	/**
	 * 自定义属性设定值2
	 */
	@ApiModelProperty(value = "自定义属性设定值2")
	private String billUdfVal2;
	/**
	 * 自定义属性设定值3
	 */
	@ApiModelProperty(value = "自定义属性设定值3")
	private String billUdfVal3;
	/**
	 * 是否启用
	 */
	@ApiModelProperty(value = "是否启用")
	private String isActive;
	/**
	 * 数据类型
	 */
	@ApiModelProperty(value = "数据类型")
	private String dataType;

}
