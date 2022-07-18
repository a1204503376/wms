package org.nodes.wms.dao.instock.receiveLog.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 收货清点记录id集合请求类
 **/
@Data
public class ReceiveLogIdListRequest implements Serializable {

	private static final long serialVersionUID = 2886469150998110206L;

	/**
	 * 收货记录id
	 */
	@NotNull(message = "参数 idList 为空")
	private List<Long> idList;
}
