package org.nodes.wms.dao.basics.location.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 库位冻结、解冻请求类
 **/
@Data
public class LocationFreezeThawRequest implements Serializable {

	private static final long serialVersionUID = -1970286921943429383L;

	/**
	 * 库位id
	 */
	@NotNull(message = "库位idList不能为空")
	@Size(min = 1,message = "库位id不能少于1个")
	private List<Long> locIdList;
}
