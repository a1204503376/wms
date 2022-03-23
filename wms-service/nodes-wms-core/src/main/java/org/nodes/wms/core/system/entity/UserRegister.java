
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
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 实体类
 *
 * @author NodeX
 * @since 2020-04-01
 */
@Data
@TableName("log_user_register")
@ApiModel(value = "UserRegister对象", description = "UserRegister对象")
public class UserRegister extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户在线ID
	 */
	@ApiModelProperty(value = "用户在线ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "lur_Id", type = IdType.ASSIGN_ID)
	private Long lurId;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
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
	 * ip地址
	 */
	@ApiModelProperty(value = "ip地址")
	private String ipAddress;
	/**
	 * mac地址
	 */
	@ApiModelProperty(value = "mac地址")
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
