package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * 补货记录实体类
 *
 * @author NodeX
 * @since 2020-04-27
 */
@Data
@TableName("st_relenishment_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RelenishmentLog对象", description = "补货策略执行记录")
public class RelenishmentLog extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 记录id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "记录id")
	private Long id;
	/**
	 * 补货策略ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "补货策略ID")
	private Long ssiId;
	/**
	 * 补货策略编码
	 */
	@ApiModelProperty(value = "补货策略编码")
	private String ssiCode;
	/**
	 * 补货策略名称
	 */
	@ApiModelProperty(value = "补货策略名称")
	private String ssiName;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 处理顺序
	 */
	@ApiModelProperty(value = "处理顺序")
	private Integer ssidProcOrder;
	/**
	 * 补货计算代码
	 */
	@ApiModelProperty(value = "补货计算代码")
	private Integer instockFunction;
	/**
	 * 执行内容
	 */
	@ApiModelProperty(value = "执行内容")
	private String aplProcLog;
	/**
	 * 执行顺序
	 */
	@ApiModelProperty(value = "执行顺序")
	private Integer executeOrder;
	/**
	 * 是否成功
	 */
	@ApiModelProperty(value = "是否成功")
	private Integer isSuccess;

}
