
package org.nodes.wms.core.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 在线用户实体类
 *
 * @author pengwei
 * @since 2020-04-01
 */
@Data
@TableName("sys_user_online")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserOnline对象", description = "在线用户")
public class UserOnline extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 在线用户ID
	 */
	@ApiModelProperty(value = "在线用户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "suo_id", type = IdType.ASSIGN_ID)
	private Long suoId;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;
	/**
	 * 用户编码
	 */
	@ApiModelProperty(value = "用户编码")
	private String userCode;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 用户类型
	 */
	@ApiModelProperty(value = "用户类型")
	private Integer userType;
	/**
	 * 联系电话
	 */
	@ApiModelProperty(value = "联系电话")
	private String mobilePhone;
	/**
	 * 域用户
	 */
	@ApiModelProperty(value = "域用户")
	private String domainUser;
	/**
	 * 在线终端
	 */
	@ApiModelProperty(value = "在线终端")
	private String onlineTerminal;
	/**
	 * token
	 */
	@ApiModelProperty(value = "token")
	private String token;
	/**
	 * IP地址
	 */
	@ApiModelProperty(value = "IP地址")
	private String ipAddress;
	/**
	 * MAC地址
	 */
	@ApiModelProperty(value = "MAC地址")
	private String macAddress;
	/**
	 * 最后登记时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "最后登记时间")
	private LocalDateTime lastLoginTime;
	/**
	 * 登记状态
	 */
	@ApiModelProperty(value = "登记状态")
	private Integer loginStatus;


}
