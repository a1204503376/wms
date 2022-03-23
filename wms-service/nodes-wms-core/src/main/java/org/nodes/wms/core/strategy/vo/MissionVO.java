package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.strategy.entity.Mission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MissionVO对象", description = "MissionVO对象")
public class MissionVO extends Mission {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务类型分类；（字符串表示形式）
	 */
	@ApiModelProperty(value = "任务类型分类；（字符串表示形式）")
	private String missionTypeName;

	/**
	 * 推送方式 （字符串表示形式）
	 */
	@ApiModelProperty(value = "推送方式 （字符串表示形式）")
	private String pushWayName;

	/**
	 * 计算方式 （字符串表示形式）
	 */
	@ApiModelProperty(value = "计算方式 （字符串表示形式）")
	private String countWayName;

	/**
	 * 超过单位数量是否分割 （字符串表示形式）
	 */
	@ApiModelProperty(value = "超过单位数量是否分割 （字符串表示形式）")
	private String isExceedcountSegmentName;

}
