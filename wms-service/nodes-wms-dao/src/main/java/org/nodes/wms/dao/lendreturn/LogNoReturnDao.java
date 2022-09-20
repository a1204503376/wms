package org.nodes.wms.dao.lendreturn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.input.LogLendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

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

	/**
	 * 根据Query中的条件查询未归还记录
	 *
	 * @param lendReturnQuery 查询条件
	 * @return 未归还记录
	 */
	List<LogNoReturn> listByQuery(LendReturnQuery lendReturnQuery);

	/**
	 * 根据发货明细id删除未归还记录
	 *
	 * @param soDetailId 发货明细id
	 * @return true: 删除成功 false: 删除失败
	 */
    boolean deleteBySoDetailId(Long soDetailId);

	/**
	 * 根据发货
	 *
	 * @param noReturnIdList 为归还明细id
	 * @return true: 删除成功，false: 删除失败
	 */
	boolean deleteByIdList(List<Long> noReturnIdList);
}
