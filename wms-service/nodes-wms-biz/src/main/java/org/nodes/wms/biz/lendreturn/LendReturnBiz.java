package org.nodes.wms.biz.lendreturn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.output.LendReturnResponse;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;

/**
 * 借出归还 业务类
 */
public interface LendReturnBiz {

	/**
	 * 保存借出归还记录
	 */
	void saveLog(LendReturnRequest lendReturnRequest);

	/**
	 * 分页查询借出归还记录
	 */
	Page<LendReturnResponse> pageLendReturn(Page<LogLendReturn> page, LendReturnQuery lendReturnQuery);

	/**
	 * 分页查询未归还记录
	 */
	Page<NoReturnResponse> pageNoReturn(Page<LogNoReturn> page, LendReturnQuery lendReturnQuery);
}
