
package org.nodes.wms.core.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.UserOnline;

/**
 * 在线用户视图实体类
 *
 * @author pengwei
 * @since 2020-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserOnlineVO对象", description = "在线用户")
public class UserOnlineVO extends UserOnline {
	private static final long serialVersionUID = 1L;

}
