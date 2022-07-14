package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 拣货记录日志id集合请求类
 **/
@Data
public class LogSoPickIdListRequest implements Serializable {

	private static final long serialVersionUID = -254108698805609158L;

	/**
	 * 拣货记录日志id
	 */
	@NotNull(message = "参数 lsopIdList 不能为空")
	private List<Long> lsopIdList;
}
