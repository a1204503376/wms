package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 实体类
 *
 * @author NodeX
 * @since 2021-05-24
 */
@Data
@TableName("st_mission")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Mission对象", description = "Mission对象")
public class Mission extends BaseEntity implements Serializable  {

	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "物品ID")
	private Long id;
	/**
	* 优先级
	*/
		@ApiModelProperty(value = "优先级")
		private Integer missionLevel;
	/**
	* 任务类型分类；对应字典code=task_type
	*/
		@ApiModelProperty(value = "任务类型分类；对应字典code=task_type")
		private Integer missionType;
	/**
	* 推送方式 0:任务最少优先推送1:单位数量最少优先推送2:效率最高优先推送3:不推送
	*/
		@ApiModelProperty(value = "推送方式 0:任务最少优先推送1:单位数量最少优先推送2:效率最高优先推送3:不推送")
		private Integer pushWay;
	/**
	* 是否考虑综合任务 0:是1:否
	*/
		@ApiModelProperty(value = "是否考虑综合任务 1:是0:否")
		private Integer isSynthesizeTask;
	/**
	* 超过单位数量是否分割 0:是1:否
	*/
		@ApiModelProperty(value = "超过单位数量是否分割 1:是0:否")
		private Integer isExceedcountSegment;
	/**
	* 计算方式 0:单位数量按任务汇总1:单位数量按任务行汇总2:单位数量按物品汇总
	*/
		@ApiModelProperty(value = "计算方式 0:单位数量按任务汇总1:单位数量按任务行汇总2:单位数量按物品汇总")
		private Integer countWay;
	/**
	* 协同任务数量下限
	*/
		@ApiModelProperty(value = "协同任务数量下限")
		private Integer missionSynergyFloor;
	/**
	* 独占任务数量上限
	*/
		@ApiModelProperty(value = "独占任务数量上限")
		private Integer missionAloneUpper;
	/**
	* 任务分割数量上限
	*/
		@ApiModelProperty(value = "任务分割数量上限")
		private Integer missionSegmentUpper;

	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String remark;


}
