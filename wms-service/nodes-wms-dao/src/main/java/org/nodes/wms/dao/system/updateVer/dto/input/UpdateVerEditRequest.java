package org.nodes.wms.dao.system.updateVer.dto.input;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统版本编辑dto请求对象
 **/
@Data
public class UpdateVerEditRequest implements Serializable {

	private static final long serialVersionUID = -736467611920814243L;

	/**
	 * 系统版本id
	 */
	@NotNull(message = "系统版本id不能为空")
	private Long suvId;

	/**
	 * 版本号编码
	 */
	@NotNull(message = "版本号编码不能为空")
	private Integer verNum;

	/**
	 *版本号名称
	 */
	@NotNull(message = "版本号名称不能为空")
	private String verName;

	/**
	 * 更新地址
	 */
	private String updateUrl;

	/**
	 * 系统类型
	 */
	private String moduleType;
}
