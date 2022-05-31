package org.nodes.wms.pdaDao.User.dto.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改用户签到状态接收前端参数request
 */
@Data
public class EditUserLoginStatusRequest {
	/**
	 * 登记状态
	 */
	private Integer loginStatus;

	/**
	 * token
	 */
	private String token;
}
