
package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.PickPlan;

/**
 * 拣货计划表视图实体类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PickPlanVO对象", description = "拣货计划表")
public class PickPlanVO extends PickPlan {
	private static final long serialVersionUID = 1L;

	/**
	 * 波次编号
	 */
	@ApiModelProperty(value = "波次编号")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenNo;

	/**
	 * 仓库名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;

	/**
	 * 包装名称
	 */
	@ApiModelProperty(value = "包装名称")
	private String wspName;

	@ApiModelProperty(value = "层级名称")
	private String skuLevelName;

	/**
	 * 单位名称
	 */
	@ApiModelProperty("单位名称")
	private String wsuName;

	/**
	 * 出库单编码
	 */
	@ApiModelProperty("出库单编码")
	private String soBillNo;
}
