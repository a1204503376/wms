package org.nodes.wms.core.outstock.so.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.outstock.so.service.impl.ISwitchAllocStrategyMode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 生成拣货计划DTO实体对象
 * @since 2020-11-02
 */
@Data
public class CreatePickPlanDTO {

	/**
	 * 出库单主键ID集合
	 */
	@NotNull
	@ApiModelProperty("出库单主键ID集合")
	private List<Long> soBillIdList;

	/**
	 * 出库单明细ID集合
	 */
	@ApiModelProperty("出库单明细ID集合")
	private List<Long> soDetailIdList;

	/**
	 * 是否为波次分配
	 */
	@ApiModelProperty("是否为波次分配")
	private Boolean isWellen;

	/**
	 * 是否确认(系统参数 outstock_pickplan_prompt = 1 时有效)
	 */
	@ApiModelProperty("是否确认(系统参数 outstock_pickplan_prompt = 1 时有效)")
	private Boolean confirm;

	/**
	 * 分配方式
	 */
	@ApiModelProperty("分配方式")
	private CreatePickPlanByWellenDTO dto;
}
