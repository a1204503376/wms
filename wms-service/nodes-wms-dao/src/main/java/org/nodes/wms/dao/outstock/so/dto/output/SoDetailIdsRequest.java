package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 发货单明细id集合请求类
 **/
@Data
public class SoDetailIdsRequest implements Serializable {

	private static final long serialVersionUID = -3894024019561829011L;

	/**
	 * 发货单明细id
	 */
	@NotNull(message = "参数 soDetailIdList 为空")
	private List<Long> soDetailIdList;
}
