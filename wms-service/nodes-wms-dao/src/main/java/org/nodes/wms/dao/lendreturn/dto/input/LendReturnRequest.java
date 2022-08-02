package org.nodes.wms.dao.lendreturn.dto.input;

import lombok.Data;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;

import java.util.List;

/**
 * 借出归还请求对象
 */
@Data
public class LendReturnRequest {

	/**
	 * 借出归还类型
	 */
	private LendReturnTypeEnum type;

	/**
	 * 单据类型
	 */
	private String billTypeCd;

	/**
	 * 借出归还记录集合
	 */
	private List<LogLendReturnRequest> logLendReturnRequestList;
}
