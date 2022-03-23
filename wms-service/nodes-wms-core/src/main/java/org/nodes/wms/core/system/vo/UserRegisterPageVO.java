
package org.nodes.wms.core.system.vo;

import io.swagger.annotations.ApiModel;
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
public class UserRegisterPageVO {
	private static final long serialVersionUID = 1L;
	private List<UserRegister>  userRegister;
	private UserOnline userOnline;
}
