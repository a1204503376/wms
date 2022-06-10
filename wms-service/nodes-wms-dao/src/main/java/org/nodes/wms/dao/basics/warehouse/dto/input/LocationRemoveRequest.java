package org.nodes.wms.dao.basics.warehouse.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 库位删除请求dto对象
 **/
@Data
public class LocationRemoveRequest implements Serializable {

	private static final long serialVersionUID = -5008024857150753781L;

	@NotNull(message = "库位id不能为空")
	@Size(min = 1,message = "库位id不能小于1个")
	private List<Long> idList;
}
