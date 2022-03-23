package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.ContainerLog;

import java.math.BigDecimal;

/**
 * 清点记录视图实体类
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ContainerLogVO对象", description = "清点记录")
public class ContainerLogVO extends ContainerLog {
	private static final long serialVersionUID = 1L;

	/**
	 * 包装层级描述
	 */
	@ApiModelProperty(value = "包装层级描述")
	private String skuLevelDesc;

	/**
	 * 清点状态描述
	 */
	@ApiModelProperty(value = "清点状态描述")
	private String aclStatusDesc;

	/**
	 * 复核状态描述
	 */
	@ApiModelProperty(value = "复核状态描述")
	private String checkStateDesc;

	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String wsuName;

	/**
	 * 物品待拣计划总数
	 */
	@ApiModelProperty("物品待拣计划总数")
	private BigDecimal totalPlanQty;

	/**
	 * 物品已拣总数
	 */
	@ApiModelProperty("物品待拣计划总数")
	private BigDecimal totalScanQty;

	/**
	 * 用户名称
	 */
	@ApiModelProperty("用户名称")
	private String userName;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
}
