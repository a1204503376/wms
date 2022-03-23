
package org.nodes.wms.core.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.system.entity.UserOnline;
import org.nodes.wms.core.system.entity.UserRegister;

import java.util.List;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2020-04-01
 */
@Data
@ApiModel(value = "UserRegisterVO对象", description = "UserRegisterVO对象")
public class UserRegisterVO {

	private static final long serialVersionUID = 1L;
	/**
	 * 当前人员签到列表
	 */
	@ApiModelProperty("当前人员签到列表")
	private List<UserRegister>  userRegister;
	/**
	 * 当前人员签到信息
	 */
	@ApiModelProperty("当前人员签到信息")
	private UserOnline userOnline;
	@ApiModelProperty(value = "最后登记天")
	private int lastLoginDay;
}
