package org.nodes.wms.dao.lendreturn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.output.LendReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.springblade.core.mp.base.BaseService;

/**
 * 借出归还记录 Dao接口
 */
public interface LogLendReturnDao extends BaseService<LogLendReturn> {

	/**
	 * 分页查询借出归还记录
	 */
	Page<LendReturnResponse> selectPage(IPage<LogLendReturn> page, LendReturnQuery lendReturnQuery);
}
