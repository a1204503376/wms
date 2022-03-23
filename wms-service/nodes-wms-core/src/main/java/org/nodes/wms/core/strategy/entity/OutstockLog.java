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
 * 分配记录实体类
 *
 * @author pengwei
 * @since 2020-05-06
 */
@Data
@TableName("st_outstock_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockLog对象", description = "分配记录")
public class OutstockLog extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 记录id
	 */
	@ApiModelProperty(value = "记录id")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 波次编号
	 */
	@ApiModelProperty(value = "波次编号")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenNo;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String soBillNo;
	/**
	 * 分配策略ID
	 */
	@ApiModelProperty(value = "分配策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssoId;
	/**
	 * 分配策略编码
	 */
	@ApiModelProperty(value = "分配策略编码")
	private String ssoCode;
	/**
	 * 分配策略名称
	 */
	@ApiModelProperty(value = "分配策略名称")
	private String ssoName;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 处理顺序
	 */
	@ApiModelProperty(value = "处理顺序")
	private Integer ssodProcOrder;
	/**
	 * 分配计算代码
	 */
	@ApiModelProperty(value = "分配计算代码")
	private String outstockFunction;
	/**
	 * 执行内容
	 */
	@ApiModelProperty(value = "执行内容")
	private String solProcLog;
	/**
	 * 是否成功
	 */
	@ApiModelProperty(value = "是否成功")
	private Integer isSuccess;


}
