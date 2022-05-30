
package org.nodes.wms.core.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类
 *
 * @author NodeX
 * @since 2020-02-20
 */
@Data
@TableName("sys_update_ver")
@ApiModel(value = "UpdateVer对象", description = "UpdateVer对象")
public class UpdateVer implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统版本ID
	 */
	@ApiModelProperty(value = "系统版本ID")
	private Long suvId;
	/**
	 * 版本号数值
	 */
	@ApiModelProperty(value = "版本号数值")
	private Integer verNum;
	/**
	 * 版本号名称
	 */
	@ApiModelProperty(value = "版本号名称")
	private String verName;
	/**
	 * 模块类型
	 */
	@ApiModelProperty(value = "模块类型")
	private String moduleType;
	/**
	 * 更新地址
	 */
	@ApiModelProperty(value = "更新地址")
	private String updateUrl;
	/**
	 * 更新备注
	 */
	@ApiModelProperty(value = "更新备注")
	private String updateRemark;
	/**
	 * 更新类型
	 */
	@ApiModelProperty(value = "更新类型")
	private String updateType;
	/**
	 * 是否启用
	 */
	@ApiModelProperty(value = "是否启用")
	private Integer status;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime createTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createUser;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Long updateUser;
	/**
	 * 删除标志
	 */
	@ApiModelProperty(value = "删除标志")
	private Integer isDeleted;


}
