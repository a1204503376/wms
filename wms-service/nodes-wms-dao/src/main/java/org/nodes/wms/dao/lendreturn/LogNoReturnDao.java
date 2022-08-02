package org.nodes.wms.dao.lendreturn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.input.LogLendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;
import org.springblade.core.mp.base.BaseService;

/**
 * 未归还日志 Dao接口
 */
public interface LogNoReturnDao extends BaseService<LogNoReturn> {

	/**
	 * 获取相同物品存在的借出记录
	 */
	LogNoReturn sameSku(LogLendReturnRequest logLendReturnRequest);

	/**
	 * 分页查询未归还记录
	 */
    Page<NoReturnResponse> selectPage(Page<LogNoReturn> page, LendReturnQuery lendReturnQuery);
}
