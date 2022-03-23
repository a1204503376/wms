
package org.nodes.wms.core.warehouse.entity;

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
 * 工作区实体类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Data
@TableName("wms_work_area")
@ApiModel(value = "WorkArea对象", description = "WorkArea对象")
public class WorkArea extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	* 工作区id
	*/
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "工作区id")
	@TableId(value = "wwa_id", type = IdType.ASSIGN_ID)
		private Long wwaId;
	/**
	* 库房ID
	*/
	@JsonSerialize(using = ToStringSerializer.class)
		@ApiModelProperty(value = "库房ID")
		private Long whId;
	/**
	* 工作区名称
	*/
		@ApiModelProperty(value = "工作区名称")
		private String wwaName;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String remark;
	/**
	* 同步状态 0：未同步  1：同步完成
	*/
		@ApiModelProperty(value = "同步状态")
		private Integer syncState;

}
